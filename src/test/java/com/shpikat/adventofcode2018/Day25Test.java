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

@DisplayName("Day 25")
class Day25Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("How many constellations are formed by the fixed points in spacetime?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day25.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named(
                            "sample 1",
                            """
                                    0,0,0,0
                                    3,0,0,0
                                    0,3,0,0
                                    0,0,3,0
                                    0,0,0,3
                                    0,0,0,6
                                    9,0,0,0
                                    12,0,0,0
                                    """
                    ), 2),
                    arguments(named(
                            "sample 2",
                            """
                                    -1,2,2,0
                                    0,0,2,-2
                                    0,0,0,-2
                                    -1,2,0,0
                                    -2,-2,-2,2
                                    3,0,2,-1
                                    -1,3,2,2
                                    -1,0,-1,0
                                    0,2,1,-2
                                    3,0,0,0
                                    """
                    ), 4),
                    arguments(named(
                            "sample 3",
                            """
                                    1,-1,0,1
                                    2,0,-1,0
                                    3,2,-1,0
                                    0,0,3,1
                                    0,0,-1,-1
                                    2,3,-2,0
                                    -2,2,0,0
                                    2,-2,0,-1
                                    1,-1,0,-1
                                    3,2,0,2
                                    """
                    ), 3),
                    arguments(named(
                            "sample 4",
                            """
                                    1,-1,-1,-2
                                    -2,-2,0,1
                                    0,2,1,3
                                    -2,3,-2,1
                                    0,2,3,-2
                                    -1,-1,1,-2
                                    0,-2,-1,0
                                    -2,2,3,-1
                                    1,2,2,0
                                    -1,-2,0,-2
                                    """
                    ), 8),
                    arguments(named("puzzle input", readInput("day25_input.txt")), 310)
            );
        }
    }
}
