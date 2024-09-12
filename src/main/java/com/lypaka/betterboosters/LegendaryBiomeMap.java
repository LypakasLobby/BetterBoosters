package com.lypaka.betterboosters;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegendaryBiomeMap {

    public static Map<String, List<Integer>> speciesMap = new HashMap<>();

    public static void load() {

        List<SpawnSet> spawnList = new ArrayList<>();
        spawnList.addAll(PixelmonSpawning.legendaries);
        for (int dexNum : PixelmonSpecies.getLegendaries()) {

            for (SpawnSet set : spawnList) {

                for (SpawnInfo info : set.spawnInfos) {

                    if (info instanceof SpawnInfoPokemon) {

                        SpawnInfoPokemon sip = (SpawnInfoPokemon) info;
                        if (sip.getSpecies() != null && sip.getSpecies().getDex() == dexNum) {

                            for (ResourceLocation b : sip.condition.biomes) {

                                List<Integer> list = new ArrayList<>();
                                if (speciesMap.containsKey(b.toString())) {

                                    list = speciesMap.get(b.toString());

                                }
                                if (!list.contains(dexNum)) {

                                    list.add(dexNum);

                                }
                                speciesMap.put(b.toString(), list);

                            }

                        }

                    }

                }

            }

        }
        for (int dexNum : PixelmonSpecies.getUltraBeasts()) {

            for (SpawnSet set : spawnList) {

                for (SpawnInfo info : set.spawnInfos) {

                    if (info instanceof SpawnInfoPokemon) {

                        SpawnInfoPokemon sip = (SpawnInfoPokemon) info;
                        if (sip.getSpecies() != null && sip.getSpecies().getDex() == dexNum) {

                            for (ResourceLocation b : sip.condition.biomes) {

                                List<Integer> list = new ArrayList<>();
                                if (speciesMap.containsKey(b.toString())) {

                                    list = speciesMap.get(b.toString());

                                }
                                if (!list.contains(dexNum)) {

                                    list.add(dexNum);

                                }
                                speciesMap.put(b.toString(), list);

                            }

                        }

                    }

                }

            }

        }

    }

}
