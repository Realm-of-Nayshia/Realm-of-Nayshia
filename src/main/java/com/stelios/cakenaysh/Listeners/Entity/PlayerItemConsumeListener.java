package com.stelios.cakenaysh.Listeners.Entity;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Util.CustomPlayer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class PlayerItemConsumeListener implements Listener {

    Main main;

    public PlayerItemConsumeListener(Main main){
        this.main = main;
    }


    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e){

        //get info from the item
        ItemStack item = e.getItem();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        //if the item consumed is a not custom item
        if (!pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING)){
            return;
        }

        //if the item consumed is not a consumable item
        if (!Objects.requireNonNull(pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING)).equals("consumable")){
            return;
        }

        //get the player
        Player player = e.getPlayer();
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        ////add the attributes of the item to the player
        //food level
        player.setFoodLevel(player.getFoodLevel() + Objects.requireNonNull(pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER)));

        //stats
        String[] stats = Objects.requireNonNull(pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "stats"), DataType.STRING_ARRAY));
        int[] statsAmount = Objects.requireNonNull(pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "statsAmount"), DataType.INTEGER_ARRAY));
        int[] statsDuration = Objects.requireNonNull(pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "statsDuration"), DataType.INTEGER_ARRAY));

        for (int i = 0; i <stats.length; i ++){
            customPlayer.addStatWithDelay(stats[i].replaceAll("\\s", ""), statsAmount[i], statsDuration[i] * 20L);
        }

        //potion effects
        PotionEffect[] potionEffects = Objects.requireNonNull(pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY));

        for (PotionEffect potionEffect : potionEffects){
            potionEffect.apply(player);
        }
    }


}
