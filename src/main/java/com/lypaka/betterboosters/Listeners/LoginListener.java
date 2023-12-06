package com.lypaka.betterboosters.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;

public class LoginListener {

    @SubscribeEvent
    public void onJoin (PlayerEvent.PlayerLoggedInEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID uuid = player.getUniqueID();
        BetterBoosters.playerConfigManager.loadPlayer(uuid);
        Map<String, Map<String, Integer>> boosters = BetterBoosters.playerConfigManager.getPlayerConfigNode(uuid, "Boosters").getValue(new TypeToken<Map<String, Map<String, Integer>>>() {});
        Map<String, Integer> stored = BetterBoosters.playerConfigManager.getPlayerConfigNode(uuid, "Stored-Boosters").getValue(new TypeToken<Map<String, Integer>>() {});
        boolean use = BetterBoosters.playerConfigManager.getPlayerConfigNode(uuid, "Use-Bars").getBoolean();
        Account account = new Account(uuid, boosters, stored, use);
        account.create();

        if (!boosters.isEmpty()) {

            List<String> boosterNames = new ArrayList<>();
            for (Map.Entry<String, Map<String, Integer>> entry : boosters.entrySet()) {

                boosterNames.add(entry.getKey());

            }

            String[] names = boosterNames.toArray(new String[0]);
            String b = "&eHey! You have these boosters active: &a";
            if (boosterNames.size() == 1) {

                b = "&eHey! You have this booster active: &a";

            }
            player.sendMessage(FancyText.getFormattedText(b + Arrays.toString(names).replace("[", "").replace("]", "")), player.getUniqueID());
            player.sendMessage(FancyText.getFormattedText("&eTo reactivate, use \"/boosts resume <boosterName>\"!"), player.getUniqueID());

        }

    }

    @SubscribeEvent
    public void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (BoosterUtils.activeBoosters.containsKey(player.getUniqueID())) {

            List<Booster> boosters = BoosterUtils.activeBoosters.get(player.getUniqueID());
            for (Booster b : boosters) {

                BoosterUtils.pauseBooster(player, b);

            }

        }
        AccountHandler.accountMap.entrySet().removeIf(entry -> {

            if (entry.getKey().toString().equalsIgnoreCase(event.getPlayer().getUniqueID().toString())) {

                entry.getValue().save();
                return true;

            }

            return false;

        });

    }

}
