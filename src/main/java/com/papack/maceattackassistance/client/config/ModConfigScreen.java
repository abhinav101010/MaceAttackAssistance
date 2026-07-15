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
 *  net.minecraft.Formatting
 *  net.minecraft.Text
 *  net.minecraft.Screen
 */
package com.papack.maceattackassistance.client.config;

import com.papack.maceattackassistance.client.ColorData;
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
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;

@Environment(value=EnvType.CLIENT)
public class ModConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle((Text)Text.translatable((String)"config.mace_attack_assistance.title"));
        builder.setGlobalized(Config.DEFAULT_EXPANDED_GLOBAL);
        builder.setGlobalizedExpanded(true);
        ConfigCategory attack = builder.getOrCreateCategory((Text)Text.literal((String)"Attack").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory aim = builder.getOrCreateCategory((Text)Text.literal((String)"Aim").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory jump = builder.getOrCreateCategory((Text)Text.literal((String)"Jump").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory elytra = builder.getOrCreateCategory((Text)Text.literal((String)"Elytra").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory item = builder.getOrCreateCategory((Text)Text.literal((String)"Item").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory slot = builder.getOrCreateCategory((Text)Text.literal((String)"Slot").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory recon = builder.getOrCreateCategory((Text)Text.literal((String)"Recon").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory friend = builder.getOrCreateCategory((Text)Text.literal((String)"Friend").styled(s -> s.withColor(Formatting.GREEN)));
        ConfigCategory effects = builder.getOrCreateCategory((Text)Text.literal((String)"Effects,Marker").styled(s -> s.withColor(Formatting.AQUA)));
        ConfigCategory spiral = builder.getOrCreateCategory((Text)Text.literal((String)"SpiralMaker").styled(s -> s.withColor(Formatting.AQUA)));
        ConfigCategory color = builder.getOrCreateCategory((Text)Text.literal((String)"Spiral,Beam Color").styled(s -> s.withColor(Formatting.AQUA)));
        ConfigCategory fov = builder.getOrCreateCategory((Text)Text.literal((String)"Search Range,Angle").styled(s -> s.withColor(Formatting.AQUA)));
        ConfigCategory others = builder.getOrCreateCategory((Text)Text.literal((String)"Others").styled(s -> s.withColor(Formatting.GRAY)));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        SubCategoryBuilder attackAssist = entryBuilder.startSubCategory((Text)Text.literal((String)"Attack Assist")).setExpanded(Config.DEFAULT_EXPANDED);
        attackAssist.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.attack_assistance"), Config.ATTACK_ASSISTANCE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ATTACK_ASSISTANCE)).setSaveConsumer(newValue -> {
            Config.ATTACK_ASSISTANCE = newValue;
        }).build());
        attackAssist.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.height_threshold"), Config.HEIGHT_THRESHOLD, 2, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HEIGHT_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.HEIGHT_THRESHOLD = newValue;
        }).build());
        attackAssist.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.weapon_swing"), Config.WEAPON_SWING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WEAPON_SWING)).setSaveConsumer(newValue -> {
            Config.WEAPON_SWING = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.weapon_swing")}).build());
        attackAssist.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.swing_toggle"), Config.SwingToggle.class, Config.SWING_TOGGLE).setDefaultValue(Config.SwingToggle.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SWING_TOGGLE))).setSaveConsumer(newValue -> {
            Config.SWING_TOGGLE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.swing_toggle")}).build());
        SubCategoryBuilder doubleTap = entryBuilder.startSubCategory((Text)Text.literal((String)"Double Tap")).setExpanded(Config.DEFAULT_EXPANDED);
        doubleTap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.double_tap"), Config.DOUBLE_TAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DOUBLE_TAP)).setSaveConsumer(newValue -> {
            Config.DOUBLE_TAP = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.double_tap")}).build());
        doubleTap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.double_tap_rewear"), Config.DOUBLE_TAP_REWEAR).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DOUBLE_TAP_REWEAR)).setSaveConsumer(newValue -> {
            Config.DOUBLE_TAP_REWEAR = newValue;
        }).build());
        SubCategoryBuilder hotSwap = entryBuilder.startSubCategory((Text)Text.literal((String)"Hot Swap")).setExpanded(Config.DEFAULT_EXPANDED);
        hotSwap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.hot_swap"), Config.HOT_SWAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HOT_SWAP)).setSaveConsumer(newValue -> {
            Config.HOT_SWAP = newValue;
        }).build());
        hotSwap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_assist"), Config.SPEAR_ASSIST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_ASSIST)).setSaveConsumer(newValue -> {
            Config.SPEAR_ASSIST = newValue;
        }).build());
        hotSwap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_swap"), Config.SPEAR_SWAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SWAP)).setSaveConsumer(newValue -> {
            Config.SPEAR_SWAP = newValue;
        }).build());
        SubCategoryBuilder spearSlam = entryBuilder.startSubCategory((Text)Text.literal((String)"Spear Slam")).setExpanded(Config.DEFAULT_EXPANDED);
        spearSlam.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_slam"), Config.SPEAR_SLAM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SLAM)).setSaveConsumer(newValue -> {
            Config.SPEAR_SLAM = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"After landing a spear hit while falling,\nautomatically swap to mace for a smash attack.\nApplies spear + mace damage sequentially.")}).build());
        spearSlam.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_slam_min_fall"), Config.SPEAR_SLAM_MIN_FALL, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SLAM_MIN_FALL)).setSaveConsumer(newValue -> {
            Config.SPEAR_SLAM_MIN_FALL = newValue;
        }).build());
        spearSlam.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_slam_cooldown"), Config.SPEAR_SLAM_COOLDOWN, 0, 40).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SLAM_COOLDOWN)).setSaveConsumer(newValue -> {
            Config.SPEAR_SLAM_COOLDOWN = newValue;
        }).build());
        spearSlam.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_slam_damage_mult"), Config.SPEAR_SLAM_DAMAGE_MULT_INT, 50, 200).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SLAM_DAMAGE_MULT)).setSaveConsumer(newValue -> {
            Config.SPEAR_SLAM_DAMAGE_MULT_INT = newValue;
        }).build());
        spearSlam.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_slam_particles"), Config.SPEAR_SLAM_PARTICLES).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SLAM_PARTICLES)).setSaveConsumer(newValue -> {
            Config.SPEAR_SLAM_PARTICLES = newValue;
        }).build());
        spearSlam.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.spear_slam_sound"), Config.SPEAR_SLAM_SOUND).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEAR_SLAM_SOUND)).setSaveConsumer(newValue -> {
            Config.SPEAR_SLAM_SOUND = newValue;
        }).build());
        SubCategoryBuilder stunSlam = entryBuilder.startSubCategory((Text)Text.literal((String)"Stun Slam")).setExpanded(Config.DEFAULT_EXPANDED);
        stunSlam.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.stun_slamming"), Config.STUN_SLAMMING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_STUN_SLAMMING)).setSaveConsumer(newValue -> {
            Config.STUN_SLAMMING = newValue;
        }).build());
        SubCategoryBuilder breachSwap = entryBuilder.startSubCategory((Text)Text.literal((String)"Breach Swap, Sword Swap")).setExpanded(Config.DEFAULT_EXPANDED);
        breachSwap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.breach_swap"), Config.BREACH_SWAP).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BREACH_SWAP)).setSaveConsumer(newValue -> {
            Config.BREACH_SWAP = newValue;
        }).build());
        breachSwap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.breach_limited"), Config.BREACH_LIMITED).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BREACH_LIMITED)).setSaveConsumer(newValue -> {
            Config.BREACH_LIMITED = newValue;
        }).build());
        breachSwap.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.breach_on_ground"), Config.BREACH_ON_GROUND).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BREACH_ON_GROUND)).setSaveConsumer(newValue -> {
            Config.BREACH_ON_GROUND = newValue;
        }).build());
        breachSwap.add(entryBuilder.startEnumSelector((Text)Text.literal((String)"Armored Target Behavior"), Config.Behavior.class, Config.SWORD_SWAP_OR_BREACH_SWAP).setDefaultValue(Config.Behavior.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SWORD_SWAP_OR_BREACH_SWAP))).setSaveConsumer(newValue -> {
            Config.SWORD_SWAP_OR_BREACH_SWAP = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"It does not affect snapback.\nBreach Swap: any to Breach Mace\nSword Swap: any to Sword(or Axe)")}).build());
        breachSwap.add(entryBuilder.startEnumSelector((Text)Text.literal((String)"UnArmored Target Behavior"), Config.Behavior.class, Config.BEHAVIOR_NOT_WEARING_ARMOR).setDefaultValue(Config.Behavior.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BEHAVIOR_NOT_WEARING_ARMOR))).setSaveConsumer(newValue -> {
            Config.BEHAVIOR_NOT_WEARING_ARMOR = newValue;
        }).build());
        breachSwap.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Sword Swap Mode Option"), Config.SWORD_OR_AXE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SWORD_OR_AXE)).setSaveConsumer(newValue -> {
            Config.SWORD_OR_AXE = newValue;
        }).setYesNoTextSupplier(value -> value != false ? Text.literal((String)"Sword") : Text.literal((String)"Axe")).build());
        SubCategoryBuilder snapback = entryBuilder.startSubCategory((Text)Text.literal((String)"Snapback")).setExpanded(Config.DEFAULT_EXPANDED);
        snapback.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.snapback"), Config.SNAPBACK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SNAPBACK)).setSaveConsumer(newValue -> {
            Config.SNAPBACK = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.snapback")}).build());
        snapback.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.snapback_threshold"), Config.SNAPBACK_THRESHOLD, 4, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SNAPBACK_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.SNAPBACK_THRESHOLD = newValue;
        }).build());
        snapback.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Snapback - Tolerance"), Config.SNAPBACK_TOLERANCE, 0, 500).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SNAPBACK_TOLERANCE)).setSaveConsumer(newValue -> {
            Config.SNAPBACK_TOLERANCE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.snapback_tolerance")}).build());
        SubCategoryBuilder shieldDraining = entryBuilder.startSubCategory((Text)Text.literal((String)"Shield Draining")).setExpanded(Config.DEFAULT_EXPANDED);
        shieldDraining.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Shield Draining"), Config.SHIELD_DRAINING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SHIELD_DRAINING)).setSaveConsumer(newValue -> {
            Config.SHIELD_DRAINING = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.shield_draining")}).build());
        SubCategoryBuilder weaponSlotSetting = entryBuilder.startSubCategory((Text)Text.literal((String)"Weapon Slot Setting")).setExpanded(Config.DEFAULT_EXPANDED);
        weaponSlotSetting.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.axe_slot"), Config.AXE_SLOT + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AXE_SLOT) + 1).setSaveConsumer(newValue -> {
            Config.AXE_SLOT = newValue - 1;
        }).build());
        weaponSlotSetting.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.mace_slot_primary"), Config.MACE_PRIMARY + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MACE_PRIMARY) + 1).setSaveConsumer(newValue -> {
            Config.MACE_PRIMARY = newValue - 1;
        }).build());
        weaponSlotSetting.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.mace_slot_breach"), Config.MACE_BREACH + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MACE_BREACH) + 1).setSaveConsumer(newValue -> {
            Config.MACE_BREACH = newValue - 1;
        }).build());
        SubCategoryBuilder returnToPrevSlot = entryBuilder.startSubCategory((Text)Text.literal((String)"Return To PrevSlot")).setExpanded(Config.DEFAULT_EXPANDED);
        returnToPrevSlot.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Return Mode [0:Auto], [1-9:Specify]"), Config.RETURN_TO_PREV_SLOT_MODE + 1, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_MODE) + 1).setSaveConsumer(newValue -> {
            Config.RETURN_TO_PREV_SLOT_MODE = newValue - 1;
        }).build());
        returnToPrevSlot.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.return_to_prev_slot"), Config.RETURN_TO_PREV_SLOT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RETURN_TO_PREV_SLOT)).setSaveConsumer(newValue -> {
            Config.RETURN_TO_PREV_SLOT = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.return_to_prev_slot")}).build());
        returnToPrevSlot.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.return_to_prev_slot_breach"), Config.RETURN_TO_PREV_SLOT_BREACH).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_BREACH)).setSaveConsumer(newValue -> {
            Config.RETURN_TO_PREV_SLOT_BREACH = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.return_to_prev_slot")}).build());
        SubCategoryBuilder aimAssist = entryBuilder.startSubCategory((Text)Text.literal((String)"Aim Assist")).setExpanded(Config.DEFAULT_EXPANDED);
        aimAssist.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.aim_assist"), Config.AIM_ASSIST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_ASSIST)).setSaveConsumer(newValue -> {
            Config.AIM_ASSIST = newValue;
        }).build());
        aimAssist.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.legacy_aim_mode"), Config.LEGACY_AIM_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_LEGACY_AIM_MODE)).setSaveConsumer(newValue -> {
            Config.LEGACY_AIM_MODE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.legacy_aim_mode")}).build());
        SubCategoryBuilder aimTarget = entryBuilder.startSubCategory((Text)Text.literal((String)"Target")).setExpanded(Config.DEFAULT_EXPANDED);
        aimTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_villager"), Config.ALLOWED_VILLAGER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_VILLAGER)).setSaveConsumer(newValue -> {
            Config.ALLOWED_VILLAGER = newValue;
        }).build());
        aimTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_iron_golem"), Config.ALLOWED_IRON_GOLEM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_IRON_GOLEM)).setSaveConsumer(newValue -> {
            Config.ALLOWED_IRON_GOLEM = newValue;
        }).build());
        aimTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_hostile"), Config.ALLOWED_HOSTILE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_HOSTILE)).setSaveConsumer(newValue -> {
            Config.ALLOWED_HOSTILE = newValue;
        }).build());
        aimTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_passive"), Config.ALLOWED_PASSIVE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_PASSIVE)).setSaveConsumer(newValue -> {
            Config.ALLOWED_PASSIVE = newValue;
        }).build());
        aimTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_ambient"), Config.ALLOWED_AMBIENT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_AMBIENT)).setSaveConsumer(newValue -> {
            Config.ALLOWED_AMBIENT = newValue;
        }).build());
        aimTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_player"), Config.ALLOWED_PLAYER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALLOWED_PLAYER)).setSaveConsumer(newValue -> {
            Config.ALLOWED_PLAYER = newValue;
        }).build());
        SubCategoryBuilder aimOption = entryBuilder.startSubCategory((Text)Text.literal((String)"Aim Option")).setExpanded(Config.DEFAULT_EXPANDED);
        aimOption.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.forced_adjustment"), Config.AIM_FORCED_ADJUSTMENT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_FORCED_ADJUSTMENT)).setSaveConsumer(newValue -> {
            Config.AIM_FORCED_ADJUSTMENT = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.forced_adjustment")}).build());
        aimOption.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.max_speed_yaw"), Config.MAX_SPEED_YAW, 0, 15).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MAX_SPEED_YAW)).setSaveConsumer(newValue -> {
            Config.MAX_SPEED_YAW = newValue;
        }).build());
        aimOption.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.aim_mode"), Config.AimMode.class, Config.AIM_MODE).setDefaultValue(Config.AimMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_MODE))).setSaveConsumer(newValue -> {
            Config.AIM_MODE = newValue;
        }).build());
        aimOption.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.auto_mode_threshold"), Config.AUTO_MODE_THRESHOLD, 10, 30).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_MODE_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.AUTO_MODE_THRESHOLD = newValue;
        }).build());
        aimOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Starting Threshold"), Config.AIM_FALL_THRESHOLD, 2, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_FALL_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.AIM_FALL_THRESHOLD = newValue;
        }).build());
        aimOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"(Experimental) Raycast Search Mode"), Config.AIM_RAYCAST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_RAYCAST)).setSaveConsumer(newValue -> {
            Config.AIM_RAYCAST = newValue;
        }).build());
        aimOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"(Experimental) Raycast : Range"), Config.AIM_RAYCAST_RANGE, 5, 30).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_RAYCAST_RANGE)).setSaveConsumer(newValue -> {
            Config.AIM_RAYCAST_RANGE = newValue;
        }).build());
        aimOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"(Experimental) Raycast : Radius"), Config.AIM_RAYCAST_RADIUS, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_RAYCAST_RADIUS)).setSaveConsumer(newValue -> {
            Config.AIM_RAYCAST_RADIUS = newValue;
        }).build());
        SubCategoryBuilder jumpAssist = entryBuilder.startSubCategory((Text)Text.literal((String)"Jump Assist")).setExpanded(Config.DEFAULT_EXPANDED);
        jumpAssist.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.jump_assist"), Config.JUMP_ASSIST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_ASSIST)).setSaveConsumer(newValue -> {
            Config.JUMP_ASSIST = newValue;
        }).build());
        SubCategoryBuilder wallClimbing = entryBuilder.startSubCategory((Text)Text.literal((String)"wallClimbing")).setExpanded(Config.DEFAULT_EXPANDED);
        wallClimbing.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.wall_climbing"), Config.WALL_CLIMBING).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WALL_CLIMBING)).setSaveConsumer(newValue -> {
            Config.WALL_CLIMBING = newValue;
        }).build());
        SubCategoryBuilder autoWindChargeSelector = entryBuilder.startSubCategory((Text)Text.literal((String)"Auto WindCharge Selector")).setExpanded(Config.DEFAULT_EXPANDED);
        autoWindChargeSelector.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Auto WindCharge Selector"), Config.AUTO_WIND_CHARGE_SELECT).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_WIND_CHARGE_SELECT)).setSaveConsumer(newValue -> {
            Config.AUTO_WIND_CHARGE_SELECT = newValue;
        }).build());
        autoWindChargeSelector.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"\"Quick double-press\" to throw a Wind Charge"), Config.THROW_WIND_CHARGE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_THROW_WIND_CHARGE)).setSaveConsumer(newValue -> {
            Config.THROW_WIND_CHARGE = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"Press the \"throw ender pearl\" key to throw an ender pearl,\n then press it again while it's on cooldown to throw a wind charge.")}).build());
        SubCategoryBuilder pearlCatch = entryBuilder.startSubCategory((Text)Text.literal((String)"Pearl Catch")).setExpanded(Config.DEFAULT_EXPANDED);
        pearlCatch.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Pearl Catch"), Config.PEARL_CATCH).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH)).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"Throw an ender pearl, then throw a wind charge at it\nto redirect it toward you for a teleport catch.")}).build());
        pearlCatch.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Pearl Catch - Delay (ticks)"), Config.PEARL_CATCH_DELAY, 0, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_DELAY)).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_DELAY = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"Ticks to wait after throwing the pearl before\nthrowing the wind charge at it.")}).build());
        pearlCatch.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Pearl Catch - Range"), Config.PEARL_CATCH_RANGE, 3, 16).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PEARL_CATCH_RANGE)).setSaveConsumer(newValue -> {
            Config.PEARL_CATCH_RANGE = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"Maximum distance to throw the wind charge at the pearl.")}).build());
        SubCategoryBuilder toggleElytraOption = entryBuilder.startSubCategory((Text)Text.literal((String)"Toggle Elytra Option")).setExpanded(Config.DEFAULT_EXPANDED);
        toggleElytraOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Toggle Elytra Input Method"), Config.ELYTRA_MANUAL_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ELYTRA_MANUAL_MODE)).setSaveConsumer(newValue -> {
            Config.ELYTRA_MANUAL_MODE = newValue;
        }).setYesNoTextSupplier(value -> value != false ? Text.literal((String)"Simulated") : Text.literal((String)"Direct")).build());
        toggleElytraOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Toggle Elytra - Equipment Search Range"), Config.ALSO_SEARCH_INVENTORY).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ALSO_SEARCH_INVENTORY)).setSaveConsumer(newValue -> {
            Config.ALSO_SEARCH_INVENTORY = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"(Only Input Method:Direct) The inventory will also be searched.")}).setYesNoTextSupplier(value -> value != false ? Text.literal((String)"Hotbar + Inventory") : Text.literal((String)"Hotbar")).build());
        toggleElytraOption.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.toggle_elytra"), Config.TOGGLE_SLOT, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_TOGGLE_SLOT)).setSaveConsumer(newValue -> {
            Config.TOGGLE_SLOT = newValue;
        }).build());
        SubCategoryBuilder rocketBlitz = entryBuilder.startSubCategory((Text)Text.literal((String)"Rocket Blitz")).setExpanded(Config.DEFAULT_EXPANDED);
        rocketBlitz.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.rocket_blitz"), Config.ROCKET_BLITZ).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_BLITZ)).setSaveConsumer(newValue -> {
            Config.ROCKET_BLITZ = newValue;
        }).build());
        rocketBlitz.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.rocket_blitz_slot"), Config.ROCKET_BLITZ_SLOT, 0, 9).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_BLITZ_SLOT)).setSaveConsumer(newValue -> {
            Config.ROCKET_BLITZ_SLOT = newValue;
        }).build());
        rocketBlitz.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.rocket_trigger"), Config.RocketTrigger.class, Config.ROCKET_TRIGGER).setDefaultValue(Config.RocketTrigger.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ROCKET_TRIGGER))).setSaveConsumer(newValue -> {
            Config.ROCKET_TRIGGER = newValue;
        }).build());
        SubCategoryBuilder jumpSpam = entryBuilder.startSubCategory((Text)Text.literal((String)"Jump Spam")).setExpanded(Config.DEFAULT_EXPANDED);
        jumpSpam.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.jump_spam"), Config.JUMP_SPAM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_SPAM)).setSaveConsumer(newValue -> {
            Config.JUMP_SPAM = newValue;
        }).build());
        jumpSpam.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.jump_spam_tick"), Config.JUMP_SPAM_TICK, 2, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_SPAM_TICK)).setSaveConsumer(newValue -> {
            Config.JUMP_SPAM_TICK = newValue;
        }).build());
        jumpSpam.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Jump Spam Threshold"), Config.JUMP_SPAM_THRESHOLD, 0, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_JUMP_SPAM_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.JUMP_SPAM_THRESHOLD = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.jump_spam_threshold")}).build());
        SubCategoryBuilder elytraBoost = entryBuilder.startSubCategory((Text)Text.literal((String)"Elytra Boost")).setExpanded(Config.DEFAULT_EXPANDED);
        elytraBoost.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Elytra Boost"), Config.ELYTRA_BOOST).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ELYTRA_BOOST)).setSaveConsumer(newValue -> {
            Config.ELYTRA_BOOST = newValue;
        }).build());
        elytraBoost.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Elytra Boost - Reflection Angle"), Config.REFLECTION_ANGLE, -60, -10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_REFLECTION_ANGLE)).setSaveConsumer(newValue -> {
            Config.REFLECTION_ANGLE = newValue;
        }).build());
        SubCategoryBuilder prioritizeUse = entryBuilder.startSubCategory((Text)Text.literal((String)"Prioritize Use")).setExpanded(Config.DEFAULT_EXPANDED);
        prioritizeUse.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Jump Assist / Elytra Boost - Prioritize Use Wind Charge"), Config.PRIORITIZE_WIND_CHARGE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE)).setSaveConsumer(newValue -> {
            Config.PRIORITIZE_WIND_CHARGE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.prioritize")}).build());
        prioritizeUse.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Rocket Blitz - Prioritize Use Fireworks Rocket"), Config.PRIORITIZE_ROCKET).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PRIORITIZE_ROCKET)).setSaveConsumer(newValue -> {
            Config.PRIORITIZE_ROCKET = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.prioritize")}).build());
        SubCategoryBuilder jumpMode = entryBuilder.startSubCategory((Text)Text.literal((String)"Jump Mode")).setExpanded(Config.DEFAULT_EXPANDED);
        jumpMode.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.in_use_elytra_jump_mode"), Config.JumpMode.class, Config.IN_USE_ELYTRA_JUMP_MODE).setDefaultValue(Config.JumpMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_IN_USE_ELYTRA_JUMP_MODE))).setSaveConsumer(newValue -> {
            Config.IN_USE_ELYTRA_JUMP_MODE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.in_use_elytra_jump_mode")}).build());
        jumpMode.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.not_in_use_elytra_jump_mode"), Config.JumpMode.class, Config.NOT_IN_USE_ELYTRA_JUMP_MODE).setDefaultValue(Config.JumpMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_NOT_USE_ELYTRA_JUMP_MODE))).setSaveConsumer(newValue -> {
            Config.NOT_IN_USE_ELYTRA_JUMP_MODE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.not_in_use_elytra_jump_mode")}).build());
        SubCategoryBuilder autoRefill = entryBuilder.startSubCategory((Text)Text.literal((String)"Auto Refill")).setExpanded(Config.DEFAULT_EXPANDED);
        autoRefill.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.auto_refill"), Config.AUTO_REFILL).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_REFILL)).setSaveConsumer(newValue -> {
            Config.AUTO_REFILL = newValue;
        }).build());
        SubCategoryBuilder markerType = entryBuilder.startSubCategory((Text)Text.literal((String)"Marker Type")).setExpanded(Config.DEFAULT_EXPANDED);
        markerType.add(entryBuilder.startEnumSelector((Text)Text.literal((String)"Target Marker Type"), Config.MarkerType.class, Config.MARKER_TYPE).setDefaultValue(Config.MarkerType.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MARKER_TYPE))).setSaveConsumer(newValue -> {
            Config.MARKER_TYPE = newValue;
        }).build());
        SubCategoryBuilder targetSearchMode = entryBuilder.startSubCategory((Text)Text.literal((String)"Target Search Mode")).setExpanded(Config.DEFAULT_EXPANDED);
        targetSearchMode.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.target_search_mode"), Config.TARGET_SEARCH_MODE).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_TARGET_SEARCH_MODE)).setSaveConsumer(newValue -> {
            Config.TARGET_SEARCH_MODE = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.target_search_mode")}).build());
        SubCategoryBuilder displayMarkerOnTarget = entryBuilder.startSubCategory((Text)Text.literal((String)"Display Marker")).setExpanded(Config.DEFAULT_EXPANDED);
        displayMarkerOnTarget.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.on_target"), Config.TARGET_MARKER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_TARGET_MARKER)).setSaveConsumer(newValue -> {
            Config.TARGET_MARKER = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.on_target")}).build());
        SubCategoryBuilder hideMarkerWhenAimOff = entryBuilder.startSubCategory((Text)Text.literal((String)"Hide Marker")).setExpanded(Config.DEFAULT_EXPANDED);
        hideMarkerWhenAimOff.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.hide_marker"), Config.HIDE_MARKER).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HIDE_MARKER)).setSaveConsumer(newValue -> {
            Config.HIDE_MARKER = newValue;
        }).build());
        SubCategoryBuilder markerOffset = entryBuilder.startSubCategory((Text)Text.literal((String)"Marker Offset")).setExpanded(Config.DEFAULT_EXPANDED);
        markerOffset.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.marker_offset"), Config.MARKER_OFFSET, 0, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MARKER_OFFSET)).setSaveConsumer(newValue -> {
            Config.MARKER_OFFSET = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.marker_offset")}).build());
        SubCategoryBuilder attackParticle = entryBuilder.startSubCategory((Text)Text.literal((String)"Falling Attack Particles")).setExpanded(Config.DEFAULT_EXPANDED);
        attackParticle.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.on_weapon"), Config.WeaponParticle.class, Config.MACE_PARTICLE).setDefaultValue(Config.WeaponParticle.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_MACE_PARTICLE))).setSaveConsumer(newValue -> {
            Config.MACE_PARTICLE = newValue;
        }).build());
        attackParticle.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.transition_threshold"), Config.PARTICLE_TRANSITION_THRESHOLD, 10, 20).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PARTICLE_TRANSITION_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.PARTICLE_TRANSITION_THRESHOLD = newValue;
        }).build());
        attackParticle.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.particle_order"), Config.TransitionOrder.class, Config.PARTICLE_ORDER).setDefaultValue(Config.TransitionOrder.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PARTICLE_ORDER))).setSaveConsumer(newValue -> {
            Config.PARTICLE_ORDER = newValue;
        }).build());
        SubCategoryBuilder reconOption = entryBuilder.startSubCategory((Text)Text.literal((String)"Recon Option")).setExpanded(Config.DEFAULT_EXPANDED);
        reconOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Quick Zoom"), Config.RECON_QUICK_ZOOM).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RECON_QUICK_ZOOM)).setSaveConsumer(newValue -> {
            Config.RECON_QUICK_ZOOM = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.quick_zoom")}).build());
        reconOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Quick Attack"), Config.RECON_QUICK_ATTACK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RECON_QUICK_ATTACK)).setSaveConsumer(newValue -> {
            Config.RECON_QUICK_ATTACK = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.quick_attack")}).build());
        reconOption.add(entryBuilder.startEnumSelector((Text)Text.literal((String)"Recon View Perspective"), Config.ReconView.class, Config.ZOOM_VIEW).setDefaultValue(Config.ReconView.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ZOOM_VIEW))).setSaveConsumer(newValue -> {
            Config.ZOOM_VIEW = newValue;
        }).build());
        reconOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Zoom In/Out Step"), Config.ZOOM_STEP, 3, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ZOOM_STEP)).setSaveConsumer(newValue -> {
            Config.ZOOM_STEP = newValue;
        }).build());
        reconOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Display Crosshairs When [THIRD_PERSON_BACK]"), Config.PERSPECTIVE_BACK_CROSSHAIR).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PERSPECTIVE_BACK_CROSSHAIR)).setSaveConsumer(newValue -> {
            Config.PERSPECTIVE_BACK_CROSSHAIR = newValue;
        }).build());
        reconOption.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Set the current viewing direction when returning to normal state"), Config.CAMERA_RETURN_BEHAVIOR).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_CAMERA_RETURN_BEHAVIOR)).setSaveConsumer(newValue -> {
            Config.CAMERA_RETURN_BEHAVIOR = newValue;
        }).build());
        SubCategoryBuilder friendProtection = entryBuilder.startSubCategory((Text)Text.literal((String)"Friend Protection")).setExpanded(Config.DEFAULT_EXPANDED);
        friendProtection.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.friend_protection"), Config.FRIEND_PROTECTION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FRIEND_PROTECTION)).setSaveConsumer(newValue -> {
            Config.FRIEND_PROTECTION = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.friend_protection")}).build());
        friendProtection.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Displays a mark in the Player List"), Config.FRIEND_MARK).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FRIEND_MARK)).setSaveConsumer(newValue -> {
            Config.FRIEND_MARK = newValue;
        }).build());
        friendProtection.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Make a list that includes all your friends"), Config.FRIEND_NOT_FOUND).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FRIEND_NOT_FOUND)).setSaveConsumer(newValue -> {
            Config.FRIEND_NOT_FOUND = newValue;
        }).build());
        SubCategoryBuilder customCrosshair = entryBuilder.startSubCategory((Text)Text.literal((String)"Custom Crosshair")).setExpanded(Config.DEFAULT_EXPANDED);
        customCrosshair.add(entryBuilder.startEnumSelector((Text)Text.literal((String)"Use Custom Crosshairs"), Config.CrosshairMode.class, Config.CROSSHAIR_MODE).setDefaultValue(Config.CrosshairMode.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_CROSSHAIR_MODE))).setSaveConsumer(newValue -> {
            Config.CROSSHAIR_MODE = newValue;
        }).build());
        SubCategoryBuilder attackOption = entryBuilder.startSubCategory((Text)Text.literal((String)"For Development - Attack")).setExpanded(Config.DEFAULT_EXPANDED);
        attackOption.add(entryBuilder.startIntField((Text)Text.literal((String)"Attack Range"), Config.ATTACK_RANGE).setMax(400).setMin(210).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_ATTACK_RANGE)).setSaveConsumer(newValue -> {
            Config.ATTACK_RANGE = newValue;
        }).build());
        attackOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"High Speed Input Threshold"), Config.FALL_VELOCITY[0], 1, 56).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FALL_VELOCITY[0])).setSaveConsumer(newValue -> {
            Config.FALL_VELOCITY[0] = newValue;
        }).build());
        attackOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"High : Mace Attack Timing"), Config.STUN_HIGH, 3, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_STUN_HIGH)).setSaveConsumer(newValue -> {
            Config.STUN_HIGH = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"for Stun Slam : Mace Attack")}).build());
        attackOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Low : Mace Attack Timing"), Config.STUN_LOW, 3, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_STUN_LOW)).setSaveConsumer(newValue -> {
            Config.STUN_LOW = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"for Stun Slam : Mace Attack")}).build());
        attackOption.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Aim Offset"), Config.AIM_OFFSET, 0, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AIM_OFFSET)).setSaveConsumer(newValue -> {
            Config.AIM_OFFSET = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"Entity Center Add (height * 0.1 * VALUE")}).build());
        SubCategoryBuilder autoRewearDelay = entryBuilder.startSubCategory((Text)Text.literal((String)"Auto Rewear Option")).setExpanded(Config.DEFAULT_EXPANDED);
        autoRewearDelay.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.double_tap_rewear_delay"), Config.DOUBLE_TAP_REWEAR_DELAY, 0, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DOUBLE_TAP_REWEAR_DELAY)).setSaveConsumer(newValue -> {
            Config.DOUBLE_TAP_REWEAR_DELAY = newValue;
        }).build());
        SubCategoryBuilder suppression = entryBuilder.startSubCategory((Text)Text.literal((String)"For Development - Suppression")).setExpanded(Config.DEFAULT_EXPANDED);
        suppression.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.fov_suppression"), Config.FOV_SUPPRESSION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_SUPPRESSION)).setSaveConsumer(newValue -> {
            Config.FOV_SUPPRESSION = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_suppression")}).build());
        suppression.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.flap_suppression"), Config.FLAP_SUPPRESSION).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FLAP_SUPPRESSION)).setSaveConsumer(newValue -> {
            Config.FLAP_SUPPRESSION = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.flap_suppression")}).build());
        suppression.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.flap_suppression_threshold"), Config.FLAP_SUPPRESSION_THRESHOLD, 5, 20).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FLAP_SUPPRESSION_THRESHOLD)).setSaveConsumer(newValue -> {
            Config.FLAP_SUPPRESSION_THRESHOLD = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.flap_suppression")}).build());
        suppression.add(entryBuilder.startIntSlider((Text)Text.translatable((String)"config.mace_attack_assistance.option.flap_suppression_tick"), Config.FLAP_SUPPRESSION_TICK, 5, 20).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FLAP_SUPPRESSION_TICK)).setSaveConsumer(newValue -> {
            Config.FLAP_SUPPRESSION_TICK = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.flap_suppression")}).build());
        SubCategoryBuilder displayActionBar = entryBuilder.startSubCategory((Text)Text.literal((String)"Notification")).setExpanded(Config.DEFAULT_EXPANDED);
        displayActionBar.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.display_action_bar"), Config.DisplayAt.class, Config.DISPLAY_ACTION_BAR).setDefaultValue(Config.DisplayAt.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DISPLAY_ACTION_BAR))).setSaveConsumer(newValue -> {
            Config.DISPLAY_ACTION_BAR = newValue;
        }).build());
        SubCategoryBuilder debugScreen = entryBuilder.startSubCategory((Text)Text.literal((String)"Debug Screen")).setExpanded(Config.DEFAULT_EXPANDED);
        debugScreen.add(entryBuilder.startBooleanToggle((Text)Text.translatable((String)"config.mace_attack_assistance.option.debug_screen"), Config.DEBUG_SCREEN).setDefaultValue(false).setSaveConsumer(newValue -> {
            Config.DEBUG_SCREEN = newValue;
        }).build());
        SubCategoryBuilder autoElytra = entryBuilder.startSubCategory((Text)Text.literal((String)" (Experimental) Elytra - Auto Swap")).setExpanded(Config.DEFAULT_EXPANDED);
        autoElytra.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Elytra - Auto Swap"), Config.AUTO_ELYTRA).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_ELYTRA)).setSaveConsumer(newValue -> {
            Config.AUTO_ELYTRA = newValue;
        }).build());
        autoElytra.add(entryBuilder.startIntField((Text)Text.literal((String)"Distance/Speed Ratio : Stun Slam (10 - 100) * 0.1 "), Config.AUTO_ELYTRA_TICK_AHEAD).setMax(100).setMin(10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD)).setSaveConsumer(newValue -> {
            Config.AUTO_ELYTRA_TICK_AHEAD = newValue;
        }).build());
        autoElytra.add(entryBuilder.startIntField((Text)Text.literal((String)"Distance/Speed Ratio : Hot Swap (10 - 100) * 0.1"), Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL)).setMax(100).setMin(10).setSaveConsumer(newValue -> {
            Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL = newValue;
        }).build());
        SubCategoryBuilder spiralSetting = entryBuilder.startSubCategory((Text)Text.literal((String)"Spiral")).setExpanded(Config.DEFAULT_EXPANDED);
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Spiral Count"), Config.SP_SPIRAL_COUNT, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_COUNT)).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_COUNT = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Spiral Length"), Config.SP_SPIRAL_LENGTH, 1, 40).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_LENGTH)).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_LENGTH = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Spiral Alpha"), Config.SP_SPIRAL_ALPHA, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_ALPHA)).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_ALPHA = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Spiral Gamma"), Config.SP_SPIRAL_GAMMA, 10, 50).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPIRAL_GAMMA)).setSaveConsumer(newValue -> {
            Config.SP_SPIRAL_GAMMA = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Coils"), Config.SP_COILS, 1, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COILS)).setSaveConsumer(newValue -> {
            Config.SP_COILS = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Height"), Config.SP_HEIGHT, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_HEIGHT)).setSaveConsumer(newValue -> {
            Config.SP_HEIGHT = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Size"), Config.SP_SIZE, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SIZE)).setSaveConsumer(newValue -> {
            Config.SP_SIZE = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Speed"), Config.SP_SPEED, 1, 50).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_SPEED)).setSaveConsumer(newValue -> {
            Config.SP_SPEED = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Base Radius"), Config.SP_BASE_RADIUS, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_BASE_RADIUS)).setSaveConsumer(newValue -> {
            Config.SP_BASE_RADIUS = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Wave Speed"), Config.SP_WAVE_SPEED, 1, 80).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WAVE_SPEED)).setSaveConsumer(newValue -> {
            Config.SP_WAVE_SPEED = newValue;
        }).build());
        spiralSetting.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Wave Amplitude"), Config.SP_WAVE_AMPLITUDE, 1, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_WAVE_AMPLITUDE)).setSaveConsumer(newValue -> {
            Config.SP_WAVE_AMPLITUDE = newValue;
        }).build());
        SubCategoryBuilder markerColor = entryBuilder.startSubCategory((Text)Text.literal((String)"Marker Color")).setExpanded(Config.DEFAULT_EXPANDED);
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_villager"), ColorData.Color.class, Config.COLOR_VILLAGER).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_VILLAGER))).setSaveConsumer(newValue -> {
            Config.COLOR_VILLAGER = newValue;
        }).build());
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_hostile"), ColorData.Color.class, Config.COLOR_HOSTILE).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_HOSTILE))).setSaveConsumer(newValue -> {
            Config.COLOR_HOSTILE = newValue;
        }).build());
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_warden"), ColorData.Color.class, Config.COLOR_WARDEN).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_WARDEN))).setSaveConsumer(newValue -> {
            Config.COLOR_WARDEN = newValue;
        }).build());
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_iron_golem"), ColorData.Color.class, Config.COLOR_IRON_GOLEM).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_IRON_GOLEM))).setSaveConsumer(newValue -> {
            Config.COLOR_IRON_GOLEM = newValue;
        }).build());
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_passive"), ColorData.Color.class, Config.COLOR_PASSIVE).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_PASSIVE))).setSaveConsumer(newValue -> {
            Config.COLOR_PASSIVE = newValue;
        }).build());
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_ambient"), ColorData.Color.class, Config.COLOR_AMBIENT).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_AMBIENT))).setSaveConsumer(newValue -> {
            Config.COLOR_AMBIENT = newValue;
        }).build());
        markerColor.add(entryBuilder.startEnumSelector((Text)Text.translatable((String)"config.mace_attack_assistance.option.allowed_player"), ColorData.Color.class, Config.COLOR_PLAYER).setDefaultValue(ColorData.Color.valueOf((String)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_COLOR_PLAYER))).setSaveConsumer(newValue -> {
            Config.COLOR_PLAYER = newValue;
        }).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.dark_blue).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.dark_green).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.dark_aqua).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.dark_red).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.dark_purple).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.gold).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.gray).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.dark_gray).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.blue).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.green).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.aqua).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.red).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.light_purple).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.yellow).build());
        markerColor.add(entryBuilder.startTextDescription((Text)ColorData.white).build());
        SubCategoryBuilder radarBoxFov = entryBuilder.startSubCategory((Text)Text.literal((String)"Radar")).setExpanded(Config.DEFAULT_EXPANDED);
        radarBoxFov.add(entryBuilder.startTextDescription((Text)Text.translatable((String)"config.mace_attack_assistance.option.desc_fov").formatted(Formatting.BOLD).formatted(Formatting.GREEN)).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"FOV X (Horizontal) * 10 degree [Gliding , Target Search: On]"), Config.FOV_HORIZONTAL, 1, 36).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_HORIZONTAL)).setSaveConsumer(newValue -> {
            Config.FOV_HORIZONTAL = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_horizontal")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"FOV Y (Vertical) * 10 degree [Gliding , Target Search: On]"), Config.FOV_VERTICAL, 1, 18).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_VERTICAL)).setSaveConsumer(newValue -> {
            Config.FOV_VERTICAL = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_vertical")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"FOV X (Horizontal) * 10 degree [Recon Camera Mode]"), Config.FOV_HORIZONTAL_ON_ZOOM, 1, 36).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_HORIZONTAL_ON_ZOOM)).setSaveConsumer(newValue -> {
            Config.FOV_HORIZONTAL_ON_ZOOM = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_horizontal")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"FOV Y (Vertical) * 10 degree [Recon Camera Mode]"), Config.FOV_VERTICAL_ON_ZOOM, 1, 18).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_FOV_VERTICAL_ON_ZOOM)).setSaveConsumer(newValue -> {
            Config.FOV_VERTICAL_ON_ZOOM = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.fov_vertical")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Rectangle Horizontal * 10 blocks"), Config.RADAR_HORIZONTAL, 1, 5).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_HORIZONTAL)).setSaveConsumer(newValue -> {
            Config.RADAR_HORIZONTAL = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_horizontal")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Rectangle Upward * 10 blocks"), Config.RADAR_UPWARD, 1, 3).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_UPWARD)).setSaveConsumer(newValue -> {
            Config.RADAR_UPWARD = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_upward")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Rectangle Downward * 10 blocks"), Config.RADAR_DOWNWARD, 1, 8).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_DOWNWARD)).setSaveConsumer(newValue -> {
            Config.RADAR_DOWNWARD = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_downward")}).build());
        radarBoxFov.add(entryBuilder.startIntSlider((Text)Text.literal((String)"Rectangle Update Interval"), Config.RADAR_UPDATE_INTERVAL, 1, 10).setDefaultValue((Integer)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_RADAR_UPDATE_INTERVAL)).setSaveConsumer(newValue -> {
            Config.RADAR_UPDATE_INTERVAL = newValue;
        }).setTooltip(new Text[]{Text.translatable((String)"config.mace_attack_assistance.option.tooltip.radar_update_interval")}).build());
        radarBoxFov.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Extraction Processing Thread"), Config.PARALLEL_SEARCH).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_PARALLEL_SEARCH)).setSaveConsumer(newValue -> {
            Config.PARALLEL_SEARCH = newValue;
        }).setTooltip(new Text[]{Text.literal((String)"(Parallel:Experimental)")}).setYesNoTextSupplier(value -> value != false ? Text.literal((String)"Parallel") : Text.literal((String)"Single")).build());
        SubCategoryBuilder defaultExpanded = entryBuilder.startSubCategory((Text)Text.literal((String)"Expand Categories")).setExpanded(Config.DEFAULT_EXPANDED);
        defaultExpanded.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Global"), Config.DEFAULT_EXPANDED_GLOBAL).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DEFAULT_EXPANDED_GLOBAL)).setSaveConsumer(newValue -> {
            Config.DEFAULT_EXPANDED_GLOBAL = newValue;
        }).build());
        defaultExpanded.add(entryBuilder.startBooleanToggle((Text)Text.literal((String)"Sub"), Config.DEFAULT_EXPANDED).setDefaultValue((Boolean)ModConfigScreen.getDefaultValue(ConfigOperation.PROP_DEFAULT_EXPANDED)).setSaveConsumer(newValue -> {
            Config.DEFAULT_EXPANDED = newValue;
        }).build());
        attack.addEntry(attackAssist.build());
        attack.addEntry(hotSwap.build());
        attack.addEntry(spearSlam.build());
        attack.addEntry(breachSwap.build());
        attack.addEntry(stunSlam.build());
        attack.addEntry(doubleTap.build());
        attack.addEntry(snapback.build());
        attack.addEntry(shieldDraining.build());
        aim.addEntry(aimAssist.build());
        aim.addEntry(aimTarget.build());
        aim.addEntry(aimOption.build());
        jump.addEntry(jumpAssist.build());
        jump.addEntry(wallClimbing.build());
        jump.addEntry(jumpSpam.build());
        jump.addEntry(jumpMode.build());
        elytra.addEntry(toggleElytraOption.build());
        elytra.addEntry(elytraBoost.build());
        elytra.addEntry(customCrosshair.build());
        item.addEntry(autoRefill.build());
        item.addEntry(autoWindChargeSelector.build());
        item.addEntry(pearlCatch.build());
        item.addEntry(rocketBlitz.build());
        item.addEntry(prioritizeUse.build());
        slot.addEntry(weaponSlotSetting.build());
        slot.addEntry(returnToPrevSlot.build());
        recon.addEntry(reconOption.build());
        friend.addEntry(friendProtection.build());
        effects.addEntry(markerType.build());
        effects.addEntry(targetSearchMode.build());
        effects.addEntry(displayMarkerOnTarget.build());
        effects.addEntry(hideMarkerWhenAimOff.build());
        effects.addEntry(markerOffset.build());
        effects.addEntry(attackParticle.build());
        spiral.addEntry(spiralSetting.build());
        color.addEntry(markerColor.build());
        fov.addEntry(radarBoxFov.build());
        others.addEntry(defaultExpanded.build());
        others.addEntry(autoRewearDelay.build());
        others.addEntry(displayActionBar.build());
        others.addEntry(attackOption.build());
        others.addEntry(suppression.build());
        others.addEntry(debugScreen.build());
        others.addEntry(autoElytra.build());
        builder.setSavingRunnable(ConfigOperation::saveFile);
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
}
