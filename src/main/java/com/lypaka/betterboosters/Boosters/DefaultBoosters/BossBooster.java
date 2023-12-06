package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class BossBooster extends Booster {

    private final Map<String, Double> bossSpawnRates;

    public BossBooster (Map<String, Integer> timerMap, Map<String, Double> bossSpawnRates, double chance) {

        super("Boss", chance, timerMap);
        this.bossSpawnRates = bossSpawnRates;

    }

    public void create() {

        BoosterUtils.boosterMap.put("Boss", this);

    }

    public Map<String, Double> getBossSpawnRates() {

        return this.bossSpawnRates;

    }

}
