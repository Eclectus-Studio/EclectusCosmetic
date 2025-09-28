package com.eclectusstudio.eclectuscosmetic.command;

import com.eclectusstudio.eclectuscosmetic.data.cape.Cape;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import com.eclectusstudio.eclectuscosmetic.packet.capes.CapeUserSend;
import com.eclectusstudio.eclectuscosmetic.registry.CapeRegistry;
import com.eclectusstudio.eclectuscosmetic.registry.UnlockedCapeRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;


import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SetCapeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setcape")
                .then(Commands.argument("cape_id", ResourceLocationArgument.id())
                        .suggests((context, builder) -> suggestCapes(builder))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            ResourceLocation capeId = ResourceLocationArgument.getId(ctx, "cape_id");

                            // Lookup the cape JSON using the ResourceLocation
                            Cape cape = Capes.INSTANCE.getCape(capeId);
                            if (cape == null) {
                                ctx.getSource().sendFailure(Component.literal("Invalid cape ID: " + capeId));
                                sendCapeListTooltip(ctx.getSource());
                                return 0;
                            }

                            if (!UnlockedCapeRegistry.hasUnlocked(player.getUUID(), capeId)) {
                                ctx.getSource().sendFailure(Component.literal("❌ You have not unlocked this cape yet!"));
                                return 0;
                            }

                            // Register the cape for the player
                            CapeRegistry.setCape(player.getUUID(), capeId);

                            // Send the ACTUAL texture to all clients
                            EclectusCosmeticNetworking.sendToAllClients(
                                    new CapeUserSend(player.getUUID(), cape.texture)
                            );
                            ctx.getSource().sendSuccess(() -> Component.literal("✅ Set your cape to: " + cape.name), false);
                            return 1;
                        })));
    }

    private static CompletableFuture<Suggestions> suggestCapes(SuggestionsBuilder builder) {
        for (ResourceLocation id : Capes.INSTANCE.getAll().keySet()) {
            builder.suggest(id.toString());
        }
        return builder.buildFuture();
    }

    private static void sendCapeListTooltip(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Available Capes:"), false);
        for (Map.Entry<ResourceLocation, Cape> entry : Capes.INSTANCE.getAll().entrySet()) {
            Cape cape = entry.getValue();
            String message = "- " + cape.name + " (Texture: " + cape.texture + ")";
            source.sendSuccess(() -> Component.literal(message), false);
        }
    }
}
