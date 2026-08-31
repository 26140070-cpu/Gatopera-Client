package cc.gatopera.dev.mod.modules.impl.combat;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.UpdateWalkingPlayerEvent;
import cc.gatopera.dev.api.utils.combat.CombatUtil;
import cc.gatopera.dev.api.utils.entity.InventoryUtil;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.exploit.Blink;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class Quiver extends Module {
    public static Quiver INSTANCE;

    public final SliderSetting delay =
            add(new SliderSetting("Delay", 200, 0, 500).setSuffix("ms"));
    private final SliderSetting targetRange =
            add(new SliderSetting("TargetRange", 8.0, 1.0, 8.0, 0.1).setSuffix("m"));
    private final BooleanSetting autoShoot =
            add(new BooleanSetting("AutoShoot", true));
    private final BooleanSetting rotate =
            add(new BooleanSetting("Rotate", true));

    private final Timer timer = new Timer();

    public Quiver() {
        super("Quiver", Category.Combat);
        setChinese("弓箭");
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

        // Find bow in hotbar
        int bowSlot = InventoryUtil.findItem(Items.BOW);
        if (bowSlot == -1) return;

        for (PlayerEntity target : CombatUtil.getEnemies(targetRange.getValue())) {
            if (autoShoot.getValue()) {
                // Switch to bow and shoot
                InventoryUtil.switchToSlot(bowSlot);
                mc.options.useKey.setPressed(true);
                timer.reset();
                return;
            }
        }
    }
}
