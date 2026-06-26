/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.tags.ItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.ItemAttributeModifiers
 *  net.minecraft.world.item.component.ItemAttributeModifiers$Entry
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyMappingInvoker;
import com.papack.maceattackassistance.mixin.MinecraftInvoker;
import com.papack.maceattackassistance.mixin.MultiPlayerGameModeInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SpearAttacks {
    public static void spearAssist(Minecraft client, LocalPlayer clientPlayer) {
        if (MAAState.z()) {
            return;
        }
        if (client.options.keyAttack.isDown()) {
            boolean isThereTarget;
            int currentSlot = clientPlayer.getInventory().getSelectedSlot();
            int weaponSlot = -1;
            boolean isFalling = clientPlayer.getDeltaMovement().y() < -0.447;
            Entity target = client.crosshairPickEntity;
            // Fix: Use spear-specific range for target detection
            boolean bl = isThereTarget = Utils.getLivingEntityInView(clientPlayer, 0.0, Utils.getRangeSpear()) != null || target != null || !Config.WEAPON_SWING;
            if (MAAState.antiCheat) {
                return;
            }
            if (MAAState.x() && target instanceof Player) {
                return;
            }
            if (Utils.isSpear(clientPlayer) && isThereTarget) {
                if (isFalling) {
                    if (target != null) {
                        weaponSlot = HotSwap.getPrimaryMaceSlotId(clientPlayer);
                    }
                } else if (target != null && clientPlayer.distanceTo(target) <= Utils.getRangeSpear() && !client.options.keySprint.isDown()) {
                    // Fix: Use spear-specific range and check for swords/maces
                    weaponSlot = Utils.findItemInHotbarByTags((TagKey<Item>)ItemTags.SWORDS);
                }
            } else if (!isFalling && Utils.getLivingEntityInView(clientPlayer, 3.0, Utils.getRangeSpear()) != null) {
                SpearAttacks.autoLungeAttack(clientPlayer);
            }
            if (weaponSlot > -1) {
                clientPlayer.getInventory().setSelectedSlot(weaponSlot);
                if (currentSlot != weaponSlot && Config.RETURN_TO_PREV_SLOT) {
                    PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
                }
            }
        }
    }

    public static void manualLungeAttack(Minecraft client, LocalPlayer clientPlayer, int prevSlot) {
        // Fix: Lower attack strength threshold for more responsive lunges
        if (!Utils.waitingToAttack(clientPlayer, 0.9f)) {
            return;
        }
        int currentSlot = prevSlot < 0 ? clientPlayer.getInventory().getSelectedSlot() : prevSlot;
        int spearSlot = Utils.findItemInHotbarByTags((TagKey<Item>)ItemTags.SPEARS);
        if (spearSlot > -1) {
            clientPlayer.getInventory().setSelectedSlot(spearSlot);
            // Sync slot with server
            if (client.gameMode != null) {
                ((MultiPlayerGameModeInvoker)client.gameMode).syncSelectedSlotInvoker();
            }
            ((MinecraftInvoker)client).doAttackInvoker();
            client.execute(() -> {
                if (currentSlot != spearSlot && Config.RETURN_TO_PREV_SLOT) {
                    PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
                }
            });
        }
    }

    public static void manualLungeAttackHandler(Minecraft client, LocalPlayer clientPlayer) {
        if (!TickScheduler.hasPendingConditionTasks()) {
            int currentSlot = clientPlayer.getInventory().getSelectedSlot();
            // Fix: Lower attack strength threshold for more responsive lunges
            if (!Config.FORCED_EXECUTION_SLOT && Utils.waitingToAttack(clientPlayer, 0.9f)) {
                if (Utils.isSpear(clientPlayer)) {
                    KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyAttack).accessorBoundKey());
                } else {
                    SpearAttacks.manualLungeAttack(client, clientPlayer, currentSlot);
                }
            } else {
                if (Config.LUNGE_EXECUTION_SLOT > -1) {
                    if (currentSlot != Config.LUNGE_EXECUTION_SLOT) {
                        clientPlayer.getInventory().setSelectedSlot(Config.LUNGE_EXECUTION_SLOT);
                        if (Config.KEEP_AFTER_EXECUTION) {
                            currentSlot = Config.LUNGE_EXECUTION_SLOT;
                        }
                    }
                } else {
                    int fastestSlot = SpearAttacks.getFastestAttackSpeedSlot();
                    if (fastestSlot > -1) {
                        if (fastestSlot != currentSlot) {
                            clientPlayer.getInventory().setSelectedSlot(fastestSlot);
                        }
                        if (Config.KEEP_AFTER_EXECUTION) {
                            currentSlot = fastestSlot;
                        }
                    }
                }
                int finalCurrentSlot = currentSlot;
                // Fix: Lower attack strength threshold for more responsive lunges
                TickScheduler.setConditionTask(() -> Utils.waitingToAttack(clientPlayer, 0.9f), () -> SpearAttacks.manualLungeAttack(client, clientPlayer, finalCurrentSlot));
            }
        }
    }

    public static void autoLungeAttack(LocalPlayer clientPlayer) {
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        int spearSlot = Utils.findItemInHotbarByTags((TagKey<Item>)ItemTags.SPEARS);
        if (spearSlot > -1) {
            clientPlayer.getInventory().setSelectedSlot(spearSlot);
            // Sync slot with server
            Minecraft client = Minecraft.getInstance();
            if (client.gameMode != null) {
                ((MultiPlayerGameModeInvoker)client.gameMode).syncSelectedSlotInvoker();
            }
            if (currentSlot != spearSlot && Config.RETURN_TO_PREV_SLOT) {
                PrevSlotManager.setPrevSlot(StatusType.NONE, currentSlot, 2);
            }
        }
    }

    public static int getFastestAttackSpeedSlot() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return -1;
        }
        int currentSelectedSlot = player.getInventory().getSelectedSlot();
        double maxSpeed = -1.0;
        int bestSlot = currentSelectedSlot;
        double baseSpeed = player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            double currentStackSpeed = baseSpeed;
            if (!stack.isEmpty()) {
                ItemAttributeModifiers modifiers = (ItemAttributeModifiers)stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
                if (modifiers == null) {
                    modifiers = (ItemAttributeModifiers)stack.getItem().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, (Object)ItemAttributeModifiers.EMPTY);
                }
                for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                    if (!entry.attribute().equals((Object)Attributes.ATTACK_SPEED) || !entry.slot().test(EquipmentSlot.MAINHAND)) continue;
                    currentStackSpeed += entry.modifier().amount();
                }
            }
            if (i == currentSelectedSlot) {
                if (!(currentStackSpeed >= maxSpeed)) continue;
                maxSpeed = currentStackSpeed;
                bestSlot = i;
                continue;
            }
            if (!(currentStackSpeed > maxSpeed)) continue;
            maxSpeed = currentStackSpeed;
            bestSlot = i;
        }
        return bestSlot;
    }
}
