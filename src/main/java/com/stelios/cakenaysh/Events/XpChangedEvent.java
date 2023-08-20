package com.stelios.cakenaysh.Events;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class XpChangedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final int xpGained;
    private final NPC npc;


    public XpChangedEvent(Player player, int xpGained, NPC npc){
        this.player = player;
        this.xpGained = xpGained;
        this.npc = npc;
    }

    //getters
    public Player getPlayer() {
        return player;
    }
    public int getXpGained() {
        return xpGained;
    }
    public NPC getNpc() {
        return npc;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

}
