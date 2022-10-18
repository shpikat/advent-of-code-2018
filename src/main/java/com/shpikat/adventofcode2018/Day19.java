package com.shpikat.adventofcode2018;

/**
 * Trivial VM implementation is sufficient for part 1, but even with some kind
 * of JIT takes way too long to finish. Here is pen-and-paper solution.
 *
 * <p>
 * First, disassemble the given instructions. Here are the letters are assigned
 * to registers to distinguish them from the literals: {@code a, b, c, ip, d, e}
 *
 * <pre>
 * #ip 3
 * addi 3 16 3         goto L1
 * seti 1 6 5      L3: e = 1
 * seti 1 8 2      L9: c = 1
 * mulr 5 2 1      L7: b = c * e
 * eqrr 1 4 1          if b == d (then b = 1)
 * addr 1 3 3          goto L4
 * addi 3 1 3          else goto L5
 * addr 5 0 0      L4: a += e
 * addi 2 1 2      L5: c += 1
 * gtrr 2 4 1          if c > d (then b = 1)
 * addr 3 1 3          goto L6
 * seti 2 3 3          else goto L7
 * addi 5 1 5      L6: e += 1
 * gtrr 5 4 1          if e > d (then b = 1)
 * addr 1 3 3          goto L8
 * seti 1 8 3          else goto L9
 * mulr 3 3 3      L8: halt
 * addi 4 2 4      L1: d += 2
 * mulr 4 4 4          d *= d
 * mulr 3 4 4          d *= 19
 * muli 4 11 4         d *= 11
 * addi 1 6 1          b += 6
 * mulr 1 3 1          b *= 22
 * addi 1 10 1         b += 10
 * addr 4 1 4          d += b
 * addr 3 0 3          if a == 1 goto L2
 * seti 0 0 3          else goto L3
 * setr 3 9 1      L2: b = 27
 * mulr 1 3 1          b *= 28
 * addr 3 1 1          b += 29
 * mulr 3 1 1          b *= 30
 * muli 1 14 1         b *= 14
 * mulr 1 3 1          b *= 32
 * addr 4 1 4          d += b
 * seti 0 4 0          a = 0
 * seti 0 0 3          goto L3
 * </pre>
 *
 * <p>
 * Following the code path, there is a piece of code that runs once at startup
 * (which is the only difference between part 1 and part 2), providing
 * {@code d = 978} for part 1 and {@code d = 10551378} for part 2.
 *
 * <p>
 * Then the main cycle starts, that can be written in Java like this:
 *
 * <pre>
 * int = 0;
 * for (int e = 1; e <= d; e++) {
 *     for (int c = 1; c <= d; c++) {
 *         if (c * e == d) {
 *             a += e;
 *         }
 *     }
 * }
 * return a;
 * </pre>
 *
 * <p>
 * Which is quite trivial to see is the code that calculates the sum of
 * all factors for the given number ({@code 1} and the number itself including).
 * Which in turn can be replaced with the far more effective algorithm.
 */
public class Day19 {

    static class Part1 {
        static int solve() {
            return getFactorsSum(978);
        }
    }

    static class Part2 {
        static int solve() {
            return getFactorsSum(10551378);
        }
    }

    private static int getFactorsSum(final int n) {
        if (n == 1) {
            return 1;
        }

        int sum = 0;
        for (int divisor = 1; divisor <= Math.sqrt(n); divisor++) {
            if (n % divisor == 0) {
                final int second = n / divisor;
                sum += divisor == second ? divisor : divisor + second;
            }
        }
        return sum;
    }
}
