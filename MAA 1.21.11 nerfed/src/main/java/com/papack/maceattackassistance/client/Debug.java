/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Vec3d
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.MaceAttackAssistanceClient;
import net.minecraft.util.math.Vec3d;

public class Debug {
    public static double pitch = 0.0;
    public static double incidenceTick = 0.0;
    public static double calculateTick = 0.0;
    public static Vec3d velocity;
    public static double altitude;
    public static double result;
    public static double previous_y;
    public static boolean logFlag;
    public static boolean throwByTick;
    public static int counter;

    public static void logOutPut() {
        if (logFlag) {
            MaceAttackAssistanceClient.LOGGER.info("pitch:{}, tick:[{}, {}], v-xyz:({}, {}, {}), pos:{}, result:{}, proc:{}", (Object)String.format("%.1f", pitch), (Object)incidenceTick, (Object)String.format("%.2f", calculateTick), (Object)String.format("%.3f", velocity.getX()), (Object)String.format("%.3f", velocity.getY()), (Object)String.format("%.3f", velocity.getZ()), (Object)String.format("%.3f", altitude), (Object)String.format("%.3f", result), (Object)throwByTick);
            logFlag = false;
            throwByTick = false;
        }
    }

    static {
        altitude = 0.0;
        result = 0.0;
        previous_y = 0.0;
        logFlag = false;
        throwByTick = false;
        counter = 0;
    }
}
