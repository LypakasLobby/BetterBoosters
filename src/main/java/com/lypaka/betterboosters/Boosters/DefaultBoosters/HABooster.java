package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class HABooster extends Booster {

    public HABooster (double chance, Map<String, Integer> timerMap) {

        super("HA", chance, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("HA", this);

    }

}
