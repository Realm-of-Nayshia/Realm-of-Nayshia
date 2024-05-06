package com.stelios.RealmOfNayshia.MenuCreation.Menus;

import com.stelios.RealmOfNayshia.Items.CustomItems;
import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.MenuCreation.Menu;
import com.stelios.RealmOfNayshia.MenuCreation.MenuButton;
import com.stelios.RealmOfNayshia.Quests.Quest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;

public class QuestAcceptMenu extends Menu {

    public QuestAcceptMenu(Quest quest) {
        super(Component.text("Do you accept this quest?", TextColor.color(0,0,0), TextDecoration.BOLD), 3);

        Main main = Main.getPlugin(Main.class);

        ////registering clickable buttons
        //accept button
        MenuButton confirm = new MenuButton(CustomItems.ACCEPT_BUTTON.getItem().build());
        confirm.setWhenClicked(player -> {
            main.getQuestManager().setQuestStatus(player, quest, "active");
            player.closeInventory();
            player.sendMessage(Component.text("Quest accepted!", TextColor.color(0,255,0)));
            player.playSound(player.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
        });
        registerButton(confirm, 11);

        //cancel button
        MenuButton cancel = new MenuButton(CustomItems.DECLINE_BUTTON.getItem().build());
        cancel.setWhenClicked(player -> {
            player.closeInventory();
            player.sendMessage(Component.text("Quest declined!", TextColor.color(255,0,0)));
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
