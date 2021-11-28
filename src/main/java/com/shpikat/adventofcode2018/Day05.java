package com.shpikat.adventofcode2018;

public class Day05 {
    static class Part1 {
        static int solve(final String input) {
            final char[] units = input.trim().toCharArray();
            return units.length - react(units);
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final char[] unitsOriginal = input.trim().toCharArray();

            int shortestPolymer = unitsOriginal.length;
            for (char remove = 'a'; remove <= 'z'; remove++) {
                final char[] units = unitsOriginal.clone();

                final int preReacted = preReact(units, remove);
                final int nReacted = react(units);
                final int count = units.length - preReacted - nReacted;

                shortestPolymer = Math.min(count, shortestPolymer);
            }

            return shortestPolymer;
        }

        private static int preReact(final char[] units, final char remove) {
            int nReacted = 0;
            final char removeUpperCase = Character.toUpperCase(remove);
            for (int i = 0; i < units.length; i++) {
                if (units[i] == remove || units[i] == removeUpperCase) {
                    units[i] = 0;
                    ++nReacted;
                }
            }
            return nReacted;
        }
    }

    private static int react(final char[] units) {
        int nReacted = 0;

        int current = 0;
        int next = 1;
        while (next < units.length) {
            final char currentUnit = units[current];
            final char nextUnit = units[next];
            if (currentUnit != nextUnit && Character.toLowerCase(currentUnit) == Character.toLowerCase(nextUnit)) {
                units[current] = 0;
                units[next] = 0;
                nReacted += 2;
                do {
                    --current;
                } while (current >= 0 && units[current] == 0);
                if (current < 0) {
                    current = next;
                    do {
                        ++current;
                    } while (current < units.length && units[current] == 0);
                    next = current;
                }
            } else {
                current = next;
            }
            do {
                ++next;
            } while (next < units.length && units[next] == 0);
        }
        return nReacted;
    }
}
