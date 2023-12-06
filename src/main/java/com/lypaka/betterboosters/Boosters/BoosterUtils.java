package com.lypaka.betterboosters.Boosters;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterboosters.API.BoosterUseEvent;
import com.lypaka.betterboosters.Accounts.Account;
import com.lypaka.betterboosters.Accounts.AccountHandler;
import com.lypaka.betterboosters.BetterBoosters;
import com.lypaka.betterboosters.Boosters.DefaultBoosters.*;
import com.lypaka.betterboosters.Boosters.ExtraBoosters.*;
import com.lypaka.betterboosters.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.lypaka.lypakautils.MiscHandlers.ItemStackBuilder;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.common.MinecraftForge;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.time.LocalDateTime;
import java.util.*;

public class BoosterUtils {

    public static Map<String, Integer> boosterIndexMap = new HashMap<>();
    public static Map<String, Booster> boosterMap = new HashMap<>();
    public static Map<UUID, Map<String, ServerBossInfo>> barMap = new HashMap<>();
    public static List<BoosterTask> activeTasks = new ArrayList<>();
    public static Map<UUID, Map<String, Integer>> currentMap = new HashMap<>();
    public static Map<UUID, Map<String, Integer>> maxMap = new HashMap<>();
    public static Map<UUID, List<Booster>> activeBoosters = new HashMap<>();
    public static Map<String, GlobalBooster> globalBoosters = new HashMap<>();

