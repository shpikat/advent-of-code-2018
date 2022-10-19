package com.shpikat.adventofcode2018;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Day20 {

    static class Part1 {
        static int solve(final String input) {
            final Map<Coordinate, Integer> doors = parseRegex(input);
            return Collections.max(doors.values());
        }

    }

    static class Part2 {
        static int solve(final String input) {
            final Map<Coordinate, Integer> doors = parseRegex(input);
            return (int) doors.values().stream().filter(n -> n >= 1000).count();
        }
    }

    private static Map<Coordinate, Integer> parseRegex(final String input) {
        assert input.startsWith("^");
        assert input.endsWith("$");

        final Map<Coordinate, Integer> doors = new HashMap<>();
        Coordinate current = new Coordinate(0, 0);
        doors.put(current, 0);
        final Deque<Coordinate> stack = new ArrayDeque<>();
        for (int i = 1; i < input.length() - 1; i++) {
            final char ch = input.charAt(i);
            switch (ch) {
                case '(' -> stack.push(current);
                case ')' -> current = stack.pop();
                case '|' -> current = Objects.requireNonNull(stack.peek());
                default -> {
                    final Coordinate next = switch (ch) {
                        case 'N' -> new Coordinate(current.x() - 1, current.y());
                        case 'E' -> new Coordinate(current.x(), current.y() + 1);
                        case 'S' -> new Coordinate(current.x() + 1, current.y());
                        case 'W' -> new Coordinate(current.x(), current.y() - 1);
                        default -> throw new IllegalArgumentException("Unexpected direction: " + ch);
                    };
                    doors.merge(next, doors.get(current) + 1, Math::min);
                    current = next;
                }
            }
        }

        return doors;
    }

    private record Coordinate(int x, int y) {
    }
}
