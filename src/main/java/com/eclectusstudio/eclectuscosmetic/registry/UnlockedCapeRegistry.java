package com.eclectusstudio.eclectuscosmetic.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class UnlockedCapeRegistry {
    public static final Map<UUID, Set<ResourceLocation>> UNLOCKED_CAPES = new HashMap<>();

    public static void unlockCape(UUID uuid, ResourceLocation capeId) {
        UNLOCKED_CAPES.computeIfAbsent(uuid, k -> new HashSet<>()).add(capeId);
    }

    public static boolean hasUnlocked(UUID uuid, ResourceLocation capeId) {
        return UNLOCKED_CAPES.getOrDefault(uuid, Collections.emptySet()).contains(capeId);
    }

    public static Set<ResourceLocation> getUnlocked(UUID uuid) {
        return UNLOCKED_CAPES.getOrDefault(uuid, Collections.emptySet());
    }
}
