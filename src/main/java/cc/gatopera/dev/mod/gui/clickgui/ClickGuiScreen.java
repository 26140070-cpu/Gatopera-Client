package cc.gatopera.dev.mod.gui.clickgui;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.api.utils.render.skia.SkiaContext;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.tabs.Tab;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import cc.gatopera.dev.mod.modules.settings.impl.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen implements Wrapper {

    public ClickGuiScreen() {
        super(Text.of("ClickGui"));
        if (!SkiaTextUtil.isReady()) {
            SkiaTextUtil.init(9f);
        }
    }
    public static boolean clicked = false;
    public static boolean rightClicked = false;
    public static boolean hoverClicked = false;

    private int surfaceWidth = -1;
    private int surfaceHeight = -1;

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        ensureSurface();
    }

    private void ensureSurface() {
        int w = mc.getFramebuffer().textureWidth;
        int h = mc.getFramebuffer().textureHeight;
        if (w != surfaceWidth || h != surfaceHeight) {
            SkiaContext.createSurface(w, h);
            surfaceWidth = w;
            surfaceHeight = h;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        super.render(drawContext, mouseX, mouseY, partialTicks);
        ensureSurface();

        Gatopera.GUI.onUpdate();
        Gatopera.GUI.armorHud.draw(drawContext, partialTicks, Gatopera.GUI.getColor());

        SkiaContext.draw(canvas -> Gatopera.GUI.drawSkia(canvas, mouseX, mouseY, partialTicks));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Gatopera.MODULE.modules.forEach(module -> module.getSettings().stream()
                .filter(setting -> setting instanceof StringSetting)
                .map(setting -> (StringSetting) setting)
                .filter(StringSetting::isListening)
                .forEach(setting -> setting.keyType(keyCode)));
        Gatopera.MODULE.modules.forEach(module -> module.getSettings().stream()
                .filter(setting -> setting instanceof SliderSetting)
                .map(setting -> (SliderSetting) setting)
                .filter(SliderSetting::isListening)
                .forEach(setting -> setting.keyType(keyCode)));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            hoverClicked = false;
            clicked = true;
            Gatopera.GUI.selectCategory(mouseX, mouseY);
        } else if (button == 1) {
            rightClicked = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            clicked = false;
            hoverClicked = false;
        } else if (button == 1) {
            rightClicked = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        super.close();
        rightClicked = false;
        hoverClicked = false;
        clicked = false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (Tab tab : Gatopera.GUI.tabs) {
            tab.setY((int) (tab.getY() + (verticalAmount * 30)));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}