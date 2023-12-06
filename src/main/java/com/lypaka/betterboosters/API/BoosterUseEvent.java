package com.lypaka.betterboosters.API;

import com.lypaka.betterboosters.Boosters.Booster;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class BoosterUseEvent extends Event {

    private final ServerPlayerEntity player;
    private Booster booster;
    private int timer;

    public BoosterUseEvent (ServerPlayerEntity player, Booster booster, int timer) {

        this.player = player;
        this.booster = booster;
        this.timer = timer;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Booster getBooster() {

        return this.booster;

    }

    public void setBooster (Booster booster) {

        this.booster = booster;

    }

    public int getTimer() {

        return this.timer;

    }

    public void setTimer (int timer) {

        this.timer = timer;

    }

}
