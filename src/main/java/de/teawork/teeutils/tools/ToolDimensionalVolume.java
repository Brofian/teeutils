package de.teawork.teeutils.tools;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import de.teawork.teeutils.Reference;
import de.teawork.teeutils.util.Tool;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ToolDimensionalVolume extends Tool {

    public static ToolDimensionalVolume INSTANCE = new ToolDimensionalVolume();

    private static final Path STORE_PATH = FabricLoader.getInstance().getConfigDir().resolve(Reference.MOD_ID).resolve("dimensional_volume.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final HashMap<String, HashMap<String, Float>> config = loadConfig();

    @Override
    public String getName() {
        return "Dimensional Volume";
    }

    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal("tdimensional_volume")
                .then(literal("toggle").executes(ctx -> toggleCommand()))
                .then(literal("set").executes(ToolDimensionalVolume::setCurrentCommand))
                .then(literal("reset").executes(ToolDimensionalVolume::resetCurrentCommand)));
    }

    private static int toggleCommand() {
        INSTANCE.toggleTool(MinecraftClient.getInstance());
        return Command.SINGLE_SUCCESS;
    }

    private static int resetCurrentCommand(CommandContext<FabricClientCommandSource> ctx) {
        String world = ctx.getSource().getWorld().getRegistryKey().getValue().toString();
        config.remove(world);
        saveConfig();
        Text feedback = Text.translatable("commands.dimensional_volume.reset.success");
        ctx.getSource().sendFeedback(feedback);
        return Command.SINGLE_SUCCESS;
    }

    private static int setCurrentCommand(CommandContext<FabricClientCommandSource> ctx) {
        HashMap<String, Float> volumes = new HashMap<>();
        for (SoundCategory cat: SoundCategory.values()) {
            volumes.put(cat.getName(), ctx.getSource().getClient().options.getSoundVolume(cat));
        }
        String world = ctx.getSource().getWorld().getRegistryKey().getValue().toString();
        config.put(world, volumes);
        saveConfig();
        Text feedback = Text.translatable("commands.dimensional_volume.set.success");
        ctx.getSource().sendFeedback(feedback);
        return Command.SINGLE_SUCCESS;
    }


    private static HashMap<String, HashMap<String, Float>> loadConfig() {
        if (!Files.exists(STORE_PATH)) {
            return new HashMap<>();
        }
        Gson gson = new Gson();
        try (Reader fileReader = Files.newBufferedReader(STORE_PATH)) {
            return gson.fromJson(new JsonReader(fileReader), new TypeToken<HashMap<String, HashMap<String, Float>>>(){}.getType());
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error("Error reading dimensional volumes file", e);
            return new HashMap<>();
        }
    }

    private static void saveConfig() {
        try (Writer writer = Files.newBufferedWriter(STORE_PATH)) {
            Gson gson = new Gson();
            gson.toJson(config, writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to save dimensional volumes", e);
        }
    }

    public void handleDimensionSwitch(ClientWorld world) {
        if (!isEnabled) return;
        if (world == null) return;
        HashMap<String, Float> settings = config.get(world.getRegistryKey().getValue().toString());
        if (settings == null) return;
        MinecraftClient client = MinecraftClient.getInstance();
        for (SoundCategory cat: SoundCategory.values()) {
            client.options.setSoundVolume(cat, settings.get(cat.getName()));
        }
        if (client.player == null) return;
        client.player.sendMessage(Text.literal("§7" + getName() + ": §2Updated Volume§r"), false);
    }


    @Override
    public void register() {

    }
}
