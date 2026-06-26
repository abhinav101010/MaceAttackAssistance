/*
 * Decompiled with CFR 0.152.
 */
package com.papack.maceattackassistance.client;

import com.papack.maceattackassistance.client.JobManager;

public enum StatusType {
    VERY_HIGH_SPEED{

        @Override
        public void execute(int value) {
            JobManager.veryHighSpeed(value);
        }
    }
    ,
    HIGH_SPEED{

        @Override
        public void execute(int value) {
            JobManager.highSpeed(value);
        }
    }
    ,
    STUN_SLAM{

        @Override
        public void execute(int value) {
            JobManager.stunSlam(value);
        }
    }
    ,
    HOT_SWAP{

        @Override
        public void execute(int value) {
            JobManager.hotSwap(value);
        }
    }
    ,
    BREACH{

        @Override
        public void execute(int value) {
            JobManager.breach(value);
        }
    }
    ,
    AIR_BREACH{

        @Override
        public void execute(int value) {
            JobManager.airBreach(value);
        }
    }
    ,
    AIR_SHIELD_BREACH{

        @Override
        public void execute(int value) {
            JobManager.airShieldBreach(value);
        }
    }
    ,
    STUN{

        @Override
        public void execute(int value) {
            JobManager.stun(value);
        }
    }
    ,
    MANUAL_STUN{

        @Override
        public void execute(int value) {
            JobManager.manualStun(value);
        }
    }
    ,
    MANUAL_STUN_HIGH{

        @Override
        public void execute(int value) {
            JobManager.manualStunHighSpeed(value);
        }
    }
    ,
    NORMAL{

        @Override
        public void execute(int value) {
            JobManager.normal(value);
        }
    }
    ,
    NONE{

        @Override
        public void execute(int value) {
            JobManager.none();
        }
    }
    ,
    ENDER_PEARL{

        @Override
        public void execute(int value) {
            JobManager.manualPearlCatch(value);
        }
    }
    ,
    ROCKET{

        @Override
        public void execute(int value) {
            JobManager.rocket(value);
        }
    }
    ,
    TRIGGER_KEY_ROCKET{

        @Override
        public void execute(int value) {
            JobManager.triggerKeyRocket(value);
        }
    }
    ,
    WIND_CHARGE{

        @Override
        public void execute(int value) {
            JobManager.windCharge(value);
        }
    }
    ,
    PRE_AXE{

        @Override
        public void execute(int value) {
            JobManager.preAxe(value);
        }
    }
    ,
    MANUAL_BREACH{

        @Override
        public void execute(int value) {
            JobManager.manualBreach(value);
        }
    }
    ,
    ELYTRA_BOOST{

        @Override
        public void execute(int value) {
            JobManager.elytraBoost(value);
        }
    }
    ,
    DOUBLE_TAP{

        @Override
        public void execute(int value) {
            JobManager.doubleTap(value);
        }
    }
    ,
    DOUBLE_TAP_ELYTRA_SPEAR{

        @Override
        public void execute(int value) {
            JobManager.doubleTapElytraSpear(value);
        }
    }
    ,
    DOUBLE_TAP_OFF_HAND{

        @Override
        public void execute(int value) {
            JobManager.doubleTapOffHand(value);
        }
    }
    ,
    ELYTRA_MANUAL_SWITCH_MODE{

        @Override
        public void execute(int value) {
            JobManager.elytraManualSwitchMode(value);
        }
    }
    ,
    AUTO_REFILL{

        @Override
        public void execute(int value) {
            JobManager.autoRefill(value);
        }
    }
    ,
    PEARL_CATCH{

        @Override
        public void execute(int value) {
            JobManager.autoPearlCatch(value);
        }
    }
    ,
    ONE_TICK_STUN{

        @Override
        public void execute(int value) {
            JobManager.oneTickStunSlum(value);
        }
    }
    ,
    PEARL_GRAPPLE{

        @Override
        public void execute(int value) {
            JobManager.pearlGrapple(value);
        }
    }
    ,
    INSTANT_PEARL_CATCH{

        @Override
        public void execute(int value) {
            JobManager.instantPearlCatch(value);
        }
    }
    ,
    PEARL_CATCH_DOWNWARD{

        @Override
        public void execute(int value) {
            JobManager.pearlCatchDownward(value);
        }
    }
    ,
    AUTO_SHIELD_DRAINING{

        @Override
        public void execute(int value) {
            JobManager.autoShieldDraining(value);
        }
    }
    ,
    AIR_POT{

        @Override
        public void execute(int value) {
            JobManager.airPot(value);
        }
    };


    public abstract void execute(int var1);
}
