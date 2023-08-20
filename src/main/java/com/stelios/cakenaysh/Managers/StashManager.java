package com.stelios.cakenaysh.Managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class StashManager {

    private final Main main = Main.getPlugin(Main.class);
    private final MongoCollection<Document> playerStashes = main.getDatabase().getPlayerStashes();

    //creates a stash for a player
    public void createStash(Player player){

        //if the player doesn't have a stash already
        if (!(playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first() == null)){
            return;
        }

        //create a new stash
        playerStashes.insertOne(new Document("uuid", player.getUniqueId().toString()).append("name", player.getName()).append("stash", new ArrayList<String>()));
    }

    //adds an item to the player's stash
    public void addItemToStash(Player player, ItemStack item){

        //get the player's stash
        ArrayList<String> stash = (ArrayList<String>) playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first().get("stash");

        //if the item is a custom item
        if (CustomItems.getNameFromItem(item) != null) {

            //add the custom item's name to the stash
            stash.add(CustomItems.getNameFromItem(item));

        //if the item is a base material
        } else {
            //add the item's material to the stash
            stash.add(item.getType().toString());
        }

        //update the player's stash
        playerStashes.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("stash", stash)));

        //send the player a message
        player.sendMessage(Component.text("Your inventory is full. Your stash has " + Objects.requireNonNull(playerStashes.find(
                Filters.eq("uuid", player.getUniqueId().toString())).first()).getList("stash", String.class).size()
                + " items.\nType /stash to collect your items.", TextColor.color(255, 0, 0)));
    }


    //adds multiple items to the player's stash
    public void addItemsToStash(Player player, ArrayList<ItemStack> items){

        //get the player's stash
        ArrayList<String> stash = (ArrayList<String>) playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first().get("stash");

        //loop through the items
        for (ItemStack item : items) {

            //if the item is a custom item
            if (CustomItems.getNameFromItem(item) != null) {

                //add the custom item's name to the stash
                stash.add(CustomItems.getNameFromItem(item));

            //if the item is a base material
            } else {
                //add the item's material to the stash
                stash.add(item.getType().toString());
            }
        }

        //update the player's stash
        playerStashes.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("stash", stash)));

        //send the player a message
        player.sendMessage(Component.text("Your inventory is full. Your stash has " + Objects.requireNonNull(playerStashes.find(
                Filters.eq("uuid", player.getUniqueId().toString())).first()).getList("stash", String.class).size()
                + " items.\nType /stash to collect your items.", TextColor.color(255, 0, 0)));
    }


    //removes an item from the player's stash
    public void removeItemFromStash(Player player, ItemStack item){

            //get the player's stash
            ArrayList<String> stash = (ArrayList<String>) playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first().get("stash");

            //if the item is a custom item
            if (CustomItems.getNameFromItem(item) != null) {

                //add the custom item's name to the stash
                stash.remove(CustomItems.getNameFromItem(item));

            //if the item is a base material
            } else {
                //add the item's material to the stash
                stash.remove(item.getType().toString());
            }

            //update the player's stash
        playerStashes.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("stash", stash)));
    }


    //removes multiple items from the player's stash
    public void removeItemsFromStash(Player player, ArrayList<ItemStack> items){

        //get the player's stash
        ArrayList<String> stash = (ArrayList<String>) playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first().get("stash");

        //loop through the items
        for (ItemStack item : items) {

            //if the item is a custom item
            if (CustomItems.getNameFromItem(item) != null) {

                //remove the custom item's name from the stash
                stash.remove(CustomItems.getNameFromItem(item));

            //if the item is a base material
            } else {
                //remove the item's material from the stash
                stash.remove(item.getType().toString());
            }
        }

        //update the player's stash
        playerStashes.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("stash", stash)));
    }

}
