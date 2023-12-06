package com.lypaka.betterboosters.Boosters.ExtraBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class LuresBooster extends Booster {

    public LuresBooster (String modifier, Map<String, Integer> timerMap) {

        super("Lures", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("Lures", this);

    }

}
