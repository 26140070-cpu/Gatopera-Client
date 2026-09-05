package cc.gatopera.dev.mod.gui.elements;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.mod.gui.clickgui.ClickGuiScreen;
import cc.gatopera.dev.mod.modules.impl.client.HUD;
import cc.gatopera.dev.core.impl.GuiManager;
import cc.gatopera.dev.api.utils.entity.EntityUtil;
import cc.gatopera.dev.api.utils.render.ColorUtil;
import cc.gatopera.dev.api.utils.render.Render2DUtil;
import cc.gatopera.dev.api.utils.render.TextUtil;
import cc.gatopera.dev.mod.gui.clickgui.tabs.Tab;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ArmorHUD extends Tab {

	public ArmorHUD() {
		this.width = 80;
		this.height = 34;
		this.x = (int) Gatopera.CONFIG.getFloat("armor_x", 0);
		this.y = (int) Gatopera.CONFIG.getFloat("armor_y", 200);
	}

	@Override
	public void update(double mouseX, double mouseY) {
		if (GuiManager.currentGrabbed == null && HUD.INSTANCE.armor.getValue()) {
			if (mouseX >= (x) && mouseX <= (x + width)) {
				if (mouseY >= (y) && mouseY <= (y + height)) {
					if (ClickGuiScreen.clicked) {
						GuiManager.currentGrabbed = this;
					}
				}
			}
		}
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks, Color color) {
		MatrixStack matrixStack = drawContext.getMatrices();
		if (HUD.INSTANCE.armor.getValue()) {
			if (Gatopera.GUI.isClickGuiOpen()) {
				Render2DUtil.drawRect(drawContext.getMatrices(), x, y, width, height, new Color(0, 0, 0, 70));
			}
			int xOff = 0;
			for (ItemStack armor : mc.player.getInventory().armor) {
				xOff += 20;

				if (armor.isEmpty()) continue;
				matrixStack.push();
				int damage = EntityUtil.getDamagePercent(armor);
				int yOffset = height / 2;
				drawContext.drawItem(armor, this.x + width - xOff, this.y + yOffset);
				drawContext.drawItemInSlot(mc.textRenderer, armor, this.x + width - xOff, this.y + yOffset);
				TextUtil.drawStringScale(drawContext, damage + "%",
                        (float) (x + width + 2 - xOff),
                        (float) (y + yOffset - mc.textRenderer.fontHeight / 4d),
						ColorUtil.fadeColor(new Color(196, 0, 0), new Color(0, 227, 0), damage / 100f).getRGB(), 0.5F);

				matrixStack.pop();
			}
		}
	}
}
