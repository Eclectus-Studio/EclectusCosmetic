package com.eclectusstudio.eclectuscosmetic.event;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import com.eclectusstudio.eclectuscosmetic.data.advancementcape.AdvancementCapes;
import com.eclectusstudio.eclectuscosmetic.registry.UnlockedCapeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

public class AdvancementUnlockEventHandler {

    @SubscribeEvent
    public void onAdvancement(AdvancementEvent.AdvancementEarnEvent earnEvent) {
        ResourceLocation id = earnEvent.getAdvancement().id();
        EclectusCosmetic.LOGGER.info("Player {} earned advancement {}",
                earnEvent.getEntity().getName().getString(), id);

        var ac = AdvancementCapes.INSTANCE.getByAdvancement(id);
        if (ac != null) {
            UnlockedCapeRegistry.unlockCape(earnEvent.getEntity().getUUID(), ac.capeId);
        }
    }
}
