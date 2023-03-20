package de.teawork.teeutils.tools;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.teawork.teeutils.util.Tool;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;

public class ToolBucketProtect extends Tool {

    public static ToolBucketProtect INSTANCE = new ToolBucketProtect();

    @Override
    public String getName() {
        return "Bucket Protect";
    }

    @Override
    public void register() {
    }

    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("tbucket_protect").executes(ctx -> toggleCommand()));
    }

    private static int toggleCommand() {
        INSTANCE.toggleTool(MinecraftClient.getInstance());
        return Command.SINGLE_SUCCESS;
    }

}
