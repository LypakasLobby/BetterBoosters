package com.lypaka.betterboosters.Listeners.Boosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterTask;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.Boosters.DefaultBoosters.*;
import com.lypaka.betterboosters.Boosters.GlobalBooster;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

/**
 * Handles Boss, HA, IV, Shiny and Texture Boosters
 */
public class DefaultPixelmonSpawnerListeners {

    @SubscribeEvent
    public void onSpawn (SpawnEvent event) {

        if (event.action.getOrCreateEntity() instanceof PixelmonEntity) {

            if (event.spawner instanceof PlayerTrackingSpawner) {

                ServerPlayerEntity player = ((PlayerTrackingSpawner) event.spawner).getTrackedPlayer();
                if (player != null) {

                    PixelmonEntity pixelmon = (PixelmonEntity) event.action.getOrCreateEntity();
                    Pokemon pokemon = pixelmon.getPokemon();
                    boolean appliedGlobal = false;

                    // Checking global boosters first
                    for (Map.Entry<String, GlobalBooster> entry : BoosterUtils.globalBoosters.entrySet()) {

                        GlobalBooster booster = entry.getValue();
                        if (booster.isActive()) {

                            List<String> permissions = booster.getPermissions();
                            boolean hasPermission = true;
                            for (String p : permissions) {

                                if (!PermissionHandler.hasPermission(player, p)) {

                                    hasPermission = false;
                                    break;

                                }

                            }

                            if (!hasPermission) continue;

                            if (!appliedGlobal) appliedGlobal = true;

                            for (Booster b : booster.getBoosters()) {

                                if (b instanceof BossBooster) {

                                    BossBooster bossBooster = (BossBooster) b;
                                    if (RandomHelper.getRandomChance(bossBooster.getChance())) {

                                        BossTier tier = getBossTier(bossBooster);
                                        if (tier != null) {

                                            pixelmon.setBossTier(tier);

                                        }

                                    }

                                } else if (b instanceof HABooster) {

                                    if (pokemon.hasHiddenAbility()) {

                                        if (RandomHelper.getRandomChance(b.getChance())) {

                                            pokemon.setAbility(pokemon.getForm().getAbilities().getHiddenAbilities()[0]);

                                        }

                                    }

                                } else if (b instanceof IVBooster) {

                                    String[] modifier = b.getModifier().split(" ");
                                    String function = modifier[0];
                                    double amount = Double.parseDouble(modifier[1]);

                                    int[] ivs = pokemon.getIVs().getArray();
                                    int hp = ivs[0];
                                    int atk = ivs[1];
                                    int def = ivs[2];
                                    int spAtk = ivs[3];
                                    int spDef = ivs[4];
                                    int spd = ivs[5];

                                    int[] newIVs = new int[6];
                                    if (function.equalsIgnoreCase("add")) {

                                        newIVs[0] = (int) Math.min(31, (hp + amount));
                                        newIVs[1] = (int) Math.min(31, (atk + amount));
                                        newIVs[2] = (int) Math.min(31, (def + amount));
                                        newIVs[3] = (int) Math.min(31, (spAtk + amount));
                                        newIVs[4] = (int) Math.min(31, (spDef + amount));
                                        newIVs[5] = (int) Math.min(31, (spd + amount));

                                    } else {

                                        newIVs[0] = (int) Math.min(31, (hp * amount));
                                        newIVs[1] = (int) Math.min(31, (atk * amount));
                                        newIVs[2] = (int) Math.min(31, (def * amount));
                                        newIVs[3] = (int) Math.min(31, (spAtk * amount));
                                        newIVs[4] = (int) Math.min(31, (spDef * amount));
                                        newIVs[5] = (int) Math.min(31, (spd * amount));

                                    }

                                    pokemon.getIVs().fillFromArray(newIVs);

                                } else if (b instanceof ShinyBooster) {

                                    if (!pokemon.isShiny()) {

                                        if (RandomHelper.getRandomChance(b.getChance())) {

                                            pokemon.setShiny(true);

                                        }

                                    }

                                } else if (b instanceof TextureBooster) {

                                    TextureBooster textureBooster = (TextureBooster) b;
                                    String status = "Shiny";
                                    if (!pokemon.isShiny()) {

                                        status = "Non-" + status;

                                    }

                                    if (textureBooster.hasTexture(pokemon.getSpecies().getName(), status)) {

                                        if (RandomHelper.getRandomChance(textureBooster.getChance())) {

                                            pokemon.setPalette(textureBooster.getTexture(pokemon.getSpecies().getName(), status));

                                        }

                                    }

                                }

                            }

                        }

                    }

                    if (appliedGlobal) return;

                    for (BoosterTask task : BoosterUtils.activeTasks) {

                        if (task.getBooster() instanceof BossBooster) {

                            List<UUID> uuids = task.playerList;
                            if (uuids.contains(player.getUniqueID())) {

                                BossBooster bossBooster = (BossBooster) task.getBooster();
                                if (RandomHelper.getRandomChance(bossBooster.getChance())) {

                                    BossTier tier = getBossTier(bossBooster);
                                    if (tier != null) {

                                        pixelmon.setBossTier(tier);

                                    }

                                }

                            }

                        } else if (task.getBooster() instanceof HABooster) {

                            List<UUID> uuids = task.playerList;
                            if (uuids.contains(player.getUniqueID())) {

                                if (pokemon.hasHiddenAbility()) {

                                    if (RandomHelper.getRandomChance(task.getBooster().getChance())) {

                                        pokemon.setAbility(pokemon.getForm().getAbilities().getHiddenAbilities()[0]);

                                    }

                                }

                            }

                        } else if (task.getBooster() instanceof IVBooster) {

                            List<UUID> uuids = task.playerList;
                            if (uuids.contains(player.getUniqueID())) {

                                String[] modifier = task.getBooster().getModifier().split(" ");
                                String function = modifier[0];
                                double amount = Double.parseDouble(modifier[1]);

                                int[] ivs = pokemon.getIVs().getArray();
                                int hp = ivs[0];
                                int atk = ivs[1];
                                int def = ivs[2];
                                int spAtk = ivs[3];
                                int spDef = ivs[4];
                                int spd = ivs[5];

                                int[] newIVs = new int[6];
                                if (function.equalsIgnoreCase("add")) {

                                    newIVs[0] = (int) Math.min(31, (hp + amount));
                                    newIVs[1] = (int) Math.min(31, (atk + amount));
                                    newIVs[2] = (int) Math.min(31, (def + amount));
                                    newIVs[3] = (int) Math.min(31, (spAtk + amount));
                                    newIVs[4] = (int) Math.min(31, (spDef + amount));
                                    newIVs[5] = (int) Math.min(31, (spd + amount));

                                } else {

                                    newIVs[0] = (int) Math.min(31, (hp * amount));
                                    newIVs[1] = (int) Math.min(31, (atk * amount));
                                    newIVs[2] = (int) Math.min(31, (def * amount));
                                    newIVs[3] = (int) Math.min(31, (spAtk * amount));
                                    newIVs[4] = (int) Math.min(31, (spDef * amount));
                                    newIVs[5] = (int) Math.min(31, (spd * amount));

                                }

                                pokemon.getIVs().fillFromArray(newIVs);

                            }

                        } else if (task.getBooster() instanceof ShinyBooster) {

                            List<UUID> uuids = task.playerList;
                            if (uuids.contains(player.getUniqueID())) {

                                if (!pokemon.isShiny()) {

                                    if (RandomHelper.getRandomChance(task.getBooster().getChance())) {

                                        pokemon.setShiny(true);

                                    }

                                }

                            }

                        } else if (task.getBooster() instanceof TextureBooster) {

                            List<UUID> uuids = task.playerList;
                            if (uuids.contains(player.getUniqueID())) {

                                TextureBooster textureBooster = (TextureBooster) task.getBooster();
                                String status = "Shiny";
                                if (!pokemon.isShiny()) {

                                    status = "Non-" + status;

                                }

                                if (textureBooster.hasTexture(pokemon.getSpecies().getName(), status)) {

                                    if (RandomHelper.getRandomChance(textureBooster.getChance())) {

                                        pokemon.setPalette(textureBooster.getTexture(pokemon.getSpecies().getName(), status));

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }

    private static BossTier getBossTier (BossBooster booster) {

        BossTier tier = null;
        Map<String, Double> map = booster.getBossSpawnRates();
        Map<UUID, String> m1 = new HashMap<>();
        Map<Double, UUID> m2 = new HashMap<>();

        for (Map.Entry<String, Double> entry : map.entrySet()) {

            UUID uuid = UUID.randomUUID();
            m1.put(uuid, entry.getKey());
            m2.put(entry.getValue(), uuid);

        }

        List<Double> chances = new ArrayList<>(m2.keySet());
        Collections.sort(chances);

        for (int i = 0; i < chances.size(); i++) {

            if (RandomHelper.getRandomChance(chances.get(i))) {

                UUID uuid = m2.get(chances.get(i));
                String tierName = m1.get(uuid);
                tier = BossTierRegistry.getBossTierUnsafe(tierName);
                break;

            }

        }

        return tier;

    }

}
