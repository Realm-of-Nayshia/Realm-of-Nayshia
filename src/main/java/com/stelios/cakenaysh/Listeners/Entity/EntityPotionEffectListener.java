package com.stelios.cakenaysh.Listeners.Entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class EntityPotionEffectListener implements Listener {

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent e) {

        if (e.getEntity() instanceof Player) {

            try {
                //if the effect is infinite, cancel its alteration
                if (e.getOldEffect().getDuration() == -1 && e.getNewEffect().getDuration() != -1) {
                    e.setCancelled(true);
                }
            } catch (NullPointerException ex) {
                //if the old effect is null, then the effect is being added
                //if the new effect is null, then the effect is being removed
                //if both are null, then the effect is being changed
                //if neither are null, then the effect is being changed
            }
        }

    }

}
