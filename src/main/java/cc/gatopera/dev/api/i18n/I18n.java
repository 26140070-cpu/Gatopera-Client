package cc.gatopera.dev.api.i18n;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;

import java.util.HashMap;
import java.util.Map;

public final class I18n {
    private static final Map<Language, Map<String, String>> TABLES = new HashMap<>();
    private static Language current = Language.EN_US;

    static {
        Map<String, String> en = new HashMap<>();
        en.put("dialog.welcome.header", "Thank you for downloading Gatopera!");
        en.put("dialog.welcome.description", "ClickGui opens with the key - Y");
        en.put("dialog.welcome.yes", "Continue");
        en.put("dialog.welcome.no", "Quit Game");
        en.put("dialog.language.header", "Hello!");
        en.put("dialog.language.description", "What is your language?");
        en.put("dialog.language.yes", "English");
        en.put("dialog.language.no", "Espanol");
        en.put("setting.language", "Language");
        en.put("setting.gui_blur", "GuiBlur");
        en.put("setting.gui_rounded", "GuiRounded");
        en.put("setting.gui_radius", "CornerRadius");
        en.put("gui.account_manager", "Account Manager");
        en.put("gui.account_manager.tooltip", "Allows you to switch your in-game account");
        TABLES.put(Language.EN_US, en);

        Map<String, String> es = new HashMap<>();
        es.put("dialog.welcome.header", "Gracias por descargar Gatopera!");
        es.put("dialog.welcome.description", "El ClickGui se abre con la tecla - Y");
        es.put("dialog.welcome.yes", "Continuar");
        es.put("dialog.welcome.no", "Salir del juego");
        es.put("dialog.language.header", "Hola!");
        es.put("dialog.language.description", "Cual es tu idioma?");
        es.put("dialog.language.yes", "English");
        es.put("dialog.language.no", "Espanol");
        es.put("setting.language", "Idioma");
        es.put("setting.gui_blur", "DesenfoqueGUI");
        es.put("setting.gui_rounded", "BordesRedondeados");
        es.put("setting.gui_radius", "RadioEsquinas");
        es.put("gui.account_manager", "Gestor de cuentas");
        es.put("gui.account_manager.tooltip", "Te permite cambiar tu cuenta del juego");
        TABLES.put(Language.ES_MX, es);
    }

    private I18n() {
    }

    public static void setLanguage(Language language) {
        if (language == null) language = Language.EN_US;
        current = language;
        if (Gatopera.CONFIG != null) {
            Gatopera.CONFIG.settingsPut("language", language.getCode());
        }
    }

    public static Language getLanguage() {
        return current;
    }

    public static void loadFromConfig() {
        if (Gatopera.CONFIG == null) return;
        current = Language.fromCode(Gatopera.CONFIG.getString("language", Language.EN_US.getCode()));
    }

    public static String t(String key) {
        Map<String, String> table = TABLES.getOrDefault(current, TABLES.get(Language.EN_US));
        String value = table.get(key);
        if (value != null) return value;
        Map<String, String> fallback = TABLES.get(Language.EN_US);
        return fallback.getOrDefault(key, key);
    }

    public static String t(String key, Object... args) {
        return String.format(t(key), args);
    }

    public static void syncFromClientSetting() {
        if (ClientSetting.INSTANCE != null && ClientSetting.INSTANCE.language != null) {
            setLanguage(ClientSetting.INSTANCE.language.getValue());
        }
    }
}