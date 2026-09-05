package cc.gatopera.dev.mod.modules.impl.combat;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.UpdateWalkingPlayerEvent;
import cc.gatopera.dev.api.utils.combat.CombatUtil;
import cc.gatopera.dev.api.utils.entity.InventoryUtil;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.api.utils.world.BlockUtil;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.exploit.Blink;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class WebCleaner extends Module {
    public static WebCleaner INSTANCE;

    public final SliderSetting delay =
            add(new SliderSetting("Delay", 50, 0, 500).setSuffix("ms"));
    private final SliderSetting range =
            add(new SliderSetting("Range", 5.0, 1.0, 6.0, 0.1).setSuffix("m"));
    private final SliderSetting targetRange =
            add(new SliderSetting("TargetRange", 8.0, 1.0, 8.0, 0.1).setSuffix("m"));
    private final BooleanSetting inventorySwap =
            add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting usingPause =
            add(new BooleanSetting("UsingPause", true));

    private final Timer timer = new Timer();

    public WebCleaner() {
        super("WebCleaner", Category.Combat);
        setChinese("清蛛网");
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

        for (PlayerEntity player : CombatUtil.getEnemies(targetRange.getValue())) {
            BlockPos playerPos = player.getBlockPos();
            for (BlockPos pos : BlockUtil.getSphere((float) range.getValue())) {
                if (mc.world.getBlockState(pos).getBlock() == Blocks.COBWEB) {

                    mc.interactionManager.attackBlock(pos, net.minecraft.util.math.Direction.UP);
                    mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                    timer.reset();
                    return;
                }
            }
        }
    }
}
