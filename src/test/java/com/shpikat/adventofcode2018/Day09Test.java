package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static com.shpikat.adventofcode2018.Utils.readInput;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Day 09")
class Day09Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("What is the winning Elf's score")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day09.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments("9 players; last marble is worth 25 points", 32),
                    arguments("10 players; last marble is worth 1618 points", 8317),
                    arguments("13 players; last marble is worth 7999 points", 146373),
                    arguments("17 players; last marble is worth 1104 points", 2764),
                    arguments("21 players; last marble is worth 6111 points", 54718),
                    arguments("30 players; last marble is worth 5807 points", 37305),
                    arguments(readInput("day09_input.txt"), 402398)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the winning Elf's score if the number of the last marble were 100 times larger?")
        @Test
        void testSolution() throws IOException {
            final String input = readInput("day09_input.txt");
            assertEquals(3426843186L, Day09.Part2.solve(input));
        }
    }
}
