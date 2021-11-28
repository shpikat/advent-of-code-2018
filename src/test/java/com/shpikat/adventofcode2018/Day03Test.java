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

@DisplayName("Day 03")
class Day03Test {

    private static final Named<String> sample1 = named("sample1", """
            #1 @ 1,3: 4x4
            #2 @ 3,1: 4x4
            #3 @ 5,5: 2x2
            """);

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("How many square inches of fabric are within two or more claims")
        @ParameterizedTest(name = "{0} - {1} sq. in.")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day03.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 4),
                    arguments(named("puzzle input", readInput("day03_input.txt")), 104241)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the ID of the only claim that doesn't overlap")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day03.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 3),
                    arguments(named("puzzle input", readInput("day03_input.txt")), 806)
            );
        }
    }
}
