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

@DisplayName("Day 01")
class Day01Test {

    private static final Named<String> sample1 = named("sample1", """
            +1
            -2
            +3
            +1
            """);

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("What is the resulting frequency")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day01.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 3),
                    arguments(named("sample2", """
                            +1
                            +1
                            +1
                            """), 3),
                    arguments(named("sample3", """
                            +1
                            +1
                            -2
                            """), 0),
                    arguments(named("sample4", """
                            -1
                            -2
                            -3
                            """), -6),
                    arguments(named("puzzle input", readInput("day01_input.txt")), 435)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the first frequency your device reaches twice")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day01.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 2),
                    arguments(named("sample2", """
                            +1
                            -1
                            """), 0),
                    arguments(named("sample3", """
                            +3
                            +3
                            +4
                            -2
                            -4
                            """), 10),
                    arguments(named("sample4", """
                            -6
                            +3
                            +8
                            +5
                            -6
                            """), 5),
                    arguments(named("sample5", """
                            +7
                            +7
                            -2
                            -7
                            -4
                            """), 14),
                    arguments(named("puzzle input", readInput("day01_input.txt")), 245)
            );
        }
    }
}
