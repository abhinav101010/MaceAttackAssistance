/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayerEntity.class})
public class MixinAbstractClientPlayerEntity {
    @Inject(method={"getFovMultiplier"}, at={@At(value="HEAD")}, cancellable=true)
    private void shouldFOV(CallbackInfoReturnable<Float> cir) {
        if (MixinAbstractClientPlayerEntity.verifyPlayerCondition()) {
            cir.setReturnValue((Object)Float.valueOf(1.0f));
        }
    }

    @Unique
    private static boolean verifyPlayerCondition() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayer = client.player;
        if (clientPlayer == null) {
            return false;
        }
        if (clientPlayer.getItemUseTime() > 0) {
            return false;
        }
        if (Utils.getHandHoldingWindCharge(client, clientPlayer) == null) {
            return false;
        }
        return Config.FOV_SUPPRESSION && clientPlayer.isSprinting() && (!clientPlayer.isOnGround() || Utils.isOnSlimeBlock(clientPlayer));
    }
}
