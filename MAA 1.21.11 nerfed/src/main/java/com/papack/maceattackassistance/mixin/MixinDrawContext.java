/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.RenderPipelines
 *  net.minecraft.Identifier
 *  net.minecraft.DrawContext
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={DrawContext.class})
public class MixinDrawContext {
    @Unique
    private static final Identifier CROSSHAIR_TEXTURE = Identifier.ofVanilla((String)"hud/crosshair");

    @Inject(method={"drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawCustomCrosshairTexture(RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, CallbackInfo ci) {
        if (sprite.equals((Object)CROSSHAIR_TEXTURE) && Utils.shouldRenderCustomCrosshair()) {
            ((DrawContext)(Object)this).drawGuiTexture(RenderPipelines.CROSSHAIR, Config.CUSTOM_CROSSHAIR_TEXTURE, x, y, width, height);
            ci.cancel();
        }
    }
}
