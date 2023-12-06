package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class EXPBooster extends Booster {

    public EXPBooster (String modifier, Map<String, Integer> timerMap) {

        super("EXP", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("EXP", this);

    }

}
