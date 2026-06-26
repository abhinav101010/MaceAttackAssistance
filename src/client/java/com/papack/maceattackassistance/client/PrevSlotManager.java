/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.JobManager;
import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.StatusType;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class PrevSlotManager {
    private static final List<PrevSlotData> prevSlotDataList = new ArrayList<PrevSlotData>();
    private static int wait = 0;

    public static void tick(Minecraft client) {
        PrevSlotData prevSlotData;
        if (PrevSlotManager.isEmpty()) {
            return;
        }
        if (PrevSlotManager.condition(client) && (prevSlotData = PrevSlotManager.getLast()) != null) {
            int slot = prevSlotData.slot;
            if (Utils.isHotBar(slot)) {
                PrevSlotManager.setSelectedSlot(client, slot);
            }
            PrevSlotManager.removeLast();
        }
        if (wait > 0) {
            --wait;
        }
    }

    public static boolean isEmpty() {
        return prevSlotDataList.isEmpty();
    }

    public static void setPrevSlot(StatusType type, int slot, int wait) {
        if (Config.DEBUG_SCREEN) {
            MaceAttackAssistanceClient.LOGGER.info("prev-type : {} , slot : {}", (Object)type, (Object)slot);
        }
        PrevSlotManager.wait = wait + Config.PREV_SLOT_DELAY;
        if (type != null && Utils.isHotBar(slot) && PrevSlotManager.isEmpty()) {
            if (type == StatusType.BREACH && !Config.RETURN_TO_PREV_SLOT_BREACH) {
                return;
            }
            if (type != StatusType.BREACH && !Config.RETURN_TO_PREV_SLOT) {
                return;
            }
            if (Utils.isHotBar(Config.RETURN_TO_PREV_SLOT_MODE)) {
                slot = Config.RETURN_TO_PREV_SLOT_MODE;
            }
            prevSlotDataList.add(new PrevSlotData(type, slot));
        }
    }

    public static int getFirstOrderSlot() {
        return PrevSlotManager.isEmpty() ? -1 : prevSlotDataList.getFirst().slot();
    }

    public static int getLastOrderSlot() {
        return PrevSlotManager.isEmpty() ? -1 : prevSlotDataList.getLast().slot();
    }

    private static PrevSlotData getLast() {
        return PrevSlotManager.isEmpty() ? null : prevSlotDataList.getLast();
    }

    private static void removeLast() {
        if (!PrevSlotManager.isEmpty()) {
            prevSlotDataList.removeLast();
        }
    }

    private static boolean condition(Minecraft client) {
        if (wait > 0) {
            return false;
        }
        if (!JobManager.checkOrderIsEmpty()) {
            return false;
        }
        return JobManager.keyReleased(client);
    }

    private static void setSelectedSlot(Minecraft client, int slot) {
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer != null) {
            if (clientPlayer.getInventory().getSelectedSlot() == slot) {
                return;
            }
            clientPlayer.getInventory().setSelectedSlot(slot);
        }
    }

    public record PrevSlotData(StatusType type, int slot) {
    }
}
