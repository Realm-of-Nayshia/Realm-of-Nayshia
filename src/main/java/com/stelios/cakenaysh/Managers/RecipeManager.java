package com.stelios.cakenaysh.Managers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.stelios.cakenaysh.Items.Recipes;
import com.stelios.cakenaysh.Main;
import org.bson.Document;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class RecipeManager {

    private final Main main = Main.getPlugin(Main.class);
    private final MongoCollection<Document> playerRecipes = main.getDatabase().getPlayerRecipes();

    //get the player's list of recipes
    public ArrayList<String> getRecipes(Player player){

        try {
            if (playerRecipes.find(new Document("uuid", player.getUniqueId().toString())).first().getList("recipes", String.class) == null) {
                return new ArrayList<>();
            }
        } catch (NullPointerException e){
            return new ArrayList<>();
        }

        return (ArrayList<String>) Objects.requireNonNull(playerRecipes.find(new Document("uuid", player.getUniqueId().toString())).first()).getList("recipes", String.class);
    }

    //create a recipes file for the player and remove all their recipes
    public void createRecipeFile(Player player){

        //if the player already has a recipes file
        if (!(playerRecipes.find(new Document("uuid", player.getUniqueId().toString())).first() == null)){
            return;
        }

        //create a new recipes file
        playerRecipes.insertOne(new Document("uuid", player.getUniqueId().toString()).append("name", player.getName()).append("recipes", new ArrayList<String>()));

        //reset the player's recipes
        for (NamespacedKey recipe : player.getDiscoveredRecipes()) {
            player.undiscoverRecipe(recipe);
        }
    }

    //add a recipe to the player's list
    public void addRecipe(Player player, String recipe){

        //current recipes
        ArrayList<String> recipes = getRecipes(player);
        recipes.add(recipe);

        //update the player's recipes
        playerRecipes.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("recipes", recipes)));
        player.discoverRecipe(new NamespacedKey(main, recipe));
    }

    //remove a recipe from the player's list
    public void removeRecipe(Player player, String recipe){

        //current recipes
        ArrayList<String> recipes = getRecipes(player);
        recipes.remove(recipe);

        //update the player's recipes
        playerRecipes.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("recipes", recipes)));
        player.undiscoverRecipe(new NamespacedKey(main, recipe));
    }

    //check if the player has a recipe
    public boolean hasRecipe(Player player, String recipe){
        return getRecipes(player).contains(recipe);
    }

    //add all recipes to the player
    public void addAllRecipes(Player player){

        for (Recipes recipe : Recipes.values()) {
            if (!hasRecipe(player, recipe.getKey().getKey())) {
                addRecipe(player, recipe.getKey().getKey());
            }
        }
    }

    //reset all recipes from the player
    public void resetRecipes(Player player){
        for (Recipes recipe : Recipes.values()) {
            if (hasRecipe(player, recipe.getKey().getKey())) {
                removeRecipe(player, recipe.getKey().getKey());
            }
        }
    }
}
