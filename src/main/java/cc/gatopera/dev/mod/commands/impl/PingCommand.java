package cc.gatopera.dev.mod.commands.impl;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.PacketEvent;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.mod.commands.Command;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.List;

public class PingCommand extends Command {

	public PingCommand() {
		super("ping", "");
	}

	private long sendTime;

	@Override
	public void runCommand(String[] parameters) {
		sendTime = System.currentTimeMillis();
		mc.getNetworkHandler().sendChatCommand("chat ");
		Gatopera.EVENT_BUS.subscribe(this);
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		return null;
	}

	@EventHandler
	public void onPacketReceive(PacketEvent.Receive e) {
		if (e.getPacket() instanceof GameMessageS2CPacket packet) {
			String msg = packet.content().getString();
			if (msg.contains("chat.use") || msg.contains("Bad command") || msg.contains("No such command") || msg.contains("<--[HERE]") || msg.contains("Unknown")) {
				CommandManager.sendChatMessage("ping: " + (System.currentTimeMillis() - sendTime) + "ms");
				Gatopera.EVENT_BUS.unsubscribe(this);
			}
		}
	}
}