package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {

    static class Part1 {

        static int solve(final String input) {
            final String[] sections = input.split("\n\n\n\n");

            int nSamplesLikeThreeOrMore = 0;
            final Device device = new Device();
            for (final Sample sample : readSamples(sections[0])) {
                int counter = 0;
                for (int opcode = 0; opcode < Device.SUPPORTED_OPCODES; opcode++) {
                    device.set(sample.before());
                    device.execute(opcode, sample.a(), sample.b(), sample.c());
                    if (device.verify(sample.after())) {
                        counter++;
                        if (counter == 3) {
                            nSamplesLikeThreeOrMore++;
                            break;
                        }
                    }
                }
            }
            return nSamplesLikeThreeOrMore;
        }
    }

    static class Part2 {

        static int solve(final String input) {
            final String[] sections = input.split("\n\n\n\n");

            final Device sampleDevice = new Device();

            final Map<Integer, Collection<Integer>> candidates = new HashMap<>();
            final Map<Integer, List<Sample>> samples = Arrays.stream(readSamples(sections[0]))
                    .collect(Collectors.groupingBy(Sample::opcode));
            for (final Map.Entry<Integer, List<Sample>> entry : samples.entrySet()) {
                final Collection<Integer> opcodes = candidates.computeIfAbsent(entry.getKey(), sample -> new ArrayList<>());

                nextOpcode:
                for (int opcode = 0; opcode < Device.SUPPORTED_OPCODES; opcode++) {
                    for (final Sample sample : entry.getValue()) {
                        sampleDevice.set(sample.before());
                        sampleDevice.execute(opcode, sample.a(), sample.b(), sample.c());
                        if (!sampleDevice.verify(sample.after())) {
                            continue nextOpcode;
                        }
                    }
                    opcodes.add(opcode);
                }
            }

            final int[] opcodes = new int[Device.SUPPORTED_OPCODES];
            while (!candidates.isEmpty()) {
                final Iterator<Map.Entry<Integer, Collection<Integer>>> iterator = candidates.entrySet().iterator();
                while (iterator.hasNext()) {
                    final Map.Entry<Integer, Collection<Integer>> entry = iterator.next();
                    if (entry.getValue().size() == 1) {
                        final Integer actualMapping = entry.getValue().iterator().next();
                        opcodes[entry.getKey()] = actualMapping;
                        iterator.remove();
                        for (final Collection<Integer> c : candidates.values()) {
                            c.remove(actualMapping);
                        }
                    }
                }
            }

            final Device device = new Device();
            for (final String line : sections[1].split("\n")) {
                final String[] command = line.split(" ");
                final int opcode = Integer.parseInt(command[0]);
                final int a = Integer.parseInt(command[1]);
                final int b = Integer.parseInt(command[2]);
                final int c = Integer.parseInt(command[3]);
                device.execute(opcodes[opcode], a, b, c);
            }

            return device.read(0);
        }
    }

    private static class Device {

        static final int SUPPORTED_OPCODES = 16;

        private final int[] registers = new int[4];

        private final List<BinaryOperator<Integer>> opcodes = Arrays.asList(
                (a, b) -> registers[a] + registers[b],
                (a, b) -> registers[a] + b,
                (a, b) -> registers[a] * registers[b],
                (a, b) -> registers[a] * b,
                (a, b) -> registers[a] & registers[b],
                (a, b) -> registers[a] & b,
                (a, b) -> registers[a] | registers[b],
                (a, b) -> registers[a] | b,
                (a, b) -> registers[a],
                (a, b) -> a,
                (a, b) -> a > registers[b] ? 1 : 0,
                (a, b) -> registers[a] > b ? 1 : 0,
                (a, b) -> registers[a] > registers[b] ? 1 : 0,
                (a, b) -> a == registers[b] ? 1 : 0,
                (a, b) -> registers[a] == b ? 1 : 0,
                (a, b) -> registers[a] == registers[b] ? 1 : 0
        );

        void set(final int[] values) {
            if (registers.length != values.length) {
                throw new IllegalArgumentException("Unexpected size of the provided data: " + values.length);
            }
            System.arraycopy(values, 0, registers, 0, values.length);
        }

        void execute(final int opcode, final int a, final int b, final int c) {
            registers[c] = opcodes.get(opcode).apply(a, b);
        }

        boolean verify(final int[] expected) {
            return Arrays.equals(registers, expected);
        }

        int read(final int register) {
            return registers[register];
        }
    }

    private record Sample(int[] before, int opcode, int a, int b, int c, int[] after) {
    }

    private static Sample[] readSamples(final String section) {
        final Pattern pattern = Pattern.compile("^Before: \\[(\\d+), (\\d+), (\\d+), (\\d+)]\n(\\d+) (\\d+) (\\d+) (\\d+)\nAfter:  \\[(\\d+), (\\d+), (\\d+), (\\d+)]");
        return Arrays.stream(section.split("\n\n"))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> {
                    final int[] before = readArray(matcher, 1, 4);
                    final int opcode = readInt(matcher, 5);
                    final int a = readInt(matcher, 6);
                    final int b = readInt(matcher, 7);
                    final int c = readInt(matcher, 8);
                    final int[] after = readArray(matcher, 9, 12);
                    return new Sample(before, opcode, a, b, c, after);
                })
                .toArray(Sample[]::new);
    }

    private static int[] readArray(final Matcher matcher, final int from, final int to) {
        final int[] array = new int[to - from + 1];
        for (int i = 0, group = from; group <= to; i++, group++) {
            array[i] = readInt(matcher, group);
        }
        return array;
    }

    private static int readInt(final Matcher matcher, final int group) {
        return Integer.parseInt(matcher.group(group));
    }
}
