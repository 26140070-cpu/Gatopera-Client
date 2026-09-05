package cc.gatopera.dev.mod.modules.settings;

import cc.gatopera.dev.api.i18n.I18n;

import java.util.function.BooleanSupplier;

public abstract class Setting {
	public static Setting current;
	public boolean hide = false;
	private final String name;
	private final String line;
	public final BooleanSupplier visibility;

	public Setting(String name, String line) {
		this.name = name;
		this.line = line;
		this.visibility = null;
	}

	public Setting(String name, String line, BooleanSupplier visibilityIn) {
		this.name = name;
		this.line = line;
		this.visibility = visibilityIn;
	}

	public final String getName() {
		return this.name;
	}

	public final String getDisplayName() {
		return I18n.setting(this.name);
	}

	public final String getLine() {
		return this.line;
	}

	public abstract void loadSetting();

    public void hide() {
		hide = true;
	}
}
