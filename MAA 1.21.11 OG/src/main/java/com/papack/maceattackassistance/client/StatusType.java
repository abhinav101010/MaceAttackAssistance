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
            JobManager.enderPearl(value);
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
    WIND_CHARGE{

        @Override
        public void execute(int value) {
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
    INSTANT_ATTACK_INTERVAL{

        @Override
        public void execute(int value) {
            JobManager.instantAttackInterval(value);
        }
    }
    ,
    ELYTRA_BOOST{

        @Override
        public void execute(int value) {
            JobManager.elytraBoost(value);
        }
    };


    public abstract void execute(int var1);
}
