package com.stelios.cakenaysh.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpeedChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final float speed;
    private final Player player;


    public SpeedChangedEvent(Player player, float speed){
        this.player = player;
        this.speed = speed;
    }

    //getters
    public Player getPlayer() {
        return player;
    }
    public float getSpeed() {
        return speed;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
