package cc.gatopera.dev.mod.modules.impl.combat;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.UpdateWalkingPlayerEvent;
import cc.gatopera.dev.api.utils.combat.CombatUtil;
import cc.gatopera.dev.api.utils.entity.EntityUtil;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.exploit.Blink;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class TPAura extends Module {
    public static TPAura INSTANCE;

    public final SliderSetting delay =
            add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final SliderSetting targetRange =
            add(new SliderSetting("TargetRange", 8.0, 1.0, 8.0, 0.1).setSuffix("m"));
    private final SliderSetting tpRange =
            add(new SliderSetting("TPRange", 4.0, 1.0, 6.0, 0.1).setSuffix("m"));
    private final BooleanSetting usingPause =
            add(new BooleanSetting("UsingPause", true));

    private final Timer timer = new Timer();

    public TPAura() {
        super("TPAura", Category.Combat);
        setChinese("传送光环");
        INSTANCE = this;
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        onUpdate();
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        if (!timer.passedMs(delay.getValueInt())) return;
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;
        if (usingPause.getValue() && mc.player.isUsingItem()) return;

        for (PlayerEntity target : CombatUtil.getEnemies(targetRange.getValue())) {
            Vec3d targetPos = target.getPos();
            Vec3d playerPos = mc.player.getPos();
            double distance = playerPos.distanceTo(targetPos);

            if (distance > tpRange.getValue()) {

                Vec3d tpPos = playerPos.lerp(targetPos, 0.9);
                mc.player.setPosition(tpPos.x, tpPos.y, tpPos.z);
                timer.reset();
                return;
            }
        }
    }
}
