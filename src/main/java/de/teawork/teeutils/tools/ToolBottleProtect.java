package de.teawork.teeutils.tools;

import com.mojang.brigadier.Command;
import de.teawork.teeutils.gui.part.PartMenu;
import de.teawork.teeutils.util.Tool;
import net.minecraft.client.MinecraftClient;

public class ToolBottleProtect extends Tool {

    public static ToolBottleProtect INSTANCE = new ToolBottleProtect();

    @Override
    public String getName() {
        return "Bottle Protect";
    }

    @Override
    public void addConfigPart(PartMenu menu) {
        enabledMenu = menu.addEntry("Bottle Protect", () -> {
            toggleTool(MinecraftClient.getInstance());
        });
    }

    @Override
    public void register() {
    }

}
