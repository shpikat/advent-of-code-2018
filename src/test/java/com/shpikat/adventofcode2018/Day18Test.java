package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static com.shpikat.adventofcode2018.Utils.readInput;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Day 18")
class Day18Test {

    private static final Named<String> sample = named("sample", """
            .#.#...|#.
            .....#|##|
            .|..|...#.
            ..|#.....#
            #.#|||#|#|
            ...#.||...
            .|....|...
            ||...#|.#|
            |.||||..|.
            ...#.|..|.
            """);

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("What will be the total resource value after 10 minutes?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day18.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample, 1147),
                    arguments(named("puzzle input", readInput("day18_input.txt")), 384416)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What will be the total resource value after 1000000000 minutes?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day18.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("puzzle input", readInput("day18_input.txt")), 195776)
            );
        }
    }
}
