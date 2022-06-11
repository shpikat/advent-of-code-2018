package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Day 11")
class Day11Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("Where is the most powerful 3x3 square?")
        @ParameterizedTest(name = "For grid {0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final String answer) {
            assertEquals(answer, Day11.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                    arguments("18", "33,45"),
                    arguments("42", "21,61"),
                    arguments("1309", "20,43")
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the most powerful square?")
        @ParameterizedTest(name = "For grid {0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final String answer) throws IOException {
            assertEquals(answer, Day11.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                    arguments("18", "90,269,16"),
                    arguments("42", "232,251,12"),
                    arguments("1309", "233,271,13")
            );
        }
    }
}
