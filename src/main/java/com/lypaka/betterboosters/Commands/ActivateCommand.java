package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Map;

public class ActivateCommand {

    public ActivateCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("activate")
                                            .then(
                                                    Commands.argument("booster", StringArgumentType.word())
                                                            .suggests(BetterBoostersCommand.BOOSTERS_WITH_RANDOM)
                                                            .then(
                                                                    Commands.argument("target", EntityArgument.players())
                                                                            .then(
                                                                                    Commands.argument("time", IntegerArgumentType.integer(0))
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "betterboosters.command.admin")) {

                                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                                        return 1;

                                                                                                    }

                                                                                                }

                                                                                                String boosterArg = StringArgumentType.getString(c, "booster");
                                                                                                Booster booster = BoosterUtils.getFromName(boosterArg);
                                                                                                if (booster == null) {

                                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid booster!"));
                                                                                                    return 1;

                                                                                                }

                                                                                                int time = IntegerArgumentType.getInteger(c, "time");
                                                                                                int current = 0;
                                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "target");
                                                                                                if (time == 0) {

                                                                                                    boolean found = false;
                                                                                                    for (Map.Entry<String, Integer> entry : booster.getTimerMap().entrySet()) {

                                                                                                        if (!entry.getKey().equalsIgnoreCase("default")) {

                                                                                                            if (PermissionHandler.hasPermission(target, entry.getKey())) {

                                                                                                                found = true;
                                                                                                                current = entry.getValue();
                                                                                                                break;

                                                                                                            }

                                                                                                        }

                                                                                                    }
                                                                                                    if (!found) {

                                                                                                        current = booster.getDefaultTimer();

                                                                                                    }

                                                                                                } else {

                                                                                                    current = time;

                                                                                                }

                                                                                                BoosterUtils.activateBooster(target.getUniqueID(), booster, false, current, time);

                                                                                                return 0;

                                                                                            })
                                                                            )
                                                            )
                                            )
                            )
            );

        }

    }

}
