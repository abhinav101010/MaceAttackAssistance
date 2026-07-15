/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Formatting
 *  net.minecraft.Text
 *  net.minecraft.MutableText
 */
package com.papack.maceattackassistance.client;

import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;

public class ColorData {
    public static final MutableText dark_blue = Text.literal((String)(Color.DARK_BLUE.getLabel() + " : " + String.format("%06X", Color.DARK_BLUE.getValue()))).formatted(Formatting.DARK_BLUE).formatted(Formatting.BOLD);
    public static final MutableText dark_green = Text.literal((String)(Color.DARK_GREEN.getLabel() + " : " + String.format("%06X", Color.DARK_GREEN.getValue()))).formatted(Formatting.DARK_GREEN).formatted(Formatting.BOLD);
    public static final MutableText dark_aqua = Text.literal((String)(Color.DARK_AQUA.getLabel() + " : " + String.format("%06X", Color.DARK_AQUA.getValue()))).formatted(Formatting.DARK_AQUA).formatted(Formatting.BOLD);
    public static final MutableText dark_red = Text.literal((String)(Color.DARK_RED.getLabel() + " : " + String.format("%06X", Color.DARK_RED.getValue()))).formatted(Formatting.DARK_RED).formatted(Formatting.BOLD);
    public static final MutableText dark_purple = Text.literal((String)(Color.DARK_PURPLE.getLabel() + " : " + String.format("%06X", Color.DARK_PURPLE.getValue()))).formatted(Formatting.DARK_PURPLE).formatted(Formatting.BOLD);
    public static final MutableText gold = Text.literal((String)(Color.GOLD.getLabel() + " : " + String.format("%06X", Color.GOLD.getValue()))).formatted(Formatting.GOLD).formatted(Formatting.BOLD);
    public static final MutableText gray = Text.literal((String)(Color.GRAY.getLabel() + " : " + String.format("%06X", Color.GRAY.getValue()))).formatted(Formatting.GRAY).formatted(Formatting.BOLD);
    public static final MutableText dark_gray = Text.literal((String)(Color.DARK_GRAY.getLabel() + " : " + String.format("%06X", Color.DARK_GRAY.getValue()))).formatted(Formatting.DARK_GRAY).formatted(Formatting.BOLD);
    public static final MutableText blue = Text.literal((String)(Color.BLUE.getLabel() + " : " + String.format("%06X", Color.BLUE.getValue()))).formatted(Formatting.BLUE).formatted(Formatting.BOLD);
    public static final MutableText green = Text.literal((String)(Color.GREEN.getLabel() + " : " + String.format("%06X", Color.GREEN.getValue()))).formatted(Formatting.GREEN).formatted(Formatting.BOLD);
    public static final MutableText aqua = Text.literal((String)(Color.AQUA.getLabel() + " : " + String.format("%06X", Color.AQUA.getValue()))).formatted(Formatting.AQUA).formatted(Formatting.BOLD);
    public static final MutableText red = Text.literal((String)(Color.RED.getLabel() + " : " + String.format("%06X", Color.RED.getValue()))).formatted(Formatting.RED).formatted(Formatting.BOLD);
    public static final MutableText light_purple = Text.literal((String)(Color.LIGHT_PURPLE.getLabel() + " : " + String.format("%06X", Color.LIGHT_PURPLE.getValue()))).formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.BOLD);
    public static final MutableText yellow = Text.literal((String)(Color.YELLOW.getLabel() + " : " + String.format("%06X", Color.YELLOW.getValue()))).formatted(Formatting.YELLOW).formatted(Formatting.BOLD);
    public static final MutableText white = Text.literal((String)(Color.WHITE.getLabel() + " : " + String.format("%06X", Color.WHITE.getValue()))).formatted(Formatting.WHITE).formatted(Formatting.BOLD);

    public static void initialize() {
    }

    public static enum Color {
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

        private Color(String label, int value) {
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
}
