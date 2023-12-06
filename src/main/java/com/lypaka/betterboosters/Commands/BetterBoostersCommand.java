package com.lypaka.betterboosters.Commands;

import com.lypaka.betterboosters.BetterBoosters;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterBoosters.MOD_ID)
public class BetterBoostersCommand {

    public static final List<String> ALIASES = Arrays.asList("betterboosters", "bboosts", "boosts");
    public static final List<String> BOOSTER_NAMES = BetterBoosters.boosters;
    public static List<String> BOOSTER_NAMES_WITH_RANDOM = new ArrayList<>();
    public static final SuggestionProvider<CommandSource> BOOSTERS = (context, builder) ->
            ISuggestionProvider.suggest(BOOSTER_NAMES.stream(), builder);
    public static final SuggestionProvider<CommandSource> BOOSTERS_WITH_RANDOM = (context, builder) ->
            ISuggestionProvider.suggest(BOOSTER_NAMES_WITH_RANDOM.stream(), builder);

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new ActivateCommand(event.getDispatcher());
        new DeactivateCommand(event.getDispatcher());
        new GiveCommand(event.getDispatcher());
        new PauseCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        new ResumeCommand(event.getDispatcher());
        new StorageCommand(event.getDispatcher());
        new ToggleCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

    public static void createListWithRandom() {

        BOOSTER_NAMES_WITH_RANDOM.addAll(BOOSTER_NAMES);
        BOOSTER_NAMES_WITH_RANDOM.add("random");

    }

}
