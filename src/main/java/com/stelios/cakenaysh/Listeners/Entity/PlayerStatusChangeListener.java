package com.stelios.cakenaysh.Listeners.Entity;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Managers.StatsManager;
import com.stelios.cakenaysh.Util.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerStatusChangeListener implements Listener {

    private final Main main;
    private final StatsManager statsManager;

    public PlayerStatusChangeListener(Main main, StatsManager statsManager){
        this.main = main;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        //set player configurations
        Player player = e.getPlayer();

        statsManager.setConfigurations(player);

        //wait 1 tick to load the player's stats
        new BukkitRunnable(){
            @Override
            public void run() {
                statsManager.displayActionBar(player);
                statsManager.updateHearts(player);
            }
        }.runTaskLater(main, 1);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        statsManager.updateDatabaseStatsPlayer(e.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent e){

        //set player configurations
        Player player = e.getPlayer();
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());
        statsManager.setConfigurations(player);

        //reset player health and stamina
        customPlayer.setHealth(customPlayer.getMaxHealth());
        customPlayer.setStamina(customPlayer.getMaxStamina());

        //display the action bar
        statsManager.displayActionBar(player);
    }

    //cancel vanilla health regen
    @EventHandler
    public void onVanillaHeal(EntityRegainHealthEvent e){
        e.setCancelled(true);
    }

}
