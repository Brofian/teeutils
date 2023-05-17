package de.teawork.teeutils.gui.part;

import de.teawork.teeutils.gui.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class PartMenu extends BasePart{

    private ArrayList<BasePart> entries = new ArrayList<>();

    private int selected_part = 0;
    public String name;

    private int yo = 0;

    public boolean active;

    public PartMenu parent;

    public final int COLOR_MENU = 0x99318794;
    public final int COLOR_BORDER_MENU = 0xdd318794;

    public int width;

    public PartMenu(int x, int y, String name, boolean active, PartMenu parent) {
        super(x, y);
        this.name = name;
        this.active = active;
        this.parent = parent;
    }

    public void selectNext() {
        if (this.entries.size() > selected_part+1) {
            selected_part += 1;
            this.entries.get(selected_part-1).selected = false;
            this.entries.get(selected_part).selected = true;
        }
    }

    public void selectPrev() {
        if (selected_part > 0) {
            selected_part -= 1;
            this.entries.get(selected_part+1).selected = false;
            this.entries.get(selected_part).selected = true;
        }
    }

    public void exitMenu() {
        for (BasePart bp: entries) {
            bp.selected = false;
        }
    }

    public void enterMenu() {
        this.entries.get(selected_part).selected = true;
    }

    public BasePart getSelected() {
        return entries.get(selected_part);
    }

    public PartMenuEntry addEntry(String text, ICallable trigger, boolean isToggle) {
        PartMenuEntry me = new PartMenuEntry(this, this.x, this.y + this.yo, text, trigger, isToggle);
        this.entries.add(me);
        this.yo += MinecraftClient.getInstance().textRenderer.fontHeight + 10;
        int w = MinecraftClient.getInstance().textRenderer.getWidth(text);
        if (w > width) {
            width = w;
            updatePartWidth();
        }
        return me;
    }

    public PartMenuEntry addEntry(String text, ICallable trigger) {
        return addEntry(text, trigger, true);
    }

    public PartMenu addMenuEntry(String name) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        PartMenu me = new PartMenu(this.x + 10 + width, this.y + this.yo, name, false, this);
        this.yo += MinecraftClient.getInstance().textRenderer.fontHeight + 10;
        this.entries.add(me);
        int w = tr.getWidth(name);
        if (w > width) {
            width = w;
            updatePartWidth();
        }
        return me;
    }

    public void updatePartWidth() {
        for (BasePart part: entries) {
            if (part instanceof PartMenu) {
                part.x = this.x + 10 + width;
                ((PartMenu) part).updatePartWidth();
            } else {
                part.x = this.x;
            }
        }
    }

    @Override
    public void render(MatrixStack stack, float delta) {
        if (parent != null) {
            int px = parent.x;
            TextRenderer tr = MinecraftClient.getInstance().textRenderer;
            int height = tr.fontHeight + 8;
            int bc = this.selected?COLOR_BORDER_SELECTED:COLOR_BORDER_MENU;
            RenderUtils.drawRect(px, this.y, parent.width + 8, height, COLOR_MENU);
            RenderUtils.drawOutline(px, this.y, parent.width + 8, height, 1, bc);
            RenderUtils.renderText(px+4, this.y+4, 0xFFFFFFFF, this.name, stack);
        }
        if (this.selected || parent == null) {
            for (BasePart e: this.entries) {
                e.render(stack, delta);
            }
        }
    }
}
