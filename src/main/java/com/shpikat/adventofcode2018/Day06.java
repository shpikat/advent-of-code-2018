package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.List;

public class Day06 {
    static class Part1 {
        static int solve(final String input) {
            final InputData data = readInput(input);

            final Location[][] grid = new Location[data.height][data.width];
            for (final Coordinate coordinate : data.coordinates) {
                for (int y = 0; y < data.height; y++) {
                    final int yDistance = Math.abs(coordinate.y - y);
                    for (int x = 0; x < data.width; x++) {
                        final int distance = yDistance + Math.abs(coordinate.x - x);
                        if (grid[y][x] == null || grid[y][x].distance > distance) {
                            grid[y][x] = new Location(coordinate.id, distance);
                        } else if (grid[y][x].distance == distance) {
                            grid[y][x] = new Location(-1, distance);
                        }
                    }
                }
            }

            final int[] areas = new int[data.coordinates.size()];
            for (Location[] row : grid) {
                for (final Location location : row) {
                    if (location.coordinateId >= 0) {
                        areas[location.coordinateId]++;
                    }
                }
            }
            for (int x = 0; x < data.width; x++) {
                int coordinateId = grid[0][x].coordinateId;
                if (coordinateId >= 0) {
                    areas[coordinateId] = 0;
                }
                coordinateId = grid[data.height - 1][x].coordinateId;
                if (coordinateId >= 0) {
                    areas[coordinateId] = 0;
                }
            }
            for (int y = 0; y < data.height; y++) {
                int coordinateId = grid[y][0].coordinateId;
                if (coordinateId >= 0) {
                    areas[coordinateId] = 0;
                }
                coordinateId = grid[y][data.width - 1].coordinateId;
                if (coordinateId >= 0) {
                    areas[coordinateId] = 0;
                }
            }

            int largestArea = 0;
            for (final int area : areas) {
                largestArea = Math.max(largestArea, area);
            }

            return largestArea;
        }

        private record Location(int coordinateId, int distance) {
        }
    }

    static class Part2 {
        static int solve(final String input, final int maxDistance) {
            final InputData data = readInput(input);

            final int[][] grid = new int[data.height][data.width];
            for (final Coordinate coordinate : data.coordinates) {
                for (int y = 0; y < data.height; y++) {
                    final int yDistance = Math.abs(coordinate.y - y);
                    for (int x = 0; x < data.width; x++) {
                        grid[y][x] += yDistance + Math.abs(coordinate.x - x);
                    }
                }
            }

            int regionSize = 0;
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    if (grid[y][x] < maxDistance) {
                        ++regionSize;
                    }
                }
            }

            return regionSize;
        }
    }

    private static InputData readInput(final String input) {
        final String[] lines = input.split("\n");
        final List<Coordinate> coordinates = new ArrayList<>(lines.length);
        int width = 0;
        int height = 0;
        for (int i = 0; i < lines.length; i++) {
            final Coordinate coordinate = Coordinate.read(i, lines[i]);
            coordinates.add(coordinate);
            width = Math.max(width, coordinate.x + 1);
            height = Math.max(height, coordinate.y + 1);
        }

        return new InputData(coordinates, width, height);
    }

    private record InputData(List<Coordinate> coordinates, int width, int height) {
    }

    private record Coordinate(int id, int x, int y) {

        static Coordinate read(final int id, final String line) {
            final int separatorIndex = line.indexOf(',');
            final int x = Integer.parseInt(line.substring(0, separatorIndex));
            final int y = Integer.parseInt(line.substring(separatorIndex + 2));
            return new Coordinate(id, x, y);
        }
    }
}
