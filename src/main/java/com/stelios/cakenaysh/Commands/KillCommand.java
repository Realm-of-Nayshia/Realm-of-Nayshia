package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KillCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is a player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //get the main class
            Main main = Main.getPlugin(Main.class);

            //if there is no target for the command, kill the player
            if (args.length == 0) {
                CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());
                customPlayer.setHealth(0);
                main.getStatsManager().displayActionBar(player);
                main.getStatsManager().updateHearts(player);

            //if there is a valid target for the command, kill the target
            } else if (args.length == 1) {
                if(Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))){
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(target.getUniqueId());
                    customPlayer.setHealth(0);
                    main.getStatsManager().displayActionBar(target);
                    main.getStatsManager().updateHearts(target);
                }else{
                    player.sendMessage(Component.text("Target player isn't online.", TextColor.color(255, 0, 0)));
                }

            }else{
                player.sendMessage(Component.text("Invalid arguments! Use /kill <player>.", TextColor.color(255, 0, 0)));
            }
        }
        return false;
    }
}
