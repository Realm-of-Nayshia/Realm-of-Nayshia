package com.stelios.RealmOfNayshia.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GetNBTCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            System.out.println(item.getItemMeta());
            player.sendMessage("ItemMeta of item in main hand:\n" + item.getItemMeta());
        } else {
            sender.sendMessage("This command can only be used by a player.");
        }
        return true;
    }
}

