package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Items.CustomItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if there are the correct # of args
        if (args.length == 3 || args.length == 2) {

            //if the target player is online
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))) {

                //get the player
                Player player = Bukkit.getServer().getPlayer(args[0]);

                //create a debounce variable
                boolean db = true;

                //if the amount is specified
                if (args.length == 3) {

                    //if the amount is a number then set value to true
                    try {
                        Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        db = false;
                        if (sender instanceof Player) {
                            sender.sendMessage(Component.text("Invalid amount.", TextColor.color(255,0,0)));
                        } else {
                            System.out.println("Invalid amount.");
                        }
                    }
                }

                //if debounce is true
                if(db) {

                    //if the item exists
                    if (CustomItems.getItemFromName(args[1]) != null) {

                        //play a sound,give the items, and send a message
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

                        if (args.length == 3) {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                player.getInventory().addItem(CustomItems.getItemFromName(args[1]).build());
                            }
                        } else {
                            player.getInventory().addItem(CustomItems.getItemFromName(args[1]).build());
                        }

                        if (sender instanceof Player) {
                            sender.sendMessage(Component.text("Custom item(s) were given.", TextColor.color(0,255,0)));
                        } else {
                            System.out.println("Custom item(s) were given.");
                        }

                        //if the item doesn't exist send an error message
                    } else {
                        if (sender instanceof Player) {
                            sender.sendMessage(Component.text("Custom item not found.", TextColor.color(255,0,0)));
                        } else {
                            System.out.println("Custom item not found.");
                        }
                    }
                }

                //if the target player is not online send an error message
            }else {
                if (sender instanceof Player) {
                    sender.sendMessage(Component.text("Player not found.", TextColor.color(255,0,0)));
                } else {
                    System.out.println("Player not found.");
                }
            }

            //if there are not the correct # of args send an error message
        }else{
            if (sender instanceof Player) {
                sender.sendMessage(Component.text("Incorrect usage! Use /giveitem <player> <item> <amount>.", TextColor.color(255,0,0)));
            } else {
                System.out.println("Incorrect usage! Use /giveitem <player> <item> <amount>.");
            }
        }
        return false;
    }

}