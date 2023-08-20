package com.stelios.cakenaysh.Commands;

import com.stelios.cakenaysh.Events.ProficiencyChangedEvent;
import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetAttributesCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if there are the correct # of args
        if (args.length == 3){

            //if the target player is online
            if (Bukkit.getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0]))) {

                //get the targeted player
                Player player = Bukkit.getServer().getPlayer(args[0]);
                assert player != null;

                //get the main class
                Main main = Main.getPlugin(Main.class);

                //if the attribute is a valid one set it to the correct value
                switch (args[1].toLowerCase()) {
                    case "rank":
                        main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setRank(args[2]);
                        //confirmation message
                        if (sender instanceof Player) {
                            sender.sendMessage(Component.text("Set " + player.getName() + "'s rank to " + args[2] + ".", TextColor.color(0, 255, 0)));
                        } else {
                            System.out.println("Set " + player.getName() + "'s rank to " + args[2] + ".");
                        }
                        break;

                    case "faction":
                        main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setFaction(args[2]);
                        //confirmation message
                        if (sender instanceof Player) {
                            sender.sendMessage(Component.text("Set " + player.getName() + "'s faction to " + args[2] + ".", TextColor.color(0, 255, 0)));
                        } else {
                            System.out.println("Set " + player.getName() + "'s faction to " + args[2] + ".");
                        }
                        break;

                    case "level":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setLevel(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s level to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s level to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid level
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid level.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid level.");
                            }
                        }
                        break;

                    case "investmentpoints":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setInvestmentPoints(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s investment points to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s investment points to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid investment points
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid investment points.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid investment points.");
                            }
                        }
                        break;

                    case "xp":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setXp(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s xp to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s xp to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid xp
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid xp.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid xp.");
                            }
                        }
                        break;

                    case "staminaregen":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setStaminaRegen(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s stamina regen to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s stamina regen to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid stamina regen
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid stamina regen.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid stamina regen.");
                            }
                        }
                        break;

                    case "stamina":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setStamina(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s stamina to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s stamina to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid stamina
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid stamina.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid stamina.");
                            }
                        }
                        break;

                    case "maxstamina":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setMaxStamina(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s max stamina to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s max stamina to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid max stamina
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid max stamina.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid max stamina.");
                            }
                        }
                        break;

                    case "healthregen":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setHealthRegen(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s health regen to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s health regen to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid health regen
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid health regen.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid health regen.");
                            }
                        }
                        break;

                    case "health":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setHealth(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s health to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s health to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid health
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid health.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid health.");
                            }
                        }
                        break;

                    case "maxhealth":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setMaxHealth(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s max health to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s max health to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid max health
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid max health.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid max health.");
                            }
                        }
                        break;

                    case "meleeproficiency":
                        try{
                            //fire the proficiency changed event
                            Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "melee"));

                            //change the proficiency
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setMeleeProficiency(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s melee proficiency to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s melee proficiency to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid melee proficiency
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid melee proficiency.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid melee proficiency.");
                            }
                        }
                        break;

                    case "rangedproficiency":
                        try{
                            //fire the proficiency changed event
                            Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "ranged"));

                            //change the proficiency
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setRangedProficiency(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s ranged proficiency to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s ranged proficiency to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid ranged proficiency
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid ranged proficiency.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid ranged proficiency.");
                            }
                        }
                        break;

                    case "armorproficiency":
                        try{
                            //fire the proficiency changed event
                            Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "armor"));

                            //change the proficiency
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setArmorProficiency(Integer.parseInt(args[2]));

                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s armor proficiency to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s armor proficiency to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid armor proficiency
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid armor proficiency.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid armor proficiency.");
                            }
                        }
                        break;

                    case "wilsoncoin":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setWilsonCoin(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s WilsonCoin to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s WilsonCoin to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid wilson coin
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid WilsonCoin.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid WilsonCoin.");
                            }
                        }
                        break;

                    case "piety":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setPiety(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s piety to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s piety to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid piety
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid piety.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid piety.");
                            }
                        }
                        break;

                    case "charisma":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setCharisma(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s charisma to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s charisma to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid charisma
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid charisma.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid charisma.");
                            }
                        }
                        break;

                    case "deception":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setDeception(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s deception to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s deception to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid deception
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid deception.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid deception.");
                            }
                        }
                        break;

                    case "agility":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setAgility(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s agility to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s agility to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid agility
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid agility.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid agility.");
                            }
                        }
                        break;

                    case "luck":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setLuck(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s luck to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s luck to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid luck
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid luck.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid luck.");
                            }
                        }
                        break;

                    case "stealth":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setStealth(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Set " + player.getName() + "'s stealth to " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println("Set " + player.getName() + "'s stealth to " + args[2] + ".");
                            }
                        }catch (NumberFormatException e){
                            //error: invalid stealth
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid stealth.", TextColor.color(255,0,0)));
                            } else {
                                System.out.println("Invalid stealth.");
                            }
                        }
                        break;

                    default:
                        //error: invalid attribute
                        if (sender instanceof Player) {
                            sender.sendMessage(Component.text("Invalid attribute.", TextColor.color(255,0,0)));
                        } else {
                            System.out.println("Invalid attribute.");
                        }
                }

            }else{
                //error: player not online
                if (sender instanceof Player) {
                    sender.sendMessage(Component.text("That player is not online.", TextColor.color(255,0,0)));
                } else {
                    System.out.println("That player is not online.");
                }
            }

        }else{
            //error: incorrect usage
            if (sender instanceof Player) {
                sender.sendMessage(Component.text("Incorrect usage! Use /setattribute <player> <attribute> <value>.", TextColor.color(255,0,0)));
            } else {
                System.out.println("Incorrect usage! Use /setattribute <player> <attribute> <value>.");
            }
        }
        return false;
    }
}
