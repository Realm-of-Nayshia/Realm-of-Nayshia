package com.stelios.cakenaysh.Listeners.Stats;

import com.stelios.cakenaysh.Events.XpChangedEvent;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class XpGainListener implements Listener {

    Main main;

    public XpGainListener(Main main){
        this.main = main;
    }

    //checking if the player has enough xp to level up
    @EventHandler
    public void onXpGain(XpChangedEvent e){

        //get the player and custom player
        Player player = e.getPlayer();
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        //if the player has enough xp to level up
        if (customPlayer.howManyLevelUps() > 0){

            int levelUps = customPlayer.howManyLevelUps();

            //level up the player
            customPlayer.addLevels(levelUps);
            player.sendMessage(Component.text("You leveled up to level " + customPlayer.getLevel() + "!", TextColor.color(0, 255, 0)));
            player.showTitle(Title.title(Component.text(" LEVEL UP! ", TextColor.color(0, 255, 0)), Component.text(customPlayer.getLevel()-levelUps + " -----> " + customPlayer.getLevel(), TextColor.color(0, 255, 0))));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            customPlayer.addInvestmentPoints(levelUps);
        }

    }

}
