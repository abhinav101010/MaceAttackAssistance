/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Key
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.MultiPlayerGameMode
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.EnderpearlItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.WindChargeItem
 */
package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.papack.maceattackassistance.client.AirPot;
import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.EnderPearlManager;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PearlCatch;
import com.papack.maceattackassistance.client.PearlGrapple;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.RocketBlitz;
import com.papack.maceattackassistance.client.ScheduleKey;
import com.papack.maceattackassistance.client.StatusEntry;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ToggleElytra;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyMappingInvoker;
import com.papack.maceattackassistance.mixin.MinecraftInvoker;
import com.papack.maceattackassistance.mixin.MultiPlayerGameModeInvoker;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WindChargeItem;

public class JobManager {
    private static final int ENDER_PEARL_BASE_TIME = 60;
    private static Entity grappleTarget;
    private static EnderPearlData enderPearlData;
    private static final Minecraft client;
    private static int BOOST_JUMP;
    private static int BOOST_THROW;
    private static InputConstants.Key attackKey;
    private static InputConstants.Key axeKey;
    private static InputConstants.Key maceKey;
    private static int maceSlot;
    private static int axeSlotId;
    private static int maceSlotId;
    public static int beforeAxeSlot;
    private static int rewearReturnSlot;
    private static int preCheckCount;
    private static int LOCKED;
    private static final Map<StatusType, StatusEntry> statusMap;

    public static void init() {
        statusMap.put(StatusType.VERY_HIGH_SPEED, new StatusEntry(StatusType.VERY_HIGH_SPEED, 5, 5, false));
        statusMap.put(StatusType.HIGH_SPEED, new StatusEntry(StatusType.HIGH_SPEED, 5, 5, false));
        statusMap.put(StatusType.STUN_SLAM, new StatusEntry(StatusType.STUN_SLAM, 5, 5, false));
        statusMap.put(StatusType.HOT_SWAP, new StatusEntry(StatusType.HOT_SWAP, 5, 5, false));
        statusMap.put(StatusType.BREACH, new StatusEntry(StatusType.BREACH, 5, 5, false));
        statusMap.put(StatusType.AIR_BREACH, new StatusEntry(StatusType.AIR_BREACH, 5, 5, false));
        statusMap.put(StatusType.AIR_SHIELD_BREACH, new StatusEntry(StatusType.AIR_SHIELD_BREACH, 5, 5, false));
        statusMap.put(StatusType.STUN, new StatusEntry(StatusType.STUN, 5, 5, false));
        statusMap.put(StatusType.MANUAL_STUN, new StatusEntry(StatusType.MANUAL_STUN, 5, 5, false));
        statusMap.put(StatusType.MANUAL_STUN_HIGH, new StatusEntry(StatusType.MANUAL_STUN_HIGH, 5, 5, false));
        statusMap.put(StatusType.MANUAL_BREACH, new StatusEntry(StatusType.MANUAL_BREACH, 5, 5, false));
        statusMap.put(StatusType.NORMAL, new StatusEntry(StatusType.NORMAL, 5, 5, false));
        statusMap.put(StatusType.NONE, new StatusEntry(StatusType.NONE, 5, 5, false));
        statusMap.put(StatusType.ENDER_PEARL, new StatusEntry(StatusType.ENDER_PEARL, 5, 5, false));
        statusMap.put(StatusType.ROCKET, new StatusEntry(StatusType.ROCKET, 5, 5, false));
        statusMap.put(StatusType.TRIGGER_KEY_ROCKET, new StatusEntry(StatusType.TRIGGER_KEY_ROCKET, 5, 5, false));
        statusMap.put(StatusType.WIND_CHARGE, new StatusEntry(StatusType.WIND_CHARGE, 5, 5, false));
        statusMap.put(StatusType.PRE_AXE, new StatusEntry(StatusType.PRE_AXE, 1, 1, false));
        statusMap.put(StatusType.ELYTRA_BOOST, new StatusEntry(StatusType.ELYTRA_BOOST, 5, 5, false));
        statusMap.put(StatusType.DOUBLE_TAP, new StatusEntry(StatusType.DOUBLE_TAP, 5, 5, false));
        statusMap.put(StatusType.DOUBLE_TAP_ELYTRA_SPEAR, new StatusEntry(StatusType.DOUBLE_TAP_ELYTRA_SPEAR, 5, 5, false));
        statusMap.put(StatusType.DOUBLE_TAP_OFF_HAND, new StatusEntry(StatusType.DOUBLE_TAP_OFF_HAND, 5, 5, false));
        statusMap.put(StatusType.ELYTRA_MANUAL_SWITCH_MODE, new StatusEntry(StatusType.ELYTRA_MANUAL_SWITCH_MODE, 5, 5, false));
        statusMap.put(StatusType.AUTO_REFILL, new StatusEntry(StatusType.AUTO_REFILL, 3, 3, false));
        statusMap.put(StatusType.PEARL_CATCH, new StatusEntry(StatusType.PEARL_CATCH, 8, 8, false));
        statusMap.put(StatusType.INSTANT_PEARL_CATCH, new StatusEntry(StatusType.INSTANT_PEARL_CATCH, 8, 8, false));
        statusMap.put(StatusType.PEARL_CATCH_DOWNWARD, new StatusEntry(StatusType.PEARL_CATCH_DOWNWARD, 8, 8, false));
        statusMap.put(StatusType.ONE_TICK_STUN, new StatusEntry(StatusType.ONE_TICK_STUN, 5, 5, false));
        statusMap.put(StatusType.PEARL_GRAPPLE, new StatusEntry(StatusType.PEARL_GRAPPLE, 60, 60, false));
        statusMap.put(StatusType.AUTO_SHIELD_DRAINING, new StatusEntry(StatusType.AUTO_SHIELD_DRAINING, 5, 5, false));
        statusMap.put(StatusType.AIR_POT, new StatusEntry(StatusType.AIR_POT, 5, 5, false));
    }

