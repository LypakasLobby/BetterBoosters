package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class WinningsBooster extends Booster {

    public WinningsBooster (String modifier, Map<String, Integer> timerMap) {

        super("Winnings", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("Winnings", this);

    }

}
