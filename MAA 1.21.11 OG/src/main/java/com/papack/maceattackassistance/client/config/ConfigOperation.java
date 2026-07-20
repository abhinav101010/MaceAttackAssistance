/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.papack.maceattackassistance.client.config;

import com.papack.maceattackassistance.client.ColorData;
import com.papack.maceattackassistance.client.config.Config;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class ConfigOperation {
    public static final ConfigData PROP_ATTACK_ASSISTANCE = new ConfigData("MaceAttackAssistance", "true", Type.B);
    public static final ConfigData PROP_WEAPON_SWING = new ConfigData("WeaponSwing", "false", Type.B);
    public static final ConfigData PROP_SWING_TOGGLE = new ConfigData("SwingToggle", "L_SHIFT", Type.E);
    public static final ConfigData PROP_HOT_SWAP = new ConfigData("HotSwap", "true", Type.B);
    public static final ConfigData PROP_STUN_SLAMMING = new ConfigData("StunSlumming", "true", Type.B);
    public static final ConfigData PROP_BREACH_SWAP = new ConfigData("BreachSwap", "true", Type.B);
    public static final ConfigData PROP_BREACH_LIMITED = new ConfigData("BreachLimited", "false", Type.B);
    public static final ConfigData PROP_BREACH_ON_GROUND = new ConfigData("BreachOnGround", "false", Type.B);
    public static final ConfigData PROP_SNAPBACK = new ConfigData("Snapback", "true", Type.B);
    public static final ConfigData PROP_SNAPBACK_THRESHOLD = new ConfigData("SnapbackThreshold", "8", Type.I);
    public static final ConfigData PROP_SNAPBACK_TOLERANCE = new ConfigData("SnapbackTolerance", "200", Type.I);
    public static final ConfigData PROP_AXE_SLOT = new ConfigData("AxeSlot", "-1", Type.I);
    public static final ConfigData PROP_MACE_PRIMARY = new ConfigData("MacePrimary", "-1", Type.I);
    public static final ConfigData PROP_MACE_BREACH = new ConfigData("MaceBreach", "-1", Type.I);
    public static final ConfigData PROP_RETURN_TO_PREV_SLOT = new ConfigData("ReturnToPrevSlot", "true", Type.B);
    public static final ConfigData PROP_AIM_ASSIST = new ConfigData("AimAssist", "true", Type.B);
    public static final ConfigData PROP_ALLOWED_VILLAGER = new ConfigData("AllowedVillager", "true", Type.B);
    public static final ConfigData PROP_ALLOWED_IRON_GOLEM = new ConfigData("AllowedIronGolem", "true", Type.B);
    public static final ConfigData PROP_ALLOWED_HOSTILE = new ConfigData("AllowedHostile", "true", Type.B);
    public static final ConfigData PROP_ALLOWED_PASSIVE = new ConfigData("AllowedPassive", "true", Type.B);
    public static final ConfigData PROP_ALLOWED_AMBIENT = new ConfigData("AllowedAmbient", "true", Type.B);
    public static final ConfigData PROP_ALLOWED_PLAYER = new ConfigData("AllowedPlayer", "true", Type.B);
    public static final ConfigData PROP_MAX_SPEED_YAW = new ConfigData("MaxSpeedYaw", "5", Type.I);
    public static final ConfigData PROP_AIM_MODE = new ConfigData("AimMode", "Auto", Type.E);
    public static final ConfigData PROP_AUTO_MODE_THRESHOLD = new ConfigData("AutoModeThreshold", "15", Type.I);
    public static final ConfigData PROP_JUMP_ASSIST = new ConfigData("JumpAssist", "true", Type.B);
    public static final ConfigData PROP_WALL_CLIMBING = new ConfigData("WallJump", "true", Type.B);
    public static final ConfigData PROP_TOGGLE_SLOT = new ConfigData("ToggleSlot", "0", Type.I);
    public static final ConfigData PROP_ROCKET_BLITZ = new ConfigData("RocketBlitz", "true", Type.B);
    public static final ConfigData PROP_ROCKET_BLITZ_SLOT = new ConfigData("RocketBlitzSlot", "0", Type.I);
    public static final ConfigData PROP_ROCKET_TRIGGER = new ConfigData("RocketTrigger", "SPACE", Type.E);
    public static final ConfigData PROP_JUMP_SPAM = new ConfigData("JumpSpam", "true", Type.B);
    public static final ConfigData PROP_JUMP_SPAM_TICK = new ConfigData("JumpSpamTick", "2", Type.I);
    public static final ConfigData PROP_REFLECTION_ANGLE = new ConfigData("ReflectionAngle", "-35", Type.I);
    public static final ConfigData PROP_PRIORITIZE_WIND_CHARGE = new ConfigData("PrioritizeWindCharge", "false", Type.B);
    public static final ConfigData PROP_PRIORITIZE_ROCKET = new ConfigData("PrioritizeRocket", "false", Type.B);
    public static final ConfigData PROP_AUTO_REFILL = new ConfigData("AutoRefill", "false", Type.B);
    public static final ConfigData PROP_TARGET_MARKER = new ConfigData("TargetMarker", "true", Type.B);
    public static final ConfigData PROP_HIDE_MARKER = new ConfigData("HideMarker", "true", Type.B);
    public static final ConfigData PROP_MARKER_OFFSET = new ConfigData("MarkerOffset", "1", Type.I);
    public static final ConfigData PROP_MACE_PARTICLE = new ConfigData("MaceParticle", "Transition", Type.E);
    public static final ConfigData PROP_PARTICLE_TRANSITION_THRESHOLD = new ConfigData("Threshold", "10", Type.I);
    public static final ConfigData PROP_PARTICLE_ORDER = new ConfigData("ParticleOrder", "Blue_Red", Type.E);
    public static final ConfigData PROP_MARKER_TYPE = new ConfigData("MarkerType", "Spiral", Type.E);
    public static final ConfigData PROP_ZOOM_VIEW = new ConfigData("ZoomView", "THIRD_PERSON_BACK", Type.E);
    public static final ConfigData PROP_ZOOM_STEP = new ConfigData("ZoomInOutStep", "7", Type.I);
    public static final ConfigData PROP_CAMERA_RETURN_BEHAVIOR = new ConfigData("CameraReturnBehavior", "true", Type.B);
    public static final ConfigData PROP_PERSPECTIVE_BACK_CROSSHAIR = new ConfigData("PerspectiveBackCrosshair", "true", Type.B);
    public static final ConfigData PROP_DISPLAY_ACTION_BAR = new ConfigData("DisplayActionBar", "true", Type.B);
    public static final ConfigData PROP_EXTREME = new ConfigData("Extreme", "false", Type.B);
    public static final ConfigData PROP_MANUAL_CAMERA_PITCH = new ConfigData("ManualCameraPitch", "30", Type.I);
    public static final ConfigData PROP_MANUAL_INCIDENCE_TICK = new ConfigData("ManualIncidenceTick", "0", Type.I);
    public static final ConfigData PROP_SNAPBACK_Y_OFFSET = new ConfigData("SnapbackYoffset", "-4", Type.I);
    public static final ConfigData[] PROP_ATTACK_DISTANCE = new ConfigData[]{new ConfigData("AttackDistance0", "80", Type.I), new ConfigData("AttackDistance1", "120", Type.I), new ConfigData("AttackDistance2", "160", Type.I), new ConfigData("AttackDistance3", "200", Type.I)};
    public static final ConfigData[] PROP_FALL_VELOCITY = new ConfigData[]{new ConfigData("FallVelocity0", "29", Type.I), new ConfigData("FallVelocity1", "34", Type.I), new ConfigData("FallVelocity2", "43", Type.I), new ConfigData("FallVelocity3", "52", Type.I)};
    public static final ConfigData PROP_ATTACK_RANGE = new ConfigData("AttackRang", "300", Type.I);
    public static final ConfigData PROP_ATTACK_RANGE_DIFF = new ConfigData("AttackYDiff", "281", Type.I);
    public static final ConfigData PROP_HEIGHT_THRESHOLD = new ConfigData("HeightThreshold", "2", Type.I);
    public static final ConfigData PROP_COOL_DOWN_TICKS = new ConfigData("CoolDownTicks", "10", Type.I);
    public static final ConfigData PROP_SPIRAL_COUNT = new ConfigData("sp_SpiralCount", "3", Type.I);
    public static final ConfigData PROP_SPAIRAL_LENGTH = new ConfigData("sp_SpiralLength", "30", Type.I);
    public static final ConfigData PROP_SPIRAL_ALPHA = new ConfigData("sp_SpiralAlpha", "10", Type.I);
    public static final ConfigData PROP_SPIRAL_GAMMA = new ConfigData("sp_SpiralGamma", "30", Type.I);
    public static final ConfigData PROP_COILS = new ConfigData("sp_Coils", "1", Type.I);
    public static final ConfigData PROP_HEIGHT = new ConfigData("sp_Height", "1", Type.I);
    public static final ConfigData PROP_SIZE = new ConfigData("sp_Size", "7", Type.I);
    public static final ConfigData PROP_SPEED = new ConfigData("sp_Speed", "8", Type.I);
    public static final ConfigData PROP_BASE_RADIUS = new ConfigData("sp_BaseRadius", "8", Type.I);
    public static final ConfigData PROP_WAVE_SPEED = new ConfigData("sp_WaveSpeed", "50", Type.I);
    public static final ConfigData PROP_WAVE_AMPLITUDE = new ConfigData("sp_WaveAmplitude", "2", Type.I);
    public static final ConfigData PROP_COLOR_VILLAGER = new ConfigData("color_Villager", "GREEN", Type.E);
    public static final ConfigData PROP_COLOR_HOSTILE = new ConfigData("color_Hostile", "BLUE", Type.E);
    public static final ConfigData PROP_COLOR_WARDEN = new ConfigData("color_Warden", "LIGHT_PURPLE", Type.E);
    public static final ConfigData PROP_COLOR_IRON_GOLEM = new ConfigData("color_IronGolem", "GREEN", Type.E);
    public static final ConfigData PROP_COLOR_PASSIVE = new ConfigData("color_Passive", "GREEN", Type.E);
    public static final ConfigData PROP_COLOR_AMBIENT = new ConfigData("color_Ambient", "GREEN", Type.E);
    public static final ConfigData PROP_COLOR_PLAYER = new ConfigData("color_Player", "GOLD", Type.E);
    public static final ConfigData PROP_FOV_HORIZONTAL = new ConfigData("FovHorizontal", "18", Type.I);
    public static final ConfigData PROP_FOV_HORIZONTAL_ON_ZOOM = new ConfigData("FovHorizontalOnZoom", "36", Type.I);
    public static final ConfigData PROP_FOV_VERTICAL = new ConfigData("FovVertical", "18", Type.I);
    public static final ConfigData PROP_FOV_VERTICAL_ON_ZOOM = new ConfigData("FovVerticalOnZoom", "18", Type.I);
    public static final ConfigData PROP_RADAR_HORIZONTAL = new ConfigData("RadarHorizontal", "3", Type.I);
    public static final ConfigData PROP_RADAR_UPWARD = new ConfigData("RadarUpward", "2", Type.I);
    public static final ConfigData PROP_RADAR_DOWNWARD = new ConfigData("RadarDownward", "5", Type.I);
    public static final ConfigData PROP_RADAR_UPDATE_INTERVAL = new ConfigData("RadarUpdateInterval", "10", Type.I);
    public static final ConfigData PROP_TARGET_SEARCH_MODE = new ConfigData("TargetSearchMode", "false", Type.B);
    public static final ConfigData PROP_FOV_SUPPRESSION = new ConfigData("FOVSuppression", "true", Type.B);
    public static final ConfigData PROP_FLAP_SUPPRESSION = new ConfigData("FlapSuppression", "true", Type.B);
    public static final ConfigData PROP_FLAP_SUPPRESSION_THRESHOLD = new ConfigData("FlapSuppressionThreshold", "10", Type.I);
    public static final ConfigData PROP_FLAP_SUPPRESSION_TICK = new ConfigData("FlapSuppressionTick", "20", Type.I);
    public static final ConfigData PROP_FRIENDS = new ConfigData("Friends", "", Type.E);
    private static final List<ConfigData> configList = new ArrayList<ConfigData>(Arrays.asList(PROP_AIM_ASSIST, PROP_AIM_MODE, PROP_AIM_MODE, PROP_ALLOWED_AMBIENT, PROP_ALLOWED_HOSTILE, PROP_ALLOWED_IRON_GOLEM, PROP_ALLOWED_PASSIVE, PROP_ALLOWED_PLAYER, PROP_ALLOWED_VILLAGER, PROP_ATTACK_ASSISTANCE, PROP_ATTACK_DISTANCE[0], PROP_ATTACK_DISTANCE[1], PROP_ATTACK_DISTANCE[2], PROP_ATTACK_DISTANCE[3], PROP_ATTACK_RANGE, PROP_ATTACK_RANGE_DIFF, PROP_AUTO_MODE_THRESHOLD, PROP_AUTO_REFILL, PROP_AXE_SLOT, PROP_BASE_RADIUS, PROP_BREACH_LIMITED, PROP_BREACH_ON_GROUND, PROP_BREACH_SWAP, PROP_CAMERA_RETURN_BEHAVIOR, PROP_COILS, PROP_COLOR_AMBIENT, PROP_COLOR_HOSTILE, PROP_COLOR_IRON_GOLEM, PROP_COLOR_PASSIVE, PROP_COLOR_PLAYER, PROP_COLOR_VILLAGER, PROP_COLOR_WARDEN, PROP_COOL_DOWN_TICKS, PROP_DISPLAY_ACTION_BAR, PROP_EXTREME, PROP_FALL_VELOCITY[0], PROP_FALL_VELOCITY[1], PROP_FALL_VELOCITY[2], PROP_FALL_VELOCITY[3], PROP_FLAP_SUPPRESSION, PROP_FLAP_SUPPRESSION_THRESHOLD, PROP_FLAP_SUPPRESSION_TICK, PROP_FOV_HORIZONTAL, PROP_FOV_HORIZONTAL_ON_ZOOM, PROP_FOV_SUPPRESSION, PROP_FOV_VERTICAL, PROP_FOV_VERTICAL_ON_ZOOM, PROP_HEIGHT, PROP_HEIGHT_THRESHOLD, PROP_HIDE_MARKER, PROP_HOT_SWAP, PROP_JUMP_ASSIST, PROP_JUMP_SPAM, PROP_JUMP_SPAM_TICK, PROP_MACE_BREACH, PROP_MACE_PARTICLE, PROP_MACE_PRIMARY, PROP_MANUAL_CAMERA_PITCH, PROP_MANUAL_INCIDENCE_TICK, PROP_MARKER_OFFSET, PROP_MARKER_TYPE, PROP_MAX_SPEED_YAW, PROP_SPAIRAL_LENGTH, PROP_PARTICLE_ORDER, PROP_PARTICLE_TRANSITION_THRESHOLD, PROP_PERSPECTIVE_BACK_CROSSHAIR, PROP_PRIORITIZE_ROCKET, PROP_PRIORITIZE_WIND_CHARGE, PROP_RADAR_DOWNWARD, PROP_RADAR_HORIZONTAL, PROP_RADAR_UPDATE_INTERVAL, PROP_RADAR_UPWARD, PROP_REFLECTION_ANGLE, PROP_ROCKET_BLITZ, PROP_ROCKET_BLITZ_SLOT, PROP_ROCKET_TRIGGER, PROP_SIZE, PROP_SNAPBACK, PROP_SNAPBACK_THRESHOLD, PROP_SNAPBACK_TOLERANCE, PROP_SNAPBACK_Y_OFFSET, PROP_SPEED, PROP_SPIRAL_ALPHA, PROP_SPIRAL_GAMMA, PROP_SPIRAL_COUNT, PROP_STUN_SLAMMING, PROP_SWING_TOGGLE, PROP_TARGET_MARKER, PROP_TARGET_SEARCH_MODE, PROP_TOGGLE_SLOT, PROP_WALL_CLIMBING, PROP_WAVE_AMPLITUDE, PROP_WAVE_SPEED, PROP_WEAPON_SWING, PROP_ZOOM_STEP, PROP_ZOOM_VIEW, PROP_RETURN_TO_PREV_SLOT));

    public static void existFile() {
        if (!Files.exists(Config.MAA_CONF_DIR, new LinkOption[0])) {
            try {
                Files.createDirectories(Config.MAA_CONF_DIR, new FileAttribute[0]);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Files.exists(Config.MAA_CONFIG, new LinkOption[0])) {
            try {
                Files.createFile(Config.MAA_CONFIG, new FileAttribute[0]);
                Properties properties = new Properties();
                for (ConfigData data : configList) {
                    properties.setProperty(data.label, data.defaultValue());
                }
                properties.store(new FileOutputStream(String.valueOf(Config.MAA_CONFIG)), "");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadFile() {
        Properties properties = new Properties();
        String ipPass = Config.MAA_CONFIG.toString();
        try {
            FileInputStream inputStream = new FileInputStream(ipPass);
            properties.load(inputStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        Config.ATTACK_ASSISTANCE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ATTACK_ASSISTANCE.label, ConfigOperation.PROP_ATTACK_ASSISTANCE.defaultValue));
        Config.AIM_ASSIST = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AIM_ASSIST.label, ConfigOperation.PROP_AIM_ASSIST.defaultValue));
        Config.JUMP_ASSIST = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_JUMP_ASSIST.label, ConfigOperation.PROP_JUMP_ASSIST.defaultValue));
        Config.WEAPON_SWING = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_WEAPON_SWING.label, ConfigOperation.PROP_WEAPON_SWING.defaultValue));
        Config.SWING_TOGGLE = Config.SwingToggle.valueOf(properties.getProperty(ConfigOperation.PROP_SWING_TOGGLE.label, ConfigOperation.PROP_SWING_TOGGLE.defaultValue));
        Config.SNAPBACK_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SNAPBACK_THRESHOLD.label, ConfigOperation.PROP_SNAPBACK_THRESHOLD.defaultValue));
        Config.SNAPBACK_TOLERANCE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SNAPBACK_TOLERANCE.label, ConfigOperation.PROP_SNAPBACK_TOLERANCE.defaultValue));
        Config.RETURN_TO_PREV_SLOT = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT.label, ConfigOperation.PROP_RETURN_TO_PREV_SLOT.defaultValue));
        Config.TARGET_MARKER = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_TARGET_MARKER.label, ConfigOperation.PROP_TARGET_MARKER.defaultValue));
        Config.MARKER_TYPE = Config.MarkerType.valueOf(properties.getProperty(ConfigOperation.PROP_MARKER_TYPE.label, ConfigOperation.PROP_MARKER_TYPE.defaultValue));
        Config.MACE_PARTICLE = Config.WeaponParticle.valueOf(properties.getProperty(ConfigOperation.PROP_MACE_PARTICLE.label, ConfigOperation.PROP_MACE_PARTICLE.defaultValue));
        Config.PARTICLE_TRANSITION_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_PARTICLE_TRANSITION_THRESHOLD.label, ConfigOperation.PROP_PARTICLE_TRANSITION_THRESHOLD.defaultValue));
        Config.PARTICLE_ORDER = Config.TransitionOrder.valueOf(properties.getProperty(ConfigOperation.PROP_PARTICLE_ORDER.label, ConfigOperation.PROP_PARTICLE_ORDER.defaultValue));
        Config.HOT_SWAP = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_HOT_SWAP.label, ConfigOperation.PROP_HOT_SWAP.defaultValue));
        Config.HEIGHT_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_HEIGHT_THRESHOLD.label, ConfigOperation.PROP_HEIGHT_THRESHOLD.defaultValue));
        Config.HIDE_MARKER = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_HIDE_MARKER.label, ConfigOperation.PROP_HIDE_MARKER.defaultValue));
        Config.MARKER_OFFSET = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MARKER_OFFSET.label, ConfigOperation.PROP_MARKER_OFFSET.defaultValue));
        Config.STUN_SLAMMING = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_STUN_SLAMMING.label, ConfigOperation.PROP_STUN_SLAMMING.defaultValue));
        Config.BREACH_SWAP = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_BREACH_SWAP.label, ConfigOperation.PROP_BREACH_SWAP.defaultValue));
        Config.BREACH_LIMITED = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_BREACH_LIMITED.label, ConfigOperation.PROP_BREACH_LIMITED.defaultValue));
        Config.BREACH_ON_GROUND = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_BREACH_ON_GROUND.label, ConfigOperation.PROP_BREACH_ON_GROUND.defaultValue));
        Config.ROCKET_BLITZ = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ROCKET_BLITZ.label, ConfigOperation.PROP_ROCKET_BLITZ.defaultValue));
        Config.ROCKET_BLITZ_SLOT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ROCKET_BLITZ_SLOT.label, ConfigOperation.PROP_ROCKET_BLITZ_SLOT.defaultValue));
        Config.AUTO_REFILL = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AUTO_REFILL.label, ConfigOperation.PROP_AUTO_REFILL.defaultValue));
        Config.ROCKET_TRIGGER = Config.RocketTrigger.valueOf(properties.getProperty(ConfigOperation.PROP_ROCKET_TRIGGER.label, ConfigOperation.PROP_ROCKET_TRIGGER.defaultValue));
        Config.AXE_SLOT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AXE_SLOT.label, ConfigOperation.PROP_AXE_SLOT.defaultValue));
        Config.MACE_PRIMARY = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MACE_PRIMARY.label, ConfigOperation.PROP_MACE_PRIMARY.defaultValue));
        Config.MACE_BREACH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MACE_BREACH.label, ConfigOperation.PROP_MACE_BREACH.defaultValue));
        Config.ALLOWED_VILLAGER = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_VILLAGER.label, ConfigOperation.PROP_ALLOWED_VILLAGER.defaultValue));
        Config.ALLOWED_IRON_GOLEM = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_IRON_GOLEM.label, ConfigOperation.PROP_ALLOWED_IRON_GOLEM.defaultValue));
        Config.ALLOWED_HOSTILE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_HOSTILE.label, ConfigOperation.PROP_ALLOWED_HOSTILE.defaultValue));
        Config.ALLOWED_PASSIVE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_PASSIVE.label, ConfigOperation.PROP_ALLOWED_PASSIVE.defaultValue));
        Config.ALLOWED_AMBIENT = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_AMBIENT.label, ConfigOperation.PROP_ALLOWED_AMBIENT.defaultValue));
        Config.ALLOWED_PLAYER = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_PLAYER.label, ConfigOperation.PROP_ALLOWED_PLAYER.defaultValue));
        Config.MAX_SPEED_YAW = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MAX_SPEED_YAW.label, ConfigOperation.PROP_MAX_SPEED_YAW.defaultValue));
        Config.AIM_MODE = Config.AimMode.valueOf(properties.getProperty(ConfigOperation.PROP_AIM_MODE.label, ConfigOperation.PROP_AIM_MODE.defaultValue));
        Config.AUTO_MODE_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AUTO_MODE_THRESHOLD.label, ConfigOperation.PROP_AUTO_MODE_THRESHOLD.defaultValue));
        Config.DISPLAY_ACTION_BAR = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_DISPLAY_ACTION_BAR.label, ConfigOperation.PROP_DISPLAY_ACTION_BAR.defaultValue));
        Config.TOGGLE_SLOT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_TOGGLE_SLOT.label, ConfigOperation.PROP_TOGGLE_SLOT.defaultValue));
        Config.JUMP_SPAM = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_JUMP_SPAM.label, ConfigOperation.PROP_JUMP_SPAM.defaultValue));
        Config.JUMP_SPAM_TICK = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_JUMP_SPAM_TICK.label, ConfigOperation.PROP_JUMP_SPAM_TICK.defaultValue));
        Config.WALL_CLIMBING = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_WALL_CLIMBING.label, ConfigOperation.PROP_WALL_CLIMBING.defaultValue));
        Config.SNAPBACK = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SNAPBACK.label, ConfigOperation.PROP_SNAPBACK.defaultValue));
        Config.REFLECTION_ANGLE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_REFLECTION_ANGLE.label, ConfigOperation.PROP_REFLECTION_ANGLE.defaultValue));
        Config.COOL_DOWN_TICKS = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_COOL_DOWN_TICKS.label, ConfigOperation.PROP_COOL_DOWN_TICKS.defaultValue));
        Config.PRIORITIZE_WIND_CHARGE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE.label, ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE.defaultValue));
        Config.PRIORITIZE_ROCKET = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PRIORITIZE_ROCKET.label, ConfigOperation.PROP_PRIORITIZE_ROCKET.defaultValue));
        Config.ATTACK_RANGE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ATTACK_RANGE.label, ConfigOperation.PROP_ATTACK_RANGE.defaultValue));
        Config.ATTACK_RANGE_DIFF = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ATTACK_RANGE_DIFF.label, ConfigOperation.PROP_ATTACK_RANGE_DIFF.defaultValue));
        for (int i = 0; i < 4; ++i) {
            Config.ATTACK_DISTANCE[i] = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ATTACK_DISTANCE[i].label, ConfigOperation.PROP_ATTACK_DISTANCE[i].defaultValue));
            Config.FALL_VELOCITY[i] = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FALL_VELOCITY[i].label, ConfigOperation.PROP_FALL_VELOCITY[i].defaultValue));
        }
        Config.MANUAL_CAMERA_PITCH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MANUAL_CAMERA_PITCH.label, ConfigOperation.PROP_MANUAL_CAMERA_PITCH.defaultValue));
        Config.SP_SPIRAL_COUNT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPIRAL_COUNT.label, ConfigOperation.PROP_SPIRAL_COUNT.defaultValue));
        Config.SP_SPIRAL_LENGTH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPAIRAL_LENGTH.label, ConfigOperation.PROP_SPAIRAL_LENGTH.defaultValue));
        Config.SP_SPIRAL_ALPHA = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPIRAL_ALPHA.label, ConfigOperation.PROP_SPIRAL_ALPHA.defaultValue));
        Config.SP_SPIRAL_GAMMA = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPIRAL_GAMMA.label, ConfigOperation.PROP_SPIRAL_GAMMA.defaultValue));
        Config.SP_COILS = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_COILS.label, ConfigOperation.PROP_COILS.defaultValue));
        Config.SP_HEIGHT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_HEIGHT.label, ConfigOperation.PROP_HEIGHT.defaultValue));
        Config.SP_SIZE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SIZE.label, ConfigOperation.PROP_SIZE.defaultValue));
        Config.SP_SPEED = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPEED.label, ConfigOperation.PROP_SPEED.defaultValue));
        Config.SP_BASE_RADIUS = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_BASE_RADIUS.label, ConfigOperation.PROP_BASE_RADIUS.defaultValue));
        Config.SP_WAVE_SPEED = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_WAVE_SPEED.label, ConfigOperation.PROP_WAVE_SPEED.defaultValue));
        Config.SP_WAVE_AMPLITUDE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_WAVE_AMPLITUDE.label, ConfigOperation.PROP_WAVE_AMPLITUDE.defaultValue));
        Config.COLOR_VILLAGER = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_VILLAGER.label, ConfigOperation.PROP_COLOR_VILLAGER.defaultValue));
        Config.COLOR_HOSTILE = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_HOSTILE.label, ConfigOperation.PROP_COLOR_HOSTILE.defaultValue));
        Config.COLOR_WARDEN = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_WARDEN.label, ConfigOperation.PROP_COLOR_WARDEN.defaultValue));
        Config.COLOR_IRON_GOLEM = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_IRON_GOLEM.label, ConfigOperation.PROP_COLOR_IRON_GOLEM.defaultValue));
        Config.COLOR_PASSIVE = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_PASSIVE.label, ConfigOperation.PROP_COLOR_PASSIVE.defaultValue));
        Config.COLOR_AMBIENT = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_AMBIENT.label, ConfigOperation.PROP_COLOR_AMBIENT.defaultValue));
        Config.COLOR_PLAYER = ColorData.Color.valueOf(properties.getProperty(ConfigOperation.PROP_COLOR_PLAYER.label, ConfigOperation.PROP_COLOR_PLAYER.defaultValue));
        Config.FOV_HORIZONTAL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FOV_HORIZONTAL.label, ConfigOperation.PROP_FOV_HORIZONTAL.defaultValue));
        Config.FOV_HORIZONTAL_ON_ZOOM = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FOV_HORIZONTAL_ON_ZOOM.label, ConfigOperation.PROP_FOV_HORIZONTAL_ON_ZOOM.defaultValue));
        Config.FOV_VERTICAL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FOV_VERTICAL.label, ConfigOperation.PROP_FOV_VERTICAL.defaultValue));
        Config.FOV_VERTICAL_ON_ZOOM = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FOV_VERTICAL_ON_ZOOM.label, ConfigOperation.PROP_FOV_VERTICAL_ON_ZOOM.defaultValue));
        Config.RADAR_HORIZONTAL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_RADAR_HORIZONTAL.label, ConfigOperation.PROP_RADAR_HORIZONTAL.defaultValue));
        Config.RADAR_UPWARD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_RADAR_UPWARD.label, ConfigOperation.PROP_RADAR_UPWARD.defaultValue));
        Config.RADAR_DOWNWARD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_RADAR_DOWNWARD.label, ConfigOperation.PROP_RADAR_DOWNWARD.defaultValue));
        Config.RADAR_UPDATE_INTERVAL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_RADAR_UPDATE_INTERVAL.label, ConfigOperation.PROP_RADAR_UPDATE_INTERVAL.defaultValue));
        Config.TARGET_SEARCH_MODE = Boolean.parseBoolean(properties.getProperty(PROP_TARGET_SEARCH_MODE.label(), PROP_TARGET_SEARCH_MODE.defaultValue()));
        Config.FOV_SUPPRESSION = Boolean.parseBoolean(properties.getProperty(PROP_FOV_SUPPRESSION.label(), PROP_FOV_SUPPRESSION.defaultValue()));
        Config.FLAP_SUPPRESSION = Boolean.parseBoolean(properties.getProperty(PROP_FLAP_SUPPRESSION.label(), PROP_FLAP_SUPPRESSION.defaultValue()));
        Config.FLAP_SUPPRESSION_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FLAP_SUPPRESSION_THRESHOLD.label, ConfigOperation.PROP_FLAP_SUPPRESSION_THRESHOLD.defaultValue));
        Config.FLAP_SUPPRESSION_TICK = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FLAP_SUPPRESSION_TICK.label, ConfigOperation.PROP_FLAP_SUPPRESSION_TICK.defaultValue));
        Config.ZOOM_VIEW = Config.ReconView.valueOf(properties.getProperty(ConfigOperation.PROP_ZOOM_VIEW.label, ConfigOperation.PROP_ZOOM_VIEW.defaultValue));
        Config.ZOOM_STEP = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ZOOM_STEP.label, ConfigOperation.PROP_ZOOM_STEP.defaultValue));
        Config.CAMERA_RETURN_BEHAVIOR = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_CAMERA_RETURN_BEHAVIOR.label, ConfigOperation.PROP_CAMERA_RETURN_BEHAVIOR.defaultValue));
        Config.PERSPECTIVE_BACK_CROSSHAIR = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PERSPECTIVE_BACK_CROSSHAIR.label, ConfigOperation.PROP_PERSPECTIVE_BACK_CROSSHAIR.defaultValue));
        Config.EXTREME = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_EXTREME.label, ConfigOperation.PROP_EXTREME.defaultValue));
        String friendsStr = properties.getProperty(ConfigOperation.PROP_FRIENDS.label, ConfigOperation.PROP_FRIENDS.defaultValue);
        Config.FRIENDS = new ArrayList<>();
        if (friendsStr != null && !friendsStr.isBlank()) {
            String[] parts = friendsStr.split(";");
            for (String part : parts) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    Config.FRIENDS.add(trimmed);
                }
            }
        }
        com.papack.maceattackassistance.client.FriendManager.setFriends(Config.FRIENDS);
    }

    public static void saveFile() {
        Properties properties = new Properties();
        try {
            properties.setProperty(ConfigOperation.PROP_ATTACK_ASSISTANCE.label, String.valueOf(Config.ATTACK_ASSISTANCE));
            properties.setProperty(ConfigOperation.PROP_AIM_ASSIST.label, String.valueOf(Config.AIM_ASSIST));
            properties.setProperty(ConfigOperation.PROP_JUMP_ASSIST.label, String.valueOf(Config.JUMP_ASSIST));
            properties.setProperty(ConfigOperation.PROP_SWING_TOGGLE.label, String.valueOf((Object)Config.SWING_TOGGLE));
            properties.setProperty(ConfigOperation.PROP_SNAPBACK_THRESHOLD.label, String.valueOf(Config.SNAPBACK_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_SNAPBACK_TOLERANCE.label, String.valueOf(Config.SNAPBACK_TOLERANCE));
            properties.setProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT.label, String.valueOf(Config.RETURN_TO_PREV_SLOT));
            properties.setProperty(ConfigOperation.PROP_TARGET_MARKER.label, String.valueOf(Config.TARGET_MARKER));
            properties.setProperty(ConfigOperation.PROP_MACE_PARTICLE.label, String.valueOf((Object)Config.MACE_PARTICLE));
            properties.setProperty(ConfigOperation.PROP_PARTICLE_TRANSITION_THRESHOLD.label, String.valueOf(Config.PARTICLE_TRANSITION_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_PARTICLE_ORDER.label, String.valueOf((Object)Config.PARTICLE_ORDER));
            properties.setProperty(ConfigOperation.PROP_HOT_SWAP.label, String.valueOf(Config.HOT_SWAP));
            properties.setProperty(ConfigOperation.PROP_HEIGHT_THRESHOLD.label, String.valueOf(Config.HEIGHT_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_WEAPON_SWING.label, String.valueOf(Config.WEAPON_SWING));
            properties.setProperty(ConfigOperation.PROP_HIDE_MARKER.label, String.valueOf(Config.HIDE_MARKER));
            properties.setProperty(ConfigOperation.PROP_MARKER_OFFSET.label, String.valueOf(Config.MARKER_OFFSET));
            properties.setProperty(ConfigOperation.PROP_MARKER_TYPE.label, String.valueOf((Object)Config.MARKER_TYPE));
            properties.setProperty(ConfigOperation.PROP_STUN_SLAMMING.label, String.valueOf(Config.STUN_SLAMMING));
            properties.setProperty(ConfigOperation.PROP_BREACH_SWAP.label, String.valueOf(Config.BREACH_SWAP));
            properties.setProperty(ConfigOperation.PROP_BREACH_LIMITED.label, String.valueOf(Config.BREACH_LIMITED));
            properties.setProperty(ConfigOperation.PROP_BREACH_ON_GROUND.label, String.valueOf(Config.BREACH_ON_GROUND));
            properties.setProperty(ConfigOperation.PROP_ROCKET_BLITZ.label, String.valueOf(Config.ROCKET_BLITZ));
            properties.setProperty(ConfigOperation.PROP_ROCKET_BLITZ_SLOT.label, String.valueOf(Config.ROCKET_BLITZ_SLOT));
            properties.setProperty(ConfigOperation.PROP_AUTO_REFILL.label, String.valueOf(Config.AUTO_REFILL));
            properties.setProperty(ConfigOperation.PROP_ROCKET_TRIGGER.label, String.valueOf((Object)Config.ROCKET_TRIGGER));
            properties.setProperty(ConfigOperation.PROP_AXE_SLOT.label, String.valueOf(Config.AXE_SLOT));
            properties.setProperty(ConfigOperation.PROP_MACE_PRIMARY.label, String.valueOf(Config.MACE_PRIMARY));
            properties.setProperty(ConfigOperation.PROP_MACE_BREACH.label, String.valueOf(Config.MACE_BREACH));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_VILLAGER.label, String.valueOf(Config.ALLOWED_VILLAGER));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_IRON_GOLEM.label, String.valueOf(Config.ALLOWED_IRON_GOLEM));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_HOSTILE.label, String.valueOf(Config.ALLOWED_HOSTILE));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_PASSIVE.label, String.valueOf(Config.ALLOWED_PASSIVE));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_AMBIENT.label, String.valueOf(Config.ALLOWED_AMBIENT));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_PLAYER.label, String.valueOf(Config.ALLOWED_PLAYER));
            properties.setProperty(ConfigOperation.PROP_MAX_SPEED_YAW.label, String.valueOf(Config.MAX_SPEED_YAW));
            properties.setProperty(ConfigOperation.PROP_AIM_MODE.label, String.valueOf((Object)Config.AIM_MODE));
            properties.setProperty(ConfigOperation.PROP_AUTO_MODE_THRESHOLD.label, String.valueOf(Config.AUTO_MODE_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_DISPLAY_ACTION_BAR.label, String.valueOf(Config.DISPLAY_ACTION_BAR));
            properties.setProperty(ConfigOperation.PROP_TOGGLE_SLOT.label, String.valueOf(Config.TOGGLE_SLOT));
            properties.setProperty(ConfigOperation.PROP_JUMP_SPAM.label, String.valueOf(Config.JUMP_SPAM));
            properties.setProperty(ConfigOperation.PROP_JUMP_SPAM_TICK.label, String.valueOf(Config.JUMP_SPAM_TICK));
            properties.setProperty(ConfigOperation.PROP_WALL_CLIMBING.label, String.valueOf(Config.WALL_CLIMBING));
            properties.setProperty(ConfigOperation.PROP_SNAPBACK.label, String.valueOf(Config.SNAPBACK));
            properties.setProperty(ConfigOperation.PROP_ATTACK_RANGE.label, String.valueOf(Config.ATTACK_RANGE));
            properties.setProperty(ConfigOperation.PROP_ATTACK_RANGE_DIFF.label, String.valueOf(Config.ATTACK_RANGE_DIFF));
            properties.setProperty(ConfigOperation.PROP_REFLECTION_ANGLE.label, String.valueOf(Config.REFLECTION_ANGLE));
            properties.setProperty(ConfigOperation.PROP_COOL_DOWN_TICKS.label, String.valueOf(Config.COOL_DOWN_TICKS));
            properties.setProperty(ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE.label, String.valueOf(Config.PRIORITIZE_WIND_CHARGE));
            properties.setProperty(ConfigOperation.PROP_PRIORITIZE_ROCKET.label, String.valueOf(Config.PRIORITIZE_ROCKET));
            properties.setProperty(ConfigOperation.PROP_MANUAL_CAMERA_PITCH.label, String.valueOf(Config.MANUAL_CAMERA_PITCH));
            properties.setProperty(ConfigOperation.PROP_SPIRAL_COUNT.label, String.valueOf(Config.SP_SPIRAL_COUNT));
            properties.setProperty(ConfigOperation.PROP_SPAIRAL_LENGTH.label, String.valueOf(Config.SP_SPIRAL_LENGTH));
            properties.setProperty(ConfigOperation.PROP_SPIRAL_ALPHA.label, String.valueOf(Config.SP_SPIRAL_ALPHA));
            properties.setProperty(ConfigOperation.PROP_SPIRAL_GAMMA.label, String.valueOf(Config.SP_SPIRAL_GAMMA));
            properties.setProperty(ConfigOperation.PROP_COILS.label, String.valueOf(Config.SP_COILS));
            properties.setProperty(ConfigOperation.PROP_HEIGHT.label, String.valueOf(Config.SP_HEIGHT));
            properties.setProperty(ConfigOperation.PROP_SIZE.label, String.valueOf(Config.SP_SIZE));
            properties.setProperty(ConfigOperation.PROP_SPEED.label, String.valueOf(Config.SP_SPEED));
            properties.setProperty(ConfigOperation.PROP_BASE_RADIUS.label, String.valueOf(Config.SP_BASE_RADIUS));
            properties.setProperty(ConfigOperation.PROP_WAVE_SPEED.label, String.valueOf(Config.SP_WAVE_SPEED));
            properties.setProperty(ConfigOperation.PROP_WAVE_AMPLITUDE.label, String.valueOf(Config.SP_WAVE_AMPLITUDE));
            properties.setProperty(ConfigOperation.PROP_COLOR_VILLAGER.label, String.valueOf((Object)Config.COLOR_VILLAGER));
            properties.setProperty(ConfigOperation.PROP_COLOR_HOSTILE.label, String.valueOf((Object)Config.COLOR_HOSTILE));
            properties.setProperty(ConfigOperation.PROP_COLOR_WARDEN.label, String.valueOf((Object)Config.COLOR_WARDEN));
            properties.setProperty(ConfigOperation.PROP_COLOR_IRON_GOLEM.label, String.valueOf((Object)Config.COLOR_IRON_GOLEM));
            properties.setProperty(ConfigOperation.PROP_COLOR_PASSIVE.label, String.valueOf((Object)Config.COLOR_PASSIVE));
            properties.setProperty(ConfigOperation.PROP_COLOR_AMBIENT.label, String.valueOf((Object)Config.COLOR_AMBIENT));
            properties.setProperty(ConfigOperation.PROP_COLOR_PLAYER.label, String.valueOf((Object)Config.COLOR_PLAYER));
            properties.setProperty(ConfigOperation.PROP_FOV_HORIZONTAL.label, String.valueOf(Config.FOV_HORIZONTAL));
            properties.setProperty(ConfigOperation.PROP_FOV_HORIZONTAL_ON_ZOOM.label, String.valueOf(Config.FOV_HORIZONTAL_ON_ZOOM));
            properties.setProperty(ConfigOperation.PROP_FOV_VERTICAL.label, String.valueOf(Config.FOV_VERTICAL));
            properties.setProperty(ConfigOperation.PROP_FOV_VERTICAL_ON_ZOOM.label, String.valueOf(Config.FOV_VERTICAL_ON_ZOOM));
            properties.setProperty(ConfigOperation.PROP_RADAR_HORIZONTAL.label, String.valueOf(Config.RADAR_HORIZONTAL));
            properties.setProperty(ConfigOperation.PROP_RADAR_UPWARD.label, String.valueOf(Config.RADAR_UPWARD));
            properties.setProperty(ConfigOperation.PROP_RADAR_DOWNWARD.label, String.valueOf(Config.RADAR_DOWNWARD));
            properties.setProperty(ConfigOperation.PROP_RADAR_UPDATE_INTERVAL.label, String.valueOf(Config.RADAR_UPDATE_INTERVAL));
            properties.setProperty(ConfigOperation.PROP_TARGET_SEARCH_MODE.label, String.valueOf(Config.TARGET_SEARCH_MODE));
            properties.setProperty(ConfigOperation.PROP_FOV_SUPPRESSION.label, String.valueOf(Config.FOV_SUPPRESSION));
            properties.setProperty(ConfigOperation.PROP_FLAP_SUPPRESSION.label, String.valueOf(Config.FLAP_SUPPRESSION));
            properties.setProperty(ConfigOperation.PROP_FLAP_SUPPRESSION_THRESHOLD.label, String.valueOf(Config.FLAP_SUPPRESSION_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_FLAP_SUPPRESSION_TICK.label, String.valueOf(Config.FLAP_SUPPRESSION_TICK));
            properties.setProperty(ConfigOperation.PROP_ZOOM_VIEW.label, String.valueOf((Object)Config.ZOOM_VIEW));
            properties.setProperty(ConfigOperation.PROP_ZOOM_STEP.label, String.valueOf(Config.ZOOM_STEP));
            properties.setProperty(ConfigOperation.PROP_CAMERA_RETURN_BEHAVIOR.label, String.valueOf(Config.CAMERA_RETURN_BEHAVIOR));
            properties.setProperty(ConfigOperation.PROP_PERSPECTIVE_BACK_CROSSHAIR.label, String.valueOf(Config.PERSPECTIVE_BACK_CROSSHAIR));
            properties.setProperty(ConfigOperation.PROP_EXTREME.label, String.valueOf(Config.EXTREME));
            StringBuilder friendsBuilder = new StringBuilder();
            for (int i = 0; i < Config.FRIENDS.size(); ++i) {
                if (i > 0) {
                    friendsBuilder.append(";");
                }
                friendsBuilder.append(Config.FRIENDS.get(i));
            }
            properties.setProperty(ConfigOperation.PROP_FRIENDS.label, friendsBuilder.toString());
            for (int i = 0; i < 4; ++i) {
                properties.setProperty(ConfigOperation.PROP_ATTACK_DISTANCE[i].label, String.valueOf(Config.ATTACK_DISTANCE[i]));
                properties.setProperty(ConfigOperation.PROP_FALL_VELOCITY[i].label, String.valueOf(Config.FALL_VELOCITY[i]));
            }
            properties.store(new FileOutputStream(String.valueOf(Config.MAA_CONFIG)), "");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record ConfigData(String label, String defaultValue, Type type) {
    }

    public static enum Type {
        B,
        E,
        I;

    }
}
