/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

public static enum ColorData.Color {
    DARK_BLUE("DARK BLUE", 170),
    DARK_GREEN("DARK GREEN", 43520),
    DARK_AQUA("DARK AQUA", 43690),
    DARK_RED("DARK RED", 0xAA0000),
    DARK_PURPLE("DARK PURPLE", 0xAA00AA),
    GOLD("GOLD", 0xFFAA00),
    GRAY("GRAY", 0xAAAAAA),
    DARK_GRAY("DARK GRAY", 0x555555),
    BLUE("BLUE", 0x5555FF),
    GREEN("GREEN", 0x55FF55),
    AQUA("AQUA", 0x55FFFF),
    RED("RED", 0xFF5555),
    LIGHT_PURPLE("LIGHT PURPLE", 0xFF55FF),
    YELLOW("YELLOW", 0xFFFF55),
    WHITE("WHITE", 0xFFFFFF);

    private final int value;
    private final String label;

    private ColorData.Color(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public int getValue() {
        return this.value;
    }
}