    public static void activateBooster (UUID uuid, Booster booster, boolean removeItem, int current, int timer) {

        ServerPlayerEntity player = JoinListener.playerMap.get(uuid);
        BoosterUseEvent event = new BoosterUseEvent(player, booster, timer);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {

            player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Used-Failed")
                    .replace("%booster%", booster.getName())
            ), player.getUniqueID());
            return;

        }
        Account account = AccountHandler.accountMap.get(uuid);
        Map<String, Integer> map = new HashMap<>();
        if (removeItem) {

            player.getHeldItem(Hand.MAIN_HAND).setCount(player.getHeldItem(Hand.MAIN_HAND).getCount() - 1);

        }

        booster = event.getBooster();
        // Check if the player currently has this booster active
        List<Booster> active = new ArrayList<>();
        if (activeBoosters.containsKey(uuid)) {

            active = activeBoosters.get(uuid);
            boolean contains = false;
            for (Booster b : active) {

                if (b.getName().equalsIgnoreCase(booster.getName())) {

                    contains = true;
                    break;

                }

            }
            if (!contains) {

                active.add(booster);

            }


        } else {

            active.add(booster);

        }

        activeBoosters.put(uuid, active);

        // Check if the server currently has a task running for this booster
        BoosterTask task = null;
        for (BoosterTask t : activeTasks) {

            Booster b = t.getBooster();
            if (b.getName().equalsIgnoreCase(booster.getName())) {

                task = t;
                break;

            }

        }
        if (task == null) {

            task = new BoosterTask(booster);

        }
        Map<String, Integer> cMap = new HashMap<>();
        Map<String, Integer> mMap = new HashMap<>();
        if (!task.playerList.contains(uuid)) {

            if (currentMap.containsKey(uuid)) {

                cMap = currentMap.get(uuid);

            }
            if (maxMap.containsKey(uuid)) {

                mMap = maxMap.get(uuid);

            }
            cMap.put(booster.getName(), current);
            mMap.put(booster.getName(), event.getTimer());
            currentMap.put(uuid, cMap);
            maxMap.put(uuid, mMap);
            task.playerList.add(uuid);
            if (ConfigGetters.useBossBars) {

                if (AccountHandler.usesBars(player)) {

                    BoosterBar bar = new BoosterBar(uuid, booster.getName());
                    bar.buildBar();

                }

            }

            map.put("Current", event.getTimer());
            map.put("Max", event.getTimer());
            account.getBoosterMap().put(booster.getName(), map);

        } else {

            // this player already has this booster active, so we need to increase the max time
            if (currentMap.containsKey(uuid)) {

                cMap = currentMap.get(uuid);

            }
            if (maxMap.containsKey(uuid)) {

                mMap = maxMap.get(uuid);

            }
            int newMax = BoosterTask.getMax(uuid, booster) + event.getTimer();
            int newCurrent = BoosterTask.getCurrent(uuid, booster) + current;
            cMap.put(booster.getName(), newCurrent);
            mMap.put(booster.getName(), newMax);
            currentMap.put(uuid, cMap);
            maxMap.put(uuid, mMap);

            if (ConfigGetters.useBossBars) {

                if (AccountHandler.usesBars(player)) {

                    BoosterBar bar = new BoosterBar(uuid, booster.getName());
                    bar.buildBar();

                }

            }

            map.put("Current", newCurrent);
            map.put("Max", newMax);
            account.getBoosterMap().put(booster.getName(), map);

        }
        if (!activeTasks.contains(task)) {

            task.timer = new Timer();
            task.timer.schedule(task, 0, 1000);
            activeTasks.add(task);

        }

        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messagesMap.get("Booster-Used")
                .replace("%booster%", booster.getName())
        ), player.getUniqueID());

    }

    public static void pauseBooster (ServerPlayerEntity player, Booster booster) {

        Account account = AccountHandler.accountMap.get(player.getUniqueID());
        activeTasks.removeIf(task -> {

            if (task.getBooster().getName().equalsIgnoreCase(booster.getName())) {

                List<UUID> uuids = task.playerList;
                uuids.removeIf(entry -> {

                    if (entry.toString().equalsIgnoreCase(player.getUniqueID().toString())) {

                        Map<String, Integer> m2 = new HashMap<>();
                        m2.put("Current", currentMap.get(player.getUniqueID()).get(booster.getName()));
                        m2.put("Max", maxMap.get(player.getUniqueID()).get(booster.getName()));
                        account.getBoosterMap().put(booster.getName(), m2);
                        if (barMap.containsKey(entry)) {

                            Map<String, ServerBossInfo> bMap = barMap.get(entry);
                            bMap.entrySet().removeIf(barEntry -> {

                                if (barEntry.getKey().equalsIgnoreCase(booster.getName())) {

                                    ServerBossInfo bar = bMap.get(booster.getName());
                                    bar.removePlayer(player);
                                    bar.setVisible(false);
                                    return true;

                                }

                                return false;

                            });
                            barMap.put(entry, bMap);

                        }
                        return true;

                    }

                    return false;

                });

            }

            if (task.playerList.size() == 0) {

                task.cancel();
                task.timer.cancel();
                task.timer = new Timer();
                BetterBoosters.logger.info(booster.getName() + " Booster has no players, stopping task!");
                return true;

            }

            return false;

        });

    }

    public static void deactivateBooster (Booster booster, UUID uuid) {

        BoosterTask task = null;

        for (BoosterTask bt : BoosterUtils.activeTasks) {

            if (bt.getBooster().getName().equalsIgnoreCase(booster.getName())) {

                task = bt;
                break;

            }

        }

        if (booster == null || task == null) {

            BetterBoosters.logger.error("Trying to deactivate a null booster!");
            return;

        }

        if (uuid != null) {

            task.playerList.removeIf(entry -> {

                if (uuid.toString().equalsIgnoreCase(entry.toString())) {

                    if (BoosterUtils.barMap.containsKey(entry)) {

                        Map<String, ServerBossInfo> barMap = BoosterUtils.barMap.get(entry);
                        barMap.entrySet().removeIf(barEntry -> {

                            if (barEntry.getKey().equalsIgnoreCase(booster.getName())) {

                                ServerBossInfo bar = barMap.get(booster.getName());
                                bar.removePlayer(JoinListener.playerMap.get(entry));
                                bar.setVisible(false);
                                return true;

                            }

                            return false;

                        });
                        BoosterUtils.barMap.put(entry, barMap);

                    }

                    return true;

                }

                return false;

            });

        } else {

            task.playerList.removeIf(entry -> {

                if (BoosterUtils.barMap.containsKey(entry)) {

                    Map<String, ServerBossInfo> barMap = BoosterUtils.barMap.get(entry);
                    barMap.entrySet().removeIf(barEntry -> {

                        if (barEntry.getKey().equalsIgnoreCase(booster.getName())) {

                            ServerBossInfo bar = barMap.get(booster.getName());
                            bar.removePlayer(JoinListener.playerMap.get(entry));
                            bar.setVisible(false);
                            return true;

                        }

                        return false;

                    });
                    BoosterUtils.barMap.put(entry, barMap);

                }

                return true;

            });

            task.cancel();
            task.timer.cancel();
            task.timer = new Timer();
            BoosterTask finalTask = task;
            BoosterUtils.activeTasks.removeIf(t -> t == finalTask);

        }

    }

    public static void loadBoosters() throws ObjectMappingException {

        boosterMap = new HashMap<>();
        int index;
        for (String b : BetterBoosters.boosters) {

            index = boosterIndexMap.get(b);
            Map<String, Integer> timerMap = BetterBoosters.boosterConfigManager.getConfigNode(index, "Timer").getValue(new TypeToken<Map<String, Integer>>() {});
            if (b.equalsIgnoreCase("Attack")) {

                List<String> npcBlacklist = BetterBoosters.boosterConfigManager.getConfigNode(index, "NPC-Blacklist").getList(TypeToken.of(String.class));
                double critChance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "Crit-Modifier", "Chance").getDouble();
                boolean critEnabled = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "Crit-Modifier", "Enabled").getBoolean();
                String critModifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "Crit-Modifier", "Modifier").getString();
                double stabChance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "STAB", "Chance").getDouble();
                boolean stabEnabled = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "STAB", "Enabled").getBoolean();
                double effectivenessChance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "Type-Effectiveness", "Chance").getDouble();
                boolean effectivenessEnabled = BetterBoosters.boosterConfigManager.getConfigNode(index, "Options", "Type-Effectiveness", "Enabled").getBoolean();

                AttackBooster booster = new AttackBooster(timerMap, npcBlacklist, critChance, critEnabled, critModifier, stabChance, stabEnabled, effectivenessChance, effectivenessEnabled);
                booster.create();

            } else if (b.equalsIgnoreCase("Boss")) {

                Map<String, Double> bossSpawnRates = BetterBoosters.boosterConfigManager.getConfigNode(index, "Boss-Spawn-Rates").getValue(new TypeToken<Map<String, Double>>() {});
                double chance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Chance").getDouble();

                BossBooster booster = new BossBooster(timerMap, bossSpawnRates, chance);
                booster.create();

            } else if (b.equalsIgnoreCase("Capture")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                CaptureBooster booster = new CaptureBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("EV")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                EVBooster booster = new EVBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("EXP")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                EXPBooster booster = new EXPBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("HA")) {

                double chance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Chance").getDouble();

                HABooster booster = new HABooster(chance, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("Hatch")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                HatchBooster booster = new HatchBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("IV")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                IVBooster booster = new IVBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("Legendary")) {

                double chance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Chance").getDouble();
                boolean lockToPlayer = BetterBoosters.boosterConfigManager.getConfigNode(index, "Lock-To-Player").getBoolean();

                LegendaryBooster booster = new LegendaryBooster(chance, timerMap, lockToPlayer);
                booster.create();

            } else if (b.equalsIgnoreCase("Lures")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                LuresBooster booster = new LuresBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("Missions")) {

                double chance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Chance").getDouble();
                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();
                boolean runCommandsTwice = BetterBoosters.boosterConfigManager.getConfigNode(index, "Run-Commands-Twice").getBoolean();

                MissionsBooster booster = new MissionsBooster(chance, modifier, timerMap, runCommandsTwice);
                booster.create();

            } else if (b.equalsIgnoreCase("Shiny")) {

                double chance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Chance").getDouble();

                ShinyBooster booster = new ShinyBooster(chance, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("Texture")) {

                double chance = BetterBoosters.boosterConfigManager.getConfigNode(index, "Chance").getDouble();
                Map<String, Map<String, String>> textures = BetterBoosters.boosterConfigManager.getConfigNode(index, "Textures").getValue(new TypeToken<Map<String, Map<String, String>>>() {});

                TextureBooster booster = new TextureBooster(chance, timerMap, textures);
                booster.create();

            } else if (b.equalsIgnoreCase("Tokens")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                TokensBooster booster = new TokensBooster(modifier, timerMap);
                booster.create();

            } else if (b.equalsIgnoreCase("Winnings")) {

                String modifier = BetterBoosters.boosterConfigManager.getConfigNode(index, "Modifier").getString();

                WinningsBooster booster = new WinningsBooster(modifier, timerMap);
                booster.create();

            }

        }

        BetterBoosters.logger.info("Successfully loaded all available boosters!");

    }

    public static void loadGlobalBoosters() throws ObjectMappingException {

        // index 2
        LocalDateTime now = LocalDateTime.now();
        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.globalBoosters.entrySet()) {

            String globalBoosterName = entry.getKey();
            List<String> boosterNames = BetterBoosters.configManager.getConfigNode(2, "Global-Boosters", globalBoosterName, "Boosters").getList(TypeToken.of(String.class));
            List<Booster> boosters = new ArrayList<>();
            for (String b : boosterNames) {

                boosters.add(getFromName(b));

            }
            List<String> permissions = BetterBoosters.configManager.getConfigNode(2, "Global-Boosters", globalBoosterName, "Permissions").getList(TypeToken.of(String.class));
            Map<String, Integer> timeMap = BetterBoosters.configManager.getConfigNode(2, "Global-Boosters", globalBoosterName, "Time").getValue(new TypeToken<Map<String, Integer>>() {});
            int endDay = timeMap.get("End-Day");
            int endHour = timeMap.get("End-Hour");
            int endMinute = timeMap.get("End-Minute");
            int endMonth = timeMap.get("End-Month");
            int startDay = timeMap.get("Start-Day");
            int startHour = timeMap.get("Start-Hour");
            int startMinute = timeMap.get("Start-Minute");
            int startMonth = timeMap.get("Start-Month");

            GlobalBooster globalBooster = new GlobalBooster(globalBoosterName, boosters, permissions, endDay, endHour, endMinute, endMonth, startDay, startHour, startMinute, startMonth);
            globalBooster.create();

            int nowDay = now.getDayOfMonth();
            int nowMonth = now.getMonthValue();
            int nowHour = now.getHour();
            int nowMinute = now.getMinute();

            if (nowMonth >= startMonth && nowMonth <= endMonth) {

                if (nowDay >= startDay && nowDay <= endDay) {

                    if (nowHour >= startHour && nowHour <= endHour) {

                        if (nowMinute >= startMinute && nowMinute <= endMinute) {

                            if (!globalBooster.isActive()) {

                                globalBooster.setActive(true);

                            }

                        }

                    }

                }

            }

        }

    }

    public static Booster getFromName (String name) {

        if (name.equalsIgnoreCase("random")) {

            List<Booster> boosters = new ArrayList<>();
            for (Map.Entry<String, Booster> entry : boosterMap.entrySet()) {

                if (!ConfigGetters.randomBlacklist.contains(entry.getKey())) {

                    boosters.add(entry.getValue());

                }

            }

            return RandomHelper.getRandomElementFromList(boosters);

        } else {

            Booster b = null;
            for (Map.Entry<String, Booster> entry : boosterMap.entrySet()) { // so it's not case-sensitive

                if (entry.getKey().equalsIgnoreCase(name)) {

                    b = entry.getValue();
                    break;

                }

            }

            return b;

        }

    }

    public static GlobalBooster getGlobalFromName (String name) {

        GlobalBooster booster = null;
        for (Map.Entry<String, GlobalBooster> entry : globalBoosters.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(name)) {

                booster = entry.getValue();
                break;

            }

        }

        return booster;

    }

    public static ItemStack buildBoosterItem (Booster booster, int count) {

        String name = booster.getName();
        String id = ConfigGetters.boosterIcons.get(name);
        ItemStack item = ItemStackBuilder.buildFromStringID(id);
        item.setDisplayName(FancyText.getFormattedText(ConfigGetters.boosterDisplayNames.get(name)));
        item.setCount(count);
        return item;

    }

}
