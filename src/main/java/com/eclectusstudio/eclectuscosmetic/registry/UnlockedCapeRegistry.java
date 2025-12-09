package com.eclectusstudio.eclectuscosmetic.registry;

import com.eclectusstudio.eclectuscosmetic.data.cape.Cape;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.packet.record.CapeToastData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class UnlockedCapeRegistry {
    public static final Map<UUID, Set<ResourceLocation>> UNLOCKED_CAPES = new HashMap<>();

    public static void unlockCape(UUID uuid, ResourceLocation capeId, MinecraftServer server) {
        UNLOCKED_CAPES.computeIfAbsent(uuid, k -> new HashSet<>()).add(capeId);
        Cape cape = Capes.INSTANCE.getCape(capeId);
        PacketDistributor.sendToPlayer(server.getPlayerList().getPlayer(uuid), new CapeToastData(cape.texture, cape.name));
    }

    public static boolean hasUnlocked(UUID uuid, ResourceLocation capeId) {
        return UNLOCKED_CAPES.getOrDefault(uuid, Collections.emptySet()).contains(capeId);
    }

    public static Set<ResourceLocation> getUnlocked(UUID uuid) {
        return UNLOCKED_CAPES.getOrDefault(uuid, Collections.emptySet());
    }
}
