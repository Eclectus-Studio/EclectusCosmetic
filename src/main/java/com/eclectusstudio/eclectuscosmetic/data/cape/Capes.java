package com.eclectusstudio.eclectuscosmetic.data.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class Capes extends SimpleJsonResourceReloadListener {
    public static final Capes INSTANCE = new Capes();
    private static final String DIRECTORY = "cape";
    private final Map<ResourceLocation, Cape> capes = new HashMap<>();

    public Capes() {
        super(new com.google.gson.GsonBuilder().create(), DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager manager, ProfilerFiller profiler) {
        capes.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            try {
                JsonObject json = entry.getValue().getAsJsonObject();
                Cape cape = CapeSerializer.fromJson(json);
                capes.put(entry.getKey(), cape);
            } catch (Exception e) {
                System.err.println("Failed to load cape " + entry.getKey() + ": " + e.getMessage());
            }
        }
    }

    public Cape getCape(ResourceLocation id) {
        return capes.get(id);
    }

    public Map<ResourceLocation, Cape> getAll() {
        return capes;
    }
}
