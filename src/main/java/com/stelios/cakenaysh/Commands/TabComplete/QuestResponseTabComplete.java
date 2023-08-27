package com.stelios.cakenaysh.Commands.TabComplete;

import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Npc.Traits.NpcQuest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestResponseTabComplete implements TabCompleter {

    Main main;

    public QuestResponseTabComplete(Main main) {
        this.main = main;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //first argument: text type
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("locked", "unlocked", "active", "completed"), new ArrayList<>());
        }

        if (args.length == 2) {

            //if the first argument is not one of the applicable ones, return
            if (!(Arrays.asList("locked", "unlocked", "active", "completed").contains(args[0]))) {
                return new ArrayList<>();
            }

            //get the player
            Player player = (Player) sender;

            //if the player doesn't have a npc with the npcquest trait selected, return
            if (CitizensAPI.getDefaultNPCSelector().getSelected(player) == null || !CitizensAPI.getDefaultNPCSelector().getSelected(player).hasTrait(NpcQuest.class)) {
                return new ArrayList<>();
            }

            //get the important npc information
            NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
            NpcQuest npcQuest = npc.getOrAddTrait(NpcQuest.class);
            ArrayList<String> lockedText = npcQuest.getLockedText();
            ArrayList<String> unlockedText = npcQuest.getUnlockedText();
            ArrayList<String> activeText = npcQuest.getActiveText();
            ArrayList<String> completedText = npcQuest.getCompletedText();

            //get the valid indexes based on the first argument
            ArrayList<String> validIndexes = new ArrayList<>();
            switch (args[0]) {
                case "locked" -> {

                    //if the locked text is null set the locked text to an empty arraylist
                    if (lockedText == null) {
                        lockedText = new ArrayList<>();
                    }

                    //loop through the locked text and add the indexes to the valid indexes
                    for (int i = 0; i < lockedText.size(); i++) {
                        validIndexes.add(String.valueOf(i));
                    }
                }

                case "unlocked" -> {

                    //if the unlocked text is null set the unlocked text to an empty arraylist
                    if (unlockedText == null) {
                        unlockedText = new ArrayList<>();
                    }

                    //loop through the unlocked text and add the indexes to the valid indexes
                    for (int i = 0; i < unlockedText.size(); i++) {
                        validIndexes.add(String.valueOf(i));
                    }
                }

                case "active" -> {

                    //if the active text is null set the active text to an empty arraylist
                    if (activeText == null) {
                        activeText = new ArrayList<>();
                    }

                    //loop through the active text and add the indexes to the valid indexes
                    for (int i = 0; i < activeText.size(); i++) {
                        validIndexes.add(String.valueOf(i));
                    }
                }

                case "completed" -> {

                    //if the completed text is null set the completed text to an empty arraylist
                    if (completedText == null) {
                        completedText = new ArrayList<>();
                    }

                    //loop through the completed text and add the indexes to the valid indexes
                    for (int i = 0; i < completedText.size(); i++) {
                        validIndexes.add(String.valueOf(i));
                    }
                }
            }

            return StringUtil.copyPartialMatches(args[1], validIndexes, new ArrayList<>());
        }

        return new ArrayList<>();
    }
}