    public static void setOrder(StatusType statusType, int slot) {
        StatusEntry statusEntry = statusMap.get((Object)statusType);
        if (!statusEntry.getFlag()) {
            if (Config.DEBUG_SCREEN) {
                MaceAttackAssistanceClient.LOGGER.info("[Job] {}", (Object)statusType);
            }
            statusMap.get((Object)statusType).setFlag(true);
            JobManager.setPreviousSlot(statusType, slot);
            if ((statusType == StatusType.BREACH || statusType == StatusType.HOT_SWAP) && JobManager.client.player != null) {
                JobManager.setKeys(JobManager.client.player, statusType, false);
            }
        }
    }

    public static boolean checkOrderIsEmpty() {
        return preCheckCount == 0;
    }

    public static void setLock(int tick) {
        LOCKED = tick;
    }

    public static boolean checkStatus(StatusType statusType) {
        return statusMap.get((Object)statusType).getFlag();
    }

    public static boolean checkAttackStatus() {
        return JobManager.checkStatus(StatusType.DOUBLE_TAP) || JobManager.checkStatus(StatusType.DOUBLE_TAP_ELYTRA_SPEAR) || JobManager.checkStatus(StatusType.DOUBLE_TAP_OFF_HAND) || JobManager.checkStatus(StatusType.HIGH_SPEED) || JobManager.checkStatus(StatusType.VERY_HIGH_SPEED) || JobManager.checkStatus(StatusType.STUN_SLAM) || JobManager.checkStatus(StatusType.MANUAL_STUN) || JobManager.checkStatus(StatusType.ONE_TICK_STUN) || JobManager.checkStatus(StatusType.AUTO_SHIELD_DRAINING);
    }

    public static int checkValue(StatusType statusType) {
        return statusMap.get((Object)statusType).getValue();
    }

    public static void setValue(StatusType statusType, int value) {
        statusMap.get((Object)statusType).setValue(value);
    }

    public static void setPreviousSlot(StatusType statusType, int slot) {
        PrevSlotManager.setPrevSlot(statusType, slot, switch (statusType) {
            case StatusType.ELYTRA_MANUAL_SWITCH_MODE -> 1;
            case StatusType.ENDER_PEARL, StatusType.WIND_CHARGE, StatusType.PEARL_GRAPPLE -> 2;
            case StatusType.MANUAL_STUN, StatusType.MANUAL_STUN_HIGH, StatusType.AUTO_SHIELD_DRAINING, StatusType.AIR_POT, StatusType.PEARL_CATCH_DOWNWARD -> 3;
            case StatusType.PEARL_CATCH, StatusType.INSTANT_PEARL_CATCH -> 3 + Config.PEARL_CATCH_DELAY;
            default -> 0;
        });
    }

