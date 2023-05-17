package de.teawork.teeutils.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {

    public static void setupBlend() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        drawRect(x, y, width, height, color, 0f);
    }

    public static void drawRect(int x, int y, int width, int height, int color, float zLevel) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >>  8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.applyModelViewMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        setupBlend();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(x, y, zLevel).color(r, g, b, a).next();
        buffer.vertex(x, y + height, zLevel).color(r, g, b, a).next();
        buffer.vertex(x + width, y + height, zLevel).color(r, g, b, a).next();
        buffer.vertex(x + width, y, zLevel).color(r, g, b, a).next();
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void drawOutline(int x, int y, int width, int height, int colorBorder, float zLevel)
    {
        drawRect(x                    , y,      1, height, colorBorder, zLevel); // left edge
        drawRect(x + width - 1        , y,      1, height, colorBorder, zLevel); // right edge
        drawRect(x + 1,              y, width - 2,      1, colorBorder, zLevel); // top edge
        drawRect(x + 1, y + height - 1, width - 2,      1, colorBorder, zLevel); // bottom edge
    }

    public static void drawOutline(int x, int y, int width, int height, int borderWidth, int colorBorder)
    {
        drawOutline(x, y, width, height, borderWidth, colorBorder, 0f);
    }

    public static void drawOutline(int x, int y, int width, int height, int borderWidth, int colorBorder, float zLevel)
    {
        drawRect(x                      ,                        y, borderWidth            , height     , colorBorder, zLevel); // left edge
        drawRect(x + width - borderWidth,                        y, borderWidth            , height     , colorBorder, zLevel); // right edge
        drawRect(x + borderWidth        ,                        y, width - 2 * borderWidth, borderWidth, colorBorder, zLevel); // top edge
        drawRect(x + borderWidth        , y + height - borderWidth, width - 2 * borderWidth, borderWidth, colorBorder, zLevel); // bottom edge
    }

    public static void renderText(int x, int y, int color, String text, MatrixStack matrixStack) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        renderer.draw(matrixStack, text, x, y, color);

    }

    public static void renderTextRight(int x, int y, int color, String text, MatrixStack matrixStack) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int width = renderer.getWidth(text);
        renderText(x-width, y, color, text, matrixStack);
    }

    public static void renderTextCenter(int x, int y, int color, String text, MatrixStack matrixStack) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int width = renderer.getWidth(text);
        renderText(x-(width/2), y, color, text, matrixStack);
    }
}
