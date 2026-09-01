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

import java.awt.Color;

public class DialogScreen extends Screen implements Wrapper {
    private final Identifier pic;
    private final String header;
    private final String description;
    private final String yesText;
    private final String noText;
    private final Runnable yesAction;
    private final Runnable noAction;

    public DialogScreen(
            Identifier pic,
            String header,
            String description,
            String yesText,
            String noText,
            Runnable yesAction,
            Runnable noAction
    ) {
        super(Text.of("Dialog"));
        this.pic = pic;
        this.header = header;
        this.description = description;
        this.yesText = yesText;
        this.noText = noText;
        this.yesAction = yesAction;
        this.noAction = noAction;
    }

    @Override
    public void render(
            @NotNull DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        context.fill(0, 0, this.width, this.height, 0xCC0A0A0F);

        float mainX = this.width / 2f - 120f;
        float mainY = this.height / 2f - 80f;
        float mainWidth = 240f;
        float mainHeight = 140f;

        GatoperaPipelines.drawWindowBackground(
                context.getMatrices(),
                mainX,
                mainY,
                mainWidth,
                mainHeight,
                new Color(18, 18, 24, 140)
        );

        drawText(context, header, mainX + mainWidth / 2f, mainY + 12, 0xFFFFFFFF);
        drawText(context, description, mainX + mainWidth / 2f, mainY + 32, 0xFFAAAAAA);

        boolean yesHovered = yesHovered(mouseX, mouseY);
        boolean noHovered = noHovered(mouseX, mouseY);

        Color base = new Color(32, 32, 40, 220);
        Color hover = new Color(55, 55, 70, 240);

        GatoperaPipelines.drawButton(
                context.getMatrices(),
                mainX + 5,
                mainY + 95,
                110,
                40,
                yesHovered,
                base,
                hover
        );

        GatoperaPipelines.drawButton(
                context.getMatrices(),
                mainX + 125,
                mainY + 95,
                110,
                40,
                noHovered,
                base,
                hover
        );

        drawText(
                context,
                yesText,
                mainX + 60,
                mainY + 110,
                yesHovered ? 0xFFFFFFFF : 0xFFCCCCCC
        );

        drawText(
                context,
                noText,
                mainX + 180,
                mainY + 110,
                noHovered ? 0xFFFFFFFF : 0xFFCCCCCC
        );

        if (pic != null) {
            context.drawTexture(
                    pic,
                    (int) (mainX + mainWidth / 2f - 35f),
                    (int) mainY + 42,
                    0,
                    0,
                    70,
                    45,
                    70,
                    45
            );
        }
    }

    private void drawText(DrawContext context, String text, float x, float y, int color) {
        boolean useCustom = FontRenderers.ui != null
                && FontRenderers.ui.getClass().getSimpleName().equals("StbFontAdapter");

        if (useCustom) {
            try {
                FontRenderers.ui.drawCenteredString(
                        context.getMatrices(),
                        text,
                        x,
                        y,
                        color
                );
                return;
            } catch (Throwable ignored) {
            }
        }

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.of(text),
                (int) x,
                (int) y,
                color
        );
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x
                && mouseX <= x + width
                && mouseY >= y
                && mouseY <= y + height;
    }

    private boolean yesHovered(int mouseX, int mouseY) {
        float mainX = this.width / 2f - 120f;
        float mainY = this.height / 2f - 80f;
        return isHovered(mouseX, mouseY, (int) mainX + 5, (int) mainY + 95, 110, 40);
    }

    private boolean noHovered(int mouseX, int mouseY) {
        float mainX = this.width / 2f - 120f;
        float mainY = this.height / 2f - 80f;
        return isHovered(mouseX, mouseY, (int) mainX + 125, (int) mainY + 95, 110, 40);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (yesHovered((int) mouseX, (int) mouseY)) {
                yesAction.run();
                return true;
            }
            if (noHovered((int) mouseX, (int) mouseY)) {
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