    public static void tick(Minecraft client, LocalPlayer clientPlayer) {
        if (LOCKED == 0) {
            int checkCount = 0;
            for (StatusEntry entry : statusMap.values()) {
                if (!entry.getFlag()) continue;
                JobManager.setKeys(clientPlayer, entry.getType(), true);
                entry.getType().execute(entry.getValue());
                ++checkCount;
            }
            if (preCheckCount == 0 && checkCount == 0 && JobManager.keyReleased(client)) {
                beforeAxeSlot = -1;
            }
            preCheckCount = checkCount;
        } else {
            --LOCKED;
        }
    }

    public static void setKeys(LocalPlayer clientPlayer, StatusType selector, boolean shouldSetOrder) {
        int prevSlotId = clientPlayer.getInventory().getSelectedSlot();
        if (prevSlotId > -1) {
            attackKey = ((KeyMappingInvoker)JobManager.client.options.keyAttack).accessorBoundKey();
            axeSlotId = -1;
            maceSlotId = -1;
            if (!MAAState.z()) {
                switch (selector) {
                    case AUTO_SHIELD_DRAINING: 
                    case VERY_HIGH_SPEED: 
                    case HIGH_SPEED: 
                    case STUN_SLAM: 
                    case HOT_SWAP: 
                    case DOUBLE_TAP: 
                    case DOUBLE_TAP_ELYTRA_SPEAR: 
                    case DOUBLE_TAP_OFF_HAND: {
                        maceSlotId = HotSwap.getPrimaryMaceSlotId(clientPlayer);
                        break;
                    }
                    case BREACH: 
                    case AIR_BREACH: {
                        maceSlotId = HotSwap.getBreachMaceSlotId(clientPlayer);
                        break;
                    }
                    case AIR_SHIELD_BREACH: 
                    case MANUAL_BREACH: {
                        maceSlotId = HotSwap.getBreachMaceSlotId(clientPlayer);
                        axeSlotId = JobManager.getAxeSlotId(clientPlayer);
                        break;
                    }
                    case STUN: {
                        axeSlotId = JobManager.getAxeSlotId(clientPlayer);
                        break;
                    }
                    case MANUAL_STUN: 
                    case MANUAL_STUN_HIGH: {
                        maceSlotId = HotSwap.getPrimaryMaceSlotId(clientPlayer);
                        axeSlotId = JobManager.getAxeSlotId(clientPlayer);
                        double yv = clientPlayer.getDeltaMovement().y;
                        if (Config.DEBUG_SCREEN) {
                            MaceAttackAssistanceClient.LOGGER.info("yv : {}", (Object)yv);
                        }
                        if (axeSlotId != -1 && (!Config.INSTANT_SHIELD_DRAINING || !JobManager.client.options.keySprint.isDown())) break;
                        axeSlotId = maceSlotId;
                    }
                }
            }
            if (maceSlotId < 0) {
                maceSlotId = prevSlotId;
            }
            if (axeSlotId < 0) {
                axeSlotId = prevSlotId;
            }
            maceSlot = maceSlotId;
            axeKey = ((KeyMappingInvoker)JobManager.client.options.keyHotbarSlots[axeSlotId]).accessorBoundKey();
            maceKey = ((KeyMappingInvoker)JobManager.client.options.keyHotbarSlots[maceSlotId]).accessorBoundKey();
            if (selector == StatusType.BREACH || selector == StatusType.HOT_SWAP) {
                clientPlayer.getInventory().setSelectedSlot(maceSlotId);
                if (JobManager.client.gameMode != null) {
                    ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
                }
                if (shouldSetOrder && JobManager.checkOrderIsEmpty()) {
                    JobManager.setOrder(selector, prevSlotId);
                }
            }
        }
    }

