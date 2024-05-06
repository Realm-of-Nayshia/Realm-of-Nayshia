package com.stelios.RealmOfNayshia.Listeners.Server;

import com.mongodb.MongoException;
import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.Managers.StatsManager;
import com.stelios.RealmOfNayshia.Util.CustomPlayer;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.logging.Level;

public class ServerStartupListener implements Listener {

    private final Main main;
    private final StatsManager statsManager;

    public ServerStartupListener(Main main, StatsManager statsManager){
        this.main = main;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onStart(ServerLoadEvent e){

        //check plugin json files
        main.getDataFolder().mkdir();
        File items = new File(main.getDataFolder(), "items.json");
        File recipes = new File(main.getDataFolder(), "recipes.json");
        File quests = new File(main.getDataFolder(), "quests.json");
        File setBonuses = new File(main.getDataFolder(), "setBonuses.json");
        if (!(items.exists() && recipes.exists() && quests.exists() && setBonuses.exists())){
            main.getLogger().log(Level.SEVERE, "Plugin files are missing! Shutting down server...");
            main.getServer().shutdown();
        }

        //saving player stats every hour
        new BukkitRunnable(){
            @Override
            public void run() {
                //send a message to everyone
                main.getLogger().log(Level.INFO, "Saving player stats...");
                statsManager.updateDatabaseStatsAll();
                main.getLogger().log(Level.INFO, "Player stats saved.");
            }
        }.runTaskTimerAsynchronously(main, 0, 20*3600);
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

                    //log the error and shutdown the server
                    main.getLogger().log(Level.SEVERE, "Database is down!");
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
                        double regenPercent = 1+((regenLevel+1)/10.0);

                        //regenerate stamina if not at max
                        if (customPlayer.getStamina() < customPlayer.getMaxStamina()) {
                            customPlayer.addStamina(customPlayer.getStaminaRegen());
                        }

                        //regenerate health if not at max
                        if (customPlayer.getHealth() < customPlayer.getMaxHealth()) {
                            customPlayer.addHealth(customPlayer.getHealthRegen() * (float) regenPercent);
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
