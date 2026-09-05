package cc.gatopera.dev.mod.gui.clickgui.components.impl;

import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.render.skia.SkiaRender2DUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import io.github.humbleui.skija.Canvas;

import java.awt.Color;

public class BooleanComponent extends Component {

    final BooleanSetting setting;

    public BooleanComponent(ClickGuiTab parent, BooleanSetting setting) {
        super();
        this.parent = parent;
        this.setting = setting;
    }

    @Override
    public boolean isVisible() {
        if (setting.visibility != null) {
            return setting.visibility.getAsBoolean();
        }
        return true;
    }

    boolean hover = false;

    @Override
    public void update(int offset, double mouseX, double mouseY) {
        // Sin cambios: la lógica de input no depende del backend de render.
        int parentX = parent.getX();
        int parentY = parent.getY();
        int parentWidth = parent.getWidth();
        if (GuiManager.currentGrabbed == null && isVisible() && (mouseX >= ((parentX + 1)) && mouseX <= (((parentX)) + parentWidth - 1)) && (mouseY >= (((parentY + offset))) && mouseY <= ((parentY + offset) + defaultHeight - 2))) {
            hover = true;
            if (ClickGuiScreen.clicked) {
                ClickGuiScreen.clicked = false;
                sound();
                setting.toggleValue();
            }
            if (ClickGuiScreen.rightClicked) {
                ClickGuiScreen.rightClicked = false;
                sound();
                setting.popped = !setting.popped;
            }
        } else {
            hover = false;
        }
    }

    public double currentWidth = 0;

    @Override
    public boolean draw(int offset, Canvas canvas, float partialTicks, Color color, boolean back) {
        int x = parent.getX();
        int y = parent.getY() + offset - 2;
        int width = parent.getWidth();

        SkiaRender2DUtil.drawRect(canvas, x + 1, y + 1, width - 2, defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1),
                hover ? ClickGui.INSTANCE.settingHover.getValue() : ClickGui.INSTANCE.setting.getValue());

        currentWidth = animation.get(setting.getValue() ? (width - 2D) : 0D);
        switch (ClickGui.INSTANCE.uiType.getValue()) {
            case New -> SkiaTextUtil.drawString(canvas, setting.getName(), x + 4, y + getTextOffsetY(),
                    setting.getValue() ? ClickGui.INSTANCE.enableTextS.getValue() : ClickGui.INSTANCE.disableText.getValue());
            case Old -> {
                if (ClickGui.INSTANCE.mainEnd.booleanValue) {
                    SkiaRender2DUtil.drawRectHorizontal(canvas, x + 1, y + 1, (float) currentWidth, defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1),
                            hover ? ClickGui.INSTANCE.mainHover.getValue() : color, ClickGui.INSTANCE.mainEnd.getValue());
                } else {
                    SkiaRender2DUtil.drawRect(canvas, x + 1, y + 1, (float) currentWidth, defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1),
                            hover ? ClickGui.INSTANCE.mainHover.getValue() : color);
                }
                SkiaTextUtil.drawString(canvas, setting.getName(), x + 4, y + getTextOffsetY(), new Color(-1).getRGB());
            }
        }

        if (setting.parent) {
            SkiaTextUtil.drawString(canvas, setting.popped ? "-" : "+", x + width - 11,
                    (float) (y + getTextOffsetY()), new Color(255, 255, 255).getRGB());
        }
        return true;
    }
}