    public static int getAxeSlotId(LocalPlayer clientPlayer) {
        int beforeSlot = clientPlayer.getInventory().getSelectedSlot();
        int hotBarSlotId = StunSlam.getAxeSlotId(clientPlayer);
        if (hotBarSlotId > -1 && hotBarSlotId != beforeSlot) {
            beforeAxeSlot = beforeSlot;
        }
        return hotBarSlotId;
    }

    private static void decValueAndSetFlag(StatusType statusType, int value) {
        if (value > 0) {
            if (--value < 1) {
                statusMap.get((Object)statusType).setFlag(false);
                value = statusMap.get((Object)statusType).getDefaultValue();
            }
            statusMap.get((Object)statusType).setValue(value);
        }
    }

    public static boolean keyReleased(Minecraft client) {
        boolean flag = false;
        if (client.player != null) {
            ItemStack itemStack = client.player.getMainHandItem();
            flag = !JobManager.checkStatus(StatusType.DOUBLE_TAP) && !JobManager.checkStatus(StatusType.DOUBLE_TAP_ELYTRA_SPEAR) && (ToggleElytra.isElytra(itemStack) || ToggleElytra.isChestPlate(itemStack) || itemStack.is(Items.FIREWORK_ROCKET) || itemStack.is(Items.WIND_CHARGE) || itemStack.is(Items.ENDER_PEARL));
        }
        boolean attackKey = client.options.keyAttack.isDown();
        boolean useKey = client.options.keyUse.isDown();
        return (flag || !attackKey) && !useKey;
    }

    private static EnderPearlData findEnderPearl(LocalPlayer clientPlayer, boolean isCooldown) {
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        Inventory inventory = clientPlayer.getInventory();
        for (int i = 0; i < 9; ++i) {
            boolean flag;
            ItemStack itemStack = inventory.getItem(i);
            boolean bl = flag = isCooldown ? itemStack.is(Items.WIND_CHARGE) : itemStack.is(Items.ENDER_PEARL);
            if (!flag || clientPlayer.getCooldowns().isOnCooldown(itemStack)) continue;
            return new EnderPearlData(true, currentSlot, i);
        }
        return new EnderPearlData(false, currentSlot, currentSlot);
    }

    public static void hotSwap(int value) {
        JobManager.decValueAndSetFlag(StatusType.HOT_SWAP, value);
    }

    public static void breach(int value) {
        if (value == 5) {
            value = 1;
        }
        JobManager.decValueAndSetFlag(StatusType.BREACH, value);
    }

