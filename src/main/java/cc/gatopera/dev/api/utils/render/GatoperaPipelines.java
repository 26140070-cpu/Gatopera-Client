package cc.gatopera.dev.api.utils.render;

import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.Color;

public final class GatoperaPipelines {
    private GatoperaPipelines() {
    }

    public static boolean isRoundedEnabled() {
        return ClientSetting.INSTANCE == null
                || ClientSetting.INSTANCE.guiRounded.getValue();
    }

    public static float getRadius() {
        if (ClientSetting.INSTANCE == null) {
            return 12f;
        }

        return ClientSetting.INSTANCE.guiRadius.getValueFloat();
    }

    private static void drawPanelInternal(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius,
            Color color
    ) {
        if (radius > 0.5f) {
            Render2DUtil.drawRound(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    Math.min(radius, Math.min(width, height) / 2f),
                    color
            );
        } else {
            Render2DUtil.drawRect(
                    matrices,
                    x,
                    y,
                    width,
                    height,
                    color
            );
        }
    }

    public static void drawWindowBackground(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            Color color
    ) {
        float radius = isRoundedEnabled() ? getRadius() : 0f;

        Color panel = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.min(160, color.getAlpha())
        );

        drawPanelInternal(
                matrices,
                x,
                y,
                width,
                height,
                radius,
                panel
        );
    }

    public static void drawPanel(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            float radius,
            Color color
    ) {
        float finalRadius = isRoundedEnabled() ? radius : 0f;

        drawPanelInternal(
                matrices,
                x,
                y,
                width,
                height,
                finalRadius,
                color
        );
    }

    public static void drawButton(
            MatrixStack matrices,
            float x,
            float y,
            float width,
            float height,
            boolean hovered,
            Color base,
            Color hover
    ) {
        float radius = isRoundedEnabled()
                ? Math.min(
                getRadius(),
                Math.min(width, height) / 2f
        )
                : 0f;

        drawPanelInternal(
                matrices,
                x,
                y,
                width,
                height,
                radius,
                hovered ? hover : base
        );
    }
}