package cc.gatopera.dev.mod.gui.clickgui.tabs;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.mod.gui.clickgui.components.Component;
import cc.gatopera.dev.mod.gui.clickgui.components.impl.ModuleComponent;
import cc.gatopera.dev.mod.modules.Module;
import io.github.humbleui.skija.Canvas;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;
import java.util.ArrayList;

public class ClickGuiTab extends Tab {
	protected String title;
	private Module.Category category = null;
	protected final ArrayList<ModuleComponent> children = new ArrayList<>();

	public ClickGuiTab(String title, int x, int y) {
		this.title = title;
		this.x = Gatopera.CONFIG.getInt(title + "_x", x);
		this.y = Gatopera.CONFIG.getInt(title + "_y", y);
		this.width = 98;
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
		int i = 0;
		for (ModuleComponent child : children) {
			child.update(i, mouseX, mouseY);
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

		int i = 0;
		for (Component child : children) {
			child.draw(i, canvas, partialTicks, color, false);
			i += child.getHeight();
		}
	}
}