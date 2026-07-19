/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client.config;

public static enum Config.SwingToggle {
    L_SHIFT(340),
    L_CTRL(341),
    L_ALT(342),
    R_SHIFT(344),
    R_CTRL(345);

    private final int glfwKey;

    private Config.SwingToggle(int glfwKey) {
        this.glfwKey = glfwKey;
    }

    public int getGlfwKey() {
        return this.glfwKey;
    }
}
