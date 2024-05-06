package com.stelios.RealmOfNayshia.Managers;

import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PlayerInventoryManager {

    Main main = Main.getPlugin(Main.class);


    //add an item to the player's inventory
    public void addItemToInventory (Player player, Item item, int amount) {

        //if the player's inventory is full, add the item to their stash the specified amount of times
        if (player.getInventory().firstEmpty() == -1) {

            for (int i = 0; i < amount; i++) {
                main.getStashManager().addItemToStash(player, item.build());
            }

        //if the player's inventory isn't full, add the item to their inventory the specified amount of times
        } else {

            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(item.build());
            }
        }
    }


    //add multiple items to the player's inventory
    public void addItemsToInventory (Player player, HashMap<Item, Integer> items) {

        //loop through the items
        for (Item item : items.keySet()) {

            //if the player's inventory is full, add the item to their stash the specified amount of times
            if (player.getInventory().firstEmpty() == -1) {

                for (int i = 0; i < items.get(item); i++) {
                    main.getStashManager().addItemToStash(player, item.build());
                }

            //if the player's inventory isn't full, add the item to their inventory the specified amount of times
            } else {
                for (int i = 0; i < items.get(item); i++) {
                    player.getInventory().addItem(item.build());
                }
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
