package cc.gatopera.dev.api.utils.render.skia;

import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;

import java.awt.Color;

public final class SkiaColorUtil {

    private SkiaColorUtil() {
    }

    public static int toArgb(Color color) {
        return color.getRGB();
    }

    public static Paint fillPaint(Color color) {
        return fillPaint(color.getRGB());
    }

    public static Paint fillPaint(int argb) {
        Paint paint = new Paint();
        paint.setColor(argb);
        paint.setMode(PaintMode.FILL);
        paint.setAntiAlias(true);
        return paint;
    }

    public static Paint strokePaint(Color color, float strokeWidth) {
        Paint paint = new Paint();
        paint.setColor(color.getRGB());
        paint.setMode(PaintMode.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        return paint;
    }

    public static int withAlpha(int argb, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (alpha << 24) | (argb & 0x00FFFFFF);
    }

    public static int withAlpha(Color color, int alpha) {
        return withAlpha(color.getRGB(), alpha);
    }
}
