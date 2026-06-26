/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.papack.maceattackassistance;

import com.papack.maceattackassistance.network.MaaPayload;
import com.papack.maceattackassistance.network.MaaSyncPayload;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaceAttackAssistanceFabric
implements ModInitializer {
    public static final String MOD_ID = "maceattackassistance";
    public static Logger LOGGER = LogManager.getLogger((String)"maceattackassistance");

    public void onInitialize() {
        PayloadTypeRegistry.clientboundPlay().register(MaaPayload.ID, MaaPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MaaPayload.ID, MaaPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MaaSyncPayload.TYPE, MaaSyncPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MaaSyncPayload.TYPE, MaaSyncPayload.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, ignored, ignored1) -> {
            LOGGER.info("send packet");
            ServerPlayer patt0$temp = handler.player;
            if (patt0$temp instanceof ServerPlayer) {
                ServerPlayer serverPlayer = patt0$temp;
                UUID uuid = serverPlayer.getUUID();
                ServerPlayNetworking.send((ServerPlayer)serverPlayer, (CustomPacketPayload)new MaaPayload(uuid));
            }
        });
    }
}
