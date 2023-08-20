package com.stelios.cakenaysh.Listeners.Inventory;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Managers.StatsManager;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryAlteredListener implements Listener {

    private final Main main;
    private final StatsManager statsManager;

    public InventoryAlteredListener(Main main, StatsManager statsManager){
        this.main = main;
        this.statsManager = statsManager;
    }

    //disallow the player to equip items that they don't have the required stats for
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        //if the inventory isn't null
        if (e.getClickedInventory() != null) {

            //if the inventory is that of a players
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {

                //get the player and the item
                Player player = (Player) e.getWhoClicked();
                ItemStack item = e.getCursor();

                //if the player doesn't have the required stats for the item
                if (!statsManager.meetsItemRequirements(player, item, false)) {

                    //if the item is being moved into the armor slots
                    if (e.getSlotType() == InventoryType.SlotType.ARMOR) {

                        //cancel the event
                        player.sendMessage(Component.text("You do not meet the requirements to equip this item.", TextColor.color(255, 0, 0)));
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    //remove the stats from the main hand item when any inventory is opened
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){

        //get the player and their main hand item
        Player player = (Player) e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //remove the stats from the main hand item
        statsManager.removePlayerStats(player, item, "weapon");
    }


    //update player stats when an item is moved into the main hand
    @EventHandler
    public void onInventorySlotChange(PlayerInventorySlotChangeEvent e){

        Player player = e.getPlayer();

        //if the item is being moved into or out of the main hand
        if (e.getSlot() == player.getInventory().getHeldItemSlot()){

            //add the stats of the new item
            statsManager.addPlayerStats(player, e.getNewItemStack(), "weapon");

            //remove the stats of the old item
            statsManager.removePlayerStats(player, e.getOldItemStack(), "weapon");

            //if the item is being moved into or out of the offhand
        } else if (e.getSlot() == 40){

            //remove the stats of the old item
            statsManager.removePlayerStats(player, e.getOldItemStack(), "accessory");

            //if the player doesn't meet the requirements for the new item
            if (!statsManager.meetsItemRequirements(player, e.getNewItemStack(), false)){

                //remove the item from the offhand
                if (player.getInventory().getItem(EquipmentSlot.OFF_HAND).equals(e.getNewItemStack())) {
                    player.getInventory().setItemInOffHand(null);
                }

                //send the player a message
                player.sendMessage(Component.text("You do not meet the requirements to equip this item.", TextColor.color(255, 0, 0)));

                //wait a second
                new BukkitRunnable(){
                    @Override
                    public void run() {

                        //if the player's inventory isn't full add the item to their inventory
                        if (player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(e.getNewItemStack());

                        //add the item to the player's stash
                        }else{
                            main.getStashManager().addItemToStash(player, e.getNewItemStack());
                        }
                    }
                }.runTaskLater(main, 20);

                //if the player meets the requirements for the new item
            } else {
                //add the stats of the new item
                statsManager.addPlayerStats(player, e.getNewItemStack(), "accessory");
            }
        }
    }

    //update player stats when an item is equipped
    @EventHandler
    public void onChangedHeldItem(PlayerItemHeldEvent e){

        Player player = e.getPlayer();

        //remove the stats from the item they were holding
        statsManager.removePlayerStats(player, player.getInventory().getItem(e.getPreviousSlot()), "weapon");

        //add the stats of the new item being held
        statsManager.addPlayerStats(player, player.getInventory().getItem(e.getNewSlot()), "weapon");
    }

    //update player stats when armor is equipped
    @EventHandler
    public void onEquipArmor(PlayerArmorChangeEvent e){

        Player player = e.getPlayer();

        //if the player doesn't have the required proficiencies for the item, put it in their inventory
        if (!statsManager.meetsItemRequirements(player, e.getNewItem(), false)){

            //remove the item from the player's armor slot
            if (player.getInventory().getItem(EquipmentSlot.HEAD).equals(e.getNewItem())) {
                player.getInventory().setHelmet(null);
            }else if (player.getInventory().getItem(EquipmentSlot.CHEST).equals(e.getNewItem())) {
                player.getInventory().setChestplate(null);
            }else if (player.getInventory().getItem(EquipmentSlot.LEGS).equals(e.getNewItem())) {
                player.getInventory().setLeggings(null);
            }else if (player.getInventory().getItem(EquipmentSlot.FEET).equals(e.getNewItem())) {
                player.getInventory().setBoots(null);
            }

            //send the player a message
            player.sendMessage(Component.text("You do not meet the requirements to equip this item.", TextColor.color(255, 0, 0)));

            //wait a second
            new BukkitRunnable(){
                @Override
                public void run() {

                    //if the player's inventory isn't full add the item to their inventory
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(e.getNewItem());

                        //add the item to the player's stash
                    }else{
                        main.getStashManager().addItemToStash(player, e.getNewItem());
                    }
                }
            }.runTaskLater(main, 20);

        }else{

            statsManager.addPlayerStats(player, e.getNewItem(), "armor");
        }

        statsManager.removePlayerStats(player, e.getOldItem(), "armor");
        statsManager.calculateEquipmentBonuses(player);
    }

}
