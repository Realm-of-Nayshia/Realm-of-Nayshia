package com.stelios.RealmOfNayshia.Commands.TabComplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestResponseTypeTabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //first argument: text type
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("locked", "unlocked", "active", "completed"), new ArrayList<>());
        }

        return new ArrayList<>();
    }
}