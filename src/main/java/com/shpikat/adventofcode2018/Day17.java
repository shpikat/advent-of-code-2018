package com.shpikat.adventofcode2018;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {

    private static final Pattern pattern = Pattern.compile("^([xy])=(\\d+), ([xy])=(\\d+)\\.\\.(\\d+)$");

    static class Part1 {
        static int solve(final String input) {
            final ReservoirResearch research = ReservoirResearch.fromInput(input);
            research.start();
//            System.out.println(research);
            return research.getAllWater();
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final ReservoirResearch research = ReservoirResearch.fromInput(input);
            research.start();
            return research.getRetainedWater();
        }
    }

    private static class ReservoirResearch {
        private static final int START_X = 500;
        private final Set<Coordinate> clay;
        private final Set<Coordinate> allWater;
        private final Set<Coordinate> retainedWater;
        private final int minY;
        private final int maxY;


        private ReservoirResearch(final Set<Coordinate> clay, final int minY, final int maxY) {
            this.clay = Collections.unmodifiableSet(clay);
            this.allWater = new HashSet<>();
            this.retainedWater = new HashSet<>();
            this.minY = minY;
            this.maxY = maxY;
        }

        void start() {
            pour(START_X, minY);
        }

        private void pour(final int x, final int y) {
            int bottom = y;
            while (flows(x, bottom)) {
                bottom++;
                if (bottom > maxY) {
                    return;
                }
            }

            // Guard against pouring several times into the same container
            if (allWater.contains(new Coordinate(x, bottom)) && overflows(x, bottom)) {
                return;
            }

            while (bottom > y) {
                final int flow = bottom - 1;
                int left = x - 1;
                while (stops(left, bottom) && flows(left, flow)) {
                    left--;
                }
                int right = x + 1;
                while (stops(right, bottom) && flows(right, flow)) {
                    right++;
                }

                final boolean hasLeftWall = stops(left, bottom) && clay.contains(new Coordinate(left, flow));
                final boolean hasRightWall = stops(right, bottom) && clay.contains(new Coordinate(right, flow));
                if (hasLeftWall && hasRightWall) {
                    retain(left, right, flow);
                } else {
                    if (!hasLeftWall) {
                        pour(left, flow);
                    }
                    if (!hasRightWall) {
                        pour(right, flow);
                    }
                    if (overflows(x, flow)) {
                        break;
                    }
                }
                bottom--;
            }
        }

        int getAllWater() {
            return allWater.size();
        }

        int getRetainedWater() {
            return retainedWater.size();
        }

        private boolean stops(final int x, final int y) {
            final Coordinate c = new Coordinate(x, y);
            return clay.contains(c) || allWater.contains(c);
        }

        private boolean flows(final int x, final int y) {
            final Coordinate c = new Coordinate(x, y);
            return !clay.contains(c) && allWater.add(c);
        }

        private boolean overflows(final int x, final int flow) {
            int left = x - 1;
            while (allWater.contains(new Coordinate(left, flow))) {
                left--;
            }
            int right = x + 1;
            while (allWater.contains(new Coordinate(right, flow))) {
                right++;
            }
            final boolean hasLeftWall = clay.contains(new Coordinate(left, flow));
            final boolean hasRightWall = clay.contains(new Coordinate(right, flow));
            final boolean contained = hasLeftWall && hasRightWall;
            if (contained) {
                retain(left, right, flow);
            }
            return !contained;
        }

        private void retain(int left, int right, int y) {
            for (int x = left + 1; x < right; x++) {
                retainedWater.add(new Coordinate(x, y));
            }
        }

        static ReservoirResearch fromInput(final String input) {
            final Set<Coordinate> clay = new HashSet<>();
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (final String line : input.split("\n")) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    final int a = Integer.parseInt(matcher.group(2));
                    final int b = Integer.parseInt(matcher.group(4));
                    final int c = Integer.parseInt(matcher.group(5));
                    if (matcher.group(1).equals("x") && matcher.group(3).equals("y")) {
                        for (int i = b; i <= c; i++) {
                            clay.add(new Coordinate(a, i));
                        }
                        minY = Math.min(minY, b);
                        maxY = Math.max(maxY, c);
                    } else {
                        for (int i = b; i <= c; i++) {
                            clay.add(new Coordinate(i, a));
                        }
                        minY = Math.min(minY, a);
                        maxY = Math.max(maxY, a);
                    }
                } else {
                    throw new IllegalArgumentException("Cannot parse line: " + line);
                }
            }
            return new ReservoirResearch(clay, minY, maxY);
        }

        private record Coordinate(int x, int y) {
        }

        @Override
        public String toString() {
            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            for (final Coordinate c : clay) {
                minX = Math.min(minX, c.x);
                maxX = Math.max(maxX, c.x);
            }

            final StringBuilder sb = new StringBuilder((maxX - minX + 10) * (maxY - minY + 2));
            sb.append(" ".repeat(Math.max(0, START_X - minX + 1))).append('+').append('\n');
            for (int y = minY; y <= maxY; y++) {
                for (int x = minX - 1; x <= maxX + 1; x++) {
                    final char ch;
                    final Coordinate c = new Coordinate(x, y);
                    if (clay.contains(c)) {
                        ch = '#';
                    } else if (retainedWater.contains(c)) {
                        ch = '~';
                    } else if (allWater.contains(c)) {
                        ch = '|';
                    } else {
                        ch = '.';
                    }
                    sb.append(ch);
                }
                sb.append('\n');
            }

            return sb.toString();
        }
    }
}
