package cc.gatopera.dev.mod.gui.misc;

import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.api.utils.render.GatoperaPipelines;
import cc.gatopera.dev.api.utils.render.Render2DUtil;
import cc.gatopera.dev.mod.gui.font.FontRenderers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DialogScreen extends Screen implements Wrapper {
    private final Identifier pic;
    private final String header;
    private final String description;
    private final String yesText;
    private final String noText;
    private final Runnable yesAction;
    private final Runnable noAction;

    public DialogScreen(Identifier pic, String header, String description, String yesText, String noText, Runnable yesAction, Runnable noAction) {
        super(Text.of("DialogScreen"));
        this.pic = pic;
        this.header = header;
        this.description = description;
        this.yesText = yesText;
        this.noText = noText;
        this.yesAction = yesAction;
        this.noAction = noAction;
    }

    @Override
    public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xFF0A0A0F);

        float mainX = this.width / 2f - 120f;
        float mainY = this.height / 2f - 80f;
        float mainWidth = 240f;
        float mainHeight = 140f;

        try {
            GatoperaPipelines.drawWindowBackground(context.getMatrices(), mainX, mainY, mainWidth, mainHeight, new Color(18, 18, 24, 230));
        } catch (Throwable t) {
            context.fill((int) mainX, (int) mainY, (int) (mainX + mainWidth), (int) (mainY + mainHeight), 0xE6121218);
        }

        boolean useCustom = FontRenderers.customFontsAvailable
                && FontRenderers.ui != null
                && FontRenderers.ui.getClass().getSimpleName().equals("StbFontAdapter");

        if (useCustom) {
            try {
                FontRenderers.ui.drawCenteredString(context.getMatrices(), header, mainX + mainWidth / 2f, mainY + 12, -1);
                FontRenderers.ui.drawCenteredString(context.getMatrices(), description, mainX + mainWidth / 2f, mainY + 32, 0xFFAAAAAA);
            } catch (Throwable t) {
                useCustom = false;
            }
        }
        if (!useCustom) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.of(header), (int) (mainX + mainWidth / 2f), (int) (mainY + 12), 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.of(description), (int) (mainX + mainWidth / 2f), (int) (mainY + 32), 0xAAAAAA);
        }

        boolean yesH = yesHovered(mouseX, mouseY);
        boolean noH = noHovered(mouseX, mouseY);
        Color base = new Color(32, 32, 40, 220);
        Color hover = new Color(55, 55, 70, 240);

        try {
            GatoperaPipelines.drawButton(context.getMatrices(), mainX + 5, mainY + 95, 110, 40, yesH, base, hover);
            GatoperaPipelines.drawButton(context.getMatrices(), mainX + 125, mainY + 95, 110, 40, noH, base, hover);
        } catch (Throwable t) {
            context.fill((int) mainX + 5, (int) mainY + 95, (int) mainX + 115, (int) mainY + 135, yesH ? 0xF0373746 : 0xDC202028);
            context.fill((int) mainX + 125, (int) mainY + 95, (int) mainX + 235, (int) mainY + 135, noH ? 0xF0373746 : 0xDC202028);
        }

        if (useCustom) {
            try {
                FontRenderers.ui.drawCenteredString(context.getMatrices(), yesText, mainX + 60, mainY + 110, yesH ? -1 : 0xFFCCCCCC);
                FontRenderers.ui.drawCenteredString(context.getMatrices(), noText, mainX + 180, mainY + 110, noH ? -1 : 0xFFCCCCCC);
            } catch (Throwable t) {
                context.drawCenteredTextWithShadow(this.textRenderer, Text.of(yesText), (int) (mainX + 60), (int) (mainY + 110), 0xFFFFFF);
                context.drawCenteredTextWithShadow(this.textRenderer, Text.of(noText), (int) (mainX + 180), (int) (mainY + 110), 0xFFFFFF);
            }
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.of(yesText), (int) (mainX + 60), (int) (mainY + 110), 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.of(noText), (int) (mainX + 180), (int) (mainY + 110), 0xFFFFFF);
        }
    }

    private boolean yesHovered(int mX, int mY) {
        int boxW = 240;
        int boxH = 140;
        int boxX = (this.width - boxW) / 2;
        int boxY = (this.height - boxH) / 2;
        int btnY = boxY + 95;
        return mX >= boxX + 10 && mX <= boxX + 110 && mY >= btnY && mY <= btnY + 30;
    }

    private boolean noHovered(int mX, int mY) {
        int boxW = 240;
        int boxH = 140;
        int boxX = (this.width - boxW) / 2;
        int boxY = (this.height - boxH) / 2;
        int btnY = boxY + 95;
        return mX >= boxX + 130 && mX <= boxX + 230 && mY >= btnY && mY <= btnY + 30;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (yesHovered((int) mouseX, (int) mouseY)) {
                yesAction.run();
                return true;
            } else if (noHovered((int) mouseX, (int) mouseY)) {
                noAction.run();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}