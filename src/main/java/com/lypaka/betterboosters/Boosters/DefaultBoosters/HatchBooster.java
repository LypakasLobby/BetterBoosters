package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class HatchBooster extends Booster {

    public HatchBooster (String modifier, Map<String, Integer> timerMap) {

        super("Hatch", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("Hatch", this);

    }

}
