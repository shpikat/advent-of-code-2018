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

@DisplayName("Day 23")
class Day23Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("How many nanobots are in range of the strongest?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day23.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample", """
                            pos=<0,0,0>, r=4
                            pos=<1,0,0>, r=1
                            pos=<4,0,0>, r=3
                            pos=<0,2,0>, r=1
                            pos=<0,5,0>, r=3
                            pos=<0,0,3>, r=1
                            pos=<1,1,1>, r=1
                            pos=<1,1,2>, r=1
                            pos=<1,3,1>, r=1
                            """), 7),
                    arguments(named("puzzle input", readInput("day23_input.txt")), 497)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the shortest manhattan distance between point in range of the most and 0,0,0?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day23.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample", """
                            pos=<10,12,12>, r=2
                            pos=<12,14,12>, r=2
                            pos=<16,12,12>, r=4
                            pos=<14,14,14>, r=6
                            pos=<50,50,50>, r=200
                            pos=<10,10,10>, r=5
                            """), 36),
                    arguments(named("puzzle input", readInput("day23_input.txt")), 85761543)
            );
        }
    }
}
