package cc.gatopera.dev.mod.commands.impl;

import cc.gatopera.dev.core.Manager;
import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.core.impl.ConfigManager;
import cc.gatopera.dev.mod.commands.Command;

import java.util.List;

public class SaveCommand extends Command {

	public SaveCommand() {
		super("save", "");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 1) {
			CommandManager.sendChatMessage("§fSaving config named " + parameters[0]);
			ConfigManager.options = Manager.getFile(parameters[0] + ".cfg");
			Gatopera.save();
			ConfigManager.options = Manager.getFile("options.txt");
		} else {
			CommandManager.sendChatMessage("§fSaving..");
		}
		Gatopera.save();
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		return null;
	}
}
