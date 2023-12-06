package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class IVBooster extends Booster {

    public IVBooster (String modifier, Map<String, Integer> timerMap) {

        super("IV", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("IV", this);

    }

}
