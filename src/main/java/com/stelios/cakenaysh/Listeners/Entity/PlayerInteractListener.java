package com.stelios.cakenaysh.Listeners.Entity;

import com.stelios.cakenaysh.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    Main main;

    public PlayerInteractListener(Main main){
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        //if the player is holding an item
        if (e.getItem() != null) {

            //get the item
            ItemStack item = e.getItem();

            //if the item is a custom item
            try {
                if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "itemType"))){

                    //if the player tries to place a placeable item and the player is trying to interact with a block
                    if ((e.isBlockInHand() && e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.getClickedBlock().getType().isInteractable()) ||
                            (e.isBlockInHand() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().isInteractable() && e.getPlayer().isSneaking())){
                        e.setCancelled(true);
                    }

                }
            }catch (NullPointerException ex){
                //item is not a custom item or the block doesn't have a type
            }
        }
    }
}