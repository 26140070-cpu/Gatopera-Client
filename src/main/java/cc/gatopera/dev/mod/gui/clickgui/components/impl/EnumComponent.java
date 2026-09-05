package cc.gatopera.dev.mod.gui.clickgui.components.impl;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.utils.math.Animation;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.render.skia.SkiaRender2DUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import io.github.humbleui.skija.Canvas;

import java.awt.*;

public class EnumComponent extends Component {
	private final EnumSetting<?> setting;
	@Override
	public boolean isVisible() {
		if (setting.visibility != null) {
			return setting.visibility.getAsBoolean();
		}
		return true;
	}
	public EnumComponent(ClickGuiTab parent, EnumSetting<?> enumSetting) {
		super();
		this.parent = parent;
		setting = enumSetting;
	}

	private boolean hover = false;

	public void update(int offset, double mouseX, double mouseY) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		if ((mouseX >= ((parentX + 2)) && mouseX <= (((parentX)) + parentWidth - 2)) && (mouseY >= (((parentY + offset))) && mouseY <= ((parentY + offset) + defaultHeight - 2))) {
			hover = true;
			if (GuiManager.currentGrabbed == null && isVisible()) {
				if (ClickGuiScreen.clicked) {
					ClickGuiScreen.clicked = false;
					setting.increaseEnum();
					sound();
				}
				if (ClickGuiScreen.rightClicked) {
					setting.popped = !setting.popped;
					ClickGuiScreen.rightClicked = false;
					sound();
				}
			}
		} else {
			hover = false;
		}

		if (GuiManager.currentGrabbed == null && isVisible() && ClickGuiScreen.clicked) {
			int cy = parentY + offset - 1 + (defaultHeight - 2) - 2;
			if (setting.popped) {
				for (Object o : setting.getValue().getDeclaringClass().getEnumConstants()) {
					if (mouseX >= parentX && mouseX <= parentX + parentWidth && mouseY >= SkiaTextUtil.getHeight() / 2 + cy && mouseY < SkiaTextUtil.getHeight() + SkiaTextUtil.getHeight() / 2 + cy) {
						setting.setEnumValue(String.valueOf(o));
						ClickGuiScreen.clicked = false;
						sound();
						break;
					}
					cy += (int) SkiaTextUtil.getHeight();
				}
			}
		}
		y = 0;
		if (setting.popped) {
			for (Object ignored : setting.getValue().getDeclaringClass().getEnumConstants()) {
				y += (int) SkiaTextUtil.getHeight();
			}
			setHeight(defaultHeight + y);
		} else {
			setHeight(defaultHeight);
		}
	}

	@Override
	public int getCurrentHeight() {
		return (int) (defaultHeight + popHeightAnimation.get(y));
	}
	int y = 0;
	public double currentY = 0;
	public Animation popHeightAnimation = new Animation();
	@Override
	public boolean draw(int offset, Canvas canvas, float partialTicks, Color color, boolean back) {
		y = 0;
		if (setting.popped) {
			for (Object ignored : setting.getValue().getDeclaringClass().getEnumConstants()) {
				y += (int) SkiaTextUtil.getHeight();
			}
			setHeight(defaultHeight + y);
		} else {
			setHeight(defaultHeight);
		}
		int x = parent.getX();
		int y = parent.getY() + offset - 2;
		int width = parent.getWidth();

		if (ClickGui.INSTANCE.mainEnd.booleanValue) {
			SkiaRender2DUtil.drawRectHorizontal(canvas, (float) x + 1, (float) y + 1, (float) width - 2, (float) defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), hover ? ClickGui.INSTANCE.mainHover.getValue() : Gatopera.GUI.getColor(), ClickGui.INSTANCE.mainEnd.getValue());
		} else {
			SkiaRender2DUtil.drawRect(canvas, (float) x + 1, (float) y + 1, (float) width - 2, (float) defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), hover ? ClickGui.INSTANCE.mainHover.getValue() : Gatopera.GUI.getColor());
		}
		SkiaTextUtil.drawString(canvas, setting.getName() + ": " + setting.getValue().name(), x + 4, y + getTextOffsetY(), -1);
		SkiaTextUtil.drawString(canvas, setting.popped ? "-" : "+", x + width - 11, y + getTextOffsetY(), new Color(255, 255, 255).getRGB());

		if (setting.popped) {
			currentY = animation.get(1);
		} else {
			currentY = animation.get(0);
		}
		double cy = (parent.getY() + offset - 1 + (defaultHeight - 2)) - 2;
		if (currentY > 0.04) {
			for (Object o : setting.getValue().getDeclaringClass().getEnumConstants()) {

				String s = o.toString();

				SkiaTextUtil.drawString(canvas, s, (float) (width / 2d - SkiaTextUtil.getWidth(s) / 2d + 2.0f + x), (float) (SkiaTextUtil.getHeight() / 2d + (cy)), setting.getValue().name().equals(s) ? new Color(255, 255, 255, (int) (currentY * 255)).getRGB() : new Color(120, 120, 120, (int) (currentY * 255)).getRGB());
				cy += SkiaTextUtil.getHeight() * currentY;
			}
		}
		return true;
	}
}