package com.stelios.cakenaysh.MenuCreation.Menus;

import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Items.Recipes;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.MenuCreation.Menu;
import com.stelios.cakenaysh.MenuCreation.MenuButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerInfoRecipes extends Menu {

    public PlayerInfoRecipes(Player player) {
        super(Component.text("Recipes", TextColor.color(0,0,0), TextDecoration.BOLD), 6);

        ////registering unclickable buttons
        //recipe book
        MenuButton recipeBookButton = new MenuButton(CustomItems.RECIPE_BOOK.getItem().build());
        registerButton(recipeBookButton, 4);

        //blank black pane frame
        for (int i : new int[]{0,1,2,3,5,6,7,8,9,17,18,26,27,35,36,44,46,47,48,50,51,52,53}){

            //if there is no button registered in the current inventory slot, register a blank pane
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_BLACK_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }


        ////registering clickable buttons
        //back button
        MenuButton backButton = new MenuButton(CustomItems.BACK_BUTTON.getItem().build());
        backButton.setWhenClicked(clicked -> {
            new PlayerInfoMain(clicked).open(clicked);
        });
        registerButton(backButton, 45);

        //close button
        MenuButton closeButton = new MenuButton(CustomItems.CLOSE.getItem().build());
        closeButton.setWhenClicked(clicked -> clicked.closeInventory());
        registerButton(closeButton, 49);


        //get the player's recipes
        ArrayList<String> playerRecipes =  Main.getPlugin(Main.class).getRecipeManager().getRecipes(player);

        //if the player has recipes
        if (!playerRecipes.isEmpty()){

            //loop through the recipe enum
            for (Recipes recipe : Recipes.values()) {

                //if the player has the recipe
                if (playerRecipes.contains(recipe.getRecipe().getKey().getKey())) {

                    //create a button for the recipe
                    MenuButton recipeButton = new MenuButton(recipe.getRecipe().getResult());
                    recipeButton.setWhenClicked(clicked -> {
                        new PlayerInfoRecipeDisplay(recipe).open(clicked);
                    });

                    //loop through the inventory slots
                    for (int i = 0; i < this.getInventory().getSize(); i++) {

                        //register the button if there is no button registered in the current inventory slot
                        if (!this.getButtonMap().containsKey(i)) {
                            registerButton(recipeButton, i);
                            break;
                        }
                    }
                }
            }
        }


        //fill the locked recipes with locked red panes
        int registeredRecipes = 0;

        for (int i = 0; i < this.getInventory().getSize(); i++){

            //if there is no button registered in the current inventory slot
            if (!this.getButtonMap().containsKey(i)) {

                //if there are recipes that the player doesn't have unlocked
                if (Recipes.values().length - (playerRecipes.size() + registeredRecipes) > 0) {

                    //create a locked red pane
                    MenuButton blankPane = new MenuButton(CustomItems.LOCKED_RED_PANE.getItem().build());
                    registerButton(blankPane, i);
                    registeredRecipes++;
                }
            }
        }


        //filling the rest with blank panes
        for (int i = 0; i < this.getInventory().getSize(); i++){

            //if there is no button registered in the current inventory slot, register a blank pane
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_BLACK_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }

    }

}
