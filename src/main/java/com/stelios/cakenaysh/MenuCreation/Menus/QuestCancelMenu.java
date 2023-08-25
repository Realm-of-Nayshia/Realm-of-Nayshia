package com.stelios.cakenaysh.MenuCreation.Menus;

import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.MenuCreation.Menu;
import com.stelios.cakenaysh.MenuCreation.MenuButton;
import com.stelios.cakenaysh.Quests.Quest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class QuestCancelMenu extends Menu {

    public QuestCancelMenu (Player player1, Quest quest, String questName) {
        super(Component.text("Cancel this quest?", TextColor.color(0,0,0), TextDecoration.BOLD), 3);

        Main main = Main.getPlugin(Main.class);

        ////registering clickable buttons
        //accept button
        MenuButton confirm = new MenuButton(CustomItems.YES_BUTTON.getItem().build());
        confirm.setWhenClicked(player -> {
            main.getQuestManager().setQuestStatus(player, quest, "unlocked");
            player.closeInventory();
            player.sendMessage(Component.text("Quest canceled!", TextColor.color(0,255,0)));
            player.playSound(player.getLocation(), Sound.UI_STONECUTTER_TAKE_RESULT, 1, 1);
        });
        registerButton(confirm, 11);

        //cancel button
        MenuButton cancel = new MenuButton(CustomItems.NO_BUTTON.getItem().build());
        cancel.setWhenClicked(player -> {
            new SpecifiedQuestInfoMenu(player1, quest, questName).open(player);
            player.closeInventory();
            player.sendMessage(Component.text("Quest not cancelled!", TextColor.color(255,0,0)));
        });
        registerButton(cancel, 15);


        ////registering unclickable buttons
        //light gray panes
        for (int i = 0; i < this.getInventory().getSize(); i++){

            //if there is no button registered in the current inventory slot, register a blank pane
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_LIGHT_GRAY_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }
    }
}
