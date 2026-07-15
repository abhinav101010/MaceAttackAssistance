/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.MinecraftClient
 *  net.minecraft.Random
 *  net.minecraft.ClientPlayNetworkHandler
 *  net.minecraft.GameMessageS2CPacket
 *  net.minecraft.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.TickScheduler;
import com.papack.maceattackassistance.client.ZoomState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayNetworkHandler.class})
public abstract class MixinClientPlayNetworkHandler {
    @Inject(method={"onGameMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        String content = packet.content().getString();
        String REGULATION = "405";
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            String uuid = player.getUuid().toString();
            if (content.contains("[MAA] Handshake init...")) {
                try {
                    int delay = Random.create().nextBetween(0, 40);
                    TickScheduler.setDelayTask(delay, () -> {
                        player.networkHandler.sendChatMessage("[MAA_CLIENT]:" + uuid + ":" + REGULATION);
                        ZoomState.MAAClientState.receivedFirstMessage = true;
                    });
                }
                catch (Exception e) {
                    MaceAttackAssistanceClient.LOGGER.warn("Invalid message");
                }
                ci.cancel();
            } else if (content.contains("[MAA] Allowed Behavior Level:")) {
                String[] data = content.split(":");
                if (data.length == 4) {
                    String id = data[1];
                    String regulation = data[2];
                    String level = data[3];
                    if (id.equals(uuid) && regulation.equals(REGULATION)) {
                        try {
                            if (ZoomState.MAAClientState.receivedFirstMessage) {
                                int allowedLevel = Integer.parseInt(level);
                                ZoomState.MAAClientState.allowedLevels = Math.max(0, Math.min(3, allowedLevel));
                                ZoomState.MAAClientState.antiCheat = ZoomState.MAAClientState.allowedLevels == 0;
                                ZoomState.MAAClientState.receivedFirstMessage = false;
                            }
                        }
                        catch (Exception e) {
                            MaceAttackAssistanceClient.LOGGER.warn("This message is not allowed.");
                        }
                    }
                }
                ci.cancel();
            }
        }
    }
}
