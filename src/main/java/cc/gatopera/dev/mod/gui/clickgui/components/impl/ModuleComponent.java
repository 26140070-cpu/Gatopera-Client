package cc.gatopera.dev.mod.gui.clickgui.components.impl;

import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.math.Animation;
import cc.gatopera.dev.api.utils.math.FadeUtils;
import cc.gatopera.dev.api.utils.render.skia.SkiaRender2DUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.Setting;
import cc.gatopera.dev.mod.modules.settings.impl.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.types.Rect;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleComponent extends Component {

	private final Module module;
	private final ClickGuiTab parent;
	private boolean popped = false;

	private int expandedHeight = defaultHeight;

	private final List<Component> settingsList = new ArrayList<>();
	public List<Component> getSettingsList() {
		return settingsList;
	}
	public ModuleComponent(ClickGuiTab parent, Module module) {
		super();
		this.parent = parent;
		this.module = module;
		for (Setting setting : this.module.getSettings()) {
			Component c;
			if (setting.hide) {
				c = null;
			} else if (setting instanceof SliderSetting) {
				c = new SliderComponent(this.parent, (SliderSetting) setting);
			} else if (setting instanceof BooleanSetting) {
				c = new BooleanComponent(this.parent, (BooleanSetting) setting);
			} else if (setting instanceof BindSetting) {
				c = new BindComponent(this.parent, (BindSetting) setting);
			} else if (setting instanceof EnumSetting) {
				c = new EnumComponent(this.parent, (EnumSetting<?>) setting);
			} else if (setting instanceof ColorSetting) {
				c = new ColorComponents(this.parent, (ColorSetting) setting);
			} else if (setting instanceof StringSetting) {
				c = new StringComponent(this.parent, (StringSetting) setting);
			} else {
				c = null;
			}
			if (c != null)
				settingsList.add(c);
		}

		RecalculateExpandedHeight();
	}

	boolean hovered = false;

	public void update(int offset, double mouseX, double mouseY) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();

		if (this.popped) {
			int i = offset + defaultHeight + 1;
			for (Component children : this.settingsList) {
				children.update(i, mouseX, mouseY);
				i += children.getHeight();
			}
		}

		hovered = ((mouseX >= parentX && mouseX <= (parentX + parentWidth)) && (mouseY >= parentY + offset && mouseY <= (parentY + offset + defaultHeight - 1)));
		if (hovered && GuiManager.currentGrabbed == null) {
			if (ClickGuiScreen.clicked) {
				ClickGuiScreen.clicked = false;
				if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
					sound();
					module.drawnSetting.toggleValue();
				} else {
					sound();
					module.toggle();
				}
			}

			if (ClickGuiScreen.rightClicked) {
				ClickGuiScreen.rightClicked = false;
				this.popped = !this.popped;
				sound();
			}
		}
		RecalculateExpandedHeight();
		if (this.popped) {
			this.setHeight(expandedHeight);
		} else {
			this.setHeight(defaultHeight);
		}
	}

	public double currentWidth = 0;
	public Animation offsetAnimation = new Animation();
	public double currentPopHeight = 0;
	public Animation popHeightAnimation = new Animation();
	@Override
	public boolean draw(int offset, Canvas canvas, float partialTicks, Color color, boolean back) {
		RecalculateExpandedHeight();
		String text = module.getDisplayName();
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		currentOffset = offsetAnimation.get(offset);
		boolean scissor = ClickGui.fade.ease(FadeUtils.Ease.Out) >= 1;
		currentPopHeight = popHeightAnimation.get(popped ? (expandedHeight - defaultHeight) : 0);
		if (currentPopHeight > 0) {
			int i = (int) (currentOffset + defaultHeight + 1);
			if (scissor) {
				canvas.save();
				canvas.clipRect(Rect.makeXYWH(parentX, (float) (parentY + currentOffset + defaultHeight), parentWidth, mc.getWindow().getScaledHeight() - (float) (parentY + currentOffset + defaultHeight)), ClipMode.INTERSECT);
				canvas.save();
				canvas.clipRect(Rect.makeXYWH(parentX, parentY + i - 1, parentWidth, (float) ((parentY + currentOffset + defaultHeight + currentPopHeight) - (parentY + i - 1))), ClipMode.INTERSECT);
			}
			for (Component children : this.settingsList) {
				if (children.isVisible()) {
					children.draw(i, canvas, partialTicks, color, !popped);
					i += children.getCurrentHeight();
				}
			}
			if (scissor) {
				canvas.restore();
				canvas.restore();
			}
		}
		currentWidth = animation.get(module.isOn() ? (parentWidth - 2D) : 0D);
		if (ClickGui.INSTANCE.activeBox.getValue()) {
			if (ClickGui.INSTANCE.mainEnd.booleanValue) {
				SkiaRender2DUtil.drawRectHorizontal(canvas, parentX + 1, (int) (parentY + currentOffset), (float) currentWidth, defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), hovered ? ClickGui.INSTANCE.mainHover.getValue() : ClickGui.INSTANCE.color.getValue(), ClickGui.INSTANCE.mainEnd.getValue());
			} else {
				SkiaRender2DUtil.drawRect(canvas, parentX + 1, (int) (parentY + currentOffset), (float) currentWidth, defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), hovered ? ClickGui.INSTANCE.mainHover.getValue() : ClickGui.INSTANCE.color.getValue());
			}
		}
		if (module.isOff() || !ClickGui.INSTANCE.activeBox.getValue())
			SkiaRender2DUtil.drawRect(canvas, parentX + 1, (int) (parentY + currentOffset), parentWidth - 2, defaultHeight - (ClickGui.INSTANCE.maxFill.getValue() ? 0 : 1), hovered ? ClickGui.INSTANCE.moduleHover.getValue() : ClickGui.INSTANCE.module.getValue());
		if (hovered && InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
			SkiaTextUtil.drawString(canvas, "Drawn " + (module.drawnSetting.getValue() ? "§aOn" : "§cOff"), (float) (parentX + 4), (float) (parentY + getTextOffsetY() + currentOffset) - 1, -1);
		} else {
			if (ClickGui.INSTANCE.center.getValue()) {
				SkiaTextUtil.drawString(canvas, text, parentX + parentWidth / 2f - SkiaTextUtil.getWidth(text) / 2, (float) (parentY + getTextOffsetY() + currentOffset) - 1,
						module.isOn() ? ClickGui.INSTANCE.enableText.getValue().getRGB() : ClickGui.INSTANCE.disableText.getValue().getRGB());
			} else {
				SkiaTextUtil.drawString(canvas, text, (float) (parentX + 4), (float) (parentY + getTextOffsetY() + currentOffset) - 1,
						module.isOn() ? ClickGui.INSTANCE.enableText.getValue().getRGB() : ClickGui.INSTANCE.disableText.getValue().getRGB());
			}
		}

		if (ClickGui.INSTANCE.bind.booleanValue) {
			if (module.getBind().getKey() != -1) {
				String bindText = "[" + module.getBind().getBind() + "]";
				SkiaTextUtil.drawStringWithScale(canvas, bindText, (ClickGui.INSTANCE.center.getValue() ? (parentX + parentWidth / 2f - SkiaTextUtil.getWidth(text) / 2) : (parentX + 4)) + 1 + SkiaTextUtil.getWidth(text), (float) (parentY + getTextOffsetY() + currentOffset - SkiaTextUtil.getHeight() / 4), ClickGui.INSTANCE.bind.getValue(), 0.5f);
			}
		}
		if (ClickGui.INSTANCE.gear.booleanValue) {
			if (popped) {
				SkiaTextUtil.drawString(canvas, "-", parentX + parentWidth - 11,
						parentY + getTextOffsetY() + currentOffset - 1, ClickGui.INSTANCE.gear.getValue().getRGB());
			} else {
				SkiaTextUtil.drawString(canvas, "+", parentX + parentWidth - 11,
						parentY + getTextOffsetY() + currentOffset - 1, ClickGui.INSTANCE.gear.getValue().getRGB());
			}
		}
		return true;
	}

	public void RecalculateExpandedHeight() {
		int height = defaultHeight;
		for (Component children : this.settingsList) {
			if (children != null && children.isVisible()) {
				height += children.getHeight();
			}
		}
		expandedHeight = height;
	}
}