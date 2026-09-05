package cc.gatopera.dev.mod.gui.clickgui.components;

import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.api.utils.math.AnimateUtil;
import cc.gatopera.dev.api.utils.math.Animation;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import io.github.humbleui.skija.Canvas;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.awt.Color;

public abstract class Component implements Wrapper {
	public int defaultHeight = 16;
	protected ClickGuiTab parent;
	private int height = defaultHeight;

	public Animation animation = new Animation();
	public Component() {
	}

	public boolean isVisible() {
		return true;
	}

	public int getHeight() {
		if (!isVisible()) {
			return 0;
		}
		return height;
	}

	public int getCurrentHeight() {
		return getHeight();
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ClickGuiTab getParent() {
		return parent;
	}

	public void setParent(ClickGuiTab parent) {
		this.parent = parent;
	}

	public abstract void update(int offset, double mouseX, double mouseY);

	public boolean draw(int offset, Canvas canvas, float partialTicks, Color color, boolean back) {
		return false;
	}

	public double currentOffset = 0;

	public double getTextOffsetY() {
		return (defaultHeight - SkiaTextUtil.getHeight()) / 2D + (ClickGui.INSTANCE.maxFill.getValue() ? 2 : 1);
	}

	public static double animate(double current, double endPoint, double speed) {
		return AnimateUtil.animate(current, endPoint, speed);
	}

	public static void sound() {
		if (ClickGui.INSTANCE.sound.getValue() && !Module.nullCheck()) {
			mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.BLOCKS, (float) 100f, 1.9f);
		}
	}
}