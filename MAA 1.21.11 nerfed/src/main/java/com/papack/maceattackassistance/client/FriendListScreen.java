/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.CharInput
 *  net.minecraft.KeyInput
 *  net.minecraft.Click
 *  net.minecraft.Text
 *  net.minecraft.Team
 *  net.minecraft.AbstractTeam
 *  net.minecraft.TextRenderer
 *  net.minecraft.DrawContext
 *  net.minecraft.Element
 *  net.minecraft.ButtonWidget
 *  net.minecraft.CheckboxWidget
 *  net.minecraft.Screen
 *  net.minecraft.MutableText
 *  net.minecraft.EditBoxWidget
 *  net.minecraft.PlayerSkinDrawer
 *  net.minecraft.SkinTextures
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
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.entity.player.SkinTextures;

public class FriendListScreen
extends Screen {
    private final Screen parent;
    private final List<CheckboxWidget> checkboxes = new ArrayList<CheckboxWidget>();
    private final List<WorldPlayerData> allPlayers = new ArrayList<WorldPlayerData>();
    private final List<WorldPlayerData> changedPlayers = new ArrayList<WorldPlayerData>();
    private final int LIST_TOP = 54;
    private EditBoxWidget searchBox;
    private int scrollOffset = 0;
    private final int ENTRY_HEIGHT = 20;
    private boolean isDraggingScrollbar = false;
    private int dragStartY = 0;
    private int scrollStartOffset = 0;

    public FriendListScreen(Screen parent) {
        super((Text)Text.literal((String)"Friend List Manager"));
        this.parent = parent;
    }

    protected void init() {
        super.init();
        this.checkboxes.clear();
        this.searchBox = EditBoxWidget.builder().x(this.width / 2 - 100).y(30).placeholder((Text)Text.literal((String)"Search...")).textColor(0xFFFFFF).textShadow(true).cursorColor(0xFFFFFF).hasBackground(true).hasOverlay(false).build(this.textRenderer, 200, 20, (Text)Text.literal((String)""));
        this.searchBox.setChangeListener(this::updateFilteredList);
        this.addDrawableChild(this.searchBox);
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
        this.addDrawableChild(ButtonWidget.builder((Text)Text.literal((String)"Done"), elementCodec -> {
            this.saveChanges();
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build());
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
            CheckboxWidget cb = this.checkboxes.get(i);
            cb.setY(startY + i * 20);
            cb.setX(this.width / 2 - 80);
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, -1);
        super.render(context, mouseX, mouseY, delta);
        int listLeft = this.width / 2 - 100;
        int listTop = 54;
        int listHeight = this.height - 80;
        int startY = listTop - this.scrollOffset;
        for (int i = 0; i < this.checkboxes.size(); ++i) {
            CheckboxWidget cb = this.checkboxes.get(i);
            int iconY = startY + i * 20;
            String realName = FriendCheckBoxManager.getRealName(cb.getMessage());
            WorldPlayerData playerData = WorldPlayerManager.getPlayerByName(realName);
            if (playerData != null && playerData.skin() != null) {
                PlayerSkinDrawer.draw((DrawContext)context, (SkinTextures)playerData.skin(), (int)listLeft, (int)iconY, (int)16);
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

    public boolean mouseClicked(Click click, boolean doubled) {
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

    public boolean mouseReleased(Click click) {
        if (this.isDraggingScrollbar) {
            this.isDraggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(click);
    }

    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
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

    private MutableText getColoredDisplayNameWithTeam(String playerName, Team team) {
        if (team != null) {
            MutableText mutableText = Team.decorateName((AbstractTeam)team, (Text)Text.literal((String)playerName));
            mutableText.append(" ").append((Text)team.getFormattedName());
            return mutableText;
        }
        return Text.literal((String)playerName);
    }

    private void buildCheckboxes(List<WorldPlayerData> playerList) {
        for (CheckboxWidget cb : this.checkboxes) {
            this.remove((Element)cb);
        }
        this.checkboxes.clear();
        int y = 54;
        FriendCheckBoxManager.clear();
        for (WorldPlayerData playerData : playerList) {
            String playerName = playerData.name();
            boolean isChanged = this.isChangedPlayerListContainsName(playerName);
            boolean changedValue = isChanged && this.getChangedData(playerName);
            boolean checked = isChanged ? changedValue : playerData.friend();
            Team team = WorldPlayerManager.getTeam(playerData.uuid());
            MutableText displayName = this.getColoredDisplayNameWithTeam(playerName, team);
            FriendCheckBoxManager.addCheckBoxData(playerName, (Text)displayName);
            CheckboxWidget cb = CheckboxWidget.builder((Text)displayName, (TextRenderer)this.textRenderer).pos(this.width / 2 - 80, y).checked(checked).callback(this::changeCheckBoxCallback).build();
            this.checkboxes.add(cb);
            this.addDrawableChild(cb);
            y += 20;
        }
        this.addDrawableChild(ButtonWidget.builder((Text)Text.literal((String)"Done"), elementCodec -> {
            this.saveChanges();
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build());
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

    public boolean keyPressed(KeyInput input) {
        if (this.searchBox.keyPressed(input)) {
            return true;
        }
        return super.keyPressed(input);
    }

    public boolean charTyped(CharInput inputs) {
        if (this.searchBox.charTyped(inputs)) {
            return true;
        }
        return super.charTyped(inputs);
    }

    private void changeCheckBoxCallback(CheckboxWidget widget, boolean checked) {
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
