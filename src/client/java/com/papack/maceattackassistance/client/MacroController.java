/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class MacroController {
    /*
     * Enabled aggressive block sorting
     */
    public static InteractionResult macroController(LocalPlayer clientPlayer, Level world, LivingEntity livingEntity, int highSpeed) {
        block17: {
            StatusType selector;
            block16: {
                Minecraft client;
                block20: {
                    block19: {
                        block18: {
                            selector = StatusType.NONE;
                            client = Minecraft.getInstance();
                            if (!world.isClientSide() || !JobManager.checkOrderIsEmpty()) break block17;
                            if (!Config.HOT_SWAP) break block16;
                            if (!Config.SNAPBACK || !MacroController.canSnapback(clientPlayer, livingEntity)) break block18;
                            if (!MacroController.isActiveShield(livingEntity)) {
                                selector = StatusType.BREACH;
                                JobManager.setOrder(selector, clientPlayer.getInventory().getSelectedSlot());
                                return InteractionResult.FAIL;
                            }
                            selector = StatusType.AIR_SHIELD_BREACH;
                            break block16;
                        }
                        if (!(clientPlayer.getDeltaMovement().y() > -0.447)) break block19;
                        if ((!Config.BREACH_LIMITED || livingEntity instanceof Monster || livingEntity instanceof Player) && MacroController.isActiveShield(livingEntity)) {
                            selector = Config.ONE_TICK_STUN_SLAM ? StatusType.ONE_TICK_STUN : StatusType.STUN_SLAM;
                        }
                        break block16;
                    }
                    if (!MacroController.isActiveShield(livingEntity)) break block20;
                    if (StunSlam.isAxe(clientPlayer.getMainHandItem())) {
                        if (Config.ONE_TICK_STUN_SLAM) {
                            selector = StatusType.ONE_TICK_STUN;
                            break block16;
                        } else {
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
                        }
                        break block16;
                    } else {
                        selector = StatusType.NORMAL;
                    }
                    break block16;
                }
                if (Config.DOUBLE_TAP && clientPlayer.isFallFlying()) {
                    boolean isOffHandChestPlate = Utils.compareHandItemsWithTags(clientPlayer, InteractionHand.OFF_HAND, (TagKey<Item>)ItemTags.CHEST_ARMOR);
                    boolean isMainHandSpear = Utils.isSpear(clientPlayer);
                    StatusType type = StatusType.DOUBLE_TAP;
                    if (isOffHandChestPlate) {
                        type = isMainHandSpear ? StatusType.NORMAL : StatusType.DOUBLE_TAP_OFF_HAND;
                    }
                    selector = client.options.keyUse.isDown() ? StatusType.DOUBLE_TAP_ELYTRA_SPEAR : type;
                } else {
                    selector = StatusType.HOT_SWAP;
                }
            }
            if (MAAState.x() && livingEntity instanceof Player) {
                selector = StatusType.NONE;
            }
            if (selector != StatusType.NONE) {
                int slot = JobManager.beforeAxeSlot > -1 ? JobManager.beforeAxeSlot : clientPlayer.getInventory().getSelectedSlot();
                JobManager.setOrder(selector, slot);
            }
        }
        return InteractionResult.PASS;
    }

    public static boolean isActiveShield(LivingEntity livingEntity) {
        return Config.STUN_SLAMMING && livingEntity.isBlocking();
    }

    public static boolean canSnapback(LocalPlayer clientPlayer, LivingEntity livingEntity) {
        if (HotSwap.getBreachMaceSlotId(clientPlayer) < 0) {
            return false;
        }
        if (livingEntity.onGround()) {
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
        double playerYV = clientPlayer.getDeltaMovement().y();
        double targetYV = livingEntity.getDeltaMovement().y();
        if (playerYV > 0.0) {
            return false;
        }
        if (playerYV < Config.VELOCITY_BY_DISTANCE[13]) {
            return false;
        }
        return targetYV < playerYV + (double)Config.SNAPBACK_TOLERANCE * 0.01;
    }
}
