package com.lypaka.betterboosters;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static boolean allowUseWithGlobal;
    public static List<String> randomBlacklist;
    public static Map<String, String> boosterIcons;
    public static Map<String, String> boosterDisplayNames;
    public static boolean useBossBars;
    public static Map<String, Map<String, String>> boosterBarInfo;
    public static Map<String, String> messagesMap;

    public static int storageGUIRows;
    public static String storageGUITitle;
    public static String storageGUIBorderID;
    public static String storageGUIBorderSlots;

    public static Map<String, Map<String, String>> globalBoosters;

    public static void load() throws ObjectMappingException {

        allowUseWithGlobal = BetterBoosters.configManager.getConfigNode(0, "Allow-Use-With-Global").getBoolean();
        randomBlacklist = BetterBoosters.configManager.getConfigNode(0, "Blacklist").getList(TypeToken.of(String.class));
        boosterIcons = BetterBoosters.configManager.getConfigNode(0, "Booster-Icons").getValue(new TypeToken<Map<String, String>>() {});
        boosterDisplayNames = BetterBoosters.configManager.getConfigNode(0, "Booster-Display-Names").getValue(new TypeToken<Map<String, String>>() {});
        useBossBars = BetterBoosters.configManager.getConfigNode(0, "Boss-Bars-Enabled").getBoolean();
        boosterBarInfo = BetterBoosters.configManager.getConfigNode(0, "Boss-Bars-Settings").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
        messagesMap = BetterBoosters.configManager.getConfigNode(0, "Messages").getValue(new TypeToken<Map<String, String>>() {});

        storageGUIRows = BetterBoosters.configManager.getConfigNode(1, "Settings", "Rows").getInt();
        storageGUITitle = BetterBoosters.configManager.getConfigNode(1, "Settings", "Title").getString();
        storageGUIBorderID = BetterBoosters.configManager.getConfigNode(1, "Slots", "Border", "ID").getString();
        storageGUIBorderSlots = BetterBoosters.configManager.getConfigNode(1, "Slots", "Border", "Slots").getString();

        globalBoosters = BetterBoosters.configManager.getConfigNode(2, "Global-Boosters").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

    }
    
}
