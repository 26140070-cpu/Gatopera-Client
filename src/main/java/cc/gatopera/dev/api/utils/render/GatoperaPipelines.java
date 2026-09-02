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
    private static boolean frozen;
    private static long frozenAtNanos;
    private static final long FROZEN_MAX_AGE_NANOS = 200_000_000L;

    private GatoperaPipelines() {
    }

    private static final Identifier BLANK_TEXTURE = new Identifier("minecraft", "textures/blank.png");

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

            capture.setClearColor(0f, 0f, 0f, 0f);
            capture.clear(MinecraftClient.IS_SYSTEM_MAC);
            blurTemp.setClearColor(0f, 0f, 0f, 0f);
            blurTemp.clear(MinecraftClient.IS_SYSTEM_MAC);
            main.beginWrite(false);
        }
    }

    private static boolean checkFramebufferComplete(String label) {
        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("[Gatopera] framebuffer incompleto en " + label + ": 0x" + Integer.toHexString(status));
            return false;
        }
        return true;
    }

    private static boolean drainGlErrors(String label) {
        boolean hadError = false;
        int error;
        while ((error = GL30.glGetError()) != GL30.GL_NO_ERROR) {
            hadError = true;
            System.err.println("[Gatopera] error GL en " + label + ": 0x" + Integer.toHexString(error));
        }
        return !hadError;
    }

    private static Framebuffer captureBlurred(float radius) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();
        ensureBuffers();

        boolean ok = runBlurPass(main, blurTemp, radius, 1f, 0f);
        ok = runBlurPass(blurTemp, capture, radius, 0f, 1f) && ok;

        main.beginWrite(false);

        return ok ? capture : null;
    }

    private static Framebuffer captureSharp() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();
        ensureBuffers();

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.fbo);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, capture.fbo);
        boolean complete = checkFramebufferComplete("captureSharp destino");
        GL30.glBlitFramebuffer(
                0, 0, main.textureWidth, main.textureHeight,
                0, 0, capture.textureWidth, capture.textureHeight,
                GL30.GL_COLOR_BUFFER_BIT, GL30.GL_LINEAR
        );
        boolean ok = complete && drainGlErrors("captureSharp blit");
        main.beginWrite(false);

        return ok ? capture : null;
    }

    public static void beginFrameBlur(float radius) {
        if (!isBlurEnabled() || !GuiShaders.available()) {
            frozen = false;
            return;
        }
        captureBlurred(radius);
        frozen = true;
        frozenAtNanos = System.nanoTime();
    }

    public static void endFrameBlur() {
        frozen = false;
    }

    private static boolean isFrozenValid() {
        if (!frozen) {
            return false;
        }
        if (System.nanoTime() - frozenAtNanos > FROZEN_MAX_AGE_NANOS) {
            frozen = false;
            return false;
        }
        return true;
    }

    private static void drawRoundedBlurredPanel(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius,
            Color color,
            boolean allowBlur
    ) {
        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            boolean shadersReady = GuiShaders.available();
            boolean rounded = isRoundedEnabled() && radius > 0.5f && shadersReady;
            boolean blur = allowBlur && isBlurEnabled() && shadersReady;

            if (!rounded) {
                if (blur) {
                    drawBlurredRegion(matrices, x, y, width, height, Math.max(2f, radius * 0.4f));
                }
                if (radius > 0.5f) {
                    Render2DUtil.drawRound(matrices, x, y, width, height, radius, color);
                } else {
                    Render2DUtil.drawRect(matrices, x, y, width, height, color);
                }
                return;
            }

            Framebuffer source = blur
                    ? (isFrozenValid() ? capture : captureBlurred(Math.max(4f, radius * 0.5f)))
                    : captureSharp();

            if (source == null) {
                drawBlankFallback(matrices, x, y, width, height, color);
                return;
            }

            Framebuffer main = mc.getFramebuffer();
            float scale = (float) mc.getWindow().getScaleFactor();
            float fbW = (float) main.textureWidth;
            float fbH = (float) main.textureHeight;

            ShaderProgram shader = GuiShaders.ROUNDED;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(() -> shader);
            RenderSystem.setShaderTexture(0, source.getColorAttachment());

            if (shader.getUniform("ScreenSize") != null) {
                shader.getUniform("ScreenSize").set(fbW, fbH);
            }
            if (shader.getUniform("RectPos") != null) {
                shader.getUniform("RectPos").set(x * scale, y * scale);
            }
            if (shader.getUniform("RectSize") != null) {
                shader.getUniform("RectSize").set(width * scale, height * scale);
            }
            if (shader.getUniform("Radius") != null) {
                shader.getUniform("Radius").set(radius * scale);
            }
            if (shader.getUniform("Smoothness") != null) {
                shader.getUniform("Smoothness").set(0.75f);
            }
            if (shader.getUniform("ColorModulator") != null) {
                shader.getUniform("ColorModulator").set(
                        color.getRed() / 255f,
                        color.getGreen() / 255f,
                        color.getBlue() / 255f,
                        color.getAlpha() / 255f
                );
            }

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            buffer.vertex(matrix, x, y + height, 0).texture(0f, 1f).next();
            buffer.vertex(matrix, x + width, y + height, 0).texture(1f, 1f).next();
            buffer.vertex(matrix, x + width, y, 0).texture(1f, 0f).next();
            buffer.vertex(matrix, x, y, 0).texture(0f, 0f).next();
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.disableBlend();
        } catch (Throwable ignored) {
        }
    }

    private static void drawBlankFallback(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            Color color
    ) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, BLANK_TEXTURE);
        RenderSystem.setShaderColor(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f
        );

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(matrix, x, y + height, 0).texture(0f, 1f).color(1f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x + width, y + height, 0).texture(1f, 1f).color(1f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x + width, y, 0).texture(1f, 0f).color(1f, 1f, 1f, 1f).next();
        buffer.vertex(matrix, x, y, 0).texture(0f, 0f).color(1f, 1f, 1f, 1f).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    public static void drawWindowBackground(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            Color color
    ) {
        float radius = isRoundedEnabled() ? getRadius() : 0f;
        Color panel = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.min(160, color.getAlpha())
        );
        drawRoundedBlurredPanel(matrices, x, y, width, height, radius, panel, true);
    }

    public static void drawBlurredFullscreen(MatrixStack matrices, float radius) {
        if (!isBlurEnabled() || !GuiShaders.available()) {
            return;
        }

        try {
            MinecraftClient mc = MinecraftClient.getInstance();
            Framebuffer capturedBlur = isFrozenValid() ? capture : captureBlurred(Math.max(2f, radius));

            if (capturedBlur == null) {
                return;
            }

            float w = mc.getWindow().getScaledWidth();
            float h = mc.getWindow().getScaledHeight();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, capturedBlur.getColorAttachment());
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
        float finalRadius = isRoundedEnabled() ? radius : 0f;
        drawRoundedBlurredPanel(matrices, x, y, width, height, finalRadius, color, true);
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
        Color color = hovered ? hover : base;
        drawRoundedBlurredPanel(matrices, x, y, width, height, radius, color, true);
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
        Framebuffer capturedBlur = isFrozenValid() ? capture : captureBlurred(radius);

        if (capturedBlur == null) {
            return;
        }

        float fbW = (float) capturedBlur.textureWidth;
        float fbH = (float) capturedBlur.textureHeight;
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
        RenderSystem.setShaderTexture(0, capturedBlur.getColorAttachment());
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

    private static boolean runBlurPass(
            Framebuffer src,
            Framebuffer dst,
            float radius,
            float dirX,
            float dirY
    ) {
        ShaderProgram shader = GuiShaders.BLUR;
        if (shader == null) {
            return false;
        }

        dst.beginWrite(true);
        if (!checkFramebufferComplete("runBlurPass destino")) {
            MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
            return false;
        }

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

        boolean ok = drainGlErrors("runBlurPass draw");

        dst.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
        return ok;
    }
}