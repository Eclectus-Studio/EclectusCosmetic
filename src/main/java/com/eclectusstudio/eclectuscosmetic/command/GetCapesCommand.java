package com.eclectusstudio.eclectuscosmetic.command;

import com.eclectusstudio.eclectuscosmetic.data.cape.Cape;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class GetCapesCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("getcapes")
                        .requires(source -> source.hasPermission(2)) // Permission level 2 = command blocks / operators
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            if (Capes.INSTANCE.getAll().isEmpty()) {
                                source.sendSuccess(() -> Component.literal("No capes loaded."), false);
                                return 0;
                            }

                            source.sendSuccess(() -> Component.literal("Loaded Capes:"), false);
                            for (Map.Entry<ResourceLocation, Cape> entry : Capes.INSTANCE.getAll().entrySet()) {
                                Cape cape = entry.getValue();
                                String message = "- " + cape.name + " (Texture: " + cape.texture + ")";
                                source.sendSuccess(() -> Component.literal(message), false);
                                return 0;
                            }

                            return 1;
                        })
        );
    }
}
