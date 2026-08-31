package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;

public class KeyboardInputEvent extends Event {
    public KeyboardInputEvent() {
        super(Stage.Pre);
    }
}
