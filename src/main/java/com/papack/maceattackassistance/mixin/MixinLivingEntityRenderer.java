/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.LivingEntityRenderState
 *  net.minecraft.OrderedRenderCommandQueue
 *  net.minecraft.CameraRenderState
 *  net.minecraft.RenderLayers
 *  net.minecraft.Entity
 *  net.minecraft.LivingEntity
 *  net.minecraft.Identifier
 *  net.minecraft.MinecraftClient
 *  net.minecraft.FeatureRendererContext
 *  net.minecraft.MatrixStack
 *  net.minecraft.VertexConsumer
 *  net.minecraft.VertexConsumerProvider
 *  net.minecraft.OverlayTexture
 *  net.minecraft.EntityRendererFactory$Context
 *  net.minecraft.EntityModel
 *  net.minecraft.EntityRenderer
 *  net.minecraft.LivingEntityRenderer
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={LivingEntityRenderer.class})
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
extends EntityRenderer<T, S>
implements FeatureRendererContext<S, M> {
    @Unique
    private static final Identifier GLOWING_TEXTURE_RED = Identifier.of((String)"maceattackassistance", (String)"textures/red_overlay.png");
    @Unique
    private static final Identifier GLOWING_TEXTURE_GREEN = Identifier.of((String)"maceattackassistance", (String)"textures/green_overlay.png");
    @Unique
    private static boolean RENDER_SWITCH;
    @Unique
    Entity currentEntity;

    protected MixinLivingEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at={@At(value="HEAD")})
    private void getEntity(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        this.currentEntity = livingEntity;
    }

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"}, at={@At(value="TAIL")})
    private void renderOverlay(S livingEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if ((!Config.HIDE_MARKER || Config.AIM_ASSIST) && Config.TARGET_MARKER && Config.MARKER_TYPE == Config.MarkerType.Frame && this.shouldRenderGlowingOverlay(this.currentEntity)) {
            RENDER_SWITCH = !RENDER_SWITCH;
            boolean inRange = Utils.isInAttackableRange(client.player, this.currentEntity);
            if (inRange || RENDER_SWITCH) {
                this.drawGlowingOverlay(this.currentEntity, matrixStack, (VertexConsumerProvider)client.getBufferBuilders().getEntityVertexConsumers(), inRange);
            }
        }
    }

    @Unique
    private boolean shouldRenderGlowingOverlay(Entity entity) {
        Entity target = MaceAttackAssistanceClient.getTargetMob();
        if (target != null) {
            return entity.getUuid().equals(target.getUuid());
        }
        return false;
    }

    @Unique
    private void drawGlowingOverlay(Entity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean inRange) {
        matrices.push();
        matrices.translate(0.0, (double)entity.getHeight() + 0.5 * (double)Config.MARKER_OFFSET, 0.0);
        matrices.scale(1.0f, 1.0f, 1.0f);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayers.entityAlpha((Identifier)(inRange ? GLOWING_TEXTURE_GREEN : GLOWING_TEXTURE_RED)));
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        this.drawVertex(vertexConsumer, positionMatrix, -0.5f, -0.5f, 0.0f, 0.0f, inRange);
        this.drawVertex(vertexConsumer, positionMatrix, -0.5f, 0.5f, 0.0f, 1.0f, inRange);
        this.drawVertex(vertexConsumer, positionMatrix, 0.5f, 0.5f, 1.0f, 1.0f, inRange);
        this.drawVertex(vertexConsumer, positionMatrix, 0.5f, -0.5f, 1.0f, 0.0f, inRange);
        matrices.pop();
    }

    @Unique
    private void drawVertex(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float z, float u, float v, boolean inRange) {
        if (inRange) {
            vertexConsumer.vertex((Matrix4fc)matrix, x, 0.0f, z).color(0, 255, 0, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0.0f, 1.0f, 0.0f);
        } else {
            vertexConsumer.vertex((Matrix4fc)matrix, x, 0.0f, z).color(255, 0, 0, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0.0f, 1.0f, 0.0f);
        }
    }
}
