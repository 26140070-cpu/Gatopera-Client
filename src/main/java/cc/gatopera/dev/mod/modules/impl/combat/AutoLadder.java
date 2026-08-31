package cc.gatopera.dev.mod.modules.impl.combat;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.LookAtEvent;
import cc.gatopera.dev.api.events.impl.UpdateWalkingPlayerEvent;
import cc.gatopera.dev.api.utils.combat.CombatUtil;
import cc.gatopera.dev.api.utils.entity.EntityUtil;
import cc.gatopera.dev.api.utils.entity.InventoryUtil;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.api.utils.world.BlockPosX;
import cc.gatopera.dev.api.utils.world.BlockUtil;
import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.client.AntiCheat;
import cc.gatopera.dev.mod.modules.impl.exploit.Blink;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static cc.gatopera.dev.api.utils.world.BlockUtil.*;

public class AutoLadder extends Module {
    public static AutoLadder INSTANCE;

    public AutoLadder() {
        super("AutoLadder", Category.Combat);
        setChinese("自动梯子");
        INSTANCE = this;
    }

    public final EnumSetting<Page> page = add(new EnumSetting<>("Page", Page.General));
    public final SliderSetting placeDelay =
            add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> page.getValue() == Page.General));
    public final SliderSetting blocksPer =
            add(new SliderSetting("BlocksPer", 2, 1, 10, () -> page.getValue() == Page.General));
    public final SliderSetting predictTicks =
            add(new SliderSetting("PredictTicks", 2, 0.0, 50, 1, () -> page.getValue() == Page.General));
    private final BooleanSetting preferAnchor = add(new BooleanSetting("PreferAnchor", true, () -> page.getValue() == Page.General));
    private final BooleanSetting detectMining =
            add(new BooleanSetting("DetectMining", true, () -> page.getValue() == Page.General));
    private final BooleanSetting onlyTick =
            add(new BooleanSetting("OnlyTick", false, () -> page.getValue() == Page.General));
    private final BooleanSetting feet =
            add(new BooleanSetting("Feet", true, () -> page.getValue() == Page.General));
    private final BooleanSetting legs =
            add(new BooleanSetting("Legs", true, () -> page.getValue() == Page.General));
    private final BooleanSetting head =
            add(new BooleanSetting("Head", false, () -> page.getValue() == Page.General));
    public final SliderSetting maxLadders =
            add(new SliderSetting("MaxLadders", 3, 1, 8, 1, () -> page.getValue() == Page.General));
    private final BooleanSetting inventorySwap =
            add(new BooleanSetting("InventorySwap", true, () -> page.getValue() == Page.General));
    private final BooleanSetting usingPause =
            add(new BooleanSetting("UsingPause", true, () -> page.getValue() == Page.General));
    public final SliderSetting placeRange =
            add(new SliderSetting("PlaceRange", 5.0, 0.0, 6.0, 0.1, () -> page.getValue() == Page.General));
    public final SliderSetting targetRange =
            add(new SliderSetting("TargetRange", 8.0, 0.0, 8.0, 0.1, () -> page.getValue() == Page.General));
    private final BooleanSetting onlyWall =
            add(new BooleanSetting("OnlyWall", true, () -> page.getValue() == Page.General));

    private final BooleanSetting rotate =
            add(new BooleanSetting("Rotate", true, () -> page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting yawStep =
            add(new BooleanSetting("YawStep", false, () -> rotate.isOpen() && page.getValue() == Page.Rotate));
    private final SliderSetting steps =
            add(new SliderSetting("Steps", 0.3, 0.1, 1.0, 0.01, () -> rotate.isOpen() && yawStep.getValue() && page.getValue() == Page.Rotate));
    private final BooleanSetting checkFov =
            add(new BooleanSetting("OnlyLooking", true, () -> rotate.isOpen() && yawStep.getValue() && page.getValue() == Page.Rotate));
    private final SliderSetting fov =
            add(new SliderSetting("Fov", 30, 0, 50, () -> rotate.isOpen() && yawStep.getValue() && checkFov.getValue() && page.getValue() == Page.Rotate));
    private final SliderSetting priority = add(new SliderSetting("Priority", 10, 0, 100, () -> rotate.isOpen() && yawStep.getValue() && page.getValue() == Page.Rotate));

    private final Timer timer = new Timer();
    public Vec3d directionVec = null;
    private final ArrayList<BlockPos> pos = new ArrayList<>();
    int progress = 0;

    @Override
    public String getInfo() {
        if (progress > 0) return "Working";
        return null;
    }

    @EventHandler
    public void onRotate(LookAtEvent event) {
        if (rotate.getValue() && yawStep.getValue() && directionVec != null) {
            event.setTarget(directionVec, steps.getValueFloat(), priority.getValueFloat());
        }
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (!onlyTick.getValue()) {
            onUpdate();
        }
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        if (!onlyTick.getValue()) {
            onUpdate();
        }
    }

    @Override
    public void onUpdate() {
        update();
    }

    private void update() {
        if (!timer.passedMs(placeDelay.getValueInt())) {
            return;
        }
        pos.clear();
        progress = 0;
        directionVec = null;
        if (preferAnchor.getValue() && AutoAnchor.INSTANCE.currentPos != null) {
            return;
        }
        if (getLadderSlot() == -1) {
            return;
        }
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return;
        if (usingPause.getValue() && mc.player.isUsingItem()) {
            return;
        }

        for (PlayerEntity player : CombatUtil.getEnemies(targetRange.getValue())) {
            Vec3d playerPos = predictTicks.getValue() > 0 ? CombatUtil.getEntityPosVec(player, predictTicks.getValueInt()) : player.getPos();
            int ladders = 0;

            if (feet.getValue()) {
                ladders += placeLaddersAtLevel(player, playerPos, 0);
            }
            if (legs.getValue()) {
                ladders += placeLaddersAtLevel(player, playerPos, 1);
            }
            if (head.getValue()) {
                ladders += placeLaddersAtLevel(player, playerPos, 2);
            }

            if (ladders >= maxLadders.getValueInt()) {
                break;
            }
        }
    }

    private int placeLaddersAtLevel(PlayerEntity player, Vec3d playerPos, int yOffset) {
        int placed = 0;
        for (Direction dir : Direction.HORIZONTAL) {
            if (placed >= maxLadders.getValueInt()) break;
            BlockPosX ladderPos = new BlockPosX(
                    playerPos.getX() + dir.getOffsetX(),
                    playerPos.getY() + yOffset,
                    playerPos.getZ() + dir.getOffsetZ()
            );
            if (placeLadder(ladderPos, dir)) {
                placed++;
            }
        }
        return placed;
    }

    private boolean placeLadder(BlockPos pos, Direction facing) {
        if (this.pos.contains(pos)) return false;
        this.pos.add(pos);
        if (progress >= blocksPer.getValueInt()) return false;
        if (getLadderSlot() == -1) return false;
        if (detectMining.getValue() && Gatopera.BREAK.isMining(pos)) return false;

        Direction attachSide = getLadderAttachSide(pos, facing);
        if (attachSide == null) return false;

        if (onlyWall.getValue()) {
            BlockPos behindPos = pos.offset(attachSide);
            if (mc.world.isAir(behindPos) || !mc.world.getBlockState(behindPos).isSolid()) {
                return false;
            }
        }

        if (mc.world.isAir(pos) && pos.getY() < 320) {
            int oldSlot = mc.player.getInventory().selectedSlot;
            int ladderSlot = getLadderSlot();
            if (!placeLadderBlock(pos, attachSide, ladderSlot)) return false;
            progress++;
            if (inventorySwap.getValue()) {
                doSwap(ladderSlot);
                EntityUtil.syncInventory();
            } else {
                doSwap(oldSlot);
            }
            timer.reset();
            return true;
        }
        return false;
    }

    private Direction getLadderAttachSide(BlockPos pos, Direction preferred) {
        for (Direction dir : Direction.HORIZONTAL) {
            BlockPos neighbor = pos.offset(dir);
            if (mc.world.getBlockState(neighbor).isSolid()) {
                if (dir == preferred) return dir;
            }
        }
        for (Direction dir : Direction.HORIZONTAL) {
            BlockPos neighbor = pos.offset(dir);
            if (mc.world.getBlockState(neighbor).isSolid()) {
                return dir;
            }
        }
        return null;
    }

    public boolean placeLadderBlock(BlockPos pos, Direction facing, int slot) {
        Vec3d directionVec = new Vec3d(
                pos.getX() + 0.5 + facing.getVector().getX() * 0.5,
                pos.getY() + 0.5 + facing.getVector().getY() * 0.5,
                pos.getZ() + 0.5 + facing.getVector().getZ() * 0.5
        );
        if (rotate.getValue()) {
            if (!faceVector(directionVec)) return false;
        }
        doSwap(slot);
        EntityUtil.swingHand(Hand.MAIN_HAND, AntiCheat.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, facing, pos, false);
        Module.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, id));
        if (rotate.getValue() && !yawStep.getValue() && AntiCheat.INSTANCE.snapBack.getValue()) {
            Gatopera.ROTATION.snapBack();
        }
        return true;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!yawStep.getValue()) {
            Gatopera.ROTATION.lookAt(directionVec);
            return true;
        } else {
            this.directionVec = directionVec;
            if (Gatopera.ROTATION.inFov(directionVec, fov.getValueFloat())) {
                return true;
            }
        }
        return !checkFov.getValue();
    }

    private void doSwap(int slot) {
        if (inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getLadderSlot() {
        if (inventorySwap.getValue()) {
            return InventoryUtil.findBlockInventorySlot(Blocks.LADDER);
        } else {
            return InventoryUtil.findBlock(Blocks.LADDER);
        }
    }

    public enum Page {
        General,
        Rotate
    }
}
