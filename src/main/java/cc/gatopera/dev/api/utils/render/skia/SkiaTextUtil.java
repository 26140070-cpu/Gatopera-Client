package cc.gatopera.dev.api.utils.render.skia;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import io.github.humbleui.skija.Canvas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class SkiaTextUtil {
    public static final float UI_SIZE = 13f;

    private static final List<TextCommand> QUEUED_TEXT = new ArrayList<>();
    private static ClipRect currentClip;

    private SkiaTextUtil() {
    }

    public static synchronized void init(float size) {
    }

    public static void setSize(float size) {
    }

    public static boolean isReady() {
        return true;
    }

    public static float getWidth(String text) {
        if (text == null || text.isEmpty()) return 0f;
        return MinecraftClient.getInstance().textRenderer.getWidth(stripCodes(text));
    }

    public static float getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public static void drawString(Canvas canvas, String text, double x, double y, Color color) {
        drawString(canvas, text, (float) x, (float) y, color.getRGB());
    }

    public static void drawString(Canvas canvas, String text, double x, double y, int color) {
        drawString(canvas, text, (float) x, (float) y, color);
    }

    public static synchronized void drawString(Canvas canvas, String text, float x, float y, int color) {
        if (text == null || text.isEmpty()) return;
        queueSegments(text, x, y, color, 1f, currentClip);
    }

    public static void drawStringWithScale(Canvas canvas, String text, float x, float y, Color color, float scale) {
        drawStringWithScale(canvas, text, x, y, color.getRGB(), scale);
    }

    public static synchronized void drawStringWithScale(Canvas canvas, String text, float x, float y, int color, float scale) {
        if (text == null || text.isEmpty()) return;
        queueSegments(text, x, y, color, scale, currentClip);
    }

    public static synchronized void pushClip(float x, float y, float width, float height) {
        currentClip = new ClipRect(x, y, width, height);
    }

    public static synchronized void popClip() {
        currentClip = null;
    }

    public static synchronized void flush(DrawContext drawContext) {
        if (QUEUED_TEXT.isEmpty()) return;
        MatrixStack matrices = drawContext.getMatrices();
        for (TextCommand command : QUEUED_TEXT) {
            matrices.push();
            ClipRect clip = command.clip();
            if (clip != null && (command.y() + getHeight() < clip.y()
                    || command.y() > clip.y() + clip.height())) {
                matrices.pop();
                continue;
            }
            if (clip != null) {
                drawContext.enableScissor(
                        Math.round(clip.x()),
                        Math.round(clip.y()),
                        Math.round(clip.x() + clip.width()),
                        Math.round(clip.y() + clip.height())
                );
            }
            if (command.scale() != 1f) {
                matrices.translate(command.x(), command.y(), 0);
                matrices.scale(command.scale(), command.scale(), 1f);
                drawSegments(drawContext, command.text(), 0f, 0f, command.color());
            } else {
                drawSegments(drawContext, command.text(), command.x(), command.y(), command.color());
            }
            if (clip != null) {
                drawContext.disableScissor();
            }
            matrices.pop();
        }
        QUEUED_TEXT.clear();
    }

    public static synchronized void clearQueuedText() {
        QUEUED_TEXT.clear();
    }

    public static String trimStringToWidth(String text, double width) {
        if (text == null) return null;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String candidate = result + String.valueOf(text.charAt(i));
            if (getWidth(candidate) > width) break;
            result.append(text.charAt(i));
        }
        return result.toString();
    }

    private static void queueSegments(String text, float x, float y, int color, float scale, ClipRect clip) {
        QUEUED_TEXT.add(new TextCommand(text, x, y, color, scale, clip));
    }

    private static void drawSegments(DrawContext drawContext, String text, float x, float y, int defaultColor) {
        int color = defaultColor | 0xFF000000;
        StringBuilder segment = new StringBuilder();
        float currentX = x;
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '§' && i + 1 < text.length()) {
                if (segment.length() > 0) {
                    drawText(drawContext, segment.toString(), currentX, y, color);
                    currentX += getWidth(segment.toString());
                    segment.setLength(0);
                }
                int parsed = colorFromCode(text.charAt(++i), color);
                if (parsed != Integer.MIN_VALUE) color = parsed;
            } else {
                segment.append(current);
            }
        }
        if (segment.length() > 0) {
            drawText(drawContext, segment.toString(), currentX, y, color);
        }
    }

    private static void drawText(DrawContext drawContext, String text, float x, float y, int color) {
        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(text),
                Math.round(x),
                Math.round(y),
                color,
                true
        );
    }

    private static String stripCodes(String text) {
        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                i++;
            } else {
                result.append(text.charAt(i));
            }
        }
        return result.toString();
    }

    private static int colorFromCode(char code, int defaultColor) {
        char normalized = Character.toLowerCase(code);
        int[] colors = {
                0x000000, 0x0000AA, 0x00AA00, 0x00AAAA,
                0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA,
                0x555555, 0x5555FF, 0x55FF55, 0x55FFFF,
                0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF
        };
        int index;
        if (normalized >= '0' && normalized <= '9') index = normalized - '0';
        else if (normalized >= 'a' && normalized <= 'f') index = 10 + normalized - 'a';
        else if (normalized == 'r') return defaultColor;
        else return Integer.MIN_VALUE;
        return (defaultColor & 0xFF000000) | colors[index];
    }

    private record TextCommand(String text, float x, float y, int color, float scale, ClipRect clip) {
    }

    private record ClipRect(float x, float y, float width, float height) {
    }
}
