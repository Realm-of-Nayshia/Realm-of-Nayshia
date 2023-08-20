package com.stelios.cakenaysh.Listeners.Server;

import com.mongodb.MongoException;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Managers.StatsManager;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerStartupListener implements Listener {

    private final Main main;
    private final StatsManager statsManager;

    public ServerStartupListener(Main main, StatsManager statsManager){
        this.main = main;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onStart(ServerLoadEvent e){

        //saving player stats every 30 minutes
        new BukkitRunnable(){
            @Override
            public void run() {
                //send a message to everyone
                main.getServer().sendMessage(Component.text("Saving player stats...", TextColor.color(100,100,100), TextDecoration.ITALIC));
                statsManager.updateDatabaseStatsAll();
                main.getServer().sendMessage(Component.text("Player stats saved.", TextColor.color(100,100,100), TextDecoration.ITALIC));
            }
        }.runTaskTimerAsynchronously(main, 0, 20*1800);
    }


    @EventHandler
    public void onStartup(ServerLoadEvent e){

        new BukkitRunnable(){
            @Override
            public void run() {

                //ping the database to make sure it's connected
                try {
                    main.getDatabase().getDatabase().runCommand(new Document("ping", 1));

                //if the database is down
                } catch (MongoException e) {

                    //kick all the players and shut down the server
                    for (Player player : main.getServer().getOnlinePlayers()){
                        player.kick(Component.text("Database connection lost.", TextColor.color(255, 0, 0)));
                    }
                    main.getServer().shutdown();
                }

                for (Player player : main.getServer().getOnlinePlayers()){

                    //get the custom player
                    CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

                    //if the player has 0 health then don't regenerate health or stamina
                    if (customPlayer.getHealth() > 0) {

                        //if the player is level 15 or under, randomly increase the players saturation (only if saturation is below 3)
                        if (player.getSaturation() < 3 && customPlayer.getLevel() <= 15) {
                            if (Math.random() > 0.96) {
                                player.setSaturation((player.getSaturation() + 1));
                            }
                        }

                        //get the level of the player's regeneration potion effect
                        int regenLevel;
                        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                            regenLevel = player.getPotionEffect(PotionEffectType.REGENERATION).getAmplifier();
                        } else {
                            regenLevel = -1;
                        }

                        //regenerate stamina if not at max
                        if (customPlayer.getStamina() < customPlayer.getMaxStamina()) {
                            customPlayer.addStamina(customPlayer.getStaminaRegen());
                        }

                        //regenerate health if not at max
                        if (customPlayer.getHealth() < customPlayer.getMaxHealth()) {
                            customPlayer.addHealth(customPlayer.getHealthRegen() + regenLevel + 1);
                        }

                        //if over max stamina, set to max stamina
                        if (customPlayer.getStamina() > customPlayer.getMaxStamina()) {
                            customPlayer.setStamina(customPlayer.getMaxStamina());
                        }

                        //if over max health, set to max health
                        if (customPlayer.getHealth() > customPlayer.getMaxHealth()) {
                            customPlayer.setHealth(customPlayer.getMaxHealth());
                        }

                        statsManager.displayActionBar(player);
                        statsManager.updateHearts(player);

                    }else{
                        //kill the player
                        statsManager.updateHearts(player);
                    }
                }
            }
        }.runTaskTimer(main,0,40);
    }

}
