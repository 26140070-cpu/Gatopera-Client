package cc.gatopera.dev.api.i18n;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.mod.modules.impl.client.ClientSetting;

import java.util.HashMap;
import java.util.Map;

public final class I18n {
    private static final Map<Language, Map<String, String>> TABLES = new HashMap<>();
    private static final Map<String, String> MODULE_ES = new HashMap<>();
    private static final Map<String, String> VALUE_ES = new HashMap<>();
    private static Language current = Language.EN_US;

    static {
        Map<String, String> en = new HashMap<>();
        Map<String, String> es = new HashMap<>();

        en.put("gui.category.combat", "Combat");
        en.put("gui.category.misc", "Misc");
        en.put("gui.category.render", "Render");
        en.put("gui.category.movement", "Movement");
        en.put("gui.category.player", "Player");
        en.put("gui.category.exploit", "Exploit");
        en.put("gui.category.client", "Client");

        es.put("gui.category.combat", "Combate");
        es.put("gui.category.misc", "Misceláneo");
        es.put("gui.category.render", "Renderizado");
        es.put("gui.category.movement", "Movimiento");
        es.put("gui.category.player", "Jugador");
        es.put("gui.category.exploit", "Exploit");
        es.put("gui.category.client", "Cliente");

        addSettings(en, false);
        addSettings(es, true);

        TABLES.put(Language.EN_US, en);
        TABLES.put(Language.ES_MX, es);

        MODULE_ES.put("ClickGui", "ClickGui");
        MODULE_ES.put("ClientSetting", "Ajustes del cliente");
        MODULE_ES.put("Colors", "Colores");
        MODULE_ES.put("FontSetting", "Configuración de fuente");
        MODULE_ES.put("ItemsCount", "Cantidad de objetos");
        MODULE_ES.put("ModuleList", "Lista de módulos");
        MODULE_ES.put("HUD", "HUD");
        MODULE_ES.put("AnchorAura", "Aura de anclas");
        MODULE_ES.put("AntiPiston", "Anti pistón");
        MODULE_ES.put("AntiRegear", "Anti reabastecimiento");
        MODULE_ES.put("AutoBurrow", "Entierro automático");
        MODULE_ES.put("AutoCev", "CEV automático");
        MODULE_ES.put("AutoCity", "AutoCity");
        MODULE_ES.put("AutoEXP", "Experiencia automática");
        MODULE_ES.put("AutoTotem", "Tótem automático");
        MODULE_ES.put("Criticals", "Críticos");
        MODULE_ES.put("HoleKick", "Expulsión de agujero");
        MODULE_ES.put("Quiver", "Quiver");
        MODULE_ES.put("Reach", "Alcance");
        MODULE_ES.put("SelfFill", "Relleno propio");
        MODULE_ES.put("SilentDouble", "Doble silencioso");
        MODULE_ES.put("TPAura", "Aura de teletransporte");
        MODULE_ES.put("WebAura", "Aura de telarañas");
        MODULE_ES.put("WebCleaner", "Limpiador de telarañas");
        MODULE_ES.put("Surround", "Protección envolvente");
        MODULE_ES.put("SelfTrap", "Autotrampa");
        MODULE_ES.put("AutoLadder", "Escalera automática");
        MODULE_ES.put("AutoHoleFill", "Relleno automático de agujeros");
        MODULE_ES.put("AutoAnchor", "Ancla automática");
        MODULE_ES.put("KillAura", "Aura de ataque");
        MODULE_ES.put("PistonCrystal", "Cristal de pistón");
        MODULE_ES.put("AutoCrystalBase", "Base de cristal automática");
        MODULE_ES.put("AntiCrawl", "Anti gateo");
        MODULE_ES.put("AutoCrystal", "Cristal automático");
        MODULE_ES.put("Burrow", "Entierro");
        MODULE_ES.put("AutoWeb", "Telaraña automática");
        MODULE_ES.put("Blocker", "Bloqueador");
        MODULE_ES.put("AutoTrap", "Trampa automática");
        MODULE_ES.put("BedAura", "Aura de camas");
        MODULE_ES.put("AutoPush", "Empuje automático");
        MODULE_ES.put("AntiHunger", "Anti hambre");
        MODULE_ES.put("Blink", "Blink");
        MODULE_ES.put("ChorusExploit", "Exploit de chorus");
        MODULE_ES.put("FakePearl", "Perla falsa");
        MODULE_ES.put("HitboxDesync", "Desincronización de hitbox");
        MODULE_ES.put("NoBadEffects", "Sin efectos negativos");
        MODULE_ES.put("PacketControl", "Control de paquetes");
        MODULE_ES.put("PearlSpoof", "Suplantación de perla");
        MODULE_ES.put("PingSpoof", "Suplantación de ping");
        MODULE_ES.put("PortalGod", "Dios de portales");
        MODULE_ES.put("RocketExtend", "Extensión de cohete");
        MODULE_ES.put("ServerLagger", "Lag del servidor");
        MODULE_ES.put("WallClip", "Atravesar paredes");
        MODULE_ES.put("XCarry", "XCarry");
        MODULE_ES.put("RaytraceBypass", "Bypass de raytrace");
        MODULE_ES.put("BowBomb", "BowBomb");
        MODULE_ES.put("PearlPhase", "Fase con perla");
        MODULE_ES.put("AntiBowBomb", "Anti BowBomb");
        MODULE_ES.put("NewChunks", "Nuevos chunks");
        MODULE_ES.put("AntiBookBan", "Anti bloqueo de libros");
        MODULE_ES.put("AutoEat", "Comer automáticamente");
        MODULE_ES.put("AutoQueue", "Cola automática");
        MODULE_ES.put("ChestStealer", "Robar cofres");
        MODULE_ES.put("NoSoundLag", "Sin lag de sonido");
        MODULE_ES.put("Nuker", "Nuker");
        MODULE_ES.put("PearlMark", "Marca de perla");
        MODULE_ES.put("Tips", "Consejos");
        MODULE_ES.put("TrueAttackCooldown", "Cooldown de ataque real");
        MODULE_ES.put("TrueDurability", "Durabilidad real");
        MODULE_ES.put("Spammer", "Spammer");
        MODULE_ES.put("ChatAppend", "Sufijo de chat");
        MODULE_ES.put("AutoReconnect", "Reconexión automática");
        MODULE_ES.put("PopCounter", "Contador de tótems");
        MODULE_ES.put("AutoEZ", "Burla automática");
        MODULE_ES.put("LavaFiller", "Relleno de lava automático");
        MODULE_ES.put("AddFriend", "Añadir amigo");

        VALUE_ES.put("General", "General");
        VALUE_ES.put("Color", "Color");
        VALUE_ES.put("Element", "Elemento");
        VALUE_ES.put("Game", "Juego");
        VALUE_ES.put("Gui", "GUI");
        VALUE_ES.put("Misc", "Misceláneo");
        VALUE_ES.put("Notification", "Notificaciones");
        VALUE_ES.put("ChatHud", "HUD del chat");
        VALUE_ES.put("Old", "Antiguo");
        VALUE_ES.put("New", "Nuevo");
        VALUE_ES.put("Scale", "Escala");
        VALUE_ES.put("Pull", "Desplazar");
        VALUE_ES.put("None", "Ninguno");
        VALUE_ES.put("Mio", "Mio");
        VALUE_ES.put("Debug", "Depuración");
        VALUE_ES.put("Lowercase", "Minúsculas");
        VALUE_ES.put("Normal", "Normal");
        VALUE_ES.put("Future", "Futuro");
        VALUE_ES.put("Earth", "Tierra");
        VALUE_ES.put("Moon", "Luna");
        VALUE_ES.put("Melon", "Melón");
        VALUE_ES.put("Chinese", "Chino");
    }

