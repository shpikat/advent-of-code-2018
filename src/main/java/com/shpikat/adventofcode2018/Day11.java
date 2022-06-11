package com.shpikat.adventofcode2018;

public class Day11 {
    private static final int SIZE = 300;

    static class Part1 {
        static String solve(final String input) {
            final int serialNumber = Integer.parseInt(input);
            final int[][] grid = computeGrid(serialNumber);

            final int size = 3;
            int max = 0;
            int maxX = 0;
            int maxY = 0;

            for (int y = 1; y <= SIZE - size; y++) {
                int currentSum = 0;
                int sliceLeft = 0;
                int sliceMiddle = 0;
                int sliceRight = 0;
                for (int x = 1; x <= size; x++) {
                    sliceLeft = sliceMiddle;
                    sliceMiddle = sliceRight;
                    sliceRight = 0;
                    for (int dy = 0; dy < size; dy++) {
                        sliceRight += grid[y + dy][x];
                    }
                    currentSum += sliceRight;
                }
                if (currentSum > max) {
                    max = currentSum;
                    maxX = 1;
                    maxY = y;
                }

                for (int x = size + 1; x <= SIZE; x++) {
                    currentSum -= sliceLeft;
                    sliceLeft = sliceMiddle;
                    sliceMiddle = sliceRight;
                    sliceRight = 0;
                    for (int dy = 0; dy < size; dy++) {
                        sliceRight += grid[y + dy][x];
                    }
                    currentSum += sliceRight;
                    if (currentSum > max) {
                        max = currentSum;
                        maxX = x - 2;
                        maxY = y;
                    }
                }
            }

            return maxX + "," + maxY;
        }
    }

    static class Part2 {
        static String solve(final String input) {
            final int serialNumber = Integer.parseInt(input);
            final int[][] grid = computeGrid(serialNumber);
            final int[][] sums = computePartialSums(grid);

            int max = 0;
            int maxX = 0;
            int maxY = 0;
            int maxSize = 0;

            for (int size = 1; size <= SIZE; size++) {
                for (int y = 0; y <= SIZE - size; y++) {
                    for (int x = 0; x <= SIZE - size; x++) {
                        final int current = sums[y + size][x + size] - sums[y + size][x] - sums[y][x + size] + sums[y][x];
                        if (current > max) {
                            max = current;
                            maxX = x;
                            maxY = y;
                            maxSize = size;
                        }
                    }
                }
            }

            // max coordinates are 1 off of the top-left corner of the actual square
            return (maxX + 1) + "," + (maxY + 1) + "," + maxSize;
        }

        private static int[][] computePartialSums(final int[][] grid) {
            // Actual grid has 1-based indexing, not 0-based like Java has
            final int[][] sums = new int[SIZE + 1][SIZE + 1];
            for (int y = 1; y <= SIZE; y++) {
                for (int x = 1; x <= SIZE; x++) {
                    // sums at actual index 0 have zero values, no need to check the index boundaries then
                    final int sumAbove = sums[y - 1][x];
                    final int sumToTheLeft = sums[y][x - 1];
                    final int sumToTheLeftAndAbove = sums[y - 1][x - 1];
                    sums[y][x] = grid[y][x] + sumAbove + sumToTheLeft - sumToTheLeftAndAbove;
                }
            }
            return sums;
        }
    }

    private static int[][] computeGrid(final int serialNumber) {
        // Actual grid has 1-based indexing, not 0-based like Java has
        final int[][] grid = new int[SIZE + 1][SIZE + 1];
        for (int y = 1; y <= SIZE; y++) {
            for (int x = 1; x <= SIZE; x++) {
                final int rackId = x + 10;
                final int aux = (rackId * y + serialNumber) * rackId;
                grid[y][x] = (aux / 100) % 10 - 5;
            }
        }
        return grid;
    }
}
