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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {

    private List<UUID> vanished = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            if (vanished.contains(player.getUniqueId())){
                vanished.remove(player.getUniqueId());
                for (Player target : Bukkit.getOnlinePlayers()){
                    target.showPlayer(Main.getPlugin(Main.class),player);
                }
                player.sendMessage(Component.text("You are no longer vanished!", TextColor.color(255,0,0)));

            }else{
                vanished.add(player.getUniqueId());
                for (Player target : Bukkit.getOnlinePlayers()){
                    target.hidePlayer(Main.getPlugin(Main.class), player);
                }
                player.sendMessage(Component.text("You are vanished!", TextColor.color(0,255,0)));
            }
        }

        return false;
    }
}
