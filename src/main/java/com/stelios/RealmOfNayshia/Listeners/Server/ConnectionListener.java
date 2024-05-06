package com.stelios.RealmOfNayshia.Listeners.Server;

import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.Util.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final Main main;

    public ConnectionListener(Main main){
        this.main = main;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();

        //inject the player
        main.getPacketManager().inject(player);

        //creates a new CustomPlayer for each joining player
        CustomPlayer playerData = new CustomPlayer(main, player.getUniqueId());
        main.getPlayerManager().addCustomPlayer(player.getUniqueId(), playerData);

        //create database collection documents for new players
        main.getStashManager().createStash(player);
        main.getRecipeManager().createRecipeFile(player);
        main.getPlayerItemManager().createItemFile(player);
        main.getQuestManager().createQuestFile(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        Player player = e.getPlayer();

        //unInject the player
        main.getPacketManager().unInject(player);

        //update database collection documents
        main.getStatsManager().updateDatabaseStatsPlayer(e.getPlayer());
        main.getPlayerItemManager().updateItemFile(e.getPlayer());

        //remove the custom player after waiting 1 tick
        main.getServer().getScheduler().runTaskLater(main, () -> main.getPlayerManager().removeCustomPlayer(player.getUniqueId()), 10);
    }

}
