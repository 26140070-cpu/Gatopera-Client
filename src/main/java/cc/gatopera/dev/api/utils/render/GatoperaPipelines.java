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
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.awt.Color;

public final class GatoperaPipelines implements Wrapper {
    private static SimpleFramebuffer capture;
    private static SimpleFramebuffer blurTemp;
    private static boolean frameCaptured;

    private GatoperaPipelines() {
    }

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

        if (main.textureWidth <= 0 || main.textureHeight <= 0) {
            return;
        }

        if (capture == null
                || blurTemp == null
                || capture.textureWidth != main.textureWidth
                || capture.textureHeight != main.textureHeight
                || blurTemp.textureWidth != main.textureWidth
                || blurTemp.textureHeight != main.textureHeight) {

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
            frameCaptured = false;
        }
    }

    private static boolean checkFramebufferComplete(String label) {
        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);

        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println(
                    "[Gatopera] framebuffer incomplete at "
                            + label
                            + ": 0x"
                            + Integer.toHexString(status)
            );
            return false;
        }

        return true;
    }

    private static boolean drainGlErrors(String label) {
        boolean hadError = false;
        int error;

        while ((error = GL30.glGetError()) != GL30.GL_NO_ERROR) {
            hadError = true;

            System.err.println(
                    "[Gatopera] OpenGL error at "
                            + label
                            + ": 0x"
                            + Integer.toHexString(error)
            );
        }

        return !hadError;
    }

    public static void beginFrameBlur(float radius) {
        frameCaptured = false;

        if (!isBlurEnabled() || !GuiShaders.available()) {
            return;
        }

        Framebuffer result = captureBlurred(radius);
        frameCaptured = result != null;
    }

    public static void endFrameBlur() {
        frameCaptured = false;
    }

    private static Framebuffer captureBlurred(float radius) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();

        ensureBuffers();

        if (capture == null || blurTemp == null) {
            main.beginWrite(false);
            return null;
        }

        if (!runBlurPass(main, blurTemp, radius, 1f, 0f)) {
            main.beginWrite(false);
            return null;
        }

        if (!runBlurPass(blurTemp, capture, radius, 0f, 1f)) {
            main.beginWrite(false);
            return null;
        }

        main.beginWrite(false);
        return capture;
    }

    private static Framebuffer captureSharp() {
        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();

        ensureBuffers();

        if (capture == null) {
            main.beginWrite(false);
            return null;
        }

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.fbo);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, capture.fbo);

        if (!checkFramebufferComplete("captureSharp destination")) {
            main.beginWrite(false);
            return null;
        }

        GL30.glBlitFramebuffer(
                0,
                0,
                main.textureWidth,
                main.textureHeight,
                0,
                0,
                capture.textureWidth,
                capture.textureHeight,
                GL30.GL_COLOR_BUFFER_BIT,
                GL30.GL_NEAREST
        );

        boolean ok = drainGlErrors("captureSharp blit");

        main.beginWrite(false);

        return ok ? capture : null;
    }

    private static void drawPlainPanel(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius,
            Color color
    ) {
        if (radius > 0.5f) {
            Render2DUtil.drawRound(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    radius,
                    color
            );
        } else {
            Render2DUtil.drawRect(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    color
            );
        }
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
        MinecraftClient mc = MinecraftClient.getInstance();

        boolean shadersReady = GuiShaders.available();
        boolean rounded = isRoundedEnabled()
                && radius > 0.5f
                && shadersReady;

        boolean blur = allowBlur
                && isBlurEnabled()
                && shadersReady;

        if (!rounded) {
            if (blur) {
                drawBlurredRegion(
                        matrices,
                        x,
                        y,
                        width,
                        height,
                        Math.max(2f, radius * 0.4f)
                );
            }

            drawPlainPanel(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    radius,
                    color
            );

            return;
        }

        Framebuffer source;

        if (blur) {
            if (!frameCaptured || capture == null) {
                source = captureBlurred(
                        Math.max(4f, radius * 0.5f)
                );
            } else {
                source = capture;
            }
        } else {
            source = captureSharp();
        }

        if (source == null) {
            drawPlainPanel(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    radius,
                    color
            );
            return;
        }

        float scale = (float) mc.getWindow().getScaleFactor();
        float fbW = source.textureWidth;
        float fbH = source.textureHeight;

        ShaderProgram shader = GuiShaders.ROUNDED;

        if (shader == null) {
            drawPlainPanel(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    radius,
                    color
            );
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderTexture(0, source.getColorAttachment());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (shader.getUniform("ScreenSize") != null) {
            shader.getUniform("ScreenSize").set(fbW, fbH);
        }

        if (shader.getUniform("RectPos") != null) {
            shader.getUniform("RectPos").set(
                    x * scale,
                    y * scale
            );
        }

        if (shader.getUniform("RectSize") != null) {
            shader.getUniform("RectSize").set(
                    width * scale,
                    height * scale
            );
        }

        if (shader.getUniform("Radius") != null) {
            shader.getUniform("Radius").set(
                    radius * scale
            );
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

        buffer.begin(
                VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_TEXTURE
        );

        buffer.vertex(
                        matrix,
                        x,
                        y + height,
                        0f
                )
                .texture(0f, 1f)
                .next();

        buffer.vertex(
                        matrix,
                        x + width,
                        y + height,
                        0f
                )
                .texture(1f, 1f)
                .next();

        buffer.vertex(
                        matrix,
                        x + width,
                        y,
                        0f
                )
                .texture(1f, 0f)
                .next();

        buffer.vertex(
                        matrix,
                        x,
                        y,
                        0f
                )
                .texture(0f, 0f)
                .next();

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
        drawWindowBackground(
                matrices,
                x,
                y,
                width,
                height,
                color,
                true
        );
    }

    public static void drawWindowBackground(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            Color color,
            boolean allowBlur
    ) {
        float radius = isRoundedEnabled()
                ? getRadius()
                : 0f;

        Color panel = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.min(160, color.getAlpha())
        );

        drawRoundedBlurredPanel(
                matrices,
                x,
                y,
                width,
                height,
                radius,
                panel,
                allowBlur
        );
    }

    public static void drawBlurredFullscreen(
            MatrixStack matrices,
            float radius
    ) {
        if (!isBlurEnabled() || !GuiShaders.available()) {
            return;
        }

        Framebuffer capturedBlur;

        if (frameCaptured && capture != null) {
            capturedBlur = capture;
        } else {
            capturedBlur = captureBlurred(
                    Math.max(2f, radius)
            );
        }

        if (capturedBlur == null) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();

        float w = mc.getWindow().getScaledWidth();
        float h = mc.getWindow().getScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(
                0,
                capturedBlur.getColorAttachment()
        );
        RenderSystem.setShaderColor(
                1f,
                1f,
                1f,
                1f
        );

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(
                VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_TEXTURE
        );

        buffer.vertex(
                        matrix,
                        0f,
                        h,
                        0f
                )
                .texture(0f, 0f)
                .next();

        buffer.vertex(
                        matrix,
                        w,
                        h,
                        0f
                )
                .texture(1f, 0f)
                .next();

        buffer.vertex(
                        matrix,
                        w,
                        0f,
                        0f
                )
                .texture(1f, 1f)
                .next();

        buffer.vertex(
                        matrix,
                        0f,
                        0f,
                        0f
                )
                .texture(0f, 1f)
                .next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.setShaderColor(
                1f,
                1f,
                1f,
                1f
        );

        RenderSystem.disableBlend();
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
        float finalRadius = isRoundedEnabled()
                ? radius
                : 0f;

        drawRoundedBlurredPanel(
                matrices,
                x,
                y,
                width,
                height,
                finalRadius,
                color,
                true
        );
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
                ? Math.min(
                getRadius(),
                Math.min(width, height) / 2f
        )
                : 0f;

        Color color = hovered
                ? hover
                : base;

        drawRoundedBlurredPanel(
                matrices,
                x,
                y,
                width,
                height,
                radius,
                color,
                true
        );
    }

    private static void drawBlurredRegion(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius
    ) {
        Framebuffer capturedBlur;

        if (frameCaptured && capture != null) {
            capturedBlur = capture;
        } else {
            capturedBlur = captureBlurred(radius);
        }

        if (capturedBlur == null) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();

        float fbW = capturedBlur.textureWidth;
        float fbH = capturedBlur.textureHeight;

        float scale = (float) mc.getWindow().getScaleFactor();

        float px0 = x * scale;
        float py0 = y * scale;
        float px1 = (x + width) * scale;
        float py1 = (y + height) * scale;

        float u0 = px0 / fbW;
        float u1 = px1 / fbW;

        float v0 = 1f - (py1 / fbH);
        float v1 = 1f - (py0 / fbH);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(
                0,
                capturedBlur.getColorAttachment()
        );
        RenderSystem.setShaderColor(
                1f,
                1f,
                1f,
                1f
        );

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(
                VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_TEXTURE
        );

        buffer.vertex(
                        matrix,
                        x,
                        y + height,
                        0f
                )
                .texture(u0, v0)
                .next();

        buffer.vertex(
                        matrix,
                        x + width,
                        y + height,
                        0f
                )
                .texture(u1, v0)
                .next();

        buffer.vertex(
                        matrix,
                        x + width,
                        y,
                        0f
                )
                .texture(u1, v1)
                .next();

        buffer.vertex(
                        matrix,
                        x,
                        y,
                        0f
                )
                .texture(u0, v1)
                .next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.setShaderColor(
                1f,
                1f,
                1f,
                1f
        );

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

        MinecraftClient mc = MinecraftClient.getInstance();
        Framebuffer main = mc.getFramebuffer();

        dst.beginWrite(true);

        if (!checkFramebufferComplete("runBlurPass destination")) {
            main.beginWrite(false);
            return false;
        }

        RenderSystem.disableBlend();
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderTexture(
                0,
                src.getColorAttachment()
        );
        RenderSystem.setShaderColor(
                1f,
                1f,
                1f,
                1f
        );

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
            shader.getUniform("Direction").set(
                    dirX,
                    dirY
            );
        }

        Matrix4f identity = new Matrix4f().identity();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        buffer.begin(
                VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_TEXTURE
        );

        buffer.vertex(
                        identity,
                        -1f,
                        -1f,
                        0f
                )
                .texture(0f, 0f)
                .next();

        buffer.vertex(
                        identity,
                        1f,
                        -1f,
                        0f
                )
                .texture(1f, 0f)
                .next();

        buffer.vertex(
                        identity,
                        1f,
                        1f,
                        0f
                )
                .texture(1f, 1f)
                .next();

        buffer.vertex(
                        identity,
                        -1f,
                        1f,
                        0f
                )
                .texture(0f, 1f)
                .next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        boolean ok = drainGlErrors(
                "runBlurPass draw"
        );

        dst.endWrite();
        main.beginWrite(false);

        RenderSystem.setShaderColor(
                1f,
                1f,
                1f,
                1f
        );

        return ok;
    }
}