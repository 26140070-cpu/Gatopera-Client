package cc.gatopera.dev.core.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.api.utils.math.FadeUtils;
import cc.gatopera.dev.api.utils.render.Snow;
import cc.gatopera.dev.api.utils.render.Render2DUtil;
import cc.gatopera.dev.api.utils.render.TextUtil;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.gui.clickgui.components.impl.ModuleComponent;
import cc.gatopera.dev.mod.gui.clickgui.tabs.ClickGuiTab;
import cc.gatopera.dev.mod.gui.clickgui.tabs.Tab;
import cc.gatopera.dev.mod.gui.elements.ArmorHUD;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
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

	public void onUpdate() {
		if (isClickGuiOpen()) {
			for (ClickGuiTab tab : tabs) {
				if (tab.getCategory() == selectedCategory) {
					tab.update(mouseX, mouseY);
				}
			}
			armorHud.update(mouseX, mouseY);
		}
	}

	public void selectCategory(double mouseX, double mouseY) {
		int left = 20;
		int top = 62;
		for (Module.Category category : Module.Category.values()) {
			if (mouseX >= left && mouseX <= left + 140 && mouseY >= top && mouseY <= top + 30) {
				selectedCategory = category;
				return;
			}
			top += 36;
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

	public void draw(int x, int y, DrawContext drawContext, float tickDelta) {
		MatrixStack matrixStack = drawContext.getMatrices();
		boolean mouseClicked = ClickGuiScreen.clicked;
		mouseX = x;
		mouseY = y;
		if (!mouseClicked) {
			currentGrabbed = null;
		}
		if (currentGrabbed != null) {
			currentGrabbed.moveWindow((lastMouseX - mouseX), (lastMouseY - mouseY));
		}
		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
		RenderSystem.enableCull();
		matrixStack.push();

		int panelX = 12;
		int panelY = 12;
		int panelWidth = mc.getWindow().getScaledWidth() - 24;
		int panelHeight = mc.getWindow().getScaledHeight() - 24;
		Render2DUtil.drawRound(matrixStack, panelX, panelY, panelWidth, panelHeight, 14,
				new Color(12, 14, 20, 238));
		Render2DUtil.drawRound(matrixStack, panelX, panelY, 164, panelHeight, 14,
				new Color(20, 23, 31, 245));
		TextUtil.drawString(drawContext, "Gatopera", 28, 28, Color.WHITE);
		TextUtil.drawString(drawContext, "MODULES", 28, 48, new Color(150, 155, 170));

		int categoryY = 62;
		for (Module.Category category : Module.Category.values()) {
			boolean selected = category == selectedCategory;
			if (selected) {
				Render2DUtil.drawRound(matrixStack, 20, categoryY, 140, 30, 8, getColor());
			}
			TextUtil.drawString(drawContext, category.name(), 32, categoryY + 9,
					selected ? Color.WHITE : new Color(190, 195, 205));
			categoryY += 36;
		}

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
