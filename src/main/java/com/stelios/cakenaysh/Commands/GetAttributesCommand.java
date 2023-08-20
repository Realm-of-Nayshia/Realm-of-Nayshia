package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetAttributesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if there are the correct # of args
        if (args.length == 1) {

            //if the target player is online
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))) {

                //get the player
                Player player = Bukkit.getServer().getPlayer(args[0]);
                assert player != null;

                //get the main class
                Main main = Main.getPlugin(Main.class);

                //return the player's attributes
                if (sender instanceof Player) {
                    sender.sendMessage(Component.text("\n" + player.getName() + "'s attributes: \n" + main.getPlayerManager().getCustomPlayer(player.getUniqueId()).getAttributes(), TextColor.color(0,255,0)));
                } else {
                    System.out.println("\n" + player.getName() + "'s attributes: \n" + main.getPlayerManager().getCustomPlayer(player.getUniqueId()).getAttributes());
                }

            }else{
                //error: player not online
                if (sender instanceof Player) {
                    sender.sendMessage(Component.text("That player is not online.", TextColor.color(255,0,0)));
                } else {
                    System.out.println("That player is not online.");
                }
            }

        }else{
            //error: incorrect usage
            if (sender instanceof Player) {
                sender.sendMessage(Component.text("Incorrect usage! Use /getattributes <player>.", TextColor.color(255,0,0)));
            } else {
                System.out.println("Incorrect usage! Use /getattributes <player>.");
            }
        }

        return false;

    }
}
