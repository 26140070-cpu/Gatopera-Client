package cc.gatopera.dev.mod.modules.impl.render;

import cc.gatopera.dev.Gatopera;
import cc.gatopera.dev.api.events.eventbus.EventHandler;
import cc.gatopera.dev.api.events.impl.Render3DEvent;
import cc.gatopera.dev.api.utils.math.Animation;
import cc.gatopera.dev.api.utils.math.Easing;
import cc.gatopera.dev.mod.modules.Module;
import cc.gatopera.dev.mod.modules.settings.impl.EnumSetting;
import cc.gatopera.dev.mod.modules.settings.impl.SliderSetting;

public class Zoom extends Module {
    public static Zoom INSTANCE;
    public double currentFov;
    private final SliderSetting animTime = add(new SliderSetting("AnimTime", 300, 0, 1000));
    public final EnumSetting<Easing> ease = add(new EnumSetting<>("Ease", Easing.CubicInOut));
    final SliderSetting fov = add(new SliderSetting("ZoomFov", 60, 0, 130, 1));
    public Zoom() {
        super("Zoom", Category.Render);
        setChinese("放大");
        INSTANCE = this;
        Gatopera.EVENT_BUS.subscribe(new ZoomAnim());
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            disable();
        }
    }
    Animation animation = new Animation();

    public static boolean on = false;
    public class ZoomAnim {
        @EventHandler
        public void onRender3D(Render3DEvent event) {
            if (isOn()) {
                currentFov = animation.get(fov.getValue(), animTime.getValueInt(), ease.getValue());
                on = true;
            } else if (on) {
                currentFov = animation.get(0, animTime.getValueInt(), ease.getValue());
                if ((int) currentFov == 0) {
                    on = false;
                }
            }
        }
    }
}
