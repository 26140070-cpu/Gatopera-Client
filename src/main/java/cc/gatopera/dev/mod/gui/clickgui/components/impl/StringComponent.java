package cc.gatopera.dev.mod.gui.clickgui.components.impl;

import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.impl.StringSetting;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.api.utils.render.skia.SkiaRender2DUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import io.github.humbleui.skija.Canvas;

import java.awt.*;

public class StringComponent extends Component {
	private final StringSetting setting;

	public StringComponent(ClickGuiTab parent, StringSetting setting) {
		super();
		this.setting = setting;
		this.parent = parent;
	}
	@Override
	public boolean isVisible() {
		if (setting.visibility != null) {
			return setting.visibility.getAsBoolean();
		}
		return true;
	}

	boolean hover = false;

	public void update(int offset, double mouseX, double mouseY) {
		if (GuiManager.currentGrabbed == null && isVisible()) {
			int parentX = parent.getX();
			int parentY = parent.getY();
			int parentWidth = parent.getWidth();
			if ((mouseX >= ((parentX + 1)) && mouseX <= (((parentX)) + parentWidth - 1)) && (mouseY >= (((parentY + offset))) && mouseY <= ((parentY + offset) + defaultHeight - 2))) {
				hover = true;
				if (ClickGuiScreen.clicked) {
					sound();
					ClickGuiScreen.clicked = false;
					setting.setListening(!setting.isListening());
				}
			} else {
				if(ClickGuiScreen.clicked && setting.isListening()) {
					sound();
					setting.setListening(false);
				}
				hover = false;
			}
		} else {
			if (setting.isListening()) {
				setting.setListening(false);
			}
			hover = false;
		}
	}

	private final Timer timer = new Timer();
	boolean b;

	@Override
	public boolean draw(int offset, Canvas canvas, float partialTicks, Color color, boolean back) {
		if (timer.passed(1000)) {
			b = !b;
			timer.reset();
		}
		if (back) {
			setting.setListening(false);
		}
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		int y = parent.getY() + offset - 2;
		int width = parent.getWidth();
		String text = setting.getValue();
		if (setting.isListening() && b) {
			text = text + "_";
		}
		String name = setting.isListening() ? "[E]" : setting.getName();
		if (hover)
			SkiaRender2DUtil.drawRect(canvas, (float) parentX + 1, (float) y + 1, (float) width - 3, (float) defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), ClickGui.INSTANCE.settingHover.getValue());
		SkiaTextUtil.drawString(canvas, text, parentX + 4 + SkiaTextUtil.getWidth(name) / 2,
				(float) (parentY + getTextOffsetY() + offset) - 2, 0xFFFFFF);
		SkiaTextUtil.drawStringWithScale(canvas, name, (float) (parentX + 4),
				(float) (parentY + getTextOffsetY() + offset - 2), -1, 0.5f);
		return true;
	}
}