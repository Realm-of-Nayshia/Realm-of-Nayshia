package com.stelios.cakenaysh.Commands.TabComplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ResetAttributesTabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //first argument: player name
        if (args.length == 1){

            //add all online players to the list
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()){
                names.add(player.getName());
            }

            return StringUtil.copyPartialMatches(args[0], names, new ArrayList<>());
        }

        //return an empty list
        return new ArrayList<>();
    }
}
