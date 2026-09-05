package cc.gatopera.dev.mod.gui.clickgui.components.impl;

import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.impl.BindSetting;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.render.skia.SkiaRender2DUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import io.github.humbleui.skija.Canvas;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class BindComponent extends Component {
	private final BindSetting bind;
	public BindComponent(ClickGuiTab parent, BindSetting bind) {
		super();
		this.bind = bind;
		this.parent = parent;
	}

	boolean hover = false;

	public void update(int offset, double mouseX, double mouseY) {
		if (GuiManager.currentGrabbed == null && isVisible()) {
			int parentX = parent.getX();
			int parentY = parent.getY();
			int parentWidth = parent.getWidth();
			if (GuiManager.currentGrabbed == null && isVisible() && (mouseX >= ((parentX + 1)) && mouseX <= (((parentX)) + parentWidth - 1)) && (mouseY >= (((parentY + offset))) && mouseY <= ((parentY + offset) + defaultHeight - 2))) {
				hover = true;
				if (ClickGuiScreen.clicked) {
					sound();
					ClickGuiScreen.clicked = false;
					if (bind.getName().equals("Key") && InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
						bind.setHoldEnable(!bind.isHoldEnable());
					} else {
						bind.setListening(!bind.isListening());
					}
				}
			} else {
				hover = false;
			}
		} else {
			hover = false;
		}
	}

	@Override
	public boolean draw(int offset, Canvas canvas, float partialTicks, Color color, boolean back) {
		if (back) {
			bind.setListening(false);
		}
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		int y = parent.getY() + offset - 2;
		int width = parent.getWidth();
		String text;
		if (hover && bind.getName().equals("Key") && InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			text = "Hold " + (bind.isHoldEnable() ? "§aOn" : "§cOff");
		} else {
			if (bind.isListening()) {
				text = bind.getName() + ": " + "Press Key..";
			} else {
				text = bind.getName() + ": " + bind.getBind();
			}
		}
		if (hover) SkiaRender2DUtil.drawRect(canvas, (float) parentX + 1, (float) y + 1, (float) width - 3, (float) defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), ClickGui.INSTANCE.settingHover.getValue());
		SkiaTextUtil.drawString(canvas, text, (float) (parentX + 4),
				(float) (parentY + getTextOffsetY() + offset) - 2, 0xFFFFFF);
		return true;
	}
}