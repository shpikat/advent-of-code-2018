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

@DisplayName("Day 02")
class Day02Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {
        @DisplayName("What is the checksum")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, int answer) {
            assertEquals(answer, Day02.Part1.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample1", """
                            abcdef
                            bababc
                            abbcde
                            abcccd
                            aabcdd
                            abcdee
                            ababab
                            """), 12),
                    arguments(named("puzzle input", readInput("day02_input.txt")), 5368)
            );
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What letters are common between the two correct box IDs")
        @ParameterizedTest(name = "{0} - {1}")
        @MethodSource("testCases")
        void testSolution(String input, String answer) {
            assertEquals(answer, Day02.Part2.solve(input));
        }

        private static Stream<Arguments> testCases() throws IOException {
            return Stream.of(
                    arguments(named("sample1", """
                            abcde
                            fghij
                            klmno
                            pqrst
                            fguij
                            axcye
                            wvxyz
                            """), "fgij"),
                    arguments(named("puzzle input", readInput("day02_input.txt")), "cvgywxqubnuaefmsljdrpfzyi")
            );
        }
    }
}
