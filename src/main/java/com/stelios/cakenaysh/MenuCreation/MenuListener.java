package com.stelios.cakenaysh.MenuCreation;

import com.stelios.cakenaysh.Managers.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e){

        Menu matchedMenu = MenuManager.getInstance().matchMenu(e.getWhoClicked().getUniqueId());

        if (matchedMenu != null){
            e.setCancelled(true);
            matchedMenu.handleClick(e);
        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e){

        Menu matchedMenu = MenuManager.getInstance().matchMenu(e.getPlayer().getUniqueId());

        if (matchedMenu != null){
            matchedMenu.handleClosed((Player) e.getPlayer());
        }

        //unregister the menu
        MenuManager.getInstance().unregisterMenu(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        Menu matchedMenu = MenuManager.getInstance().matchMenu(e.getPlayer().getUniqueId());

        if (matchedMenu != null){
            matchedMenu.handleClosed(e.getPlayer());
        }

        //unregister the menu
        MenuManager.getInstance().unregisterMenu(e.getPlayer().getUniqueId());
    }

}
