package com.eclectusstudio.eclectuscosmetic;

import com.eclectusstudio.eclectuscosmetic.command.GetCapesCommand;
import com.eclectusstudio.eclectuscosmetic.command.SetCapeCommand;
import com.eclectusstudio.eclectuscosmetic.data.cape.Capes;
import com.eclectusstudio.eclectuscosmetic.packet.EclectusCosmeticNetworking;
import com.eclectusstudio.eclectuscosmetic.storage.EquippedCapeStorage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EclectusCosmetic.MODID)
public class EclectusCosmetic {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "eclectuscosmetic";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public EclectusCosmetic(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            event.addListener(Capes.INSTANCE);
        });

        //Commands
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            GetCapesCommand.register(event.getDispatcher());
            SetCapeCommand.register(event.getDispatcher());
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        EclectusCosmeticNetworking.register();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        Path worldSavePath = level.getServer().getWorldPath(LevelResource.ROOT);
        EquippedCapeStorage.init(worldSavePath);

        EquippedCapeStorage.loadFromSerializer();
    }


    @SubscribeEvent
    public void onWorldSave(LevelEvent.Save event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        EquippedCapeStorage.saveToSerializer();
    }
}
