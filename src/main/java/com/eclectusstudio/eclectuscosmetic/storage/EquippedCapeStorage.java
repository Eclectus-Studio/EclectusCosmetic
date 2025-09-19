package com.eclectusstudio.eclectuscosmetic.storage;

import com.eclectusstudio.eclectuscosmetic.EclectusCosmetic;
import com.eclectusstudio.eclectuscosmetic.registry.CapeRegistry;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class EquippedCapeStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path saveDir = null;

    private static File getFile() {
        return saveDir.resolve("equipped.json").toFile();
    }

    public static void init(Path worldSaveDirectory) {
        saveDir = worldSaveDirectory.resolve(EclectusCosmetic.MODID);
        File file = getFile();

        try {
            // Create directory if missing
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
                EclectusCosmetic.LOGGER.info("Created folder: {}", saveDir);
            }

            // Create file if missing
            if (!file.exists()) {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]"); // empty JSON array
                }
                EclectusCosmetic.LOGGER.info("Created file: {}", file);
            } else {
                EclectusCosmetic.LOGGER.info("File already exists: {}", file);
            }
        } catch (Exception e) {
            EclectusCosmetic.LOGGER.error("Failed to initialize EquippedCapeStorage", e);
        }
    }

    public static void loadFromSerializer() {
        if (saveDir == null) {
            throw new IllegalStateException("EquippedCapeStorage was not initialized with a path!");
        }

        File file = getFile();
        CapeRegistry.CAPES.clear();

        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            JsonArray array = GSON.fromJson(reader, JsonArray.class);
            if (array == null) return;

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                UUID uuid = UUID.fromString(obj.get("uuid").getAsString());
                ResourceLocation cape = ResourceLocation.tryParse(obj.get("cape").getAsString());
                CapeRegistry.setCape(uuid, cape);
            }
            EclectusCosmetic.LOGGER.info("Loaded {} equipped capes", CapeRegistry.CAPES.size());
        } catch (Exception e) {
            EclectusCosmetic.LOGGER.error("Failed to load equipped capes", e);
        }
    }

    public static void saveToSerializer() {
        if (saveDir == null) {
            throw new IllegalStateException("EquippedCapeStorage was not initialized with a path!");
        }

        File file = getFile();

        try (FileWriter writer = new FileWriter(file)) {
            JsonArray array = new JsonArray();
            for (Map.Entry<UUID, ResourceLocation> entry : CapeRegistry.CAPES.entrySet()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("uuid", entry.getKey().toString());
                obj.addProperty("cape", entry.getValue().toString());
                array.add(obj);
            }
            GSON.toJson(array, writer);
        } catch (Exception e) {
            EclectusCosmetic.LOGGER.error("Failed to save equipped capes", e);
        }
    }
}
