package com.shpikat.adventofcode2018;

import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day15 {

    static class Part1 {
        static int solve(final String input) {
            final String[] lines = input.split("\n");
            final CombatSimulator combat = new CombatSimulator(lines);
            return combat.run();
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final String[] lines = input.split("\n");
            final CombatSimulator combat = new CombatSimulator(lines);
            return combat.runForNoElfLosses();
        }
    }

    private static class CombatSimulator {
        private static final byte EMPTY = '.';
        private static final byte ELF = 'E';
        private static final byte GOBLIN = 'G';
        private static final int STANDARD_ATTACK_POWER = 3;

        private static final Comparator<Coordinate> readingOrder = Comparator.comparingInt(Coordinate::y).thenComparingInt(Coordinate::x);
        private final String[] lines;

        CombatSimulator(final String[] lines) {
            this.lines = lines;
        }

        int run() {
            return run(STANDARD_ATTACK_POWER, false);
        }

        int runForNoElfLosses() {
            int elfAttackPower = STANDARD_ATTACK_POWER;
            int combatOutcome;
            do {
                combatOutcome = run(++elfAttackPower, true);
            } while (combatOutcome < 0);
            return combatOutcome;
        }

        private int run(final int elfAttackPower, final boolean isElfDefeatForbidden) {
            final byte[][] map = new byte[lines.length][];
            for (int i = 0; i < lines.length; i++) {
                map[i] = lines[i].getBytes(StandardCharsets.ISO_8859_1);
            }

            final Map<Coordinate, Unit> elves = new HashMap<>();
            final Map<Coordinate, Unit> goblins = new HashMap<>();
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (map[y][x] == ELF) {
                        elves.put(new Coordinate(x, y), new Unit());
                    } else if (map[y][x] == GOBLIN) {
                        goblins.put(new Coordinate(x, y), new Unit());
                    }
                }
            }

            final Queue<Coordinate> units = new PriorityQueue<>(readingOrder);
            int round = 0;
            while (true) {
                units.addAll(elves.keySet());
                units.addAll(goblins.keySet());

                while (!units.isEmpty()) {
                    final Coordinate current = units.poll();

                    final Map<Coordinate, Unit> friends;
                    final Map<Coordinate, Unit> enemies;
                    final int attackPower;
                    if (elves.containsKey(current)) {
                        friends = elves;
                        enemies = goblins;
                        attackPower = elfAttackPower;
                    } else if (goblins.containsKey(current)) {
                        friends = goblins;
                        enemies = elves;
                        attackPower = STANDARD_ATTACK_POWER;
                    } else {
                        // There is no unit to act by this time
                        continue;
                    }

                    if (enemies.isEmpty()) {
                        return round * friends.values().
                                stream().
                                mapToInt(Unit::getHp).
                                sum();
                    }

                    Coordinate position = current;
                    // Fast path when already within an attack range
                    boolean isAlreadyInRange = false;
                    for (final Coordinate c : current.getAdjacent()) {
                        if (enemies.containsKey(c)) {
                            isAlreadyInRange = true;
                            break;
                        }
                    }

                    if (!isAlreadyInRange) {
                        final Set<Coordinate> candidates = new HashSet<>();
                        for (final Coordinate enemy : enemies.keySet()) {
                            for (final Coordinate c : enemy.getAdjacent()) {
                                if (map[c.y()][c.x()] == EMPTY) {
                                    candidates.add(c);
                                }
                            }
                        }

                        final Map<Coordinate, Path> paths = new HashMap<>();
                        Coordinate closest = null;
                        if (!candidates.isEmpty()) {
                            final Queue<Entry> queue = new PriorityQueue<>(Comparator.comparingInt(Entry::steps));
                            queue.add(new Entry(current, 0));

                            int fewestSteps = Integer.MAX_VALUE;
                            while (!queue.isEmpty() && queue.peek().steps() <= fewestSteps) {
                                final Entry entry = queue.poll();
                                if (candidates.contains(entry.coordinate())) {
                                    if (entry.steps() < fewestSteps) {
                                        fewestSteps = entry.steps();
                                        closest = entry.coordinate();
                                    } else if (readingOrder.compare(entry.coordinate(), closest) < 0) {
                                        closest = entry.coordinate();
                                    }
                                }
                                for (final Coordinate c : entry.coordinate().getAdjacent()) {
                                    if (map[c.y()][c.x()] == EMPTY) {
                                        final int steps = entry.steps() + 1;
                                        final Path path = paths.computeIfAbsent(c, coordinate -> new Path());
                                        if (steps < path.steps) {
                                            path.steps = steps;
                                            path.tracks.clear();
                                            queue.add(new Entry(c, steps));
                                        }
                                        if (steps <= path.steps) {
                                            path.tracks.add(entry.coordinate());
                                        }
                                    }
                                }
                            }
                        }

                        if (closest == null) {
                            // There are no free spaces to move to
                            continue;
                        } else {
                            Coordinate next = new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE);

                            final Queue<Coordinate> queue = new ArrayDeque<>();
                            queue.add(closest);
                            while (!queue.isEmpty()) {
                                final Coordinate c = queue.poll();
                                final Set<Coordinate> tracks = paths.get(c).tracks;
                                if (tracks.contains(current)) {
                                    if (readingOrder.compare(c, next) < 0) {
                                        next = c;
                                    }
                                } else {
                                    queue.addAll(tracks);
                                }
                            }

                            map[next.y()][next.x()] = map[current.y()][current.x()];
                            map[current.y()][current.x()] = EMPTY;
                            friends.put(next, friends.remove(current));
                            position = next;
                        }
                    }

                    int lowestHp = Integer.MAX_VALUE;
                    Coordinate target = null;
                    for (final Coordinate c : position.getAdjacent()) {
                        final Unit enemy = enemies.get(c);
                        if (enemy != null && enemy.getHp() <= lowestHp) {
                            if (enemy.getHp() < lowestHp) {
                                lowestHp = enemy.getHp();
                                target = c;
                            } else if (readingOrder.compare(c, target) < 0) {
                                target = c;
                            }
                        }
                    }
                    if (target != null) {
                        final Unit enemy = enemies.get(target);
                        if (enemy.attack(attackPower)) {
                            if (isElfDefeatForbidden && map[target.y()][target.x()] == ELF) {
                                return -1;
                            }
                            enemies.remove(target);
                            map[target.y()][target.x()] = EMPTY;
                        }
                    }
                }
                ++round;
            }
        }

        private record Entry(Coordinate coordinate, int steps) {
        }

        private static class Path {
            final Set<Coordinate> tracks = new HashSet<>();
            int steps = Integer.MAX_VALUE;
        }

        private static class Unit {
            private int hp = 200;

            public int getHp() {
                return hp;
            }

            public boolean attack(final int attackPower) {
                hp -= attackPower;
                return hp <= 0;
            }
        }

        private record Coordinate(int x, int y) {

            Collection<Coordinate> getAdjacent() {
                return Arrays.asList(
                        new Coordinate(x(), y() - 1),
                        new Coordinate(x() - 1, y()),
                        new Coordinate(x() + 1, y()),
                        new Coordinate(x(), y() + 1)
                );
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }
        }
    }
}
