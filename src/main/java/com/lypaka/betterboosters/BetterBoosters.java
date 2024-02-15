package com.lypaka.betterboosters;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.ConfigurationLoaders.PlayerConfigManager;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betterboosters")
public class BetterBoosters {

    public static final String MOD_ID = "betterboosters";
    public static final String MOD_NAME = "BetterBoosters";
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static boolean isBetterMissionsLoaded = false;
    public static boolean isBetterLuresLoaded = false;
    public static BasicConfigManager configManager;
    public static BasicConfigManager boosterConfigManager;
    public static PlayerConfigManager playerConfigManager;
    public static Path dir;
    public static List<String> boosters = new ArrayList<>(Arrays.asList("Attack", "Boss", "Capture", "EXP", "HA",
            "IV", "Legendary", "Shiny", "Texture"));

    // TODO:
    /*
    Add Attack Booster
    Add Tokens Booster for BetterTokens
    Add PokeStop, Raid drops to drops booster (with settings like the Attack Booster)
    Add Fishing Booster
        chance of nothing decrease modifier
        Pokemon modification
    Add global events for boosters, meaning all players (whether online at the time of activation or not) get booster effects on join, removed on log off
        expiration timer
        Discord webhook for announcing event activation and expiration
        config to store events, boolean value to toggle activated or not
        can be multiple boosters
    Support BetterAreas
        IV, Shiny, Texture, HA, Boss, Legendary
    Skills Booster?
    Support other Pokemon-receiving events for Pokemon-related boosters
        Fossil, starter, egg
     */

    public BetterBoosters() throws IOException, ObjectMappingException {

        dir = ConfigUtils.checkDir(Paths.get("./config/betterboosters"));
        String[] fileNames = new String[]{"betterboosters.conf", "gui-settings.conf", "global-boosters.conf"};
        configManager = new BasicConfigManager(fileNames, dir, BetterBoosters.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        playerConfigManager = new PlayerConfigManager("account.conf", "player-accounts", dir, BetterBoosters.class, MOD_NAME, MOD_ID, logger);
        playerConfigManager.init();
        ConfigGetters.load();

    }

}
