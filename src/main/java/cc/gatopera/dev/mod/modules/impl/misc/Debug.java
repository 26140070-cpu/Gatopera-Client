package cc.gatopera.dev.mod.modules.impl.misc;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.PacketEvent;
import cc.gatopera.dev.core.impl.CommandManager;
import cc.gatopera.dev.api.utils.entity.EntityUtil;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.mod.modules.Module;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Debug extends Module {

    public Debug() {
        super("Debug", Category.Misc);
    }

    Timer timer = new Timer();
    @EventHandler
    public void onReceivePacket(PacketEvent.Send event) {
        if (nullCheck()) return;
        if (event.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                timer.reset();
            } else if (packet.getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
                CommandManager.sendChatMessage(timer.getPassedTimeMs() + "ms");
            }
            CommandManager.sendChatMessage(packet.getAction().name());
        }
    }

    @Override
    public void onEnable() {

        if (nullCheck()) return;
        BlockPos pos = EntityUtil.getPlayerPos(true).down();
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.DOWN));
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN));
    }
}