package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    private static final Pattern pattern = Pattern.compile("^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$");

    static class Part1 {
        static int solve(final String input) {
            final String[] allLines = input.split("\n");
            final List<Rectangle> claims = new ArrayList<>(allLines.length);
            int width = 0;
            int height = 0;
            for (String line : allLines) {
                final Rectangle r = readClaim(line).rectangle;
                claims.add(r);
                width = Math.max(width, r.x2 + 1);
                height = Math.max(height, r.y2 + 1);
            }

            final int[][] grid = new int[height][width];
            for (Rectangle claim : claims) {
                for (int y = claim.y1; y < claim.y2; y++) {
                    for (int x = claim.x1; x < claim.x2; x++) {
                        grid[y][x]++;
                    }
                }
            }

            int area = 0;
            for (int[] row : grid) {
                for (int overlaps : row) {
                    if (overlaps >= 2) {
                        area++;
                    }
                }
            }

            return area;
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final String[] allLines = input.split("\n");
            final List<Claim> claims = new ArrayList<>(allLines.length);
            final Set<Integer> ids = new HashSet<>(allLines.length * 2);
            for (String line : allLines) {
                final Claim claim = readClaim(line);
                claims.add(claim);
                ids.add(claim.id);
            }

            for (int i = 0; i < claims.size() - 1; i++) {
                final Claim claim1 = claims.get(i);
                for (int j = i + 1; j < claims.size(); j++) {
                    final Claim claim2 = claims.get(j);
                    if (claim1.rectangle.intersects(claim2.rectangle)) {
                        ids.remove(claim1.id);
                        ids.remove(claim2.id);
                    }
                }
            }

            if (ids.size() != 1) {
                throw new IllegalStateException("Expecting only one non-overlapping claim, but found " + ids.size());
            }

            return ids.iterator().next();
        }
    }

    private static Claim readClaim(final String line) {
        final Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            final int id = Integer.parseInt(matcher.group(1));
            final int x = Integer.parseInt(matcher.group(2));
            final int y = Integer.parseInt(matcher.group(3));
            final int width = Integer.parseInt(matcher.group(4));
            final int height = Integer.parseInt(matcher.group(5));
            return new Claim(id, new Rectangle(x, y, x + width, y + height));
        } else {
            throw new IllegalArgumentException("Pattern doesn't match for line: " + line);
        }
    }

    private record Claim(int id, Rectangle rectangle) {
    }

    private record Rectangle(int x1, int y1, int x2, int y2) {

        private boolean intersects(final Rectangle that) {
            return Math.min(this.x2, that.x2) > Math.max(this.x1, that.x1)
                    && Math.min(this.y2, that.y2) > Math.max(this.y1, that.y1);
        }
    }
}
