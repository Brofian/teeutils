package de.teawork.teeutils.util;

import de.teawork.teeutils.gui.part.BasePart;
import de.teawork.teeutils.gui.part.PartMenu;
import de.teawork.teeutils.gui.part.PartMenuEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public abstract class Tool {

    public abstract String getName();

    public KeyBinding toggleBind = null;

    public boolean isEnabled = false;

    public PartMenuEntry enabledMenu;

    public void toggleTool(MinecraftClient client) {
        isEnabled = !isEnabled;
        if (enabledMenu != null) {
            enabledMenu.active = isEnabled;
        }
        ToolManager.saveConfig();
        client.player.sendMessage(Text.literal(getName() + ": " + (isEnabled? "§2Enabled§r" : "§4Disabled§r")), true);
    }

    public void syncEnabledMenu() {
        if (enabledMenu != null) {
            enabledMenu.active = isEnabled;
        }
    }

    public abstract void addConfigPart(PartMenu menu);

    public abstract void register();
}
