package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
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

@DisplayName("Day 13")
class Day13Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("What is the location of the first crash?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final String answer) {
            assertEquals(answer, Day13.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample", """
                            /->-\\       \s
                            |   |  /----\\
                            | /-+--+-\\  |
                            | | |  | v  |
                            \\-+-/  \\-+--/
                              \\------/  \s
                            """), "7,3"),
                    arguments(named("puzzle input", readInput("day13_input.txt")), "43,91")
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the location of the last cart?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final String answer) {
            assertEquals(answer, Day13.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample", """
                            />-<\\ \s
                            |   | \s
                            | /<+-\\
                            | | | v
                            \\>+</ |
                              |   ^
                              \\<->/
                            """), "6,4"),
                    arguments(named("puzzle input", readInput("day13_input.txt")), "35,59")
            );
        }
    }
}
