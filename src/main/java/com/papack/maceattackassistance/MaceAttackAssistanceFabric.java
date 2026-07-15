/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.ServerPlayerEntity
 *  net.minecraft.CustomPayload
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.papack.maceattackassistance;

import com.papack.maceattackassistance.network.MaaPayload;
import java.util.UUID;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.network.packet.CustomPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MaceAttackAssistanceFabric
implements ModInitializer {
    public static final String MOD_ID = "maceattackassistance";
    public static Logger LOGGER = LogManager.getLogger((String)"maceattackassistance");

    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(MaaPayload.ID, MaaPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(MaaPayload.ID, MaaPayload.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            LOGGER.info("send packet");
            ServerPlayerEntity patt0$temp = handler.player;
            if (patt0$temp instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = patt0$temp;
                UUID uuid = serverPlayer.getUuid();
                ServerPlayNetworking.send((ServerPlayerEntity)serverPlayer, (CustomPayload)new MaaPayload(uuid));
            }
        });
    }
}
