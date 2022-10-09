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

@DisplayName("Day 15")
class Day15Test {

    private static final Named<String> sample1 = named("sample 1", """
            #######
            #.G...#
            #...EG#
            #.#.#G#
            #..G#E#
            #.....#
            #######
            """);
    private static final Named<String> sample2 = named("sample 2", """
            #######
            #G..#E#
            #E#E.E#
            #G.##.#
            #...#E#
            #...E.#
            #######
            """);
    private static final Named<String> sample3 = named("sample 3", """
            #######
            #E..EG#
            #.#G.E#
            #E.##E#
            #G..#.#
            #..E#.#
            #######
            """);
    private static final Named<String> sample4 = named("sample 4", """
            #######
            #E.G#.#
            #.#G..#
            #G.#.G#
            #G..#.#
            #...E.#
            #######
            """);
    private static final Named<String> sample5 = named("sample 5", """
            #######
            #.E...#
            #.#..G#
            #.###.#
            #E#G#G#
            #...#G#
            #######
            """);
    private static final Named<String> sample6 = named("sample 6", """
            #########
            #G......#
            #.E.#...#
            #..##..G#
            #...##..#
            #...#...#
            #.G...G.#
            #.....G.#
            #########
            """);

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("What is the outcome?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day15.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 27730),
                    arguments(sample2, 36334),
                    arguments(sample3, 39514),
                    arguments(sample4, 27755),
                    arguments(sample5, 28944),
                    arguments(sample6, 18740),
                    arguments(named("puzzle input", readInput("day15_input.txt")), 246176)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the outcome?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day15.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 4988),
                    arguments(sample3, 31284),
                    arguments(sample4, 3478),
                    arguments(sample5, 6474),
                    arguments(sample6, 1140),
                    arguments(named("puzzle input", readInput("day15_input.txt")), 58128)
            );
        }
    }
}
