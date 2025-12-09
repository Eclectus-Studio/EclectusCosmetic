package com.eclectusstudio.eclectuscosmetic.command;

import com.eclectusstudio.eclectuscosmetic.data.cape.Cape;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.registry.UnlockedCapeRegistry;
import com.eclectusstudio.eclectuscosmetic.storage.UnlockedCapeStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UnlockCapeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("unlockcape")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("cape_id", ResourceLocationArgument.id())
                        .suggests((ctx, builder) -> suggestCapes(builder))
                        .executes(ctx -> {
                            ResourceLocation capeId = ResourceLocationArgument.getId(ctx, "cape_id");
                            try {
                                if (ctx.getSource().getEntity() instanceof ServerPlayer player) {
                                    // Player sender unlocks for self
                                    return unlockCape(ctx.getSource(), player, capeId);
                                } else {
                                    ctx.getSource().sendFailure(Component.literal(
                                            "Console cannot unlock capes for itself, use /unlockcape <cape_id> <player>"
                                    ));
                                    return 0;
                                }
                            } catch (Exception e) {
                                ctx.getSource().sendFailure(Component.literal("Error unlocking cape: " + e.getMessage()));
                                return 0;
                            }
                        })
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes(ctx -> {
                                    ResourceLocation capeId = ResourceLocationArgument.getId(ctx, "cape_id");
                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "player");
                                    int unlockedCount = 0;
                                    for (ServerPlayer target : targets) {
                                        try {
                                            unlockedCount += unlockCape(ctx.getSource(), target, capeId);
                                        } catch (Exception e) {
                                            ctx.getSource().sendFailure(Component.literal(
                                                    "Failed to unlock for " + target.getName().getString() + ": " + e.getMessage()
                                            ));
                                        }
                                    }
                                    // Always return at least 1 if something unlocked
                                    return unlockedCount > 0 ? unlockedCount : 1;
                                }))));
    }

    private static int unlockCape(CommandSourceStack source, ServerPlayer player, ResourceLocation capeId) {
        Cape cape = Capes.INSTANCE.getCape(capeId);
        if (cape == null) {
            source.sendFailure(Component.literal("Invalid cape ID: " + capeId));
            sendCapeListTooltip(source);
            return 0;
        }

        UnlockedCapeRegistry.unlockCape(player.getUUID(), capeId, player.getServer());
        UnlockedCapeStorage.saveToSerializer();

        if (source.getEntity() == player) {
            source.sendSuccess(() -> Component.literal("✅ You unlocked cape: " + cape.name), false);
        } else {
            source.sendSuccess(() -> Component.literal("✅ Unlocked cape " + cape.name + " for " + player.getName().getString()), true);
        }
        return 1;
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
