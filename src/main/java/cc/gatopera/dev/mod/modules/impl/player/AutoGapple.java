package cc.gatopera.dev.mod.modules.impl.player;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.UpdateWalkingPlayerEvent;
import cc.gatopera.dev.api.utils.combat.CombatUtil;
import cc.gatopera.dev.api.utils.entity.EntityUtil;
import cc.gatopera.dev.api.utils.entity.InventoryUtil;
import cc.gatopera.dev.api.utils.math.Timer;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.impl.combat.AutoCrystal;
import cc.gatopera.dev.mod.modules.impl.exploit.Blink;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

import java.util.List;

public class AutoGapple extends Module {
    public static AutoGapple INSTANCE;

    public AutoGapple() {
        super("AutoGapple", Category.Player);
        setChinese("自动金苹果");
        INSTANCE = this;
    }

    public final EnumSetting<Page> page = add(new EnumSetting<>("Page", Page.General));

    // General
    private final SliderSetting healthThreshold =
            add(new SliderSetting("Health", 12.0, 0.0, 20.0, 0.5, () -> page.getValue() == Page.General).setSuffix("hp"));
    private final SliderSetting absorptionThreshold =
            add(new SliderSetting("Absorption", 0.0, 0.0, 20.0, 0.5, () -> page.getValue() == Page.General).setSuffix("hp"));
    private final EnumSetting<AppleMode> appleMode =
            add(new EnumSetting<>("AppleMode", AppleMode.Normal, () -> page.getValue() == Page.General));
    private final BooleanSetting inventorySwap =
            add(new BooleanSetting("InventorySwap", true, () -> page.getValue() == Page.General));
    private final BooleanSetting usingPause =
            add(new BooleanSetting("UsingPause", true, () -> page.getValue() == Page.General));
    private final BooleanSetting onlyInCombat =
            add(new BooleanSetting("OnlyCombat", false, () -> page.getValue() == Page.General));

    // Smart
    private final BooleanSetting smart =
            add(new BooleanSetting("Smart", true, () -> page.getValue() == Page.Smart).setParent());
    private final SliderSetting predictDamage =
            add(new SliderSetting("PredictDmg", 8.0, 0.0, 20.0, 0.5, () -> page.getValue() == Page.Smart && smart.getValue()).setSuffix("dmg"));
    private final SliderSetting crystalRange =
            add(new SliderSetting("CrystalRange", 6.0, 1.0, 10.0, 0.5, () -> page.getValue() == Page.Smart && smart.getValue()).setSuffix("m"));

    // Delay
    private final SliderSetting eatDelay =
            add(new SliderSetting("EatDelay", 0, 0, 500, 10, () -> page.getValue() == Page.General).setSuffix("ms"));
    private final Timer eatTimer = new Timer();

    private boolean eating = false;

    @Override
    public void onDisable() {
        stopEating();
    }

    @Override
    public String getInfo() {
        if (eating) return "Eating";
        return null;
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        onUpdate();
    }

    @Override
    public void onUpdate() {
        if (eating && !shouldEat()) {
            stopEating();
            return;
        }

        if (!eating && shouldEat() && eatTimer.passedMs(eatDelay.getValueInt())) {
            startEating();
        }
    }

    private boolean shouldEat() {
        if (nullCheck()) return false;
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) return false;
        if (usingPause.getValue() && mc.player.isUsingItem()) return false;
        if (mc.currentScreen != null) return false;

        if (onlyInCombat.getValue() && !isInCombat()) return false;

        float health = EntityUtil.getHealth(mc.player);
        float absorption = mc.player.getAbsorptionAmount();

        if (health > healthThreshold.getValueFloat()) return false;
        if (absorption >= absorptionThreshold.getValueFloat()) return false;

        if (smart.getValue() && !willTakeDamage()) return false;

        return findApple() != -1 || (inventorySwap.getValue() && findAppleInventory() != -1);
    }

    private boolean willTakeDamage() {
        float predictedHealth = mc.player.getHealth() - predictDamage.getValueFloat();
        return predictedHealth <= healthThreshold.getValueFloat();
    }

    private boolean isInCombat() {
        List<PlayerEntity> enemies = CombatUtil.getEnemies(crystalRange.getValue());
        if (!enemies.isEmpty()) return true;

        return AutoCrystal.INSTANCE.isOn() && AutoCrystal.crystalPos != null;
    }

    private void startEating() {
        int appleSlot = findApple();

        if (appleSlot == -1 && inventorySwap.getValue()) {
            appleSlot = findAppleInventory();
        }

        if (appleSlot == -1) return;

        doSwap(appleSlot);
        mc.options.useKey.setPressed(true);
        eating = true;
        eatTimer.reset();
    }

    private void stopEating() {
        mc.options.useKey.setPressed(false);
        eating = false;
    }

    private int findApple() {
        if (appleMode.getValue() == AppleMode.Enchanted || appleMode.getValue() == AppleMode.Both) {
            int slot = InventoryUtil.findItem(Items.ENCHANTED_GOLDEN_APPLE);
            if (slot != -1) return slot;
        }
        if (appleMode.getValue() == AppleMode.Normal || appleMode.getValue() == AppleMode.Both) {
            return InventoryUtil.findItem(Items.GOLDEN_APPLE);
        }
        return -1;
    }

    private int findAppleInventory() {
        if (appleMode.getValue() == AppleMode.Enchanted || appleMode.getValue() == AppleMode.Both) {
            int slot = InventoryUtil.findItemInventorySlot(Items.ENCHANTED_GOLDEN_APPLE);
            if (slot != -1) return slot;
        }
        if (appleMode.getValue() == AppleMode.Normal || appleMode.getValue() == AppleMode.Both) {
            return InventoryUtil.findItemInventorySlot(Items.GOLDEN_APPLE);
        }
        return -1;
    }

    private void doSwap(int slot) {
        if (inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public enum Page {
        General,
        Smart
    }

    public enum AppleMode {
        Normal,
        Enchanted,
        Both
    }
}
