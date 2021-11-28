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

@DisplayName("Day 06")
class Day06Test {

    private static final Named<String> sample1 = named("sample1", """
            1, 1
            1, 6
            8, 3
            3, 4
            5, 5
            8, 9
            """);

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("What is the size of the largest area")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day06.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 17),
                    arguments(named("puzzle input", readInput("day06_input.txt")), 4475)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the size of the region containing all locations")
        @ParameterizedTest(name = "{0} within {1} - {2}")
        @MethodSource("testCases")
        void testSolution(String input, int maxDistance, int answer) {
            assertEquals(answer, Day06.Part2.solve(input, maxDistance));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 32, 16),
                    arguments(named("puzzle input", readInput("day06_input.txt")), 10000, 35237)
            );
        }
    }
}
