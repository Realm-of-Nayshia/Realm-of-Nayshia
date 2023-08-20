package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Events.ProficiencyChangedEvent;
import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetAttributesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if there are the correct # of args
        if (args.length == 1){

            //if the target player is online
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))) {

                //get the player
                Player player = Bukkit.getServer().getPlayer(args[0]);
                assert player != null;

                //get the main class
                Main main = Main.getPlugin(Main.class);

                //fire the proficiency changed event
                Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "all"));

                //reset the player's attributes
                main.getPlayerManager().getCustomPlayer(player.getUniqueId()).resetAttributes(player);

                //confirmation message
                if (sender instanceof Player) {
                    sender.sendMessage(Component.text(player.getName() + " has been reset!", TextColor.color(0,255,0)));
                } else {
                    System.out.println(player.getName() + " has been reset!");
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
                sender.sendMessage(Component.text("Incorrect usage! Use /resetattributes <player>.", TextColor.color(255,0,0)));
            } else {
                System.out.println("Incorrect usage! Use /resetattributes <player>.");
            }
        }

        return false;

    }
}
