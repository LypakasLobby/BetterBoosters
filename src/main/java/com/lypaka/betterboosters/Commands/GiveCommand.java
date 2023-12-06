package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class GiveCommand {

    public GiveCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("give")
                                            .then(
                                                    Commands.argument("target", EntityArgument.players())
                                                            .then(
                                                                    Commands.argument("booster", StringArgumentType.word())
                                                                            .suggests(BetterBoostersCommand.BOOSTERS_WITH_RANDOM)
                                                                            .then(
                                                                                    Commands.argument("quantity", IntegerArgumentType.integer(1))
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "betterboosters.command.admin")) {

                                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                                        return 1;

                                                                                                    }

                                                                                                }

                                                                                                String boosterArg = StringArgumentType.getString(c, "booster");
                                                                                                int quantity = Math.min(64, IntegerArgumentType.getInteger(c, "quantity"));

                                                                                                Booster booster = BoosterUtils.getFromName(boosterArg);
                                                                                                if (booster == null) {

                                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid booster!"));
                                                                                                    return 1;

                                                                                                }

                                                                                                ItemStack boosterItem = BoosterUtils.buildBoosterItem(booster, quantity);
                                                                                                ServerPlayerEntity player = EntityArgument.getPlayer(c, "target");
                                                                                                player.addItemStackToInventory(boosterItem);
                                                                                                String message = ConfigGetters.messagesMap.get("Booster-Given")
                                                                                                        .replace("%booster%", booster.getName())
                                                                                                        .replace("%amount%", String.valueOf(quantity));
                                                                                                if (quantity == 1) {

                                                                                                    message = message.replace("Boosters", "Booster");

                                                                                                }
                                                                                                player.sendMessage(FancyText.getFormattedText(message), player.getUniqueID());
                                                                                                return 0;

                                                                                            })
                                                                            )
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

                                                                                ItemStack boosterItem = BoosterUtils.buildBoosterItem(booster, 1);
                                                                                ServerPlayerEntity player = EntityArgument.getPlayer(c, "target");
                                                                                player.addItemStackToInventory(boosterItem);
                                                                                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Given")
                                                                                        .replace("%amount%", String.valueOf(1))
                                                                                        .replace("%booster%", booster.getName())
                                                                                        .replace("Boosters", "Booster")
                                                                                ), player.getUniqueID());
                                                                                return 0;

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
