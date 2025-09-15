package com.eclectusstudio.eclectuscosmetic.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CapeRegistry {
    private static final Map<UUID, ResourceLocation> CAPES = new HashMap<>();

    public static void setCape(UUID uuid, ResourceLocation cape) {
        CAPES.put(uuid, cape);
    }

    public static ResourceLocation getCape(UUID uuid) {
        return CAPES.get(uuid);
    }

    public static boolean hasCape(UUID uuid) {
        return CAPES.containsKey(uuid);
    }
}
