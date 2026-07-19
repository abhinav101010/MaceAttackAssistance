package com.papack.maceattackassistance.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={ClientPlayerEntity.class})
public interface ClientPlayerEntityAccessor {
    @Accessor(value="lastPitchClient")
    public void setLastPitchClient(float value);
}
