package com.shpikat.adventofcode2018;

import java.util.Arrays;

public class Day12 {

    private static final char EMPTY = '.';
    private static final char PLANT = '#';

    static class Part1 {

        private static final int GENERATIONS = 20;

        static int solve(final String input) {
            final String[] sections = input.split("\n\n", 2);
            assert sections.length == 2;
            final Pots pots = Pots.fromInput(sections[0]);
            final String[] notes = sections[1].split("\n");

            final Rule[] rules = Arrays.stream(notes)
                    .map(Rule::fromInput)
                    .filter(Rule::mustGrow)
                    .toArray(Rule[]::new);

            // The solution from Part 2 is universal for both parts,
            // but for the sake of having separate solutions.
            for (int i = 0; i < GENERATIONS; i++) {
                pots.applyRules(rules);
            }

            return pots.getSumOfPlantNumbers();
        }
    }

    static class Part2 {

        private static final long GENERATIONS = 50_000_000_000L;

        static long solve(final String input) {
            final String[] sections = input.split("\n\n");
            assert sections.length == 2;
            final Pots pots = Pots.fromInput(sections[0]);
            final String[] notes = sections[1].split("\n");

            final Rule[] rules = Arrays.stream(notes)
                    .map(Rule::fromInput)
                    .filter(Rule::mustGrow)
                    .toArray(Rule[]::new);

            for (long i = 0; i < GENERATIONS; i++) {
                final Pots.Result result = pots.applyRules(rules);
                if (result.isRepeated()) {
                    final long generationsLeft = GENERATIONS - i - 1;
                    return pots.getSumOfPlantNumbersInPerspective(result.shift(), generationsLeft);
                }
            }

            return pots.getSumOfPlantNumbers();
        }
    }

    private static final class Pots {

        private static final int POTS_TO_KNOW = 2;
        private byte[] state;
        private int potNumberOffset;

        private Pots(final int size) {
            // Reserve two pots in both directions for potential plants to grow
            state = new byte[size + POTS_TO_KNOW * 2];
            potNumberOffset = POTS_TO_KNOW;
        }

        static Pots fromInput(final String input) {
            final String initialState = input.substring("initial state: ".length());
            final Pots pots = new Pots(initialState.length());
            for (int i = 0; i < initialState.length(); i++) {
                if (initialState.charAt(i) == PLANT) {
                    pots.set(i);
                }
            }
            return pots;
        }

        Result applyRules(final Rule[] rules) {

            final byte[] snapshot = state;
            final int snapshotOffset = potNumberOffset;

            // Shrink the state for unused pots
            int start = 0;
            for (int i = 0; i < state.length; i++) {
                if (state[i] != 0) {
                    start = i;
                    break;
                }
            }
            int end = 0;
            for (int i = state.length - 1; i > start; i--) {
                if (state[i] != 0) {
                    end = i;
                    break;
                }
            }

            state = new byte[end + 1 - start + POTS_TO_KNOW * 2];
            potNumberOffset += POTS_TO_KNOW - start;
            for (final Rule rule : rules) {
                for (int i = start; i <= end; i++) {
                    if (snapshot[i] == rule.pattern()) {
                        set(i - snapshotOffset);
                    }
                }
            }

            return new Result(Arrays.equals(snapshot, state), snapshotOffset - potNumberOffset);
        }

        int getSumOfPlantNumbers() {
            return Math.toIntExact(getSumOfPlantNumbersInPerspective(1, 0));
        }

        long getSumOfPlantNumbersInPerspective(final int shift, final long generations) {
            int n = 0;
            int count = 0;
            for (int i = 0; i < state.length; i++) {
                if ((state[i] & (1 << POTS_TO_KNOW)) != 0) {
                    n += i;
                    count++;
                }
            }
            return n + (count * (generations - potNumberOffset) * shift);
        }

        private void set(final int pot) {
            // Store plant itself and let surrounding pots know, too
            final int index = potNumberOffset + pot;
            state[index - POTS_TO_KNOW] |= 1 << 4;
            state[index - 1] |= 1 << 3;
            state[index] |= 1 << POTS_TO_KNOW;
            state[index + 1] |= 1 << 1;
            state[index + POTS_TO_KNOW] |= 1;
        }

        @Override
        public String toString() {
            final int gapSize = Math.max(0, 10 - potNumberOffset);
            final StringBuilder sb = new StringBuilder(" ".repeat(gapSize));
            sb.ensureCapacity(gapSize + state.length);
            for (final byte pot : state) {
                sb.append((pot & (1 << POTS_TO_KNOW)) == 0 ? EMPTY : PLANT);
            }
            return sb.toString();
        }

        private record Result(boolean isRepeated, int shift) {
        }
    }

    private record Rule(byte pattern, boolean mustGrow) {

        static Rule fromInput(final String input) {
            final String[] splits = input.split(" => ");
            assert splits.length == 2;
            byte pattern = 0;
            for (int i = 0; i < splits[0].length(); i++) {
                if (splits[0].charAt(i) == PLANT) {
                    pattern |= 1 << i;
                }
            }
            return new Rule(pattern, splits[1].charAt(0) == PLANT);
        }
    }
}
