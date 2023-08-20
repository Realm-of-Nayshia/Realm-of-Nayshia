package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Npc.Traits.NpcStats;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetNpcStatsCommand implements CommandExecutor{


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //check if the sender is a player
        if (sender instanceof Player){

            //if there are the correct # of args
            if (args.length == 0){

                //if a npc is selected
                if (CitizensAPI.getDefaultNPCSelector().getSelected(sender) != null) {

                    //get the npc
                    NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

                    //if the npc has the NpcStats trait
                    if (npc.getOrAddTrait(NpcStats.class) != null) {

                        //get the NpcStats trait
                        NpcStats npcStats = npc.getOrAddTrait(NpcStats.class);

                        //reset the stats
                        npcStats.resetStats();

                        //success message
                        sender.sendMessage(Component.text("NPC stats reset.", TextColor.color(0,255,0)));

                    }else{
                        //error: npc does not have the NpcStats trait
                        sender.sendMessage(Component.text("That NPC does not have the NpcStats trait.", TextColor.color(255,0,0)));
                    }

                }else{
                    //error: no npc selected
                    sender.sendMessage(Component.text("You must select an NPC first.", TextColor.color(255,0,0)));
                }

            }else{
                //error: incorrect usage
                sender.sendMessage(Component.text("Incorrect usage! Use /resetnpcstats", TextColor.color(255,0,0)));
            }

        }else{
            //error: not a player
            System.out.println("You must be a player to use this command.");
        }

        return false;

    }
}
