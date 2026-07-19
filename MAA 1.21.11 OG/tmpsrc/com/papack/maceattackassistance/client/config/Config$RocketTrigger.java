/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client.config;

public static enum Config.RocketTrigger {
    L_SHIFT(340),
    L_CTRL(341),
    L_ALT(342),
    SPACE(32),
    MOUSE_R(1);

    private final int glfwKey;

    private Config.RocketTrigger(int glfwKey) {
        this.glfwKey = glfwKey;
    }

    public int getGlfwKey() {
        return this.glfwKey;
    }
}
