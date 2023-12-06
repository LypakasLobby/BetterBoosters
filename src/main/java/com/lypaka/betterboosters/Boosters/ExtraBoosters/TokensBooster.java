package com.lypaka.betterboosters.Boosters.ExtraBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class TokensBooster extends Booster {

    public TokensBooster (String modifier, Map<String, Integer> timerMap) {

        super("Tokens", modifier, timerMap);

    }

    public void create() {

        BoosterUtils.boosterMap.put("Tokens", this);

    }

}
