package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class ShinyBooster extends Booster {

    public ShinyBooster (double chance, Map<String, Integer> timerMap) {

        super("Shiny", chance, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("Shiny", this);

    }

}
