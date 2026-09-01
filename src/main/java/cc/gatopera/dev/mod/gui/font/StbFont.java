package cc.gatopera.dev.mod.gui.font;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.UUID;

public class StbFont {
    private static final int ATLAS_SIZE = 2048;

    private final Identifier textureId;
    private final int pixelHeight;
    private final float scale;
    private final float ascent;
    private final Int2ObjectOpenHashMap<CharData> chars = new Int2ObjectOpenHashMap<>();

    public StbFont(ByteBuffer ttf, int pixelHeight) {
        this.pixelHeight = pixelHeight;

        STBTTFontinfo info = STBTTFontinfo.create();
        if (!STBTruetype.stbtt_InitFont(info, ttf)) {
            throw new IllegalStateException("Failed to init STB font");
        }

        ByteBuffer bitmap = BufferUtils.createByteBuffer(ATLAS_SIZE * ATLAS_SIZE);

        STBTTPackedchar.Buffer basic = STBTTPackedchar.create(95);
        STBTTPackedchar.Buffer latin1 = STBTTPackedchar.create(96);
        STBTTPackedchar.Buffer latinExt = STBTTPackedchar.create(128);

        STBTTPackContext pc = STBTTPackContext.create();
        STBTruetype.stbtt_PackBegin(pc, bitmap, ATLAS_SIZE, ATLAS_SIZE, 0, 1);

        STBTTPackRange.Buffer ranges = STBTTPackRange.create(3);
        ranges.put(STBTTPackRange.create().set(pixelHeight, 32, null, 95, basic, (byte) 1, (byte) 1));
        ranges.put(STBTTPackRange.create().set(pixelHeight, 160, null, 96, latin1, (byte) 1, (byte) 1));
        ranges.put(STBTTPackRange.create().set(pixelHeight, 256, null, 128, latinExt, (byte) 1, (byte) 1));
        ranges.flip();

        STBTruetype.stbtt_PackFontRanges(pc, ttf, 0, ranges);
        STBTruetype.stbtt_PackEnd(pc);

        this.scale = STBTruetype.stbtt_ScaleForPixelHeight(info, pixelHeight);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer ascentBuf = stack.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(info, ascentBuf, null, null);
            this.ascent = ascentBuf.get(0);
        }

        putRange(basic, 32);
        putRange(latin1, 160);
        putRange(latinExt, 256);

        NativeImage image = new NativeImage(NativeImage.Format.RGBA, ATLAS_SIZE, ATLAS_SIZE, false);
        for (int y = 0; y < ATLAS_SIZE; y++) {
            for (int x = 0; x < ATLAS_SIZE; x++) {
                int a = bitmap.get(x + y * ATLAS_SIZE) & 0xFF;
                image.setColor(x, y, (a << 24) | 0x00FFFFFF);
            }
        }

        textureId = new Identifier("gatopera", "fonts/" + UUID.randomUUID());
        NativeImageBackedTexture tex = new NativeImageBackedTexture(image);
        MinecraftClient.getInstance().getTextureManager().registerTexture(textureId, tex);
    }

    private void putRange(STBTTPackedchar.Buffer buf, int firstCodepoint) {
        float ip = 1f / ATLAS_SIZE;
        for (int i = 0; i < buf.capacity(); i++) {
            STBTTPackedchar c = buf.get(i);
            chars.put(firstCodepoint + i, new CharData(
                    c.xoff(), c.yoff(), c.xoff2(), c.yoff2(),
                    c.x0() * ip, c.y0() * ip, c.x1() * ip, c.y1() * ip,
                    c.xadvance()
            ));
        }
    }

    public float getWidth(String text) {
        float w = 0;
        for (int i = 0; i < text.length(); i++) {
            CharData c = chars.getOrDefault((int) text.charAt(i), chars.get(32));
            if (c != null) w += c.xAdvance;
        }
        return w;
    }

    public float getHeight() {
        return pixelHeight;
    }

    public void draw(MatrixStack matrices, String text, float x, float y, float r, float g, float b, float a, float scaleMul) {
        if (text == null || text.isEmpty()) return;

        y += ascent * this.scale * scaleMul;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, textureId);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);

        Matrix4f mat = matrices.peek().getPositionMatrix();
        BufferBuilder bb = Tessellator.getInstance().getBuffer();
        bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        float cx = x;
        for (int i = 0; i < text.length(); i++) {
            CharData c = chars.getOrDefault((int) text.charAt(i), chars.get(32));
            if (c == null) continue;

            float x0 = cx + c.x0 * scaleMul;
            float y0 = y + c.y0 * scaleMul;
            float x1 = cx + c.x1 * scaleMul;
            float y1 = y + c.y1 * scaleMul;

            bb.vertex(mat, x0, y1, 0).texture(c.u0, c.v1).color(r, g, b, a).next();
            bb.vertex(mat, x1, y1, 0).texture(c.u1, c.v1).color(r, g, b, a).next();
            bb.vertex(mat, x1, y0, 0).texture(c.u1, c.v0).color(r, g, b, a).next();
            bb.vertex(mat, x0, y0, 0).texture(c.u0, c.v0).color(r, g, b, a).next();

            cx += c.xAdvance * scaleMul;
        }

        BufferRenderer.drawWithGlobalProgram(bb.end());
        RenderSystem.disableBlend();
    }

    private record CharData(float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1, float xAdvance) {}
}