    public static void airBreach(int value) {
        switch (value) {
            case 4: {
                KeyMapping.click((InputConstants.Key)maceKey);
                break;
            }
            case 2: {
                JobManager.doAttack();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.AIR_BREACH, value);
    }

    public static void veryHighSpeed(int value) {
        if (value == 4) {
            JobManager.selectHotbarSlot(maceSlot);
            JobManager.doAttack();
        }
        if (value == 2) {
            JobManager.setLock(1);
        }
        JobManager.decValueAndSetFlag(StatusType.VERY_HIGH_SPEED, value);
    }

    public static void highSpeed(int value) {
        if (value == Config.STUN_HIGH) {
            JobManager.selectHotbarSlot(maceSlot);
            KeyMapping.click((InputConstants.Key)attackKey);
        }
        if (value == 2) {
            JobManager.setLock(1);
        }
        JobManager.decValueAndSetFlag(StatusType.HIGH_SPEED, value);
    }

    public static void stunSlam(int value) {
        if (value == Config.STUN_LOW) {
            JobManager.selectHotbarSlot(maceSlot);
            KeyMapping.click((InputConstants.Key)attackKey);
        }
        if (value == 2) {
            JobManager.setLock(1);
        }
        JobManager.decValueAndSetFlag(StatusType.STUN_SLAM, value);
    }

    public static void oneTickStunSlum(int value) {
        if (value == 5) {
            MaceAttackAssistanceClient.flagStunSlam = true;
        }
        if (value == 4) {
            MaceAttackAssistanceClient.flagStunSlam = false;
            JobManager.setLock(3);
        }
        JobManager.decValueAndSetFlag(StatusType.ONE_TICK_STUN, value);
    }

    public static void airShieldBreach(int value) {
        switch (value) {
            case 5: {
                KeyMapping.click((InputConstants.Key)axeKey);
                break;
            }
            case 4: {
                JobManager.doAttack();
                KeyMapping.click((InputConstants.Key)maceKey);
                break;
            }
            case 2: {
                JobManager.doAttack();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.AIR_SHIELD_BREACH, value);
    }

    public static void stun(int value) {
        if (value == 5) {
            KeyMapping.click((InputConstants.Key)axeKey);
            KeyMapping.click((InputConstants.Key)attackKey);
        }
        JobManager.decValueAndSetFlag(StatusType.STUN, value);
    }

    public static void manualStun(int value) {
        switch (value) {
            case 5: {
                JobManager.selectHotbarSlot(axeSlotId);
                KeyMapping.click((InputConstants.Key)attackKey);
                KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)true);
                if (axeSlotId != maceSlot && axeSlotId >= 0) break;
                value = 2;
                break;
            }
            case 4: {
                JobManager.selectHotbarSlot(maceSlotId);
                KeyMapping.click((InputConstants.Key)attackKey);
                break;
            }
            case 2: {
                KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)false);
            }
        }
        JobManager.decValueAndSetFlag(StatusType.MANUAL_STUN, value);
    }

    public static void autoShieldDraining(int value) {
        if (value == 5) {
            JobManager.selectHotbarSlot(maceSlot);
            KeyMapping.click((InputConstants.Key)attackKey);
            value = 2;
        }
        JobManager.decValueAndSetFlag(StatusType.AUTO_SHIELD_DRAINING, value);
    }

    public static void manualStunHighSpeed(int value) {
        switch (value) {
            case 5: {
                JobManager.selectHotbarSlot(axeSlotId);
                JobManager.doAttack();
                break;
            }
            case 4: {
                JobManager.selectHotbarSlot(maceSlotId);
                JobManager.doAttack();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.MANUAL_STUN_HIGH, value);
    }

    public static void manualBreach(int value) {
        if (value == 4) {
            KeyMapping.click((InputConstants.Key)maceKey);
            KeyMapping.click((InputConstants.Key)attackKey);
        }
        JobManager.decValueAndSetFlag(StatusType.MANUAL_BREACH, value);
    }

    public static void autoPearlCatch(int value) {
        PearlCatch.autoPearlCatch(value);
        JobManager.decValueAndSetFlag(StatusType.PEARL_CATCH, value);
    }

    public static void instantPearlCatch(int value) {
        PearlCatch.instantPearlCatch(value);
        JobManager.decValueAndSetFlag(StatusType.INSTANT_PEARL_CATCH, value);
    }

    public static void pearlCatchDownward(int value) {
        PearlCatch.downwardPearlCatch(value);
        JobManager.decValueAndSetFlag(StatusType.PEARL_CATCH_DOWNWARD, value);
    }

    public static void manualPearlCatch(int value) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer != null) {
            switch (value) {
                case 5: {
                    boolean isCooldown = EnderPearlManager.isCooldown();
                    enderPearlData = JobManager.findEnderPearl(clientPlayer, isCooldown);
                    if (JobManager.enderPearlData.canThrow) {
                        KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyHotbarSlots[JobManager.enderPearlData.newSlot]).accessorBoundKey());
                        if (isCooldown) break;
                        EnderPearlManager.startCooldown();
                        break;
                    }
                    value = 1;
                    break;
                }
                case 4: {
                    KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)client.options.keyUse).accessorBoundKey());
                    enderPearlData = new EnderPearlData(false, JobManager.enderPearlData.previousSlot, JobManager.enderPearlData.previousSlot);
                    JobManager.setLock(2);
                    value = 1;
                }
            }
        }
        JobManager.decValueAndSetFlag(StatusType.ENDER_PEARL, value);
    }

    public static void windCharge(int value) {
        int chargeSlot;
        LocalPlayer clientPlayer = JobManager.client.player;
        if (clientPlayer != null && value == 5 && (chargeSlot = Utils.findItemInHotbar(WindChargeItem.class, true)) > -1 && chargeSlot < 9) {
            clientPlayer.getInventory().setSelectedSlot(chargeSlot);
            if (JobManager.client.gameMode != null) {
                ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
            }
            KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)JobManager.client.options.keyUse).accessorBoundKey());
            value = 1;
        }
        JobManager.decValueAndSetFlag(StatusType.WIND_CHARGE, value);
    }

    public static void rocket(int value) {
        if (value == 5) {
            value = 1;
        }
        JobManager.decValueAndSetFlag(StatusType.ROCKET, value);
    }

    public static void triggerKeyRocket(int value) {
        LocalPlayer clientPlayer = JobManager.client.player;
        if (clientPlayer != null && value == 5) {
            int currentSlot = clientPlayer.getInventory().getSelectedSlot();
            int slot = RocketBlitz.getRocketSlotId(clientPlayer);
            if (slot > -1) {
                if (Utils.isSpear(clientPlayer)) {
                    boolean useKeyStatus = JobManager.client.options.keyUse.isDown();
                    KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)JobManager.client.options.keyUse).accessorBoundKey(), (boolean)false);
                    TickScheduler.setDelayTask(0, () -> {
                        clientPlayer.getInventory().setSelectedSlot(slot);
                        if (JobManager.client.gameMode != null) {
                            ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
                        }
                        KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)JobManager.client.options.keyUse).accessorBoundKey());
                        TickScheduler.setDelayTask(0, () -> {
                            clientPlayer.getInventory().setSelectedSlot(currentSlot);
                            if (JobManager.client.gameMode != null) {
                                ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
                            }
                        });
                        TickScheduler.setDelayTask(1, () -> JobManager.client.options.keyUse.setDown(useKeyStatus));
                    });
                    value = 2;
                } else {
                    clientPlayer.getInventory().setSelectedSlot(slot);
                    if (JobManager.client.gameMode != null) {
                        ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
                    }
                    KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)JobManager.client.options.keyUse).accessorBoundKey());
                    value = 1;
                    TickScheduler.setDelayTask(0, () -> {
                        clientPlayer.getInventory().setSelectedSlot(currentSlot);
                        if (JobManager.client.gameMode != null) {
                            ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
                        }
                    });
                }
            }
        }
        JobManager.decValueAndSetFlag(StatusType.TRIGGER_KEY_ROCKET, value);
    }

    public static void preAxe(int value) {
        JobManager.decValueAndSetFlag(StatusType.PRE_AXE, value);
    }

    public static void normal(int value) {
        if (value == 4) {
            MaceAttackAssistanceClient.setAttackInterval();
        }
        JobManager.decValueAndSetFlag(StatusType.NORMAL, value);
    }

    public static void none() {
        int value = 1;
        JobManager.decValueAndSetFlag(StatusType.NONE, value);
    }

    public static void elytraBoost(int value) {
        LocalPlayer clientPlayer = JobManager.client.player;
        if (clientPlayer != null) {
            if (value == BOOST_THROW && !ElytraBoost.flag_elytra_boost) {
                if (Utils.getHandHoldingWindCharge(client, clientPlayer) == null) {
                    Utils.findToSetWindCharge(clientPlayer);
                }
                ZoomState.TEMPORARY_GAZE_BOOST = true;
                ZoomState.gazeCounter = 2;
                ZoomState.setValue(client, clientPlayer);
                ElytraBoost.flag_elytra_boost = true;
                clientPlayer.setXRot(90.0f);
                KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)JobManager.client.options.keyUse).accessorBoundKey());
            }
            if (value == BOOST_JUMP) {
                clientPlayer.setXRot((float)Config.REFLECTION_ANGLE);
            }
            if (value == BOOST_JUMP - 1) {
                MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), 10);
                ElytraBoost.flag_elytra_boost = false;
                ZoomState.TEMPORARY_GAZE_BOOST = false;
                ZoomState.resetZoomPitch();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.ELYTRA_BOOST, value);
    }

    public static void doubleTap(int value) {
        LocalPlayer clientPlayer;
        int v;
        int n = v = Config.DT_AERIAL_DIVE_MODE ? 5 : 4;
        if (!Config.ELYTRA_MANUAL_MODE) {
            if (value == v) {
                if (JobManager.client.player != null && Utils.isUsingElytra(JobManager.client.player)) {
                    ToggleElytra.toggleElytra();
                }
                KeyMapping.click((InputConstants.Key)maceKey);
                KeyMapping.click((InputConstants.Key)attackKey);
                KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)true);
            }
        } else if (value == v && (clientPlayer = JobManager.client.player) != null && Utils.isUsingElytra(clientPlayer)) {
            rewearReturnSlot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
            int slot = ToggleElytra.toggleElytra();
            if (slot > -1) {
                JobManager.elytraSwap(slot);
                KeyMapping.click((InputConstants.Key)maceKey);
                KeyMapping.click((InputConstants.Key)attackKey);
                KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)true);
            }
        }
        if (value == 3) {
            JobManager.setLock(1);
        }
        if (value == 2) {
            KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)false);
            JobManager.automaticRewear();
        }
        JobManager.decValueAndSetFlag(StatusType.DOUBLE_TAP, value);
    }

    public static void doubleTapElytraSpear(int value) {
        MaceAttackAssistanceClient.setFlagElytraSpear(true);
        if (!Config.ELYTRA_MANUAL_MODE) {
            switch (value) {
                case 5: {
                    if (JobManager.client.player != null && Utils.isUsingElytra(JobManager.client.player)) {
                        ToggleElytra.toggleElytra();
                    }
                    KeyMapping.click((InputConstants.Key)maceKey);
                    KeyMapping.click((InputConstants.Key)attackKey);
                    break;
                }
                case 3: {
                    JobManager.setLock(1);
                    break;
                }
                case 2: {
                    JobManager.automaticRewear();
                }
            }
        } else {
            switch (value) {
                case 5: {
                    LocalPlayer clientPlayer = JobManager.client.player;
                    if (clientPlayer == null || !Utils.isUsingElytra(clientPlayer)) break;
                    rewearReturnSlot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
                    int slot = ToggleElytra.toggleElytra();
                    if (slot <= -1) break;
                    JobManager.elytraSwap(slot);
                    KeyMapping.click((InputConstants.Key)maceKey);
                    KeyMapping.click((InputConstants.Key)attackKey);
                    break;
                }
                case 3: {
                    JobManager.setLock(1);
                    break;
                }
                case 2: {
                    JobManager.automaticRewear();
                }
            }
        }
        JobManager.decValueAndSetFlag(StatusType.DOUBLE_TAP_ELYTRA_SPEAR, value);
    }

    public static void doubleTapOffHand(int value) {
        switch (value) {
            case 5: {
                KeyMapping.click((InputConstants.Key)((KeyMappingInvoker)JobManager.client.options.keyUse).accessorBoundKey());
                KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)true);
                break;
            }
            case 4: {
                KeyMapping.click((InputConstants.Key)maceKey);
                KeyMapping.click((InputConstants.Key)attackKey);
                break;
            }
            case 3: {
                JobManager.setLock(1);
                break;
            }
            case 2: {
                KeyMapping.set((InputConstants.Key)((KeyMappingInvoker)Minecraft.getInstance().options.keyAttack).accessorBoundKey(), (boolean)false);
            }
        }
        JobManager.decValueAndSetFlag(StatusType.DOUBLE_TAP_OFF_HAND, value);
    }

    public static void elytraManualSwitchMode(int value) {
        int slot;
        if (value == 5 && (slot = ToggleElytra.toggleElytra()) > -1) {
            JobManager.elytraSwap(slot);
            value = 1;
        }
        JobManager.decValueAndSetFlag(StatusType.ELYTRA_MANUAL_SWITCH_MODE, value);
    }

    private static void elytraSwap(int slot) {
        LocalPlayer player = JobManager.client.player;
        MultiPlayerGameMode interactionManager = JobManager.client.gameMode;
        if (player != null && interactionManager != null) {
            JobManager.selectHotbarSlot(slot);
            AutoRefill.equipmentSwap(interactionManager, player, slot);
        }
    }

    private static void doAttack() {
        ((MinecraftInvoker)client).doAttackInvoker();
    }

    private static void selectHotbarSlot(int slot) {
        LocalPlayer clientPlayer = JobManager.client.player;
        if (clientPlayer != null && Utils.isHotBar(slot)) {
            clientPlayer.getInventory().setSelectedSlot(slot);
            if (JobManager.client.gameMode != null) {
                ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
            }
        }
    }

    public static void setBoostValue(int boostJump, int boostThrow) {
        BOOST_JUMP = boostJump;
        BOOST_THROW = boostThrow;
    }

    private static void automaticRewear() {
        LocalPlayer player;
        if (!Config.DOUBLE_TAP_REWEAR) {
            return;
        }
        if (TickScheduler.isPendingConditionTaskWithKey((Object)ScheduleKey.REWEAR)) {
            TickScheduler.cancelPendingConditionTask((Object)ScheduleKey.REWEAR);
        }
        if ((player = Minecraft.getInstance().player) != null) {
            TickScheduler.setDelayTask(Config.DOUBLE_TAP_REWEAR_DELAY, () -> TickScheduler.setConditionTaskWithKey(() -> JobManager.checkOrderIsEmpty() && PrevSlotManager.isEmpty() && RefillManager.isEmpty() && !TickScheduler.hasPendingOrReadyDelayedTasks() && !TickScheduler.hasOtherConditionTasks((Object)ScheduleKey.REWEAR), () -> {
                if (Utils.isNotUsingElytra(player)) {
                    if (Config.ELYTRA_MANUAL_MODE) {
                        JobManager.setOrder(StatusType.ELYTRA_MANUAL_SWITCH_MODE, rewearReturnSlot);
                    } else {
                        ToggleElytra.toggleElytra();
                    }
                }
            }, (Object)ScheduleKey.REWEAR));
        }
    }

    public static void autoRefill(int value) {
        JobManager.decValueAndSetFlag(StatusType.AUTO_REFILL, value);
    }

    public static void pearlGrapple(int value) {
        LocalPlayer player = JobManager.client.player;
        if (player == null) {
            JobManager.decValueAndSetFlag(StatusType.PEARL_GRAPPLE, value);
            return;
        }
        if (value == 60) {
            int pearlSlot = Utils.findItemInHotbar(EnderpearlItem.class, true);
            if (pearlSlot == -1) {
                JobManager.decValueAndSetFlag(StatusType.PEARL_GRAPPLE, 1);
                return;
            }
            player.getInventory().setSelectedSlot(pearlSlot);
            if (JobManager.client.gameMode != null) {
                ((MultiPlayerGameModeInvoker)JobManager.client.gameMode).syncSelectedSlotInvoker();
            }
            grappleTarget = PearlGrapple.findNearestMobForGrapple(30);
            if (grappleTarget == null) {
                JobManager.decValueAndSetFlag(StatusType.PEARL_GRAPPLE, 1);
                return;
            }
            MaceAttackAssistanceClient.setTargetMob(grappleTarget);
        }
        if (value <= 58) {
            if (grappleTarget == null || !grappleTarget.isAlive()) {
                JobManager.decValueAndSetFlag(StatusType.PEARL_GRAPPLE, 1);
                return;
            }
            int pingMs = Utils.getMyPing();
            int pingTicks = (int)Math.ceil((double)pingMs / 50.0);
            int delay = Math.max(0, PearlGrapple.getAdvancedDelayTicks(player, grappleTarget) - pingTicks);
            float[] aim = PearlGrapple.calculateAngleWithObservedVel(player, grappleTarget);
            if (delay == 0) {
                MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
                if (gameMode != null) {
                    if (aim != null) {
                        player.setYRot(aim[0]);
                        player.setXRot(aim[1]);
                    }
                    gameMode.useItem((Player)player, InteractionHand.MAIN_HAND);
                }
                value = 1;
            }
        }
        JobManager.decValueAndSetFlag(StatusType.PEARL_GRAPPLE, value);
    }

    public static void airPot(int value) {
        value = AirPot.airPotionCatch(value);
        JobManager.decValueAndSetFlag(StatusType.AIR_POT, value);
    }

    static {
        enderPearlData = new EnderPearlData(false, 0, 0);
        client = Minecraft.getInstance();
        axeSlotId = -1;
        maceSlotId = -1;
        beforeAxeSlot = -1;
        preCheckCount = 0;
        LOCKED = 0;
        statusMap = new EnumMap<StatusType, StatusEntry>(StatusType.class);
    }

    private record EnderPearlData(boolean canThrow, int previousSlot, int newSlot) {
    }
}
