package com.lypaka.betterboosters.Listeners;

import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.GlobalBoosterTask;
import com.lypaka.betterboosters.Commands.BetterBoostersCommand;
import com.lypaka.betterboosters.LegendaryBiomeMap;
import com.lypaka.betterboosters.Listeners.Boosters.*;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = BetterBoosters.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) {

        BetterBoostersCommand.createListWithRandom();

        MinecraftForge.EVENT_BUS.register(new InteractionListener());
        MinecraftForge.EVENT_BUS.register(new LoginListener());

        Pixelmon.EVENT_BUS.register(new AttackListeners());
        Pixelmon.EVENT_BUS.register(new BattleListener());
        Pixelmon.EVENT_BUS.register(new CaptureListener());
        Pixelmon.EVENT_BUS.register(new DefaultPixelmonSpawnerListeners());
        Pixelmon.EVENT_BUS.register(new ExperienceListener());

        if (ModList.get().isLoaded("spawnmanager")) {

            MinecraftForge.EVENT_BUS.register(new SpawnManagerListeners());

        }

        GlobalBoosterTask.runGlobalTimer();
        LegendaryBiomeMap.load();

    }

}
