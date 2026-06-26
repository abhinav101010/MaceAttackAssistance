/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.components.PlayerTabOverlay
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.renderer.RenderPipelines
 *  net.minecraft.resources.Identifier
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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerTabOverlay.class})
public abstract class MixinPlayerTabOverlay {
    @Unique
    private static final Identifier ICONS_TEXTURE = Identifier.fromNamespaceAndPath((String)"maceattackassistance", (String)"hud/friend");

    @Inject(method={"extractPingIcon"}, at={@At(value="TAIL")})
    private void addHeartIcon(GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info, CallbackInfo ci) {
        if (Config.FRIEND_MARK) {
            UUID uuid = info.getProfile().id();
            if (!FriendManager.isFriend(uuid)) {
                return;
            }
            graphics.pose().pushMatrix();
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ICONS_TEXTURE, xo + slotWidth, yo, 9, 9);
            graphics.pose().popMatrix();
        }
    }
}
