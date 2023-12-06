package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class EVBooster extends Booster {

    public EVBooster (String modifier, Map<String, Integer> timerMap) {

        super("EV", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("EV", this);

    }

}
