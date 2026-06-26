/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 */
package com.papack.maceattackassistance.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class TickScheduler {
    private static final List<ScheduledTask> scheduledTasks = new LinkedList<ScheduledTask>();
    private static final List<ConditionTask> conditionTasks = new LinkedList<ConditionTask>();
    private static final List<ScheduledTask> scheduledTasksToAdd = new LinkedList<ScheduledTask>();
    private static final List<ConditionTask> conditionTasksToAdd = new LinkedList<ConditionTask>();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setDelayTask(int ticksLater, Runnable task) {
        List<ScheduledTask> list = scheduledTasksToAdd;
        synchronized (list) {
            scheduledTasksToAdd.add(new ScheduledTask(ticksLater, task));
        }
    }

    public static void setConditionTask(BooleanSupplier condition, Runnable task) {
        TickScheduler.setConditionTaskWithKey(condition, task, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setConditionTaskWithKey(BooleanSupplier condition, Runnable task, Object key) {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            conditionTasksToAdd.add(new ConditionTask(condition, task, key));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void cancelPendingConditionTask(Object key) {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            conditionTasksToAdd.removeIf(task -> key != null && key.equals(task.key));
        }
        list = conditionTasks;
        synchronized (list) {
            conditionTasks.removeIf(task -> key != null && key.equals(task.key));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isPendingConditionTaskWithKey(Object key) {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            for (ConditionTask task : conditionTasksToAdd) {
                if (!key.equals(task.key)) continue;
                return true;
            }
        }
        list = conditionTasks;
        synchronized (list) {
            for (ConditionTask task : conditionTasks) {
                if (!key.equals(task.key)) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasOtherConditionTasks(Object excludedKey) {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            for (ConditionTask task : conditionTasksToAdd) {
                if (excludedKey != null && excludedKey.equals(task.key)) continue;
                return true;
            }
        }
        list = conditionTasks;
        synchronized (list) {
            for (ConditionTask task : conditionTasks) {
                if (excludedKey != null && excludedKey.equals(task.key)) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasPendingConditionTasks() {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            if (!conditionTasksToAdd.isEmpty()) {
                return true;
            }
        }
        list = conditionTasks;
        synchronized (list) {
            if (!conditionTasks.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasPendingOrReadyConditionTasks() {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            if (!conditionTasksToAdd.isEmpty()) {
                return true;
            }
        }
        list = conditionTasks;
        synchronized (list) {
            if (!conditionTasks.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasPendingDelayedTasks() {
        List<ScheduledTask> list = scheduledTasksToAdd;
        synchronized (list) {
            if (!scheduledTasksToAdd.isEmpty()) {
                return true;
            }
        }
        list = scheduledTasks;
        synchronized (list) {
            for (ScheduledTask t : scheduledTasks) {
                if (t.ticksRemaining <= 0) continue;
                return true;
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasPendingOrReadyDelayedTasks() {
        List<ScheduledTask> list = scheduledTasksToAdd;
        synchronized (list) {
            if (!scheduledTasksToAdd.isEmpty()) {
                return true;
            }
        }
        list = scheduledTasks;
        synchronized (list) {
            return !scheduledTasks.isEmpty();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void clearAllConditionTasks() {
        List<ConditionTask> list = conditionTasksToAdd;
        synchronized (list) {
            conditionTasksToAdd.clear();
        }
        list = conditionTasks;
        synchronized (list) {
            conditionTasks.clear();
        }
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            synchronized (scheduledTasksToAdd) {
                scheduledTasks.addAll(scheduledTasksToAdd);
                scheduledTasksToAdd.clear();
            }
            synchronized (conditionTasksToAdd) {
                synchronized (conditionTasks) {
                    conditionTasks.addAll(conditionTasksToAdd);
                }
                conditionTasksToAdd.clear();
            }
            synchronized (scheduledTasks) {
                java.util.Iterator<ScheduledTask> iterator = scheduledTasks.iterator();
                while (iterator.hasNext()) {
                    ScheduledTask scheduledTask = iterator.next();
                    --scheduledTask.ticksRemaining;
                    if (scheduledTask.ticksRemaining > 0) continue;
                    scheduledTask.task.run();
                    iterator.remove();
                }
            }
            ArrayList<ConditionTask> toRun = new ArrayList<ConditionTask>();
            synchronized (conditionTasks) {
                for (ConditionTask task : conditionTasks) {
                    if (!task.condition.getAsBoolean()) continue;
                    toRun.add(task);
                }
            }
            for (ConditionTask conditionTask : toRun) {
                conditionTask.task.run();
                List<ConditionTask> list2 = conditionTasks;
                synchronized (list2) {
                    conditionTasks.remove(conditionTask);
                }
            }
        });
    }

    private static class ScheduledTask {
        int ticksRemaining;
        Runnable task;

        ScheduledTask(int ticksRemaining, Runnable task) {
            this.ticksRemaining = ticksRemaining;
            this.task = task;
        }
    }

    private static class ConditionTask {
        BooleanSupplier condition;
        Runnable task;
        Object key;

        ConditionTask(BooleanSupplier condition, Runnable task, Object key) {
            this.condition = condition;
            this.task = task;
            this.key = key;
        }
    }
}
