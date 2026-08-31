package cc.gatopera.dev.mod.gui.font;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static cc.gatopera.dev.api.utils.Wrapper.mc;

class GlyphMap {
    private static final int PADDING = 5;
    private static boolean awtFailed = false;
    final char fromIncl;
    final char toExcl;
    final Font[] font;
    final Identifier bindToTexture;
    private final Char2ObjectArrayMap<Glyph> glyphs = new Char2ObjectArrayMap<>();
    int width, height;

    boolean generated = false;

    public GlyphMap(char from, char to, Font[] fonts, Identifier identifier) {
        fromIncl = from;
        toExcl = to;
        font = fonts;
        bindToTexture = identifier;
    }

    public Glyph getGlyph(char c) {
        if (!generated) {
            generate();
        }
        return glyphs.get(c);
    }

    public void destroy() {
        MinecraftClient.getInstance().getTextureManager().destroyTexture(this.bindToTexture);
        this.glyphs.clear();
        this.width = -1;
        this.height = -1;
        generated = false;
    }

    public boolean contains(char c) {
        return c >= fromIncl && c < toExcl;
    }

    private Font getFontForGlyph(char c) {
        for (Font font1 : this.font) {
            if (font1.canDisplay(c)) {
                return font1;
            }
        }
        return this.font[0];
    }

    public void generate() {
        if (generated) {
            return;
        }

        if (!awtFailed) {
            try {
                generateWithAWT();
                return;
            } catch (Throwable e) {
                awtFailed = true;
                e.printStackTrace();
            }
        }

        generateFallback();
    }

    private void generateWithAWT() {
        int range = toExcl - fromIncl - 1;
        int charsVert = (int) (Math.ceil(Math.sqrt(range)) * 1.5);
        glyphs.clear();
        int generatedChars = 0;
        int charNX = 0;
        int maxX = 0, maxY = 0;
        int currentX = 0, currentY = 0;
        int currentRowMaxY = 0;
        List<Glyph> glyphs1 = new ArrayList<>();
        AffineTransform af = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(af, true, true);
        while (generatedChars <= range) {
            char currentChar = (char) (fromIncl + generatedChars);
            Font font = getFontForGlyph(currentChar);
            Rectangle2D stringBounds = font.getStringBounds(String.valueOf(currentChar), frc);

            int width = (int) Math.ceil(stringBounds.getWidth());
            int height = (int) Math.ceil(stringBounds.getHeight());
            generatedChars++;
            maxX = Math.max(maxX, currentX + width);
            maxY = Math.max(maxY, currentY + height);
            if (charNX >= charsVert) {
                currentX = 0;
                currentY += currentRowMaxY + PADDING;
                charNX = 0;
                currentRowMaxY = 0;
            }
            currentRowMaxY = Math.max(currentRowMaxY, height);
            glyphs1.add(new Glyph(currentX, currentY, width, height, currentChar, this));
            currentX += width + PADDING;
            charNX++;
        }
        BufferedImage bi = new BufferedImage(Math.max(maxX + PADDING, 1), Math.max(maxY + PADDING, 1),
                BufferedImage.TYPE_INT_ARGB);
        width = bi.getWidth();
        height = bi.getHeight();
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.WHITE);

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (Glyph glyph : glyphs1) {
            g2d.setFont(getFontForGlyph(glyph.value()));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            g2d.drawString(String.valueOf(glyph.value()), glyph.u(), glyph.v() + fontMetrics.getAscent());
            glyphs.put(glyph.value(), glyph);
        }
        registerBufferedImageTexture(bindToTexture, bi);
        generated = true;
    }

    private void generateFallback() {
        int range = toExcl - fromIncl - 1;
        int defaultWidth = 8;
        int defaultHeight = 12;
        int charsPerRow = (int) Math.ceil(Math.sqrt(range));
        int rows = (int) Math.ceil((double) range / charsPerRow);

        width = charsPerRow * (defaultWidth + PADDING) + PADDING;
        height = rows * (defaultHeight + PADDING) + PADDING;

        glyphs.clear();
        int generatedChars = 0;

        for (int row = 0; row < rows && generatedChars <= range; row++) {
            for (int col = 0; col < charsPerRow && generatedChars <= range; col++) {
                char currentChar = (char) (fromIncl + generatedChars);
                int x = col * (defaultWidth + PADDING) + PADDING;
                int y = row * (defaultHeight + PADDING) + PADDING;

                glyphs.put(currentChar, new Glyph(x, y, defaultWidth, defaultHeight, currentChar, this));
                generatedChars++;
            }
        }

        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bi.createGraphics();
            g2d.setColor(new Color(0, 0, 0, 0));
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();

            registerBufferedImageTexture(bindToTexture, bi);
        } catch (Throwable e) {
            e.printStackTrace();
            width = 64;
            height = 64;
            try {
                BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
                registerBufferedImageTexture(bindToTexture, bi);
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
        generated = true;
    }

    public static void registerBufferedImageTexture(Identifier i, BufferedImage bi) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", out);
            byte[] bytes = out.toByteArray();

            ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            data.flip();
            NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.read(data));
            mc.execute(() -> mc.getTextureManager().registerTexture(i, tex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}