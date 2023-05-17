package de.teawork.teeutils.tools;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.teawork.teeutils.gui.part.PartMenu;
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
    public void addConfigPart(PartMenu menu) {
        enabledMenu = menu.addEntry("Bucket Protect", () -> {
            toggleTool(MinecraftClient.getInstance());
        });
    }

    @Override
    public void register() {
    }

}
