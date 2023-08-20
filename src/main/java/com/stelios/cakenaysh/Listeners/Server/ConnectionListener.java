package com.stelios.cakenaysh.Listeners.Server;

import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Util.CustomPlayer;
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

        //create a stash for new players
        main.getStashManager().createStash(player);

        //create a recipe file for new players and remove all their recipes
        main.getRecipeManager().createRecipeFile(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        Player player = e.getPlayer();

        //unInject the player
        main.getPacketManager().unInject(player);

        //remove the custom player after waiting 1 tick
        main.getServer().getScheduler().runTaskLater(main, () -> main.getPlayerManager().removeCustomPlayer(player.getUniqueId()), 10);
    }

}
