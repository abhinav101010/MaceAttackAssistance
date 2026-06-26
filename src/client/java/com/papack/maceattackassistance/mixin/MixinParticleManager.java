package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ParticleEngine.class})
public class MixinParticleManager {
    @Inject(method={"createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;)V"}, at={@At(value="HEAD")})
    private void onAddEmitter(Entity entity, ParticleOptions particle, CallbackInfo ci) {
        if (Config.DEBUG_SCREEN && particle == ParticleTypes.CRIT) {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer player = client.player;
            if (player != null) {
                // MC 26.2: gui.getChat() moved to gui.hud.getChat(), addMessage() → addClientSystemMessage()
                client.gui.hud.getChat().addClientSystemMessage(Component.literal("Critical Hit!"));
            }
        }
    }
}
