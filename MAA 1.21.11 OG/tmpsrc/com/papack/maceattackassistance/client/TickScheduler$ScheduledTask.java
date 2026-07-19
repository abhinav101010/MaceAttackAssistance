/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

private static class TickScheduler.ScheduledTask {
    int ticksRemaining;
    Runnable task;

    TickScheduler.ScheduledTask(int ticksRemaining, Runnable task) {
        this.ticksRemaining = ticksRemaining;
        this.task = task;
    }
}
