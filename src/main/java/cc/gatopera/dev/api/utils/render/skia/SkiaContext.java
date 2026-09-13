package cc.gatopera.dev.api.utils.render.skia;

import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.FramebufferFormat;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceOrigin;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.function.Consumer;

public final class SkiaContext {
    private static final int GL_BLEND_EQUATION = 0x8009;
    private static DirectContext context;
    private static BackendRenderTarget renderTarget;
    private static Surface surface;
    private static int boundFbo = -1;
    private static int boundWidth = -1;
    private static int boundHeight = -1;

    private SkiaContext() {
    }

    public static void createSurface(int width, int height) {
        int fbo = MinecraftClient.getInstance().getFramebuffer().fbo;
        if (surface != null && !surface.isClosed() && boundFbo == fbo && boundWidth == width && boundHeight == height) {
            return;
        }
        if (context == null) {
            context = DirectContext.makeGL();
        }
        if (surface != null) {
            surface.close();
            surface = null;
        }
        if (renderTarget != null) {
            renderTarget.close();
            renderTarget = null;
        }
        renderTarget = BackendRenderTarget.makeGL(width, height, 0, 8, fbo, FramebufferFormat.GR_GL_RGBA8);
        surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT, ColorType.RGBA_8888, ColorSpace.getSRGB());
        if (surface == null) {
            throw new IllegalStateException("[Gatopera] Failed to create Skia surface");
        }
        boundFbo = fbo;
        boundWidth = width;
        boundHeight = height;
    }

    public static boolean isReady() {
        return surface != null && !surface.isClosed();
    }

    public static void draw(Consumer<Canvas> block) {
        if (surface == null || surface.isClosed()) return;

        int[] lastViewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, lastViewport);
        boolean lastScissor = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
        int[] lastScissorBox = new int[4];
        GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, lastScissorBox);
        boolean lastBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean lastDepth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean lastCull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        int lastBlendSrcRgb = GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB);
        int lastBlendDstRgb = GL11.glGetInteger(GL14.GL_BLEND_DST_RGB);
        int lastBlendSrcAlpha = GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA);
        int lastBlendDstAlpha = GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA);
        int lastBlendEquation = GL11.glGetInteger(GL_BLEND_EQUATION);
        boolean lastDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        int lastActiveTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        int lastTexture2D = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int lastFbo = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
        int lastProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int lastVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        int lastArrayBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);

        try {
            context.resetAll();

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL14.glBlendEquation(GL14.GL_FUNC_ADD);
            GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            Canvas canvas = surface.getCanvas();
            canvas.save();
            canvas.resetMatrix();
            MinecraftClient client = MinecraftClient.getInstance();
            int scaledWidth = client.getWindow().getScaledWidth();
            int scaledHeight = client.getWindow().getScaledHeight();
            if (scaledWidth > 0 && scaledHeight > 0) {
                canvas.scale(
                        (float) boundWidth / scaledWidth,
                        (float) boundHeight / scaledHeight
                );
            }

            block.accept(canvas);

            canvas.restore();
            context.flush();
            context.submit();
        } finally {
            GL11.glViewport(lastViewport[0], lastViewport[1], lastViewport[2], lastViewport[3]);
            if (lastScissor) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glScissor(lastScissorBox[0], lastScissorBox[1], lastScissorBox[2], lastScissorBox[3]);
            } else {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
            if (lastBlend) {
                GL11.glEnable(GL11.GL_BLEND);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL14.glBlendEquation(lastBlendEquation);
            GL14.glBlendFuncSeparate(lastBlendSrcRgb, lastBlendDstRgb, lastBlendSrcAlpha, lastBlendDstAlpha);
            if (lastDepth) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            } else {
                GL11.glDisable(GL11.GL_DEPTH_TEST);
            }
            GL11.glDepthMask(lastDepthMask);
            if (lastCull) {
                GL11.glEnable(GL11.GL_CULL_FACE);
            } else {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
            GL30.glBindVertexArray(lastVao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, lastArrayBuffer);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, lastFbo);
            GL13.glActiveTexture(lastActiveTexture);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, lastTexture2D);
            GL20.glUseProgram(lastProgram);
        }
    }
}