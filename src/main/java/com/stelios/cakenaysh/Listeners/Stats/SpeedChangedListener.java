package com.stelios.cakenaysh.Listeners.Stats;

import com.stelios.cakenaysh.Events.SpeedChangedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpeedChangedListener implements Listener {

    @EventHandler
    public void onSpeedChanged(SpeedChangedEvent e){
        float walkSpeed = 0.2F * ((e.getSpeed() + 100) / 100);
        if (walkSpeed > 1) {
            walkSpeed = 1;
        }

        e.getPlayer().setWalkSpeed(walkSpeed);
    }

}