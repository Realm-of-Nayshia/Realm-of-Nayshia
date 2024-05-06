package com.stelios.RealmOfNayshia.Managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.stelios.RealmOfNayshia.Main;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bson.Document;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerItemManager {

    private final Main main = Main.getPlugin(Main.class);
    private final MongoCollection<Document> playerItems = main.getDatabase().getPlayerItems();


    //creates an item file for the player
    public void createItemFile(Player player){

        if (!(playerItems.find(new Document("uuid", player.getUniqueId().toString())).first() == null)){
            return;
        }

        //create a new item file
        playerItems.insertOne(new Document("uuid", player.getUniqueId().toString()).append("name", player.getName())
                .append("inventory", new ArrayList<String>()));
    }


    //updates the player's items
    public void updateItemFile(Player player){

        //get the player's items
        ArrayList<String> items = new ArrayList<>();
        ItemStack[] itemStacks = player.getInventory().getContents();

        //loop through the player's items
        for (ItemStack item : itemStacks) {
            if (item == null) {
                continue;
            }

            //if the item is a custom item, add its unique name to the list
            if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "itemType"))){
                items.add(PlainTextComponentSerializer.plainText().serialize(item.displayName()) + ":" + item.getAmount());

            //if the item is a base material, add its material name to the list
            } else {
                items.add(item.getType() + ":" + item.getAmount());
            }
        }

        //update the player's items
        playerItems.updateOne(Filters.eq("uuid", player.getUniqueId().toString()),
                new Document("$set", new Document("inventory", items)));
    }

}
