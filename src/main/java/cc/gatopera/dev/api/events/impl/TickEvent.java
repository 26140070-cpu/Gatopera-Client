package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;

public class TickEvent extends Event {
    public TickEvent(Stage stage) {
        super(stage);
    }
}
