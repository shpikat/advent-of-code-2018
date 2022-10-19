package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22 {

    static class Part1 {

        static int solve(final String input) {
            return Cave.fromInput(input).calculateRiskLevel();
        }
    }

    static class Part2 {
        static int solve(final String input) {
            return Cave.fromInput(input).findFastestPath();
        }
    }

    private static class Cave {

        private static final int MODULO = 20183;

        private static final int ROCKY = 0;
        private static final int WET = 1;
        private static final int NARROW = 2;
        private static final int NEITHER = ROCKY;
        private static final int TORCH = WET;
        private static final int CLIMBING_GEAR = NARROW;

        private static final int[] DX = new int[]{0, -1, 1, 0};
        private static final int[] DY = new int[]{-1, 0, 0, 1};

        private final List<List<Integer>> levels = new ArrayList<>();

        private final int depth;
        private final int targetX;
        private final int targetY;


        public Cave(final int depth, final int targetX, final int targetY) {
            this.depth = depth;
            this.targetX = targetX;
            this.targetY = targetY;
        }

        int calculateRiskLevel() {
            int risk = 0;
            for (int y = 0; y <= targetY; y++) {
                for (int x = 0; x <= targetX; x++) {
                    risk += getType(x, y);
                }
            }

            return risk;
        }

        int findFastestPath() {
            final State initial = new State(0, 0, TORCH);
            final Map<State, Integer> times = new HashMap<>(targetX * targetY * 3);
            times.put(initial, 0);
            final Queue<Entry> queue = new PriorityQueue<>(Comparator.comparingInt(e ->
                    e.minutes()
                            + Math.abs(e.state().x() - targetX)
                            + Math.abs(e.state().y() - targetY)));
            queue.add(new Entry(initial, 0));
            while (!queue.isEmpty()) {
                final Entry entry = queue.poll();
                final State current = entry.state();
                if (current.x() == targetX && current.y() == targetY && current.tool() == TORCH) {
                    return entry.minutes();
                }

                for (int t = 0; t < 3; t++) {
                    if (t != current.tool() && t != getType(current.x(), current.y())) {
                        final int time = entry.minutes() + 7;
                        times.compute(new State(current.x(), current.y(), t), (state, minutes) -> {
                            if (minutes == null || minutes > time) {
                                queue.add(new Entry(state, time));
                                minutes = time;
                            }
                            return minutes;
                        });
                    }
                }

                for (int i = 0; i < DX.length; i++) {
                    final int nextX = current.x() + DX[i];
                    final int nextY = current.y() + DY[i];
                    if (nextX >= 0 && nextY >= 0 && current.tool() != getType(nextX, nextY)) {
                        final int time = entry.minutes() + 1;
                        times.compute(new State(nextX, nextY, current.tool()), (state, minutes) -> {
                            if (minutes == null || minutes > time) {
                                queue.add(new Entry(state, time));
                                minutes = time;
                            }
                            return minutes;
                        });
                    }
                }
            }
            throw new IllegalStateException("Unable to reach the target");
        }

        private int getType(final int x, final int y) {
            return getLevel(x, y) % 3;
        }

        private int getLevel(final int x, final int y) {
            final int level;
            if (y < levels.size() && x < levels.get(y).size()) {
                level = levels.get(y).get(x);
            } else {
                final int sizeBeforeRowsAdded = levels.size();
                for (int i = sizeBeforeRowsAdded; i <= y; i++) {
                    levels.add(new ArrayList<>());
                }
                if (y == 0) {
                    final List<Integer> row = levels.get(y);
                    for (int i = row.size(); i < x; i++) {
                        row.add((i * 16807 + depth) % MODULO);
                    }
                    level = (x * 16807 + depth) % MODULO;
                } else if (x == 0) {
                    for (int i = sizeBeforeRowsAdded; i < y; i++) {
                        levels.get(i).add((i * 48271 + depth) % MODULO);
                    }
                    level = (y * 48271 + depth) % MODULO;
                } else if (x == targetX && y == targetY) {
                    // ensure invariants
                    getLevel(x, y - 1);
                    getLevel(x - 1, y);
                    level = depth;
                } else {
                    level = (getLevel(x, y - 1) * getLevel(x - 1, y) + depth) % MODULO;
                }
                levels.get(y).add(level);
            }
            return level;
        }

        static Cave fromInput(final String input) {
            final Pattern pattern = Pattern.compile("depth: (?<depth>\\d+)\\s+target: (?<x>\\d+),(?<y>\\d+)");
            final Matcher matcher = pattern.matcher(input.stripTrailing());
            if (matcher.matches()) {
                final int depth = Integer.parseInt(matcher.group("depth"));
                final int x = Integer.parseInt(matcher.group("x"));
                final int y = Integer.parseInt(matcher.group("y"));
                return new Cave(depth, x, y);
            } else {
                throw new IllegalArgumentException("Unable to parse input: " + input);
            }
        }

        private record State(int x, int y, int tool) {
        }

        private record Entry(State state, int minutes) {
        }
    }
}
