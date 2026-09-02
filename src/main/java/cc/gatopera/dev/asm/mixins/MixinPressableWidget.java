package cc.gatopera.dev.asm.mixins;

import cc.gatopera.dev.api.utils.math.Animation;
import cc.gatopera.dev.api.utils.math.Easing;
import cc.gatopera.dev.api.utils.render.ColorUtil;
import cc.gatopera.dev.api.utils.render.GatoperaPipelines;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(PressableWidget.class)
public abstract class MixinPressableWidget
        extends ClickableWidget {

    public MixinPressableWidget(
            int x,
            int y,
            int width,
            int height,
            Text message
    ) {
        super(
                x,
                y,
                width,
                height,
                message
        );
    }

    @Unique
    private Animation animation =
            new Animation();

    @Unique
    private double progress = 0;

    @Inject(
            method = "renderWidget",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onRender(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {
        if (!ClientSetting.INSTANCE.customButton.booleanValue) {
            return;
        }

        ci.cancel();

        if (this.isSelected()) {
            progress = animation.get(
                    1,
                    (long) ClientSetting.INSTANCE.speed.getValue(),
                    Easing.Linear
            );
        } else {
            progress = animation.get(
                    0,
                    (long) ClientSetting.INSTANCE.speed.getValue(),
                    Easing.Linear
            );
        }

        MinecraftClient minecraftClient =
                MinecraftClient.getInstance();

        Color base =
                ColorUtil.fadeColor(
                        ClientSetting.INSTANCE.customButton.getValue(),
                        ClientSetting.INSTANCE.hover.getValue(),
                        progress
                );

        Color hover =
                ColorUtil.fadeColor(
                        ClientSetting.INSTANCE.hover.getValue(),
                        ClientSetting.INSTANCE.customButton.getValue(),
                        progress
                );

        GatoperaPipelines.drawButton(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                this.isHovered(),
                base,
                hover
        );

        int color =
                this.active
                        ? 16777215
                        : 10526880;

        this.drawMessage(
                context,
                minecraftClient.textRenderer,
                color
                        | MathHelper.ceil(
                        this.alpha * 255.0F
                ) << 24
        );
    }

    @Shadow
    public void drawMessage(
            DrawContext context,
            TextRenderer textRenderer,
            int color
    ) {
    }
}