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
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class WebAura extends Module {
    public static WebAura INSTANCE;

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

    public WebAura() {
        super("WebAura", Category.Combat);
        setChinese("蛛网光环");
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
            BlockPos targetPos = target.getBlockPos();

            for (float x : new float[]{0, 0.5f, -0.5f}) {
                for (float z : new float[]{0, 0.5f, -0.5f}) {
                    BlockPos placePos = new BlockPos(
                        (int) (targetPos.getX() + x),
                        targetPos.getY(),
                        (int) (targetPos.getZ() + z)
                    );
                    if (mc.world.isAir(placePos) && BlockUtil.getPlaceSide(placePos, range.getValue()) != null) {
                        int slot = inventorySwap.getValue() ?
                            InventoryUtil.findBlockInventorySlot(Blocks.COBWEB) :
                            InventoryUtil.findBlock(Blocks.COBWEB);
                        if (slot != -1) {

                            timer.reset();
                            return;
                        }
                    }
                }
            }
        }
    }
}
