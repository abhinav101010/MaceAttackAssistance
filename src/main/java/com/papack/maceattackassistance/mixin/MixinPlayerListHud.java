/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.RenderPipelines
 *  net.minecraft.Identifier
 *  net.minecraft.DrawContext
 *  net.minecraft.PlayerListHud
 *  net.minecraft.PlayerListEntry
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.papack.maceattackassistance.mixin;

import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.config.Config;
import java.util.UUID;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerListHud.class})
public abstract class MixinPlayerListHud {
    @Unique
    private static final Identifier ICONS_TEXTURE = Identifier.of((String)"maceattackassistance", (String)"hud/friend");

    @Inject(method={"renderLatencyIcon"}, at={@At(value="TAIL")})
    private void addHeartIcon(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if (Config.FRIEND_MARK) {
            UUID uuid = entry.getProfile().id();
            if (!FriendManager.isFriend(uuid)) {
                return;
            }
            context.getMatrices().pushMatrix();
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, ICONS_TEXTURE, x + width, y, 9, 9);
            context.getMatrices().popMatrix();
        }
    }
}
