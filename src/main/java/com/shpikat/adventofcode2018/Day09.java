package com.shpikat.adventofcode2018;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 {

    private static final Pattern pattern = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");

    static class Part1 {
        static int solve(final String input) {
            final Rules rules = readRules(input);

            final var scores = new IntScores(rules.nPlayers);
            playMarbles(rules, scores);
            return scores.getMax();
        }
    }

    static class Part2 {
        static long solve(final String input) {
            final Rules rules = readRules(input);

            final Rules updatedRules = new Rules(rules.nPlayers, rules.nMarbles * 100);
            final var scores = new LongScores(rules.nPlayers);
            playMarbles(updatedRules, scores);
            return scores.getMax();
        }
    }

    private interface Scores {
        void update(int player, int score);
    }

    private static class IntScores implements Scores {
        private final int[] scores;

        private IntScores(final int nPlayers) {
            this.scores = new int[nPlayers];
        }

        @Override
        public void update(final int player, final int score) {
            scores[player] += score;
        }

        public int getMax() {
            return Arrays.stream(scores).max().orElse(0);
        }
    }

    private static class LongScores implements Scores {
        private final long[] scores;

        private LongScores(final int nPlayers) {
            this.scores = new long[nPlayers];
        }

        @Override
        public void update(final int player, final int score) {
            scores[player] += score;
        }

        public long getMax() {
            return Arrays.stream(scores).max().orElse(0);
        }
    }

    private static void playMarbles(final Rules rules, final Scores scores) {
        final int[] forward = new int[rules.nMarbles + 1];
        final int[] backward = new int[rules.nMarbles + 1];
        forward[0] = backward[0] = 0;

        int currentMarble = 0;
        int nextSpecialMarble = 23;
        for (int marble = 1; marble <= rules.nMarbles; marble++) {
            if (marble == nextSpecialMarble) {
                // something entirely different happens
                int removedMarble = currentMarble;
                for (int i = 0; i < 7; i++) {
                    removedMarble = backward[removedMarble];
                }
                currentMarble = forward[removedMarble];
                forward[backward[removedMarble]] = currentMarble;
                backward[currentMarble] = backward[removedMarble];

                final int currentPlayer = (nextSpecialMarble - 1) % rules.nPlayers;
                scores.update(currentPlayer, marble + removedMarble);
                nextSpecialMarble += 23;
            } else {
                final int nextMarble = forward[currentMarble];
                forward[marble] = forward[nextMarble];
                forward[nextMarble] = marble;
                backward[marble] = nextMarble;
                backward[forward[marble]] = marble;
                currentMarble = marble;
            }
        }
    }

    private record Rules(int nPlayers, int nMarbles) {
    }

    private static Rules readRules(final String input) {
        final Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            final int nPlayers = Integer.parseInt(matcher.group(1));
            final int lastMarbleValue = Integer.parseInt(matcher.group(2));
            return new Rules(nPlayers, lastMarbleValue);
        } else {
            throw new IllegalArgumentException("Pattern doesn't match for line: " + input);
        }
    }
}
