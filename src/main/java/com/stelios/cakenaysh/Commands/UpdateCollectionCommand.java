package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateCollectionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is not a player, return false
        if (!(sender instanceof Player)){
            return false;
        }

        //if there is more than one argument, return false
        if (args.length > 1){
            sender.sendMessage(Component.text("Too many arguments!", TextColor.color(255,0,0)));
            return false;
        }

        //get the main class
        Main main = Main.getPlugin(Main.class);

        //if there is one argument, update the specified collection
        if (args.length == 1){

            switch (args[0].toLowerCase()){
                case "stats":
                    //update each player's stats
                    sender.sendMessage(Component.text("Updating stats...", TextColor.color(0,255,0)));
                    main.getStatsManager().updateDatabaseStatsAll();
                    sender.sendMessage(Component.text("Stats updated!", TextColor.color(0,255,0)));

                    break;
                case "items":
                    //update each player's current items
                    sender.sendMessage(Component.text("Updating items...", TextColor.color(0,255,0)));
                    //insert code here
                    sender.sendMessage(Component.text("Items updated!", TextColor.color(0,255,0)));
                    break;
                default:
                    sender.sendMessage(Component.text("Invalid argument!", TextColor.color(255,0,0)));
                    return false;
            }
            return true;
        }

        //if there are no arguments, update every database collection
        sender.sendMessage(Component.text("Updating collections...", TextColor.color(0,255,0)));
        main.getStatsManager().updateDatabaseStatsAll();
        //update the player's current items, insert code here
        sender.sendMessage(Component.text("Collections updated!", TextColor.color(0,255,0)));
        return true;
    }
}
