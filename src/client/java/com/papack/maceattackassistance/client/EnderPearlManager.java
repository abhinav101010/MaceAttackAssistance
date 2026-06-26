/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;

public class EnderPearlManager {
    private static int cooldownTime = 0;

    public static void tick() {
        if (cooldownTime > 0) {
            --cooldownTime;
        }
    }

    public static boolean isCooldown() {
        return cooldownTime > 0;
    }

    public static void startCooldown() {
        cooldownTime = Config.ENDER_PEARL_COOLDOWN_TIME;
    }

    public static float getCooldownProgress() {
        return EnderPearlManager.isCooldown() ? (float)cooldownTime / (float)Config.ENDER_PEARL_COOLDOWN_TIME : 0.0f;
    }
}
