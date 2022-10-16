package com.shpikat.adventofcode2018;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Day18 {
    private static final char SYMBOL_GROUND = '.';
    private static final char SYMBOL_TREE = '|';
    private static final char SYMBOL_LUMBERYARD = '#';
    private static final byte GROUND = 0b00;
    private static final byte TREE = 0b01;
    private static final byte LUMBERYARD = 0b10;
    private static final int SIZE_BITS = 2;
    private static final int MASK = (1 << SIZE_BITS * 9) - 1;

    private static final byte[] patterns = calculatePatterns();

    static class Part1 {

        private static final int MINUTES = 10;

        static int solve(final String input) {
            final byte[][] area = readInput(input);

            for (int minute = 0; minute < MINUTES; minute++) {
                update(area);
            }

            return getResourceValue(area);
        }
    }

    static class Part2 {

        public static final int MINUTES = 1_000_000_000;

        static int solve(final String input) {
            final byte[][] area = readInput(input);

            // Observing the output, suggests the history will repeat itself.
            final Map<BitSet, Integer> historyIndex = new HashMap<>();
            final Map<Integer, BitSet> history = new HashMap<>();
            for (int minute = 0; minute < MINUTES; minute++) {
                final BitSet compacted = compact(area);
                final Integer previous = historyIndex.putIfAbsent(compacted, minute);
                if (previous == null) {
                    history.put(minute, compacted);
                    update(area);
                } else {
                    restore(history.get(previous + (MINUTES - previous) % (minute - previous)), area);
                    break;
                }
            }

            return getResourceValue(area);
        }
    }

    private static byte[][] readInput(final String input) {
        final String[] lines = input.split("\n");
        assert lines.length > 0;
        final byte[][] area = new byte[lines.length][];
        for (int y = 0; y < lines.length; y++) {
            area[y] = new byte[lines[y].length()];
            for (int x = 0; x < lines[y].length(); x++) {
                final char symbol = lines[y].charAt(x);
                area[y][x] = switch (symbol) {
                    case SYMBOL_GROUND -> GROUND;
                    case SYMBOL_TREE -> TREE;
                    case SYMBOL_LUMBERYARD -> LUMBERYARD;
                    default -> throw new IllegalArgumentException("Unexpected symbol: " + symbol);
                };
            }
        }
        return area;
    }

    private static void update(final byte[][] area) {
        final int width = area[0].length;

        // Rolling updates, allowing almost in-place update of the area.
        byte[] delayed = new byte[width];
        byte[] current = new byte[width];

        // The adjacent layout is two bits per acre left-to-right: acres to the left, aligned acres, then acres
        // to the right--all top-to-bottom.
        // The first and last rows are unrolled from the loops to minimize the conditional checks.

        int adjacent = area[0][0] << SIZE_BITS | area[1][0];
        for (int x = 0; x < area[0].length - 1; x++) {
            adjacent = adjacent << SIZE_BITS * 3 & MASK | area[0][x + 1] << SIZE_BITS | area[1][x + 1];
            delayed[x] = patterns[adjacent];
        }
        delayed[area[0].length - 1] = patterns[adjacent << SIZE_BITS * 3 & MASK];

        for (int y = 1; y < area.length - 1; y++) {
            adjacent = area[y - 1][0] << SIZE_BITS * 2 | area[y][0] << SIZE_BITS | area[y + 1][0];
            for (int x = 0; x < area[y].length - 1; x++) {
                adjacent = adjacent << SIZE_BITS * 3 & MASK | area[y - 1][x + 1] << SIZE_BITS * 2 | area[y][x + 1] << SIZE_BITS | area[y + 1][x + 1];
                current[x] = patterns[adjacent];
            }
            current[area[y].length - 1] = patterns[adjacent << SIZE_BITS * 3 & MASK];

            final byte[] temp = area[y - 1];
            area[y - 1] = delayed;
            delayed = current;
            current = temp;
        }

        final int y = area.length - 1;
        adjacent = area[y - 1][0] << SIZE_BITS * 2 | area[y][0] << SIZE_BITS;
        for (int x = 0; x < area[y].length - 1; x++) {
            adjacent = adjacent << SIZE_BITS * 3 & MASK | area[y - 1][x + 1] << SIZE_BITS * 2 | area[y][x + 1] << SIZE_BITS;
            current[x] = patterns[adjacent];
        }
        current[area[y].length - 1] = patterns[adjacent << SIZE_BITS * 3 & MASK];

        area[y - 1] = delayed;
        area[y] = current;
    }

    private static int getResourceValue(final byte[][] area) {
        final int[] counts = new int[3];
        for (final byte[] row : area) {
            for (final byte acre : row) {
                counts[acre & 0b11] += 1;
            }
        }

        return counts[TREE] * counts[LUMBERYARD];
    }

    private static BitSet compact(final byte[][] area) {
        final BitSet bits = new BitSet(area.length * area[0].length * 2);
        int bit = 0;
        for (final byte[] row : area) {
            for (final byte acre : row) {
                if ((acre & 0b1) != 0) {
                    bits.set(bit);
                }
                bit++;
                if ((acre & 0b10) != 0) {
                    bits.set(bit);
                }
                bit++;
            }
        }
        return bits;
    }

    private static void restore(final BitSet bits, final byte[][] area) {
        int bit = 0;
        for (int y = 0; y < area.length; y++) {
            for (int x = 0; x < area[y].length; x++) {
                area[y][x] = 0;
                if (bits.get(bit)) {
                    area[y][x] |= 0b01;
                }
                bit++;
                if (bits.get(bit)) {
                    area[y][x] |= 0b10;
                }
                bit++;
            }
        }
    }

    private static byte[] calculatePatterns() {
        final byte[] patterns = new byte[1 << SIZE_BITS * 9];

        final int TREES_MASK = 0b010101010001010101;
        final int LUMBERYARDS_MASK = 0b101010100010101010;
        for (int adjacent = 0; adjacent < patterns.length; adjacent++) {
            final int current = adjacent >> SIZE_BITS * 4 & 0b11;
            patterns[adjacent] = switch (current) {
                case GROUND -> Integer.bitCount(adjacent & TREES_MASK) >= 3 ? TREE : GROUND;
                case TREE -> Integer.bitCount(adjacent & LUMBERYARDS_MASK) >= 3 ? LUMBERYARD : TREE;
                case LUMBERYARD -> (adjacent & LUMBERYARDS_MASK) != 0 && (adjacent & TREES_MASK) != 0
                        ? LUMBERYARD
                        : GROUND;
                default -> 0; // some combinations are invalid, it's safe to ignore them
            };
        }

        return patterns;
    }

}