    private static void addSettings(
            Map<String, String> table,
            boolean spanish
    ) {
        table.put("setting.Language", spanish ? "Idioma" : "Language");
        table.put("setting.GuiRounded", spanish ? "Bordes redondeados" : "Rounded Corners");
        table.put("setting.CornerRadius", spanish ? "Radio de esquinas" : "Corner Radius");
        table.put("setting.Page", spanish ? "Página" : "Page");
        table.put("setting.UIType", spanish ? "Tipo de interfaz" : "UI Type");
        table.put("setting.ActiveBox", spanish ? "Caja activa" : "Active Box");
        table.put("setting.Center", spanish ? "Centrar" : "Center");
        table.put("setting.Bind", spanish ? "Tecla" : "Bind");
        table.put("setting.Gear", spanish ? "Engranaje" : "Gear");
        table.put("setting.Font", spanish ? "Fuente" : "Font");
        table.put("setting.MaxFill", spanish ? "Relleno máximo" : "Max Fill");
        table.put("setting.Sound", spanish ? "Sonido" : "Sound");
        table.put("setting.Height", spanish ? "Altura" : "Height");
        table.put("setting.EnableAnim", spanish ? "Animación de activación" : "Enable Animation");
        table.put("setting.AnimationTime", spanish ? "Duración de animación" : "Animation Time");
        table.put("setting.Ease", spanish ? "Interpolación" : "Easing");
        table.put("setting.Main", spanish ? "Principal" : "Main");
        table.put("setting.MainEnd", spanish ? "Final principal" : "Main End");
        table.put("setting.Hover", spanish ? "Al pasar el cursor" : "Hover");
        table.put("setting.Bar", spanish ? "Barra" : "Bar");
        table.put("setting.BarEnd", spanish ? "Final de barra" : "Bar End");
        table.put("setting.DisableText", spanish ? "Texto desactivado" : "Disable Text");
        table.put("setting.EnableText", spanish ? "Texto activado" : "Enable Text");
        table.put("setting.EnableText2", spanish ? "Texto activado 2" : "Enable Text 2");
        table.put("setting.Module", spanish ? "Módulo" : "Module");
        table.put("setting.ModuleHover", spanish ? "Hover de módulo" : "Module Hover");
        table.put("setting.Setting", spanish ? "Ajuste" : "Setting");
        table.put("setting.SettingHover", spanish ? "Hover de ajuste" : "Setting Hover");
        table.put("setting.Background", spanish ? "Fondo" : "Background");
        table.put("setting.1.12", spanish ? "Compatibilidad 1.12" : "1.12 Compatibility");
        table.put("setting.Crawl", spanish ? "Gatear" : "Crawl");
        table.put("setting.ShowRotations", spanish ? "Mostrar rotaciones" : "Show Rotations");
        table.put("setting.TitleFix", spanish ? "Corregir título" : "Title Fix");
        table.put("setting.PortalGui", spanish ? "GUI en portales" : "Portal GUI");
        table.put("setting.WindowTitle", spanish ? "Título de ventana" : "Window Title");
        table.put("setting.TitleOverride", spanish ? "Sobrescribir título" : "Title Override");
        table.put("setting.DebugException", spanish ? "Depurar excepciones" : "Debug Exceptions");
        table.put("setting.CaughtException", spanish ? "Excepción capturada" : "Caught Exception");
        table.put("setting.Log", spanish ? "Registro" : "Log");
        table.put("setting.InventoryAnim", spanish ? "Animación de inventario" : "Inventory Animation");
        table.put("setting.InvTime", spanish ? "Duración de inventario" : "Inventory Time");
        table.put("setting.HotbarAnim", spanish ? "Animación de barra rápida" : "Hotbar Animation");
        table.put("setting.HotbarTime", spanish ? "Duración de barra rápida" : "Hotbar Time");
        table.put("setting.AnimEase", spanish ? "Interpolación de animación" : "Animation Easing");
        table.put("setting.GuiBackground", spanish ? "Fondo de GUI" : "GUI Background");
        table.put("setting.CustomBackground", spanish ? "Fondo personalizado" : "Custom Background");
        table.put("setting.End", spanish ? "Final" : "End");
        table.put("setting.CustomButton", spanish ? "Botón personalizado" : "Custom Button");
        table.put("setting.Time", spanish ? "Duración" : "Time");
        table.put("setting.Snow", spanish ? "Nieve" : "Snow");
        table.put("setting.Notification", spanish ? "Notificación" : "Notification");
        table.put("setting.Color", "Color");
        table.put("setting.Pulse", spanish ? "Pulso" : "Pulse");
        table.put("setting.Speed", spanish ? "Velocidad" : "Speed");
        table.put("setting.Counter", spanish ? "Contador" : "Counter");
        table.put("setting.Style", spanish ? "Estilo" : "Style");
        table.put("setting.ModuleToggle", spanish ? "Activación de módulos" : "Module Toggle");
        table.put("setting.OnlyOne", spanish ? "Solo uno" : "Only One");
        table.put("setting.KeepHistory", spanish ? "Conservar historial" : "Keep History");
        table.put("setting.InfiniteChat", spanish ? "Chat infinito" : "Infinite Chat");
        table.put("setting.AnimTime", spanish ? "Duración de animación" : "Animation Time");
        table.put("setting.AnimOffset", spanish ? "Desplazamiento de animación" : "Animation Offset");
        table.put("setting.Fade", spanish ? "Desvanecimiento" : "Fade");
        table.put("setting.YAnim", spanish ? "Animación Y" : "Y Animation");
        table.put("setting.FadeTime", spanish ? "Duración de desvanecimiento" : "Fade Time");
        table.put("setting.InputBoxAnim", spanish ? "Animación de caja de entrada" : "Input Box Animation");
        table.put("setting.HideIndicator", spanish ? "Ocultar indicador" : "Hide Indicator");
        table.put("setting.Key", spanish ? "Tecla" : "Key");
        table.put("setting.Drawn", spanish ? "Mostrar" : "Drawn");
        table.put("state.on", spanish ? "Activado" : "On");
        table.put("state.off", spanish ? "Desactivado" : "Off");
        table.put("bind.press_key", spanish ? "Presiona una tecla..." : "Press Key...");
        table.put("bind.hold", spanish ? "Mantener" : "Hold");

        table.put("dialog.welcome.header",
                spanish ? "¡Gracias por descargar Gatopera!" : "Thank you for downloading Gatopera!");
        table.put("dialog.welcome.description",
                spanish ? "El ClickGui se abre con la tecla - Y" : "ClickGui opens with the key - Y");
        table.put("dialog.welcome.yes",
                spanish ? "Continuar" : "Continue");
        table.put("dialog.welcome.no",
                spanish ? "Salir del juego" : "Quit Game");
        table.put("dialog.language.header",
                spanish ? "¡Hola!" : "Hello!");
        table.put("dialog.language.description",
                spanish ? "¿Cuál es tu idioma?" : "What is your language?");
        table.put("dialog.language.yes", "English");
        table.put("dialog.language.no", "Español");
    }

