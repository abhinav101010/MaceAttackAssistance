/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.PlayerEntityRenderer
 *  net.minecraft.OrderedRenderCommandQueue
 *  net.minecraft.Identifier
 *  net.minecraft.MinecraftClient
 *  net.minecraft.MatrixStack
 *  net.minecraft.Perspective
 *  net.minecraft.ModelPart
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.config.Config;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntityRenderer.class})
public class MixinPlayerEntityRenderer {
    @Inject(method={"renderArm"}, at={@At(value="HEAD")}, cancellable=true)
    private void hidePlayerArm(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, Identifier skinTexture, ModelPart arm, boolean sleeveVisible, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (Config.ZOOM_CAMERA && client.options.getPerspective() == Perspective.FIRST_PERSON) {
            ci.cancel();
        }
    }
}
