package cc.gatopera.dev.api.utils.render.skia;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Shader;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

import java.awt.Color;

public final class SkiaRender2DUtil {

    private SkiaRender2DUtil() {
    }

    public static void drawRect(Canvas canvas, float x, float y, float width, float height, int argb) {
        drawRect(canvas, x, y, width, height, new Color(argb, true));
    }

    public static void drawRect(Canvas canvas, float x, float y, float width, float height, Color c) {
        if (c.getAlpha() <= 5) return;
        try (Paint paint = SkiaColorUtil.fillPaint(c)) {
            canvas.drawRect(Rect.makeXYWH(x, y, Math.max(width, 0f), Math.max(height, 0f)), paint);
        }
    }

    public static void drawRectHorizontal(Canvas canvas, float x, float y, float width, float height, Color startColor, Color endColor) {
        horizontalGradient(canvas, x, y, x + width, y + height, startColor, endColor);
    }

    public static void drawRectVertical(Canvas canvas, float x, float y, float width, float height, Color startColor, Color endColor) {
        verticalGradient(canvas, x, y, x + width, y + height, startColor, endColor);
    }

    public static void horizontalGradient(Canvas canvas, float x1, float y1, float x2, float y2, Color startColor, Color endColor) {
        if (width(x1, x2) <= 0 || height(y1, y2) <= 0) return;
        try (Shader shader = Shader.makeLinearGradient(x1, y1, x2, y1, new int[]{startColor.getRGB(), endColor.getRGB()});
             Paint paint = new Paint()) {
            paint.setShader(shader);
            paint.setAntiAlias(true);
            canvas.drawRect(Rect.makeLTRB(x1, y1, x2, y2), paint);
        }
    }

    public static void verticalGradient(Canvas canvas, float left, float top, float right, float bottom, Color startColor, Color endColor) {
        if (width(left, right) <= 0 || height(top, bottom) <= 0) return;
        try (Shader shader = Shader.makeLinearGradient(left, top, left, bottom, new int[]{startColor.getRGB(), endColor.getRGB()});
             Paint paint = new Paint()) {
            paint.setShader(shader);
            paint.setAntiAlias(true);
            canvas.drawRect(Rect.makeLTRB(left, top, right, bottom), paint);
        }
    }

    public static void drawRound(Canvas canvas, float x, float y, float width, float height, float radius, Color color) {
        if (color.getAlpha() <= 5) return;
        radius = Math.max(0f, Math.min(radius, Math.min(width, height) / 2f));
        try (Paint paint = SkiaColorUtil.fillPaint(color)) {
            canvas.drawRRect(RRect.makeXYWH(x, y, Math.max(width, 0f), Math.max(height, 0f), radius), paint);
        }
    }

    public static void withClip(Canvas canvas, float x, float y, float width, float height, Runnable draw) {
        canvas.save();
        canvas.clipRect(Rect.makeXYWH(x, y, Math.max(width, 0f), Math.max(height, 0f)));
        try {
            draw.run();
        } finally {
            canvas.restore();
        }
    }

    public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private static float width(float a, float b) {
        return Math.abs(b - a);
    }

    private static float height(float a, float b) {
        return Math.abs(b - a);
    }
}