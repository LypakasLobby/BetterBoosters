package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.API.BoosterPauseEvent;
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

public class PauseCommand {

    public PauseCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("pause")
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

                                                                    BoosterPauseEvent event = new BoosterPauseEvent(player, booster);
                                                                    MinecraftForge.EVENT_BUS.post(event);
                                                                    if (!event.isCanceled()) {

                                                                        BoosterUtils.pauseBooster(player, booster);
                                                                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Paused")
                                                                                .replace("%booster%", booster.getName())
                                                                        ), player.getUniqueID());

                                                                    } else {

                                                                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Paused-Failed")
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
