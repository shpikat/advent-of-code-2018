package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

    private static final Pattern pattern = Pattern.compile("^\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})] (.+)$");

    static class Part1 {
        static int solve(final String input) {
            final Map<Integer, List<Sleep>> guards = readGuardsSchedule(input);

            Integer sleepyGuard = 0;
            int mostMinutesAsleep = 0;
            for (final Map.Entry<Integer, List<Sleep>> entry : guards.entrySet()) {
                int minutesAsleep = 0;
                for (Sleep sleep : entry.getValue()) {
                    minutesAsleep += sleep.end - sleep.start;
                }
                if (minutesAsleep > mostMinutesAsleep) {
                    mostMinutesAsleep = minutesAsleep;
                    sleepyGuard = entry.getKey();
                }
            }

            final int[] minutes = new int[60];
            for (final Sleep sleep : guards.get(sleepyGuard)) {
                for (int i = sleep.start; i < sleep.end; i++) {
                    minutes[i]++;
                }
            }

            int sleepyMinute = -1;
            int maxSleepsInMinute = -1;
            for (int i = 0; i < minutes.length; i++) {
                if (minutes[i] > maxSleepsInMinute) {
                    maxSleepsInMinute = minutes[i];
                    sleepyMinute = i;
                }
            }

            return sleepyGuard * sleepyMinute;
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final Map<Integer, List<Sleep>> guards = readGuardsSchedule(input);

            Integer sleepyGuard = -1;
            int sleepyMinute = -1;
            int maxSleepsInMinute = -1;
            for (final Map.Entry<Integer, List<Sleep>> guard : guards.entrySet()) {

                final int[] minutes = new int[60];
                for (final Sleep sleep : guard.getValue()) {
                    for (int i = sleep.start; i < sleep.end; i++) {
                        minutes[i]++;
                    }
                }

                for (int i = 0; i < minutes.length; i++) {
                    if (minutes[i] > maxSleepsInMinute) {
                        maxSleepsInMinute = minutes[i];
                        sleepyMinute = i;
                        sleepyGuard = guard.getKey();
                    }
                }
            }

            return sleepyGuard * sleepyMinute;
        }
    }

    private static Map<Integer, List<Sleep>> readGuardsSchedule(final String input) {
        final String[] allLines = input.split("\n");
        Arrays.sort(allLines, Comparator.comparing(line -> line.substring(0, 18)));

        final Map<Integer, List<Sleep>> guards = new HashMap<>();
        int currentGuard = 0;
        int start = 0;
        int end;
        for (final String line : allLines) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                final String message = matcher.group(6);
                if (message.startsWith("Guard")) {
                    final int beginIndex = 7;
                    final int endIndex = message.indexOf(' ', beginIndex);
                    currentGuard = Integer.parseInt(message.substring(beginIndex, endIndex));
                } else if (message.equals("falls asleep")) {
                    start = Integer.parseInt(matcher.group(5));
                } else if (message.equals("wakes up")) {
                    end = Integer.parseInt(matcher.group(5));
                    guards.computeIfAbsent(currentGuard, key -> new ArrayList<>()).add(new Sleep(start, end));
                } else {
                    throw new IllegalArgumentException("Unexpected message in line: " + line);
                }
            } else {
                throw new IllegalArgumentException("Pattern doesn't match for line: " + line);
            }

        }
        return guards;
    }

    private record Sleep(int start, int end) {
        @Override
        public String toString() {
            return start + "-" + end;
        }
    }
}
