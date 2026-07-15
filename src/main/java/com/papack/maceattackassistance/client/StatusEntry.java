/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.StatusType;

public class StatusEntry {
    private final StatusType type;
    private final int defaultValue;
    private int value;
    private boolean flag;

    public StatusEntry(StatusType type, int value, int defaultValue, boolean flag) {
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
        this.flag = flag;
    }

    public StatusType getType() {
        return this.type;
    }

    public int getValue() {
        return this.value;
    }

    public int getDefaultValue() {
        return this.defaultValue;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
