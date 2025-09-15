package com.eclectusstudio.eclectuscosmetic.client;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientCapeCache {
    private static final Map<UUID, ResourceLocation> CAPE_MAP = new HashMap<>();

    public static void set(UUID uuid, ResourceLocation location) {
        CAPE_MAP.put(uuid, location);
    }

    public static ResourceLocation get(UUID uuid) {
        return CAPE_MAP.get(uuid);
    }

    public static boolean has(UUID uuid) {
        return CAPE_MAP.containsKey(uuid);
    }

    public static void clear(){
        CAPE_MAP.clear();
    }
}
