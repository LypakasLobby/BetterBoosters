package com.lypaka.betterboosters.Listeners;

import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = BetterBoosters.MOD_ID)
public class ServerStartingListener {

    @SubscribeEvent
    public static void onServerStarting (FMLServerStartingEvent event) throws IOException, ObjectMappingException {

        boolean save = false;
        if (ModList.get().isLoaded("betterlures")) {

            BetterBoosters.isBetterLuresLoaded = true;
            BetterBoosters.boosters.add("Lures");

        }
        if (ModList.get().isLoaded("bettermissions")) {

            BetterBoosters.isBetterMissionsLoaded = true;
            BetterBoosters.boosters.add("Missions");

        }

        // Updates the main config automatically with the addition of boosters
        for (int i = 0; i < BetterBoosters.boosters.size(); i++) {

            String b = BetterBoosters.boosters.get(i);
            BoosterUtils.boosterIndexMap.put(b, i);
            if (BetterBoosters.configManager.getConfigNode(0, "Booster-Icons", b).isVirtual()) {

                BetterBoosters.configManager.getConfigNode(0, "Booster-Icons", b).setValue("minecraft:diamond");
                BetterBoosters.configManager.getConfigNode(0, "Booster-Display-Names", b).setValue("&b" + b + " Booster");
                BetterBoosters.configManager.getConfigNode(0, "Boss-Bars", b, "Color").setValue("RED");
                BetterBoosters.configManager.getConfigNode(0, "Boss-Bars", b, "Title").setValue("&e" + b + " Booster: %time% seconds");
                ConfigGetters.boosterIcons.put(b, "minecraft:diamond");
                Map<String, String> barStuff = new HashMap<>();
                barStuff.put("Color", "RED");
                barStuff.put("Title", "&e" + b + " Booster: %time% seconds");
                ConfigGetters.boosterBarInfo.put(b, barStuff);
                if (!save) save = true;

            }

        }

        if (save) BetterBoosters.configManager.save();

        String[] files = new String[BetterBoosters.boosters.size()];
        for (Map.Entry<String, Integer> entry : BoosterUtils.boosterIndexMap.entrySet()) {

            files[entry.getValue()] = entry.getKey().toLowerCase() + "-booster.conf";

        }

        Path boosterDir = ConfigUtils.checkDir(BetterBoosters.dir.resolve("booster-files"));
        BetterBoosters.boosterConfigManager = new BasicConfigManager(files, boosterDir, BetterBoosters.class, BetterBoosters.MOD_NAME, BetterBoosters.MOD_ID, BetterBoosters.logger);
        BetterBoosters.boosterConfigManager.init();

        BoosterUtils.loadBoosters();
        BoosterUtils.loadGlobalBoosters();

    }

}
