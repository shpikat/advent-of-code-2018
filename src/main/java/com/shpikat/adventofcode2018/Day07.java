package com.shpikat.adventofcode2018;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class Day07 {
    static class Part1 {
        static String solve(final String input) {
            final TasksManager manager = TasksManager.fromInput(input);

            final StringBuilder answer = new StringBuilder(manager.tasks.size());
            final NavigableSet<Character> nextTasks = new TreeSet<>();
            while (!(manager.tasks.isEmpty() && nextTasks.isEmpty())) {
                manager.updateNextTasks(nextTasks);

                final Character task = nextTasks.pollFirst();
                assert task != null;
                answer.append(task);

                nextTasks.addAll(manager.taskCompleted(task));
            }
            return answer.toString();
        }
    }

    static class Part2 {
        static int solve(final String input, final int numberOfWorkers, final int taskTimeOffset) {
            final TasksManager manager = TasksManager.fromInput(input);
            int tick = 0;
            final Queue<UnitOfWork> workQueue = new PriorityQueue<>(Comparator.comparing(UnitOfWork::finishTime));
            final NavigableSet<Character> nextTasks = new TreeSet<>();
            while (!(manager.tasks.isEmpty() && nextTasks.isEmpty() && workQueue.isEmpty())) {
                manager.updateNextTasks(nextTasks);
                for (final UnitOfWork unitOfWork : workQueue) {
                    nextTasks.remove(unitOfWork.task);
                }
                while (!nextTasks.isEmpty() && workQueue.size() < numberOfWorkers) {
                    final Character task = nextTasks.pollFirst();
                    assert task != null;
                    workQueue.add(new UnitOfWork(task, tick + taskTimeOffset + task - 'A' + 1));
                }

                final UnitOfWork completed = workQueue.poll();
                assert completed != null;
                tick = completed.finishTime;

                nextTasks.addAll(manager.taskCompleted(completed.task));
            }
            return tick;
        }

        private record UnitOfWork(char task, int finishTime) {
        }
    }

    private static class TasksManager {
        private final Map<Character, Set<Character>> tasks;
        private final Map<Character, Set<Character>> dependencies;

        private TasksManager(final Map<Character, Set<Character>> tasks,
                             final Map<Character, Set<Character>> dependencies) {
            this.tasks = tasks;
            this.dependencies = dependencies;
        }

        private void updateNextTasks(final Collection<Character> nextTasks) {
            nextTasks.addAll(dependencies.keySet());
            nextTasks.removeAll(tasks.keySet());
        }

        private Collection<Character> taskCompleted(final Character task) {
            final Collection<Character> unblockedTasks;
            final Set<Character> removed = dependencies.remove(task);
            if (dependencies.isEmpty()) {
                unblockedTasks = new ArrayList<>(tasks.keySet());
                tasks.clear();
            } else {
                unblockedTasks = Collections.emptySet();
                for (final Character dependentTask : removed) {
                    tasks.computeIfPresent(dependentTask, (key, taskDependencies) -> {
                        taskDependencies.remove(task);
                        return taskDependencies.isEmpty() ? null : taskDependencies;
                    });
                }
            }
            return unblockedTasks;
        }

        private static TasksManager fromInput(final String input) {
            final Map<Character, Set<Character>> tasks = new HashMap<>();
            final Map<Character, Set<Character>> dependencies = new HashMap<>();
            for (final String line : input.split("\n")) {
                final char task = line.charAt(36);
                final char requiredTask = line.charAt(5);

                tasks
                        .computeIfAbsent(task, key -> new HashSet<>())
                        .add(requiredTask);
                dependencies
                        .computeIfAbsent(requiredTask, key -> new HashSet<>())
                        .add(task);
            }
            return new TasksManager(tasks, dependencies);
        }
    }
}
