package com.eclectusstudio.eclectuscosmetic.storage;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import com.eclectusstudio.eclectuscosmetic.registry.UnlockedCapeRegistry;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UnlockedCapeStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path saveDir = null;

    private static File getFile() {
        return saveDir.resolve("unlocked.json").toFile();
    }

    public static void init(Path worldSaveDirectory) {
        saveDir = worldSaveDirectory.resolve(EclectusCosmetic.MODID);

        try {
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
                EclectusCosmetic.LOGGER.info("Created cape save dir: {}", saveDir);
            }

            File file = getFile();
            if (!file.exists()) {
                if (file.createNewFile()) {
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write("{}"); // empty JSON object
                    }
                    EclectusCosmetic.LOGGER.info("Created new unlocked.json at {}", file.getAbsolutePath());
                }
            } else {
                EclectusCosmetic.LOGGER.info("Cape save file already exists: {}", file.getAbsolutePath());
            }
        } catch (Exception e) {
            EclectusCosmetic.LOGGER.error("Failed to initialize UnlockedCapeStorage", e);
        }
    }

    public static void loadFromSerializer() {
        UnlockedCapeRegistry.UNLOCKED_CAPES.clear();
        File file = getFile();
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            if (obj == null) return;

            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                UUID uuid = UUID.fromString(entry.getKey());
                JsonArray array = entry.getValue().getAsJsonArray();
                Set<ResourceLocation> capes = new HashSet<>();
                for (JsonElement el : array) {
                    ResourceLocation id = ResourceLocation.tryParse(el.getAsString());
                    if (id != null) capes.add(id);
                }
                UnlockedCapeRegistry.UNLOCKED_CAPES.put(uuid, capes);
            }
        } catch (Exception e) {
            EclectusCosmetic.LOGGER.error("Failed to load unlocked capes", e);
        }
    }

    public static void saveToSerializer() {
        if (saveDir == null) {
            EclectusCosmetic.LOGGER.warn("Cannot save unlocked capes: UnlockedCapeStorage is not initialized!");
            return;
        }
        File file = getFile();
        JsonObject obj = new JsonObject();

        for (Map.Entry<UUID, Set<ResourceLocation>> entry : UnlockedCapeRegistry.UNLOCKED_CAPES.entrySet()) {
            JsonArray array = new JsonArray();
            for (ResourceLocation cape : entry.getValue()) {
                array.add(cape.toString());
            }
            obj.add(entry.getKey().toString(), array);
        }

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(obj, writer);
        } catch (Exception e) {
            EclectusCosmetic.LOGGER.error("Failed to save unlocked capes", e);
        }
    }
}
