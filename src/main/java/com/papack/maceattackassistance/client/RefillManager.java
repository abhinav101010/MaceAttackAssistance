/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Item
 *  net.minecraft.MinecraftClient
 *  net.minecraft.ClientPlayerInteractionManager
 *  net.minecraft.ClientPlayerEntity
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.AutoRefill;
import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.PrevSlotManager;
import com.papack.maceattackassistance.client.ScheduleKey;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;

public class RefillManager {
    private static final List<RefillData> refillDataList = new ArrayList<RefillData>();
    private static int wait = 0;

    public static void tick(MinecraftClient client, ClientPlayerEntity player) {
        if (RefillManager.isEmpty() || player == null) {
            return;
        }
        if (RefillManager.condition(client)) {
            ClientPlayerInteractionManager manager = client.interactionManager;
            RefillData refillData = RefillManager.getFirst();
            if (refillData != null && manager != null) {
                boolean isTargetItemAfterRefill;
                int slot = refillData.slot;
                int refillSlot = slot + 36;
                JobManager.setOrder(StatusType.AUTO_REFILL, -1);
                boolean isTargetItem = refillData.item.equals(player.getInventory().getStack(Utils.isHotBar(slot) ? slot : 40).getItem());
                if (isTargetItem) {
                    if (AutoRefill.checkStockQuantity(player, refillData.item())) {
                        AutoRefill.autoRefill(manager, player, refillSlot);
                    }
                } else {
                    int inventorySlot = Utils.findItemInInventory(refillData.item);
                    if (inventorySlot > -1) {
                        AutoRefill.autoRefillInventory(manager, player, inventorySlot, refillSlot);
                    }
                }
                if (!(isTargetItemAfterRefill = refillData.item.equals(player.getInventory().getStack(Utils.isHotBar(slot) ? slot : 40).getItem())) && AutoRefill.checkStockQuantity(player, refillData.item())) {
                    RefillManager.setRefillData(StatusType.AUTO_REFILL, refillData.slot, refillData.item, 0);
                }
                RefillManager.removeFirst();
            }
        }
        if (wait > 0) {
            --wait;
        }
    }

    public static boolean isEmpty() {
        return refillDataList.isEmpty();
    }

    public static void setRefillData(StatusType type, int slot, Item item, int wait) {
        if (!RefillManager.isRegistered(item) && Config.AUTO_REFILL) {
            RefillManager.wait = wait;
            refillDataList.add(new RefillData(type, slot, item));
        }
    }

    private static RefillData getFirst() {
        return RefillManager.isEmpty() ? null : refillDataList.getFirst();
    }

    private static void removeFirst() {
        if (!RefillManager.isEmpty()) {
            refillDataList.removeFirst();
        }
    }

    private static boolean isRegistered(Item queryItem) {
        return refillDataList.stream().anyMatch(f -> f.item().equals(queryItem));
    }

    private static boolean condition(MinecraftClient client) {
        if (wait > 0) {
            return false;
        }
        if (MaceAttackAssistanceClient.requireChargeJump) {
            return false;
        }
        if (!PrevSlotManager.isEmpty()) {
            return false;
        }
        if (!JobManager.checkOrderIsEmpty()) {
            return false;
        }
        if (TickScheduler.hasPendingOrReadyDelayedTasks()) {
            return false;
        }
        if (TickScheduler.hasOtherConditionTasks((Object)ScheduleKey.REWEAR)) {
            return false;
        }
        if (client.options.attackKey.isPressed()) {
            return false;
        }
        return !client.options.useKey.isPressed();
    }

    public record RefillData(StatusType type, int slot, Item item) {
    }
}
