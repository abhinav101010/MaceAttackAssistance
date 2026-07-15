/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.BlockPos
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.network.ClientPlayerEntity;

public class AimCondition {
    private static boolean flagFallThreshold;

    public static void tick(ClientPlayerEntity clientPlayer) {
        double vy = clientPlayer.getVelocity().getY();
        BlockPos entityPos = clientPlayer.getBlockPos();
        if (!flagFallThreshold && !clientPlayer.isOnGround() && vy < 0.0) {
            int threshold = Config.AIM_FALL_THRESHOLD - 1;
            if (AimCondition.playerCondition(clientPlayer) || Utils.verifyGround(clientPlayer, threshold)) {
                flagFallThreshold = true;
            }
        }
        if (flagFallThreshold && (vy >= 0.0 || clientPlayer.isOnGround())) {
            flagFallThreshold = false;
        }
    }

    public static boolean canAim() {
        return flagFallThreshold;
    }

    private static boolean playerCondition(ClientPlayerEntity clientPlayer) {
        return JobManager.checkStatus(StatusType.DOUBLE_TAP) || clientPlayer.isGliding() || Utils.speedOverThreshold(clientPlayer) > 0;
    }
}
