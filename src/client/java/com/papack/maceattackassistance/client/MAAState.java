/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;

public class MAAState {
    private static final int DEFAULT_LEVEL = 1;
    public static int allowedLevels = 1;
    public static boolean antiCheat = false;
    public static boolean f1 = false;
    public static boolean f2 = false;
    public static boolean f3 = false;

    public static void clear() {
        allowedLevels = 1;
        antiCheat = Config.FORCED_DISABLE;
    }

    public static boolean z() {
        // Intended: block features when f1 is false (not on MAA server)
        // Original decompiled code was !f1 && !x() which simplifies to !f1 && f1 = always false (dead code)
        return !f1;
    }

    public static boolean x() {
        return !f1;
    }
}
