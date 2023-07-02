package de.teawork.teeutils.gui.part;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public abstract class BasePart {

    public int x;
    public int y;

    public final int COLOR_INACTIVE = 0x99222222;
    public final int COLOR_ACTIVE = 0x9932a852;

    public final int COLOR_BORDER_ACTIVE = 0xdd32a852;
    public final int COLOR_BORDER_INACTIVE = 0xdd222222;

    public final int COLOR_BORDER_SELECTED = 0xddfa2a00;

    public boolean selected = false;

    public BasePart(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(DrawContext context, float delta);
}
