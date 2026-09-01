package cc.gatopera.dev.api.utils.render;

import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.awt.Color;

public final class GatoperaPipelines implements Wrapper {
    private static SimpleFramebuffer capture;
    private static SimpleFramebuffer blurTemp;

    private GatoperaPipelines() {
    }

    private static final Identifier BLANK_TEXTURE =
            new Identifier("minecraft", "textures/misc/white.png");

    public static boolean isBlurEnabled() {
        return ClientSetting.INSTANCE == null
                || ClientSetting.INSTANCE.guiBlur.getValue();
    }

    public static boolean isRoundedEnabled() {
        return ClientSetting.INSTANCE == null
                || ClientSetting.INSTANCE.guiRounded.getValue();
    }

    public static float getRadius() {
        if (ClientSetting.INSTANCE == null) {
            return 12f;
        }
        return ClientSetting.INSTANCE.guiRadius.getValueFloat();
    }

    private static void ensureBuffers() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();

        if (capture == null
                || capture.textureWidth != main.textureWidth
                || capture.textureHeight != main.textureHeight) {

            if (capture != null) {
                capture.delete();
            }
            if (blurTemp != null) {
                blurTemp.delete();
            }

            capture = new SimpleFramebuffer(
                    main.textureWidth,
                    main.textureHeight,
                    false,
                    MinecraftClient.IS_SYSTEM_MAC
            );

            blurTemp = new SimpleFramebuffer(
                    main.textureWidth,
                    main.textureHeight,
                    false,
                    MinecraftClient.IS_SYSTEM_MAC
            );
        }
    }

    public static void drawWindowBackground(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            Color color
    ) {
        try {
            float radius = isRoundedEnabled() ? getRadius() : 0f;

            if (isBlurEnabled() && GuiShaders.available()) {
                drawBlurredRegion(matrices, x, y, width, height, Math.max(4f, radius * 0.5f));
            }

            Color panel = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    Math.min(160, color.getAlpha())
            );

            if (radius > 0.5f) {
                Render2DUtil.drawRound(matrices, x, y, width, height, radius, panel);
            } else {
                Render2DUtil.drawRect(matrices, x, y, width, height, panel);
            }
        } catch (Throwable ignored) {
        }
    }

    public static void drawBlurredFullscreen(MatrixStack matrices, float radius) {
        if (!isBlurEnabled() || !GuiShaders.available()) {
            return;
        }

        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            Framebuffer main = mc.getFramebuffer();
            ensureBuffers();

            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.fbo);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, capture.fbo);
            GL30.glBlitFramebuffer(
                    0, 0, main.textureWidth, main.textureHeight,
                    0, 0, capture.textureWidth, capture.textureHeight,
                    GL30.GL_COLOR_BUFFER_BIT, GL30.GL_LINEAR
            );
            main.beginWrite(false);

            runBlurPass(capture, blurTemp, Math.max(2f, radius), 1f, 0f);
            runBlurPass(blurTemp, capture, Math.max(2f, radius), 0f, 1f);

            float w = mc.getWindow().getScaledWidth();
            float h = mc.getWindow().getScaledHeight();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, capture.getColorAttachment());
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            buffer.vertex(matrix, 0, h, 0).texture(0f, 0f).next();
            buffer.vertex(matrix, w, h, 0).texture(1f, 0f).next();
            buffer.vertex(matrix, w, 0, 0).texture(1f, 1f).next();
            buffer.vertex(matrix, 0, 0, 0).texture(0f, 1f).next();
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableBlend();
        } catch (Throwable ignored) {
        }
    }

    public static void drawPanel(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius,
            Color color
    ) {
        try {
            if (radius <= 0.5f || !isRoundedEnabled()) {
                Render2DUtil.drawRect(matrices, x, y, width, height, color);
                return;
            }
            Render2DUtil.drawRound(matrices, x, y, width, height, radius, color);
        } catch (Throwable ignored) {
        }
    }

    public static void drawButton(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            boolean hovered,
            Color base,
            Color hover
    ) {
        float radius = isRoundedEnabled()
                ? Math.min(getRadius(), Math.min(width, height) / 2f)
                : 0f;

        if (isBlurEnabled() && GuiShaders.available()) {
            drawBlurredRegion(matrices, x, y, width, height, Math.max(2f, radius * 0.4f));
        }

        if (radius > 0.5f) {
            Render2DUtil.drawRound(matrices, x, y, width, height, radius, hovered ? hover : base);
        } else {
            Render2DUtil.drawRect(matrices, x, y, width, height, hovered ? hover : base);
        }
    }

    private static void drawBlurredRegion(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius
    ) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();

        ensureBuffers();

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.fbo);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, capture.fbo);
        GL30.glBlitFramebuffer(
                0, 0, main.textureWidth, main.textureHeight,
                0, 0, capture.textureWidth, capture.textureHeight,
                GL30.GL_COLOR_BUFFER_BIT, GL30.GL_LINEAR
        );
        main.beginWrite(false);

        runBlurPass(capture, blurTemp, radius, 1f, 0f);
        runBlurPass(blurTemp, capture, radius, 0f, 1f);

        float fbW = (float) capture.textureWidth;
        float fbH = (float) capture.textureHeight;
        float scale = (float) mc.getWindow().getScaleFactor();

        float px0 = x * scale;
        float py0 = y * scale;
        float px1 = (x + width) * scale;
        float py1 = (y + height) * scale;

        float u0 = px0 / fbW;
        float u1 = px1 / fbW;
        float v0 = 1.0f - (py1 / fbH);
        float v1 = 1.0f - (py0 / fbH);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, capture.getColorAttachment());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(matrix, x, y + height, 0).texture(u0, v0).next();
        buffer.vertex(matrix, x + width, y + height, 0).texture(u1, v0).next();
        buffer.vertex(matrix, x + width, y, 0).texture(u1, v1).next();
        buffer.vertex(matrix, x, y, 0).texture(u0, v1).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    private static void runBlurPass(
            Framebuffer src,
            Framebuffer dst,
            float radius,
            float dirX,
            float dirY
    ) {
        ShaderProgram shader = GuiShaders.BLUR;
        if (shader == null) {
            return;
        }

        dst.beginWrite(true);

        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderTexture(0, src.getColorAttachment());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (shader.getUniform("OutSize") != null) {
            shader.getUniform("OutSize").set(
                    (float) dst.textureWidth,
                    (float) dst.textureHeight
            );
        }
        if (shader.getUniform("Radius") != null) {
            shader.getUniform("Radius").set(radius);
        }
        if (shader.getUniform("Direction") != null) {
            shader.getUniform("Direction").set(dirX, dirY);
        }

        Matrix4f identity = new Matrix4f().identity();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(identity, -1, -1, 0).texture(0, 0).next();
        buffer.vertex(identity, 1, -1, 0).texture(1, 0).next();
        buffer.vertex(identity, 1, 1, 0).texture(1, 1).next();
        buffer.vertex(identity, -1, 1, 0).texture(0, 1).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        dst.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
    }
}