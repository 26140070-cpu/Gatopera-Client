package cc.gatopera.dev.api.utils.math;

import cc.gatopera.dev.mod.modules.impl.client.ClickGui;

public class Animation {
    public final FadeUtils fadeUtils = new FadeUtils(0);
    public double from = 0;
    public double to = 0;

    public double get(double target) {
        return get(target, ClickGui.INSTANCE.animationTime.getValueInt(), ClickGui.INSTANCE.ease.getValue());
    }

    public double get(double target, long length, Easing ease) {
        if (length <= 0) return target;
        if (target != to) {
            from = from + (to - from) * fadeUtils.ease(ease);
            to = target;
            fadeUtils.reset();
        }
        fadeUtils.setLength(length);
        return from + (to - from) * fadeUtils.ease(ease);
    }
}