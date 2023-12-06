package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DeactivateCommand {

    public DeactivateCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("deactivate")
                                            .then(
                                                    Commands.argument("booster", StringArgumentType.word())
                                                            .suggests(BetterBoostersCommand.BOOSTERS)
                                                            .then(
                                                                    Commands.argument("target", EntityArgument.players())
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

                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid booster name!"));
                                                                                    return 1;

                                                                                }

                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "target");
                                                                                BoosterUtils.deactivateBooster(booster, target.getUniqueID());
                                                                                return 0;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
