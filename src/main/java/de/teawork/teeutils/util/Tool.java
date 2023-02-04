package de.teawork.teeutils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public abstract class Tool {

    public abstract String getName();

    public KeyBinding toggleBind = null;

    public boolean isEnabled = false;

    public void toggleTool(MinecraftClient client) {
        isEnabled = !isEnabled;
        ToolManager.saveConfig();
        client.player.sendMessage(Text.literal(getName() + ": " + (isEnabled? "§2Enabled§r" : "§4Disabled§r")), true);
    }

    public abstract void register();
}
