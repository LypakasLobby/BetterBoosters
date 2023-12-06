package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.API.BoosterResumeEvent;
import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class ResumeCommand {

    public ResumeCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("resume")
                                            .then(
                                                    Commands.argument("booster", StringArgumentType.word())
                                                            .suggests(BetterBoostersCommand.BOOSTERS)
                                                            .executes(c -> {

                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                    String boosterArg = StringArgumentType.getString(c, "booster");
                                                                    Booster booster = BoosterUtils.getFromName(boosterArg);
                                                                    if (booster == null) {

                                                                        player.sendMessage(FancyText.getFormattedText("&cInvalid booster name!"), player.getUniqueID());
                                                                        return 1;

                                                                    }

                                                                    BoosterResumeEvent event = new BoosterResumeEvent(player, booster);
                                                                    MinecraftForge.EVENT_BUS.post(event);
                                                                    if (!event.isCanceled()) {

                                                                        Account account = AccountHandler.accountMap.get(player.getUniqueID());
                                                                        if (!account.getBoosterMap().containsKey(booster.getName())) {

                                                                            player.sendMessage(FancyText.getFormattedText("&eYou do not have this booster currently active!"), player.getUniqueID());
                                                                            return 1;

                                                                        }

                                                                        Map<String, Integer> boosterMap = account.getBoosterMap().get(booster.getName());
                                                                        int current = boosterMap.get("Current");
                                                                        int max = boosterMap.get("Max");
                                                                        BoosterUtils.activateBooster(player.getUniqueID(), booster, false, current, max);
                                                                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Resumed")
                                                                                .replace("%booster%", booster.getName())
                                                                        ), player.getUniqueID());

                                                                    } else {

                                                                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Resumed-Failed")
                                                                                .replace("%booster%", booster.getName())
                                                                        ), player.getUniqueID());

                                                                    }

                                                                }

                                                                return 0;

                                                            })
                                            )
                            )
            );

        }

    }

}
