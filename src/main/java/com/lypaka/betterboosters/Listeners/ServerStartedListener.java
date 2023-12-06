package com.lypaka.betterboosters.Listeners;

import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.GlobalBoosterTask;
import com.lypaka.betterboosters.Commands.BetterBoostersCommand;
import com.lypaka.betterboosters.Listeners.Boosters.AttackListeners;
import com.lypaka.betterboosters.Listeners.Boosters.CaptureListener;
import com.lypaka.betterboosters.Listeners.Boosters.DefaultPixelmonSpawnerListeners;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        Pixelmon.EVENT_BUS.register(new CaptureListener());
        Pixelmon.EVENT_BUS.register(new DefaultPixelmonSpawnerListeners());

        GlobalBoosterTask.runGlobalTimer();

    }

}
