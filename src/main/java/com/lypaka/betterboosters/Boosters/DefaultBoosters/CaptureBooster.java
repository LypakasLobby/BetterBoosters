package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class CaptureBooster extends Booster {

    public CaptureBooster (String modifier, Map<String, Integer> timerMap) {

        super("Capture", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("Capture", this);

    }

}
