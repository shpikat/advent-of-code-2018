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

@DisplayName("Day 14")
class Day14Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("What are the scores of the ten recipes immediately after?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final int input, final String answer) {
            assertEquals(answer, Day14.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                    arguments(9, "5158916779"),
                    arguments(5, "0124515891"),
                    arguments(18, "9251071085"),
                    arguments(2018, "5941429882"),
                    arguments(793031, "4910101614")
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("How many recipes appear on the scoreboard to the left of the score sequence?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) throws IOException {
            assertEquals(answer, Day14.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                    arguments("51589", 9),
                    arguments("01245", 5),
                    arguments("92510", 18),
                    arguments("59414", 2018),
                    arguments("793031", 20253137)
            );
        }
    }
}
