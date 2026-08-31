package cc.gatopera.dev.mod.gui.misc;

import cc.gatopera.dev.api.utils.Wrapper;
import cc.gatopera.dev.api.utils.render.GatoperaPipelines;
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
        float halfOfWidth = mc.getWindow().getScaledWidth() / 2f;
        float halfOfHeight = mc.getWindow().getScaledHeight() / 2f;
        float mainX = halfOfWidth - 120f;
        float mainY = halfOfHeight - 80f;
        float mainWidth = 240f;
        float mainHeight = 140f;

        GatoperaPipelines.drawWindowBackground(context.getMatrices(), mainX, mainY, mainWidth, mainHeight, new Color(18, 18, 24, 230));

        if (FontRenderers.ui != null) {
            FontRenderers.ui.drawCenteredString(context.getMatrices(), header, mainX + mainWidth / 2f, mainY + 10, -1);
            FontRenderers.ui.drawCenteredString(context.getMatrices(), description, mainX + mainWidth / 2f, mainY + 28, new Color(0xABFFFFFF, true).getRGB());
        }

        boolean yesH = yesHovered(mouseX, mouseY);
        boolean noH = noHovered(mouseX, mouseY);
        Color base = new Color(32, 32, 40, 220);
        Color hover = new Color(55, 55, 70, 240);

        GatoperaPipelines.drawButton(context.getMatrices(), mainX + 5, mainY + 95, 110, 40, yesH, base, hover);
        GatoperaPipelines.drawButton(context.getMatrices(), mainX + 125, mainY + 95, 110, 40, noH, base, hover);

        if (FontRenderers.ui != null) {
            FontRenderers.ui.drawCenteredString(context.getMatrices(), yesText, mainX + 60, mainY + 112, yesH ? -1 : new Color(0xABFFFFFF, true).getRGB());
            FontRenderers.ui.drawCenteredString(context.getMatrices(), noText, mainX + 180f, mainY + 112, noH ? -1 : new Color(0xABFFFFFF, true).getRGB());
        }

        if (pic != null) {
            context.drawTexture(pic, (int) (mainX + mainWidth / 2f - 35), (int) mainY + 42, 0, 0, 70, 45, 70, 45);
        }
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    private boolean yesHovered(int mX, int mY) {
        float mainX = (mc.getWindow().getScaledWidth() / 2f) - 120f;
        float mainY = (mc.getWindow().getScaledHeight() / 2f) - 80f;
        return isHovered(mX, mY, (int) mainX + 5, (int) mainY + 95, 110, 40);
    }

    private boolean noHovered(int mX, int mY) {
        float mainX = (mc.getWindow().getScaledWidth() / 2f) - 120f;
        float mainY = (mc.getWindow().getScaledHeight() / 2f) - 80f;
        return isHovered(mX, mY, (int) mainX + 125, (int) mainY + 95, 110, 40);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (yesHovered((int) mouseX, (int) mouseY)) {
            yesAction.run();
        } else if (noHovered((int) mouseX, (int) mouseY)) {
            noAction.run();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}