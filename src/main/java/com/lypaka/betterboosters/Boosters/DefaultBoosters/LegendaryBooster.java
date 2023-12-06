package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class LegendaryBooster extends Booster {

    private final boolean lockToPlayer;

    public LegendaryBooster (double chance, Map<String, Integer> timerMap, boolean lockToPlayer) {

        super("Legendary", chance, timerMap);
        this.lockToPlayer = lockToPlayer;

    }

    public void create() {

        BoosterUtils.boosterMap.put("Legendary", this);

    }

    public boolean locksToPlayer() {

        return this.lockToPlayer;

    }

}
