package com.stelios.cakenaysh.MenuCreation;

import com.stelios.cakenaysh.Managers.MenuManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private Inventory inventory;
    private Map<Integer, MenuButton> buttonMap;
    private Consumer<Player> inventoryClosed;
    private Consumer<Player> inventoryOpened;

    //menu constructor
    //@param title: the title of the menu
    //@param rows: the amount of rows the menu has
    public Menu(Component title, int rows){

        //checks if the menu is valid
        if (rows > 6 || rows < 1){
            throw new IllegalArgumentException("Invalid menu creation parameters");
        }

        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        this.buttonMap = new HashMap<>();
    }

    //getters
    public Inventory getInventory(){
        return inventory;
    }
    public Map<Integer, MenuButton> getButtonMap(){
        return buttonMap;
    }

    //registers the button in the specified slot
    public void registerButton(MenuButton button, int slot){
        buttonMap.put(slot, button);
    }

    //setting the opening/closing status of the inventory
    public void setInventoryClosed(Consumer<Player> inventoryClosed){
        this.inventoryClosed = inventoryClosed;
    }
    public void setInventoryOpened(Consumer<Player> inventoryOpened){
        this.inventoryOpened = inventoryOpened;
    }

    //handling the opening/closing of the inventory
    //@param player: the player who opened/closed the inventory
    public void handleClosed(Player player){
        if (inventoryClosed != null){
            inventoryClosed.accept(player);
        }
    }
    public void handleOpened(Player player){
        if (inventoryOpened != null){
            inventoryOpened.accept(player);
        }
    }

    //handling the click of a button
    public void handleClick(InventoryClickEvent e){
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();

        if (clicked == null){
            return;
        }

        if (buttonMap.containsKey(e.getRawSlot())){

            //clicked on a valid button
            Consumer<Player> consumer = buttonMap.get(e.getRawSlot()).getWhenClicked();

            //does the clicked button have an action associated with it
            if (consumer != null){
                consumer.accept((Player) e.getWhoClicked());
            }
        }
    }

    //opens the inventory to a specified player
    public void open(Player player){
        MenuManager manager = MenuManager.getInstance();

        buttonMap.forEach((slot, button) -> {
            inventory.setItem(slot, button.getItemStack());
        });

        //open the inventory and handle the open event
        player.openInventory(inventory);
        manager.registerMenu(player.getUniqueId(), this);
        handleOpened(player);
    }

}
