/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.InputConstants$Type
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.KeyMapping$Category
 *  net.minecraft.resources.Identifier
 */
package com.papack.maceattackassistance.client.config;

import com.mojang.blaze3d.platform.InputConstants;
import com.papack.maceattackassistance.client.ColorData;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

@Environment(value=EnvType.CLIENT)
public class Config {
    public static final Identifier CROSSHAIR_TEXTURE = Identifier.withDefaultNamespace((String)"hud/crosshair");
    public static final Identifier CUSTOM_CROSSHAIR_TEXTURE = Identifier.fromNamespaceAndPath((String)"maceattackassistance", (String)"hud/custom_crosshair");
    public static final Path MAA_CONF_DIR = Paths.get(String.valueOf(FabricLoader.getInstance().getGameDir()) + "/config/MaceAttackAssistance", new String[0]);
    public static final Path MAA_CONFIG = Paths.get(String.valueOf(MAA_CONF_DIR) + "/mace_attack_assistance_v500.properties", new String[0]);
    public static final KeyMapping.Category KEY_CATEGORY_MAA = KeyMapping.Category.register((Identifier)Identifier.fromNamespaceAndPath((String)"maceattackassistance", (String)"main"));
    public static final KeyMapping.Category KEY_CATEGORY_MAA_TRIGGER = KeyMapping.Category.register((Identifier)Identifier.fromNamespaceAndPath((String)"maceattackassistance", (String)"trigger"));
    public static final String KEY_DESC_AIM = "key.desc.mace_aim_assistance";
    public static final String KEY_DESC_AIR_POT = "key.desc.air_pot";
    public static final String KEY_DESC_ATTACK = "key.desc.mace_attack_assistance";
    public static final String KEY_DESC_AUTO_WIND_CHARGE_SELECT = "key.desc.auto_wind_charge_select";
    public static final String KEY_DESC_BREACH_SWAP_MODE = "key.desc.breach_swap_mode";
    public static final String KEY_DESC_BULK_REFILL = "key.desc.bulk_refill";
    public static final String KEY_DESC_CONFIG = "key.desc.mace_config_screen";
    public static final String KEY_DESC_ELYTRA_BOOST = "key.desc.elytra_boost";
    public static final String KEY_DESC_ENDER_PEARL = "key.desc.ender_pearl";
    public static final String KEY_DESC_FRIEND = "key.desc.friend";
    public static final String KEY_DESC_INSTANT_PEARL_CATCH = "key.desc.instant_pearl_catch";
    public static final String KEY_DESC_JUMP = "key.desc.jump_assist";
    public static final String KEY_DESC_KNOCKBACK_DISPLACEMENT = "key.desc.knockback_displacement";
    public static final String KEY_DESC_MANUAL_STUN_SLAM = "key.desc.manual_stun_slam";
    public static final String KEY_DESC_PEARL_CATCH_DOWNWARD = "key.desc.pearl_catch_downward";
    public static final String KEY_DESC_PEARL_GRAPPLE = "key.desc.pearl_grapple";
    public static final String KEY_DESC_RETURN_TO_PREV_SLOT = "key.desc.return_to_prev_slot";
    public static final String KEY_DESC_RETURN_TO_PREV_SLOT_BREACH = "key.desc.return_to_prev_slot_breach";
    public static final String KEY_DESC_ROCKET_BLITZ = "key.desc.rocket_blitz";
    public static final String KEY_DESC_SEARCH = "key.desc.target_search";
    public static final String KEY_DESC_SPEAR_LUNGE = "key.desc.spear_lunge";
    public static final String KEY_DESC_SWORD_OR_AXE = "key.desc.sword_or_axe";
    public static final String KEY_DESC_TOGGLE_BREACH = "key.desc.toggle_breach";
    public static final String KEY_DESC_TOGGLE_DOUBLE_TAP = "key.desc.toggle_double_tap";
    public static final String KEY_DESC_TOGGLE_ELYTRA = "key.desc.mace_toggle_elytra";
    public static final String KEY_DESC_TOGGLE_JUMP_MODE = "key.desc.toggle_jump_mode";
    public static final String KEY_DESC_TOGGLE_REWEAR = "key.desc.toggle_rewear";
    public static final String KEY_DESC_WIND_CHARGE = "key.desc.wind_charge";
    public static final String KEY_DESC_ZOOM_CAMERA = "key.desc.zoom_camera";
    public static final long AIM_DURATION_80 = 80L;
    public static final long AIM_DURATION_50 = 50L;
    public static boolean ALWAYS_USE_UPWARD_ANGLE;
    public static boolean ALWAYS_USE_DOWNWARD_ANGLE;
    public static int PEARL_CATCH_UPWARD_ANGLE;
    public static int PEARL_CATCH_DOWNWARD_ANGLE;
    public static boolean AUTO_LEFT_CLICK;
    public static int KBD_RANGE;
    public static boolean KBD_INSTANT_AIM;
    public static boolean KBD_AUTO_FLICK;
    public static int KBD_FLICK_ANGLE;
    public static boolean PEARL_CATCH_MODE;
    public static boolean PEARL_CATCH_ADJUST;
    public static int PEARL_CATCH_DELAY;
    public static int PREV_SLOT_DELAY;
    public static int LUNGE_EXECUTION_SLOT;
    public static boolean KEEP_AFTER_EXECUTION;
    public static boolean FORCED_EXECUTION_SLOT;
    public static boolean FRIEND_PROTECTION;
    public static boolean FRIEND_MARK;
    public static boolean FRIEND_NOT_FOUND;
    public static boolean ONE_TICK_STUN_SLAM;
    public static boolean FORCED_DISABLE;
    public static int AIM_FALL_THRESHOLD;
    public static boolean AIM_FORCED_ADJUSTMENT;
    public static boolean AIM_RAYCAST;
    public static int AIM_RAYCAST_RANGE;
    public static int AIM_RAYCAST_RADIUS;
    public static boolean AIM_ELYTRA;
    public static boolean DEFAULT_EXPANDED_SUB;
    public static boolean DEFAULT_EXPANDED_GLOBAL;
    public static boolean EXTREME;
    public static boolean AUTO_ELYTRA;
    public static int AUTO_ELYTRA_TICK_AHEAD;
    public static int AUTO_ELYTRA_DISTANCE;
    public static int AUTO_ELYTRA_TICK_AHEAD_NORMAL;
    public static int AUTO_ELYTRA_DISTANCE_NORMAL;
    public static boolean AUTO_WIND_CHARGE_SELECT;
    public static boolean RETURN_TO_PREV_SLOT;
    public static boolean RETURN_TO_PREV_SLOT_BREACH;
    public static int RETURN_TO_PREV_SLOT_MODE;
    public static boolean AUTO_SHIELD_DRAINING;
    public static boolean INSTANT_SHIELD_DRAINING;
    public static boolean ATTACK_ASSISTANCE;
    public static boolean AIM_ASSIST;
    public static boolean JUMP_ASSIST;
    public static boolean SPEAR_ASSIST;
    public static boolean HOT_SWAP;
    public static int HEIGHT_THRESHOLD;
    public static boolean WEAPON_SWING;
    public static SwingToggle SWING_TOGGLE;
    public static int SNAPBACK_THRESHOLD;
    public static boolean SNAPBACK;
    public static int SNAPBACK_TOLERANCE;
    public static boolean STUN_SLAMMING;
    public static boolean DOUBLE_TAP;
    public static boolean PARALLEL_SEARCH;
    public static boolean DOUBLE_TAP_REWEAR;
    public static int DOUBLE_TAP_REWEAR_DELAY;
    public static boolean DT_AERIAL_DIVE_MODE;
    public static Behavior SWORD_SWAP_OR_BREACH_SWAP;
    public static boolean SWORD_OR_AXE;
    public static boolean BREACH_SWAP;
    public static boolean BREACH_LIMITED;
    public static boolean BREACH_ON_GROUND;
    public static Behavior BEHAVIOR_NOT_WEARING_ARMOR;
    public static boolean ROCKET_BLITZ;
    public static int ROCKET_BLITZ_SLOT;
    public static boolean ROCKET_BLITZ_CAN_EMPTY_SLOT;
    public static boolean AUTO_REFILL;
    public static RocketTrigger ROCKET_TRIGGER;
    public static RocketTrigger ROCKET_TRIGGER_SPEAR;
    public static int AXE_SLOT;
    public static int MACE_PRIMARY;
    public static int MACE_BREACH;
    public static int STUN_HIGH;
    public static int STUN_LOW;
    public static int STUN_ADJUSTMENT;
    public static boolean TARGET_MARKER;
    public static boolean HIDE_MARKER;
    public static int MARKER_OFFSET;
    public static MarkerType MARKER_TYPE;
    public static WeaponParticle MACE_PARTICLE;
    public static TransitionOrder PARTICLE_ORDER;
    public static int PARTICLE_TRANSITION_THRESHOLD;
    public static boolean ALLOWED_VILLAGER;
    public static boolean ALLOWED_IRON_GOLEM;
    public static boolean ALLOWED_HOSTILE;
    public static boolean ALLOWED_PASSIVE;
    public static boolean ALLOWED_AMBIENT;
    public static boolean ALLOWED_PLAYER;
    public static int MAX_SPEED_YAW;
    public static AimMode AIM_MODE;
    public static int AUTO_MODE_THRESHOLD;
    public static boolean LEGACY_AIM_MODE;
    public static DisplayAt DISPLAY_ACTION_BAR;
    public static int TOGGLE_SLOT;
    public static boolean JUMP_SPAM;
    public static int JUMP_SPAM_TICK;
    public static int JUMP_SPAM_THRESHOLD;
    public static boolean WALL_CLIMBING;
    public static JumpMode IN_USE_ELYTRA_JUMP_MODE;
    public static JumpMode NOT_IN_USE_ELYTRA_JUMP_MODE;
    public static boolean TOGGLE_JUMP_MODE;
    public static boolean ALSO_SEARCH_INVENTORY;
    public static boolean RECON_QUICK_ZOOM;
    public static boolean RECON_QUICK_ATTACK;
    public static boolean ELYTRA_MANUAL_MODE;
    public static CrosshairMode CROSSHAIR_MODE;
    public static int ATTACK_RANGE;
    public static int ATTACK_RANGE_STUN;
    public static int ATTACK_RANGE_SPEAR;
    public static boolean ATTACK_MODE;
    public static int[] ATTACK_DISTANCE;
    public static int[] FALL_VELOCITY;
    public static boolean ELYTRA_BOOST;
    public static int COOL_DOWN_TICKS;
    public static int REFLECTION_ANGLE;
    public static boolean PRIORITIZE_WIND_CHARGE;
    public static boolean PRIORITIZE_ROCKET;
    public static int MANUAL_CAMERA_PITCH;
    public static boolean ZOOM_CAMERA;
    public static ReconView ZOOM_VIEW;
    public static int ZOOM_STEP;
    public static boolean CAMERA_RETURN_BEHAVIOR;
    public static boolean PERSPECTIVE_BACK_CROSSHAIR;
    public static boolean DEBUG_SCREEN;
    public static boolean DEBUG_LANDING_POS;
    public static int SP_SPIRAL_COUNT;
    public static int SP_SPIRAL_LENGTH;
    public static int SP_SPIRAL_ALPHA;
    public static int SP_SPIRAL_GAMMA;
    public static int SP_COILS;
    public static int SP_HEIGHT;
    public static int SP_SIZE;
    public static int SP_SPEED;
    public static int SP_BASE_RADIUS;
    public static int SP_WAVE_SPEED;
    public static int SP_WAVE_AMPLITUDE;
    public static ColorData.Color COLOR_VILLAGER;
    public static ColorData.Color COLOR_HOSTILE;
    public static ColorData.Color COLOR_WARDEN;
    public static ColorData.Color COLOR_IRON_GOLEM;
    public static ColorData.Color COLOR_PASSIVE;
    public static ColorData.Color COLOR_AMBIENT;
    public static ColorData.Color COLOR_PLAYER;
    public static int FOV_HORIZONTAL;
    public static int FOV_HORIZONTAL_ON_ZOOM;
    public static int FOV_VERTICAL;
    public static int FOV_VERTICAL_ON_ZOOM;
    public static int RADAR_HORIZONTAL;
    public static int RADAR_UPWARD;
    public static int RADAR_DOWNWARD;
    public static int RADAR_UPDATE_INTERVAL;
    public static boolean TARGET_SEARCH_MODE;
    public static boolean FLAP_SUPPRESSION;
    public static int FLAP_SUPPRESSION_THRESHOLD;
    public static int FLAP_SUPPRESSION_TICK;
    public static boolean FOV_SUPPRESSION;
    public static int ENDER_PEARL_COOLDOWN_TIME;
    public static final int JUMP_COOLDOWN = 20;
    public static final float A_CURVE = 0.5f;
    public static final double FALLING_20 = 1.5;
    public static final double FALLING_ONE_HALF_BLOCKS = -0.447;
    public static final double RANGE = 8.0;
    public static KeyMapping AIM_SETTING_KEY;
    public static KeyMapping ATTACK_SETTING_KEY;
    public static KeyMapping BREACH_SWAP_SETTING_KEY;
    public static KeyMapping BULK_REFILL_KEY;
    public static KeyMapping CONFIG_SCREEN_KEY;
    public static KeyMapping DOUBLE_TAP_SETTING_KEY;
    public static KeyMapping ENDER_PEARL_KEY;
    public static KeyMapping WIND_CHARGE_KEY;
    public static KeyMapping JUMP_SETTING_KEY;
    public static KeyMapping RETURN_TO_PREV_SLOT_SETTING_KEY;
    public static KeyMapping REWEAR_SETTING_KEY;
    public static KeyMapping SEARCH_SETTING_KEY;
    public static KeyMapping TOGGLE_ELYTRA_KEY;
    public static KeyMapping TOGGLE_JUMP_MODE_KEY;
    public static KeyMapping ZOOM_CAMERA_KEY;
    public static KeyMapping ELYTRA_BOOST_SETTING_KEY;
    public static KeyMapping RETURN_TO_PREV_SLOT_BREACH_SETTING_KEY;
    public static KeyMapping AUTO_WIND_CHARGE_SELECT_SETTING_KEY;
    public static KeyMapping FRIEND_PROTECTION_SETTING_KEY;
    public static KeyMapping TOGGLE_BREACH_SWAP_MODE_KEY;
    public static KeyMapping TOGGLE_SWORD_OR_AXE_KEY;
    public static KeyMapping SPEAR_LUNGE_KEY;
    public static KeyMapping MANUAL_STUN_SLAM_KEY;
    public static KeyMapping PEARL_GRAPPLE_KEY;
    public static KeyMapping PEARL_GRAPPLE_DOWNWARD_KEY;
    public static KeyMapping INSTANT_PEARL_CATCH_KEY;
    public static KeyMapping KNOCKBACK_DISPLACEMENT_KEY;
    public static KeyMapping ROCKET_BLITZ_KEY;
    public static KeyMapping AIR_POT_KEY;
    public static final double[] VELOCITY_BY_DISTANCE;

