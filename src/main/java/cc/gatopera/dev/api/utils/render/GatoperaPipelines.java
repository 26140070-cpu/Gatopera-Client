package cc.gatopera.dev.api.utils.render;

import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

public final class GatoperaPipelines implements Wrapper {
    private static SimpleFramebuffer blurBuffer;
    private static boolean prepared;

    private GatoperaPipelines() {
    }

    public static void prepareFrame() {
        Framebuffer main = MinecraftClient.getInstance().getFramebuffer();
        if (blurBuffer == null || blurBuffer.textureWidth != main.textureWidth || blurBuffer.textureHeight != main.textureHeight) {
            if (blurBuffer != null) blurBuffer.delete();
            blurBuffer = new SimpleFramebuffer(main.textureWidth, main.textureHeight, false, MinecraftClient.IS_SYSTEM_MAC);
        }
        prepared = true;
    }

    public static boolean isBlurEnabled() {
        return ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.guiBlur.getValue();
    }

    public static boolean isRoundedEnabled() {
        return ClientSetting.INSTANCE == null || ClientSetting.INSTANCE.guiRounded.getValue();
    }

    public static float getRadius() {
        if (ClientSetting.INSTANCE == null) return 12f;
        return ClientSetting.INSTANCE.guiRadius.getValueFloat();
    }

    public static void drawWindowBackground(MatrixStack matrices, float x, float y, float width, float height, Color color) {
        try {
            prepareFrame();
            float radius = isRoundedEnabled() ? getRadius() : 0f;
            if (isBlurEnabled()) {
                try {
                    drawBlurredRegion(x, y, width, height, 6f);
                } catch (Throwable ignored) {
                }
            }
            if (radius > 0.5f) {
                Render2DUtil.drawRound(matrices, x, y, width, height, radius, color);
            } else {
                Render2DUtil.drawRect(matrices, x, y, width, height, color);
            }
        } catch (Throwable ignored) {
        }
    }

    public static void drawPanel(MatrixStack matrices, float x, float y, float width, float height, float radius, Color color) {
        try {
            if (radius <= 0.5f || !isRoundedEnabled()) {
                Render2DUtil.drawRect(matrices, x, y, width, height, color);
                return;
            }
            Render2DUtil.drawRound(matrices, x, y, width, height, radius, color);
        } catch (Throwable ignored) {
        }
    }

    public static void drawButton(MatrixStack matrices, float x, float y, float width, float height, boolean hovered, Color base, Color hover) {
        float radius = isRoundedEnabled() ? Math.min(getRadius(), Math.min(width, height) / 2f) : 0f;
        Color c = hovered ? hover : base;
        drawPanel(matrices, x, y, width, height, radius, c);
    }

    private static void drawBlurredRegion(float x, float y, float width, float height, float strength) {
        if (blurBuffer == null) return;
        Framebuffer main = MinecraftClient.getInstance().getFramebuffer();
        blurBuffer.beginWrite(false);
        main.draw(blurBuffer.textureWidth, blurBuffer.textureHeight, false);
        blurBuffer.endWrite();
        main.beginWrite(false);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, blurBuffer.getColorAttachment());
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);

        float sw = mc.getWindow().getScaledWidth();
        float sh = mc.getWindow().getScaledHeight();
        float u0 = x / sw;
        float v0 = 1f - (y + height) / sh;
        float u1 = (x + width) / sw;
        float v1 = 1f - y / sh;

        Matrix4f matrix = new Matrix4f().ortho(0, sw, sh, 0, 1000, 3000);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrix, x, y + height, 0).texture(u0, v0).next();
        buffer.vertex(matrix, x + width, y + height, 0).texture(u1, v0).next();
        buffer.vertex(matrix, x + width, y, 0).texture(u1, v1).next();
        buffer.vertex(matrix, x, y, 0).texture(u0, v1).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
    }
}