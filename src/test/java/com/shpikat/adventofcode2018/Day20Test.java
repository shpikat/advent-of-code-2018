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

@DisplayName("Day 20")
class Day20Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("What is the largest number of doors you would be required to pass through to reach a room?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day20.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample 1", "^WNE$"), 3),
                    arguments(named("sample 2", "^ENWWW(NEEE|SSE(EE|N))$"), 10),
                    arguments(named("sample 3", "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"), 18),
                    arguments(named("sample 4", "^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$"), 23),
                    arguments(named("sample 5", "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"), 31),
                    arguments(named("puzzle input", readInput("day20_input.txt")), 3314)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("How many rooms have a shortest path from your current location that pass through at least 1000 doors?")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(final String input, final int answer) {
            assertEquals(answer, Day20.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("puzzle input", readInput("day20_input.txt")), 8550)
            );
        }
    }
}
