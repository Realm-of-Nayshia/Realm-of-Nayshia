package com.stelios.cakenaysh.Managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CombatManager {

    private final Cache<UUID, Long> combatTimer = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).build();

    //checks if a player is in combat
    public boolean isPlayerInCombat(UUID uuid) {
        return combatTimer.asMap().containsKey(uuid);
    }

    //puts a player into combat
    public void addCombatTimer(UUID uuid) {

        //only send the message if the player is not already in combat
        if (!isPlayerInCombat(uuid)) {
            Bukkit.getPlayer(uuid).sendMessage(Component.text("You are now in combat!", TextColor.color(255, 0, 0)));
        }

        combatTimer.put(uuid, System.currentTimeMillis() + 30000);
    }

    //removes a player from combat
    public void removeCombatTimer(UUID uuid){
        if (isPlayerInCombat(uuid)) {
            combatTimer.asMap().remove(uuid);
        }
    }

}
