package cc.gatopera.dev.mod.gui.font;

import net.minecraft.client.util.math.MatrixStack;

public class StbFontAdapter implements FontAdapter {
    private final StbFont font;
    private final float drawScale;

    public StbFontAdapter(StbFont font, float targetSize) {
        this.font = font;
        this.drawScale = targetSize / (float) font.getHeight();
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, int color) {
        if ((color & 0xFC000000) == 0) color |= 0xFF000000;
        float a = ((color >> 24) & 255) / 255f;
        float r = ((color >> 16) & 255) / 255f;
        float g = ((color >> 8) & 255) / 255f;
        float b = (color & 255) / 255f;
        font.draw(matrices, text, x + 1, y + 1, 0, 0, 0, a * 0.5f, drawScale);
        font.draw(matrices, text, x, y, r, g, b, a, drawScale);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, double x, double y, int color) {
        drawString(matrices, text, (float) x, (float) y, color);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, float r, float g, float b, float a) {
        font.draw(matrices, text, x + 1, y + 1, 0, 0, 0, a * 0.5f, drawScale);
        font.draw(matrices, text, x, y, r, g, b, a, drawScale);
    }

    @Override
    public void drawGradientString(MatrixStack matrices, String s, float x, float y, int offset, boolean hud) {
        drawString(matrices, s, x, y, 0xFFFFFFFF);
    }

    @Override
    public void drawCenteredString(MatrixStack matrices, String text, double x, double y, int color) {
        drawString(matrices, text, (float) (x - getWidth(text) / 2f), (float) y, color);
    }

    @Override
    public void drawCenteredString(MatrixStack matrices, String text, double x, double y, float r, float g, float b, float a) {
        drawString(matrices, text, (float) (x - getWidth(text) / 2f), (float) y, r, g, b, a);
    }

    @Override
    public float getWidth(String text) {
        return font.getWidth(text) * drawScale;
    }

    @Override
    public float getFontHeight() {
        return font.getHeight() * drawScale;
    }

    @Override
    public float getFontHeight(String text) {
        return getFontHeight();
    }

    @Override
    public float getMarginHeight() {
        return getFontHeight();
    }

    @Override
    public void drawString(MatrixStack matrices, String s, float x, float y, int color, boolean dropShadow) {
        drawString(matrices, s, x, y, color);
    }

    @Override
    public void drawString(MatrixStack matrices, String s, float x, float y, float r, float g, float b, float a, boolean dropShadow) {
        drawString(matrices, s, x, y, r, g, b, a);
    }

    @Override
    public String trimStringToWidth(String in, double width) {
        StringBuilder sb = new StringBuilder();
        for (char c : in.toCharArray()) {
            if (getWidth(sb.toString() + c) >= width) break;
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public String trimStringToWidth(String in, double width, boolean reverse) {
        return trimStringToWidth(in, width);
    }
}