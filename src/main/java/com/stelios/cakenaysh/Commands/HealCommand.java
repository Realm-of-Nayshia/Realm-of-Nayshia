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

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is a player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //get the main class
            Main main = Main.getPlugin(Main.class);

            //if there is no target for the command, heal the player
            if (args.length == 0) {
                CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());
                customPlayer.setHealth(customPlayer.getMaxHealth());
                main.getStatsManager().displayActionBar(player);
                main.getStatsManager().updateHearts(player);
                player.sendMessage(Component.text("You have been healed!", TextColor.color(0, 255, 0)));

            //if there is a valid target for the command, heal the target
            } else if (args.length == 1) {
                if(Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))){
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(target.getUniqueId());
                    main.getStatsManager().displayActionBar(player);
                    main.getStatsManager().updateHearts(player);
                    customPlayer.setHealth(customPlayer.getMaxHealth());
                    player.sendMessage(Component.text("You have healed " + target.getName() + "!", TextColor.color(0, 255, 0)));
                    target.sendMessage(Component.text("You have been healed!", TextColor.color(0, 255, 0)));
                }else{
                    player.sendMessage(Component.text("Target player isn't online.", TextColor.color(255, 0, 0)));
                }

            }else{
                player.sendMessage(Component.text("Invalid arguments! Use /heal <player>.", TextColor.color(255, 0, 0)));
            }
        }
        return false;
    }
}
