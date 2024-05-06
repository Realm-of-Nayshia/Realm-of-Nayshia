package com.stelios.RealmOfNayshia.Commands;

import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.Npc.Traits.NpcQuest;
import com.stelios.RealmOfNayshia.Quests.Quests;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetNpcQuestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is not a player
        if (!(sender instanceof Player)) {
            return false;
        }

        //get the player
        Player player = (Player) sender;

        //if the arg # are not correct or the quest is not valid
        if (args.length != 1 || !Quests.getQuestConstants().contains(args[0])) {
            player.sendMessage(Component.text("Incorrect syntax. Use /setnpcquest <quest>", TextColor.color(255,0,0)));
            return false;
        }

        //if a npc is selected with the NpcQuest trait
        if (CitizensAPI.getDefaultNPCSelector().getSelected(player) != null &&
                CitizensAPI.getDefaultNPCSelector().getSelected(player).getOrAddTrait(NpcQuest.class) != null) {

            //get the npc and the NpcQuest trait
            NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
            NpcQuest npcQuest = npc.getOrAddTrait(NpcQuest.class);

            //set the quest
            npcQuest.setQuestName(Main.getPlugin(Main.class).getQuestManager().getQuestFromName(args[0]).getName());

            //confirmation message
            player.sendMessage(Component.text("Successfully set quest to " + args[0] + ".", TextColor.color(0,255,0)));
            return true;

        //if a npc is not selected with the NpcQuest trait
        } else {
            player.sendMessage(Component.text("No NPC selected or the NPC does not have the NpcQuest trait.", TextColor.color(255,0,0)));
        }

        return false;
    }
}
