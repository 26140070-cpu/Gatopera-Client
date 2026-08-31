package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;

public class EntityVelocityUpdateEvent extends Event {
    public EntityVelocityUpdateEvent() {
        super(Stage.Pre);
    }
}
