package com.stelios.RealmOfNayshia.Commands;

import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.MenuCreation.Menu;
import com.stelios.RealmOfNayshia.MenuCreation.Menus.PlayerInfoMainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class MenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //if the sender is a player
        if (sender instanceof Player){

            //get the player
            Player player = (Player) sender;

            //open the rpg menu
            Menu menu = new PlayerInfoMainMenu(player);
            menu.open(player);

        }else{
            //error: sender is not a player
            Main.getPlugin(Main.class).getLogger().log(Level.WARNING,"You must be a player to use this command!");
        }

        return false;
    }

}
