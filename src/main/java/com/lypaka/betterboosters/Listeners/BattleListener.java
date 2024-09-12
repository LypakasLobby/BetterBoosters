package com.lypaka.betterboosters.Listeners;

import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BattleListener {

    @SubscribeEvent
    public void onBattlePre (BattleStartedEvent.Pre event) {

        BattleController bc = event.getBattleController();
        WildPixelmonParticipant wpp;
        PlayerParticipant pp;
        if (bc.participants.get(0) instanceof PlayerParticipant && bc.participants.get(1) instanceof WildPixelmonParticipant) {

            wpp = (WildPixelmonParticipant) bc.participants.get(1);
            pp = (PlayerParticipant) bc.participants.get(0);

        } else if (bc.participants.get(0) instanceof WildPixelmonParticipant && bc.participants.get(1) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bc.participants.get(0);
            pp = (PlayerParticipant) bc.participants.get(1);

        } else {

            return;

        }

        Pokemon pokemon = wpp.controlledPokemon.get(0).pokemon;
        ServerPlayerEntity player = pp.player;

        if (pokemon.getPersistentData().contains("LockedTo")) {

            String uuidString = pokemon.getPersistentData().getString("LockedTo");
            if (!uuidString.equalsIgnoreCase(player.getUniqueID().toString())) {

                event.setCanceled(true);

            }

        }

    }

}
