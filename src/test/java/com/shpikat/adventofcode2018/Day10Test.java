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

@DisplayName("Day 10")
class Day10Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("What message appears?")
        @ParameterizedTest(name = "{0} - {2}")
        @MethodSource("testCases")
        void testSolution(final String input, final String answer, final String text) {
            assertEquals(answer, Day10.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(
                            named("sample1", """
                                            position=< 9,  1> velocity=< 0,  2>
                                            position=< 7,  0> velocity=<-1,  0>
                                            position=< 3, -2> velocity=<-1,  1>
                                            position=< 6, 10> velocity=<-2, -1>
                                            position=< 2, -4> velocity=< 2,  2>
                                            position=<-6, 10> velocity=< 2, -2>
                                            position=< 1,  8> velocity=< 1, -1>
                                            position=< 1,  7> velocity=< 1,  0>
                                            position=<-3, 11> velocity=< 1, -2>
                                            position=< 7,  6> velocity=<-1, -1>
                                            position=<-2,  3> velocity=< 1,  0>
                                            position=<-4,  3> velocity=< 2,  0>
                                            position=<10, -3> velocity=<-1,  1>
                                            position=< 5, 11> velocity=< 1, -2>
                                            position=< 4,  7> velocity=< 0, -1>
                                            position=< 8, -2> velocity=< 0,  1>
                                            position=<15,  0> velocity=<-2,  0>
                                            position=< 1,  6> velocity=< 1,  0>
                                            position=< 8,  9> velocity=< 0, -1>
                                            position=< 3,  3> velocity=<-1,  1>
                                            position=< 0,  5> velocity=< 0, -1>
                                            position=<-2,  2> velocity=< 2,  0>
                                            position=< 5, -2> velocity=< 1,  2>
                                            position=< 1,  4> velocity=< 2,  1>
                                            position=<-2,  7> velocity=< 2, -2>
                                            position=< 3,  6> velocity=<-1, -1>
                                            position=< 5,  0> velocity=< 1,  0>
                                            position=<-6,  0> velocity=< 2,  0>
                                            position=< 5,  9> velocity=< 1, -2>
                                            position=<14,  7> velocity=<-2,  0>
                                            position=<-3,  6> velocity=< 2, -1>
                                            """), """
                                            #...#..###
                                            #...#...#.
                                            #...#...#.
                                            #####...#.
                                            #...#...#.
                                            #...#...#.
                                            #...#...#.
                                            #...#..###
                                            """, "HI"),
                    arguments(named("puzzle input", readInput("day10_input.txt")), """
                            ######..#####.....##....#####...#....#..#....#.....###...####.
                            .....#..#....#...#..#...#....#..#....#..#....#......#...#....#
                            .....#..#....#..#....#..#....#...#..#....#..#.......#...#.....
                            ....#...#....#..#....#..#....#...#..#....#..#.......#...#.....
                            ...#....#####...#....#..#####.....##......##........#...#.....
                            ..#.....#..#....######..#....#....##......##........#...#.....
                            .#......#...#...#....#..#....#...#..#....#..#.......#...#.....
                            #.......#...#...#....#..#....#...#..#....#..#...#...#...#.....
                            #.......#....#..#....#..#....#..#....#..#....#..#...#...#....#
                            ######..#....#..#....#..#####...#....#..#....#...###.....####.
                            """, "ZRABXXJC")
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("How many seconds to wait?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) throws IOException {
            assertEquals(answer, Day10.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(
                            named("sample1", """
                                            position=< 9,  1> velocity=< 0,  2>
                                            position=< 7,  0> velocity=<-1,  0>
                                            position=< 3, -2> velocity=<-1,  1>
                                            position=< 6, 10> velocity=<-2, -1>
                                            position=< 2, -4> velocity=< 2,  2>
                                            position=<-6, 10> velocity=< 2, -2>
                                            position=< 1,  8> velocity=< 1, -1>
                                            position=< 1,  7> velocity=< 1,  0>
                                            position=<-3, 11> velocity=< 1, -2>
                                            position=< 7,  6> velocity=<-1, -1>
                                            position=<-2,  3> velocity=< 1,  0>
                                            position=<-4,  3> velocity=< 2,  0>
                                            position=<10, -3> velocity=<-1,  1>
                                            position=< 5, 11> velocity=< 1, -2>
                                            position=< 4,  7> velocity=< 0, -1>
                                            position=< 8, -2> velocity=< 0,  1>
                                            position=<15,  0> velocity=<-2,  0>
                                            position=< 1,  6> velocity=< 1,  0>
                                            position=< 8,  9> velocity=< 0, -1>
                                            position=< 3,  3> velocity=<-1,  1>
                                            position=< 0,  5> velocity=< 0, -1>
                                            position=<-2,  2> velocity=< 2,  0>
                                            position=< 5, -2> velocity=< 1,  2>
                                            position=< 1,  4> velocity=< 2,  1>
                                            position=<-2,  7> velocity=< 2, -2>
                                            position=< 3,  6> velocity=<-1, -1>
                                            position=< 5,  0> velocity=< 1,  0>
                                            position=<-6,  0> velocity=< 2,  0>
                                            position=< 5,  9> velocity=< 1, -2>
                                            position=<14,  7> velocity=<-2,  0>
                                            position=<-3,  6> velocity=< 2, -1>
                                            """), 3),
                    arguments(named("puzzle input", readInput("day10_input.txt")), 10710)
            );
        }
    }
}
