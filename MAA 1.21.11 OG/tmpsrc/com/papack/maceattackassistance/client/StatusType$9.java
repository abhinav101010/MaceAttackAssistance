/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;

final class StatusType.9
extends StatusType {
    @Override
    public void execute(int value) {
        JobManager.manualStun(value);
    }
}
