package cc.gatopera.dev.api.utils.render;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceFactory;

public final class GuiShaders {
    public static ShaderProgram BLUR;
    public static ShaderProgram ROUNDED;
    private static boolean failed;

    private GuiShaders() {
    }

    public static void load(ResourceFactory factory) {
        try {
            BLUR = new ShaderProgram(factory, "gatopera_blur", VertexFormats.POSITION_TEXTURE);
            ROUNDED = new ShaderProgram(factory, "gatopera_rounded", VertexFormats.POSITION_TEXTURE);
            failed = false;
            System.out.println("[Gatopera] GUI shaders loaded");
        } catch (Throwable t) {
            failed = true;
            BLUR = null;
            ROUNDED = null;
            System.err.println("[Gatopera] GUI shaders failed: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public static boolean available() {
        return !failed && BLUR != null && ROUNDED != null;
    }
}