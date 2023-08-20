package com.stelios.cakenaysh.Managers;

import com.stelios.cakenaysh.MenuCreation.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    private final Map<UUID, Menu> openMenus;
    private static MenuManager instance;

    public MenuManager(){
        openMenus = new HashMap<>();
    }

    public static MenuManager getInstance(){
        if(instance == null){
            instance = new MenuManager();
        }
        return instance;
    }

    //register a menu to the user
    public void registerMenu(UUID uuid, Menu menu){
        openMenus.put(uuid, menu);
    }

    //unregister a menu from the user
    public void unregisterMenu(UUID uuid){
        openMenus.remove(uuid);
    }

    //find a menu
    public Menu matchMenu(UUID uuid){
        return openMenus.get(uuid);
    }
}
