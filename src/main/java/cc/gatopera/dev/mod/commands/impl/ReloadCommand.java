package cc.gatopera.dev.mod.commands.impl;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.core.impl.ConfigManager;
import cc.gatopera.dev.mod.commands.Command;

import java.util.List;

public class ReloadCommand extends Command {

	public ReloadCommand() {
		super("reload", "");
	}

	@Override
	public void runCommand(String[] parameters) {
		CommandManager.sendChatMessage("§fReloading..");
		Gatopera.CONFIG = new ConfigManager();
		Gatopera.PREFIX = Gatopera.CONFIG.getString("prefix", Gatopera.PREFIX);
		Gatopera.CONFIG.loadSettings();
		Gatopera.XRAY.read();
		Gatopera.TRADE.read();
		Gatopera.FRIEND.read();
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		return null;
	}
}
