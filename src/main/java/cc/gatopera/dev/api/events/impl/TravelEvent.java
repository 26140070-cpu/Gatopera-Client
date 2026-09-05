package cc.gatopera.dev.api.events.impl;

import cc.gatopera.dev.api.events.Event;
import net.minecraft.entity.player.PlayerEntity;

public class TravelEvent extends Event {

    private final PlayerEntity entity;

    public TravelEvent(Stage stage, PlayerEntity entity) {
        super(stage);
        this.entity = entity;
    }

    public PlayerEntity getEntity() {
        return entity;
    }
}