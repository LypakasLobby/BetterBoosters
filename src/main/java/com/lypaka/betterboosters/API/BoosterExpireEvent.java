package com.lypaka.betterboosters.API;

import com.lypaka.betterboosters.Boosters.Booster;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class BoosterExpireEvent extends Event {

    private final ServerPlayerEntity player;
    private final Booster booster;

    public BoosterExpireEvent (ServerPlayerEntity player, Booster booster) {

        this.player = player;
        this.booster = booster;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Booster getBooster() {

        return this.booster;

    }

}
