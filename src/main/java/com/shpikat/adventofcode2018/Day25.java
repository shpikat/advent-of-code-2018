package com.shpikat.adventofcode2018;

public class Day25 {
    static class Part1 {

        static int solve(final String input) {
            final String[] lines = input.split("\n");

            final Point[] points = new Point[lines.length];
            final DisjointSets sets = new DisjointSets(lines.length);

            for (int i = 0; i < lines.length; i++) {
                final String[] parts = lines[i].trim().split(",", 4);
                assert parts.length == 4;
                final int[] coordinates = new int[parts.length];
                for (int j = 0; j < parts.length; j++) {
                    coordinates[j] = Integer.parseInt(parts[j]);
                }
                final Point point = new Point(coordinates);
                points[i] = point;
                for (int j = 0; j < i; j++) {
                    if (point.getManhattan(points[j]) <= 3) {
                        sets.union(i, j);
                    }
                }
            }

            return sets.getCount();
        }

        private record Point(int[] coordinates) {
            int getManhattan(final Point that) {
                assert this.coordinates().length == that.coordinates().length;
                int sum = 0;
                for (int i = 0; i < this.coordinates().length; i++) {
                    sum += Math.abs(that.coordinates()[i] - this.coordinates()[i]);
                }
                return sum;
            }
        }

        private static class DisjointSets {
            private final int[] rank;
            private final int[] parent;

            DisjointSets(final int size) {
                rank = new int[size];
                parent = new int[size];
                for (int i = 0; i < size; i++) {
                    parent[i] = i;
                }
            }

            int find(int x) {
                while (parent[x] != x) {
                    final int p = parent[x];
                    parent[x] = parent[p];
                    x = p;
                }
                return x;
            }

            void union(final int x, final int y) {
                final int parent1 = find(x);
                final int parent2 = find(y);
                if (parent1 != parent2) {
                    if (rank[parent1] < rank[parent2]) {
                        parent[parent1] = parent2;
                    } else if (rank[parent1] > rank[parent2]) {
                        parent[parent2] = parent1;
                    } else {
                        parent[parent2] = parent1;
                        rank[parent1]++;
                    }
                }
            }

            int getCount() {
                int count = 0;
                for (int i = 0; i < parent.length; i++) {
                    if (find(i) == i) {
                        count++;
                    }
                }
                return count;
            }
        }
    }
}
