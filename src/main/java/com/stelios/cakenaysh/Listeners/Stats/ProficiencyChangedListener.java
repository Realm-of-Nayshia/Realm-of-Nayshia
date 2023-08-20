package com.stelios.cakenaysh.Listeners.Stats;

import com.stelios.cakenaysh.Events.ProficiencyChangedEvent;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Managers.StatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class ProficiencyChangedListener implements Listener {

    private final Main main;
    private final StatsManager statsManager;

    public ProficiencyChangedListener(Main main, StatsManager statsManager){
        this.main = main;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onProficiencyChanged(ProficiencyChangedEvent e){
        Player player = e.getPlayer();

        //remove the player's stats before the proficiency is changed
        statsManager.removePlayerStats(player, player.getInventory().getItemInMainHand(), "weapon");
        statsManager.removePlayerStats(player, player.getInventory().getHelmet(), "armor");
        statsManager.removePlayerStats(player, player.getInventory().getChestplate(), "armor");
        statsManager.removePlayerStats(player, player.getInventory().getLeggings(), "armor");
        statsManager.removePlayerStats(player, player.getInventory().getBoots(), "armor");

        //wait 1 tick to allow the proficiency to be changed
        new BukkitRunnable(){
            @Override
            public void run() {

                //recalculate the player's stats after the proficiency has been changed
                statsManager.addPlayerStats(player, player.getInventory().getItemInMainHand(), "weapon");
                statsManager.addPlayerStats(player, player.getInventory().getHelmet(), "armor");
                statsManager.addPlayerStats(player, player.getInventory().getChestplate(), "armor");
                statsManager.addPlayerStats(player, player.getInventory().getLeggings(), "armor");
                statsManager.addPlayerStats(player, player.getInventory().getBoots(), "armor");

            }
        }.runTaskLater(main, 1);
    }

}
