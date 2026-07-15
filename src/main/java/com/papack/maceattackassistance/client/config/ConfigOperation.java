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
    public static final ConfigData PROP_HEIGHT_THRESHOLD = new ConfigData("HeightThreshold", "2", Type.I);
    public static final ConfigData PROP_SPEAR_ASSIST = new ConfigData("SpearAssist", "true", Type.B);
    public static final ConfigData PROP_SPEAR_SWAP = new ConfigData("SpearSwap", "true", Type.B);
    public static final ConfigData PROP_SPEAR_SLAM = new ConfigData("SpearSlam", "true", Type.B);
    public static final ConfigData PROP_SPEAR_SLAM_MIN_FALL = new ConfigData("SpearSlamMinFall", "2", Type.I);
    public static final ConfigData PROP_SPEAR_SLAM_COOLDOWN = new ConfigData("SpearSlamCooldown", "10", Type.I);
    public static final ConfigData PROP_SPEAR_SLAM_DAMAGE_MULT = new ConfigData("SpearSlamDamageMult", "100", Type.I);
    public static final ConfigData PROP_SPEAR_SLAM_PARTICLES = new ConfigData("SpearSlamParticles", "true", Type.B);
    public static final ConfigData PROP_SPEAR_SLAM_SOUND = new ConfigData("SpearSlamSound", "true", Type.B);
    public static final ConfigData PROP_WEAPON_SWING = new ConfigData("WeaponSwing", "true", Type.B);
    public static final ConfigData PROP_SWING_TOGGLE = new ConfigData("SwingToggle", "L_SHIFT", Type.E);
    public static final ConfigData PROP_HOT_SWAP = new ConfigData("HotSwap", "true", Type.B);
    public static final ConfigData PROP_STUN_SLAMMING = new ConfigData("StunSlam", "true", Type.B);
    public static final ConfigData PROP_BREACH_SWAP = new ConfigData("BreachSwap", "true", Type.B);
    public static final ConfigData PROP_BREACH_LIMITED = new ConfigData("BreachLimited", "true", Type.B);
    public static final ConfigData PROP_BREACH_ON_GROUND = new ConfigData("BreachOnGround", "true", Type.B);
    public static final ConfigData PROP_BEHAVIOR_NOT_WEARING_ARMOR = new ConfigData("NonWearingBehavior", "Off", Type.E);
    public static final ConfigData PROP_SNAPBACK = new ConfigData("Snapback", "true", Type.B);
    public static final ConfigData PROP_SNAPBACK_THRESHOLD = new ConfigData("SnapbackThreshold", "6", Type.I);
    public static final ConfigData PROP_SNAPBACK_TOLERANCE = new ConfigData("SnapbackTolerance", "500", Type.I);
    public static final ConfigData PROP_DOUBLE_TAP = new ConfigData("DoubleTap", "true", Type.B);
    public static final ConfigData PROP_DOUBLE_TAP_REWEAR = new ConfigData("DoubleTapReWear", "true", Type.B);
    public static final ConfigData PROP_DOUBLE_TAP_REWEAR_DELAY = new ConfigData("DoubleTapReWearDelay", "1", Type.I);
    public static final ConfigData PROP_PARALLEL_SEARCH = new ConfigData("ParallelSearch", "false", Type.B);
    public static final ConfigData PROP_SWORD_SWAP_OR_BREACH_SWAP = new ConfigData("SwordOrBreachSwap", "BreachSwap", Type.E);
    public static final ConfigData PROP_SWORD_OR_AXE = new ConfigData("SwordOrAxe", "true", Type.B);
    public static final ConfigData PROP_AXE_SLOT = new ConfigData("AxeSlot", "-1", Type.I);
    public static final ConfigData PROP_MACE_PRIMARY = new ConfigData("MacePrimary", "-1", Type.I);
    public static final ConfigData PROP_MACE_BREACH = new ConfigData("MaceBreach", "-1", Type.I);
    public static final ConfigData PROP_STUN_HIGH = new ConfigData("StunSlamHigh", "5", Type.I);
    public static final ConfigData PROP_STUN_LOW = new ConfigData("StunSlamLow", "5", Type.I);
    public static final ConfigData PROP_RETURN_TO_PREV_SLOT_MODE = new ConfigData("ReturnToPrevSlotMode", "-1", Type.I);
    public static final ConfigData PROP_RETURN_TO_PREV_SLOT = new ConfigData("ReturnToPrevSlot", "true", Type.B);
    public static final ConfigData PROP_RETURN_TO_PREV_SLOT_BREACH = new ConfigData("ReturnToPrevSlotBreach", "true", Type.B);
    public static final ConfigData PROP_AUTO_WIND_CHARGE_SELECT = new ConfigData("AutoWindChargeSelect", "true", Type.B);
    public static final ConfigData PROP_SHIELD_DRAINING = new ConfigData("ShieldDraining", "true", Type.B);
    public static final ConfigData PROP_AUTO_ELYTRA = new ConfigData("AutoElytraSwap", "false", Type.B);
    public static final ConfigData PROP_AUTO_ELYTRA_TICK_AHEAD = new ConfigData("AutoElytraSwapTickAhead", "58", Type.I);
    public static final ConfigData PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL = new ConfigData("AutoElytraSwapTickAheadNormal", "40", Type.I);
    public static final ConfigData PROP_AUTO_ELYTRA_DISTANCE = new ConfigData("AutoElytraSwapDistance", "45", Type.I);
    public static final ConfigData PROP_AUTO_ELYTRA_DISTANCE_NORMAL = new ConfigData("AutoElytraSwapDistanceNormal", "40", Type.I);
    public static final ConfigData PROP_FRIEND_PROTECTION = new ConfigData("FriendProtection", "false", Type.B);
    public static final ConfigData PROP_FRIEND_MARK = new ConfigData("FriendMark", "true", Type.B);
    public static final ConfigData PROP_FRIEND_NOT_FOUND = new ConfigData("FriendNotFound", "true", Type.B);
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
    public static final ConfigData PROP_LEGACY_AIM_MODE = new ConfigData("LegacyAimMode", "false", Type.B);
    public static final ConfigData PROP_AIM_OFFSET = new ConfigData("AimOffset", "1", Type.I);
    public static final ConfigData PROP_AIM_FORCED_ADJUSTMENT = new ConfigData("ForcedAdjustment", "false", Type.B);
    public static final ConfigData PROP_AIM_FALL_THRESHOLD = new ConfigData("AimFallThreshold", "2", Type.I);
    public static final ConfigData PROP_AIM_RAYCAST = new ConfigData("AimRaycast", "false", Type.B);
    public static final ConfigData PROP_AIM_RAYCAST_RANGE = new ConfigData("AimRaycastRange", "30", Type.I);
    public static final ConfigData PROP_AIM_RAYCAST_RADIUS = new ConfigData("AimRaycastRadius", "3", Type.I);
    public static final ConfigData PROP_JUMP_ASSIST = new ConfigData("JumpAssist", "true", Type.B);
    public static final ConfigData PROP_WALL_CLIMBING = new ConfigData("WallJump", "true", Type.B);
    public static final ConfigData PROP_ALSO_SEARCH_INVENTORY = new ConfigData("AlsoSearchInventory", "false", Type.B);
    public static final ConfigData PROP_TOGGLE_SLOT = new ConfigData("ToggleSlot", "0", Type.I);
    public static final ConfigData PROP_ROCKET_BLITZ = new ConfigData("RocketBlitz", "true", Type.B);
    public static final ConfigData PROP_ROCKET_BLITZ_SLOT = new ConfigData("RocketBlitzSlot", "0", Type.I);
    public static final ConfigData PROP_ROCKET_TRIGGER = new ConfigData("RocketTrigger", "SPACE", Type.E);
    public static final ConfigData PROP_JUMP_SPAM = new ConfigData("JumpSpam", "true", Type.B);
    public static final ConfigData PROP_JUMP_SPAM_TICK = new ConfigData("JumpSpamTick", "2", Type.I);
    public static final ConfigData PROP_JUMP_SPAM_THRESHOLD = new ConfigData("JumpSpamThreshold", "3", Type.I);
    public static final ConfigData PROP_ELYTRA_BOOST = new ConfigData("ElytraBoost", "true", Type.B);
    public static final ConfigData PROP_REFLECTION_ANGLE = new ConfigData("ReflectionAngle", "-35", Type.I);
    public static final ConfigData PROP_IN_USE_ELYTRA_JUMP_MODE = new ConfigData("InUseElytraJumpMode", "High", Type.E);
    public static final ConfigData PROP_NOT_USE_ELYTRA_JUMP_MODE = new ConfigData("NotInUseElytraJumpMode", "High", Type.E);
    public static final ConfigData PROP_TOGGLE_JUMP_MODE = new ConfigData("ToggleJumpMode", "false", Type.B);
    public static final ConfigData PROP_ELYTRA_MANUAL_MODE = new ConfigData("ElytraManualMode", "false", Type.B);
    public static final ConfigData PROP_PRIORITIZE_WIND_CHARGE = new ConfigData("PrioritizeWindCharge", "true", Type.B);
    public static final ConfigData PROP_PRIORITIZE_ROCKET = new ConfigData("PrioritizeRocket", "true", Type.B);
    public static final ConfigData PROP_AUTO_REFILL = new ConfigData("AutoRefill", "true", Type.B);
    public static final ConfigData PROP_TARGET_MARKER = new ConfigData("TargetMarker", "true", Type.B);
    public static final ConfigData PROP_HIDE_MARKER = new ConfigData("HideMarker", "true", Type.B);
    public static final ConfigData PROP_MARKER_OFFSET = new ConfigData("MarkerOffset", "1", Type.I);
    public static final ConfigData PROP_MACE_PARTICLE = new ConfigData("MaceParticle", "Transition", Type.E);
    public static final ConfigData PROP_PARTICLE_TRANSITION_THRESHOLD = new ConfigData("Threshold", "10", Type.I);
    public static final ConfigData PROP_PARTICLE_ORDER = new ConfigData("ParticleOrder", "Blue_Red", Type.E);
    public static final ConfigData PROP_MARKER_TYPE = new ConfigData("MarkerType", "Spiral", Type.E);
    public static final ConfigData PROP_CROSSHAIR_MODE = new ConfigData("CrosshairMode", "Elytra_Equipped", Type.E);
    public static final ConfigData PROP_ZOOM_VIEW = new ConfigData("ZoomView", "THIRD_PERSON_BACK", Type.E);
    public static final ConfigData PROP_ZOOM_STEP = new ConfigData("ZoomInOutStep", "4", Type.I);
    public static final ConfigData PROP_CAMERA_RETURN_BEHAVIOR = new ConfigData("CameraReturnBehavior", "true", Type.B);
    public static final ConfigData PROP_PERSPECTIVE_BACK_CROSSHAIR = new ConfigData("PerspectiveBackCrosshair", "true", Type.B);
    public static final ConfigData PROP_RECON_QUICK_ZOOM = new ConfigData("ReconQuickZoom", "true", Type.B);
    public static final ConfigData PROP_RECON_QUICK_ATTACK = new ConfigData("ReconQuickAttack", "true", Type.B);
    public static final ConfigData PROP_DISPLAY_ACTION_BAR = new ConfigData("DisplayActionBar", "ActionBar", Type.E);
    public static final ConfigData PROP_EXTREME = new ConfigData("Extreme", "true", Type.B);
    public static final ConfigData PROP_DEBUG_LANDING_POS = new ConfigData("DebugLandingPos", "false", Type.B);
    public static final ConfigData PROP_THROW_WIND_CHARGE = new ConfigData("ThrowWindCharge", "false", Type.B);
    public static final ConfigData PROP_PEARL_CATCH = new ConfigData("PearlCatch", "true", Type.B);
    public static final ConfigData PROP_PEARL_CATCH_DELAY = new ConfigData("PearlCatchDelay", "3", Type.I);
    public static final ConfigData PROP_PEARL_CATCH_RANGE = new ConfigData("PearlCatchRange", "8", Type.I);
    public static final ConfigData PROP_MANUAL_CAMERA_PITCH = new ConfigData("ManualCameraPitch", "30", Type.I);
    public static final ConfigData PROP_MANUAL_INCIDENCE_TICK = new ConfigData("ManualIncidenceTick", "0", Type.I);
    public static final ConfigData PROP_SNAPBACK_Y_OFFSET = new ConfigData("SnapbackYoffset", "-4", Type.I);
    public static final ConfigData[] PROP_ATTACK_DISTANCE = new ConfigData[]{new ConfigData("AttackDistance0", "80", Type.I), new ConfigData("AttackDistance1", "120", Type.I), new ConfigData("AttackDistance2", "160", Type.I), new ConfigData("AttackDistance3", "200", Type.I)};
    public static final ConfigData[] PROP_FALL_VELOCITY = new ConfigData[]{new ConfigData("FallVelocity0", "20", Type.I), new ConfigData("FallVelocity1", "1", Type.I), new ConfigData("FallVelocity2", "43", Type.I), new ConfigData("FallVelocity3", "52", Type.I)};
    public static final ConfigData PROP_ATTACK_RANGE = new ConfigData("AttackRang", "281", Type.I);
    public static final ConfigData PROP_ATTACK_MODE = new ConfigData("AttackMode", "true", Type.B);
    public static final ConfigData PROP_COOL_DOWN_TICKS = new ConfigData("CoolDownTicks", "10", Type.I);
    public static final ConfigData PROP_SPIRAL_COUNT = new ConfigData("sp_SpiralCount", "3", Type.I);
    public static final ConfigData PROP_SPIRAL_LENGTH = new ConfigData("sp_SpiralLength", "30", Type.I);
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
    public static final ConfigData PROP_DEFAULT_EXPANDED = new ConfigData("DefaultExpanded", "true", Type.B);
    public static final ConfigData PROP_DEFAULT_EXPANDED_GLOBAL = new ConfigData("DefaultExpandedGLOBAL", "false", Type.B);
    private static final List<ConfigData> configList = new ArrayList<ConfigData>(Arrays.asList(PROP_AIM_ASSIST, PROP_AIM_FALL_THRESHOLD, PROP_AIM_FORCED_ADJUSTMENT, PROP_AIM_MODE, PROP_AIM_OFFSET, PROP_AIM_RAYCAST, PROP_AIM_RAYCAST_RADIUS, PROP_AIM_RAYCAST_RANGE, PROP_ALLOWED_AMBIENT, PROP_ALLOWED_HOSTILE, PROP_ALLOWED_IRON_GOLEM, PROP_ALLOWED_PASSIVE, PROP_ALLOWED_PLAYER, PROP_ALLOWED_VILLAGER, PROP_ALSO_SEARCH_INVENTORY, PROP_ATTACK_ASSISTANCE, PROP_ATTACK_DISTANCE[0], PROP_ATTACK_DISTANCE[1], PROP_ATTACK_DISTANCE[2], PROP_ATTACK_DISTANCE[3], PROP_ATTACK_MODE, PROP_ATTACK_RANGE, PROP_AUTO_ELYTRA, PROP_AUTO_ELYTRA_DISTANCE, PROP_AUTO_ELYTRA_DISTANCE_NORMAL, PROP_AUTO_ELYTRA_TICK_AHEAD, PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL, PROP_AUTO_MODE_THRESHOLD, PROP_AUTO_REFILL, PROP_AUTO_WIND_CHARGE_SELECT, PROP_AXE_SLOT, PROP_BASE_RADIUS, PROP_BEHAVIOR_NOT_WEARING_ARMOR, PROP_BREACH_LIMITED, PROP_BREACH_ON_GROUND, PROP_BREACH_SWAP, PROP_CAMERA_RETURN_BEHAVIOR, PROP_COILS, PROP_COLOR_AMBIENT, PROP_COLOR_HOSTILE, PROP_COLOR_IRON_GOLEM, PROP_COLOR_PASSIVE, PROP_COLOR_PLAYER, PROP_COLOR_VILLAGER, PROP_COLOR_WARDEN, PROP_COOL_DOWN_TICKS, PROP_CROSSHAIR_MODE, PROP_DEBUG_LANDING_POS, PROP_DEFAULT_EXPANDED, PROP_DEFAULT_EXPANDED_GLOBAL, PROP_DISPLAY_ACTION_BAR, PROP_DOUBLE_TAP, PROP_DOUBLE_TAP_REWEAR, PROP_DOUBLE_TAP_REWEAR_DELAY, PROP_ELYTRA_BOOST, PROP_ELYTRA_MANUAL_MODE, PROP_EXTREME, PROP_FALL_VELOCITY[0], PROP_FALL_VELOCITY[1], PROP_FALL_VELOCITY[2], PROP_FALL_VELOCITY[3], PROP_FLAP_SUPPRESSION, PROP_FLAP_SUPPRESSION_THRESHOLD, PROP_FLAP_SUPPRESSION_TICK, PROP_FOV_HORIZONTAL, PROP_FOV_HORIZONTAL_ON_ZOOM, PROP_FOV_SUPPRESSION, PROP_FOV_VERTICAL, PROP_FOV_VERTICAL_ON_ZOOM, PROP_FRIEND_MARK, PROP_FRIEND_NOT_FOUND, PROP_FRIEND_PROTECTION, PROP_HEIGHT, PROP_HEIGHT_THRESHOLD, PROP_HIDE_MARKER, PROP_HOT_SWAP, PROP_IN_USE_ELYTRA_JUMP_MODE, PROP_JUMP_ASSIST, PROP_JUMP_SPAM, PROP_JUMP_SPAM_THRESHOLD, PROP_JUMP_SPAM_TICK, PROP_LEGACY_AIM_MODE, PROP_MACE_BREACH, PROP_MACE_PARTICLE, PROP_MACE_PRIMARY, PROP_MANUAL_CAMERA_PITCH, PROP_MANUAL_INCIDENCE_TICK, PROP_MARKER_OFFSET, PROP_MARKER_TYPE, PROP_MAX_SPEED_YAW, PROP_NOT_USE_ELYTRA_JUMP_MODE, PROP_PARALLEL_SEARCH, PROP_PARTICLE_ORDER, PROP_PARTICLE_TRANSITION_THRESHOLD, PROP_PEARL_CATCH, PROP_PEARL_CATCH_DELAY, PROP_PEARL_CATCH_RANGE, PROP_PERSPECTIVE_BACK_CROSSHAIR, PROP_PRIORITIZE_ROCKET, PROP_PRIORITIZE_WIND_CHARGE, PROP_RADAR_DOWNWARD, PROP_RADAR_HORIZONTAL, PROP_RADAR_UPDATE_INTERVAL, PROP_RADAR_UPWARD, PROP_RECON_QUICK_ATTACK, PROP_RECON_QUICK_ZOOM, PROP_REFLECTION_ANGLE, PROP_RETURN_TO_PREV_SLOT, PROP_RETURN_TO_PREV_SLOT_BREACH, PROP_RETURN_TO_PREV_SLOT_MODE, PROP_ROCKET_BLITZ, PROP_ROCKET_BLITZ_SLOT, PROP_ROCKET_TRIGGER, PROP_SHIELD_DRAINING, PROP_SIZE, PROP_SNAPBACK, PROP_SNAPBACK_THRESHOLD, PROP_SNAPBACK_TOLERANCE, PROP_SNAPBACK_Y_OFFSET, PROP_SPEAR_ASSIST, PROP_SPEAR_SWAP, PROP_SPEAR_SLAM, PROP_SPEAR_SLAM_MIN_FALL, PROP_SPEAR_SLAM_COOLDOWN, PROP_SPEAR_SLAM_DAMAGE_MULT, PROP_SPEAR_SLAM_PARTICLES, PROP_SPEAR_SLAM_SOUND, PROP_SPEED, PROP_SPIRAL_ALPHA, PROP_SPIRAL_COUNT, PROP_SPIRAL_GAMMA, PROP_SPIRAL_LENGTH, PROP_STUN_HIGH, PROP_STUN_LOW, PROP_STUN_SLAMMING, PROP_SWING_TOGGLE, PROP_SWORD_OR_AXE, PROP_SWORD_SWAP_OR_BREACH_SWAP, PROP_TARGET_MARKER, PROP_TARGET_SEARCH_MODE, PROP_THROW_WIND_CHARGE, PROP_TOGGLE_JUMP_MODE, PROP_TOGGLE_SLOT, PROP_WALL_CLIMBING, PROP_WAVE_AMPLITUDE, PROP_WAVE_SPEED, PROP_WEAPON_SWING, PROP_ZOOM_STEP, PROP_ZOOM_VIEW));

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
        Config.SPEAR_ASSIST = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SPEAR_ASSIST.label, ConfigOperation.PROP_SPEAR_ASSIST.defaultValue));
        Config.SPEAR_SWAP = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SPEAR_SWAP.label, ConfigOperation.PROP_SPEAR_SWAP.defaultValue));
        Config.SPEAR_SLAM = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SPEAR_SLAM.label, ConfigOperation.PROP_SPEAR_SLAM.defaultValue));
        Config.SPEAR_SLAM_MIN_FALL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPEAR_SLAM_MIN_FALL.label, ConfigOperation.PROP_SPEAR_SLAM_MIN_FALL.defaultValue));
        Config.SPEAR_SLAM_COOLDOWN = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPEAR_SLAM_COOLDOWN.label, ConfigOperation.PROP_SPEAR_SLAM_COOLDOWN.defaultValue));
        Config.SPEAR_SLAM_DAMAGE_MULT_INT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPEAR_SLAM_DAMAGE_MULT.label, ConfigOperation.PROP_SPEAR_SLAM_DAMAGE_MULT.defaultValue));
        Config.SPEAR_SLAM_PARTICLES = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SPEAR_SLAM_PARTICLES.label, ConfigOperation.PROP_SPEAR_SLAM_PARTICLES.defaultValue));
        Config.SPEAR_SLAM_SOUND = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SPEAR_SLAM_SOUND.label, ConfigOperation.PROP_SPEAR_SLAM_SOUND.defaultValue));
        Config.WEAPON_SWING = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_WEAPON_SWING.label, ConfigOperation.PROP_WEAPON_SWING.defaultValue));
        Config.SWING_TOGGLE = Config.SwingToggle.valueOf(properties.getProperty(ConfigOperation.PROP_SWING_TOGGLE.label, ConfigOperation.PROP_SWING_TOGGLE.defaultValue));
        Config.SNAPBACK_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SNAPBACK_THRESHOLD.label, ConfigOperation.PROP_SNAPBACK_THRESHOLD.defaultValue));
        Config.SNAPBACK_TOLERANCE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SNAPBACK_TOLERANCE.label, ConfigOperation.PROP_SNAPBACK_TOLERANCE.defaultValue));
        Config.RETURN_TO_PREV_SLOT = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT.label, ConfigOperation.PROP_RETURN_TO_PREV_SLOT.defaultValue));
        Config.DOUBLE_TAP = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_DOUBLE_TAP.label, ConfigOperation.PROP_DOUBLE_TAP.defaultValue));
        Config.PARALLEL_SEARCH = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PARALLEL_SEARCH.label, ConfigOperation.PROP_PARALLEL_SEARCH.defaultValue));
        Config.DOUBLE_TAP_REWEAR = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_DOUBLE_TAP_REWEAR.label, ConfigOperation.PROP_DOUBLE_TAP_REWEAR.defaultValue));
        Config.DOUBLE_TAP_REWEAR_DELAY = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_DOUBLE_TAP_REWEAR_DELAY.label, ConfigOperation.PROP_DOUBLE_TAP_REWEAR_DELAY.defaultValue));
        Config.FRIEND_PROTECTION = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_FRIEND_PROTECTION.label, ConfigOperation.PROP_FRIEND_PROTECTION.defaultValue));
        Config.FRIEND_MARK = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_FRIEND_MARK.label, ConfigOperation.PROP_FRIEND_MARK.defaultValue));
        Config.FRIEND_NOT_FOUND = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_FRIEND_NOT_FOUND.label, ConfigOperation.PROP_FRIEND_NOT_FOUND.defaultValue));
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
        Config.BEHAVIOR_NOT_WEARING_ARMOR = Config.Behavior.valueOf(properties.getProperty(ConfigOperation.PROP_BEHAVIOR_NOT_WEARING_ARMOR.label, ConfigOperation.PROP_BEHAVIOR_NOT_WEARING_ARMOR.defaultValue));
        Config.ROCKET_BLITZ = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ROCKET_BLITZ.label, ConfigOperation.PROP_ROCKET_BLITZ.defaultValue));
        Config.ROCKET_BLITZ_SLOT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ROCKET_BLITZ_SLOT.label, ConfigOperation.PROP_ROCKET_BLITZ_SLOT.defaultValue));
        Config.AUTO_REFILL = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AUTO_REFILL.label, ConfigOperation.PROP_AUTO_REFILL.defaultValue));
        Config.ROCKET_TRIGGER = Config.RocketTrigger.valueOf(properties.getProperty(ConfigOperation.PROP_ROCKET_TRIGGER.label, ConfigOperation.PROP_ROCKET_TRIGGER.defaultValue));
        Config.SWORD_SWAP_OR_BREACH_SWAP = Config.Behavior.valueOf(properties.getProperty(ConfigOperation.PROP_SWORD_SWAP_OR_BREACH_SWAP.label, ConfigOperation.PROP_SWORD_SWAP_OR_BREACH_SWAP.defaultValue));
        Config.SWORD_OR_AXE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SWORD_OR_AXE.label, ConfigOperation.PROP_SWORD_OR_AXE.defaultValue));
        Config.AXE_SLOT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AXE_SLOT.label, ConfigOperation.PROP_AXE_SLOT.defaultValue));
        Config.MACE_PRIMARY = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MACE_PRIMARY.label, ConfigOperation.PROP_MACE_PRIMARY.defaultValue));
        Config.MACE_BREACH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MACE_BREACH.label, ConfigOperation.PROP_MACE_BREACH.defaultValue));
        Config.STUN_HIGH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_STUN_HIGH.label, ConfigOperation.PROP_STUN_HIGH.defaultValue));
        Config.STUN_LOW = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_STUN_LOW.label, ConfigOperation.PROP_STUN_LOW.defaultValue));
        Config.ALLOWED_VILLAGER = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_VILLAGER.label, ConfigOperation.PROP_ALLOWED_VILLAGER.defaultValue));
        Config.ALLOWED_IRON_GOLEM = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_IRON_GOLEM.label, ConfigOperation.PROP_ALLOWED_IRON_GOLEM.defaultValue));
        Config.ALLOWED_HOSTILE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_HOSTILE.label, ConfigOperation.PROP_ALLOWED_HOSTILE.defaultValue));
        Config.ALLOWED_PASSIVE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_PASSIVE.label, ConfigOperation.PROP_ALLOWED_PASSIVE.defaultValue));
        Config.ALLOWED_AMBIENT = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_AMBIENT.label, ConfigOperation.PROP_ALLOWED_AMBIENT.defaultValue));
        Config.ALLOWED_PLAYER = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALLOWED_PLAYER.label, ConfigOperation.PROP_ALLOWED_PLAYER.defaultValue));
        Config.MAX_SPEED_YAW = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MAX_SPEED_YAW.label, ConfigOperation.PROP_MAX_SPEED_YAW.defaultValue));
        Config.AIM_MODE = Config.AimMode.valueOf(properties.getProperty(ConfigOperation.PROP_AIM_MODE.label, ConfigOperation.PROP_AIM_MODE.defaultValue));
        Config.AUTO_MODE_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AUTO_MODE_THRESHOLD.label, ConfigOperation.PROP_AUTO_MODE_THRESHOLD.defaultValue));
        Config.LEGACY_AIM_MODE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_LEGACY_AIM_MODE.label, ConfigOperation.PROP_LEGACY_AIM_MODE.defaultValue));
        Config.AIM_OFFSET = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AIM_OFFSET.label, ConfigOperation.PROP_AIM_OFFSET.defaultValue));
        Config.AIM_FORCED_ADJUSTMENT = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AIM_FORCED_ADJUSTMENT.label, ConfigOperation.PROP_AIM_FORCED_ADJUSTMENT.defaultValue));
        Config.AIM_FALL_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AIM_FALL_THRESHOLD.label, ConfigOperation.PROP_AIM_FALL_THRESHOLD.defaultValue));
        Config.AIM_RAYCAST = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AIM_RAYCAST.label, ConfigOperation.PROP_AIM_RAYCAST.defaultValue));
        Config.AIM_RAYCAST_RANGE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AIM_RAYCAST_RANGE.label, ConfigOperation.PROP_AIM_RAYCAST_RANGE.defaultValue));
        Config.AIM_RAYCAST_RADIUS = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AIM_RAYCAST_RADIUS.label, ConfigOperation.PROP_AIM_RAYCAST_RADIUS.defaultValue));
        Config.AUTO_ELYTRA = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AUTO_ELYTRA.label, ConfigOperation.PROP_AUTO_ELYTRA.defaultValue));
        Config.AUTO_ELYTRA_TICK_AHEAD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD.label, ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD.defaultValue));
        Config.AUTO_ELYTRA_DISTANCE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AUTO_ELYTRA_DISTANCE.label, ConfigOperation.PROP_AUTO_ELYTRA_DISTANCE.defaultValue));
        Config.AUTO_ELYTRA_DISTANCE_NORMAL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AUTO_ELYTRA_DISTANCE_NORMAL.label, ConfigOperation.PROP_AUTO_ELYTRA_DISTANCE_NORMAL.defaultValue));
        Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL.label, ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL.defaultValue));
        Config.DISPLAY_ACTION_BAR = Config.DisplayAt.valueOf(properties.getProperty(ConfigOperation.PROP_DISPLAY_ACTION_BAR.label, ConfigOperation.PROP_DISPLAY_ACTION_BAR.defaultValue));
        Config.TOGGLE_SLOT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_TOGGLE_SLOT.label, ConfigOperation.PROP_TOGGLE_SLOT.defaultValue));
        Config.JUMP_SPAM = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_JUMP_SPAM.label, ConfigOperation.PROP_JUMP_SPAM.defaultValue));
        Config.JUMP_SPAM_TICK = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_JUMP_SPAM_TICK.label, ConfigOperation.PROP_JUMP_SPAM_TICK.defaultValue));
        Config.JUMP_SPAM_THRESHOLD = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_JUMP_SPAM_THRESHOLD.label, ConfigOperation.PROP_JUMP_SPAM_THRESHOLD.defaultValue));
        Config.WALL_CLIMBING = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_WALL_CLIMBING.label, ConfigOperation.PROP_WALL_CLIMBING.defaultValue));
        Config.TOGGLE_JUMP_MODE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_TOGGLE_JUMP_MODE.label, ConfigOperation.PROP_TOGGLE_JUMP_MODE.defaultValue));
        Config.IN_USE_ELYTRA_JUMP_MODE = Config.JumpMode.valueOf(properties.getProperty(ConfigOperation.PROP_IN_USE_ELYTRA_JUMP_MODE.label, ConfigOperation.PROP_IN_USE_ELYTRA_JUMP_MODE.defaultValue));
        Config.NOT_IN_USE_ELYTRA_JUMP_MODE = Config.JumpMode.valueOf(properties.getProperty(ConfigOperation.PROP_NOT_USE_ELYTRA_JUMP_MODE.label, ConfigOperation.PROP_NOT_USE_ELYTRA_JUMP_MODE.defaultValue));
        Config.SNAPBACK = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SNAPBACK.label, ConfigOperation.PROP_SNAPBACK.defaultValue));
        Config.REFLECTION_ANGLE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_REFLECTION_ANGLE.label, ConfigOperation.PROP_REFLECTION_ANGLE.defaultValue));
        Config.COOL_DOWN_TICKS = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_COOL_DOWN_TICKS.label, ConfigOperation.PROP_COOL_DOWN_TICKS.defaultValue));
        Config.PRIORITIZE_WIND_CHARGE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE.label, ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE.defaultValue));
        Config.PRIORITIZE_ROCKET = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PRIORITIZE_ROCKET.label, ConfigOperation.PROP_PRIORITIZE_ROCKET.defaultValue));
        Config.ATTACK_RANGE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ATTACK_RANGE.label, ConfigOperation.PROP_ATTACK_RANGE.defaultValue));
        Config.ATTACK_MODE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ATTACK_MODE.label, ConfigOperation.PROP_ATTACK_MODE.defaultValue));
        for (int i = 0; i < 4; ++i) {
            Config.ATTACK_DISTANCE[i] = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_ATTACK_DISTANCE[i].label, ConfigOperation.PROP_ATTACK_DISTANCE[i].defaultValue));
            Config.FALL_VELOCITY[i] = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_FALL_VELOCITY[i].label, ConfigOperation.PROP_FALL_VELOCITY[i].defaultValue));
        }
        Config.MANUAL_CAMERA_PITCH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_MANUAL_CAMERA_PITCH.label, ConfigOperation.PROP_MANUAL_CAMERA_PITCH.defaultValue));
        Config.SP_SPIRAL_COUNT = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPIRAL_COUNT.label, ConfigOperation.PROP_SPIRAL_COUNT.defaultValue));
        Config.SP_SPIRAL_LENGTH = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_SPIRAL_LENGTH.label, ConfigOperation.PROP_SPIRAL_LENGTH.defaultValue));
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
        Config.ALSO_SEARCH_INVENTORY = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ALSO_SEARCH_INVENTORY.label, ConfigOperation.PROP_ALSO_SEARCH_INVENTORY.defaultValue));
        Config.CROSSHAIR_MODE = Config.CrosshairMode.valueOf(properties.getProperty(ConfigOperation.PROP_CROSSHAIR_MODE.label, ConfigOperation.PROP_CROSSHAIR_MODE.defaultValue));
        Config.RECON_QUICK_ZOOM = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_RECON_QUICK_ZOOM.label, ConfigOperation.PROP_RECON_QUICK_ZOOM.defaultValue));
        Config.RECON_QUICK_ATTACK = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_RECON_QUICK_ATTACK.label, ConfigOperation.PROP_RECON_QUICK_ATTACK.defaultValue));
        Config.RETURN_TO_PREV_SLOT_MODE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_MODE.label, ConfigOperation.PROP_RETURN_TO_PREV_SLOT_MODE.defaultValue));
        Config.RETURN_TO_PREV_SLOT_BREACH = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_BREACH.label, ConfigOperation.PROP_RETURN_TO_PREV_SLOT_BREACH.defaultValue));
        Config.AUTO_WIND_CHARGE_SELECT = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_AUTO_WIND_CHARGE_SELECT.label, ConfigOperation.PROP_AUTO_WIND_CHARGE_SELECT.defaultValue));
        Config.SHIELD_DRAINING = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_SHIELD_DRAINING.label, ConfigOperation.PROP_SHIELD_DRAINING.defaultValue));
        Config.ELYTRA_BOOST = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ELYTRA_BOOST.label, ConfigOperation.PROP_ELYTRA_BOOST.defaultValue));
        Config.ELYTRA_MANUAL_MODE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_ELYTRA_MANUAL_MODE.label, ConfigOperation.PROP_ELYTRA_MANUAL_MODE.defaultValue));
        Config.THROW_WIND_CHARGE = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_THROW_WIND_CHARGE.label, ConfigOperation.PROP_THROW_WIND_CHARGE.defaultValue));
        Config.EXTREME = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_EXTREME.label, ConfigOperation.PROP_EXTREME.defaultValue));
        Config.DEBUG_LANDING_POS = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_DEBUG_LANDING_POS.label, ConfigOperation.PROP_DEBUG_LANDING_POS.defaultValue));
        Config.DEFAULT_EXPANDED = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_DEFAULT_EXPANDED.label, ConfigOperation.PROP_DEFAULT_EXPANDED.defaultValue));
        Config.DEFAULT_EXPANDED_GLOBAL = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_DEFAULT_EXPANDED_GLOBAL.label, ConfigOperation.PROP_DEFAULT_EXPANDED_GLOBAL.defaultValue));
        Config.PEARL_CATCH = Boolean.parseBoolean(properties.getProperty(ConfigOperation.PROP_PEARL_CATCH.label, ConfigOperation.PROP_PEARL_CATCH.defaultValue));
        Config.PEARL_CATCH_DELAY = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_PEARL_CATCH_DELAY.label, ConfigOperation.PROP_PEARL_CATCH_DELAY.defaultValue));
        Config.PEARL_CATCH_RANGE = Integer.parseInt(properties.getProperty(ConfigOperation.PROP_PEARL_CATCH_RANGE.label, ConfigOperation.PROP_PEARL_CATCH_RANGE.defaultValue));
    }

    public static void saveFile() {
        Properties properties = new Properties();
        try {
            properties.setProperty(ConfigOperation.PROP_ATTACK_ASSISTANCE.label, String.valueOf(Config.ATTACK_ASSISTANCE));
            properties.setProperty(ConfigOperation.PROP_AIM_ASSIST.label, String.valueOf(Config.AIM_ASSIST));
            properties.setProperty(ConfigOperation.PROP_JUMP_ASSIST.label, String.valueOf(Config.JUMP_ASSIST));
            properties.setProperty(ConfigOperation.PROP_SPEAR_ASSIST.label, String.valueOf(Config.SPEAR_ASSIST));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SWAP.label, String.valueOf(Config.SPEAR_SWAP));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SLAM.label, String.valueOf(Config.SPEAR_SLAM));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SLAM_MIN_FALL.label, String.valueOf(Config.SPEAR_SLAM_MIN_FALL));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SLAM_COOLDOWN.label, String.valueOf(Config.SPEAR_SLAM_COOLDOWN));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SLAM_DAMAGE_MULT.label, String.valueOf(Config.SPEAR_SLAM_DAMAGE_MULT_INT));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SLAM_PARTICLES.label, String.valueOf(Config.SPEAR_SLAM_PARTICLES));
            properties.setProperty(ConfigOperation.PROP_SPEAR_SLAM_SOUND.label, String.valueOf(Config.SPEAR_SLAM_SOUND));
            properties.setProperty(ConfigOperation.PROP_SWING_TOGGLE.label, String.valueOf((Object)Config.SWING_TOGGLE));
            properties.setProperty(ConfigOperation.PROP_SNAPBACK_THRESHOLD.label, String.valueOf(Config.SNAPBACK_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_SNAPBACK_TOLERANCE.label, String.valueOf(Config.SNAPBACK_TOLERANCE));
            properties.setProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT.label, String.valueOf(Config.RETURN_TO_PREV_SLOT));
            properties.setProperty(ConfigOperation.PROP_DOUBLE_TAP.label, String.valueOf(Config.DOUBLE_TAP));
            properties.setProperty(ConfigOperation.PROP_PARALLEL_SEARCH.label, String.valueOf(Config.PARALLEL_SEARCH));
            properties.setProperty(ConfigOperation.PROP_DOUBLE_TAP_REWEAR.label, String.valueOf(Config.DOUBLE_TAP_REWEAR));
            properties.setProperty(ConfigOperation.PROP_DOUBLE_TAP_REWEAR_DELAY.label, String.valueOf(Config.DOUBLE_TAP_REWEAR_DELAY));
            properties.setProperty(ConfigOperation.PROP_FRIEND_PROTECTION.label, String.valueOf(Config.FRIEND_PROTECTION));
            properties.setProperty(ConfigOperation.PROP_FRIEND_MARK.label, String.valueOf(Config.FRIEND_MARK));
            properties.setProperty(ConfigOperation.PROP_FRIEND_NOT_FOUND.label, String.valueOf(Config.FRIEND_NOT_FOUND));
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
            properties.setProperty(ConfigOperation.PROP_BEHAVIOR_NOT_WEARING_ARMOR.label, String.valueOf((Object)Config.BEHAVIOR_NOT_WEARING_ARMOR));
            properties.setProperty(ConfigOperation.PROP_ROCKET_BLITZ.label, String.valueOf(Config.ROCKET_BLITZ));
            properties.setProperty(ConfigOperation.PROP_ROCKET_BLITZ_SLOT.label, String.valueOf(Config.ROCKET_BLITZ_SLOT));
            properties.setProperty(ConfigOperation.PROP_AUTO_REFILL.label, String.valueOf(Config.AUTO_REFILL));
            properties.setProperty(ConfigOperation.PROP_ROCKET_TRIGGER.label, String.valueOf((Object)Config.ROCKET_TRIGGER));
            properties.setProperty(ConfigOperation.PROP_SWORD_SWAP_OR_BREACH_SWAP.label, String.valueOf((Object)Config.SWORD_SWAP_OR_BREACH_SWAP));
            properties.setProperty(ConfigOperation.PROP_SWORD_OR_AXE.label, String.valueOf(Config.SWORD_OR_AXE));
            properties.setProperty(ConfigOperation.PROP_AXE_SLOT.label, String.valueOf(Config.AXE_SLOT));
            properties.setProperty(ConfigOperation.PROP_MACE_PRIMARY.label, String.valueOf(Config.MACE_PRIMARY));
            properties.setProperty(ConfigOperation.PROP_MACE_BREACH.label, String.valueOf(Config.MACE_BREACH));
            properties.setProperty(ConfigOperation.PROP_SNAPBACK.label, String.valueOf(Config.SNAPBACK));
            properties.setProperty(ConfigOperation.PROP_STUN_HIGH.label, String.valueOf(Config.STUN_HIGH));
            properties.setProperty(ConfigOperation.PROP_STUN_LOW.label, String.valueOf(Config.STUN_LOW));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_VILLAGER.label, String.valueOf(Config.ALLOWED_VILLAGER));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_IRON_GOLEM.label, String.valueOf(Config.ALLOWED_IRON_GOLEM));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_HOSTILE.label, String.valueOf(Config.ALLOWED_HOSTILE));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_PASSIVE.label, String.valueOf(Config.ALLOWED_PASSIVE));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_AMBIENT.label, String.valueOf(Config.ALLOWED_AMBIENT));
            properties.setProperty(ConfigOperation.PROP_ALLOWED_PLAYER.label, String.valueOf(Config.ALLOWED_PLAYER));
            properties.setProperty(ConfigOperation.PROP_MAX_SPEED_YAW.label, String.valueOf(Config.MAX_SPEED_YAW));
            properties.setProperty(ConfigOperation.PROP_AIM_MODE.label, String.valueOf((Object)Config.AIM_MODE));
            properties.setProperty(ConfigOperation.PROP_AUTO_MODE_THRESHOLD.label, String.valueOf(Config.AUTO_MODE_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_LEGACY_AIM_MODE.label, String.valueOf(Config.LEGACY_AIM_MODE));
            properties.setProperty(ConfigOperation.PROP_AIM_OFFSET.label, String.valueOf(Config.AIM_OFFSET));
            properties.setProperty(ConfigOperation.PROP_AIM_FORCED_ADJUSTMENT.label, String.valueOf(Config.AIM_FORCED_ADJUSTMENT));
            properties.setProperty(ConfigOperation.PROP_AIM_FALL_THRESHOLD.label, String.valueOf(Config.AIM_FALL_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_AIM_RAYCAST.label, String.valueOf(Config.AIM_RAYCAST));
            properties.setProperty(ConfigOperation.PROP_AIM_RAYCAST_RANGE.label, String.valueOf(Config.AIM_RAYCAST_RANGE));
            properties.setProperty(ConfigOperation.PROP_AIM_RAYCAST_RADIUS.label, String.valueOf(Config.AIM_RAYCAST_RADIUS));
            properties.setProperty(ConfigOperation.PROP_AUTO_ELYTRA.label, String.valueOf(Config.AUTO_ELYTRA));
            properties.setProperty(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD.label, String.valueOf(Config.AUTO_ELYTRA_TICK_AHEAD));
            properties.setProperty(ConfigOperation.PROP_AUTO_ELYTRA_DISTANCE.label, String.valueOf(Config.AUTO_ELYTRA_DISTANCE));
            properties.setProperty(ConfigOperation.PROP_AUTO_ELYTRA_DISTANCE_NORMAL.label, String.valueOf(Config.AUTO_ELYTRA_DISTANCE_NORMAL));
            properties.setProperty(ConfigOperation.PROP_AUTO_ELYTRA_TICK_AHEAD_NORMAL.label, String.valueOf(Config.AUTO_ELYTRA_TICK_AHEAD_NORMAL));
            properties.setProperty(ConfigOperation.PROP_DISPLAY_ACTION_BAR.label, String.valueOf((Object)Config.DISPLAY_ACTION_BAR));
            properties.setProperty(ConfigOperation.PROP_TOGGLE_SLOT.label, String.valueOf(Config.TOGGLE_SLOT));
            properties.setProperty(ConfigOperation.PROP_JUMP_SPAM.label, String.valueOf(Config.JUMP_SPAM));
            properties.setProperty(ConfigOperation.PROP_JUMP_SPAM_TICK.label, String.valueOf(Config.JUMP_SPAM_TICK));
            properties.setProperty(ConfigOperation.PROP_JUMP_SPAM_THRESHOLD.label, String.valueOf(Config.JUMP_SPAM_THRESHOLD));
            properties.setProperty(ConfigOperation.PROP_WALL_CLIMBING.label, String.valueOf(Config.WALL_CLIMBING));
            properties.setProperty(ConfigOperation.PROP_IN_USE_ELYTRA_JUMP_MODE.label, String.valueOf((Object)Config.IN_USE_ELYTRA_JUMP_MODE));
            properties.setProperty(ConfigOperation.PROP_NOT_USE_ELYTRA_JUMP_MODE.label, String.valueOf((Object)Config.NOT_IN_USE_ELYTRA_JUMP_MODE));
            properties.setProperty(ConfigOperation.PROP_TOGGLE_JUMP_MODE.label, String.valueOf(Config.TOGGLE_JUMP_MODE));
            properties.setProperty(ConfigOperation.PROP_ATTACK_RANGE.label, String.valueOf(Config.ATTACK_RANGE));
            properties.setProperty(ConfigOperation.PROP_ATTACK_MODE.label, String.valueOf(Config.ATTACK_MODE));
            properties.setProperty(ConfigOperation.PROP_REFLECTION_ANGLE.label, String.valueOf(Config.REFLECTION_ANGLE));
            properties.setProperty(ConfigOperation.PROP_COOL_DOWN_TICKS.label, String.valueOf(Config.COOL_DOWN_TICKS));
            properties.setProperty(ConfigOperation.PROP_PRIORITIZE_WIND_CHARGE.label, String.valueOf(Config.PRIORITIZE_WIND_CHARGE));
            properties.setProperty(ConfigOperation.PROP_PRIORITIZE_ROCKET.label, String.valueOf(Config.PRIORITIZE_ROCKET));
            properties.setProperty(ConfigOperation.PROP_MANUAL_CAMERA_PITCH.label, String.valueOf(Config.MANUAL_CAMERA_PITCH));
            properties.setProperty(ConfigOperation.PROP_SPIRAL_COUNT.label, String.valueOf(Config.SP_SPIRAL_COUNT));
            properties.setProperty(ConfigOperation.PROP_SPIRAL_LENGTH.label, String.valueOf(Config.SP_SPIRAL_LENGTH));
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
            properties.setProperty(ConfigOperation.PROP_ALSO_SEARCH_INVENTORY.label, String.valueOf(Config.ALSO_SEARCH_INVENTORY));
            properties.setProperty(ConfigOperation.PROP_CROSSHAIR_MODE.label, String.valueOf((Object)Config.CROSSHAIR_MODE));
            properties.setProperty(ConfigOperation.PROP_RECON_QUICK_ZOOM.label, String.valueOf(Config.RECON_QUICK_ZOOM));
            properties.setProperty(ConfigOperation.PROP_RECON_QUICK_ATTACK.label, String.valueOf(Config.RECON_QUICK_ATTACK));
            properties.setProperty(ConfigOperation.PROP_ELYTRA_BOOST.label, String.valueOf(Config.ELYTRA_BOOST));
            properties.setProperty(ConfigOperation.PROP_ELYTRA_MANUAL_MODE.label, String.valueOf(Config.ELYTRA_MANUAL_MODE));
            properties.setProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_MODE.label, String.valueOf(Config.RETURN_TO_PREV_SLOT_MODE));
            properties.setProperty(ConfigOperation.PROP_RETURN_TO_PREV_SLOT_BREACH.label, String.valueOf(Config.RETURN_TO_PREV_SLOT_BREACH));
            properties.setProperty(ConfigOperation.PROP_AUTO_WIND_CHARGE_SELECT.label, String.valueOf(Config.AUTO_WIND_CHARGE_SELECT));
            properties.setProperty(ConfigOperation.PROP_SHIELD_DRAINING.label, String.valueOf(Config.SHIELD_DRAINING));
            properties.setProperty(ConfigOperation.PROP_THROW_WIND_CHARGE.label, String.valueOf(Config.THROW_WIND_CHARGE));
            properties.setProperty(ConfigOperation.PROP_EXTREME.label, String.valueOf(Config.EXTREME));
            properties.setProperty(ConfigOperation.PROP_DEBUG_LANDING_POS.label, String.valueOf(Config.DEBUG_LANDING_POS));
            properties.setProperty(ConfigOperation.PROP_DEFAULT_EXPANDED.label, String.valueOf(Config.DEFAULT_EXPANDED));
            properties.setProperty(ConfigOperation.PROP_DEFAULT_EXPANDED_GLOBAL.label, String.valueOf(Config.DEFAULT_EXPANDED_GLOBAL));
            properties.setProperty(ConfigOperation.PROP_PEARL_CATCH.label, String.valueOf(Config.PEARL_CATCH));
            properties.setProperty(ConfigOperation.PROP_PEARL_CATCH_DELAY.label, String.valueOf(Config.PEARL_CATCH_DELAY));
            properties.setProperty(ConfigOperation.PROP_PEARL_CATCH_RANGE.label, String.valueOf(Config.PEARL_CATCH_RANGE));
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
