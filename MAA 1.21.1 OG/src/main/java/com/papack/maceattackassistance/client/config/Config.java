/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.util.InputUtil$class_307
 */
package com.papack.maceattackassistance.client.config;

import com.papack.maceattackassistance.client.ColorData;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(value=EnvType.CLIENT)
public class Config {
    public static final Path MAA_CONF_DIR = Paths.get(String.valueOf(FabricLoader.getInstance().getGameDir()) + "/config/MaceAttackAssistance", new String[0]);
    public static final Path MAA_CONFIG = Paths.get(String.valueOf(MAA_CONF_DIR) + "/mace_attack_assistance_v3.properties", new String[0]);
    public static final String KEY_CATEGORY_MACE_ATTACK_ASSISTANCE = "key.category.mace_attack_assistance";
    public static final String KEY_DESC_ATTACK = "key.desc.mace_attack_assistance";
    public static final String KEY_DESC_AIM = "key.desc.mace_aim_assistance";
    public static final String KEY_DESC_JUMP = "key.desc.jump_assist";
    public static final String KEY_DESC_SEARCH = "key.desc.target_search";
    public static final String KEY_DESC_CONFIG = "key.desc.mace_config_screen";
    public static final String KEY_DESC_TOGGLE_ELYTRA = "key.desc.mace_toggle_elytra";
    public static final String KEY_DESC_ENDER_PEARL = "key.desc.ender_pearl";
    public static final String KEY_DESC_BULK_REFILL = "key.desc.bulk_refill";
    public static final String KEY_DESC_ZOOM_CAMERA = "key.desc.zoom_camera";
    public static final String KEY_DESC_RETURN_TO_PREV_SLOT = "key.desc.return_to_prev_slot";
    public static boolean EXTREME;
    public static boolean RETURN_TO_PREV_SLOT;
    public static boolean ATTACK_ASSISTANCE;
    public static boolean AIM_ASSIST;
    public static boolean JUMP_ASSIST;
    public static boolean HOT_SWAP;
    public static int HEIGHT_THRESHOLD;
    public static boolean WEAPON_SWING;
    public static SwingToggle SWING_TOGGLE;
    public static int SNAPBACK_THRESHOLD;
    public static boolean SNAPBACK;
    public static int SNAPBACK_TOLERANCE;
    public static boolean STUN_SLAMMING;
    public static boolean BREACH_SWAP;
    public static boolean BREACH_LIMITED;
    public static boolean BREACH_ON_GROUND;
    public static boolean ROCKET_BLITZ;
    public static int ROCKET_BLITZ_SLOT;
    public static boolean AUTO_REFILL;
    public static RocketTrigger ROCKET_TRIGGER;
    public static int AXE_SLOT;
    public static int MACE_PRIMARY;
    public static int MACE_BREACH;
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
    public static boolean DISPLAY_ACTION_BAR;
    public static int TOGGLE_SLOT;
    public static boolean JUMP_SPAM;
    public static int JUMP_SPAM_TICK;
    public static boolean WALL_CLIMBING;
    public static int ATTACK_RANGE;
    public static int ATTACK_RANGE_DIFF;
    public static int[] ATTACK_DISTANCE;
    public static int[] FALL_VELOCITY;
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
    public static final int JUMP_COOLDOWN = 20;
    public static final float A_CURVE = 0.5f;
    public static final double FALLING_20 = 1.5;
    public static final double FALLING_ONE_HALF_BLOCKS = -0.447;
    public static final double RANGE = 3.5;
    public static KeyBinding ATTACK_SETTING_KEY;
    public static KeyBinding AIM_SETTING_KEY;
    public static KeyBinding JUMP_SETTING_KEY;
    public static KeyBinding SEARCH_SETTING_KEY;
    public static KeyBinding CONFIG_SCREEN_KEY;
    public static KeyBinding TOGGLE_ELYTRA_KEY;
    public static KeyBinding ENDER_PEARL_KEY;
    public static KeyBinding BULK_REFILL_KEY;
    public static KeyBinding ZOOM_CAMERA_KEY;
    public static KeyBinding RETURN_TO_PREV_SLOT_SETTING_KEY;
    public static final double[] VELOCITY_BY_DISTANCE;

    public static void initialize() {
        ATTACK_SETTING_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_ATTACK, InputUtil.Type.KEYSYM, 295, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        AIM_SETTING_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_AIM, InputUtil.Type.KEYSYM, 296, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        CONFIG_SCREEN_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_CONFIG, InputUtil.Type.KEYSYM, 73, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        TOGGLE_ELYTRA_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_TOGGLE_ELYTRA, InputUtil.Type.KEYSYM, 82, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        ENDER_PEARL_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_ENDER_PEARL, InputUtil.Type.KEYSYM, 90, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        BULK_REFILL_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_BULK_REFILL, InputUtil.Type.KEYSYM, 86, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        ZOOM_CAMERA_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_ZOOM_CAMERA, InputUtil.Type.KEYSYM, 78, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        JUMP_SETTING_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_JUMP, InputUtil.Type.KEYSYM, 75, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        SEARCH_SETTING_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_SEARCH, InputUtil.Type.KEYSYM, 76, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
        RETURN_TO_PREV_SLOT_SETTING_KEY = KeyBindingHelper.registerKeyBinding((KeyBinding)new KeyBinding(KEY_DESC_RETURN_TO_PREV_SLOT, InputUtil.Type.KEYSYM, 66, KEY_CATEGORY_MACE_ATTACK_ASSISTANCE));
    }

    static {
        ATTACK_DISTANCE = new int[4];
        FALL_VELOCITY = new int[4];
        ZOOM_CAMERA = false;
        DEBUG_SCREEN = false;
        VELOCITY_BY_DISTANCE = new double[]{-0.0784, -0.3766, -0.5169, -0.6517, -0.717, -0.8391, -0.9054, -0.9657, -1.0248, -1.0827, -1.1394, -1.195, -1.195, -1.2495, -1.3029, -1.355, -1.4066, -1.4066, -1.4568, -1.5061, -1.5061, -1.5544, -1.6017, -1.648, -1.6935, -1.738, -1.7817, -1.8244, -1.8663, -1.9074, -1.9477, -1.9871, -2.0258, -2.0636, -2.1008, -2.1371, -2.1728, -2.2077, -2.242, -2.2756, -2.3084, -2.3407, -2.3723, -2.4032, -2.4335, -2.4633, -2.4924, -2.521, -2.5489, -2.5764, -2.6032, -2.6296, -2.6554, -2.6807, -2.7054, -2.7297, -2.7535};
    }

    public static enum RocketTrigger {
        L_SHIFT(340),
        L_CTRL(341),
        L_ALT(342),
        SPACE(32),
        MOUSE_R(1);

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
        R_CTRL(345);

        private final int glfwKey;

        private SwingToggle(int glfwKey) {
            this.glfwKey = glfwKey;
        }

        public int getGlfwKey() {
            return this.glfwKey;
        }
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

    public record VecData(Vec3d position, Vec3d velocity) {
    }
}
