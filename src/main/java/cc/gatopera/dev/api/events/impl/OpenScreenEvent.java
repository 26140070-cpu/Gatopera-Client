package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;
import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends Event {
    public Screen screen;
    public OpenScreenEvent(Screen screen) {
        super(Stage.Pre);
        this.screen = screen;
    }
}

