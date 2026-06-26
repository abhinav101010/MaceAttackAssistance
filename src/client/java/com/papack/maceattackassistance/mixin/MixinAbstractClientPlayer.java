/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.item.WindChargeItem
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.WindChargeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayer.class})
public class MixinAbstractClientPlayer {
    @Inject(method={"getFieldOfViewModifier"}, at={@At(value="HEAD")}, cancellable=true)
    private void shouldFOV(CallbackInfoReturnable<Float> cir) {
        if (MixinAbstractClientPlayer.verifyPlayerCondition()) {
            cir.setReturnValue(Float.valueOf(1.0f));
        }
    }

    @Unique
    private static boolean verifyPlayerCondition() {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer clientPlayer = client.player;
        if (clientPlayer == null) {
            return false;
        }
        if (clientPlayer.getTicksUsingItem() > 0) {
            return false;
        }
        if (!Config.FOV_SUPPRESSION || !clientPlayer.isSprinting() || clientPlayer.onGround() && !Utils.isOnSlimeBlock(clientPlayer)) {
            return false;
        }
        return Utils.getHandHoldingWindCharge(client, clientPlayer) != null && Config.AUTO_WIND_CHARGE_SELECT && Utils.isSuccessFoundItItemInHotbar(WindChargeItem.class, true);
    }
}
