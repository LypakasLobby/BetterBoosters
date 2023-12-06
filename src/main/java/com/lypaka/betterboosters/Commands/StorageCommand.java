package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.betterboosters.GUIs.StorageGUI;
import com.lypaka.lypakautils.FancyText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.Arrays;

public class StorageCommand {

    private static final SuggestionProvider<CommandSource> OPTIONS = (context, builder) ->
            ISuggestionProvider.suggest(Arrays.asList("menu", "deposit"), builder);

    public StorageCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("storage")
                                            .then(
                                                    Commands.literal("menu")
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    try {

                                                                        StorageGUI.open(player);

                                                                    } catch (ObjectMappingException e) {

                                                                        e.printStackTrace();

                                                                    }

                                                                }

                                                                return 1;

                                                            })
                                            )
                                            .then(
                                                    Commands.literal("deposit")
                                                            .then(
                                                                    Commands.argument("amount", IntegerArgumentType.integer(0))
                                                                            .executes(c -> {

                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                    if (player.getHeldItem(Hand.MAIN_HAND).getDisplayName().getString().contains("tile.air")) return 1;
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
                                                                                            return 1;

                                                                                        }

                                                                                        int amount = IntegerArgumentType.getInteger(c, "amount");
                                                                                        Account account = AccountHandler.accountMap.get(player.getUniqueID());
                                                                                        if (amount == 0) amount = player.getHeldItem(Hand.MAIN_HAND).getCount();

                                                                                        account.getStoredMap().put(b.getName(), amount);
                                                                                        player.getHeldItem(Hand.MAIN_HAND).setCount(player.getHeldItem(Hand.MAIN_HAND).getCount() - amount);
                                                                                        String message = ConfigGetters.messagesMap.get("Booster-Deposited");
                                                                                        if (amount == 1) {

                                                                                            message = message.replace("Boosters", "Booster");

                                                                                        }
                                                                                        player.sendMessage(FancyText.getFormattedText(message.replace("%booster%", b.getName())), player.getUniqueID());

                                                                                    }

                                                                                }

                                                                                return 0;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
