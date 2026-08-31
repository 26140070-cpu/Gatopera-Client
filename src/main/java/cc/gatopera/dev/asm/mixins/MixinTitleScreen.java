package cc.gatopera.dev.asm.mixins;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.i18n.I18n;
import cc.gatopera.dev.api.i18n.Language;
import cc.gatopera.dev.mod.gui.misc.DialogScreen;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.vidtu.ias.screen.AccountScreen;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

    public MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        if (Gatopera.CONFIG == null) return;
        if (Gatopera.CONFIG.getBoolean("welcome_shown", false)) return;

        DialogScreen languageDialog = new DialogScreen(
                null,
                I18n.t("dialog.language.header"),
                I18n.t("dialog.language.description"),
                I18n.t("dialog.language.yes"),
                I18n.t("dialog.language.no"),
                () -> {
                    I18n.setLanguage(Language.EN_US);
                    if (ClientSetting.INSTANCE != null) {
                        ClientSetting.INSTANCE.language.setEnumValue("EN_US");
                    }
                    client.setScreen(buildWelcome());
                },
                () -> {
                    I18n.setLanguage(Language.ES_MX);
                    if (ClientSetting.INSTANCE != null) {
                        ClientSetting.INSTANCE.language.setEnumValue("ES_MX");
                    }
                    client.setScreen(buildWelcome());
                }
        );
        client.setScreen(languageDialog);
    }

    private DialogScreen buildWelcome() {
        return new DialogScreen(
                null,
                I18n.t("dialog.welcome.header"),
                I18n.t("dialog.welcome.description"),
                I18n.t("dialog.welcome.yes"),
                I18n.t("dialog.welcome.no"),
                () -> {
                    Gatopera.CONFIG.settingsPut("welcome_shown", "true");
                    Gatopera.save();
                    client.setScreen(this);
                },
                () -> {
                    Gatopera.CONFIG.settingsPut("welcome_shown", "true");
                    Gatopera.save();
                    client.stop();
                }
        );
    }

    @Inject(method = "initWidgetsNormal", at = @At(
            target = "Lnet/minecraft/client/gui/screen/TitleScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;",
            value = "INVOKE", shift = At.Shift.AFTER, ordinal = 1), cancellable = true)
    public void hookInit(int y, int spacingY, CallbackInfo ci) {
        ci.cancel();
        final ButtonWidget widget = ButtonWidget.builder(Text.of(I18n.t("gui.account_manager")), (action) -> client.setScreen(new AccountScreen(this)))
                .dimensions(this.width / 2 + 2, y + spacingY * 2, 98, 20)
                .tooltip(Tooltip.of(Text.of(I18n.t("gui.account_manager.tooltip"))))
                .build();
        widget.active = true;
        addDrawableChild(widget);
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("menu.online"), button -> this.switchToRealms())
                        .dimensions(this.width / 2 - 100, y + spacingY * 2, 98, 20)
                        .build()
        ).active = true;
    }

    @Shadow
    private void switchToRealms() {
    }
}