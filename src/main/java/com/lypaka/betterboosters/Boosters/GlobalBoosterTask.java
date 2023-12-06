package com.lypaka.betterboosters.Boosters;

import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class GlobalBoosterTask {

    public static void runGlobalTimer() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                LocalDateTime now = LocalDateTime.now();
                int nowDay = now.getDayOfMonth();
                int nowMonth = now.getMonthValue();
                int nowHour = now.getHour();
                int nowMinute = now.getMinute();
                for (Map.Entry<String, GlobalBooster> entry : BoosterUtils.globalBoosters.entrySet()) {

                    GlobalBooster booster = entry.getValue();
                    int endDay = booster.getEndDay();
                    int endHour = booster.getEndHour();
                    int endMinute = booster.getEndMinute();
                    int endMonth = booster.getEndMonth();
                    int startDay = booster.getStartDay();
                    int startHour = booster.getStartHour();
                    int startMinute = booster.getStartMinute();
                    int startMonth = booster.getStartMonth();

                    if (!booster.isActive()) {

                        if (nowMonth >= startMonth && nowMonth <= endMonth) {

                            if (nowDay >= startDay && nowDay <= endDay) {

                                if (nowHour >= startHour && nowHour <= endHour) {

                                    if (nowMinute >= startMinute && nowMinute <= endMinute) {

                                        booster.setActive(true);
                                        BetterBoosters.logger.info("Activating Global Booster: " + entry.getKey());

                                        for (Map.Entry<UUID, ServerPlayerEntity> players : JoinListener.playerMap.entrySet()) {

                                            boolean hasPermission = true;
                                            for (String p : booster.getPermissions()) {

                                                if (!PermissionHandler.hasPermission(players.getValue(), p)) {

                                                    hasPermission = false;
                                                    break;

                                                }

                                            }
                                            if (hasPermission) {

                                                for (Booster b : booster.getBoosters()) {

                                                    BoosterUtils.pauseBooster(players.getValue(), b);

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    } else {

                        if (nowMonth >= endMonth) {

                            if (nowDay >= endDay) {

                                if (nowHour >= endHour) {

                                    if (nowMinute >= endMinute) {

                                        booster.setActive(false);
                                        BetterBoosters.logger.info("Deactivating Global Booster: " + entry.getKey());

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }, 0, 1000);

    }

}
