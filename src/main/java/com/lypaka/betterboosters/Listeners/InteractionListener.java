package com.lypaka.betterboosters.Listeners;

import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.Map;

public class InteractionListener {

    @SubscribeEvent
    public void onItemUse (PlayerInteractEvent.RightClickItem event) {

        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getHand() == Hand.OFF_HAND) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if (player.getHeldItem(Hand.MAIN_HAND).getDisplayName().getString().contains("Booster")) {

            Booster b = null;
            for (String s : BetterBoosters.boosters) {

                if (player.getHeldItem(Hand.MAIN_HAND).getDisplayName().getUnformattedComponentText().equalsIgnoreCase(FancyText.getFormattedText(ConfigGetters.boosterDisplayNames.get(s)).getUnformattedComponentText())) {

                    b = BoosterUtils.getFromName(s);
                    break;

                }

            }


            if (b == null) {

                player.sendMessage(FancyText.getFormattedText("&cThe item in your hand is not a valid, registered booster!"), player.getUniqueID());
                return;

            }

            if (player.isSneaking()) {

                int amount = player.getHeldItem(Hand.MAIN_HAND).getCount();
                Account account = AccountHandler.accountMap.get(player.getUniqueID());
                account.getStoredMap().put(b.getName(), amount);
                player.getHeldItem(Hand.MAIN_HAND).setCount(player.getHeldItem(Hand.MAIN_HAND).getCount() - amount);
                String message = ConfigGetters.messagesMap.get("Booster-Deposited");
                if (amount == 1) {

                    message = message.replace("Boosters", "Booster");

                }
                player.sendMessage(FancyText.getFormattedText(message.replace("%booster%", b.getName())), player.getUniqueID());

            } else {

                int timer = b.getDefaultTimer();
                for (Map.Entry<String, Integer> entry : b.getTimerMap().entrySet()) {

                    if (!entry.getKey().equalsIgnoreCase("default")) {

                        if (PermissionHandler.hasPermission(player, entry.getKey())) {

                            timer = entry.getValue();
                            break;

                        }

                    }

                }

                BoosterUtils.activateBooster(player.getUniqueID(), b, true, timer, timer);

            }

        }

    }

}