    private I18n() {
    }

    public static void setLanguage(Language language) {
        if (language == null) {
            language = Language.EN_US;
        }

        current = language;

        if (Gatopera.CONFIG != null) {
            Gatopera.CONFIG.settingsPut(
                    "language",
                    language.getCode()
            );
        }
    }

    public static Language getLanguage() {
        return current;
    }

    public static void loadFromConfig() {
        if (Gatopera.CONFIG == null) {
            return;
        }

        current = Language.fromCode(
                Gatopera.CONFIG.getString(
                        "language",
                        Language.EN_US.getCode()
                )
        );
    }

    public static String t(String key) {
        Map<String, String> table =
                TABLES.getOrDefault(
                        current,
                        TABLES.get(Language.EN_US)
                );

        String value = table.get(key);

        if (value != null) {
            return value;
        }

        return TABLES.get(Language.EN_US)
                .getOrDefault(key, key);
    }

    public static String t(
            String key,
            Object... args
    ) {
        return String.format(
                t(key),
                args
        );
    }

    public static String setting(String name) {
        return t("setting." + name);
    }

    public static String value(String name) {
        if (current == Language.ES_MX) {
            String translated = VALUE_ES.get(name);

            if (translated != null) {
                return translated;
            }
        }

        return t("value." + name);
    }

    public static String category(String name) {
        return t(
                "gui.category."
                        + name.toLowerCase()
        );
    }

    public static String module(String name) {
        if (current == Language.ES_MX) {
            String translated = MODULE_ES.get(name);

            if (translated != null) {
                return translated;
            }
        }

        return name;
    }

    public static void syncFromClientSetting() {
        if (ClientSetting.INSTANCE != null
                && ClientSetting.INSTANCE.language != null) {
            setLanguage(
                    ClientSetting.INSTANCE.language.getValue()
            );
        }
    }
}