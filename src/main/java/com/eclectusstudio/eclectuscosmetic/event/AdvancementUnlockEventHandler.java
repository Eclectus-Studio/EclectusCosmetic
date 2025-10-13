package com.eclectusstudio.eclectuscosmetic.event;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import com.eclectusstudio.eclectuscosmetic.data.advancementcape.AdvancementCapes;
import com.eclectusstudio.eclectuscosmetic.registry.UnlockedCapeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AdvancementUnlockEventHandler {

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        // Fires for ANY advancement progress update (grant or revoke)
        if (event instanceof AdvancementEvent.AdvancementEarnEvent earnEvent) {
            ResourceLocation id = earnEvent.getAdvancement().id();
            EclectusCosmetic.LOGGER.info("Player {} earned advancement {}",
                    earnEvent.getEntity().getName().getString(), id);

            var ac = AdvancementCapes.INSTANCE.getByAdvancement(id);
            if (ac != null) {
                UnlockedCapeRegistry.unlockCape(earnEvent.getEntity().getUUID(), ac.capeId);
            }
        }
    }
}
