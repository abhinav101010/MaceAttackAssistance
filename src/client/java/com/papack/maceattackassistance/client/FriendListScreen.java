/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Checkbox
 *  net.minecraft.client.gui.components.MultiLineEditBox
 *  net.minecraft.client.gui.components.PlayerFaceExtractor
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.input.CharacterEvent
 *  net.minecraft.client.input.KeyEvent
 *  net.minecraft.client.input.MouseButtonEvent
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.player.PlayerSkin
 *  net.minecraft.world.scores.PlayerTeam
 *  net.minecraft.world.scores.Team
 *  org.jspecify.annotations.NonNull
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.FriendCheckBoxManager;
import com.papack.maceattackassistance.client.FriendManager;
import com.papack.maceattackassistance.client.WorldPlayerData;
import com.papack.maceattackassistance.client.WorldPlayerManager;
import com.papack.maceattackassistance.client.config.Config;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.jspecify.annotations.NonNull;

public class FriendListScreen
extends Screen {
    private final Screen parent;
    private final List<Checkbox> checkboxes = new ArrayList<Checkbox>();
    private final List<WorldPlayerData> allPlayers = new ArrayList<WorldPlayerData>();
    private final List<WorldPlayerData> changedPlayers = new ArrayList<WorldPlayerData>();
    private final int LIST_TOP = 54;
    private MultiLineEditBox searchBox;
    private int scrollOffset = 0;
    private final int ENTRY_HEIGHT = 20;
    private boolean isDraggingScrollbar = false;
    private int dragStartY = 0;
    private int scrollStartOffset = 0;

    public FriendListScreen(Screen parent) {
        super((Component)Component.literal((String)"Friend List Manager"));
        this.parent = parent;
    }

    protected void init() {
        super.init();
        this.checkboxes.clear();
        this.searchBox = MultiLineEditBox.builder().setX(this.width / 2 - 100).setY(30).setPlaceholder((Component)Component.literal((String)"Search...")).setTextColor(0xFFFFFF).setTextShadow(true).setCursorColor(0xFFFFFF).setShowBackground(true).setShowDecorations(false).build(this.font, 200, 20, (Component)Component.literal((String)""));
        this.searchBox.setValueListener(this::updateFilteredList);
        this.addRenderableWidget(this.searchBox);
        HashSet<? extends String> savedFriends = new HashSet<String>(FriendManager.getFriendNameList());
        if (Config.FRIEND_NOT_FOUND) {
            for (String string : savedFriends) {
                if (WorldPlayerManager.isContainsName(string)) continue;
                WorldPlayerManager.addPlayer(string, FriendManager.getUUID(string), null, null, true);
            }
        }
        this.allPlayers.clear();
        this.allPlayers.addAll(WorldPlayerManager.getSortedList());
        this.buildCheckboxes(this.allPlayers);
        this.addRenderableWidget(Button.builder((Component)Component.literal((String)"Done"), ignored -> {
            this.saveChanges();
            this.minecraft.setScreenAndShow(this.parent);
        }).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
        this.updateCheckboxPositions();
    }

    private void saveChanges() {
        for (WorldPlayerData data : this.changedPlayers) {
            if (data.name() == null || data.uuid() == null) continue;
            if (data.friend()) {
                FriendManager.addFriend(data.name(), data.uuid());
                continue;
            }
            FriendManager.removeFriend(data.uuid());
        }
    }

    private void updateCheckboxPositions() {
        int startY = 54 - this.scrollOffset;
        for (int i = 0; i < this.checkboxes.size(); ++i) {
            Checkbox cb = this.checkboxes.get(i);
            cb.setY(startY + i * 20);
            cb.setX(this.width / 2 - 80);
        }
    }

    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        context.text(this.font, this.title, this.width / 2, 15, -1);
        super.extractBackground(context, mouseX, mouseY, delta);
        int listLeft = this.width / 2 - 100;
        int listTop = 54;
        int listHeight = this.height - 80;
        int startY = listTop - this.scrollOffset;
        for (int i = 0; i < this.checkboxes.size(); ++i) {
            Checkbox cb = this.checkboxes.get(i);
            int iconY = startY + i * 20;
            String realName = FriendCheckBoxManager.getRealName(cb.getMessage());
            WorldPlayerData playerData = WorldPlayerManager.getPlayerByName(realName);
            if (playerData != null && playerData.skin() != null) {
                PlayerFaceExtractor.extractRenderState((GuiGraphicsExtractor)context, (PlayerSkin)playerData.skin(), (int)listLeft, (int)iconY, (int)16);
            }
            int checkboxX = listLeft + 20;
            cb.setX(checkboxX);
            cb.setY(iconY);
        }
        int SCROLLBAR_WIDTH = 8;
        int scrollbarX = this.width - SCROLLBAR_WIDTH - 5;
        context.fill(scrollbarX, listTop, scrollbarX + SCROLLBAR_WIDTH, listTop + listHeight, -13421773);
        int contentHeight = this.checkboxes.size() * 20;
        if (contentHeight > listHeight) {
            int thumbHeight = Math.max(listHeight * listHeight / contentHeight, 20);
            int maxScroll = contentHeight - listHeight;
            int thumbY = listTop + (int)((float)this.scrollOffset / (float)maxScroll * (float)(listHeight - thumbHeight));
            context.fill(scrollbarX, thumbY, scrollbarX + SCROLLBAR_WIDTH, thumbY + thumbHeight, -5592406);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int listHeight = this.height - 80;
        int contentHeight = this.checkboxes.size() * 20;
        int maxScroll = Math.max(0, contentHeight - listHeight);
        this.scrollOffset -= (int)(verticalAmount * 10.0);
        if (this.scrollOffset < 0) {
            this.scrollOffset = 0;
        }
        if (this.scrollOffset > maxScroll) {
            this.scrollOffset = maxScroll;
        }
        this.updateCheckboxPositions();
        return true;
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        int screenWidth = this.width;
        int listLeft = screenWidth / 2 - 100;
        int listTop = 40;
        int listHeight = this.height - 80;
        int listRight = listLeft + 200;
        int contentHeight = this.checkboxes.size() * 20;
        if (contentHeight > listHeight) {
            int thumbHeight = Math.max(listHeight * listHeight / contentHeight, 20);
            int maxScroll = contentHeight - listHeight;
            int thumbY = listTop + (int)((float)this.scrollOffset / (float)maxScroll * (float)(listHeight - thumbHeight));
            if ((mouseX >= (double)listRight && mouseX <= (double)screenWidth || mouseX >= 0.0 && mouseX <= (double)listLeft) && mouseY >= (double)thumbY && mouseY <= (double)(thumbY + thumbHeight)) {
                this.isDraggingScrollbar = true;
                this.dragStartY = (int)mouseY;
                this.scrollStartOffset = this.scrollOffset;
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    public boolean mouseReleased(@NonNull MouseButtonEvent click) {
        if (this.isDraggingScrollbar) {
            this.isDraggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(click);
    }

    public boolean mouseDragged(@NonNull MouseButtonEvent click, double offsetX, double offsetY) {
        if (this.isDraggingScrollbar) {
            int listHeight = this.height - 80;
            int contentHeight = this.checkboxes.size() * 20;
            int maxScroll = contentHeight - listHeight;
            int thumbHeight = Math.max(listHeight * listHeight / contentHeight, 20);
            int deltaYMouse = (int)click.y() - this.dragStartY;
            float scrollRatio = (float)maxScroll / (float)(listHeight - thumbHeight);
            this.scrollOffset = this.scrollStartOffset + (int)((float)deltaYMouse * scrollRatio);
            if (this.scrollOffset < 0) {
                this.scrollOffset = 0;
            }
            if (this.scrollOffset > maxScroll) {
                this.scrollOffset = maxScroll;
            }
            this.updateCheckboxPositions();
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    private MutableComponent getColoredDisplayNameWithTeam(String playerName, PlayerTeam team) {
        if (team != null) {
            MutableComponent mutableText = PlayerTeam.formatNameForTeam((Team)team, (Component)Component.literal((String)playerName));
            mutableText.append(" ").append((Component)team.getFormattedDisplayName());
            return mutableText;
        }
        return Component.literal((String)playerName);
    }

    private void buildCheckboxes(List<WorldPlayerData> playerList) {
        for (Checkbox cb : this.checkboxes) {
            this.removeWidget(cb);
        }
        this.checkboxes.clear();
        int y = 54;
        FriendCheckBoxManager.clear();
        for (WorldPlayerData playerData : playerList) {
            String playerName = playerData.name();
            boolean isChanged = this.isChangedPlayerListContainsName(playerName);
            boolean changedValue = isChanged && this.getChangedData(playerName);
            boolean checked = isChanged ? changedValue : playerData.friend();
            PlayerTeam team = WorldPlayerManager.getTeam(playerData.uuid());
            MutableComponent displayName = this.getColoredDisplayNameWithTeam(playerName, team);
            FriendCheckBoxManager.addCheckBoxData(playerName, (Component)displayName);
            Checkbox cb = Checkbox.builder((Component)displayName, (Font)this.font).pos(this.width / 2 - 80, y).selected(checked).onValueChange(this::changeCheckBoxCallback).build();
            this.checkboxes.add(cb);
            this.addRenderableWidget(cb);
            y += 20;
        }
        this.addRenderableWidget(Button.builder((Component)Component.literal((String)"Done"), ignored -> {
            this.saveChanges();
            this.minecraft.setScreenAndShow(this.parent);
        }).bounds(this.width / 2 - 50, this.height - 30, 100, 20).build());
        this.updateCheckboxPositions();
    }

    private void updateFilteredList(String query) {
        if (query == null || query.isEmpty()) {
            this.buildCheckboxes(this.allPlayers);
        } else {
            List<WorldPlayerData> filtered = this.allPlayers.stream().filter(p -> p.name().toLowerCase().contains(query.toLowerCase()) || p.team() != null && p.team().getName().toLowerCase().contains(query.toLowerCase())).toList();
            this.buildCheckboxes(filtered);
        }
    }

    public boolean keyPressed(@NonNull KeyEvent input) {
        if (this.searchBox.keyPressed(input)) {
            return true;
        }
        return super.keyPressed(input);
    }

    public boolean charTyped(@NonNull CharacterEvent inputs) {
        if (this.searchBox.charTyped(inputs)) {
            return true;
        }
        return super.charTyped(inputs);
    }

    private void changeCheckBoxCallback(Checkbox widget, boolean checked) {
        String realName = FriendCheckBoxManager.getRealName(widget.getMessage());
        WorldPlayerData playerData = WorldPlayerManager.getPlayerByName(realName);
        boolean isContain = this.isChangedPlayerListContainsName(realName);
        if (playerData != null) {
            if (isContain) {
                this.removePlayerByName(realName);
            }
            this.changedPlayers.add(new WorldPlayerData(playerData.name(), playerData.uuid(), playerData.skin(), playerData.team(), checked));
        }
    }

    private boolean isChangedPlayerListContainsName(String realName) {
        return this.changedPlayers.stream().anyMatch(f -> f.name().equals(realName));
    }

    private boolean getChangedData(String realName) {
        for (WorldPlayerData data : this.changedPlayers) {
            if (!Objects.equals(realName, data.name())) continue;
            return data.friend();
        }
        return false;
    }

    public void removePlayerByName(String realName) {
        this.changedPlayers.removeIf(p -> p.name().equals(realName));
    }
}
