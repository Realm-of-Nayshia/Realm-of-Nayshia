package com.stelios.cakenaysh.Commands.TabComplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UpdateCollectionTabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if there is one argument, return a list of possible collections
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], java.util.Arrays.asList("stats", "items"), new ArrayList<>());
        }

        //return an empty list
        return new ArrayList<>();
    }

}
