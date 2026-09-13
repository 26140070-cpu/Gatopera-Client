package cc.gatopera.dev.core.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.api.utils.math.FadeUtils;
import cc.gatopera.dev.api.utils.render.Snow;
import cc.gatopera.dev.api.utils.render.Render2DUtil;
import cc.gatopera.dev.api.utils.render.TextUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaRender2DUtil;
import cc.gatopera.dev.api.utils.render.skia.SkiaTextUtil;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.components.impl.ModuleComponent;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import cc.gatopera.dev.mod.gui.clickgui.tabs.Tab;
import cc.gatopera.dev.mod.gui.elements.ArmorHUD;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import io.github.humbleui.skija.Canvas;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GuiManager implements Wrapper {

	public final ArrayList<ClickGuiTab> tabs = new ArrayList<>();
	public static final ClickGuiScreen clickGui = new ClickGuiScreen();
	public final ArmorHUD armorHud;
	public static Tab currentGrabbed = null;
	private Module.Category selectedCategory = Module.Category.Combat;
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private int mouseX;
	private int mouseY;

	private static final int PANEL_WIDTH = 520;
	private static final int PANEL_HEIGHT = 340;
	private static final int SIDEBAR_WIDTH = 130;
	private static final int CONTENT_PADDING = 10;
	private static final int CONTENT_TOP_PADDING = 34;

	private int panelX;
	private int panelY;
	private int panelWidth;
	private int panelHeight;
	private int categoryScroll;

	public GuiManager() {

		armorHud = new ArmorHUD();

		int xOffset = 190;
		for (Module.Category category : Module.Category.values()) {
			ClickGuiTab tab = new ClickGuiTab(category, xOffset, 42);
			tab.setX(xOffset);
			tab.setY(42);
			for (Module module : Gatopera.MODULE.modules) {
				if (module.getCategory() == category) {
					ModuleComponent button = new ModuleComponent(tab, module);
					tab.addChild(button);
				}
			}
			tabs.add(tab);
		}
	}

	public Color getColor() {
		return ClickGui.INSTANCE.color.getValue();
	}

	private void computePanelBounds() {
		panelWidth = Math.min(PANEL_WIDTH, mc.getWindow().getScaledWidth() - 40);
		panelHeight = Math.min(PANEL_HEIGHT, mc.getWindow().getScaledHeight() - 40);
		panelX = (mc.getWindow().getScaledWidth() - panelWidth) / 2;
		panelY = (mc.getWindow().getScaledHeight() - panelHeight) / 2;
	}

	public void onUpdate() {
		computePanelBounds();
		if (isClickGuiOpen()) {
			ClickGuiTab selectedTab = getSelectedTab();
			if (selectedTab != null) {
				layoutContentTab(selectedTab);
				selectedTab.update(mouseX, mouseY);
			}
			armorHud.update(mouseX, mouseY);
		}
	}

	public void resetInteraction() {
		currentGrabbed = null;
		ClickGuiScreen.clicked = false;
		ClickGuiScreen.rightClicked = false;
		ClickGuiScreen.hoverClicked = false;
	}

	public double toGuiX(double x) {
		return (x - getGuiCenterX()) / getGuiScale() + getGuiCenterX();
	}

	public double toGuiY(double y) {
		return (y - getGuiCenterY()) / getGuiScale() + getGuiCenterY();
	}

	public void scrollContent(double mouseX, double mouseY, double amount) {
		computePanelBounds();
		double guiMouseX = toGuiX(mouseX);
		double guiMouseY = toGuiY(mouseY);
		int categoryTop = panelY + 50;
		int categoryBottom = panelY + panelHeight - 10;
		if (guiMouseX >= panelX + 8 && guiMouseX <= panelX + SIDEBAR_WIDTH - 8
				&& guiMouseY >= categoryTop && guiMouseY <= categoryBottom) {
			int contentHeight = Module.Category.values().length * 32;
			int viewportHeight = Math.max(0, categoryBottom - categoryTop);
			int maxScroll = Math.max(0, contentHeight - viewportHeight);
			categoryScroll -= (int) Math.round(amount * 24);
			categoryScroll = Math.max(-maxScroll, Math.min(0, categoryScroll));
			return;
		}
		ClickGuiTab selectedTab = getSelectedTab();
		if (selectedTab == null) {
			return;
		}
		layoutContentTab(selectedTab);
		if (guiMouseX >= selectedTab.getX() && guiMouseX <= selectedTab.getX() + selectedTab.getWidth()
				&& guiMouseY >= selectedTab.getY() + ClickGuiTab.HEADER_HEIGHT
				&& guiMouseY <= selectedTab.getY() + ClickGuiTab.HEADER_HEIGHT
				+ Math.max(0, panelHeight - CONTENT_TOP_PADDING - CONTENT_PADDING - ClickGuiTab.HEADER_HEIGHT)) {
			selectedTab.scrollBy(amount);
		}
	}

	public void selectCategory(double mouseX, double mouseY) {
		computePanelBounds();
		int left = panelX + 8;
		int width = SIDEBAR_WIDTH - 16;
		for (int index = 0; index < Module.Category.values().length; index++) {
			Module.Category category = Module.Category.values()[index];
			int top = panelY + 50 + categoryScroll + index * 32;
			if (mouseX >= left && mouseX <= left + width && mouseY >= top && mouseY <= top + 28) {
				selectedCategory = category;
				return;
			}
		}
	}

	public Module.Category getSelectedCategory() {
		return selectedCategory;
	}

	private ClickGuiTab getSelectedTab() {
		for (ClickGuiTab tab : tabs) {
			if (tab.getCategory() == selectedCategory) {
				return tab;
			}
		}
		return null;
	}

	private void layoutContentTab(ClickGuiTab tab) {
		tab.setX(panelX + SIDEBAR_WIDTH + CONTENT_PADDING);
		tab.setY(panelY + CONTENT_TOP_PADDING);
		tab.setWidth(panelWidth - SIDEBAR_WIDTH - CONTENT_PADDING * 2);
		tab.setMaxViewportHeight(panelHeight - CONTENT_TOP_PADDING - CONTENT_PADDING - ClickGuiTab.HEADER_HEIGHT);
	}

	private void trackMouseAndDrag(int x, int y) {
		boolean mouseClicked = ClickGuiScreen.clicked;
		mouseX = (int) toGuiX(x);
		mouseY = (int) toGuiY(y);
		if (!mouseClicked) {
			currentGrabbed = null;
		}
		if (currentGrabbed != null) {
			currentGrabbed.moveWindow((lastMouseX - mouseX), (lastMouseY - mouseY));
		}
		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
	}

	private float getGuiScale() {
		if (ClickGui.INSTANCE == null) {
			return 0.82f;
		}
		float scale = ClickGui.INSTANCE.guiScale.getValueInt() / 100f;
		return Math.max(scale, 0.1f);
	}

	private float getGuiCenterX() {
		return mc.getWindow().getScaledWidth() / 2f;
	}

	private float getGuiCenterY() {
		return mc.getWindow().getScaledHeight() / 2f;
	}

	public void draw(int x, int y, DrawContext drawContext, float tickDelta) {
		MatrixStack matrixStack = drawContext.getMatrices();
		trackMouseAndDrag(x, y);
		computePanelBounds();
		RenderSystem.enableCull();
		matrixStack.push();

		Render2DUtil.drawRound(matrixStack, panelX, panelY, panelWidth, panelHeight, 14,
				new Color(12, 14, 20, 238));
		Render2DUtil.drawRound(matrixStack, panelX, panelY, SIDEBAR_WIDTH, panelHeight, 14,
				new Color(20, 23, 31, 245));
		TextUtil.drawString(drawContext, "Gatopera", panelX + 16, panelY + 16, Color.WHITE);
		TextUtil.drawString(drawContext, "MODULES", panelX + 16, panelY + 34, new Color(150, 155, 170));

		int categoryY = panelY + 50 + categoryScroll;
		matrixStack.push();
		drawContext.enableScissor(panelX, panelY + 50, panelX + SIDEBAR_WIDTH, panelY + panelHeight - 10);
		for (Module.Category category : Module.Category.values()) {
			if (categoryY + 28 >= panelY + 50 && categoryY <= panelY + panelHeight - 10) {
				boolean selected = category == selectedCategory;
				if (selected) {
					Render2DUtil.drawRound(matrixStack, panelX + 8, categoryY, SIDEBAR_WIDTH - 16, 28, 8, getColor());
				}
				TextUtil.drawString(drawContext, category.name(), panelX + 20, categoryY + 8,
						selected ? Color.WHITE : new Color(190, 195, 205));
			}
			categoryY += 32;
		}
		drawContext.disableScissor();
		matrixStack.pop();

		armorHud.draw(drawContext, tickDelta, getColor());
		double quad = ClickGui.fade.ease(FadeUtils.Ease.In2);
		if (quad < 1) {
			switch (ClickGui.INSTANCE.mode.getValue()) {
				case Pull -> {
					quad = 1 - quad;
					matrixStack.translate(0, -100 * quad, 0);
				}
				case Scale -> matrixStack.scale((float) quad, (float) quad, 1);
			}
		}
		ClickGuiTab selectedTab = getSelectedTab();
		if (selectedTab != null) {
			selectedTab.draw(drawContext, tickDelta, getColor());
		}
		matrixStack.pop();
	}

	public void drawSkia(Canvas canvas, int x, int y, float tickDelta) {
		trackMouseAndDrag(x, y);
		computePanelBounds();
		canvas.save();
		float scale = getGuiScale();
		canvas.translate(getGuiCenterX(), getGuiCenterY());
		canvas.scale(scale, scale);
		canvas.translate(-getGuiCenterX(), -getGuiCenterY());

		SkiaRender2DUtil.drawRound(canvas, panelX, panelY, panelWidth, panelHeight, 14,
				new Color(12, 14, 20, 238));
		SkiaRender2DUtil.drawRound(canvas, panelX, panelY, SIDEBAR_WIDTH, panelHeight, 14,
				new Color(20, 23, 31, 245));
		SkiaTextUtil.drawString(canvas, "Gatopera", panelX + 16, panelY + 16, Color.WHITE);
		SkiaTextUtil.drawString(canvas, "MODULES", panelX + 16, panelY + 34, new Color(150, 155, 170));

		int categoryY = panelY + 50 + categoryScroll;
		canvas.save();
		canvas.clipRect(io.github.humbleui.types.Rect.makeXYWH(panelX, panelY + 50, SIDEBAR_WIDTH, panelHeight - 60));
		for (Module.Category category : Module.Category.values()) {
			if (categoryY + 28 >= panelY + 50 && categoryY <= panelY + panelHeight - 10) {
				boolean selected = category == selectedCategory;
				if (selected) {
					SkiaRender2DUtil.drawRound(canvas, panelX + 8, categoryY, SIDEBAR_WIDTH - 16, 28, 8, getColor());
				}
				SkiaTextUtil.drawString(canvas, category.name(), panelX + 20, categoryY + 8,
						selected ? Color.WHITE : new Color(190, 195, 205));
			}
			categoryY += 32;
		}
		canvas.restore();
		int categoryMaxScroll = Math.max(0, Module.Category.values().length * 32 - (panelHeight - 60));
		if (categoryMaxScroll > 0) {
			float trackHeight = panelHeight - 60;
			float thumbHeight = Math.max(24f, trackHeight * trackHeight / (Module.Category.values().length * 32f));
			float thumbY = panelY + 50 + (-categoryScroll / (float) categoryMaxScroll) * (trackHeight - thumbHeight);
			SkiaRender2DUtil.drawRound(canvas, panelX + SIDEBAR_WIDTH - 5, thumbY, 3, thumbHeight, 1.5f,
					new Color(255, 255, 255, 110));
		}

		double quad = ClickGui.fade.ease(FadeUtils.Ease.In2);
		if (quad < 1) {
			switch (ClickGui.INSTANCE.mode.getValue()) {
				case Pull -> {
					quad = 1 - quad;
					canvas.translate(0, (float) (-100 * quad));
				}
				case Scale -> canvas.scale((float) quad, (float) quad);
			}
		}
		ClickGuiTab selectedTab = getSelectedTab();
		if (selectedTab != null) {
			layoutContentTab(selectedTab);
			selectedTab.drawSkia(canvas, tickDelta, getColor());
			int maxScroll = selectedTab.getMaxScroll();
			if (maxScroll > 0) {
				float trackHeight = selectedTab.getViewportHeightForRender();
				float thumbHeight = Math.max(22f, trackHeight * trackHeight / selectedTab.getContentHeightForRender());
				float thumbY = selectedTab.getY() + ClickGuiTab.HEADER_HEIGHT
						+ (-selectedTab.getScrollOffset() / (float) maxScroll) * (trackHeight - thumbHeight);
				SkiaRender2DUtil.drawRound(canvas, selectedTab.getX() + selectedTab.getWidth() - 5,
						thumbY, 3, thumbHeight, 1.5f, new Color(255, 255, 255, 110));
			}
		}
		canvas.restore();
	}

	public void drawNativeText(DrawContext drawContext) {
		computePanelBounds();
		MatrixStack matrices = drawContext.getMatrices();
		matrices.push();
		float scale = getGuiScale();
		matrices.translate(getGuiCenterX(), getGuiCenterY(), 0);
		matrices.scale(scale, scale, 1);
		matrices.translate(-getGuiCenterX(), -getGuiCenterY(), 0);

		double quad = ClickGui.fade.ease(FadeUtils.Ease.In2);
		if (quad < 1) {
			switch (ClickGui.INSTANCE.mode.getValue()) {
				case Pull -> {
					quad = 1 - quad;
					matrices.translate(0, -100 * quad, 0);
				}
				case Scale -> matrices.scale((float) quad, (float) quad, 1);
			}
		}
		SkiaTextUtil.flush(drawContext);
		matrices.pop();
	}

	public boolean isClickGuiOpen() {
		return mc.currentScreen instanceof ClickGuiScreen;
	}

	public static final ArrayList<Snow> snows = new ArrayList<>(){
		{
			Random random = new Random();
			for (int i = 0; i < 100; ++i) {
				for (int y = 0; y < 3; ++y) {
					add(new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1));
				}
			}
		}
	};
}