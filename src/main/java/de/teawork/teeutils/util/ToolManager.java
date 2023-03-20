package de.teawork.teeutils.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import de.teawork.teeutils.Reference;
import de.teawork.teeutils.tools.ToolBucketProtect;
import de.teawork.teeutils.tools.ToolDimensionalVolume;
import de.teawork.teeutils.tools.ToolRepair;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;

public class ToolManager {

    public static ToolManager INSTANCE = new ToolManager();

    public ArrayDeque<Tool> tools = new ArrayDeque<>();

    private static final Path STORE_PATH = FabricLoader.getInstance().getConfigDir().resolve(Reference.MOD_ID).resolve("manager.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    public ToolManager() {
        tools.add(ToolRepair.INSTANCE);
        tools.add(ToolDimensionalVolume.INSTANCE);
        tools.add(ToolBucketProtect.INSTANCE);
    }

    public void registerTools() {
        for (Tool tool: tools) {
            tool.register();
        }
        ClientTickEvents.END_CLIENT_TICK.register(this::checkToolToggle);
    }

    public void checkToolToggle(MinecraftClient client) {
        for (Tool tool: tools) {
            if (tool.toggleBind != null) {
                while (tool.toggleBind.wasPressed()) {
                    tool.toggleTool(client);
                }
            }
        }
    }

    public void loadConfig() {
        if (!Files.exists(STORE_PATH)) {
            return;
        }
        Gson gson = new Gson();
        try (Reader fileReader = Files.newBufferedReader(STORE_PATH)) {
            HashMap<String, Boolean> settings = gson.fromJson(new JsonReader(fileReader), new TypeToken<HashMap<String, Boolean>>(){}.getType());
            for (Tool tool: tools) {
                if (settings.containsKey(tool.getName())) {
                    tool.isEnabled = settings.get(tool.getName());
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error("Error reading Tool settings file", e);
        }
    }

    public static void saveConfig() {
        try (Writer writer = Files.newBufferedWriter(STORE_PATH)) {
            Gson gson = new Gson();
            HashMap<String, Boolean> config = new HashMap<>();
            for (Tool tool: INSTANCE.tools) {
                config.put(tool.getName(), tool.isEnabled);
            }
            gson.toJson(config, writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to save dimensional volumes", e);
        }
    }

}
