package com.eclectusstudio.eclectuscosmetic.data.advancementcape;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class AdvancementCapeSerializer {

    public static AdvancementCape fromJson(JsonObject json) {
        ResourceLocation cape = ResourceLocation.tryParse(json.get("cape").getAsString());
        ResourceLocation advancement = ResourceLocation.tryParse(json.get("advancement").getAsString());
        return new AdvancementCape(cape, advancement);
    }
}