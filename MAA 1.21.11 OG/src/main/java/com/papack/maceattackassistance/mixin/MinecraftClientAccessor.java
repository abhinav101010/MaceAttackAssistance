package com.papack.maceattackassistance.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={MinecraftClient.class})
public interface MinecraftClientAccessor {
    @Accessor(value="cameraEntity")
    public net.minecraft.entity.Entity accessorCameraEntity();
}
