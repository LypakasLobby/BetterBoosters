package com.lypaka.betterboosters.Listeners.Boosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterTask;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.Boosters.DefaultBoosters.CaptureBooster;
import com.lypaka.betterboosters.Boosters.GlobalBooster;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CaptureListener {

    @SubscribeEvent
    public void onCapture (CaptureEvent.StartCapture event) {

        int current = event.getCaptureValues().getCatchRate();
        ServerPlayerEntity player = event.getPlayer();

        // Checking global boosters first
        for (Map.Entry<String, GlobalBooster> entry : BoosterUtils.globalBoosters.entrySet()) {

            GlobalBooster booster = entry.getValue();
            if (booster.isActive()) {

                for (Booster b : booster.getBoosters()) {

                    if (b instanceof CaptureBooster) {

                        List<String> permissions = booster.getPermissions();
                        boolean hasPermission = true;
                        for (String p : permissions) {

                            if (!PermissionHandler.hasPermission(player, p)) {

                                hasPermission = false;
                                break;

                            }

                        }

                        if (!hasPermission) break;

                        String[] modifier = b.getModifier().split(" ");
                        String function = modifier[0];
                        double amount = Double.parseDouble(modifier[1]);
                        if (function.equalsIgnoreCase("add")) {

                            event.getCaptureValues().setCatchRate((int) (amount + current));

                        } else {

                            event.getCaptureValues().setCatchRate((int) (amount * current));

                        }
                        return;

                    }

                }

            }

        }

        // checking if the player has a Capture Booster active
        for (BoosterTask tasks : BoosterUtils.activeTasks) {

            if (tasks.getBooster() instanceof CaptureBooster) {

                List<UUID> uuids = tasks.playerList;
                if (uuids.contains(player.getUniqueID())) {

                    Booster b = tasks.getBooster();
                    String[] modifier = b.getModifier().split(" ");
                    String function = modifier[0];
                    double amount = Double.parseDouble(modifier[1]);
                    if (function.equalsIgnoreCase("add")) {

                        event.getCaptureValues().setCatchRate((int) (amount + current));

                    } else {

                        event.getCaptureValues().setCatchRate((int) (amount * current));

                    }

                }
                return;

            }

        }

    }

}
