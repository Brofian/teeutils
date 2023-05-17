package de.teawork.teeutils.gui;

import de.teawork.teeutils.gui.part.BasePart;
import de.teawork.teeutils.gui.part.PartMenu;
import de.teawork.teeutils.gui.part.PartMenuEntry;
import de.teawork.teeutils.util.Tool;
import de.teawork.teeutils.util.ToolManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class GuiMenu extends Screen {

    @Nullable
    private Screen parent;

    private final PartMenu menu = new PartMenu(5, 5, "", true, null);

    private PartMenu activeMenu;

    public GuiMenu() {
        super(ScreenTexts.EMPTY);
        activeMenu = menu;
        for (Tool tool: ToolManager.INSTANCE.tools) {
            tool.addConfigPart(menu);
            tool.syncEnabledMenu();
        }
        activeMenu.enterMenu();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_DOWN -> activeMenu.selectNext();
            case GLFW.GLFW_KEY_UP -> activeMenu.selectPrev();
            case GLFW.GLFW_KEY_RIGHT -> {
                BasePart bp = activeMenu.getSelected();
                if (bp instanceof PartMenu) {
                    activeMenu.exitMenu();
                    activeMenu.selected = false;
                    bp.selected = true;
                    activeMenu = (PartMenu) bp;
                    activeMenu.enterMenu();
                }
            }
            case GLFW.GLFW_KEY_LEFT -> {
                if (activeMenu.parent != null) {
                    activeMenu.selected = false;
                    activeMenu.exitMenu();
                    activeMenu = activeMenu.parent;
                    activeMenu.enterMenu();
                }
            }
            case GLFW.GLFW_KEY_ENTER -> {
                BasePart selected = activeMenu.getSelected();
                if (selected instanceof PartMenuEntry) {
                    ((PartMenuEntry) selected).triggerAction();
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public GuiMenu setParent(@Nullable Screen parent) {
        if (parent == null || parent.getClass() != this.getClass()) {
            this.parent = parent;
        }
        return this;
    }

    @Nullable
    public Screen getParent() {
        return this.parent;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawBackground(mouseX, mouseY);
        this.menu.render(matrices, delta);
    }

    protected void drawBackground(int mouseX, int mouseY) {
        RenderUtils.drawRect(0, 0, this.width, this.height, 0xB0000000);
    }
}
