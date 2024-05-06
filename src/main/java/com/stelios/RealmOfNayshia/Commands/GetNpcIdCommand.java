package com.stelios.RealmOfNayshia.Commands;

import net.citizensnpcs.api.CitizensAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetNpcIdCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //check if the sender is a player
        if (!(sender instanceof Player)) {
            return false;
        }

        //get the player
        Player player = (Player) sender;

        //if the player has no npc selected
        if (CitizensAPI.getDefaultNPCSelector().getSelected(player) == null) {
            player.sendMessage(Component.text("You have no NPC selected!", TextColor.color(0,255,0)));
            return false;
        }

        //get the uuid and id of the npc
        String uuid = CitizensAPI.getDefaultNPCSelector().getSelected(player).getUniqueId().toString();
        int idNumber = CitizensAPI.getDefaultNPCSelector().getSelected(player).getId();

        //send the id and uuid of the npc to the player
        TextComponent uuidSetup = Component.text("UUID: ", TextColor.color(128,128,128));
        TextComponent uuidString = Component.text(uuid, TextColor.color(0,255,0), TextDecoration.ITALIC);
        TextComponent idSetup = Component.text("  ID: ", TextColor.color(128,128,128));
        TextComponent idNumberString = Component.text(idNumber, TextColor.color(0,255,0), TextDecoration.ITALIC);

        //make the uuid and id text components clickable and hoverable
        uuidString = uuidString.clickEvent(ClickEvent.copyToClipboard(uuid)).hoverEvent(Component.text("Click to copy", TextColor.color(128,128,128)));
        idNumberString = idNumberString.clickEvent(ClickEvent.copyToClipboard(String.valueOf(idNumber))).hoverEvent(Component.text("Click to copy", TextColor.color(128,128,128)));

        //send the message to the player
        player.sendMessage(uuidSetup.append(uuidString).append(idSetup).append(idNumberString));

        return true;
    }
}
