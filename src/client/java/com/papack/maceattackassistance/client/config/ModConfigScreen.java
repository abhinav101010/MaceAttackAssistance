/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.shedaniel.clothconfig2.api.AbstractConfigListEntry
 *  me.shedaniel.clothconfig2.api.ConfigBuilder
 *  me.shedaniel.clothconfig2.api.ConfigCategory
 *  me.shedaniel.clothconfig2.api.ConfigEntryBuilder
 *  me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder
 *  me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder
 *  me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder
 *  me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package com.papack.maceattackassistance.client.config;

import com.papack.maceattackassistance.client.ColorData;
import com.papack.maceattackassistance.client.MAAState;
import com.papack.maceattackassistance.client.config.Config;
import com.papack.maceattackassistance.client.config.ConfigOperation;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(value=EnvType.CLIENT)
public class ModConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        try {
            return getClothConfigScreen(parent);
        } catch (NoClassDefFoundError e) {
            // ClothConfig2 not available at runtime, show placeholder
            return new PlaceholderScreen(parent);
        }
    }

    private static Screen getClothConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle((Component)Component.translatable((String)"config.mace_attack_assistance.title"));
        builder.setGlobalized(Config.DEFAULT_EXPANDED_GLOBAL);
        builder.setGlobalizedExpanded(true);
        ConfigCategory attack = builder.getOrCreateCategory((Component)Component.literal((String)"Attack 1").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory attack2 = builder.getOrCreateCategory((Component)Component.literal((String)"Attack 2").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory aim = builder.getOrCreateCategory((Component)Component.literal((String)"Aim").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory jump = builder.getOrCreateCategory((Component)Component.literal((String)"Jump").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory elytra = builder.getOrCreateCategory((Component)Component.literal((String)"Elytra").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory item = builder.getOrCreateCategory((Component)Component.literal((String)"Item").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory slot = builder.getOrCreateCategory((Component)Component.literal((String)"Slot").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory recon = builder.getOrCreateCategory((Component)Component.literal((String)"Recon").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory friend = builder.getOrCreateCategory((Component)Component.literal((String)"Friend").withStyle(s -> s.withColor(ChatFormatting.GREEN)));
        ConfigCategory effects = builder.getOrCreateCategory((Component)Component.literal((String)"Effects,Marker").withStyle(s -> s.withColor(ChatFormatting.AQUA)));
        ConfigCategory spiral = builder.getOrCreateCategory((Component)Component.literal((String)"SpiralMaker").withStyle(s -> s.withColor(ChatFormatting.AQUA)));
        ConfigCategory color = builder.getOrCreateCategory((Component)Component.literal((String)"Spiral,Beam Color").withStyle(s -> s.withColor(ChatFormatting.AQUA)));
        ConfigCategory fov = builder.getOrCreateCategory((Component)Component.literal((String)"Search Range,Angle").withStyle(s -> s.withColor(ChatFormatting.AQUA)));
        ConfigCategory others = builder.getOrCreateCategory((Component)Component.literal((String)"Others").withStyle(s -> s.withColor(ChatFormatting.GRAY)));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        SubCategoryBuilder attackAssist = entryBuilder.startSubCategory((Component)Component.literal((String)"Attack Assist")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        attackAssist.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.attack_assistance"), Config.ATTACK_ASSISTANCE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ATTACK_ASSISTANCE))).setSaveConsumer(newValue -> {
            Config.ATTACK_ASSISTANCE = newValue;
        }).build());
        attackAssist.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.height_threshold"), Config.HEIGHT_THRESHOLD, 2, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HEIGHT_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.HEIGHT_THRESHOLD = newValue;
        }).build());
        attackAssist.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.weapon_swing"), Config.WEAPON_SWING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WEAPON_SWING))).setSaveConsumer(newValue -> {
            Config.WEAPON_SWING = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.weapon_swing")}).build());
        attackAssist.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.swing_toggle"), Config.SwingToggle.class, Config.SWING_TOGGLE).setDefaultValue(Config.SwingToggle.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SWING_TOGGLE))).setSaveConsumer(newValue -> {
            Config.SWING_TOGGLE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.swing_toggle")}).build());
        SubCategoryBuilder doubleTap = entryBuilder.startSubCategory((Component)Component.literal((String)"Double Tap")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        doubleTap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.double_tap"), Config.DOUBLE_TAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DOUBLE_TAP))).setSaveConsumer(newValue -> {
            Config.DOUBLE_TAP = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.double_tap")}).build());
        doubleTap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.double_tap_auto_left_click"), Config.AUTO_LEFT_CLICK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_LEFT_CLICK))).setSaveConsumer(newValue -> {
            Config.AUTO_LEFT_CLICK = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.double_tap_auto_left_click")}).build());
        doubleTap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.dt_aerial_dive_mode"), Config.DT_AERIAL_DIVE_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DT_AERIAL_DIVE_MODE))).setSaveConsumer(newValue -> {
            Config.DT_AERIAL_DIVE_MODE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.dt_aerial_dive_mode")}).build());
        doubleTap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.double_tap_rewear"), Config.DOUBLE_TAP_REWEAR).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DOUBLE_TAP_REWEAR))).setSaveConsumer(newValue -> {
            Config.DOUBLE_TAP_REWEAR = newValue;
        }).build());
        SubCategoryBuilder hotSwap = entryBuilder.startSubCategory((Component)Component.literal((String)"Hot Swap")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        hotSwap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.hot_swap"), Config.HOT_SWAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HOT_SWAP))).setSaveConsumer(newValue -> {
            Config.HOT_SWAP = newValue;
        }).build());
        hotSwap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.spear_assist"), Config.SPEAR_ASSIST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_ASSIST))).setSaveConsumer(newValue -> {
            Config.SPEAR_ASSIST = newValue;
        }).build());
        SubCategoryBuilder stunSlam = entryBuilder.startSubCategory((Component)Component.literal((String)"Stun Slam")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        stunSlam.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.stun_slamming"), Config.STUN_SLAMMING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_STUN_SLAMMING))).setSaveConsumer(newValue -> {
            Config.STUN_SLAMMING = newValue;
        }).build());
        SubCategoryBuilder breachSwap = entryBuilder.startSubCategory((Component)Component.literal((String)"Breach Swap, Sword Swap")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        breachSwap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.breach_swap"), Config.BREACH_SWAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BREACH_SWAP))).setSaveConsumer(newValue -> {
            Config.BREACH_SWAP = newValue;
        }).build());
        breachSwap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.breach_limited"), Config.BREACH_LIMITED).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BREACH_LIMITED))).setSaveConsumer(newValue -> {
            Config.BREACH_LIMITED = newValue;
        }).build());
        breachSwap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.breach_on_ground"), Config.BREACH_ON_GROUND).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BREACH_ON_GROUND))).setSaveConsumer(newValue -> {
            Config.BREACH_ON_GROUND = newValue;
        }).build());
        breachSwap.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.literal((String)"Armored Target Behavior"), Config.Behavior.class, Config.SWORD_SWAP_OR_BREACH_SWAP).setDefaultValue(Config.Behavior.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SWORD_SWAP_OR_BREACH_SWAP))).setSaveConsumer(newValue -> {
            Config.SWORD_SWAP_OR_BREACH_SWAP = newValue;
        }).setTooltip(new Component[]{Component.literal((String)"It does not affect snapback.\nBreach Swap: any to Breach Mace\nSword Swap: any to Sword(or Axe)")}).build());
        breachSwap.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.literal((String)"UnArmored Target Behavior"), Config.Behavior.class, Config.BEHAVIOR_NOT_WEARING_ARMOR).setDefaultValue(Config.Behavior.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BEHAVIOR_NOT_WEARING_ARMOR))).setSaveConsumer(newValue -> {
            Config.BEHAVIOR_NOT_WEARING_ARMOR = newValue;
        }).build());
        breachSwap.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Sword Swap Mode Option"), Config.SWORD_OR_AXE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SWORD_OR_AXE))).setSaveConsumer(newValue -> {
            Config.SWORD_OR_AXE = newValue;
        }).setYesNoTextSupplier(value -> value != false ? Component.literal((String)"Sword") : Component.literal((String)"Axe")).build());
        SubCategoryBuilder snapback = entryBuilder.startSubCategory((Component)Component.literal((String)"Snapback")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        snapback.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.snapback"), Config.SNAPBACK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SNAPBACK))).setSaveConsumer(newValue -> {
            Config.SNAPBACK = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.snapback")}).build());
        snapback.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.snapback_threshold"), Config.SNAPBACK_THRESHOLD, 4, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SNAPBACK_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.SNAPBACK_THRESHOLD = newValue;
        }).build());
        snapback.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Snapback - Tolerance"), Config.SNAPBACK_TOLERANCE, 0, 500).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SNAPBACK_TOLERANCE))).setSaveConsumer(newValue -> {
            Config.SNAPBACK_TOLERANCE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.snapback_tolerance")}).build());
        SubCategoryBuilder shieldDraining = entryBuilder.startSubCategory((Component)Component.literal((String)"Shield Draining")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        shieldDraining.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Auto Shield Draining"), Config.AUTO_SHIELD_DRAINING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_SHIELD_DRAINING))).setSaveConsumer(newValue -> {
            Config.AUTO_SHIELD_DRAINING = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.auto_shield_draining")}).build());
        shieldDraining.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Instant Shield Draining"), Config.INSTANT_SHIELD_DRAINING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_INSTANT_SHIELD_DRAINING))).setSaveConsumer(newValue -> {
            Config.INSTANT_SHIELD_DRAINING = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.instant_shield_draining")}).build());
        SubCategoryBuilder weaponSlotSetting = entryBuilder.startSubCategory((Component)Component.literal((String)"Weapon Slot Setting")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        weaponSlotSetting.add((AbstractConfigListEntry)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.axe_slot"), Config.AXE_SLOT + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AXE_SLOT) + 1).setSaveConsumer(newValue -> {
            Config.AXE_SLOT = newValue - 1;
        }).build());
        weaponSlotSetting.add((AbstractConfigListEntry)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.mace_slot_primary"), Config.MACE_PRIMARY + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MACE_PRIMARY) + 1).setSaveConsumer(newValue -> {
            Config.MACE_PRIMARY = newValue - 1;
        }).build());
        weaponSlotSetting.add((AbstractConfigListEntry)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.mace_slot_breach"), Config.MACE_BREACH + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MACE_BREACH) + 1).setSaveConsumer(newValue -> {
            Config.MACE_BREACH = newValue - 1;
        }).build());
        weaponSlotSetting.add((AbstractConfigListEntry)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.lunge_execution_slot"), Config.LUNGE_EXECUTION_SLOT + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_LUNGE_EXECUTION_SLOT) + 1).setSaveConsumer(newValue -> {
            Config.LUNGE_EXECUTION_SLOT = newValue - 1;
        }).build());
        weaponSlotSetting.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.keep_after_execution"), Config.KEEP_AFTER_EXECUTION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_KEEP_AFTER_EXECUTION))).setSaveConsumer(newValue -> {
            Config.KEEP_AFTER_EXECUTION = newValue;
        }).build());
        weaponSlotSetting.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.forced_execution_slot"), Config.FORCED_EXECUTION_SLOT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FORCED_EXECUTION_SLOT))).setSaveConsumer(newValue -> {
            Config.FORCED_EXECUTION_SLOT = newValue;
        }).build());
        SubCategoryBuilder returnToPrevSlot = entryBuilder.startSubCategory((Component)Component.literal((String)"Return To PrevSlot")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        returnToPrevSlot.add((AbstractConfigListEntry)entryBuilder.startIntSlider((Component)Component.literal((String)"Return Mode [0:Auto], [1-9:Specify]"), Config.RETURN_TO_PREV_SLOT_MODE + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_MODE) + 1).setSaveConsumer(newValue -> {
            Config.RETURN_TO_PREV_SLOT_MODE = newValue - 1;
        }).build());
        returnToPrevSlot.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.return_to_prev_slot"), Config.RETURN_TO_PREV_SLOT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RETURN_TO_PREV_SLOT))).setSaveConsumer(newValue -> {
            Config.RETURN_TO_PREV_SLOT = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.return_to_prev_slot")}).build());
        returnToPrevSlot.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.return_to_prev_slot_breach"), Config.RETURN_TO_PREV_SLOT_BREACH).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_BREACH))).setSaveConsumer(newValue -> {
            Config.RETURN_TO_PREV_SLOT_BREACH = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.return_to_prev_slot")}).build());
        returnToPrevSlot.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Delay Execution"), Config.PREV_SLOT_DELAY, 0, 3).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PREV_SLOT_DELAY))).setSaveConsumer(newValue -> {
            Config.PREV_SLOT_DELAY = newValue;
        }).build());
        SubCategoryBuilder aimAssist = entryBuilder.startSubCategory((Component)Component.literal((String)"Aim Assist")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        aimAssist.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.aim_assist"), Config.AIM_ASSIST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_ASSIST))).setSaveConsumer(newValue -> {
            Config.AIM_ASSIST = newValue;
        }).build());
        aimAssist.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.legacy_aim_mode"), Config.LEGACY_AIM_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_LEGACY_AIM_MODE))).setSaveConsumer(newValue -> {
            Config.LEGACY_AIM_MODE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.legacy_aim_mode")}).build());
        aimAssist.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Aim Assist (Elytra)"), Config.AIM_ELYTRA).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_ELYTRA))).setSaveConsumer(newValue -> {
            Config.AIM_ELYTRA = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.aim_elytra")}).build());
        SubCategoryBuilder aimTarget = entryBuilder.startSubCategory((Component)Component.literal((String)"Target")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        aimTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_villager"), Config.ALLOWED_VILLAGER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_VILLAGER))).setSaveConsumer(newValue -> {
            Config.ALLOWED_VILLAGER = newValue;
        }).build());
        aimTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_iron_golem"), Config.ALLOWED_IRON_GOLEM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_IRON_GOLEM))).setSaveConsumer(newValue -> {
            Config.ALLOWED_IRON_GOLEM = newValue;
        }).build());
        aimTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_hostile"), Config.ALLOWED_HOSTILE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_HOSTILE))).setSaveConsumer(newValue -> {
            Config.ALLOWED_HOSTILE = newValue;
        }).build());
        aimTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_passive"), Config.ALLOWED_PASSIVE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_PASSIVE))).setSaveConsumer(newValue -> {
            Config.ALLOWED_PASSIVE = newValue;
        }).build());
        aimTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_ambient"), Config.ALLOWED_AMBIENT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_AMBIENT))).setSaveConsumer(newValue -> {
            Config.ALLOWED_AMBIENT = newValue;
        }).build());
        aimTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_player"), Config.ALLOWED_PLAYER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_PLAYER))).setSaveConsumer(newValue -> {
            Config.ALLOWED_PLAYER = newValue;
        }).build());
        SubCategoryBuilder aimOption = entryBuilder.startSubCategory((Component)Component.literal((String)"Aim Option")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        aimOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.forced_adjustment"), Config.AIM_FORCED_ADJUSTMENT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_FORCED_ADJUSTMENT))).setSaveConsumer(newValue -> {
            Config.AIM_FORCED_ADJUSTMENT = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.forced_adjustment")}).build());
        aimOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.max_speed_yaw"), Config.MAX_SPEED_YAW, 0, 15).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MAX_SPEED_YAW))).setSaveConsumer(newValue -> {
            Config.MAX_SPEED_YAW = newValue;
        }).build());
        aimOption.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.aim_mode"), Config.AimMode.class, Config.AIM_MODE).setDefaultValue(Config.AimMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_MODE))).setSaveConsumer(newValue -> {
            Config.AIM_MODE = newValue;
        }).build());
        aimOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.auto_mode_threshold"), Config.AUTO_MODE_THRESHOLD, 10, 30).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_MODE_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.AUTO_MODE_THRESHOLD = newValue;
        }).build());
        aimOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Starting Threshold"), Config.AIM_FALL_THRESHOLD, 2, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_FALL_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.AIM_FALL_THRESHOLD = newValue;
        }).build());
        aimOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"(Experimental) Raycast Search Mode"), Config.AIM_RAYCAST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_RAYCAST))).setSaveConsumer(newValue -> {
            Config.AIM_RAYCAST = newValue;
        }).build());
        aimOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"(Experimental) Raycast : Range"), Config.AIM_RAYCAST_RANGE, 5, 30).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_RAYCAST_RANGE))).setSaveConsumer(newValue -> {
            Config.AIM_RAYCAST_RANGE = newValue;
        }).build());
        aimOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"(Experimental) Raycast : Radius"), Config.AIM_RAYCAST_RADIUS, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_RAYCAST_RADIUS))).setSaveConsumer(newValue -> {
            Config.AIM_RAYCAST_RADIUS = newValue;
        }).build());
        SubCategoryBuilder jumpAssist = entryBuilder.startSubCategory((Component)Component.literal((String)"Jump Assist")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        jumpAssist.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.jump_assist"), Config.JUMP_ASSIST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_ASSIST))).setSaveConsumer(newValue -> {
            Config.JUMP_ASSIST = newValue;
        }).build());
        SubCategoryBuilder wallClimbing = entryBuilder.startSubCategory((Component)Component.literal((String)"wallClimbing")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        wallClimbing.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.wall_climbing"), Config.WALL_CLIMBING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WALL_CLIMBING))).setSaveConsumer(newValue -> {
            Config.WALL_CLIMBING = newValue;
        }).build());
        SubCategoryBuilder autoWindChargeSelector = entryBuilder.startSubCategory((Component)Component.literal((String)"Auto WindCharge Selector")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        autoWindChargeSelector.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Auto WindCharge Selector"), Config.AUTO_WIND_CHARGE_SELECT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_WIND_CHARGE_SELECT))).setSaveConsumer(newValue -> {
            Config.AUTO_WIND_CHARGE_SELECT = newValue;
        }).build());
        SubCategoryBuilder pearlCatch = entryBuilder.startSubCategory((Component)Component.literal((String)"Pearl Catch")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        pearlCatch.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Pearl Catch Mode"), Config.PEARL_CATCH_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_MODE))).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_MODE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_mode")}).setYesNoTextSupplier(value -> value != false ? Component.literal((String)"Auto") : Component.literal((String)"Manual")).build());
        pearlCatch.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Pearl Catch (Auto, Upward) - Camera Adjustment"), Config.PEARL_CATCH_ADJUST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_ADJUST))).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_ADJUST = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_camera_adjustment")}).build());
        pearlCatch.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Pearl Catch (Auto, Upward) - Add Delay"), Config.PEARL_CATCH_DELAY, 0, 2).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_DELAY))).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_DELAY = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_delay")}).build());
        pearlCatch.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Pearl Catch Upward Angle"), Config.PEARL_CATCH_UPWARD_ANGLE, -90, -15).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_UPWARD_ANGLE))).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_UPWARD_ANGLE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_upward_angle")}).build());
        pearlCatch.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Pearl Catch Downward Angle"), Config.PEARL_CATCH_DOWNWARD_ANGLE, 45, 90).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_DOWNWARD_ANGLE))).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_DOWNWARD_ANGLE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_downward_angle")}).build());
        pearlCatch.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Always Use The Set Angle (Upward)"), Config.ALWAYS_USE_UPWARD_ANGLE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALWAYS_USE_UPWARD_ANGLE))).setSaveConsumer(newValue -> {
            Config.ALWAYS_USE_UPWARD_ANGLE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_always_use_set_angle")}).build());
        pearlCatch.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Always Use The Set Angle (Downward)"), Config.ALWAYS_USE_DOWNWARD_ANGLE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALWAYS_USE_DOWNWARD_ANGLE))).setSaveConsumer(newValue -> {
            Config.ALWAYS_USE_DOWNWARD_ANGLE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.pearl_catch_always_use_set_angle")}).build());
        SubCategoryBuilder toggleElytraOption = entryBuilder.startSubCategory((Component)Component.literal((String)"Toggle Elytra Option")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        toggleElytraOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Toggle Elytra Input Method"), Config.ELYTRA_MANUAL_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ELYTRA_MANUAL_MODE))).setSaveConsumer(newValue -> {
            Config.ELYTRA_MANUAL_MODE = newValue;
        }).setYesNoTextSupplier(value -> value != false ? Component.literal((String)"Simulated") : Component.literal((String)"Direct")).build());
        toggleElytraOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Toggle Elytra - Equipment Search Range"), Config.ALSO_SEARCH_INVENTORY).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALSO_SEARCH_INVENTORY))).setSaveConsumer(newValue -> {
            Config.ALSO_SEARCH_INVENTORY = newValue;
        }).setTooltip(new Component[]{Component.literal((String)"(Only Input Method:Direct) The inventory will also be searched.")}).setYesNoTextSupplier(value -> value != false ? Component.literal((String)"Hotbar + Inventory") : Component.literal((String)"Hotbar")).build());
        toggleElytraOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.toggle_elytra"), Config.TOGGLE_SLOT, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_TOGGLE_SLOT))).setSaveConsumer(newValue -> {
            Config.TOGGLE_SLOT = newValue;
        }).build());
        SubCategoryBuilder rocketBlitz = entryBuilder.startSubCategory((Component)Component.literal((String)"Rocket Blitz")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        rocketBlitz.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.rocket_blitz"), Config.ROCKET_BLITZ).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_BLITZ))).setSaveConsumer(newValue -> {
            Config.ROCKET_BLITZ = newValue;
        }).build());
        rocketBlitz.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.rocket_blitz_slot"), Config.ROCKET_BLITZ_SLOT, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_BLITZ_SLOT))).setSaveConsumer(newValue -> {
            Config.ROCKET_BLITZ_SLOT = newValue;
        }).build());
        rocketBlitz.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.rocket_blitz_empty_slot"), Config.ROCKET_BLITZ_CAN_EMPTY_SLOT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_BLITZ_CAN_EMPTY_SLOT))).setSaveConsumer(newValue -> {
            Config.ROCKET_BLITZ_CAN_EMPTY_SLOT = newValue;
        }).build());
        rocketBlitz.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.rocket_trigger"), Config.RocketTrigger.class, Config.ROCKET_TRIGGER).setDefaultValue(Config.RocketTrigger.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_TRIGGER))).setSaveConsumer(newValue -> {
            Config.ROCKET_TRIGGER = newValue;
        }).build());
        rocketBlitz.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.rocket_trigger_spear"), Config.RocketTrigger.class, Config.ROCKET_TRIGGER_SPEAR).setDefaultValue(Config.RocketTrigger.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_TRIGGER_SPEAR))).setSaveConsumer(newValue -> {
            Config.ROCKET_TRIGGER_SPEAR = newValue;
        }).build());
        SubCategoryBuilder jumpSpam = entryBuilder.startSubCategory((Component)Component.literal((String)"Jump Spam")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        jumpSpam.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.jump_spam"), Config.JUMP_SPAM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_SPAM))).setSaveConsumer(newValue -> {
            Config.JUMP_SPAM = newValue;
        }).build());
        jumpSpam.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.jump_spam_tick"), Config.JUMP_SPAM_TICK, 2, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_SPAM_TICK))).setSaveConsumer(newValue -> {
            Config.JUMP_SPAM_TICK = newValue;
        }).build());
        jumpSpam.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Jump Spam Threshold"), Config.JUMP_SPAM_THRESHOLD, 0, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_SPAM_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.JUMP_SPAM_THRESHOLD = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.jump_spam_threshold")}).build());
        SubCategoryBuilder elytraBoost = entryBuilder.startSubCategory((Component)Component.literal((String)"Elytra Boost")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        elytraBoost.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Elytra Boost"), Config.ELYTRA_BOOST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ELYTRA_BOOST))).setSaveConsumer(newValue -> {
            Config.ELYTRA_BOOST = newValue;
        }).build());
        elytraBoost.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Elytra Boost - Reflection Angle"), Config.REFLECTION_ANGLE, -60, -10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_REFLECTION_ANGLE))).setSaveConsumer(newValue -> {
            Config.REFLECTION_ANGLE = newValue;
        }).build());
        SubCategoryBuilder prioritizeUse = entryBuilder.startSubCategory((Component)Component.literal((String)"Prioritize Use")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        prioritizeUse.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Jump Assist / Elytra Boost - Prioritize Use Wind Charge"), Config.PRIORITIZE_WIND_CHARGE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE))).setSaveConsumer(newValue -> {
            Config.PRIORITIZE_WIND_CHARGE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.prioritize")}).build());
        prioritizeUse.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Rocket Blitz - Prioritize Use Fireworks Rocket"), Config.PRIORITIZE_ROCKET).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PRIORITIZE_ROCKET))).setSaveConsumer(newValue -> {
            Config.PRIORITIZE_ROCKET = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.prioritize")}).build());
        SubCategoryBuilder jumpMode = entryBuilder.startSubCategory((Component)Component.literal((String)"Jump Mode")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        jumpMode.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.in_use_elytra_jump_mode"), Config.JumpMode.class, Config.IN_USE_ELYTRA_JUMP_MODE).setDefaultValue(Config.JumpMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_IN_USE_ELYTRA_JUMP_MODE))).setSaveConsumer(newValue -> {
            Config.IN_USE_ELYTRA_JUMP_MODE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.in_use_elytra_jump_mode")}).build());
        jumpMode.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.not_in_use_elytra_jump_mode"), Config.JumpMode.class, Config.NOT_IN_USE_ELYTRA_JUMP_MODE).setDefaultValue(Config.JumpMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_NOT_USE_ELYTRA_JUMP_MODE))).setSaveConsumer(newValue -> {
            Config.NOT_IN_USE_ELYTRA_JUMP_MODE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.not_in_use_elytra_jump_mode")}).build());
        SubCategoryBuilder autoRefill = entryBuilder.startSubCategory((Component)Component.literal((String)"Auto Refill")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        autoRefill.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.auto_refill"), Config.AUTO_REFILL).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_REFILL))).setSaveConsumer(newValue -> {
            Config.AUTO_REFILL = newValue;
        }).build());
        SubCategoryBuilder markerType = entryBuilder.startSubCategory((Component)Component.literal((String)"Marker Type")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        markerType.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.literal((String)"Target Marker Type"), Config.MarkerType.class, Config.MARKER_TYPE).setDefaultValue(Config.MarkerType.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MARKER_TYPE))).setSaveConsumer(newValue -> {
            Config.MARKER_TYPE = newValue;
        }).build());
        SubCategoryBuilder targetSearchMode = entryBuilder.startSubCategory((Component)Component.literal((String)"Target Search Mode")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        targetSearchMode.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.target_search_mode"), Config.TARGET_SEARCH_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_TARGET_SEARCH_MODE))).setSaveConsumer(newValue -> {
            Config.TARGET_SEARCH_MODE = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.target_search_mode")}).build());
        SubCategoryBuilder displayMarkerOnTarget = entryBuilder.startSubCategory((Component)Component.literal((String)"Display Marker")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        displayMarkerOnTarget.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.on_target"), Config.TARGET_MARKER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_TARGET_MARKER))).setSaveConsumer(newValue -> {
            Config.TARGET_MARKER = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.on_target")}).build());
        SubCategoryBuilder hideMarkerWhenAimOff = entryBuilder.startSubCategory((Component)Component.literal((String)"Hide Marker")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        hideMarkerWhenAimOff.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.hide_marker"), Config.HIDE_MARKER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HIDE_MARKER))).setSaveConsumer(newValue -> {
            Config.HIDE_MARKER = newValue;
        }).build());
        SubCategoryBuilder markerOffset = entryBuilder.startSubCategory((Component)Component.literal((String)"Marker Offset")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        markerOffset.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.marker_offset"), Config.MARKER_OFFSET, 0, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MARKER_OFFSET))).setSaveConsumer(newValue -> {
            Config.MARKER_OFFSET = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.marker_offset")}).build());
        SubCategoryBuilder attackParticle = entryBuilder.startSubCategory((Component)Component.literal((String)"Falling Attack Particles")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        attackParticle.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.on_weapon"), Config.WeaponParticle.class, Config.MACE_PARTICLE).setDefaultValue(Config.WeaponParticle.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MACE_PARTICLE))).setSaveConsumer(newValue -> {
            Config.MACE_PARTICLE = newValue;
        }).build());
        attackParticle.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.transition_threshold"), Config.PARTICLE_TRANSITION_THRESHOLD, 10, 20).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PARTICLE_TRANSITION_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.PARTICLE_TRANSITION_THRESHOLD = newValue;
        }).build());
        attackParticle.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.particle_order"), Config.TransitionOrder.class, Config.PARTICLE_ORDER).setDefaultValue(Config.TransitionOrder.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PARTICLE_ORDER))).setSaveConsumer(newValue -> {
            Config.PARTICLE_ORDER = newValue;
        }).build());
        SubCategoryBuilder reconOption = entryBuilder.startSubCategory((Component)Component.literal((String)"Recon Option")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        reconOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Quick Zoom"), Config.RECON_QUICK_ZOOM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RECON_QUICK_ZOOM))).setSaveConsumer(newValue -> {
            Config.RECON_QUICK_ZOOM = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.quick_zoom")}).build());
        reconOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Quick Attack"), Config.RECON_QUICK_ATTACK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RECON_QUICK_ATTACK))).setSaveConsumer(newValue -> {
            Config.RECON_QUICK_ATTACK = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.quick_attack")}).build());
        reconOption.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.literal((String)"Recon View Perspective"), Config.ReconView.class, Config.ZOOM_VIEW).setDefaultValue(Config.ReconView.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ZOOM_VIEW))).setSaveConsumer(newValue -> {
            Config.ZOOM_VIEW = newValue;
        }).build());
        reconOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Zoom In/Out Step"), Config.ZOOM_STEP, 3, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ZOOM_STEP))).setSaveConsumer(newValue -> {
            Config.ZOOM_STEP = newValue;
        }).build());
        reconOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Display Crosshairs When [THIRD_PERSON_BACK]"), Config.PERSPECTIVE_BACK_CROSSHAIR).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PERSPECTIVE_BACK_CROSSHAIR))).setSaveConsumer(newValue -> {
            Config.PERSPECTIVE_BACK_CROSSHAIR = newValue;
        }).build());
        reconOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Set the current viewing direction when returning to normal state"), Config.CAMERA_RETURN_BEHAVIOR).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_CAMERA_RETURN_BEHAVIOR))).setSaveConsumer(newValue -> {
            Config.CAMERA_RETURN_BEHAVIOR = newValue;
        }).build());
        SubCategoryBuilder friendProtection = entryBuilder.startSubCategory((Component)Component.literal((String)"Friend Protection")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        friendProtection.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.friend_protection"), Config.FRIEND_PROTECTION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FRIEND_PROTECTION))).setSaveConsumer(newValue -> {
            Config.FRIEND_PROTECTION = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.friend_protection")}).build());
        friendProtection.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Displays a mark in the Player List"), Config.FRIEND_MARK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FRIEND_MARK))).setSaveConsumer(newValue -> {
            Config.FRIEND_MARK = newValue;
        }).build());
        friendProtection.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Make a list that includes all your friends"), Config.FRIEND_NOT_FOUND).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FRIEND_NOT_FOUND))).setSaveConsumer(newValue -> {
            Config.FRIEND_NOT_FOUND = newValue;
        }).build());
        SubCategoryBuilder customCrosshair = entryBuilder.startSubCategory((Component)Component.literal((String)"Custom Crosshair")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        customCrosshair.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.literal((String)"Use Custom Crosshairs"), Config.CrosshairMode.class, Config.CROSSHAIR_MODE).setDefaultValue(Config.CrosshairMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_CROSSHAIR_MODE))).setSaveConsumer(newValue -> {
            Config.CROSSHAIR_MODE = newValue;
        }).build());
        SubCategoryBuilder attackOption = entryBuilder.startSubCategory((Component)Component.literal((String)"For Development - Attack")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        attackOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Forced Disable"), Config.FORCED_DISABLE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FORCED_DISABLE))).setSaveConsumer(newValue -> {
            Config.FORCED_DISABLE = newValue;
        }).build());
        attackOption.add((AbstractConfigListEntry)((IntFieldBuilder)entryBuilder.startIntField((Component)Component.literal((String)"Attack Range"), Config.ATTACK_RANGE).setMax(38).setMin(30).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ATTACK_RANGE))).setSaveConsumer(newValue -> {
            Config.ATTACK_RANGE = newValue;
        }).build());
        attackOption.add((AbstractConfigListEntry)((IntFieldBuilder)entryBuilder.startIntField((Component)Component.literal((String)"Attack Range - Stun Slam"), Config.ATTACK_RANGE_STUN).setMax(38).setMin(30).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ATTACK_RANGE_STUN))).setSaveConsumer(newValue -> {
            Config.ATTACK_RANGE_STUN = newValue;
        }).build());
        attackOption.add((AbstractConfigListEntry)((IntFieldBuilder)entryBuilder.startIntField((Component)Component.literal((String)"Attack Range - Spear"), Config.ATTACK_RANGE_SPEAR).setMax(80).setMin(45).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ATTACK_RANGE_SPEAR))).setSaveConsumer(newValue -> {
            Config.ATTACK_RANGE_SPEAR = newValue;
        }).build());
        SubCategoryBuilder autoRewearDelay = entryBuilder.startSubCategory((Component)Component.literal((String)"Auto Rewear Option")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        autoRewearDelay.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.double_tap_rewear_delay"), Config.DOUBLE_TAP_REWEAR_DELAY, 0, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DOUBLE_TAP_REWEAR_DELAY))).setSaveConsumer(newValue -> {
            Config.DOUBLE_TAP_REWEAR_DELAY = newValue;
        }).build());
        SubCategoryBuilder suppression = entryBuilder.startSubCategory((Component)Component.literal((String)"For Development - Suppression")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        suppression.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.fov_suppression"), Config.FOV_SUPPRESSION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_SUPPRESSION))).setSaveConsumer(newValue -> {
            Config.FOV_SUPPRESSION = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_suppression")}).build());
        suppression.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.flap_suppression"), Config.FLAP_SUPPRESSION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FLAP_SUPPRESSION))).setSaveConsumer(newValue -> {
            Config.FLAP_SUPPRESSION = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.flap_suppression")}).build());
        suppression.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.flap_suppression_threshold"), Config.FLAP_SUPPRESSION_THRESHOLD, 5, 20).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FLAP_SUPPRESSION_THRESHOLD))).setSaveConsumer(newValue -> {
            Config.FLAP_SUPPRESSION_THRESHOLD = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.flap_suppression")}).build());
        suppression.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.translatable((String)"config.mace_attack_assistance.option.flap_suppression_tick"), Config.FLAP_SUPPRESSION_TICK, 5, 20).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FLAP_SUPPRESSION_TICK))).setSaveConsumer(newValue -> {
            Config.FLAP_SUPPRESSION_TICK = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.flap_suppression")}).build());
        SubCategoryBuilder displayActionBar = entryBuilder.startSubCategory((Component)Component.literal((String)"Notification")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        displayActionBar.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.display_action_bar"), Config.DisplayAt.class, Config.DISPLAY_ACTION_BAR).setDefaultValue(Config.DisplayAt.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DISPLAY_ACTION_BAR))).setSaveConsumer(newValue -> {
            Config.DISPLAY_ACTION_BAR = newValue;
        }).build());
        SubCategoryBuilder debugScreen = entryBuilder.startSubCategory((Component)Component.literal((String)"Debug Screen")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        debugScreen.add((AbstractConfigListEntry)entryBuilder.startBooleanToggle((Component)Component.translatable((String)"config.mace_attack_assistance.option.debug_screen"), Config.DEBUG_SCREEN).setDefaultValue(false).setSaveConsumer(newValue -> {
            Config.DEBUG_SCREEN = newValue;
        }).build());
        debugScreen.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Debug : Predicted Landing Position"), Config.DEBUG_LANDING_POS).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DEBUG_LANDING_POS))).setSaveConsumer(newValue -> {
            Config.DEBUG_LANDING_POS = newValue;
        }).build());
        SubCategoryBuilder autoElytra = entryBuilder.startSubCategory((Component)Component.literal((String)" (Experimental) Elytra - Auto Swap")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        autoElytra.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Elytra - Auto Swap"), Config.AUTO_ELYTRA).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_ELYTRA))).setSaveConsumer(newValue -> {
            Config.AUTO_ELYTRA = newValue;
        }).build());
        autoElytra.add((AbstractConfigListEntry)((IntFieldBuilder)entryBuilder.startIntField((Component)Component.literal((String)"Distance/Speed Ratio : Stun Slam (10 - 100) * 0.1 "), Config.AUTO_ELYTRA_TICK_AHEAD).setMax(100).setMin(10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD))).setSaveConsumer(newValue -> {
            Config.AUTO_ELYTRA_TICK_AHEAD = newValue;
        }).build());
        autoElytra.add((AbstractConfigListEntry)((IntFieldBuilder)entryBuilder.startIntField((Component)Component.literal((String)"Distance/Speed Ratio : Hot Swap (10 - 100) * 0.1"), Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL))).setMax(100).setMin(10).setSaveConsumer(newValue -> {
            Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL = newValue;
        }).build());
        SubCategoryBuilder spiralSetting = entryBuilder.startSubCategory((Component)Component.literal((String)"Spiral")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Spiral Count"), Config.SP_SPIRAL_COUNT, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_COUNT))).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_COUNT = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Spiral Length"), Config.SP_SPIRAL_LENGTH, 1, 40).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_LENGTH))).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_LENGTH = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Spiral Alpha"), Config.SP_SPIRAL_ALPHA, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_ALPHA))).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_ALPHA = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Spiral Gamma"), Config.SP_SPIRAL_GAMMA, 10, 50).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_GAMMA))).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_GAMMA = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Coils"), Config.SP_COILS, 1, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COILS))).setSaveConsumer(newValue -> {
            Config.SP_COILS = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Height"), Config.SP_HEIGHT, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HEIGHT))).setSaveConsumer(newValue -> {
            Config.SP_HEIGHT = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Size"), Config.SP_SIZE, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SIZE))).setSaveConsumer(newValue -> {
            Config.SP_SIZE = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Speed"), Config.SP_SPEED, 1, 50).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEED))).setSaveConsumer(newValue -> {
            Config.SP_SPEED = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Base Radius"), Config.SP_BASE_RADIUS, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BASE_RADIUS))).setSaveConsumer(newValue -> {
            Config.SP_BASE_RADIUS = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Wave Speed"), Config.SP_WAVE_SPEED, 1, 80).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WAVE_SPEED))).setSaveConsumer(newValue -> {
            Config.SP_WAVE_SPEED = newValue;
        }).build());
        spiralSetting.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Wave Amplitude"), Config.SP_WAVE_AMPLITUDE, 1, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WAVE_AMPLITUDE))).setSaveConsumer(newValue -> {
            Config.SP_WAVE_AMPLITUDE = newValue;
        }).build());
        SubCategoryBuilder markerColor = entryBuilder.startSubCategory((Component)Component.literal((String)"Marker Color")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_villager"), ColorData.Color.class, Config.COLOR_VILLAGER).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_VILLAGER))).setSaveConsumer(newValue -> {
            Config.COLOR_VILLAGER = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_hostile"), ColorData.Color.class, Config.COLOR_HOSTILE).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_HOSTILE))).setSaveConsumer(newValue -> {
            Config.COLOR_HOSTILE = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_warden"), ColorData.Color.class, Config.COLOR_WARDEN).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_WARDEN))).setSaveConsumer(newValue -> {
            Config.COLOR_WARDEN = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_iron_golem"), ColorData.Color.class, Config.COLOR_IRON_GOLEM).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_IRON_GOLEM))).setSaveConsumer(newValue -> {
            Config.COLOR_IRON_GOLEM = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_passive"), ColorData.Color.class, Config.COLOR_PASSIVE).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_PASSIVE))).setSaveConsumer(newValue -> {
            Config.COLOR_PASSIVE = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_ambient"), ColorData.Color.class, Config.COLOR_AMBIENT).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_AMBIENT))).setSaveConsumer(newValue -> {
            Config.COLOR_AMBIENT = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startEnumSelector((Component)Component.translatable((String)"config.mace_attack_assistance.option.allowed_player"), ColorData.Color.class, Config.COLOR_PLAYER).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_PLAYER))).setSaveConsumer(newValue -> {
            Config.COLOR_PLAYER = newValue;
        }).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.dark_blue).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.dark_green).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.dark_aqua).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.dark_red).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.dark_purple).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.gold).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.gray).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.dark_gray).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.blue).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.green).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.aqua).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.red).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.light_purple).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.yellow).build());
        markerColor.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)ColorData.white).build());
        SubCategoryBuilder radarBoxFov = entryBuilder.startSubCategory((Component)Component.literal((String)"Radar")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        radarBoxFov.add((AbstractConfigListEntry)entryBuilder.startTextDescription((Component)Component.translatable((String)"config.mace_attack_assistance.option.desc_fov").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GREEN)).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"FOV X (Horizontal) * 10 degree [Gliding , Target Search: On]"), Config.FOV_HORIZONTAL, 1, 36).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_HORIZONTAL))).setSaveConsumer(newValue -> {
            Config.FOV_HORIZONTAL = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_horizontal")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"FOV Y (Vertical) * 10 degree [Gliding , Target Search: On]"), Config.FOV_VERTICAL, 1, 18).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_VERTICAL))).setSaveConsumer(newValue -> {
            Config.FOV_VERTICAL = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_vertical")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"FOV X (Horizontal) * 10 degree [Recon Camera Mode]"), Config.FOV_HORIZONTAL_ON_ZOOM, 1, 36).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_HORIZONTAL_ON_ZOOM))).setSaveConsumer(newValue -> {
            Config.FOV_HORIZONTAL_ON_ZOOM = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_horizontal")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"FOV Y (Vertical) * 10 degree [Recon Camera Mode]"), Config.FOV_VERTICAL_ON_ZOOM, 1, 18).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_VERTICAL_ON_ZOOM))).setSaveConsumer(newValue -> {
            Config.FOV_VERTICAL_ON_ZOOM = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_vertical")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Rectangle Horizontal * 10 blocks"), Config.RADAR_HORIZONTAL, 1, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_HORIZONTAL))).setSaveConsumer(newValue -> {
            Config.RADAR_HORIZONTAL = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_horizontal")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Rectangle Upward * 10 blocks"), Config.RADAR_UPWARD, 1, 3).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_UPWARD))).setSaveConsumer(newValue -> {
            Config.RADAR_UPWARD = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_upward")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Rectangle Downward * 10 blocks"), Config.RADAR_DOWNWARD, 1, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_DOWNWARD))).setSaveConsumer(newValue -> {
            Config.RADAR_DOWNWARD = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_downward")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Rectangle Update Interval"), Config.RADAR_UPDATE_INTERVAL, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_UPDATE_INTERVAL))).setSaveConsumer(newValue -> {
            Config.RADAR_UPDATE_INTERVAL = newValue;
        }).setTooltip(new Component[]{Component.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_update_interval")}).build());
        radarBoxFov.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Extraction Processing Thread"), Config.PARALLEL_SEARCH).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PARALLEL_SEARCH))).setSaveConsumer(newValue -> {
            Config.PARALLEL_SEARCH = newValue;
        }).setTooltip(new Component[]{Component.literal((String)"(Parallel:Experimental)")}).setYesNoTextSupplier(value -> value != false ? Component.literal((String)"Parallel") : Component.literal((String)"Single")).build());
        SubCategoryBuilder kbdOption = entryBuilder.startSubCategory((Component)Component.literal((String)"Knockback Displacement")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        kbdOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Range"), Config.KBD_RANGE, 3, 15).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_KBD_RANGE))).setSaveConsumer(newValue -> {
            Config.KBD_RANGE = newValue;
        }).setTooltip(new Component[]{Component.literal((String)"Range for detecting the target.")}).build());
        kbdOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Auto Flick"), Config.KBD_AUTO_FLICK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_KBD_AUTO_FLICK))).setSaveConsumer(newValue -> {
            Config.KBD_AUTO_FLICK = newValue;
        }).setTooltip(new Component[]{Component.literal((String)"If the angle is smaller than the Flick Angle, the gaze will move to the set angle.")}).build());
        kbdOption.add((AbstractConfigListEntry)((IntSliderBuilder)entryBuilder.startIntSlider((Component)Component.literal((String)"Flick Angle"), Config.KBD_FLICK_ANGLE, 10, 120).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_KBD_FLICK_ANGLE))).setSaveConsumer(newValue -> {
            Config.KBD_FLICK_ANGLE = newValue;
        }).build());
        kbdOption.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Instant Aim"), Config.KBD_INSTANT_AIM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_KBD_INSTANT_AIM))).setSaveConsumer(newValue -> {
            Config.KBD_INSTANT_AIM = newValue;
        }).setTooltip(new Component[]{Component.literal((String)"If the target is outside the attack range, only aiming will be performed.")}).build());
        SubCategoryBuilder defaultExpanded = entryBuilder.startSubCategory((Component)Component.literal((String)"Expand Categories")).setExpanded(Config.DEFAULT_EXPANDED_SUB);
        defaultExpanded.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Global"), Config.DEFAULT_EXPANDED_GLOBAL).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DEFAULT_EXPANDED_GLOBAL))).setSaveConsumer(newValue -> {
            Config.DEFAULT_EXPANDED_GLOBAL = newValue;
        }).build());
        defaultExpanded.add((AbstractConfigListEntry)((BooleanToggleBuilder)entryBuilder.startBooleanToggle((Component)Component.literal((String)"Sub"), Config.DEFAULT_EXPANDED_SUB).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DEFAULT_EXPANDED_SUB))).setSaveConsumer(newValue -> {
            Config.DEFAULT_EXPANDED_SUB = newValue;
        }).build());
        attack.addEntry((AbstractConfigListEntry)attackAssist.build());
        attack.addEntry((AbstractConfigListEntry)hotSwap.build());
        attack.addEntry((AbstractConfigListEntry)breachSwap.build());
        attack.addEntry((AbstractConfigListEntry)stunSlam.build());
        attack.addEntry((AbstractConfigListEntry)doubleTap.build());
        attack.addEntry((AbstractConfigListEntry)snapback.build());
        attack.addEntry((AbstractConfigListEntry)shieldDraining.build());
        attack2.addEntry((AbstractConfigListEntry)kbdOption.build());
        aim.addEntry((AbstractConfigListEntry)aimAssist.build());
        aim.addEntry((AbstractConfigListEntry)aimTarget.build());
        aim.addEntry((AbstractConfigListEntry)aimOption.build());
        jump.addEntry((AbstractConfigListEntry)jumpAssist.build());
        jump.addEntry((AbstractConfigListEntry)wallClimbing.build());
        jump.addEntry((AbstractConfigListEntry)jumpSpam.build());
        jump.addEntry((AbstractConfigListEntry)jumpMode.build());
        elytra.addEntry((AbstractConfigListEntry)toggleElytraOption.build());
        elytra.addEntry((AbstractConfigListEntry)elytraBoost.build());
        elytra.addEntry((AbstractConfigListEntry)customCrosshair.build());
        item.addEntry((AbstractConfigListEntry)autoRefill.build());
        item.addEntry((AbstractConfigListEntry)autoWindChargeSelector.build());
        item.addEntry((AbstractConfigListEntry)pearlCatch.build());
        item.addEntry((AbstractConfigListEntry)rocketBlitz.build());
        item.addEntry((AbstractConfigListEntry)prioritizeUse.build());
        slot.addEntry((AbstractConfigListEntry)weaponSlotSetting.build());
        slot.addEntry((AbstractConfigListEntry)returnToPrevSlot.build());
        recon.addEntry((AbstractConfigListEntry)reconOption.build());
        friend.addEntry((AbstractConfigListEntry)friendProtection.build());
        effects.addEntry((AbstractConfigListEntry)markerType.build());
        effects.addEntry((AbstractConfigListEntry)targetSearchMode.build());
        effects.addEntry((AbstractConfigListEntry)displayMarkerOnTarget.build());
        effects.addEntry((AbstractConfigListEntry)hideMarkerWhenAimOff.build());
        effects.addEntry((AbstractConfigListEntry)markerOffset.build());
        effects.addEntry((AbstractConfigListEntry)attackParticle.build());
        spiral.addEntry((AbstractConfigListEntry)spiralSetting.build());
        color.addEntry((AbstractConfigListEntry)markerColor.build());
        fov.addEntry((AbstractConfigListEntry)radarBoxFov.build());
        others.addEntry((AbstractConfigListEntry)defaultExpanded.build());
        others.addEntry((AbstractConfigListEntry)autoRewearDelay.build());
        others.addEntry((AbstractConfigListEntry)displayActionBar.build());
        others.addEntry((AbstractConfigListEntry)attackOption.build());
        others.addEntry((AbstractConfigListEntry)suppression.build());
        others.addEntry((AbstractConfigListEntry)debugScreen.build());
        others.addEntry((AbstractConfigListEntry)autoElytra.build());
        builder.setSavingRunnable(() -> {
            if (Config.FORCED_DISABLE) {
                MAAState.antiCheat = true;
            } else if (MAAState.allowedLevels > 0) {
                MAAState.antiCheat = false;
            }
            ConfigOperation.saveFile();
        });
        return builder.build();
    }

    public static Object getDefaultValue(ConfigOperation.ConfigData configData) {
        switch (configData.type()) {
            case I: {
                return Integer.parseInt(configData.defaultValue());
            }
            case B: {
                return Boolean.parseBoolean(configData.defaultValue());
            }
        }
        return configData.defaultValue();
    }

    /**
     * Placeholder screen shown when ClothConfig2 is not available at runtime.
     */
    private static class PlaceholderScreen extends Screen {
        private final Screen parent;

        protected PlaceholderScreen(Screen parent) {
            super(Component.literal("Mace Attack Assistance - Config"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            this.addRenderableWidget(
                net.minecraft.client.gui.components.Button.builder(
                    Component.literal("Done"),
                    button -> this.minecraft.setScreenAndShow(this.parent)
                ).pos(this.width / 2 - 100, this.height / 6 + 168).size(200, 20).build()
            );
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
            guiGraphics.centeredText(this.font, Component.literal(
                "ClothConfig2 is not available at runtime."), this.width / 2, 40, 0xFF5555);
            guiGraphics.centeredText(this.font, Component.literal(
                "Config cannot be edited without ClothConfig2."), this.width / 2, 55, 0xAAAAAA);
        }

        @Override
        public void onClose() {
            this.minecraft.setScreenAndShow(this.parent);
        }
    }
}
