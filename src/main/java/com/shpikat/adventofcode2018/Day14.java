package com.shpikat.adventofcode2018;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day14 {
    static class Part1 {
        static String solve(final int nRecipes) {
            final int required = nRecipes + 10;
            // One extra byte for possible two-digit recipe in the last run
            final int[] recipes = new int[required + 1];

            int elf1 = 0;
            int elf2 = 1;
            recipes[elf1] = 3;
            recipes[elf2] = 7;
            int length = 2;

            while (length < required) {
                int newRecipe = recipes[elf1] + recipes[elf2];
                if (newRecipe >= 10) {
                    recipes[length++] = 1;
                    newRecipe %= 10;
                }
                recipes[length++] = newRecipe;
                elf1 = (elf1 + recipes[elf1] + 1) % length;
                elf2 = (elf2 + recipes[elf2] + 1) % length;
            }

            return Arrays.stream(Arrays.copyOfRange(recipes, nRecipes, required))
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining());
        }
    }

    static class Part2 {

        private static final int CHECK_BATCH_SIZE = 100;

        static int solve(final String input) {
            // Convenient class for growing and searching, especially as it has
            // @IntrinsicCandidate on indexOf
            final StringBuilder sb = new StringBuilder(1_000_000);
            sb.append(3).append(7);
            int elf1 = 0;
            int elf2 = 1;

            // As it appeared, indexOf has some call costs, it pays off
            // to amortize it through batching
            int batch = CHECK_BATCH_SIZE;
            int from = 0;
            while (true) {
                final int recipe1 = sb.charAt(elf1) - '0';
                final int recipe2 = sb.charAt(elf2) - '0';
                sb.append(recipe1 + recipe2);
                elf1 = (elf1 + recipe1 + 1) % sb.length();
                elf2 = (elf2 + recipe2 + 1) % sb.length();
                if (--batch == 0) {
                    final int index = sb.indexOf(input, from);
                    if (index < 0) {
                        from = sb.length() - input.length();
                        batch = CHECK_BATCH_SIZE;
                    } else {
                        return index;
                    }
                }
            }
        }
    }
}
