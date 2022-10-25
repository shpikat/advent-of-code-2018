package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day24 {
    private static final Pattern pattern = Pattern.compile("(?<units>\\d+) units each with (?<hitPoints>\\d+) hit points( \\((?<features>[^)]+)\\))? with an attack that does (?<damage>\\d+) (?<damageType>\\w+) damage at initiative (?<initiative>\\d+)");
    private static final Comparator<Group> orderTargetSelection = Comparator
            .comparingInt(Group::effectivePower)
            .thenComparingInt(Group::initiative)
            .reversed();
    private static final Comparator<Group> orderAttack = Comparator
            .comparingInt(Group::initiative)
            .reversed();

    static class Part1 {

        static int solve(final String input) {
            final String[] sections = input.split("\n\n", 2);
            assert sections.length == 2;
            final Collection<Group> immuneSystem = parseArmy(sections[0]);
            final Collection<Group> infection = parseArmy(sections[1]);

            fight(immuneSystem, infection);

            return Stream.concat(immuneSystem.stream(), infection.stream())
                    .mapToInt(Group::units)
                    .sum();
        }
    }

    static class Part2 {
        static int solve(final String input) {
            final String[] sections = input.split("\n\n", 2);
            assert sections.length == 2;
            final Collection<Group> immuneSystem = parseArmy(sections[0]);
            final Collection<Group> infection = parseArmy(sections[1]);

            final Collection<Group> boostedImmuneSystem = new ArrayList<>(immuneSystem.size());
            final Collection<Group> infectionCopy = new ArrayList<>(infection.size());
            int boost = 1;
            while (true) {
                for (final Group group : immuneSystem) {
                    boostedImmuneSystem.add(group.getBoosted(boost));
                }
                for (final Group group : infection) {
                    infectionCopy.add(group.getCopy());
                }

                fight(boostedImmuneSystem, infectionCopy);

                if (!boostedImmuneSystem.isEmpty()) {
                    return boostedImmuneSystem.stream().mapToInt(Group::units).sum();
                }

                boost++;
                infectionCopy.clear();
            }
        }
    }

    private static void fight(final Collection<Group> immuneSystem, final Collection<Group> infection) {
        while (!immuneSystem.isEmpty() && !infection.isEmpty()) {
            final List<Group> attackers = new ArrayList<>(immuneSystem.size() + infection.size());
            attackers.addAll(immuneSystem);
            attackers.addAll(infection);
            attackers.sort(orderTargetSelection);

            final Collection<Group> defendersImmuneSystem = new HashSet<>(immuneSystem);
            final Collection<Group> defendersInfection = new HashSet<>(infection);

            final SortedMap<Group, Group> attacks = new TreeMap<>(orderAttack);
            for (final Group attacker : attackers) {
                final Collection<Group> enemies = immuneSystem.contains(attacker)
                        ? defendersInfection
                        : defendersImmuneSystem;

                int maxDamage = 0;
                Group defender = null;
                for (final Group enemy : enemies) {
                    final int damage = attacker.calculateDamage(enemy);
                    if (damage != 0) {
                        if (damage > maxDamage) {
                            maxDamage = damage;
                            defender = enemy;
                        } else if (damage == maxDamage && orderTargetSelection.compare(enemy, defender) < 0) {
                            defender = enemy;
                        }
                    }
                }

                if (defender != null) {
                    if (defender.willTakeDamage(maxDamage)) {
                        attacks.put(attacker, defender);
                    }
                    enemies.remove(defender);
                }
            }

            if (attacks.isEmpty()) {
                // No clear winner means infection will persist, which effectively means immune system loses
                immuneSystem.clear();
            } else {
                attacks.forEach((attacker, defender) -> defender.takeDamage(attacker.calculateDamage(defender)));

                immuneSystem.removeIf(group -> group.units() == 0);
                infection.removeIf(group -> group.units() == 0);
            }
        }
    }

    private static Collection<Group> parseArmy(final String input) {
        final String[] lines = input.split("\n");
        final Collection<Group> army = new ArrayList<>(lines.length);
        for (int i = 1; i < lines.length; i++) {
            final Matcher matcher = pattern.matcher(lines[i]);
            if (matcher.matches()) {
                String[] immunities = new String[0];
                String[] weaknesses = new String[0];
                final String features = matcher.group("features");
                if (features != null) {
                    for (final String feature : features.split("; ")) {
                        if (feature.startsWith("weak to ")) {
                            weaknesses = feature.substring("weak to ".length()).split(", ");
                        } else if (feature.startsWith("immune to ")) {
                            immunities = feature.substring("immune to ".length()).split(", ");
                        } else {
                            throw new IllegalArgumentException("Unable to parse feature: " + feature);
                        }
                    }
                }

                army.add(new Group(
                        Integer.parseInt(matcher.group("hitPoints")),
                        Set.of(immunities),
                        Set.of(weaknesses),
                        matcher.group("damageType"),
                        Integer.parseInt(matcher.group("damage")),
                        Integer.parseInt(matcher.group("initiative")),
                        Integer.parseInt(matcher.group("units"))
                ));
            } else {
                throw new IllegalArgumentException("Unable to parse line: " + lines[i]);
            }
        }
        return army;
    }

    private static class Group {
        private final int hitPoints;
        private final Set<String> immunities;
        private final Set<String> weaknesses;
        private final String damageType;
        private final int damage;
        private final int initiative;
        private int units;

        private Group(final int hitPoints,
                      final Set<String> immunities,
                      final Set<String> weaknesses,
                      final String damageType,
                      final int damage,
                      final int initiative,
                      final int units
        ) {
            this.hitPoints = hitPoints;
            this.immunities = immunities;
            this.weaknesses = weaknesses;
            this.damageType = damageType;
            this.damage = damage;
            this.initiative = initiative;
            this.units = units;
        }

        int effectivePower() {
            return units() * damage();
        }

        int damage() {
            return damage;
        }

        int initiative() {
            return initiative;
        }

        int units() {
            return units;
        }

        int calculateDamage(final Group enemy) {
            int damage = 0;
            if (!enemy.immunities.contains(damageType)) {
                damage = effectivePower();
                if (enemy.weaknesses.contains(damageType)) {
                    damage *= 2;
                }
            }
            return damage;
        }

        boolean willTakeDamage(final int damage) {
            return damage >= hitPoints;
        }

        void takeDamage(final int damage) {
            units = Math.max(0, units - damage / hitPoints);
        }

        Group getCopy() {
            return new Group(hitPoints, immunities, weaknesses, damageType, damage, initiative, units);
        }

        Group getBoosted(final int boost) {
            return new Group(hitPoints, immunities, weaknesses, damageType, damage + boost, initiative, units);
        }
    }
}
