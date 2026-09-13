package cc.gatopera.dev.api.utils.render.skia;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PaintMode;
import io.github.humbleui.types.RRect;

import java.awt.Color;

public final class SkiaGlassUtil {
    private SkiaGlassUtil() {
    }

    public static void drawGlassPanel(Canvas canvas, float x, float y, float width, float height, float radius, Color tint, Color border, float blurSigma) {
        if (width <= 0f || height <= 0f) return;
        radius = Math.max(0f, Math.min(radius, Math.min(width, height) / 2f));
        RRect rrect = RRect.makeXYWH(x, y, width, height, radius);

        try (Paint fill = new Paint()) {
            fill.setColor(tint.getRGB());
            fill.setAntiAlias(true);
            canvas.drawRRect(rrect, fill);
        }

        if (border != null && border.getAlpha() > 0) {
            try (Paint stroke = new Paint()) {
                stroke.setColor(border.getRGB());
                stroke.setAntiAlias(true);
                stroke.setMode(PaintMode.STROKE);
                stroke.setStrokeWidth(1f);
                canvas.drawRRect(rrect, stroke);
            }
        }
    }

    public static void drawGlassPanel(Canvas canvas, float x, float y, float width, float height, float radius, Color tint) {
        drawGlassPanel(canvas, x, y, width, height, radius, tint, new Color(255, 255, 255, 35), 16f);
    }
}