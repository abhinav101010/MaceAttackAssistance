package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import com.papack.maceattackassistance.client.Utils;
import com.papack.maceattackassistance.client.config.Config;
import java.util.IdentityHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={LivingEntityRenderer.class})
public abstract class MixinLivingEntityRenderer {
    @Unique
    private static final Identifier GLOWING_TEXTURE_RED = Identifier.of((String)"maceattackassistance", (String)"textures/red_overlay.png");
    @Unique
    private static final Identifier GLOWING_TEXTURE_GREEN = Identifier.of((String)"maceattackassistance", (String)"textures/green_overlay.png");
    @Unique
    private static boolean RENDER_SWITCH;
    @Unique
    private final IdentityHashMap<EntityRenderState, LivingEntity> maa_stateToEntity = new IdentityHashMap<>();

    @Inject(method={"updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/EntityRenderState;F)V"}, at={@At(value="TAIL")})
    private void maa_captureEntity(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            this.maa_stateToEntity.put(state, livingEntity);
        }
    }

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"}, at={@At(value="TAIL")})
    private void renderOverlay(LivingEntityRenderState state, MatrixStack matrixStack, OrderedRenderCommandQueue commandQueue, CameraRenderState cameraState, CallbackInfo ci) {
        LivingEntity livingEntity = this.maa_stateToEntity.remove(state);
        if (livingEntity == null) return;
        if ((!Config.HIDE_MARKER || Config.AIM_ASSIST) && Config.TARGET_MARKER && Config.MARKER_TYPE == Config.MarkerType.Frame && this.shouldRenderGlowingOverlay(livingEntity)) {
            RENDER_SWITCH = !RENDER_SWITCH;
            boolean inRange = Utils.isInAttackableRange(MinecraftClient.getInstance().player, livingEntity);
            if (inRange || RENDER_SWITCH) {
                this.drawGlowingOverlay(livingEntity, matrixStack, commandQueue, inRange);
            }
        }
    }

    @Unique
    private boolean shouldRenderGlowingOverlay(LivingEntity entity) {
        Entity target = MaceAttackAssistanceClient.getTargetMob();
        if (target != null) {
            return entity.getUuid().equals(target.getUuid());
        }
        return false;
    }

    @Unique
    private void drawGlowingOverlay(LivingEntity entity, MatrixStack matrices, OrderedRenderCommandQueue commandQueue, boolean inRange) {
        matrices.push();
        matrices.translate(0.0, (double)entity.getHeight() + 0.5 * (double)Config.MARKER_OFFSET, 0.0);
        matrices.scale(1.0f, 1.0f, 1.0f);
        Identifier texture = inRange ? GLOWING_TEXTURE_GREEN : GLOWING_TEXTURE_RED;
        commandQueue.submitCustom(matrices, RenderLayers.eyes(texture), (entry, vertexConsumer) -> {
            Matrix4f positionMatrix = entry.getPositionMatrix();
            this.drawVertex(vertexConsumer, positionMatrix, -0.5f, -0.5f, 0.0f, 0.0f, inRange);
            this.drawVertex(vertexConsumer, positionMatrix, -0.5f, 0.5f, 0.0f, 1.0f, inRange);
            this.drawVertex(vertexConsumer, positionMatrix, 0.5f, 0.5f, 1.0f, 1.0f, inRange);
            this.drawVertex(vertexConsumer, positionMatrix, 0.5f, -0.5f, 1.0f, 0.0f, inRange);
        });
        matrices.pop();
    }

    @Unique
    private void drawVertex(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float z, float u, float v, boolean inRange) {
        if (inRange) {
            vertexConsumer.vertex(matrix, x, 0.0f, z).color(0, 255, 0, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0.0f, 1.0f, 0.0f);
        } else {
            vertexConsumer.vertex(matrix, x, 0.0f, z).color(255, 0, 0, 128).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(0.0f, 1.0f, 0.0f);
        }
    }
}
