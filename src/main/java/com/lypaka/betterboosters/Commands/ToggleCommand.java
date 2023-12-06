package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterBar;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerBossInfo;

import java.util.List;
import java.util.Map;

public class ToggleCommand {

    public ToggleCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterBoostersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("togglebars")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (PermissionHandler.hasPermission(player, "betterboosters.command.toggle")) {

                                                        Account account = AccountHandler.accountMap.get(player.getUniqueID());
                                                        boolean on = account.doesUseBars();
                                                        if (on) {

                                                            account.setUseBars(false);
                                                            // removes any currently visible boss bars when the command is typed to turn them off
                                                            if (BoosterUtils.activeBoosters.containsKey(player.getUniqueID())) {

                                                                List<Booster> boosters = BoosterUtils.activeBoosters.get(player.getUniqueID());
                                                                for (Booster b : boosters) {

                                                                    BoosterUtils.pauseBooster(player, b);
                                                                    if (BoosterUtils.barMap.containsKey(player.getUniqueID())) {

                                                                        Map<String, ServerBossInfo> bMap = BoosterUtils.barMap.get(player.getUniqueID());
                                                                        bMap.entrySet().removeIf(barEntry -> {

                                                                            if (barEntry.getKey().equalsIgnoreCase(b.getName())) {

                                                                                ServerBossInfo bar = bMap.get(b.getName());
                                                                                bar.removePlayer(player);
                                                                                bar.setVisible(false);
                                                                                return true;

                                                                            }

                                                                            return false;

                                                                        });
                                                                        BoosterUtils.barMap.put(player.getUniqueID(), bMap);

                                                                    }

                                                                }


                                                            }

                                                        } else {

                                                            account.setUseBars(true);
                                                            if (BoosterUtils.activeBoosters.containsKey(player.getUniqueID())) {

                                                                List<Booster> boosters = BoosterUtils.activeBoosters.get(player.getUniqueID());
                                                                for (Booster b : boosters) {

                                                                    BoosterBar bar = new BoosterBar(player.getUniqueID(), b.getName());
                                                                    bar.buildBar();

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                                return 1;

                                            })
                            )
            );

        }

    }

}
