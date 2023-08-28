package com.stelios.cakenaysh.Managers;

import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;

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
    public void removeItemWithNameFromInventory (Player player, String itemName) {

        //get the player's inventory
        ItemStack[] inventory = player.getInventory().getContents();

        //loop through all the items in the player's inventory
        for (ItemStack item : inventory) {

            //if the item is null or doesn't have a custom name, continue
            if (item == null || item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING) == null) {
                continue;
            }

            //if the item has the same name as the item name, remove it
            if (Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)).equals(itemName)) {
                player.getInventory().removeItem(item);
                break;
            }
        }
    }

    //remove multiple items from the inventory
    public void removeItemsWithNameFromInventory(Player player, ArrayList<String> items) {

        //get the player's inventory
        ItemStack[] inventory = player.getInventory().getContents();

        //loop through all the item names
        for (String itemName : items) {

            //loop through all the items in the player's inventory
            for (ItemStack item : inventory) {

                //if the item is null or doesn't have a custom name, continue
                if (item == null || item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING) == null) {
                    continue;
                }

                //if the item has the same name as the item name, remove it
                if (Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)).equals(itemName)) {
                    player.getInventory().removeItem(item);
                    break;
                }
            }
        }

    }
}
