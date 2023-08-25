package com.stelios.cakenaysh.Managers;

import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerInventoryManager {

    Main main = Main.getPlugin(Main.class);


    //add an item to the player's inventory
    public void addItemToInventory (Player player, Item item) {

        //if the player's inventory is full, add the item to their stash
        if (player.getInventory().firstEmpty() == -1) {
            main.getStashManager().addItemToStash(player, item.build());

        //if the player's inventory isn't full, add the item to their inventory
        } else {
            player.getInventory().addItem(item.build());
        }
    }


    //add multiple items to the player's inventory
    public void addItemsToInventory (Player player, ArrayList<Item> items) {

        //loop through the items
        for (Item item : items) {

            //if the player's inventory is full, add the item to their stash
            if (player.getInventory().firstEmpty() == -1) {
                main.getStashManager().addItemToStash(player, item.build());

            //if the player's inventory isn't full, add the item to their inventory
            } else {
                player.getInventory().addItem(item.build());
            }
        }
    }


    //remove an item from the inventory
    public void removeItemFromInventory (Player player, Item item) {
        player.getInventory().remove(item.build());
    }

    //remove multiple items from the inventory
    public void removeItemsFromInventory (Player player, ArrayList<Item> items) {

        //loop through the items
        for (Item item : items) {
            player.getInventory().remove(item.build());
        }
    }
}
