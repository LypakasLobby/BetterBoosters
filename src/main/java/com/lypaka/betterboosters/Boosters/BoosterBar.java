package com.lypaka.betterboosters.Boosters;

import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterBar {

    private final UUID player;
    private final String booster;

    public BoosterBar (UUID player, String booster) {

        this.player = player;
        this.booster = booster;

    }

    public void buildBar() {

        Map<String, Map<String, String>> map = ConfigGetters.boosterBarInfo;
        if (!BoosterUtils.barMap.containsKey(this.player)) {

            Map<String, ServerBossInfo> innerMap = new HashMap<>();
            if (map.containsKey(this.booster)) {

                Map<String, String> info = map.get(this.booster);
                ServerBossInfo bar = new ServerBossInfo(
                        FancyText.getFormattedText(info.get("Title")),
                        getColorFromName(info.get("Color")),
                        BossInfo.Overlay.PROGRESS
                );

                innerMap.put(this.booster, bar);
                BoosterUtils.barMap.put(this.player, innerMap);
                bar.addPlayer(JoinListener.playerMap.get(this.player));
                bar.setPercent(100);
                bar.setVisible(true);

            }

        } else {

            Map<String, ServerBossInfo> innerMap = BoosterUtils.barMap.get(this.player);
            if (!innerMap.containsKey(this.booster)) {

                if (map.containsKey(this.booster)) {

                    Map<String, String> info = map.get(this.booster);
                    ServerBossInfo bar = new ServerBossInfo(
                            FancyText.getFormattedText(info.get("Title")),
                            getColorFromName(info.get("Color")),
                            BossInfo.Overlay.PROGRESS
                    );

                    innerMap.put(this.booster, bar);
                    BoosterUtils.barMap.put(this.player, innerMap);
                    bar.addPlayer(JoinListener.playerMap.get(this.player));
                    bar.setPercent(100);
                    bar.setVisible(true);

                }

            }

        }

    }

    private static BossInfo.Color getColorFromName (String name) {

        return BossInfo.Color.byName(name.toLowerCase());

    }

}
