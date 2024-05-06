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

import java.util.ArrayList;
import java.util.Arrays;

public class QuestResponseResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is not a player, return
        if (!(sender instanceof Player)) {
            return false;
        }

        //get the player
        Player player = (Player) sender;

        //if the argument length is not 1, return
        if (args.length != 1) {
            player.sendMessage(Component.text("Invalid syntax! Use /questresponseget <textType>", TextColor.color(255,0,0)));
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

        //get the important npc information
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        NpcQuest npcQuest = npc.getOrAddTrait(NpcQuest.class);

        //reset the npc's quest response
        switch (args[0]) {
            case "locked" -> {
                npcQuest.setLockedText(new ArrayList<>(Arrays.asList(
                        "Quest locked text 1",
                        "Quest locked text 2",
                        "Quest locked text 3")));
                player.sendMessage(Component.text("Reset the locked text!", TextColor.color(0,255,0)));
            }

            case "unlocked" -> {
                npcQuest.setUnlockedText(new ArrayList<>(Arrays.asList(
                        "Quest unlocked text 1",
                        "Quest unlocked text 2",
                        "Quest unlocked text 3")));
                player.sendMessage(Component.text("Reset the unlocked text!", TextColor.color(0,255,0)));
            }

            case "active" -> {
                npcQuest.setActiveText(new ArrayList<>(Arrays.asList(
                        "Quest active text 1",
                        "Quest active text 2",
                        "Quest active text 3")));
                player.sendMessage(Component.text("Reset the active text!", TextColor.color(0,255,0)));
            }

            case "completed" -> {
                npcQuest.setCompletedText(new ArrayList<>(Arrays.asList(
                        "Quest completed text 1",
                        "Quest completed text 2",
                        "Quest completed text 3")));
                player.sendMessage(Component.text("Reset the completed text!", TextColor.color(0,255,0)));
            }
        }

        return true;
    }
}
