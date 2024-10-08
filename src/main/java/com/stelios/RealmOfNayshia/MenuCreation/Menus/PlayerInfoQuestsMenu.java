package com.stelios.RealmOfNayshia.MenuCreation.Menus;

import com.stelios.RealmOfNayshia.Items.CustomItems;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.MenuCreation.Menu;
import com.stelios.RealmOfNayshia.MenuCreation.MenuButton;
import com.stelios.RealmOfNayshia.Quests.Quest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerInfoQuestsMenu extends Menu {

    public PlayerInfoQuestsMenu(Player player) {
        super(Component.text("Quests", TextColor.color(0,0,0), TextDecoration.BOLD), 1);

        Main main = Main.getPlugin(Main.class);

        ////registering clickable buttons
        //back button
        MenuButton backButton = new MenuButton(CustomItems.BACK_BUTTON.getItem().build());
        backButton.setWhenClicked(clicked -> {
            new PlayerInfoMainMenu(clicked).open(clicked);
        });
        registerButton(backButton, 0);

        //close button
        MenuButton closeButton = new MenuButton(CustomItems.CLOSE.getItem().build());
        closeButton.setWhenClicked(clicked -> clicked.closeInventory());
        registerButton(closeButton, 8);

        //loop through the player's active quests
        for (int i = 0; i < main.getQuestManager().getActiveQuests(player).size(); i++){

            //get the quest and its name
            Quest quest = main.getQuestManager().getQuestFromName(main.getQuestManager().getActiveQuests(player).get(i));
            String questName = main.getQuestManager().getActiveQuests(player).get(i).replaceAll("_", " ");

            //create a quest button for each active quest
            Item questItem = new Item(Material.PAPER, 1, false)
                    .setDisplayName(Collections.singletonList(questName),
                            new ArrayList<>(Arrays.asList(152, 240, 115)),
                            new ArrayList<>(List.of(true)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)))
                    .setLore(new ArrayList<>(List.of("Click to view quest details.")),
                            new ArrayList<>(Arrays.asList(128,128,128)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)),
                            new ArrayList<>(List.of(false)));
            MenuButton questButton = new MenuButton(questItem.build());
            questButton.setWhenClicked(clicked -> {
                new SpecifiedQuestInfoMenu(clicked, quest, questName).open(clicked);
            });
            registerButton(questButton, i+3);
        }

        ////registering unclickable buttons
        //blank black pane frame
        for (int i : new int[]{1,2,6,7}){
            //if there is no button registered in the current inventory slot, register a blank pane
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_BLACK_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }

        //fill the rest of the inventory with light gray panes
        for (int i = 0; i < 9; i++){
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_LIGHT_GRAY_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }
    }
}
