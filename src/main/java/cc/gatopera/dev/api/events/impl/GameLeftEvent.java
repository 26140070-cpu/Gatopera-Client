package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;

public class GameLeftEvent extends Event {
    public GameLeftEvent() {
        super(Stage.Post);
    }
}
