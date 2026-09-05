package cc.gatopera.dev.mod.gui.clickgui.components.impl;

import cc.gatopera.dev.api.i18n.I18n;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.impl.BindSetting;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.render.Render2DUtil;
import cc.gatopera.dev.api.utils.render.TextUtil;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
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

	public boolean draw(int offset, DrawContext drawContext, float partialTicks, Color color, boolean back) {
		if (back) {
			bind.setListening(false);
		}
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		int y = parent.getY() + offset - 2;
		int width = parent.getWidth();
		MatrixStack matrixStack = drawContext.getMatrices();
		String text;
		if (hover && bind.getName().equals("Key") && InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			text = I18n.t("bind.hold") + " " + (bind.isHoldEnable() ? "§a" + I18n.t("state.on") : "§c" + I18n.t("state.off"));
		} else {
			if (bind.isListening()) {
				text = bind.getDisplayName() + ": " + I18n.t("bind.press_key");
			} else {
				text = bind.getDisplayName() + ": " + bind.getBind();
			}
		}
		if (hover) Render2DUtil.drawRect(matrixStack, (float) parentX + 1, (float) y + 1, (float) width - 3, (float) defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), ClickGui.INSTANCE.settingHover.getValue());
		TextUtil.drawString(drawContext, text, (float) (parentX + 4),
				(float) (parentY + getTextOffsetY() + offset) - 2, 0xFFFFFF);
		return true;
	}
}