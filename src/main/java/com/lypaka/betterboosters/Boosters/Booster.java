package com.lypaka.betterboosters.Boosters;

import java.util.Map;

public abstract class Booster {

    private final String name;
    private double chance = -1;
    private String modifier = null;
    private final Map<String, Integer> timerMap;

    public Booster (String name, double chance, Map<String, Integer> timerMap) {

        this.name = name;
        this.chance = chance;
        this.timerMap = timerMap;

    }

    public Booster (String name, String modifier, Map<String, Integer> timerMap) {

        this.name = name;
        this.modifier = modifier;
        this.timerMap = timerMap;

    }

    public Booster (String name, double chance, String modifier, Map<String, Integer> timerMap) {

        this.name = name;
        this.chance = chance;
        this.modifier = modifier;
        this.timerMap = timerMap;

    }

    public Booster (String name, Map<String, Integer> timerMap) {

        this.name = name;
        this.timerMap = timerMap;

    }

    public String getName() {

        return this.name;

    }

    public double getChance() {

        return this.chance;

    }

    public boolean hasChance() {

        return this.chance > -1;

    }

    public String getModifier() {

        return this.modifier;

    }

    public boolean hasModifier() {

        return this.modifier != null;

    }

    public Map<String, Integer> getTimerMap() {

        return this.timerMap;

    }

    public int getDefaultTimer() {

        return this.timerMap.get("default");

    }

}
