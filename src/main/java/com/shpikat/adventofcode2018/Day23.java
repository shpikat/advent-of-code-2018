package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day23 {

    private static final Pattern pattern = Pattern.compile("^pos=<(?<x>-?\\d+),(?<y>-?\\d+),(?<z>-?\\d+)>, r=(?<r>\\d+)$");

    static class Part1 {

        static int solve(final String input) {
            final Collection<Nanobot> nanobots = readInput(input);

            final Nanobot strongest = Collections.max(nanobots, Comparator.comparingInt(Nanobot::range));

            return (int) nanobots.stream()
                    .mapToInt(nanobot -> nanobot.coordinate().getManhattan(strongest.coordinate()))
                    .filter(range -> range <= strongest.range())
                    .count();
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final List<Nanobot> nanobots = readInput(input);


            int maxNanobot = 0;
            for (final Nanobot nanobot : nanobots) {
                maxNanobot = Math.max(maxNanobot, Math.abs(nanobot.coordinate().x()) + nanobot.range());
                maxNanobot = Math.max(maxNanobot, Math.abs(nanobot.coordinate().y()) + nanobot.range());
                maxNanobot = Math.max(maxNanobot, Math.abs(nanobot.coordinate().z()) + nanobot.range());
            }
            final int maxBox = Integer.highestOneBit(maxNanobot) << 1;

            // Preferring maximum nanobots count in the smallest box closest to the center
            final Queue<Box> queue = new PriorityQueue<>(Comparator
                    .comparingInt(Box::count).reversed()
                    .thenComparingInt(Box::size)
                    .thenComparingInt(box -> box.position().getManhattanToCenter()));
            queue.add(new Box(new Coordinate(-maxBox, -maxBox, -maxBox), maxBox << 1, nanobots.size()));

            while (!queue.isEmpty()) {
                final Box current = queue.poll();

                if (current.size() == 1) {
                    return current.position().getManhattanToCenter();
                }

                final int offset = current.size() >>> 1;
                for (int x = current.position().x(); x < current.position().x() + current.size(); x += offset) {
                    for (int y = current.position().y(); y < current.position().y() + current.size(); y += offset) {
                        for (int z = current.position().z(); z < current.position().z() + current.size(); z += offset) {
                            int count = 0;
                            for (final Nanobot nanobot : nanobots) {
                                final Coordinate c = nanobot.coordinate();
                                final int size = offset - 1;

                                // Use average of two absolute values to calculate Manhattan distance of the nanobot
                                // from the box: the distance is zero if within the box and grows when outside--for each
                                // of the coordinates.
                                final int distance = ((Math.abs(c.x() - x) + Math.abs(c.x() - (x + size)) - size)
                                        + (Math.abs(c.y() - y) + Math.abs(c.y() - (y + size)) - size)
                                        + (Math.abs(c.z() - z) + Math.abs(c.z() - (z + size)) - size)
                                ) / 2;

                                if (distance <= nanobot.range()) {
                                    count++;
                                }
                            }

                            queue.add(new Box(new Coordinate(x, y, z), offset, count));
                        }
                    }
                }
            }

            throw new IllegalArgumentException("Unable to find a solution");
        }

        private record Box(Coordinate position, int size, int count) {
        }
    }

    private static List<Nanobot> readInput(final String input) {
        final String[] lines = input.split("\n");
        final List<Nanobot> nanobots = new ArrayList<>(lines.length);
        for (final String line : lines) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                nanobots.add(new Nanobot(
                        new Coordinate(
                                Integer.parseInt(matcher.group("x")),
                                Integer.parseInt(matcher.group("y")),
                                Integer.parseInt(matcher.group("z"))
                        ),
                        Integer.parseInt(matcher.group("r"))
                ));
            } else {
                throw new IllegalArgumentException("Unable to parse line: " + line);
            }
        }
        return nanobots;
    }

    private record Coordinate(int x, int y, int z) {
        int getManhattanToCenter() {
            return Math.abs(x()) + Math.abs(y()) + Math.abs(z());
        }

        int getManhattan(final Coordinate that) {
            return Math.abs(that.x() - this.x()) + Math.abs(that.y() - this.y()) + Math.abs(that.z() - this.z());
        }
    }

    private record Nanobot(Coordinate coordinate, int range) {
    }
}
