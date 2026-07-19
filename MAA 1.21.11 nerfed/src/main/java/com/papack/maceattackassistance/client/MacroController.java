/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ActionResult
 *  net.minecraft.LivingEntity
 *  net.minecraft.HostileEntity
 *  net.minecraft.PlayerEntity
 *  net.minecraft.World
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.client.network.ClientPlayerEntity;

public class MacroController {
    /*
     * Enabled aggressive block sorting
     */
    public static ActionResult macroController(ClientPlayerEntity clientPlayer, World world, LivingEntity livingEntity, int highSpeed) {
        StatusType selector = StatusType.NONE;
        if (world.isClient() && JobManager.checkOrderIsEmpty()) {
            if (Config.HOT_SWAP) {
                if (Config.SNAPBACK && MacroController.canSnapback(clientPlayer, livingEntity)) {
                    if (!MacroController.isActiveShield(livingEntity)) {
                        selector = StatusType.AIR_BREACH;
                        JobManager.setOrder(selector, clientPlayer.getInventory().getSelectedSlot());
                        return ActionResult.FAIL;
                    }
                    selector = StatusType.AIR_SHIELD_BREACH;
                } else if (clientPlayer.getVelocity().getY() > -0.447) {
                    if ((!Config.BREACH_LIMITED || livingEntity instanceof HostileEntity || livingEntity instanceof PlayerEntity) && MacroController.isActiveShield(livingEntity)) {
                        selector = StatusType.STUN_SLAM;
                    }
                } else if (MacroController.isActiveShield(livingEntity)) {
                    if (StunSlam.isAxe(clientPlayer.getMainHandStack())) {
                        switch (highSpeed) {
                            case 2: {
                                selector = StatusType.VERY_HIGH_SPEED;
                                break;
                            }
                            case 1: {
                                selector = StatusType.HIGH_SPEED;
                                break;
                            }
                            case 0: {
                                selector = StatusType.STUN_SLAM;
                                break;
                            }
                        }
                    } else {
                        selector = StatusType.NORMAL;
                    }
                } else {
                    selector = Config.DOUBLE_TAP && clientPlayer.isGliding() ? StatusType.DOUBLE_TAP : StatusType.HOT_SWAP;
                }
            }
            if (selector != StatusType.NONE) {
                int slot = JobManager.beforeAxeSlot > -1 ? JobManager.beforeAxeSlot : clientPlayer.getInventory().getSelectedSlot();
                JobManager.setOrder(selector, slot);
            }
        }
        return ActionResult.PASS;
    }

    public static boolean isActiveShield(LivingEntity livingEntity) {
        return Config.STUN_SLAMMING && livingEntity.isBlocking();
    }

    public static boolean canSnapback(ClientPlayerEntity clientPlayer, LivingEntity livingEntity) {
        if (HotSwap.getBreachMaceSlotId(clientPlayer) < 0) {
            return false;
        }
        if (livingEntity.isOnGround()) {
            return false;
        }
        if (!Utils.verifyGround(clientPlayer, Config.SNAPBACK_THRESHOLD)) {
            return false;
        }
        if (!Utils.verifyGround(livingEntity, 2)) {
            return false;
        }
        if (clientPlayer.isGliding()) {
            return false;
        }
        if (livingEntity.isGliding()) {
            return false;
        }
        double playerYV = clientPlayer.getVelocity().getY();
        double targetYV = livingEntity.getVelocity().getY();
        if (playerYV > 0.0) {
            return false;
        }
        if (playerYV < Config.VELOCITY_BY_DISTANCE[13]) {
            return false;
        }
        return targetYV < playerYV + (double)Config.SNAPBACK_TOLERANCE * 0.01;
    }
}
