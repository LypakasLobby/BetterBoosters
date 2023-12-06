package com.lypaka.betterboosters.Accounts;

import com.lypaka.betterboosters.BetterBoosters;

import java.util.Map;
import java.util.UUID;

public class Account {

    private final UUID uuid;
    private final Map<String, Map<String, Integer>> boosterMap;
    private final Map<String, Integer> storedMap;
    private boolean useBars;

    public Account (UUID uuid, Map<String, Map<String, Integer>> boosterMap, Map<String, Integer> storedMap, boolean useBars) {

        this.uuid = uuid;
        this.boosterMap = boosterMap;
        this.storedMap = storedMap;
        this.useBars = useBars;

    }

    public void create() {

        AccountHandler.accountMap.put(this.uuid, this);

    }

    public UUID getUUID() {

        return this.uuid;

    }

    public Map<String, Map<String, Integer>> getBoosterMap() {

        return this.boosterMap;

    }

    public Map<String, Integer> getStoredMap() {

        return this.storedMap;

    }

    public boolean doesUseBars() {

        return this.useBars;

    }

    public void setUseBars (boolean use) {

        this.useBars = use;

    }

    public void save() {

        BetterBoosters.playerConfigManager.getPlayerConfigNode(this.uuid, "Boosters").setValue(this.boosterMap);
        BetterBoosters.playerConfigManager.getPlayerConfigNode(this.uuid, "Stored-Boosters").setValue(this.storedMap);
        BetterBoosters.playerConfigManager.getPlayerConfigNode(this.uuid, "Use-Bars").setValue(this.useBars);
        BetterBoosters.playerConfigManager.savePlayer(this.uuid);

    }

}
