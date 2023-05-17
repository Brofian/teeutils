package de.teawork.teeutils.gui.part;

import de.teawork.teeutils.gui.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class PartMenuEntry extends BasePart{

    private String text;
    private ICallable trigger;

    public boolean active = false;

    public boolean isToggle = false;
    private float deltaCooldown = 0;

    private PartMenu parent;

    public PartMenuEntry(PartMenu parent, int x, int y, String text, ICallable trigger) {
        super(x, y);
        this.parent = parent;
        this.text = text;
        this.trigger = trigger;
    }

    public PartMenuEntry(PartMenu parent, int x, int y, String text, ICallable trigger, boolean isToggle) {
        super(x, y);
        this.parent = parent;
        this.text = text;
        this.isToggle = isToggle;
        this.trigger = trigger;
    }

    public void triggerAction() {
        active = !active;
        if (!isToggle) {
            deltaCooldown = 1;
        }
        if (trigger != null) {
            this.trigger.called();
        }
    }

    @Override
    public void render(MatrixStack stack, float delta) {
        if (deltaCooldown > 0 && !isToggle) {
            deltaCooldown -= delta;
            if (deltaCooldown <= 0) {
                active = false;
            }
        }
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        int height = tr.fontHeight + 8;
        int bc = this.selected?COLOR_BORDER_SELECTED:(this.active?COLOR_BORDER_ACTIVE:COLOR_BORDER_INACTIVE);
        RenderUtils.drawRect(this.x, this.y, parent.width + 8, height, this.active?COLOR_ACTIVE:COLOR_INACTIVE);
        RenderUtils.drawOutline(this.x, this.y, parent.width + 8, height, 1, bc);
        RenderUtils.renderText(this.x+4, this.y+4, 0xFFFFFFFF, this.text, stack);
    }
}
