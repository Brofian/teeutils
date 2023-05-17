package de.teawork.teeutils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import de.teawork.teeutils.gui.GuiMenu;
import de.teawork.teeutils.tools.ToolBucketProtect;
import de.teawork.teeutils.tools.ToolDimensionalVolume;
import de.teawork.teeutils.tools.ToolRepair;
import de.teawork.teeutils.tools.ToolTotemRestock;
import de.teawork.teeutils.util.ToolManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandRegistryAccess;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class TeeUtilsClient implements ClientModInitializer {

    private final ToolManager manager = ToolManager.INSTANCE;

    public static Path configDir;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static KeyBinding OPEN_CONFIG;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(TeeUtilsClient::registerCommands);

        OPEN_CONFIG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.teeutils.open_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_T,
                "category.teeutils.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_CONFIG.wasPressed()) {
                client.setScreen(new GuiMenu());
            }
        });

        configDir = FabricLoader.getInstance().getConfigDir().resolve("teeutils");
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            LOGGER.error("Failed to create config dir", e);
        }

        manager.registerTools();
        manager.loadConfig();
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher,
                                        CommandRegistryAccess registryAccess) {
        ToolRepair.registerCommand(dispatcher, registryAccess);
        ToolDimensionalVolume.registerCommand(dispatcher, registryAccess);
        ToolBucketProtect.registerCommand(dispatcher, registryAccess);
        ToolTotemRestock.registerCommand(dispatcher, registryAccess);
    }

}
