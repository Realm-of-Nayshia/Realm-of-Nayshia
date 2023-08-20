package com.stelios.cakenaysh.Listeners.Entity;

import com.stelios.cakenaysh.Items.Recipes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

public class PlayerDiscoverRecipeEvent implements Listener {

    @EventHandler
    public void onPlayerDiscoverRecipe(PlayerRecipeDiscoverEvent e) {

        //if the discovered recipe is a custom recipe, do nothing
        for (Recipes recipe : Recipes.values()){
            if (e.getRecipe().equals(recipe.getKey())){
                return;
            }
        }

        //if the recipe is not a custom recipe, cancel the event
        e.setCancelled(true);
    }

}
