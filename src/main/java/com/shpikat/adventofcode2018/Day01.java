package com.shpikat.adventofcode2018;

import java.util.HashSet;
import java.util.Set;

public class Day01 {

    static class Part1 {
        static int solve(final String input) {
            final int[] changes = readChanges(input);

            int frequency = 0;
            for (final int change : changes) {
                frequency += change;
            }

            return frequency;
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final int[] changes = readChanges(input);

            final Set<Integer> frequencies = new HashSet<>();
            int frequency = 0;
            int index = 0;
            while (frequencies.add(frequency)) {
                frequency += changes[index++ % changes.length];
            }

            return frequency;
        }
    }

    private static int[] readChanges(final String input) {
        final String[] splits = input.split("\n");
        int[] changes = new int[splits.length];
        int insertIndex = 0;
        for (String line : splits) {
            changes[insertIndex++] = Integer.parseInt(line);
        }
        return changes;
    }
}
