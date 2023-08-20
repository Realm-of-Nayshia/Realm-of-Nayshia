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

public class AddAttributesCommand implements CommandExecutor {


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

                //if the attribute is a valid one add the correct value to it
                switch (args[1].toLowerCase()) {
                    case "level":
                        try {
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addLevels(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s level has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s level has increased by " + args[2] + ".");
                            }
                        } catch (NumberFormatException e) {
                            //error: invalid level
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid level.", TextColor.color(255, 0, 0)));
                            } else {
                                System.out.println("Invalid level.");
                            }
                        }
                        break;

                    case "investmentpoints":
                        try {
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addInvestmentPoints(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s investment points have increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s investment points have increased by " + args[2] + ".");
                            }
                        } catch (NumberFormatException e) {
                            //error: invalid investment points
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text("Invalid investment points.", TextColor.color(255, 0, 0)));
                            } else {
                                System.out.println("Invalid investment points.");
                            }
                        }
                        break;

                    case "xp":
                        try{
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addXp(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s xp has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s xp has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addStaminaRegen(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s stamina regen has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s stamina regen has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addStamina(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s stamina has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s stamina has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addMaxStamina(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s max stamina has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s max stamina has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addHealthRegen(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s health regen has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s health regen has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addHealth(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s health has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s health has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addMaxHealth(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s max health has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s max health has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addMeleeProficiency(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s melee proficiency has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s melee proficiency has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addRangedProficiency(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s ranged proficiency has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s ranged proficiency has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addArmorProficiency(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s armor proficiency has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s armor proficiency has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addWilsonCoin(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s WilsonCoin has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s WilsonCoin has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addPiety(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s piety has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s piety has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addCharisma(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s charisma has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s charisma has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addDeception(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s deception has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s deception has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addAgility(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s agility has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s agility has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addLuck(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s luck has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s luck has increased by " + args[2] + ".");
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
                            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addStealth(Integer.parseInt(args[2]));
                            //confirmation message
                            if (sender instanceof Player) {
                                sender.sendMessage(Component.text(player.getName() + "'s stealth has increased by " + args[2] + ".", TextColor.color(0, 255, 0)));
                            } else {
                                System.out.println(player.getName() + "'s stealth has increased by " + args[2] + ".");
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
                sender.sendMessage(Component.text("Incorrect usage! Use /addattribute <player> <attribute> <amount>.", TextColor.color(255,0,0)));
            } else {
                System.out.println("Incorrect usage! Use /addattribute <player> <attribute> <amount>.");
            }
        }
        return false;

    }

}
