package cc.gatopera.dev.mod.modules.impl.combat;

import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.UpdateWalkingPlayerEvent;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.settings.impl.BooleanSetting;

public class SilentDouble extends Module {
    public static SilentDouble INSTANCE;

    private final BooleanSetting onlyKill =
            add(new BooleanSetting("OnlyKill", false));

    public SilentDouble() {
        super("SilentDouble", Category.Combat);
        setChinese("静默双持");
        INSTANCE = this;
    }

    @EventHandler
    public void onUpdateWalking(UpdateWalkingPlayerEvent event) {
        if (nullCheck()) return;

    }
}
