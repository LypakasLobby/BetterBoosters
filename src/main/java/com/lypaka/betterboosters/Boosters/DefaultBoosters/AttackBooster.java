package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.List;
import java.util.Map;

public class AttackBooster extends Booster {

    private final List<String> npcBlacklist;
    private final double critChance;
    private final boolean critEnabled;
    private final String critModifier;
    private final double stabChance;
    private final boolean stabEnabled;
    private final double effectivenessChance;
    private final boolean effectivenessEnabled;

    public AttackBooster (Map<String, Integer> timerMap, List<String> npcBlacklist, double critChance, boolean critEnabled, String critModifier, double stabChance, boolean stabEnabled,
                          double effectivenessChance, boolean effectivenessEnabled) {

        super("Attack", timerMap);
        this.npcBlacklist = npcBlacklist;
        this.critChance = critChance;
        this.critEnabled = critEnabled;
        this.critModifier = critModifier;
        this.stabChance = stabChance;
        this.stabEnabled = stabEnabled;
        this.effectivenessChance = effectivenessChance;
        this.effectivenessEnabled = effectivenessEnabled;

    }

    public void create() {

        BoosterUtils.boosterMap.put("Attack", this);

    }

    public List<String> getNPCBlacklist() {

        return this.npcBlacklist;

    }

    public double getCritChance() {

        return this.critChance;

    }

    public boolean isCritEnabled() {

        return this.critEnabled;

    }

    public String getCritModifier() {

        return this.critModifier;

    }

    public double getSTABChance() {

        return this.stabChance;

    }

    public boolean isSTABEnabled() {

        return this.stabEnabled;

    }

    public double getEffectivenessChance() {

        return this.effectivenessChance;

    }

    public boolean isEffectivenessEnabled() {

        return this.effectivenessEnabled;

    }

}
