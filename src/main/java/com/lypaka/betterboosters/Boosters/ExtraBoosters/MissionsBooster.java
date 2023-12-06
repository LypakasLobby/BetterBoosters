package com.lypaka.betterboosters.Boosters.ExtraBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class MissionsBooster extends Booster {

    private final boolean runCommandsTwice;

    public MissionsBooster (double chance, String modifier, Map<String, Integer> timerMap, boolean runCommandsTwice) {

        super("Missions", chance, modifier, timerMap);
        this.runCommandsTwice = runCommandsTwice;

    }

    public void create() {

        BoosterUtils.boosterMap.put("Missions", this);

    }

    public boolean doesRunCommandsTwice() {

        return this.runCommandsTwice;

    }

}
