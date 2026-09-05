package cc.gatopera.dev.mod.gui.clickgui;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.i18n.I18n;
import cc.gatopera.dev.api.utils.math.FadeUtils;
import cc.gatopera.dev.api.utils.render.Render2DUtil;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;
import cc.gatopera.dev.mod.modules.impl.client.ClickGui;
import cc.gatopera.dev.mod.modules.settings.Setting;
import cc.gatopera.dev.mod.modules.settings.impl.BindSetting;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.ColorSetting;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import cc.gatopera.dev.mod.modules.settings.impl.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ClickGuiScreen extends Screen {
    private Color panel = new Color(18, 21, 29, 248);
    private Color rail = new Color(25, 29, 39, 250);
    private Color surface = new Color(32, 37, 49, 245);
    private Color surfaceHover = new Color(43, 49, 64, 250);
    private Color muted = new Color(164, 172, 190);
    private Color primary = new Color(105, 142, 255);
    private Color primaryDark = new Color(62, 91, 180);
    private Color text = Color.WHITE;
    private Color overlay = new Color(0, 0, 0, 105);

    public static boolean clicked;
    public static boolean rightClicked;
    public static boolean hoverClicked;

    private enum Page {
        HOME("Home"), MODULES("Modules"), SETTINGS("Settings"), CLIENT("Client"),
        PROFILES("Profiles"), MUSIC("Music");

        private final String title;

        Page(String title) {
            this.title = title;
        }
    }

    private Page page = Page.HOME;
    private Module selectedModule;
    private String search = "";
    private boolean searchFocused;
    private Setting draggingSlider;
    private double scroll;
    private double panelX;
    private double panelY;
    private double panelWidth;
    private double panelHeight;
    private final FadeUtils openAnimation = new FadeUtils(300);
    private final FadeUtils pageAnimation = new FadeUtils(220);

    public ClickGuiScreen() {
        super(Text.of("Gatopera"));
    }

    @Override
    protected void init() {
        page = Page.HOME;
        selectedModule = null;
        search = "";
        searchFocused = false;
        draggingSlider = null;
        scroll = 0;
        openAnimation.setLength(Math.max(120, ClickGui.INSTANCE.animationTime.getValueInt()));
        openAnimation.reset();
        pageAnimation.reset();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float tickDelta) {
        updateTheme();
        context.fill(0, 0, width, height, overlay.getRGB());

        double scale = ClickGui.INSTANCE.guiScale.getValue() / 100.0;
        panelWidth = Math.min(1120 * scale, Math.max(620 * scale, width - 32));
        panelHeight = Math.min(720 * scale, Math.max(400 * scale, height - 32));
        panelX = (width - panelWidth) / 2 + (1 - Math.min(1, pageAnimation.easeCubicOut())) * 8;
        panelY = (height - panelHeight) / 2 + (1 - Math.min(1, openAnimation.easeCubicOut())) * 20;

        Render2DUtil.drawRound(context.getMatrices(), (float) panelX, (float) panelY,
                (float) panelWidth, (float) panelHeight, 24, panel);
        Render2DUtil.drawRound(context.getMatrices(), (float) panelX, (float) panelY,
                (float) (176 * scale), (float) panelHeight, 24, rail);

        drawRail(context, mouseX, mouseY);
        if (selectedModule != null) {
            drawModuleSettings(context, mouseX, mouseY);
        } else {
            switch (page) {
                case HOME -> drawHome(context);
                case MODULES -> drawModules(context, mouseX, mouseY, false);
                case SETTINGS -> drawModules(context, mouseX, mouseY, true);
                case CLIENT -> drawClientSettings(context, mouseX, mouseY);
                case PROFILES -> drawUnavailable(context, I18n.t("gui.profiles_unavailable"),
                        I18n.t("gui.profiles_description"));
                case MUSIC -> drawUnavailable(context, I18n.t("gui.music_unavailable"),
                        I18n.t("gui.music_description"));
            }
        }
    }

    private void drawRail(DrawContext context, int mouseX, int mouseY) {
        drawText(context, "Gatopera", panelX + 24, panelY + 28, text, true);
        drawText(context, I18n.t("gui.mod_menu"), panelX + 24, panelY + 50, muted, false);

        double y = panelY + 82;
        for (Page item : Page.values()) {
            boolean active = page == item && selectedModule == null;
            boolean hovered = inside(mouseX, mouseY, panelX + 14, y, 148, 36);
            if (active || hovered) {
                Render2DUtil.drawRound(context.getMatrices(), (float) panelX + 14, (float) y,
                        148, 36, 10, active ? primaryDark : surfaceHover);
            }
            drawText(context, I18n.t("gui." + item.name().toLowerCase(Locale.ROOT)),
                    panelX + 30, y + 12, active ? text : muted, false);
            y += 44;
        }

        drawText(context, I18n.t("gui.fabric_version"), panelX + 24, panelY + panelHeight - 28, muted, false);
    }

    private void drawHome(DrawContext context) {
        double x = contentX();
        double y = panelY + 52;
        drawText(context, I18n.t("gui.welcome"), x + 28, y + 30, text, true);
        drawText(context, I18n.t("gui.welcome.description"), x + 28, y + 54, muted, false);

        card(context, x + 28, y + 92, panelWidth - 232, 126, false);
        drawText(context, I18n.t("gui.quick_start"), x + 48, y + 122, text, true);
        drawText(context, I18n.t("gui.open_modules"), x + 48, y + 150, muted, false);
        drawText(context, I18n.t("gui.open_settings"), x + 48, y + 174, muted, false);
        drawText(context, I18n.t("gui.client_settings"), x + 48, y + 198, muted, false);

        card(context, x + 28, y + 238, panelWidth - 232, 92, false);
        drawText(context, I18n.t("gui.loaded_modules"), x + 48, y + 268, text, true);
        drawText(context, String.valueOf(Gatopera.MODULE.modules.size()), x + 48, y + 298,
                primary, true);
        drawText(context, I18n.t("gui.use_navigation"), x + 115, y + 298, muted, false);
    }

    private void drawModules(DrawContext context, int mouseX, int mouseY, boolean settingsOnly) {
        drawHeader(context, settingsOnly ? I18n.t("gui.settings") : I18n.t("gui.modules"),
                settingsOnly ? I18n.t("gui.configuration") : I18n.t("gui.toggle_modules"));
        drawSearch(context, mouseX, mouseY);

        List<Module> modules = new ArrayList<>();
        for (Module module : Gatopera.MODULE.modules) {
            if (settingsOnly && module.getCategory() != Module.Category.Client) {
                continue;
            }

            if (!settingsOnly && !matches(module.getDisplayName() + " " + module.getDescription())) {
                continue;
            }
            if (settingsOnly && !matches(module.getDisplayName() + " " + module.getDescription())) {
                continue;
            }
            if (settingsOnly && !module.hasSettings()) {
                continue;
            }
            modules.add(module);
        }
        modules.sort(Comparator.comparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER));

        double x = contentX() + 28;
        double y = panelY + 112 + scroll;
        double cardWidth = 205;
        double cardHeight = 78;
        double gap = 12;
        int columns = Math.max(1, (int) ((panelWidth - 232 - 56 + gap) / (cardWidth + gap)));

        context.enableScissor((int) x - 4, (int) (panelY + 104),
                (int) (panelX + panelWidth - 22), (int) (panelY + panelHeight - 18));
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int column = i % columns;
            int row = i / columns;
            double cardX = x + column * (cardWidth + gap);
            double cardY = y + row * (cardHeight + gap);
            boolean hovered = inside(mouseX, mouseY, cardX, cardY, cardWidth, cardHeight);
            card(context, cardX, cardY, cardWidth, cardHeight, hovered);
            drawText(context, module.getDisplayName(), cardX + 14, cardY + 22,
                    module.isOn() ? text : muted, true);
            drawText(context, module.isOn() ? I18n.t("state.on") : I18n.t("state.off"), cardX + 14, cardY + 46,
                    module.isOn() ? primary : muted, false);
            drawText(context, I18n.category(module.getCategory().name()), cardX + cardWidth - 66, cardY + 46,
                    muted, false);
        }
        context.disableScissor();

        if (modules.isEmpty()) {
            drawText(context, I18n.t("gui.no_matches"), x, panelY + 142, muted, false);
        }
    }

    private void drawClientSettings(DrawContext context, int mouseX, int mouseY) {
        drawHeader(context, I18n.t("gui.client"), I18n.t("gui.client_description"));
        double x = contentX() + 28;
        double y = panelY + 92;
        double w = panelWidth - 232 - 56;
        drawClientSetting(context, I18n.t("setting.Language"),
                I18n.value(ClientSetting.INSTANCE.language.getValue().name()), x, y, w,
                inside(mouseX, mouseY, x, y, w, 52));
        drawClientSetting(context, I18n.t("setting.GuiScale"),
                String.format(Locale.ROOT, "%.0f%%", ClickGui.INSTANCE.guiScale.getValue()), x, y + 64, w,
                inside(mouseX, mouseY, x, y + 64, w, 52));
        drawClientSetting(context, I18n.t("setting.Theme"),
                I18n.value(ClickGui.INSTANCE.theme.getValue().name()), x, y + 128, w,
                inside(mouseX, mouseY, x, y + 128, w, 52));
    }

    private void drawClientSetting(DrawContext context, String title, String value,
                                   double x, double y, double w, boolean hovered) {
        card(context, x, y, w, 52, hovered);
        drawText(context, title, x + 16, y + 16, text, false);
        drawText(context, value, x + w - 16 - textWidth(value), y + 16, primary, false);
    }

    private void drawModuleSettings(DrawContext context, int mouseX, int mouseY) {
        drawHeader(context, selectedModule.getDisplayName(), selectedModule.getDescription());
        double x = contentX() + 28;
        double backY = panelY + 52;
        if (inside(mouseX, mouseY, x, backY, 74, 28)) {
            Render2DUtil.drawRound(context.getMatrices(), (float) x, (float) backY, 74, 28, 8, surfaceHover);
        }
        drawText(context, "< Back", x + 12, backY + 9, Color.WHITE, false);

        double toggleX = panelX + panelWidth - 150;
        card(context, toggleX, backY, 112, 28, inside(mouseX, mouseY, toggleX, backY, 112, 28));
        drawText(context, selectedModule.isOn() ? I18n.t("gui.enabled") : I18n.t("gui.disabled"),
                toggleX + 14, backY + 9, selectedModule.isOn() ? primary : muted, false);

        double y = panelY + 104 + scroll;
        context.enableScissor((int) x - 4, (int) (panelY + 96),
                (int) (panelX + panelWidth - 22), (int) (panelY + panelHeight - 18));
        for (Setting setting : selectedModule.getSettings()) {
            if (!visible(setting)) {
                continue;
            }
            drawSetting(context, setting, x, y, panelWidth - 232 - 56, mouseX, mouseY);
            y += 64;
        }
        context.disableScissor();
    }

    private void drawSetting(DrawContext context, Setting setting, double x, double y,
                             double w, int mouseX, int mouseY) {
        boolean hovered = inside(mouseX, mouseY, x, y, w, 52);
        card(context, x, y, w, 52, hovered);
        drawText(context, setting.getDisplayName(), x + 16, y + 16, text, false);
        String value = settingValue(setting);
        if (setting instanceof SliderSetting slider) {
            double ratio = (slider.getValue() - slider.getMinimum()) / slider.getRange();
            Render2DUtil.drawRound(context.getMatrices(), (float) x + 170, (float) y + 35,
                    (float) (w - 220), 5, 3, new Color(55, 61, 77));
            Render2DUtil.drawRound(context.getMatrices(), (float) x + 170, (float) y + 35,
                    (float) ((w - 220) * Math.max(0, Math.min(1, ratio))), 5, 3, primary);
        }
        drawText(context, value, x + w - 16 - textWidth(value), y + 17, primary, false);
    }

    private void drawUnavailable(DrawContext context, String title, String description) {
        double x = contentX() + 28;
        double y = panelY + panelHeight / 2 - 30;
        drawText(context, title, x, y, text, true);
        drawText(context, description, x, y + 28, muted, false);
    }

    private void drawHeader(DrawContext context, String title, String subtitle) {
        double x = contentX() + 28;
        drawText(context, title, x, panelY + 28, text, true);
        drawText(context, subtitle == null ? "" : subtitle, x, panelY + 50, muted, false);
    }

    private void drawSearch(DrawContext context, int mouseX, int mouseY) {
        double x = panelX + panelWidth - 290;
        double y = panelY + 24;
        card(context, x, y, 230, 30, searchFocused || inside(mouseX, mouseY, x, y, 230, 30));
        drawText(context, search.isEmpty() ? I18n.t("gui.search") : search, x + 12, y + 10,
                search.isEmpty() ? muted : text, false);
    }

    private void card(DrawContext context, double x, double y, double w, double h, boolean hovered) {
        Render2DUtil.drawRound(context.getMatrices(), (float) x, (float) y, (float) w, (float) h,
                12, hovered ? surfaceHover : surface);
    }

    private void drawText(DrawContext context, String value, double x, double y, Color color, boolean shadow) {
        if (shadow) {
            context.drawTextWithShadow(textRenderer, value, (int) x, (int) y, color.getRGB());
        } else {
            context.drawText(textRenderer, value, (int) x, (int) y, color.getRGB(), false);
        }
    }

    private int textWidth(String value) {
        return textRenderer.getWidth(value);
    }

    private double contentX() {
        return panelX + 176;
    }

    private boolean visible(Setting setting) {
        return !setting.hide && (setting.visibility == null || setting.visibility.getAsBoolean());
    }

    private String settingValue(Setting setting) {
        if (setting instanceof BooleanSetting value) {
            return value.getValue() ? I18n.t("state.on") : I18n.t("state.off");
        }
        if (setting instanceof SliderSetting value) {
            return String.format(Locale.ROOT, "%.2f%s", value.getValue(), value.getSuffix());
        }
        if (setting instanceof EnumSetting<?> value) {
            return I18n.value(value.getValue().name());
        }
        if (setting instanceof BindSetting value) {
            return value.isListening() ? I18n.t("gui.press_key") : value.getBind();
        }
        if (setting instanceof ColorSetting value) {
            String color = value.isRainbow ? I18n.t("gui.rainbow") : String.format("#%08X", value.getValue().getRGB());
            return value.injectBoolean ? color + (value.booleanValue ? " / On" : " / Off") : color;
        }
        if (setting instanceof StringSetting value) {
            return value.isListening() ? value.getValue() + "|" : value.getValue();
        }
        return "";
    }

    private boolean matches(String value) {
        return search.isEmpty() || value.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT));
    }

    private boolean inside(double mouseX, double mouseY, double x, double y, double w, double h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    private void selectPage(Page next) {
        page = next;
        selectedModule = null;
        scroll = 0;
        search = "";
        searchFocused = false;
        pageAnimation.reset();
    }

    private void updateTheme() {
        switch (ClickGui.INSTANCE.theme.getValue()) {
            case Light -> {
                panel = new Color(238, 241, 248, 250);
                rail = new Color(224, 229, 239, 250);
                surface = new Color(255, 255, 255, 245);
                surfaceHover = new Color(229, 235, 247, 250);
                muted = new Color(82, 91, 111);
                primary = new Color(50, 94, 210);
                primaryDark = new Color(38, 73, 164);
                text = new Color(20, 25, 35);
                overlay = new Color(20, 25, 35, 70);
            }
            case Dark -> {
                panel = new Color(27, 29, 34, 250);
                rail = new Color(35, 38, 45, 250);
                surface = new Color(45, 48, 57, 245);
                surfaceHover = new Color(59, 64, 76, 250);
                muted = new Color(180, 184, 195);
                primary = new Color(125, 155, 255);
                primaryDark = new Color(78, 106, 194);
                text = Color.WHITE;
                overlay = new Color(0, 0, 0, 105);
            }
            case Midnight -> {
                panel = new Color(18, 21, 29, 248);
                rail = new Color(25, 29, 39, 250);
                surface = new Color(32, 37, 49, 245);
                surfaceHover = new Color(43, 49, 64, 250);
                muted = new Color(164, 172, 190);
                primary = new Color(105, 142, 255);
                primaryDark = new Color(62, 91, 180);
                text = Color.WHITE;
                overlay = new Color(0, 0, 0, 105);
            }
            case Catppuccin -> {
                panel = new Color(30, 30, 46, 250);
                rail = new Color(24, 24, 37, 250);
                surface = new Color(49, 50, 68, 245);
                surfaceHover = new Color(69, 70, 94, 250);
                muted = new Color(166, 173, 200);
                primary = new Color(203, 166, 247);
                primaryDark = new Color(136, 57, 239);
                text = new Color(205, 214, 244);
                overlay = new Color(17, 17, 27, 105);
            }
            case Pink -> {
                panel = new Color(49, 25, 38, 250);
                rail = new Color(67, 31, 49, 250);
                surface = new Color(83, 40, 61, 245);
                surfaceHover = new Color(112, 51, 79, 250);
                muted = new Color(232, 174, 199);
                primary = new Color(255, 105, 170);
                primaryDark = new Color(190, 54, 116);
                text = Color.WHITE;
                overlay = new Color(35, 8, 22, 105);
            }
            case Purple -> {
                panel = new Color(34, 25, 52, 250);
                rail = new Color(45, 31, 69, 250);
                surface = new Color(61, 42, 91, 245);
                surfaceHover = new Color(84, 57, 124, 250);
                muted = new Color(204, 183, 235);
                primary = new Color(181, 125, 255);
                primaryDark = new Color(119, 69, 186);
                text = Color.WHITE;
                overlay = new Color(22, 10, 38, 105);
            }
            case Blue -> {
                panel = new Color(20, 34, 58, 250);
                rail = new Color(25, 47, 79, 250);
                surface = new Color(32, 64, 103, 245);
                surfaceHover = new Color(42, 86, 137, 250);
                muted = new Color(170, 205, 238);
                primary = new Color(77, 174, 255);
                primaryDark = new Color(35, 108, 181);
                text = Color.WHITE;
                overlay = new Color(5, 18, 38, 105);
            }
            case Green -> {
                panel = new Color(20, 47, 39, 250);
                rail = new Color(25, 65, 51, 250);
                surface = new Color(33, 82, 62, 245);
                surfaceHover = new Color(44, 111, 81, 250);
                muted = new Color(171, 222, 193);
                primary = new Color(77, 220, 151);
                primaryDark = new Color(30, 145, 94);
                text = Color.WHITE;
                overlay = new Color(5, 30, 20, 105);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clicked = false;
        rightClicked = false;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (selectedModule != null) {
                handleSettingsClick(mouseX, mouseY, button);
            } else {
                handlePageClick(mouseX, mouseY, button);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handlePageClick(double mouseX, double mouseY, int button) {
        double navY = panelY + 82;
        for (Page item : Page.values()) {
            if (inside(mouseX, mouseY, panelX + 14, navY, 148, 36)) {
                selectPage(item);
                return;
            }
            navY += 44;
        }
        if (page == Page.CLIENT) {
            double x = contentX() + 28;
            double y = panelY + 92;
            double w = panelWidth - 232 - 56;
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && inside(mouseX, mouseY, x, y, w, 52)) {
                ClientSetting.INSTANCE.language.increaseEnum();
                I18n.setLanguage(ClientSetting.INSTANCE.language.getValue());
                return;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && inside(mouseX, mouseY, x, y + 64, w, 52)) {
                updateClientScale(mouseX, x, w);
                return;
            }
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && inside(mouseX, mouseY, x, y + 128, w, 52)) {
                ClickGui.INSTANCE.theme.increaseEnum();
                return;
            }
            return;
        }
        if ((page == Page.MODULES || page == Page.SETTINGS) &&
                inside(mouseX, mouseY, panelX + panelWidth - 290, panelY + 24, 230, 30)) {
            searchFocused = true;
            return;
        }
        if (page == Page.MODULES || page == Page.SETTINGS) {
            searchFocused = false;
            List<Module> modules = filteredModules(page == Page.SETTINGS);
            double x = contentX() + 28;
            double y = panelY + 112 + scroll;
            double cardWidth = 205;
            double cardHeight = 78;
            double gap = 12;
            int columns = Math.max(1, (int) ((panelWidth - 232 - 56 + gap) / (cardWidth + gap)));
            for (int i = 0; i < modules.size(); i++) {
                double cardX = x + (i % columns) * (cardWidth + gap);
                double cardY = y + (i / columns) * (cardHeight + gap);
                if (inside(mouseX, mouseY, cardX, cardY, cardWidth, cardHeight)) {
                    if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                        selectedModule = modules.get(i);
                        scroll = 0;
                    } else {
                        modules.get(i).toggle();
                    }
                    return;
                }
            }
        }
    }

    private List<Module> filteredModules(boolean settingsOnly) {
        List<Module> modules = new ArrayList<>();
        for (Module module : Gatopera.MODULE.modules) {
            if (settingsOnly && module.getCategory() != Module.Category.Client) continue;
            if (settingsOnly && !module.hasSettings()) continue;
            if (!matches(module.getDisplayName() + " " + module.getDescription())) continue;
            modules.add(module);
        }
        modules.sort(Comparator.comparing(Module::getDisplayName, String.CASE_INSENSITIVE_ORDER));
        return modules;
    }

    private void handleSettingsClick(double mouseX, double mouseY, int button) {
        double x = contentX() + 28;
        double backY = panelY + 52;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && inside(mouseX, mouseY, x, backY, 74, 28)) {
            selectedModule = null;
            scroll = 0;
            return;
        }
        double toggleX = panelX + panelWidth - 150;
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && inside(mouseX, mouseY, toggleX, backY, 112, 28)) {
            selectedModule.toggle();
            return;
        }
        double y = panelY + 104 + scroll;
        double w = panelWidth - 232 - 56;
        for (Setting setting : selectedModule.getSettings()) {
            if (!visible(setting)) continue;
            if (inside(mouseX, mouseY, x, y, w, 52)) {
                if (setting instanceof BooleanSetting value) {
                    value.toggleValue();
                } else if (setting instanceof EnumSetting<?> value) {
                    value.increaseEnum();
                } else if (setting instanceof SliderSetting value) {
                    draggingSlider = value;
                    updateSlider(value, mouseX, x, w);
                } else if (setting instanceof StringSetting value) {
                    value.setListening(true);
                } else if (setting instanceof BindSetting value) {
                    value.setListening(true);
                } else if (setting instanceof ColorSetting value) {
                    if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                        value.setRainbow(!value.isRainbow);
                    } else if (value.injectBoolean) {
                        value.booleanValue = !value.booleanValue;
                    } else {
                        float[] hsb = Color.RGBtoHSB(value.getValue().getRed(),
                                value.getValue().getGreen(), value.getValue().getBlue(), null);
                        Color next = Color.getHSBColor((hsb[0] + 0.08f) % 1.0f, Math.max(0.35f, hsb[1]),
                                Math.max(0.65f, hsb[2]));
                        value.setValue(new Color(next.getRed(), next.getGreen(), next.getBlue(),
                                value.getValue().getAlpha()));
                    }
                }
                return;
            }
            y += 64;
        }
    }

    private void updateSlider(SliderSetting setting, double mouseX, double x, double w) {
        double ratio = Math.max(0, Math.min(1, (mouseX - (x + 170)) / (w - 220)));
        setting.setValue(setting.getMinimum() + ratio * setting.getRange());
    }

    private void updateClientScale(double mouseX, double x, double w) {
        double ratio = Math.max(0, Math.min(1, (mouseX - x) / w));
        ClickGui.INSTANCE.guiScale.setValue(70 + ratio * 60);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingSlider instanceof SliderSetting slider) {
            updateSlider(slider, mouseX, contentX() + 28, panelWidth - 232 - 56);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingSlider = null;
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (selectedModule != null || page == Page.MODULES || page == Page.SETTINGS) {
            scroll = Math.max(-1200, Math.min(0, scroll + verticalAmount * 28));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (searchFocused && chr >= 32) {
            search += chr;
            return true;
        }
        if (selectedModule != null) {
            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof StringSetting value && value.isListening()) {
                    value.charType(chr);
                    return true;
                }
                if (setting instanceof SliderSetting value && value.isListening()) {
                    value.charType(chr);
                    return true;
                }
            }
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (selectedModule != null) {
                selectedModule = null;
                return true;
            }
            close();
            return true;
        }
        if (searchFocused) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !search.isEmpty()) {
                search = search.substring(0, search.length() - 1);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                searchFocused = false;
                return true;
            }
        }
        if (selectedModule != null) {
            for (Setting setting : selectedModule.getSettings()) {
                if (setting instanceof StringSetting value && value.isListening()) {
                    value.keyType(keyCode);
                    return true;
                }
                if (setting instanceof SliderSetting value && value.isListening()) {
                    value.keyType(keyCode);
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        Gatopera.CONFIG.saveSettings();
        clicked = false;
        rightClicked = false;
        hoverClicked = false;
        super.close();
    }
}
