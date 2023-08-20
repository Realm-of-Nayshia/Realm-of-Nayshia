package com.stelios.cakenaysh.MenuCreation.Menus;

import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Items.Recipes;
import com.stelios.cakenaysh.MenuCreation.Menu;
import com.stelios.cakenaysh.MenuCreation.MenuButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerInfoRecipeDisplay extends Menu {

    public PlayerInfoRecipeDisplay(Recipes recipe) {
        super(Component.text("Recipes", TextColor.color(0,0,0), TextDecoration.BOLD), 6);

        ////registering clickable buttons
        //back button
        MenuButton backButton = new MenuButton(CustomItems.BACK_BUTTON.getItem().build());
        backButton.setWhenClicked(clicked -> {
            new PlayerInfoRecipes(clicked).open(clicked);
        });
        registerButton(backButton, 45);

        //close button
        MenuButton closeButton = new MenuButton(CustomItems.CLOSE.getItem().build());
        closeButton.setWhenClicked(clicked -> clicked.closeInventory());
        registerButton(closeButton, 49);


        ////registering unclickable buttons
        //equal sign
        MenuButton equalSign = new MenuButton(CustomItems.EQUAL_SIGN.getItem().build());
        registerButton(equalSign, 23);

        //recipe result
        MenuButton recipeResult = new MenuButton(recipe.getResult());
        registerButton(recipeResult, 24);

        //set the ingredients
        StringBuilder shape = new StringBuilder();
        for (String string : recipe.getShape()){
            shape.append(string);
        }

        Map<Character, ItemStack> ingredients = recipe.getIngredients();

        //loop through the shape
        for (int i = 0; i < shape.length(); i++){

            //if the current character is not a space
            if (shape.charAt(i) != ' '){

                //get the ingredient
                ItemStack ingredient = ingredients.get(shape.charAt(i));

                //if the ingredient is not null
                if (ingredient != null){

                    //location of the ingredient
                    int index = 0;
                    switch (i) {
                        case 0: index = 11; break;
                        case 1: index = 12; break;
                        case 2: index = 13; break;
                        case 3: index = 20; break;
                        case 4: index = 21; break;
                        case 5: index = 22; break;
                        case 6: index = 29; break;
                        case 7: index = 30; break;
                        case 8: index = 31; break;
                    }

                    //register the ingredient
                    MenuButton ingredientButton = new MenuButton(ingredient);
                    registerButton(ingredientButton, index);
                }
            }
        }


        //set blank white panes for any of the 9 leftover crafting slots
        for (int i : new int[]{11,12,13,20,21,22,29,30,31}) {
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_LIGHT_GRAY_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }

        //set blank black panes for the rest of the inventory
        for (int i = 0; i < this.getInventory().getSize(); i++){
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_BLACK_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }




    }

}
