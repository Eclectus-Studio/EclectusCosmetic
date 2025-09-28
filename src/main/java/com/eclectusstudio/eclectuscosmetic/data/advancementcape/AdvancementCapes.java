package com.eclectusstudio.eclectuscosmetic.data.advancementcape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AdvancementCapes extends SimpleJsonResourceReloadListener {
    public static final AdvancementCapes INSTANCE = new AdvancementCapes();
    private static final String DIRECTORY = "advancement_cape";

    private final Map<ResourceLocation, AdvancementCape> advancementCapes = new HashMap<>();

    public AdvancementCapes() {
        super(new com.google.gson.GsonBuilder().create(), DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager manager, ProfilerFiller profiler) {
        advancementCapes.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            try {
                JsonObject json = entry.getValue().getAsJsonObject();
                AdvancementCape ac = AdvancementCapeSerializer.fromJson(json);
                advancementCapes.put(entry.getKey(), ac);
            } catch (Exception e) {
                System.err.println("Failed to load advancement_cape " + entry.getKey() + ": " + e.getMessage());
            }
        }
    }

    public AdvancementCape getByAdvancement(ResourceLocation advancementId) {
        for (AdvancementCape ac : advancementCapes.values()) {
            if (ac.advancementId.equals(advancementId)) {
                return ac;
            }
        }
        return null;
    }

    public Collection<AdvancementCape> getAll() {
        return advancementCapes.values();
    }
}