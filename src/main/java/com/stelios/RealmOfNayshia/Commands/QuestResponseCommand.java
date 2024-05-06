package com.stelios.RealmOfNayshia.Commands;

import com.stelios.RealmOfNayshia.Npc.Traits.NpcQuest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestResponseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is not a player, return
        if (!(sender instanceof Player)) {
            return false;
        }

        //get the player
        Player player = (Player) sender;

        //if the argument length is not at least 3, return
        if (args.length < 3) {
            player.sendMessage(Component.text("Invalid syntax! Use /questresponse <textType> <index> <response>", TextColor.color(255,0,0)));
            return false;
        }

        //if the first argument is not one of the applicable ones, return
        if (!(Arrays.asList("locked", "unlocked", "active", "completed").contains(args[0]))) {
            player.sendMessage(Component.text("Invalid status! Status options are locked, unlocked, active, completed.", TextColor.color(255,0,0)));
            return false;
        }

        //if the second argument is not an int, return
        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Invalid index! The index value must be an integer.", TextColor.color(255,0,0)));
            return false;
        }

        //if the player doesn't have a npc with the npcquest trait selected, return
        if (CitizensAPI.getDefaultNPCSelector().getSelected(player) == null || !CitizensAPI.getDefaultNPCSelector().getSelected(player).hasTrait(NpcQuest.class)) {
            player.sendMessage(Component.text("You must have a npc with the npcquest trait selected!", TextColor.color(255,0,0)));
            return false;
        }

        //get the important npc information
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        NpcQuest npcQuest = npc.getOrAddTrait(NpcQuest.class);

        //concatenate all the arguments after the second argument into one string with spaces
        StringBuilder response = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            response.append(args[i]).append(" ");
        }

        //set the text based on the first argument
        switch (args[0]) {
            case "locked" -> {

                //if the index is out of bounds, return
                if (npcQuest.getLockedText().size() <= Integer.parseInt(args[1])) {
                    player.sendMessage(Component.text("Invalid index Size of the response is " + npcQuest.getLockedText().size() + ".", TextColor.color(255, 0, 0)));
                    return false;

                } else {
                    //set the text
                    ArrayList<String> lockedText = npcQuest.getLockedText();
                    lockedText.set(Integer.parseInt(args[1]), ChatColor.translateAlternateColorCodes('&', response.toString()));
                    npcQuest.setLockedText(lockedText);
                    player.sendMessage(Component.text("Set the locked text!", TextColor.color(0, 255, 0)));
                }
            }

            case "unlocked" -> {

                //if the index is out of bounds, return
                if (npcQuest.getUnlockedText().size() <= Integer.parseInt(args[1])) {
                    player.sendMessage(Component.text("Invalid index! Size of the response is " + npcQuest.getUnlockedText().size() + ".", TextColor.color(255, 0, 0)));
                    return false;
                } else {
                    //set the text
                    ArrayList<String> unlockedText = npcQuest.getUnlockedText();
                    unlockedText.set(Integer.parseInt(args[1]), ChatColor.translateAlternateColorCodes('&', response.toString()));
                    npcQuest.setUnlockedText(unlockedText);
                    player.sendMessage(Component.text("Set the unlocked text!", TextColor.color(0, 255, 0)));
                }
            }

            case "active" -> {

                //if the index is out of bounds, return
                if (npcQuest.getActiveText().size() <= Integer.parseInt(args[1])) {
                    player.sendMessage(Component.text("Invalid index! Size of the response is " + npcQuest.getActiveText().size() + ".", TextColor.color(255, 0, 0)));
                    return false;
                } else {
                    //set the text
                    ArrayList<String> activeText = npcQuest.getActiveText();
                    activeText.set(Integer.parseInt(args[1]), ChatColor.translateAlternateColorCodes('&', response.toString()));
                    npcQuest.setActiveText(activeText);
                    player.sendMessage(Component.text("Set the active text!", TextColor.color(0, 255, 0)));
                }
            }

            case "completed" -> {

                //if the index is out of bounds, return
                if (npcQuest.getCompletedText().size() <= Integer.parseInt(args[1])) {
                    player.sendMessage(Component.text("Invalid index! Size of the response is " + npcQuest.getCompletedText().size() + ".", TextColor.color(255, 0, 0)));
                    return false;
                } else {
                    //set the text
                    ArrayList<String> completedText = npcQuest.getCompletedText();
                    completedText.set(Integer.parseInt(args[1]), ChatColor.translateAlternateColorCodes('&', response.toString()));
                    npcQuest.setCompletedText(completedText);
                    player.sendMessage(Component.text("Set the completed text!", TextColor.color(0, 255, 0)));
                }
            }
        }

        return true;
    }

}