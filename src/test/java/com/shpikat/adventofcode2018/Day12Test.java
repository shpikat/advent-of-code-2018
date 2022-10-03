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

@DisplayName("Day 12")
class Day12Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("After 20 generations, what is the sum of the numbers of all pots which contain a plant?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day12.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample1", """
                            initial state: #..#.#..##......###...###
                                            
                            ...## => #
                            ..#.. => #
                            .#... => #
                            .#.#. => #
                            .#.## => #
                            .##.. => #
                            .#### => #
                            #.#.# => #
                            #.### => #
                            ##.#. => #
                            ##.## => #
                            ###.. => #
                            ###.# => #
                            ####. => #
                            """), 325),
                    arguments(named("puzzle input", readInput("day12_input.txt")), 1430)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("After fifty billion generations, what is the sum of the numbers of all pots which contain a plant?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final long answer) throws IOException {
            assertEquals(answer, Day12.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("puzzle input", readInput("day12_input.txt")), 1_150_000_000_457L)
            );
        }
    }
}
