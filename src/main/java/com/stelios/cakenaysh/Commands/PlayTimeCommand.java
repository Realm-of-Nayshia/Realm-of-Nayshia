package com.stelios.cakenaysh.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayTimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is a player
        if (sender instanceof Player){
            Player player = (Player) sender;

            //if there are no arguments
            if (args.length == 0){
                player.sendMessage(Component.text("You have played for " + (float) ((int) ((float) player.getStatistic(Statistic.PLAY_ONE_MINUTE) / (20*60*60) * 10)) / 10 + " hours.", TextColor.color(0,255, 0)));

            //error: invalid syntax
            }else{
                player.sendMessage(Component.text("Invalid syntax. Use /playtime", TextColor.color(255, 0, 0)));
            }
        }

        return false;
    }
}
