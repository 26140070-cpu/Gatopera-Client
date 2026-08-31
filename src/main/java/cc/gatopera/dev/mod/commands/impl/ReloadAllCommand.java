package cc.gatopera.dev.mod.commands.impl;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.mod.commands.Command;

import java.util.List;

public class ReloadAllCommand extends Command {

	public ReloadAllCommand() {
		super("reloadall", "");
	}

	@Override
	public void runCommand(String[] parameters) {
		CommandManager.sendChatMessage("§fReloading..");
		Gatopera.unload();
        try {
            Gatopera.load();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		return null;
	}
}
