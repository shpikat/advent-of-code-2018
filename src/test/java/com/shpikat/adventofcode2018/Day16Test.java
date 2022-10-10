package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.shpikat.adventofcode2018.Utils.readInput;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 16")
class Day16Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("How many samples behave like three or more opcodes?")
        @Test
        void testSolution() throws IOException {
            assertEquals(547, Day16.Part1.solve(readInput("day16_input.txt")));
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What value is contained in register 0 after executing the test program?")
        @Test
        void testSolution() throws IOException {
            assertEquals(582, Day16.Part2.solve(readInput("day16_input.txt")));
        }
    }
}
