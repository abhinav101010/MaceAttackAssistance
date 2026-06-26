/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.renderer.RenderPipelines
 *  net.minecraft.resources.Identifier
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiGraphicsExtractor.class})
public class MixinGuiGraphicsExtractor {
    @Inject(method={"blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void drawCustomCrosshairTexture(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, CallbackInfo ci) {
        if (location.equals((Object)Config.CROSSHAIR_TEXTURE) && Utils.shouldRenderCustomCrosshair()) {
            ((GuiGraphicsExtractor)(Object)this).blitSprite(RenderPipelines.CROSSHAIR, Config.CUSTOM_CROSSHAIR_TEXTURE, x, y, width, height);
            ci.cancel();
        }
    }
}
