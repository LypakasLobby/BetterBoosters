package com.lypaka.betterboosters.Boosters;

import com.lypaka.betterboosters.API.BoosterExpireEvent;
import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class BoosterTask extends TimerTask {

    public List<UUID> playerList = new ArrayList<>();
    public Timer timer;
    private final Booster booster;

    public BoosterTask (Booster booster) {

        this.booster = booster;

    }

    public Booster getBooster() {

        return this.booster;

    }

    public static int getCurrent (UUID uuid, Booster booster) {

        return BoosterUtils.currentMap.get(uuid).get(booster.getName());

    }

    public static int getMax (UUID uuid, Booster booster) {

        return BoosterUtils.maxMap.get(uuid).get(booster.getName());

    }

    @Override
    public void run() {

        if (this.playerList.isEmpty()) {

            this.cancel();
            this.timer.cancel();
            this.timer = new Timer();
            BoosterUtils.activeTasks.removeIf(t -> t == this);

        } else {

            this.playerList.removeIf(entry -> {

                ServerPlayerEntity player = JoinListener.playerMap.get(entry);
                int current = BoosterUtils.currentMap.get(entry).get(this.booster.getName());
                int max = BoosterUtils.maxMap.get(entry).get(this.booster.getName());
                if (current == 0) {

                    Map<String, Integer> cMapEntry = BoosterUtils.currentMap.get(entry);
                    cMapEntry.entrySet().removeIf(cEntry -> cEntry.getKey().equalsIgnoreCase(this.booster.getName()));
                    BoosterUtils.currentMap.put(entry, cMapEntry);
                    Map<String, Integer> mMapEntry = BoosterUtils.maxMap.get(entry);
                    mMapEntry.entrySet().removeIf(mEntry -> mEntry.getKey().equalsIgnoreCase(this.booster.getName()));
                    BoosterUtils.maxMap.put(entry, mMapEntry);

                    // handle the removal of the boss bar
                    if (BoosterUtils.barMap.containsKey(entry)) {

                        Map<String, ServerBossInfo> barMap = BoosterUtils.barMap.get(entry);
                        barMap.entrySet().removeIf(barEntry -> {

                            if (barEntry.getKey().equalsIgnoreCase(this.booster.getName())) {

                                ServerBossInfo bar = barMap.get(this.booster.getName());
                                bar.removePlayer(player);
                                bar.setVisible(false);
                                return true;

                            }

                            return false;

                        });
                        BoosterUtils.barMap.put(entry, barMap);

                    }

                    BoosterExpireEvent event = new BoosterExpireEvent(player, this.booster);
                    MinecraftForge.EVENT_BUS.post(event);

                    return true;

                }

                Map<String, Integer> tempMap = new HashMap<>();
                if (BoosterUtils.currentMap.containsKey(entry)) {

                    tempMap = BoosterUtils.currentMap.get(entry);

                }
                int updated = current - 1;
                tempMap.put(this.booster.getName(), updated);
                BoosterUtils.currentMap.put(entry, tempMap);

                // handle the decrementing of the boss bar
                if (BoosterUtils.barMap.containsKey(entry)) {

                    Map<String, ServerBossInfo> barMap = BoosterUtils.barMap.get(entry);
                    if (barMap.containsKey(this.booster.getName())) {

                        ServerBossInfo bar = barMap.get(this.booster.getName());
                        float percent = (float) current / max;
                        bar.setPercent(percent);
                        String name = ConfigGetters.boosterBarInfo.get(this.booster.getName()).get("Title");
                        if (current == 1) {

                            name = name.replace("seconds", "second");

                        }
                        bar.setName(FancyText.getFormattedText(name.replace("%time%", String.valueOf(current))));
                        barMap.put(this.booster.getName(), bar);
                        BoosterUtils.barMap.put(entry, barMap);

                    }

                }

                return false;

            });

            if (this.playerList.isEmpty()) {

                this.cancel();
                this.timer.cancel();
                this.timer = new Timer();
                BoosterUtils.activeTasks.removeIf(t -> t == this);
                BetterBoosters.logger.info(this.booster.getName() + " Booster has no players, stopping task!");

            }

        }

    }

}
