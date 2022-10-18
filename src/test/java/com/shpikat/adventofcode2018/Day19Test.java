package com.shpikat.adventofcode2018;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 19")
class Day19Test {

    @DisplayName("Part 1")
    @Nested
    class Part1Test {

        @DisplayName("What value is left in register 0 when the background process halts?")
        @Test
        void testSolution() {
            assertEquals(1968, Day19.Part1.solve());
        }
    }

    @DisplayName("Part 2")
    @Nested
    class Part2Test {
        @DisplayName("What value is left in register 0 when the second background process halts?")
        @Test
        void testSolution() {
            assertEquals(21211200, Day19.Part2.solve());
        }
    }
}
