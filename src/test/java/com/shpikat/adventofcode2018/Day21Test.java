package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 21")
class Day21Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("What is the lowest non-negative integer value for register 0 that causes the program to halt after executing the fewest instructions?")
        @Test
        void testSolution() throws IOException {
            assertEquals(2159153, Day21.Part1.solve());
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What is the lowest non-negative integer value for register 0 that causes the program to halt after executing the most instructions?")
        @Test
        void testSolution() throws IOException {
            assertEquals(7494885, Day21.Part2.solve());
        }
    }
}
