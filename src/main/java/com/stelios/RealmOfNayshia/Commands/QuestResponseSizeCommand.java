package com.stelios.RealmOfNayshia.Commands;

import com.stelios.RealmOfNayshia.Npc.Traits.NpcQuest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class QuestResponseSizeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is not a player, return
        if (!(sender instanceof Player)) {
            return false;
        }

        //get the player
        Player player = (Player) sender;

        //if the argument length is not 3, return
        if (args.length != 2) {
            player.sendMessage(Component.text("Invalid syntax! Use /questresponsesize <textType> <size>", TextColor.color(255,0,0)));
            return false;
        }

        //if the first argument is not one of the applicable ones, return
        if (!(Arrays.asList("locked", "unlocked", "active", "completed").contains(args[0]))) {
            player.sendMessage(Component.text("Invalid status! Status options are locked, unlocked, active, completed.", TextColor.color(255,0,0)));
            return false;
        }

        //if the player doesn't have a npc with the npcquest trait selected, return
        if (CitizensAPI.getDefaultNPCSelector().getSelected(player) == null || !CitizensAPI.getDefaultNPCSelector().getSelected(player).hasTrait(NpcQuest.class)) {
            player.sendMessage(Component.text("You must have a npc with the npcquest trait selected!", TextColor.color(255,0,0)));
            return false;
        }

        //if the second argument is not an int, return
        try {
            if (Integer.parseInt(args[1]) == 0) {
                player.sendMessage(Component.text("Invalid size! The size value must be greater than 0.", TextColor.color(255,0,0)));
                return false;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text("Invalid index! The index value must be an integer.", TextColor.color(255,0,0)));
            return false;
        }

        //get the important npc information
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        NpcQuest npcQuest = npc.getOrAddTrait(NpcQuest.class);

        //set the text based on the first argument
        switch (args[0]) {
            case "locked" -> {
                npcQuest.setLockedResponseSize(Integer.parseInt(args[1]));
                player.sendMessage(Component.text("Locked response size set to " + args[1] + "!", TextColor.color(0,255,0)));
            }
            case "unlocked" -> {
                npcQuest.setUnlockedResponseSize(Integer.parseInt(args[1]));
                player.sendMessage(Component.text("Unlocked response size set to " + args[1] + "!", TextColor.color(0,255,0)));
            }
            case "active" -> {
                npcQuest.setActiveResponseSize(Integer.parseInt(args[1]));
                player.sendMessage(Component.text("Active response size set to " + args[1] + "!", TextColor.color(0,255,0)));
            }
            case "completed" -> {
                npcQuest.setCompletedResponseSize(Integer.parseInt(args[1]));
                player.sendMessage(Component.text("Completed response size set to " + args[1] + "!", TextColor.color(0,255,0)));
            }
        }

        return true;
    }

}