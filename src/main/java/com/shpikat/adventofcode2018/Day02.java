package com.shpikat.adventofcode2018;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day02 {

    static class Part1 {
        static int solve(final String input) {
            final String[] allLines = input.split("\n");

            int nTwos = 0;
            int nThrees = 0;
            final Map<Character, Integer> counter = new HashMap<>();
            for (final String line : allLines) {
                counter.clear();
                for (int i = 0; i < line.length(); i++) {
                    final char c = line.charAt(i);
                    counter.merge(c, 1, Integer::sum);
                }
                if (counter.containsValue(2)) {
                    nTwos++;
                }
                if (counter.containsValue(3)) {
                    nThrees++;
                }
            }

            return nTwos * nThrees;
        }
    }

    static class Part2 {
        static String solve(final String input) {
            final String[] allLines = input.split("\n");
            final char[][] ids = new char[allLines.length][];
            int setterIndex = 0;
            for (final String line : allLines) {
                ids[setterIndex++] = line.toCharArray();
            }

            for (int i = 0; i < ids.length - 1; i++) {
                for (int j = i + 1; j < ids.length; j++) {
                    final int firstMismatchIndex = Arrays.mismatch(ids[i], ids[j]);
                    if (firstMismatchIndex >= 0) {
                        final int nextFromIndex = firstMismatchIndex + 1;
                        final int nextMismatchIndex = Arrays.mismatch(
                                ids[i], nextFromIndex, ids[i].length,
                                ids[j], nextFromIndex, ids[j].length);
                        if (nextMismatchIndex < 0) {
                            final String firstPart = new String(ids[i], 0, firstMismatchIndex);
                            final String secondPart = new String(ids[i], nextFromIndex, ids[i].length - nextFromIndex);
                            return firstPart + secondPart;
                        }
                    }
                }
            }
            throw new IllegalStateException("No common letters were found!");
        }
    }
}
