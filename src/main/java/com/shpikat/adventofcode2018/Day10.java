package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {

    private static final Pattern pattern = Pattern.compile("position=< *(-?\\d+), +(-?\\d+)> velocity=< *(-?\\d+), +(-?\\d+)>");

    static class Part1 {
        static String solve(final String input) {
            final List<Message.Point> points = readPoints(input);

            final Message message = new Message(points);
            message.findMessage();
            return message.getMessage();
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final List<Message.Point> points = readPoints(input);

            final Message message = new Message(points);
            message.findMessage();
            return message.getSecond();
        }
    }


    private static class Message {
        private final List<Point> points;
        private int second = 0;

        Message(final List<Point> points) {
            this.points = points;
        }

        void findMessage() {
            if (points.isEmpty()) {
                throw new IllegalStateException("No message without the points");
            }

            int previousWidth = Integer.MAX_VALUE;
            while (true) {
                next();

                int mostLeft = Integer.MAX_VALUE;
                int mostRight = Integer.MIN_VALUE;
                for (final Point point : points) {
                    mostLeft = Math.min(mostLeft, point.getX());
                    mostRight = Math.max(mostRight, point.getX());
                }
                final int width = mostRight - mostLeft;

                if (previousWidth < width) {
                    previous();
                    return;
                }
                previousWidth = width;
            }
        }

        void next() {
            for (final Point point : points) {
                point.x += point.dx;
                point.y += point.dy;
            }
            second++;
        }

        void previous() {
            for (final Point point : points) {
                point.x -= point.dx;
                point.y -= point.dy;
            }
            second--;
        }

        String getMessage() {
            points.sort(Comparator.comparingInt(Point::getY).thenComparingInt(Point::getX));

            int mostLeft = Integer.MAX_VALUE;
            int mostRight = Integer.MIN_VALUE;
            for (final Point point : points) {
                mostLeft = Math.min(mostLeft, point.getX());
                mostRight = Math.max(mostRight, point.getX());
            }

            final int width = mostRight - mostLeft + 1;

            final String empty = ".".repeat(width);

            final StringBuilder sb = new StringBuilder(points.size() * (width + 1));

            int y = points.get(0).getY();
            int x = mostLeft;
            for (final Point point : points) {
                while (point.getY() > y) {
                    sb.append(empty, 0, width + mostLeft - x).append('\n');
                    y++;
                    x = mostLeft;
                }
                if (x <= point.getX()) {
                    sb.append(empty, 0, point.getX() - x).append('#');
                    x = point.getX() + 1;
                }
            }
            sb.append(empty, 0, width + mostLeft - x).append('\n');

            return sb.toString();
        }

        public int getSecond() {
            return second;
        }

        private static class Point {
            private final int dx, dy;
            private int x, y;

            private Point(final int x, final int y, final int dx, final int dy) {
                this.x = x;
                this.y = y;
                this.dx = dx;
                this.dy = dy;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }
    }

    private static List<Message.Point> readPoints(final String input) {
        final String[] lines = input.split("\n");
        final List<Message.Point> points = new ArrayList<>(lines.length);
        for (final String line : lines) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                points.add(new Message.Point(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))
                ));
            } else {
                throw new IllegalArgumentException("Pattern doesn't match for line: " + line);
            }
        }
        return points;
    }
}
