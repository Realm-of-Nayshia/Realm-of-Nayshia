package com.stelios.cakenaysh.Events;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ProficiencyChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String proficiencyType;

    public ProficiencyChangedEvent(Player player, String proficiencyType){
        this.player = player;
        this.proficiencyType = proficiencyType;
    }

    //getters
    public Player getPlayer() {
        return player;
    }
    public String getProficiencyType() {
        return proficiencyType;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

}
