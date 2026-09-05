package cc.gatopera.dev.api.i18n;

public enum Language {
    EN_US("en_us", "English"),
    ES_MX("es_mx", "Español");

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Language fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return EN_US;
        }
        for (Language language : values()) {
            if (language.code.equalsIgnoreCase(code) || language.name().equalsIgnoreCase(code)) {
                return language;
            }
        }
        return EN_US;
    }
}
