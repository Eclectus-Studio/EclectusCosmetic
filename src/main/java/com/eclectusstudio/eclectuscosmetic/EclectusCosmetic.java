package com.eclectusstudio.eclectuscosmetic;

import com.eclectusstudio.eclectuscosmetic.command.GetCapesCommand;
import com.eclectusstudio.eclectuscosmetic.command.SetCapeCommand;
import com.eclectusstudio.eclectuscosmetic.command.UnlockCapeCommand;
import com.eclectusstudio.eclectuscosmetic.command.UnlockedCapesCommand;
import com.eclectusstudio.eclectuscosmetic.data.advancementcape.AdvancementCapes;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.event.AdvancementUnlockEventHandler;
import com.eclectusstudio.eclectuscosmetic.event.PlayerJoinEventHandler;
import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import com.eclectusstudio.eclectuscosmetic.storage.EquippedCapeStorage;
import com.eclectusstudio.eclectuscosmetic.storage.UnlockedCapeStorage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(EclectusCosmetic.MODID)
public class EclectusCosmetic {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "eclectuscosmetic";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EclectusCosmetic(IEventBus modEventBus, ModContainer modContainer) {

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        // Gameplay events MUST go to NeoForge.EVENT_BUS:
        NeoForge.EVENT_BUS.addListener(this::onReload);
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);

        // Register gameplay event handlers
        NeoForge.EVENT_BUS.register(new PlayerJoinEventHandler());
        NeoForge.EVENT_BUS.register(new AdvancementUnlockEventHandler());
        modEventBus.register(new EclectusCosmeticNetworking());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Starting Eclectus Cosmetic Server Side");
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        Path worldSavePath = level.getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent();
        LOGGER.error(worldSavePath.toString());
        EquippedCapeStorage.init(worldSavePath);
        UnlockedCapeStorage.init(worldSavePath);

        EquippedCapeStorage.loadFromSerializer();
        UnlockedCapeStorage.loadFromSerializer();
    }


    @SubscribeEvent
    public void onWorldSave(LevelEvent.Save event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        EquippedCapeStorage.saveToSerializer();
        UnlockedCapeStorage.saveToSerializer();
    }

    private void onReload(AddReloadListenerEvent event) {
        event.addListener(AdvancementCapes.INSTANCE);
        event.addListener(Capes.INSTANCE);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        GetCapesCommand.register(event.getDispatcher());
        SetCapeCommand.register(event.getDispatcher());
        UnlockCapeCommand.register(event.getDispatcher());
        UnlockedCapesCommand.register(event.getDispatcher());
    }
}
