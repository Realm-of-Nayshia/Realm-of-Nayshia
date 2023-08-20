package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Items.Recipes;
import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RecipeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (sender instanceof Player) {

            //if there are the correct # of args
            if (args.length == 2 || args.length == 3) {

                //if the target player is online
                if (Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))) {

                    //get the targeted player
                    Player player = Bukkit.getServer().getPlayer(args[0]);
                    assert player != null;

                    Main main = Main.getPlugin(Main.class);


                    switch (args[1]) {

                        case "add":
                            //for each recipe
                            for (Recipes recipe : Recipes.values()) {

                                //if the recipe matches the inputted recipe
                                if (recipe.getKey().getKey().equalsIgnoreCase(args[2])) {

                                    //if the player has recipes unlocked
                                    if (!main.getRecipeManager().getRecipes(player).isEmpty()) {

                                        //check if the player already has the recipe
                                        if (main.getRecipeManager().hasRecipe(player, recipe.getKey().getKey())) {
                                            sender.sendMessage(Component.text("That player already has that recipe.", TextColor.color(255, 0, 0)));
                                            return false;
                                        }
                                    }

                                    //add the recipe to the player
                                    main.getRecipeManager().addRecipe(player, recipe.getKey().getKey());
                                    sender.sendMessage(Component.text("Recipe added.", TextColor.color(0, 255, 0)));
                                    return true;
                                }
                            }

                            sender.sendMessage(Component.text("That recipe does not exist.", TextColor.color(255, 0, 0)));
                            break;


                        case "remove":
                            //if the player has no recipes unlocked
                            if (main.getRecipeManager().getRecipes(player).isEmpty()) {
                                sender.sendMessage(Component.text("That player does not have any recipes unlocked.", TextColor.color(255, 0, 0)));
                                return false;
                            }

                            //for each recipe
                            for (String key : main.getRecipeManager().getRecipes(player)) {

                                //if the recipe matches the inputted recipe
                                if (key.equalsIgnoreCase(args[2])) {

                                    //remove the recipe from the player
                                    main.getRecipeManager().removeRecipe(player, key);
                                    sender.sendMessage(Component.text("Recipe removed.", TextColor.color(0, 255, 0)));
                                    return true;
                                }
                            }
                            sender.sendMessage(Component.text("That player does not have that recipe.", TextColor.color(255, 0, 0)));
                            break;


                        case "all":
                            //add the player all recipes
                            main.getRecipeManager().addAllRecipes(player);
                            sender.sendMessage(Component.text("All recipes added.", TextColor.color(0, 255, 0)));
                            break;


                        case "reset":
                            //remove all recipes from the player
                            main.getRecipeManager().resetRecipes(player);
                            sender.sendMessage(Component.text("All recipes removed.", TextColor.color(0, 255, 0)));
                            break;


                        default:
                            //error: incorrect usage
                            sender.sendMessage(Component.text("Incorrect usage. /recipe <player> <add/remove/all/reset> <recipe>", TextColor.color(255, 0, 0)));
                            break;
                    }

                //error: target player is not online
                } else {
                    sender.sendMessage(Component.text("That player is not online.", TextColor.color(255,0,0)));
                }

            //error: incorrect usage
            } else {
                sender.sendMessage(Component.text("Incorrect usage. /recipe <player> <add/remove/all/reset> <recipe>", TextColor.color(255,0,0)));
            }

        //error: sender is not a player
        }

        return false;
    }
}
