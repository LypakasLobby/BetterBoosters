package com.lypaka.betterboosters.Accounts;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountHandler {

    public static Map<UUID, Account> accountMap = new HashMap<>();

    public static boolean usesBars (ServerPlayerEntity player) {

        return accountMap.get(player.getUniqueID()).doesUseBars();

    }

}
