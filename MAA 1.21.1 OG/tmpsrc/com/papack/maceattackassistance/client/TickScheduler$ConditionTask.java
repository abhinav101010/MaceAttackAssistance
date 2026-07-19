/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import java.util.function.BooleanSupplier;

private static class TickScheduler.ConditionTask {
    BooleanSupplier condition;
    Runnable task;

    TickScheduler.ConditionTask(BooleanSupplier condition, Runnable task) {
        this.condition = condition;
        this.task = task;
    }
}
