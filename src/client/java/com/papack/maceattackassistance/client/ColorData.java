/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 */
package com.papack.maceattackassistance.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ColorData {
    public static final MutableComponent dark_blue = Component.literal((String)(Color.DARK_BLUE.getLabel() + " : " + String.format("%06X", Color.DARK_BLUE.getValue()))).withStyle(ChatFormatting.DARK_BLUE).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent dark_green = Component.literal((String)(Color.DARK_GREEN.getLabel() + " : " + String.format("%06X", Color.DARK_GREEN.getValue()))).withStyle(ChatFormatting.DARK_GREEN).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent dark_aqua = Component.literal((String)(Color.DARK_AQUA.getLabel() + " : " + String.format("%06X", Color.DARK_AQUA.getValue()))).withStyle(ChatFormatting.DARK_AQUA).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent dark_red = Component.literal((String)(Color.DARK_RED.getLabel() + " : " + String.format("%06X", Color.DARK_RED.getValue()))).withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent dark_purple = Component.literal((String)(Color.DARK_PURPLE.getLabel() + " : " + String.format("%06X", Color.DARK_PURPLE.getValue()))).withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent gold = Component.literal((String)(Color.GOLD.getLabel() + " : " + String.format("%06X", Color.GOLD.getValue()))).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent gray = Component.literal((String)(Color.GRAY.getLabel() + " : " + String.format("%06X", Color.GRAY.getValue()))).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent dark_gray = Component.literal((String)(Color.DARK_GRAY.getLabel() + " : " + String.format("%06X", Color.DARK_GRAY.getValue()))).withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent blue = Component.literal((String)(Color.BLUE.getLabel() + " : " + String.format("%06X", Color.BLUE.getValue()))).withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent green = Component.literal((String)(Color.GREEN.getLabel() + " : " + String.format("%06X", Color.GREEN.getValue()))).withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent aqua = Component.literal((String)(Color.AQUA.getLabel() + " : " + String.format("%06X", Color.AQUA.getValue()))).withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent red = Component.literal((String)(Color.RED.getLabel() + " : " + String.format("%06X", Color.RED.getValue()))).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent light_purple = Component.literal((String)(Color.LIGHT_PURPLE.getLabel() + " : " + String.format("%06X", Color.LIGHT_PURPLE.getValue()))).withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent yellow = Component.literal((String)(Color.YELLOW.getLabel() + " : " + String.format("%06X", Color.YELLOW.getValue()))).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD);
    public static final MutableComponent white = Component.literal((String)(Color.WHITE.getLabel() + " : " + String.format("%06X", Color.WHITE.getValue()))).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD);

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
