package com.shpikat.adventofcode2018;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * As in {@link Day19} trivial VM implementation takes unacceptably long for
 * part 2. Here is pen-and-paper solution.
 *
 * <p>
 * Again, disassemble the given instructions. Here are the letters are assigned
 * to registers to distinguish them from the literals: {@code a, b, c, d, e, ip}
 *
 * <pre>
 * #ip 5
 * seti 123 0 1         b = 123
 * bani 1 456 1     L2: b &= 456
 * eqri 1 72 1          if (b == 72) (then b = 1)
 * addr 1 5 5           goto L1
 * seti 0 0 5           else goto L2
 * seti 0 4 1       L1: b = 0
 * bori 1 65536 4   L11:e = b | 65536
 * seti 12772194 7 1    b = 12772194
 * bani 4 255 3     L10:d = e & 255
 * addr 1 3 1           b += d
 * bani 1 16777215 1    b &= 16777215
 * muli 1 65899 1       b *= 65899
 * bani 1 16777215 1    b &= 16777215
 * gtir 256 4 3         if (256 > e) (then d = 1)
 * addr 3 5 5           goto L3
 * addi 5 1 5           else goto L4
 * seti 27 3 5      L3: goto L5
 * seti 0 0 3       L4: d = 0
 * addi 3 1 2       L9: c = d + 1
 * muli 2 256 2         c *= 256
 * gtrr 2 4 2           if (c > e) (then c = 1)
 * addr 2 5 5           goto L6
 * addi 5 1 5           else goto L7
 * seti 25 5 5      L6: goto L8
 * addi 3 1 3       L7: d += 1
 * seti 17 4 5          goto L9
 * setr 3 4 4       L8: e = d
 * seti 7 1 5           goto L10
 * eqrr 1 0 3       L5: if (b == a) (then d = 1)
 * addr 3 5             halt
 * seti 5 1 5           else goto L11
 * </pre>
 *
 * <p>
 * After the code is converted to Java and command sequences simplified
 * by a human, it runs fast enough:
 *
 * <pre>
 * int b = 0;
 * do {
 *     int e = b | 0x10000;
 *     b = 12772194;
 *     while (e != 0) {
 *         b += e & 0xff;
 *         b &= 0xffffff;
 *         b *= 65899;
 *         b &= 0xffffff;
 *         e >>= 8;
 *     }
 * } while (a != b);
 * </pre>
 */
public class Day21 {

    static class Part1 {

        static int solve() {
            final FewestInstructions hook = new FewestInstructions();
            runNative(hook);
            return hook.getValue();
        }

        private static class FewestInstructions implements IntPredicate {
            private int value;

            @Override
            public boolean test(final int value) {
                this.value = value;
                return false;
            }

            int getValue() {
                return value;
            }
        }
    }

    static class Part2 {

        static int solve() {
            final MostInstructions hook = new MostInstructions();
            runNative(hook);
            return hook.getValue();
        }

        private static class MostInstructions implements IntPredicate {
            private final Set<Integer> nonRepeatedValues = new HashSet<>();
            private int lastNonRepeatedValue;

            @Override
            public boolean test(final int value) {
                final boolean mustContinue = nonRepeatedValues.add(value);
                if (mustContinue) {
                    this.lastNonRepeatedValue = value;
                }
                return mustContinue;
            }

            int getValue() {
                return lastNonRepeatedValue;
            }
        }
    }

    private static void runNative(final IntPredicate hook) {
        int b = 0;
        do {
            int e = b | 0x10000;
            b = 12772194;
            while (e != 0) {
                b += e & 0xff;
                b &= 0xffffff;
                b *= 65899;
                b &= 0xffffff;
                e >>= 8;
            }
        } while (hook.test(b));
    }
}
