package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;

public class UpdateWalkingPlayerEvent extends Event {
    public UpdateWalkingPlayerEvent(Stage stage) {
        super(stage);
    }
}
