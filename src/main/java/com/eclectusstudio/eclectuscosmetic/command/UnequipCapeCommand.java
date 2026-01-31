package com.eclectusstudio.eclectuscosmetic.command;

import com.eclectusstudio.eclectuscosmetic.packet.record.ClearPlayerCapeData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class UnequipCapeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("unequipcape")
                        .requires(source -> source.hasPermission(0))
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            PacketDistributor.sendToAllPlayers(new ClearPlayerCapeData(context.getSource().getPlayer().getStringUUID()));
                            source.sendSuccess(() -> Component.literal("Unequipped your cape!"), false);

                            return 0;
                        })
        );
    }
}
