package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.ElytraCrosshair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameHud.class})
public class MixinInGameHud {
    @Inject(method={"renderCrosshair"}, at={@At(value="HEAD")}, cancellable=true)
    private void maa_replaceCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null && ElytraCrosshair.isElytraEquipped(player)) {
            ElytraCrosshair.render(context, client);
            ci.cancel();
        }
    }
}
