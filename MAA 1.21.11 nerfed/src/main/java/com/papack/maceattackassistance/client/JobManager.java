/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.PlayerInventory
 *  net.minecraft.ItemStack
 *  net.minecraft.Items
 *  net.minecraft.KeyBinding
 *  net.minecraft.MinecraftClient
 *  net.minecraft.InputUtil$Key
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.ElytraBoost;
import com.papack.maceattackassistance.client.EnderPearlManager;
import com.papack.maceattackassistance.client.HotSwap;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.RefillManager;
import com.papack.maceattackassistance.client.ScheduleKey;
import com.papack.maceattackassistance.client.StatusEntry;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.StunSlam;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ToggleElytra;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.ZoomState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.mixin.KeyBindingInvoker;
import com.papack.maceattackassistance.mixin.MinecraftClientInvoker;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

public class JobManager {
    public static boolean JUMP_OPTION = false;
    public static boolean JUMP_MODE = false;
    public static boolean QUICK_JUMP = false;
    private static EnderPearlData enderPearlData = new EnderPearlData(false, 0, 0);
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static int BOOST_JUMP;
    private static int BOOST_THROW;
    private static InputUtil.Key attackKey;
    private static InputUtil.Key axeKey;
    private static InputUtil.Key maceKey;
    private static int maceSlot;
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
        statusMap.put(StatusType.MANUAL_BREACH, new StatusEntry(StatusType.MANUAL_BREACH, 5, 5, false));
        statusMap.put(StatusType.NORMAL, new StatusEntry(StatusType.NORMAL, 5, 5, false));
        statusMap.put(StatusType.NONE, new StatusEntry(StatusType.NONE, 5, 5, false));
        statusMap.put(StatusType.ENDER_PEARL, new StatusEntry(StatusType.ENDER_PEARL, 5, 5, false));
        statusMap.put(StatusType.PEARL_CATCH, new StatusEntry(StatusType.PEARL_CATCH, 5, 5, false));
        statusMap.put(StatusType.ROCKET, new StatusEntry(StatusType.ROCKET, 5, 5, false));
        statusMap.put(StatusType.WIND_CHARGE, new StatusEntry(StatusType.WIND_CHARGE, 5, 5, false));
        statusMap.put(StatusType.PRE_AXE, new StatusEntry(StatusType.PRE_AXE, 1, 1, false));
        statusMap.put(StatusType.ELYTRA_BOOST, new StatusEntry(StatusType.ELYTRA_BOOST, 5, 5, false));
        statusMap.put(StatusType.DOUBLE_TAP, new StatusEntry(StatusType.DOUBLE_TAP, 5, 5, false));
        statusMap.put(StatusType.ELYTRA_MANUAL_SWITCH_MODE, new StatusEntry(StatusType.ELYTRA_MANUAL_SWITCH_MODE, 5, 5, false));
        statusMap.put(StatusType.AUTO_REFILL, new StatusEntry(StatusType.AUTO_REFILL, 3, 3, false));
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
        return JobManager.checkStatus(StatusType.DOUBLE_TAP) || JobManager.checkStatus(StatusType.HIGH_SPEED) || JobManager.checkStatus(StatusType.VERY_HIGH_SPEED) || JobManager.checkStatus(StatusType.STUN_SLAM);
    }

    public static int checkValue(StatusType statusType) {
        return statusMap.get((Object)statusType).getValue();
    }

    public static void setPreviousSlot(StatusType statusType, int slot) {
        PrevSlotManager.setPrevSlot(statusType, slot, switch (statusType) {
            case StatusType.ENDER_PEARL -> 2;
            case StatusType.ELYTRA_MANUAL_SWITCH_MODE -> 1;
            default -> 0;
        });
    }

    public static void tick(MinecraftClient client, ClientPlayerEntity clientPlayer) {
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

    public static void setKeys(ClientPlayerEntity clientPlayer, StatusType selector, boolean shouldSetOrder) {
        int prevSlotId = clientPlayer.getInventory().getSelectedSlot();
        if (prevSlotId > -1) {
            attackKey = ((KeyBindingInvoker)JobManager.client.options.attackKey).accessorBoundKey();
            int axeSlotId = -1;
            int maceSlotId = -1;
            if (!ZoomState.KeyManager.keyManager()) {
                switch (selector) {
                    case VERY_HIGH_SPEED: 
                    case HIGH_SPEED: 
                    case STUN_SLAM: 
                    case HOT_SWAP: 
                    case DOUBLE_TAP: {
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
                clientPlayer.getInventory().setSelectedSlot(maceSlotId);
                if (shouldSetOrder && JobManager.checkOrderIsEmpty()) {
                    JobManager.setOrder(selector, prevSlotId);
                }
            }
        }
    }

    public static int getAxeSlotId(ClientPlayerEntity clientPlayer) {
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

    public static boolean keyReleased(MinecraftClient client) {
        boolean flag = false;
        if (client.player != null) {
            ItemStack itemStack = client.player.getMainHandStack();
            flag = !JobManager.checkStatus(StatusType.DOUBLE_TAP) && (ToggleElytra.isElytra(itemStack) || ToggleElytra.isChestPlate(itemStack) || itemStack.isOf(Items.FIREWORK_ROCKET) || itemStack.isOf(Items.WIND_CHARGE) || itemStack.isOf(Items.ENDER_PEARL));
        }
        boolean attackKey = client.options.attackKey.isPressed();
        boolean useKey = client.options.useKey.isPressed();
        return (flag || !attackKey) && !useKey;
    }

    private static EnderPearlData findEnderPearl(ClientPlayerEntity clientPlayer, boolean isCooldown) {
        int currentSlot = clientPlayer.getInventory().getSelectedSlot();
        PlayerInventory inventory = clientPlayer.getInventory();
        for (int i = 0; i < 9; ++i) {
            boolean flag;
            ItemStack itemStack = inventory.getStack(i);
            boolean bl = flag = isCooldown && Config.THROW_WIND_CHARGE ? itemStack.isOf(Items.WIND_CHARGE) : itemStack.isOf(Items.ENDER_PEARL);
            if (!flag || clientPlayer.getItemCooldownManager().isCoolingDown(itemStack)) continue;
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
            JobManager.doAttack();
        }
        if (value == 2) {
            JobManager.setLock(1);
        }
        JobManager.decValueAndSetFlag(StatusType.HIGH_SPEED, value);
    }

    public static void stunSlam(int value) {
        if (value == Config.STUN_LOW) {
            JobManager.selectHotbarSlot(maceSlot);
            JobManager.doAttack();
        }
        if (value == 2) {
            JobManager.setLock(1);
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
                JobManager.doAttack();
                KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
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
                    boolean isCooldown = EnderPearlManager.isCooldown();
                    enderPearlData = JobManager.findEnderPearl(clientPlayer, isCooldown);
                    if (JobManager.enderPearlData.canThrow) {
                        KeyBinding.onKeyPressed((InputUtil.Key)((KeyBindingInvoker)client.options.hotbarKeys[JobManager.enderPearlData.newSlot]).accessorBoundKey());
                        if (isCooldown || !Config.THROW_WIND_CHARGE) break;
                        EnderPearlManager.startCooldown();
                        break;
                    }
                    value = 1;
                    break;
                }
                case 4: {
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
        if (value == 3) {
            value = 1;
        }
        JobManager.decValueAndSetFlag(StatusType.ROCKET, value);
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
        ClientPlayerEntity clientPlayer = JobManager.client.player;
        if (clientPlayer != null) {
            if (value == BOOST_THROW && !ElytraBoost.flag_elytra_boost) {
                if (Utils.getHandHoldingWindCharge(client, clientPlayer) == null) {
                    Utils.findToSetWindCharge(clientPlayer);
                }
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
                MaceAttackAssistanceClient.afterJump(clientPlayer, clientPlayer.getInventory().getSelectedSlot(), 10);
                ElytraBoost.flag_elytra_boost = false;
                ZoomState.TEMPORARY_GAZE_BOOST = false;
                ZoomState.resetZoomPitch();
            }
        }
        JobManager.decValueAndSetFlag(StatusType.ELYTRA_BOOST, value);
    }

    public static void doubleTap(int value) {
        if (!Config.ELYTRA_MANUAL_MODE) {
            switch (value) {
                case 5: {
                    if (JobManager.client.player != null && Utils.isUsingElytra(JobManager.client.player)) {
                        ToggleElytra.toggleElytra();
                    }
                    KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
                    KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
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
                case 4: {
                    ClientPlayerEntity clientPlayer = JobManager.client.player;
                    if (clientPlayer == null || !Utils.isUsingElytra(clientPlayer)) break;
                    rewearReturnSlot = PrevSlotManager.isEmpty() ? clientPlayer.getInventory().getSelectedSlot() : PrevSlotManager.getLastOrderSlot();
                    int slot = ToggleElytra.toggleElytra();
                    if (slot <= -1) break;
                    JobManager.elytraSwap(slot);
                    KeyBinding.onKeyPressed((InputUtil.Key)maceKey);
                    KeyBinding.onKeyPressed((InputUtil.Key)attackKey);
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
        JobManager.decValueAndSetFlag(StatusType.DOUBLE_TAP, value);
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
        ClientPlayerEntity player = JobManager.client.player;
        ClientPlayerInteractionManager interactionManager = JobManager.client.interactionManager;
        if (player != null && interactionManager != null) {
            JobManager.selectHotbarSlot(slot);
            AutoRefill.equipmentSwap(interactionManager, player, slot);
        }
    }

    private static void doAttack() {
        ((MinecraftClientInvoker)client).doAttackInvoker();
    }

    private static void selectHotbarSlot(int slot) {
        ClientPlayerEntity clientPlayer = JobManager.client.player;
        if (clientPlayer != null && Utils.isHotBar(slot)) {
            clientPlayer.getInventory().setSelectedSlot(slot);
        }
    }

    public static void setBoostValue(int boostJump, int boostThrow) {
        BOOST_JUMP = boostJump;
        BOOST_THROW = boostThrow;
    }

    private static void automaticRewear() {
        ClientPlayerEntity player;
        if (!Config.DOUBLE_TAP_REWEAR) {
            return;
        }
        if (TickScheduler.isPendingConditionTaskWithKey((Object)ScheduleKey.REWEAR)) {
            TickScheduler.cancelPendingConditionTask((Object)ScheduleKey.REWEAR);
        }
        if ((player = MinecraftClient.getInstance().player) != null) {
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

    public static void pearlCatch(int value) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer != null) {
            if (!PearlCatch.isActive()) {
                PearlCatch.activate(clientPlayer);
            }
            PearlCatch.tick(client, clientPlayer);
            if (!PearlCatch.isActive()) {
                statusMap.get((Object)StatusType.PEARL_CATCH).setFlag(false);
                statusMap.get((Object)StatusType.PEARL_CATCH).setValue(statusMap.get((Object)StatusType.PEARL_CATCH).getDefaultValue());
                return;
            }
        } else {
            statusMap.get((Object)StatusType.PEARL_CATCH).setFlag(false);
            statusMap.get((Object)StatusType.PEARL_CATCH).setValue(statusMap.get((Object)StatusType.PEARL_CATCH).getDefaultValue());
        }
    }

    public static boolean jumpOption() {
        return !JUMP_OPTION;
    }

    static {
        beforeAxeSlot = -1;
        preCheckCount = 0;
        LOCKED = 0;
        statusMap = new EnumMap<StatusType, StatusEntry>(StatusType.class);
    }

    private record EnderPearlData(boolean canThrow, int previousSlot, int newSlot) {
    }
}
