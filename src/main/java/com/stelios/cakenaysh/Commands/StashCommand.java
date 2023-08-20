package com.stelios.cakenaysh.Commands;

import com.mongodb.client.MongoCollection;
import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class StashCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is a player
        if (sender instanceof Player){

            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;

            //get the stash collection
            MongoCollection<Document> playerStashes = main.getDatabase().getPlayerStashes();

            //if the player doesn't have a stash, send them a message
            if (playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first() == null) {
                player.sendMessage(Component.text("Your stash is empty.", TextColor.color(0, 255, 0)));
                return false;
            }

            //get the player's stash
            ArrayList<String> stash = (ArrayList<String>) Objects.requireNonNull(playerStashes.find(new Document("uuid", player.getUniqueId().toString())).first()).getList("stash", String.class);

            //if the player has nothing in the stash
            if (stash == null || stash.isEmpty()) {
                player.sendMessage(Component.text("Your stash is empty.", TextColor.color(0, 255, 0)));
                return false;
            }

            //if the player has item(s) in the stash
            int emptySlots = 0;

            //if items are in the armor or offhand slots, subtract 1 from the empty slots
            if (player.getInventory().getItem(EquipmentSlot.HEAD).getType() == Material.AIR) {
                emptySlots--;
            }
            if (player.getInventory().getItem(EquipmentSlot.CHEST).getType() == Material.AIR) {
                emptySlots--;
            }
            if (player.getInventory().getItem(EquipmentSlot.LEGS).getType() == Material.AIR) {
                emptySlots--;
            }
            if (player.getInventory().getItem(EquipmentSlot.FEET).getType() == Material.AIR) {
                emptySlots--;
            }
            if (player.getInventory().getItem(EquipmentSlot.OFF_HAND).getType() == Material.AIR) {
                emptySlots--;
            }

            //loop through the player's inventory and count the empty slots
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null || item.getType().equals(Material.AIR)) {
                    emptySlots++;
                }
            }

            //array list of items to remove from the stash
            ArrayList<ItemStack> itemsToRemove = new ArrayList<>();

            //boolean that determines if the player has enough space in their inventory
            boolean hasEnoughSpace = true;

            //loop through the stash and give the player the items
            for (String itemString : stash) {
                if (emptySlots > 0) {

                    //if the item is a custom item
                    if (CustomItems.getNames().contains(itemString)) {
                        ItemStack item = Objects.requireNonNull(CustomItems.getItemFromName(itemString)).getItemStack();

                        //give the player the item
                        player.getInventory().addItem(item);
                        itemsToRemove.add(item);

                    //if the item is a base material
                    } else {
                        ItemStack item = new ItemStack(Material.valueOf(itemString));

                        //give the player the material
                        player.getInventory().addItem(item);
                        itemsToRemove.add(item);
                    }
                    emptySlots--;

                } else {
                    hasEnoughSpace = false;
                    player.sendMessage(Component.text("Not enough space. Items left: " + (stash.size() - itemsToRemove.size()), TextColor.color(255, 0, 0)));
                    break;
                }
            }

            //remove the items from the stash
            main.getStashManager().removeItemsFromStash(player, itemsToRemove);

            if (hasEnoughSpace) {
                player.sendMessage(Component.text("Successfully retrieved items from the stash.", TextColor.color(0, 255, 0)));
            }
        }
        return false;
    }
}
