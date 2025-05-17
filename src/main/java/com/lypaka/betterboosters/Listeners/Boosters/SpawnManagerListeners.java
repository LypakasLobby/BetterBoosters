package com.lypaka.betterboosters.Listeners.Boosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterTask;
import com.lypaka.betterboosters.Boosters.BoosterUtils;
import com.lypaka.betterboosters.Boosters.DefaultBoosters.*;
import com.lypaka.betterboosters.Boosters.GlobalBooster;
import com.lypaka.betterboosters.LegendaryBiomeMap;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.lypakautils.MiscHandlers.PixelmonHelpers;
import com.lypaka.spawnmanager.API.AreaGrassSpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpawnManagerListeners {

    @SubscribeEvent
    public void onGrass (AreaGrassSpawnEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();
        if (pokemon == null) return;
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

                    if (b instanceof LegendaryBooster) {

                        String biome = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                        if (RandomHelper.getRandomChance(b.getChance())) {

                            if (LegendaryBiomeMap.speciesMap.containsKey(biome)) {

                                int dexNum = RandomHelper.getRandomElementFromList(LegendaryBiomeMap.speciesMap.get(biome));
                                Pokemon newPokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(dexNum).build());
                                newPokemon.setLevel(RandomHelper.getRandomNumberBetween(newPokemon.getForm().minLevel, newPokemon.getForm().maxLevel));
                                pokemon.setSpecies(newPokemon.getSpecies(), true);
                                if (((LegendaryBooster) b).locksToPlayer()) {

                                    pokemon.getPersistentData().putString("LockedTo", player.getUniqueID().toString());

                                }

                            }

                        }

                    } else if (b instanceof BossBooster) {

                        BossBooster bossBooster = (BossBooster) b;
                        if (RandomHelper.getRandomChance(bossBooster.getChance())) {

                            BossTier tier = DefaultPixelmonSpawnerListeners.getBossTier(bossBooster);
                            if (tier != null) {

                                pokemon.getOrCreatePixelmon().setBossTier(tier);

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

                            if (RandomHelper.getRandomNumberBetween(1, (int) b.getChance()) == 1) {

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

            if (task.getBooster() instanceof LegendaryBooster) {

                String biome = player.world.getBiome(player.getPosition()).getRegistryName().toString();
                if (RandomHelper.getRandomChance(task.getBooster().getChance())) {

                    if (LegendaryBiomeMap.speciesMap.containsKey(biome)) {

                        int dexNum = RandomHelper.getRandomElementFromList(LegendaryBiomeMap.speciesMap.get(biome));
                        Pokemon newPokemon = PixelmonHelpers.fixPokemonIVsAndGender(PokemonBuilder.builder().species(dexNum).build());
                        newPokemon.setLevel(RandomHelper.getRandomNumberBetween(newPokemon.getForm().minLevel, newPokemon.getForm().maxLevel));
                        pokemon.setSpecies(newPokemon.getSpecies(), true);

                    }

                }

            } else if (task.getBooster() instanceof BossBooster) {

                List<UUID> uuids = task.playerList;
                if (uuids.contains(player.getUniqueID())) {

                    BossBooster bossBooster = (BossBooster) task.getBooster();
                    if (RandomHelper.getRandomChance(bossBooster.getChance())) {

                        BossTier tier = DefaultPixelmonSpawnerListeners.getBossTier(bossBooster);
                        if (tier != null) {

                            pokemon.getOrCreatePixelmon().setBossTier(tier);

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

                        if (RandomHelper.getRandomNumberBetween(1, (int) task.getBooster().getChance()) == 1) {

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
