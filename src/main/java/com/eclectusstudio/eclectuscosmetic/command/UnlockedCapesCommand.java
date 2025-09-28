package com.eclectusstudio.eclectuscosmetic.command;

import com.eclectusstudio.eclectuscosmetic.data.cape.Cape;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.registry.UnlockedCapeRegistry;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class UnlockedCapesCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("unlockedcapes")
                        .requires(source -> source.hasPermission(2)) // Permission level 2 = command blocks / operators
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            if (UnlockedCapeRegistry.getUnlocked(source.getPlayer().getUUID()).isEmpty()) {
                                source.sendSuccess(() -> Component.literal("You haven't unlocked any capes!"), false);
                                return 0;
                            }

                            source.sendSuccess(() -> Component.literal("Unlocked Capes:"), false);
                            for (ResourceLocation entry : UnlockedCapeRegistry.getUnlocked(source.getPlayer().getUUID())){
                                Cape cape = Capes.INSTANCE.getCape(entry);
                                String message = "- " + cape.name + " (Texture: " + cape.texture + ")";
                                source.sendSuccess(() -> Component.literal(message), false);
                            }
                            return 1;
                        })
        );
    }
}