    public static void initialize() {
        ATTACK_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_ATTACK, InputConstants.Type.KEYSYM, 295, KEY_CATEGORY_MAA));
        AIM_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_AIM, InputConstants.Type.KEYSYM, 296, KEY_CATEGORY_MAA));
        CONFIG_SCREEN_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_CONFIG, InputConstants.Type.KEYSYM, 73, KEY_CATEGORY_MAA));
        TOGGLE_ELYTRA_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_TOGGLE_ELYTRA, InputConstants.Type.KEYSYM, 82, KEY_CATEGORY_MAA_TRIGGER));
        ENDER_PEARL_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_ENDER_PEARL, InputConstants.Type.KEYSYM, 90, KEY_CATEGORY_MAA_TRIGGER));
        WIND_CHARGE_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_WIND_CHARGE, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        BULK_REFILL_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_BULK_REFILL, InputConstants.Type.KEYSYM, 86, KEY_CATEGORY_MAA_TRIGGER));
        ZOOM_CAMERA_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_ZOOM_CAMERA, InputConstants.Type.KEYSYM, 78, KEY_CATEGORY_MAA_TRIGGER));
        JUMP_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_JUMP, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        SEARCH_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_SEARCH, InputConstants.Type.KEYSYM, 76, KEY_CATEGORY_MAA));
        RETURN_TO_PREV_SLOT_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_RETURN_TO_PREV_SLOT, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        BREACH_SWAP_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_TOGGLE_BREACH, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        DOUBLE_TAP_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_TOGGLE_DOUBLE_TAP, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        TOGGLE_JUMP_MODE_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_TOGGLE_JUMP_MODE, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        REWEAR_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_TOGGLE_REWEAR, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        ELYTRA_BOOST_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_ELYTRA_BOOST, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        RETURN_TO_PREV_SLOT_BREACH_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_RETURN_TO_PREV_SLOT_BREACH, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        AUTO_WIND_CHARGE_SELECT_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_AUTO_WIND_CHARGE_SELECT, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        FRIEND_PROTECTION_SETTING_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_FRIEND, InputConstants.Type.KEYSYM, 84, KEY_CATEGORY_MAA));
        TOGGLE_BREACH_SWAP_MODE_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_BREACH_SWAP_MODE, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        TOGGLE_SWORD_OR_AXE_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_SWORD_OR_AXE, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA));
        SPEAR_LUNGE_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_SPEAR_LUNGE, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        MANUAL_STUN_SLAM_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_MANUAL_STUN_SLAM, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        PEARL_GRAPPLE_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_PEARL_GRAPPLE, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        PEARL_GRAPPLE_DOWNWARD_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_PEARL_CATCH_DOWNWARD, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        INSTANT_PEARL_CATCH_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_INSTANT_PEARL_CATCH, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        KNOCKBACK_DISPLACEMENT_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_KNOCKBACK_DISPLACEMENT, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        ROCKET_BLITZ_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_ROCKET_BLITZ, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
        AIR_POT_KEY = KeyMappingHelper.registerKeyMapping((KeyMapping)new KeyMapping(KEY_DESC_AIR_POT, InputConstants.Type.KEYSYM, -1, KEY_CATEGORY_MAA_TRIGGER));
    }

    static {
        ATTACK_DISTANCE = new int[4];
        FALL_VELOCITY = new int[4];
        ZOOM_CAMERA = false;
        DEBUG_SCREEN = false;
        ENDER_PEARL_COOLDOWN_TIME = 20;
        VELOCITY_BY_DISTANCE = new double[]{-0.0784, -0.3766, -0.5169, -0.6517, -0.717, -0.8391, -0.9054, -0.9657, -1.0248, -1.0827, -1.1394, -1.195, -1.195, -1.2495, -1.3029, -1.355, -1.4066, -1.4066, -1.4568, -1.5061, -1.5061, -1.5544, -1.6017, -1.648, -1.6935, -1.738, -1.7817, -1.8244, -1.8663, -1.9074, -1.9477, -1.9871, -2.0258, -2.0636, -2.1008, -2.1371, -2.1728, -2.2077, -2.242, -2.2756, -2.3084, -2.3407, -2.3723, -2.4032, -2.4335, -2.4633, -2.4924, -2.521, -2.5489, -2.5764, -2.6032, -2.6296, -2.6554, -2.6807, -2.7054, -2.7297, -2.7535};
    }

    public static enum RocketTrigger {
        L_SHIFT(340),
        L_CTRL(341),
        L_ALT(342),
        SPACE(32);

        private final int glfwKey;

        private RocketTrigger(int glfwKey) {
            this.glfwKey = glfwKey;
        }

        public int getGlfwKey() {
            return this.glfwKey;
        }
    }

    public static enum SwingToggle {
        L_SHIFT(340),
        L_CTRL(341),
        L_ALT(342),
        R_SHIFT(344),
        R_CTRL(345),
        Off(-1);

        private final int glfwKey;

        private SwingToggle(int glfwKey) {
            this.glfwKey = glfwKey;
        }

        public int getGlfwKey() {
            return this.glfwKey;
        }
    }

    public static enum Behavior {
        Off,
        BreachSwap,
        SwordSwap;

    }

    public static enum DisplayAt {
        Off,
        ActionBar,
        Chat;

    }

    public static enum CrosshairMode {
        Off,
        Elytra_Equipped,
        While_Gliding;

    }

    public static enum JumpMode {
        High,
        Low,
        Toggle;

    }

    public static enum ReconView {
        FIRST_PERSON,
        THIRD_PERSON_BACK;

    }

    public static enum MarkerType {
        Frame,
        Spiral,
        Beam;

    }

    public static enum AimMode {
        Track,
        Nearest,
        Auto;

    }

    public static enum TransitionOrder {
        Blue_Red,
        Red_Blue;

    }

    public static enum WeaponParticle {
        off,
        Blue,
        Red,
        Transition;

    }

    public record SwingData(boolean canSwing, boolean isKeyPressed) {
    }
}
