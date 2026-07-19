/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.mob.HostileEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ShieldItem
 *  net.minecraft.world.World
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.world.World;
import net.minecraft.client.network.ClientPlayerEntity;

public class MacroController {
    /*
     * Enabled aggressive block sorting
     */
    public static ActionResult macroController(ClientPlayerEntity clientPlayer, World world, LivingEntity livingEntity, int highSpeed) {
        block14: {
            StatusType selector;
            block13: {
                block15: {
                    block17: {
                        block16: {
                            selector = StatusType.NONE;
                            if (!world.isClient || !JobManager.checkOrderIsEmpty()) break block14;
                            if (!Config.HOT_SWAP) break block15;
                            if (!Config.SNAPBACK || !MacroController.canSnapback(clientPlayer, livingEntity)) break block16;
                            if (!MacroController.isActiveShield(livingEntity)) {
                                selector = StatusType.AIR_BREACH;
                                JobManager.setOrder(selector, clientPlayer.getInventory().selectedSlot);
                                return ActionResult.FAIL;
                            }
                            selector = StatusType.AIR_SHIELD_BREACH;
                            break block13;
                        }
                        if (!(clientPlayer.getVelocity().getY() > -0.447)) break block17;
                        if (!Config.BREACH_LIMITED || livingEntity instanceof HostileEntity || livingEntity instanceof PlayerEntity) {
                            if (MacroController.isActiveShield(livingEntity)) {
                                selector = StatusType.STUN;
                                break block13;
                            } else if (Config.BREACH_SWAP && (!clientPlayer.isOnGround() || Config.BREACH_ON_GROUND && clientPlayer.isOnGround())) {
                                selector = StatusType.BREACH;
                            }
                        }
                        break block13;
                    }
                    if (MacroController.isActiveShield(livingEntity)) {
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
                        break block13;
                    } else {
                        selector = StatusType.HOT_SWAP;
                    }
                    break block13;
                }
                selector = StatusType.NORMAL;
            }
            if (selector != StatusType.NONE) {
                int slot = JobManager.beforeAxeSlot > -1 ? JobManager.beforeAxeSlot : clientPlayer.getInventory().selectedSlot;
                JobManager.setOrder(selector, slot);
            }
        }
        return ActionResult.PASS;
    }

    public static boolean isActiveShield(LivingEntity livingEntity) {
        return Config.STUN_SLAMMING && livingEntity.isUsingItem() && livingEntity.getActiveItem().getItem() instanceof ShieldItem;
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
        if (clientPlayer.isFallFlying()) {
            return false;
        }
        if (livingEntity.isFallFlying()) {
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
