package cc.gatopera.dev.mod.gui.font;

import org.lwjgl.BufferUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FontRenderers {
    public static FontAdapter ui;
    public static FontAdapter Calibri;
    public static boolean customFontsAvailable = false;

    public static void createDefault(float size) {
        try {
            ByteBuffer buffer = loadResource("/assets/minecraft/font/font.ttf");
            if (buffer == null) buffer = loadResource("/assets/gatopera/font/font.ttf");
            if (buffer == null) {
                System.err.println("[Gatopera] font.ttf not found");
                customFontsAvailable = false;
                ui = null;
                Calibri = null;
                return;
            }
            StbFont stb = new StbFont(buffer, Math.max(18, (int) (size * 2)));
            ui = new StbFontAdapter(stb, size);
            Calibri = ui;
            customFontsAvailable = true;
            System.out.println("[Gatopera] STB custom fonts loaded");
        } catch (Throwable t) {
            System.err.println("[Gatopera] STB fonts failed: " + t.getMessage());
            t.printStackTrace();
            customFontsAvailable = false;
            ui = null;
            Calibri = null;
        }
    }

    public static FontAdapter create(String name, int style, float size) {
        return ui;
    }

    private static ByteBuffer loadResource(String path) {
        try (InputStream in = FontRenderers.class.getResourceAsStream(path)) {
            if (in == null) return null;
            ReadableByteChannel channel = Channels.newChannel(in);
            ByteBuffer buffer = BufferUtils.createByteBuffer(512 * 1024);
            while (true) {
                int n = channel.read(buffer);
                if (n == -1) break;
                if (buffer.remaining() == 0) {
                    ByteBuffer bigger = BufferUtils.createByteBuffer(buffer.capacity() * 2);
                    buffer.flip();
                    bigger.put(buffer);
                    buffer = bigger;
                }
            }
            buffer.flip();
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }
}