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

@DisplayName("Day 07")
class Day07Test {

    private static final Named<String> sample1 = named("sample1", """
            Step C must be finished before step A can begin.
            Step C must be finished before step F can begin.
            Step A must be finished before step B can begin.
            Step A must be finished before step D can begin.
            Step B must be finished before step E can begin.
            Step D must be finished before step E can begin.
            Step F must be finished before step E can begin.
            """);

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("In what order should the steps in your instructions be completed")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, String answer) {
            assertEquals(answer, Day07.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, "CABDFE"),
                    arguments(named("puzzle input", readInput("day07_input.txt")), "BGKDMJCNEQRSTUZWHYLPAFIVXO")
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("How long will it take to complete all of the steps")
        @ParameterizedTest(name = "{0} with {1} workers and {2} seconds requirement - {3} seconds")
        @MethodSource("testCases")
        void testSolution(String input, int numberOfWorkers, int taskTimeRequirement, int answer) {
            assertEquals(answer, Day07.Part2.solve(input, numberOfWorkers, taskTimeRequirement));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(sample1, 2, 0, 15),
                    arguments(named("puzzle input", readInput("day07_input.txt")), 5, 60, 941)
            );
        }
    }
}
