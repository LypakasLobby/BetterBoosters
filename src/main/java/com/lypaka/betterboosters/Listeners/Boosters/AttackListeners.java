package com.lypaka.betterboosters.Listeners.Boosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterTask;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.Boosters.DefaultBoosters.AttackBooster;
import com.lypaka.betterboosters.Boosters.GlobalBooster;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AttackListeners {

    @SubscribeEvent
    public void onCrit (AttackEvent.CriticalHit event) {

        PixelmonWrapper pokemon = event.user;
        double current = event.critMultiplier;
        if (pokemon.getPlayerOwner() != null) {

            ServerPlayerEntity player = pokemon.getPlayerOwner();
            if (event.target.getPlayerOwner() != null) return; // we don't want this triggering in player vs player battles

            // Checking global boosters first
            for (Map.Entry<String, GlobalBooster> entry : BoosterUtils.globalBoosters.entrySet()) {

                GlobalBooster booster = entry.getValue();
                if (booster.isActive()) {

                    for (Booster b : booster.getBoosters()) {

                        if (b instanceof AttackBooster) {

                            AttackBooster attackBooster = (AttackBooster) b;
                            if (attackBooster.isCritEnabled()) {

                                if (RandomHelper.getRandomChance(attackBooster.getCritChance())) {

                                    List<String> permissions = booster.getPermissions();
                                    boolean hasPermission = true;
                                    for (String p : permissions) {

                                        if (!PermissionHandler.hasPermission(player, p)) {

                                            hasPermission = false;
                                            break;

                                        }

                                    }

                                    if (!hasPermission) break;

                                    String[] modifier = attackBooster.getCritModifier().split(" ");
                                    String function = modifier[0];
                                    double amount = Double.parseDouble(modifier[1]);
                                    double value;
                                    if (function.equalsIgnoreCase("add")) {

                                        value = amount + current;

                                    } else {

                                        value = amount * current;

                                    }
                                    event.setCrit(RandomHelper.getRandomChance(value));
                                    return;

                                }

                            }

                        }

                    }

                }

            }

            // checking if the player has an Attack Booster active
            for (BoosterTask tasks : BoosterUtils.activeTasks) {

                if (tasks.getBooster() instanceof AttackBooster) {

                    List<UUID> uuids = tasks.playerList;
                    if (uuids.contains(player.getUniqueID())) {

                        Booster b = tasks.getBooster();
                        AttackBooster attackBooster = (AttackBooster) b;
                        if (attackBooster.isCritEnabled()) {

                            if (RandomHelper.getRandomChance(attackBooster.getCritChance())) {

                                String[] modifier = attackBooster.getCritModifier().split(" ");
                                String function = modifier[0];
                                double amount = Double.parseDouble(modifier[1]);
                                double value;
                                if (function.equalsIgnoreCase("add")) {

                                    value = amount + current;

                                } else {

                                    value = amount * current;

                                }
                                event.setCrit(RandomHelper.getRandomChance(value));
                                return;

                            }

                        }

                    }
                    return;

                }

            }

        }

    }

    @SubscribeEvent
    public void onSTAB (AttackEvent.Stab event) {

        PixelmonWrapper pokemon = event.user;
        if (pokemon.getPlayerOwner() != null) {

            ServerPlayerEntity player = pokemon.getPlayerOwner();
            if (event.target.getPlayerOwner() != null) return; // we don't want this triggering in player vs player battles

            // Checking global boosters first
            for (Map.Entry<String, GlobalBooster> entry : BoosterUtils.globalBoosters.entrySet()) {

                GlobalBooster booster = entry.getValue();
                if (booster.isActive()) {

                    for (Booster b : booster.getBoosters()) {

                        if (b instanceof AttackBooster) {

                            AttackBooster attackBooster = (AttackBooster) b;
                            if (attackBooster.isSTABEnabled()) {

                                List<String> permissions = booster.getPermissions();
                                boolean hasPermission = true;
                                for (String p : permissions) {

                                    if (!PermissionHandler.hasPermission(player, p)) {

                                        hasPermission = false;
                                        break;

                                    }

                                }

                                if (!hasPermission) break;

                                if (!event.isStabbing()) {

                                    if (RandomHelper.getRandomChance(attackBooster.getSTABChance())) {

                                        event.setStabbing(true);

                                    }

                                }
                                return;

                            }

                        }

                    }

                }

            }

            // checking if the player has an Attack Booster active
            for (BoosterTask tasks : BoosterUtils.activeTasks) {

                if (tasks.getBooster() instanceof AttackBooster) {

                    List<UUID> uuids = tasks.playerList;
                    if (uuids.contains(player.getUniqueID())) {

                        Booster b = tasks.getBooster();
                        AttackBooster attackBooster = (AttackBooster) b;
                        if (attackBooster.isSTABEnabled()) {

                            if (!event.isStabbing()) {

                                if (RandomHelper.getRandomChance(attackBooster.getSTABChance())) {

                                    event.setStabbing(true);
                                    return;

                                }

                            }

                        }

                    }
                    return;

                }

            }

        }

    }

    @SubscribeEvent
    public void onTypeEffectiveness (AttackEvent.TypeEffectiveness event) {

        PixelmonWrapper pokemon = event.user;
        if (pokemon.getPlayerOwner() != null) {

            ServerPlayerEntity player = pokemon.getPlayerOwner();
            if (event.target.getPlayerOwner() != null) return; // we don't want this triggering in player vs player battles

            // Checking global boosters first
            for (Map.Entry<String, GlobalBooster> entry : BoosterUtils.globalBoosters.entrySet()) {

                GlobalBooster booster = entry.getValue();
                if (booster.isActive()) {

                    for (Booster b : booster.getBoosters()) {

                        if (b instanceof AttackBooster) {

                            AttackBooster attackBooster = (AttackBooster) b;
                            if (attackBooster.isEffectivenessEnabled()) {

                                List<String> permissions = booster.getPermissions();
                                boolean hasPermission = true;
                                for (String p : permissions) {

                                    if (!PermissionHandler.hasPermission(player, p)) {

                                        hasPermission = false;
                                        break;

                                    }

                                }

                                if (!hasPermission) break;

                                if (event.getEffectiveness() != Effectiveness.Super) {

                                    if (RandomHelper.getRandomChance(attackBooster.getEffectivenessChance())) {

                                        event.setEffectiveness(Effectiveness.Super);

                                    }

                                }
                                return;

                            }

                        }

                    }

                }

            }

            // checking if the player has an Attack Booster active
            for (BoosterTask tasks : BoosterUtils.activeTasks) {

                if (tasks.getBooster() instanceof AttackBooster) {

                    List<UUID> uuids = tasks.playerList;
                    if (uuids.contains(player.getUniqueID())) {

                        Booster b = tasks.getBooster();
                        AttackBooster attackBooster = (AttackBooster) b;
                        if (attackBooster.isEffectivenessEnabled()) {

                            if (event.getEffectiveness() != Effectiveness.Super) {

                                if (RandomHelper.getRandomChance(attackBooster.getEffectivenessChance())) {

                                    event.setEffectiveness(Effectiveness.Super);

                                }

                            }
                            return;

                        }

                    }
                    return;

                }

            }

        }

    }

}
