package com.shpikat.adventofcode2018;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Function;

public class Day13 {

    static class Part1 {

        static String solve(final String input) {
            final String[] map = input.split("\n");
            final Map<Coordinate, Cart> carts = findCarts(map);
            final Queue<Coordinate> cartsOrder = new PriorityQueue<>(Comparator.comparingInt(Coordinate::y).thenComparingInt(Coordinate::x));
            while (!carts.isEmpty()) {
                cartsOrder.addAll(carts.keySet());
                while (!cartsOrder.isEmpty()) {
                    final Coordinate current = cartsOrder.poll();
                    final Cart cart = carts.remove(current);
                    final Coordinate next = cart.move(map, current);
                    if (carts.putIfAbsent(next, cart) != null) {
                        return next.x() + "," + next.y();
                    }
                }
            }
            throw new IllegalStateException("No solution found");
        }
    }

    static class Part2 {

        static String solve(final String input) {
            final String[] map = input.split("\n");
            final Map<Coordinate, Cart> carts = findCarts(map);
            final Queue<Coordinate> cartsOrder = new PriorityQueue<>(Comparator.comparingInt(Coordinate::y).thenComparingInt(Coordinate::x));
            while (carts.size() > 1) {
                cartsOrder.addAll(carts.keySet());
                while (!cartsOrder.isEmpty()) {
                    final Coordinate current = cartsOrder.poll();
                    final Cart cart = carts.remove(current);
                    // May have been removed because of the crash with another cart
                    if (cart != null) {
                        final Coordinate next = cart.move(map, current);
                        // Add if absent, or remove both
                        carts.merge(next, cart, (cart1, cart2) -> null);
                    }
                }
            }
            final Coordinate last = carts.keySet().iterator().next();
            return last.x() + "," + last.y();
        }
    }

    private static Map<Coordinate, Cart> findCarts(final String[] map) {
        final Map<Coordinate, Cart> carts = new HashMap<>();
        for (int y = 0; y < map.length; y++) {
            final String line = map[y];
            for (int x = 0; x < line.length(); x++) {
                final char ch = line.charAt(x);
                if (ch == '^' || ch == '>' || ch == 'v' || ch == '<') {
                    carts.put(new Coordinate(x, y), new Cart(ch));
                }
            }
        }
        return carts;
    }

    private record Coordinate(int x, int y) {
    }

    private static class Cart {

        private static final Turn[] turns = new Turn[]{Turn.LEFT, Turn.STRAIGHT, Turn.RIGHT};
        private Direction direction;
        private int nextTurn = 0;

        public Cart(final char cart) {
            this.direction = switch (cart) {
                case '^' -> Direction.UP;
                case '>' -> Direction.RIGHT;
                case 'v' -> Direction.DOWN;
                case '<' -> Direction.LEFT;
                default -> throw new IllegalArgumentException("Unexpected cart symbol " + cart);
            };
        }

        Coordinate move(final String[] map, final Coordinate current) {
            final Coordinate next = direction.next(current);
            switch (map[next.y()].charAt(next.x())) {
                case '\\' -> direction = switch (direction) {
                    case DOWN -> Direction.RIGHT;
                    case LEFT -> Direction.UP;
                    case RIGHT -> Direction.DOWN;
                    case UP -> Direction.LEFT;
                };
                case '/' -> direction = switch (direction) {
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                    case RIGHT -> Direction.UP;
                    case UP -> Direction.RIGHT;
                };
                case '+' -> {
                    switch (turns[nextTurn]) {
                        case LEFT -> direction = switch (direction) {
                            case DOWN -> Direction.RIGHT;
                            case LEFT -> Direction.DOWN;
                            case RIGHT -> Direction.UP;
                            case UP -> Direction.LEFT;
                        };
                        case STRAIGHT -> {
                        }
                        case RIGHT -> direction = switch (direction) {
                            case DOWN -> Direction.LEFT;
                            case LEFT -> Direction.UP;
                            case RIGHT -> Direction.DOWN;
                            case UP -> Direction.RIGHT;
                        };
                    }
                    nextTurn = (nextTurn + 1) % turns.length;
                }
            }

            return next;
        }

        private enum Direction {
            DOWN, LEFT, RIGHT, UP;

            private static final EnumMap<Direction, Function<Coordinate, Coordinate>> transformations = new EnumMap<>(
                    Map.of(
                            DOWN, c -> new Coordinate(c.x(), c.y() + 1),
                            LEFT, c -> new Coordinate(c.x() - 1, c.y()),
                            RIGHT, c -> new Coordinate(c.x() + 1, c.y()),
                            UP, c -> new Coordinate(c.x(), c.y() - 1)
                    )
            );

            Coordinate next(final Coordinate current) {
                return transformations.get(this).apply(current);
            }
        }

        private enum Turn {
            LEFT, STRAIGHT, RIGHT
        }
    }
}
