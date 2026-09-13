package cc.gatopera.dev.mod.gui.clickgui.tabs;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.render.skia.SkiaGlassUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.components.impl.ModuleComponent;
import cc.gatopera.dev.mod.modules.Module;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.types.Rect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;
import java.util.ArrayList;

public class ClickGuiTab extends Tab {
	public static final int HEADER_HEIGHT = 26;
	protected String title;
	private Module.Category category = null;
	protected final ArrayList<ModuleComponent> children = new ArrayList<>();
	private int scrollOffset;
	private int maxViewportHeight = Integer.MAX_VALUE;

	public ClickGuiTab(String title, int x, int y) {
		this.title = title;
		this.x = Gatopera.CONFIG.getInt(title + "_x", x);
		this.y = Gatopera.CONFIG.getInt(title + "_y", y);
		this.width = 132;
		this.mc = MinecraftClient.getInstance();
	}

	public ClickGuiTab(Module.Category category, int x, int y) {
		this(category.name(), x, y);
		this.category = category;
	}

	public final Module.Category getCategory() {
		return category;
	}

	public ArrayList<ModuleComponent> getChildren() {
		return children;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public final int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		this.width = width;
	}

	public final int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		this.height = height;
	}

	public final void setMaxViewportHeight(int maxViewportHeight) {
		this.maxViewportHeight = maxViewportHeight;
	}

	public final boolean isGrabbed() {
		return (GuiManager.currentGrabbed == this);
	}

	public final void addChild(ModuleComponent component) {
		this.children.add(component);
	}

	@Override
	public void update(double mouseX, double mouseY) {
		int tempHeight = 1;
		for (ModuleComponent child : children) {
			tempHeight += child.getHeight();
		}
		this.height = tempHeight;
		clampScroll();
		int i = HEADER_HEIGHT;
		for (ModuleComponent child : children) {
			int childTop = y + i + scrollOffset;
			int childBottom = childTop + child.getHeight();
			boolean visible = childBottom >= getContentTop() && childTop <= getContentBottom();
			child.update(i + scrollOffset, visible ? mouseX : -100000, visible ? mouseY : -100000);
			i += child.getHeight();
		}
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks, Color color) {
	}

	public void drawSkia(Canvas canvas, float partialTicks, Color color) {
		int tempHeight = 1;
		for (ModuleComponent child : children) {
			tempHeight += child.getHeight();
		}
		this.height = tempHeight;
		clampScroll();
		int viewportHeight = getViewportHeight();

		SkiaGlassUtil.drawGlassPanel(canvas, x, y, width, HEADER_HEIGHT + viewportHeight,
				Component.PANEL_RADIUS, new Color(20, 20, 28, 150),
				new Color(255, 255, 255, 35), 0f);

		SkiaTextUtil.drawString(canvas, title, x + 10f, y + (HEADER_HEIGHT - SkiaTextUtil.getHeight()) / 2f, 0xFFFFFFFF);

		canvas.save();
		canvas.clipRect(Rect.makeXYWH(x, getContentTop(), width, viewportHeight), ClipMode.INTERSECT);
		SkiaTextUtil.pushClip(x, getContentTop(), width, viewportHeight);
		int i = HEADER_HEIGHT + scrollOffset;
		for (Component child : children) {
			child.draw(i, canvas, partialTicks, color, false);
			i += child.getHeight();
		}
		SkiaTextUtil.popClip();
		canvas.restore();
	}

	public void scroll(double mouseX, double mouseY, double amount) {
		if (mouseX < x || mouseX > x + width || mouseY < getContentTop() || mouseY > getContentBottom()) {
			return;
		}
		scrollBy(amount);
	}

	public void scrollBy(double amount) {
		scrollOffset -= (int) Math.round(amount * 24);
		clampScroll();
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public int getMaxScroll() {
		return Math.max(0, height - 1 - getViewportHeight());
	}

	public float getViewportHeightForRender() {
		return getViewportHeight();
	}

	public float getContentHeightForRender() {
		return Math.max(1, height - 1);
	}

	private int getContentTop() {
		return y + HEADER_HEIGHT;
	}

	private int getContentBottom() {
		return getContentTop() + getViewportHeight();
	}

	private int getViewportHeight() {
		int available = mc.getWindow().getScaledHeight() - getContentTop() - 12;
		return Math.max(0, Math.min(available, maxViewportHeight));
	}

	private void clampScroll() {
		int maxScroll = Math.max(0, height - 1 - getViewportHeight());
		scrollOffset = Math.max(-maxScroll, Math.min(0, scrollOffset));
	}
}