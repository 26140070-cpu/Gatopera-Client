package cc.gatopera.dev.mod.commands.impl;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.core.Manager;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.core.impl.ConfigManager;
import cc.gatopera.dev.mod.commands.Command;

import java.util.List;

public class LoadCommand extends Command {

	public LoadCommand() {
		super("load", "[config]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			sendUsage();
			return;
		}
		CommandManager.sendChatMessage("§fLoading..");
		ConfigManager.options = Manager.getFile(parameters[0] + ".cfg");
		Gatopera.CONFIG = new ConfigManager();
		Gatopera.PREFIX = Gatopera.CONFIG.getString("prefix", Gatopera.PREFIX);
		Gatopera.CONFIG.loadSettings();
        ConfigManager.options = Manager.getFile("options.txt");
		Gatopera.save();
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		return null;
	}
}
