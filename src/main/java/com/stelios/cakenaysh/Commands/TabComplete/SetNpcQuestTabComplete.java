package com.stelios.cakenaysh.Commands.TabComplete;

import com.stelios.cakenaysh.Quests.Quests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetNpcQuestTabComplete implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //first argument: quest names
        if (args.length == 1) {
            return Quests.getQuestConstants();
        }

        //return an empty list
        return new ArrayList<>();
    }
}
