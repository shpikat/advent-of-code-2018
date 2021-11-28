package com.shpikat.adventofcode2018;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Day08 {
    static class Part1 {
        static int solve(final String input) {
            final Node root = readTree(input);

            int sum = 0;
            final Queue<Node> nodes = new ArrayDeque<>(Collections.singleton(root));
            while (!nodes.isEmpty()) {
                final Node node = nodes.remove();
                for (final int value : node.metadata) {
                    sum += value;
                }
                nodes.addAll(node.children);
            }
            return sum;
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final Node root = readTree(input);

            int sum = 0;
            final Queue<Node> nodes = new ArrayDeque<>(Collections.singleton(root));
            while (!nodes.isEmpty()) {
                final Node node = nodes.remove();
                if (node.children.isEmpty()) {
                    for (final int value : node.metadata) {
                        sum += value;
                    }
                } else {
                    for (final int value : node.metadata) {
                        final int index = value - 1;
                        if (index >= 0 && index < node.children.size()) {
                            nodes.add(node.children.get(index));
                        }
                    }
                }
            }
            return sum;
        }

    }

    private static Node readTree(final String input) {
        return readNode(Arrays
                .stream(input.split(" "))
                .mapToInt(Integer::parseInt)
                .iterator());
    }

    private static Node readNode(final PrimitiveIterator.OfInt iterator) {
        final int nChildren = iterator.nextInt();
        final int nMetadata = iterator.nextInt();

        final List<Node> children = Stream
                .generate(() -> readNode(iterator))
                .limit(nChildren)
                .toList();
        final int[] metadata = StreamSupport
                .intStream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .limit(nMetadata)
                .toArray();
        return new Node(metadata, children);
    }

    private record Node(int[] metadata, List<Node> children) {
    }
}
