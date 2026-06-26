/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.player.LocalPlayer
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.player.LocalPlayer;

public class FlappingSuppression {
    private static long lastTimestampA = -1000L;
    private static long lastTimestampB = 0L;
    private static boolean wasFallFlying = false;
    public static int jumpSuppressionCounter = 0;

    public static void tick(LocalPlayer player) {
        if (jumpSuppressionCounter > 0) {
            --jumpSuppressionCounter;
        }
        boolean isFlying = player.isFallFlying();
        if (!wasFallFlying && isFlying) {
            long now = player.tickCount;
            lastTimestampA = lastTimestampB;
            lastTimestampB = now;
        }
        wasFallFlying = isFlying;
    }

    public static boolean isRecentlyFluttering() {
        if (lastTimestampB - lastTimestampA < (long)Config.FLAP_SUPPRESSION_THRESHOLD) {
            FlappingSuppression.reset();
            return true;
        }
        return false;
    }

    private static void reset() {
        lastTimestampA = -1000L;
        lastTimestampB = 0L;
    }
}
