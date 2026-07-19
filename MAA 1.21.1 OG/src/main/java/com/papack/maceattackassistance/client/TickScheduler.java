/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 */
package com.papack.maceattackassistance.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class TickScheduler {
    private static final List<ScheduledTask> scheduledTasks = new LinkedList<ScheduledTask>();
    private static final List<ConditionTask> conditionTasks = new LinkedList<ConditionTask>();

    public static void schedule(int ticksLater, Runnable task) {
        scheduledTasks.add(new ScheduledTask(ticksLater, task));
    }

    public static void scheduleWhen(BooleanSupplier condition, Runnable task) {
        conditionTasks.add(new ConditionTask(condition, task));
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Iterator<ScheduledTask> iterator = scheduledTasks.iterator();
            while (iterator.hasNext()) {
                ScheduledTask task = iterator.next();
                --task.ticksRemaining;
                if (task.ticksRemaining > 0) continue;
                task.task.run();
                iterator.remove();
            }
            Iterator<ConditionTask> cIter = conditionTasks.iterator();
            while (cIter.hasNext()) {
                ConditionTask task = cIter.next();
                if (!task.condition.getAsBoolean()) continue;
                task.task.run();
                cIter.remove();
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

        ConditionTask(BooleanSupplier condition, Runnable task) {
            this.condition = condition;
            this.task = task;
        }
    }
}
