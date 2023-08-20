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

import java.util.Arrays;

public class SetNpcStatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //only run the command if a player is sending it
        if (sender instanceof Player){

            //if there are the correct # of args
            if (args.length == 2){

                //if a npc is selected
                if (CitizensAPI.getDefaultNPCSelector().getSelected(sender) != null){

                    //get the npc
                    NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

                    //if the npc has the NpcStats trait
                    if (npc.getOrAddTrait(NpcStats.class) != null){

                        //get the NpcStats trait
                        NpcStats npcStats = npc.getOrAddTrait(NpcStats.class);

                        //if the stat is valid
                        if (Arrays.asList("xp","critdamage","critchance","strength","thorns","defense","infernaldefense","infernaldamage","undeaddefense",
                                "undeaddamage","aquaticdefense","aquaticdamage","aerialdefense","aerialdamage","meleedefense",
                                "meleedamage","rangeddefense","rangeddamage","magicdefense","magicdamage")
                                .contains(args[0].toLowerCase())) {

                            //if the value is a number
                            try {
                                float value = Float.parseFloat(args[1]);

                                //set the stat
                                npcStats.setStat(args[0].toLowerCase(), value);

                                //success
                                sender.sendMessage(Component.text("Successfully set " + args[0].toLowerCase() + " to " + value + ".", TextColor.color(0,255,0)));
                                return true;


                            } catch (NumberFormatException e) {
                                //error: value is not a number
                                sender.sendMessage(Component.text("The value must be a number.", TextColor.color(255,0,0)));
                                return false;
                            }
                        }else{
                            //error: invalid stat
                            sender.sendMessage(Component.text("Invalid stat." , TextColor.color(255,0,0)));
                        }

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
                sender.sendMessage(Component.text("Incorrect usage! Use /setnpcstat <stat> <value>.", TextColor.color(255,0,0)));
            }

        }else{
            //error: not a player
            System.out.println("You must be a player to use this command.");
        }

        return false;

    }
}
