package com.stelios.cakenaysh.Listeners.Entity;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAdvancementCompletedListener implements Listener {

    @EventHandler
    public void onPlayerAdvancementCompleted(PlayerAdvancementCriterionGrantEvent e) {
        e.setCancelled(true);
    }

}
