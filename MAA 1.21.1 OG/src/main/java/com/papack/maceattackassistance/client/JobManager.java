/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.EnderPearlItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.InputUtil$class_306
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.network.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.StatusEntry;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

public class JobManager {
    private static EnderPearlData enderPearlData = new EnderPearlData(false, 0, 0);
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static int BOOST_JUMP;
    private static int BOOST_THROW;
    private static InputUtil.Key attackKey;
    private static InputUtil.Key axeKey;
    private static InputUtil.Key maceKey;
    private static int maceSlot;
    public static int beforeAxeSlot;
    private static int preCheckCount;
    private static int previousSlot;
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
        statusMap.put(StatusType.MANUAL_BREACH, new StatusEntry(StatusType.MANUAL_BREACH, 5, 5, false));
        statusMap.put(StatusType.NORMAL, new StatusEntry(StatusType.NORMAL, 5, 5, false));
        statusMap.put(StatusType.NONE, new StatusEntry(StatusType.NONE, 5, 5, false));
        statusMap.put(StatusType.ENDER_PEARL, new StatusEntry(StatusType.ENDER_PEARL, 5, 5, false));
        statusMap.put(StatusType.ROCKET, new StatusEntry(StatusType.ROCKET, 5, 5, false));
        statusMap.put(StatusType.WIND_CHARGE, new StatusEntry(StatusType.WIND_CHARGE, 5, 5, false));
        statusMap.put(StatusType.PRE_AXE, new StatusEntry(StatusType.PRE_AXE, 1, 1, false));
        statusMap.put(StatusType.INSTANT_ATTACK_INTERVAL, new StatusEntry(StatusType.INSTANT_ATTACK_INTERVAL, 5, 5, false));
        statusMap.put(StatusType.ELYTRA_BOOST, new StatusEntry(StatusType.ELYTRA_BOOST, 5, 5, false));
    }

    public static void setOrder(StatusType statusType, int slot) {
        StatusEntry statusEntry = statusMap.get((Object)statusType);
        if (!statusEntry.getFlag()) {
            statusMap.get((Object)statusType).setFlag(true);
            JobManager.setPreviousSlot(statusType, slot);
            if ((statusType == StatusType.BREACH || statusType == StatusType.HOT_SWAP) && JobManager.client.player != null) {
                JobManager.setKeys(JobManager.client.player, statusType);
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

    public static int checkValue(StatusType statusType) {
        return statusMap.get((Object)statusType).getValue();
    }

    public static void setPreviousSlot(StatusType statusType, int slot) {
        if (!Config.RETURN_TO_PREV_SLOT && statusType != StatusType.ROCKET && statusType != StatusType.ENDER_PEARL) {
            slot = -1;
        }
        if (slot > -1 && previousSlot == -1) {
            previousSlot = slot;
        }
    }

    private static void returnToPreviousSlot(MinecraftClient client) {
        if (previousSlot > -1 && previousSlot != 9) {
            KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[previousSlot]).accessorBoundKey());
        }
        previousSlot = -1;
        beforeAxeSlot = -1;
    }

    public static void tick(MinecraftClient client, ClientPlayerEntity clientPlayer) {
        if (LOCKED == 0) {
            int checkCount = 0;
            for (StatusEntry entry : statusMap.values()) {
                if (!entry.getFlag()) continue;
                JobManager.setKeys(clientPlayer, entry.getType());
                entry.getType().execute(entry.getValue());
                ++checkCount;
            }
            if (preCheckCount == 0 && checkCount == 0 && JobManager.keyReleased(client)) {
                JobManager.returnToPreviousSlot(client);
            }
            preCheckCount = checkCount;
        } else {
            --LOCKED;
        }
    }

    public static void setKeys(ClientPlayerEntity clientPlayer, StatusType selector) {
        int prevSlotId = clientPlayer.getInventory().selectedSlot;
        if (prevSlotId > -1) {
            attackKey = ((KeyBindingInvoker)JobManager.client.options.attackKey).accessorBoundKey();
            int axeSlotId = -1;
            int maceSlotId = -1;
            switch (selector) {
                case VERY_HIGH_SPEED: 
                case HIGH_SPEED: 
                case STUN_SLAM: 
                case HOT_SWAP: {
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
                case MANUAL_STUN: {
                    maceSlotId = HotSwap.getPrimaryMaceSlotId(clientPlayer);
                    axeSlotId = JobManager.getAxeSlotId(clientPlayer);
                }
            }
            if (maceSlotId < 0) {
                maceSlotId = prevSlotId;
            }
            if (axeSlotId < 0) {
                axeSlotId = prevSlotId;
            }
            maceSlot = maceSlotId;
            axeKey = ((KeyBindingInvoker)JobManager.client.options.hotbarKeys[axeSlotId]).accessorBoundKey();
            maceKey = ((KeyBindingInvoker)JobManager.client.options.hotbarKeys[maceSlotId]).accessorBoundKey();
            if (selector == StatusType.BREACH || selector == StatusType.HOT_SWAP) {
                clientPlayer.getInventory().selectedSlot = maceSlotId;
                JobManager.setOrder(selector, prevSlotId);
            }
        }
    }

    public static int getAxeSlotId(ClientPlayerEntity clientPlayer) {
        int hotBarSlotId;
        int beforeSlot;
        block3: {
            block2: {
                beforeSlot = clientPlayer.getInventory().selectedSlot;
                hotBarSlotId = -1;
                if (Config.AXE_SLOT <= -1) break block2;
                ItemStack itemStack = clientPlayer.getInventory().getStack(Config.AXE_SLOT);
                if (!JobManager.isAxe(itemStack)) break block3;
                hotBarSlotId = Config.AXE_SLOT;
                break block3;
            }
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = clientPlayer.getInventory().getStack(i);
                if (!JobManager.isAxe(itemStack)) continue;
                hotBarSlotId = i;
                break;
            }
        }
        if (hotBarSlotId > -1 && hotBarSlotId != beforeSlot) {
            beforeAxeSlot = beforeSlot;
        }
        return hotBarSlotId;
    }

    private static boolean isAxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem;
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

    public static boolean keyReleased(MinecraftClient client) {
        boolean attackKey = client.options.attackKey.isPressed();
        boolean useKey = client.options.useKey.isPressed();
        return !attackKey && !useKey;
    }

    private static EnderPearlData findEnderPearl(ClientPlayerEntity clientPlayer) {
        int currentSlot = clientPlayer.getInventory().selectedSlot;
        PlayerInventory inventory = clientPlayer.getInventory();
        for (int i = 0; i < 9; ++i) {
            Item class_17922 = inventory.getStack(i).getItem();
            if (!(class_17922 instanceof EnderPearlItem)) continue;
            EnderPearlItem enderPearlItem = (EnderPearlItem)class_17922;
            if (clientPlayer.getItemCooldownManager().isCoolingDown((Item)enderPearlItem)) continue;
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
                KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
                break;
            }
            case 2: {
                JobManager.attack();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.AIR_BREACH, value);
    }

    public static void veryHighSpeed(int value) {
        switch (value) {
            case 4: {
                JobManager.selectHotbarSlot(maceSlot);
                JobManager.attack();
                break;
            }
            case 2: {
                JobManager.setLock(1);
            }
        }
        JobManager.decValueAndSetFlag(StatusType.VERY_HIGH_SPEED, value);
    }

    public static void highSpeed(int value) {
        switch (value) {
            case 5: {
                KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
                KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
                break;
            }
            case 2: {
                JobManager.setLock(1);
            }
        }
        JobManager.decValueAndSetFlag(StatusType.HIGH_SPEED, value);
    }

    public static void stunSlam(int value) {
        switch (value) {
            case 4: {
                KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
                KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
                break;
            }
            case 2: {
                JobManager.setLock(1);
            }
        }
        JobManager.decValueAndSetFlag(StatusType.STUN_SLAM, value);
    }

    public static void airShieldBreach(int value) {
        switch (value) {
            case 5: {
                KeyBinding.onKeyPressed((InputUtil.Key)axeKey);
                break;
            }
            case 4: {
                JobManager.attack();
                KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
                break;
            }
            case 2: {
                JobManager.attack();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.AIR_SHIELD_BREACH, value);
    }

    public static void stun(int value) {
        if (value == 5) {
            KeyBinding.onKeyPressed((InputUtil.Key)axeKey);
            KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
        }
        JobManager.decValueAndSetFlag(StatusType.STUN, value);
    }

    public static void manualStun(int value) {
        switch (value) {
            case 5: {
                KeyBinding.onKeyPressed((InputUtil.Key)axeKey);
                break;
            }
            case 2: 
            case 4: {
                KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
                break;
            }
            case 3: {
                KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
            }
        }
        JobManager.decValueAndSetFlag(StatusType.MANUAL_STUN, value);
    }

    public static void manualBreach(int value) {
        if (value == 4) {
            KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
            KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
        }
        JobManager.decValueAndSetFlag(StatusType.MANUAL_BREACH, value);
    }

    public static void enderPearl(int value) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null) {
            switch (value) {
                case 5: {
                    enderPearlData = JobManager.findEnderPearl(clientPlayer);
                    if (JobManager.enderPearlData.canThrow) {
                        KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[JobManager.enderPearlData.newSlot]).accessorBoundKey());
                        break;
                    }
                    value = 1;
                    break;
                }
                case 3: {
                    KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.useKey).accessorBoundKey());
                    enderPearlData = new EnderPearlData(false, JobManager.enderPearlData.previousSlot, JobManager.enderPearlData.previousSlot);
                    JobManager.setLock(2);
                    value = 1;
                }
            }
        }
        JobManager.decValueAndSetFlag(StatusType.ENDER_PEARL, value);
    }

    public static void rocket(int value) {
        JobManager.decValueAndSetFlag(StatusType.ROCKET, value);
    }

    public static void preAxe(int value) {
        JobManager.decValueAndSetFlag(StatusType.PRE_AXE, value);
    }

    public static void normal(int value) {
        if (value == 5) {
            JobManager.attack();
            value = 2;
        }
        JobManager.decValueAndSetFlag(StatusType.NORMAL, value);
    }

    public static void none() {
        int value = 1;
        JobManager.decValueAndSetFlag(StatusType.NONE, value);
    }

    public static void instantAttackInterval(int value) {
        JobManager.decValueAndSetFlag(StatusType.INSTANT_ATTACK_INTERVAL, value);
    }

    public static void elytraBoost(int value) {
        ClientPlayerEntity clientPlayer = JobManager.client.player;
        if (clientPlayer != null) {
            if (value == BOOST_THROW && !ElytraBoost.flag_elytra_boost) {
                ZoomState.TEMPORARY_GAZE_BOOST = true;
                ZoomState.gazeCounter = 2;
                ZoomState.setValue(client, clientPlayer);
                ElytraBoost.flag_elytra_boost = true;
                clientPlayer.setPitch(90.0f);
                KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)JobManager.client.options.useKey).accessorBoundKey());
            }
            if (value == BOOST_JUMP) {
                clientPlayer.setPitch((float)Config.REFLECTION_ANGLE);
            }
            if (value == BOOST_JUMP - 1) {
                ElytraBoost.flag_elytra_boost = false;
                ZoomState.TEMPORARY_GAZE_BOOST = false;
                ZoomState.resetZoomPitch();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.ELYTRA_BOOST, value);
    }

    private static void attack() {
        ClientPlayerEntity clientPlayer = JobManager.client.player;
        ClientPlayerInteractionManager interactionManager = JobManager.getInteractionManager();
        Entity target = JobManager.client.targetedEntity;
        if (interactionManager != null && clientPlayer != null && target != null) {
            interactionManager.attackEntity((PlayerEntity)clientPlayer, target);
        }
    }

    private static void selectHotbarSlot(int slot) {
        ClientPlayerEntity clientPlayer = JobManager.client.player;
        if (clientPlayer != null) {
            clientPlayer.getInventory().selectedSlot = slot;
        }
    }

    private static ClientPlayerInteractionManager getInteractionManager() {
        return JobManager.client.interactionManager;
    }

    public static void setBoostValue(int boostJump, int boostThrow) {
        BOOST_JUMP = boostJump;
        BOOST_THROW = boostThrow;
    }

    static {
        beforeAxeSlot = -1;
        preCheckCount = 0;
        previousSlot = -1;
        LOCKED = 0;
        statusMap = new EnumMap<StatusType, StatusEntry>(StatusType.class);
    }

    private record EnderPearlData(boolean canThrow, int previousSlot, int newSlot) {
    }
}
