/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.player.LocalPlayer
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.player.LocalPlayer;

public class AimCondition {
    private static boolean flagFallThreshold;

    public static void tick(LocalPlayer clientPlayer) {
        double vy = clientPlayer.getDeltaMovement().y();
        if (!flagFallThreshold && !clientPlayer.onGround() && vy < 0.0) {
            int threshold = Config.AIM_FALL_THRESHOLD - 1;
            if (AimCondition.playerCondition(clientPlayer) || Utils.verifyGround(clientPlayer, threshold)) {
                flagFallThreshold = true;
            }
        }
        if (flagFallThreshold && (vy >= 0.0 || clientPlayer.onGround())) {
            flagFallThreshold = false;
        }
    }

    public static boolean canAim() {
        return flagFallThreshold;
    }

    private static boolean playerCondition(LocalPlayer clientPlayer) {
        return JobManager.checkStatus(StatusType.DOUBLE_TAP) || clientPlayer.isFallFlying() || Utils.speedOverThreshold(clientPlayer) > 0;
    }
}
