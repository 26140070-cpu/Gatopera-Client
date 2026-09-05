package cc.gatopera.dev.mod.modules.impl.combat;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.PacketEvent;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AntiPiston extends Module {
    public static AntiPiston INSTANCE;

    public final SliderSetting range =
            add(new SliderSetting("Range", 4.0, 1.0, 6.0, 0.1).setSuffix("m"));
    private final BooleanSetting onlyHole =
            add(new BooleanSetting("OnlyHole", true));

    public AntiPiston() {
        super("AntiPiston", Category.Combat);
        setChinese("防活塞");
        INSTANCE = this;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof BlockUpdateS2CPacket packet) {

        }
    }
}
