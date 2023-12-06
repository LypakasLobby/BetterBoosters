package com.lypaka.betterboosters.Boosters.DefaultBoosters;

import com.lypaka.betterboosters.Boosters.Booster;
import com.lypaka.betterboosters.Boosters.BoosterUtils;

import java.util.Map;

public class TextureBooster extends Booster {

    private final Map<String, Map<String, String>> textures;

    public TextureBooster (double chance, Map<String, Integer> timerMap, Map<String, Map<String, String>> textures) {

        super("Texture", chance, timerMap);
        this.textures = textures;

    }

    public void create() {

        BoosterUtils.boosterMap.put("Texture", this);

    }

    public Map<String, Map<String, String>> getTextures() {

        return this.textures;

    }

    public boolean hasTexture (String pokemon, String status) {

        if (!this.textures.containsKey(pokemon)) return false;
        return this.textures.get(pokemon).containsKey(status);

    }

    public String getTexture (String pokemon, String status) {

        return this.textures.get(pokemon).get(status);

    }

}
