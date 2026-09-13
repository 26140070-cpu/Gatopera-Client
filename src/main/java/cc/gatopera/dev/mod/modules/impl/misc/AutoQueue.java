package cc.gatopera.dev.mod.modules.impl.misc;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.PacketEvent;
import cc.gatopera.dev.api.utils.entity.InventoryUtil;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.HashMap;

public class AutoQueue extends Module {
    public static HashMap<String, String> asks = new HashMap<>();

    public AutoQueue() {
        super("AutoQueue", Category.Misc);
    }
    private final BooleanSetting queueCheck = add(new BooleanSetting("QueueCheck", true));

    public static boolean inQueue = false;
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            inQueue = false;
            return;
        }
        inQueue = InventoryUtil.findItem(Items.COMPASS) != -1;
    }

    @Override
    public void onDisable() {
        inQueue = false;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive e) {
        if (!inQueue && queueCheck.getValue()) return;
        if (e.getPacket() instanceof GameMessageS2CPacket packet) {
            for (String key : asks.keySet()) {
                if (packet.content().getString().contains(key)) {
                    String[] abc = new String[]{"A", "B", "C"};
                    for (String s : abc) {
                        if (packet.content().getString().contains(s + "." + asks.get(key))) {
                            mc.getNetworkHandler().sendChatMessage(s.toLowerCase());
                            return;
                        }
                    }
                }
            }
        }
    }
}