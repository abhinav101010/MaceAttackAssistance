/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.ElytraBoost;

public class JumpController {
    public static boolean ON_SLIME_BLOCK = false;
    public static boolean JUMP;
    private static int chargeJumpCounter;

    public static void tick() {
        if (chargeJumpCounter > 0 && --chargeJumpCounter == 0) {
            JUMP = false;
        }
    }

    public static void setChargeJumpCounter(int value) {
        if (!ElytraBoost.isElytraBoostIdle()) {
            JUMP = true;
            chargeJumpCounter = value;
        }
    }

    static {
        chargeJumpCounter = 0;
    }
}
