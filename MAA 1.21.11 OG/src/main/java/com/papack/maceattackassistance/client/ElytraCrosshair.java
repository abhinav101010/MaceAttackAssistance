package com.papack.maceattackassistance.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class ElytraCrosshair {
    private static final Identifier ELYTRA_CROSSHAIR_TEXTURE = Identifier.of("maceattackassistance", "textures/elytra_crosshair.png");
    private static final int CROSSHAIR_SIZE = 15;

    public static boolean isElytraEquipped(ClientPlayerEntity player) {
        ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
        return chestStack.isOf(Items.ELYTRA);
    }

    public static void render(DrawContext context, MinecraftClient client) {
        int x = (context.getScaledWindowWidth() - CROSSHAIR_SIZE) / 2;
        int y = (context.getScaledWindowHeight() - CROSSHAIR_SIZE) / 2;
        context.drawTexture(RenderPipelines.CROSSHAIR, ELYTRA_CROSSHAIR_TEXTURE, x, y, 0.0f, 0.0f, CROSSHAIR_SIZE, CROSSHAIR_SIZE, CROSSHAIR_SIZE, CROSSHAIR_SIZE);
    }
}
