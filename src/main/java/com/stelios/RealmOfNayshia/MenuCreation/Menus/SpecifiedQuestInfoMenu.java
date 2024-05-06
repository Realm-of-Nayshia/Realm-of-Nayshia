package com.stelios.RealmOfNayshia.MenuCreation.Menus;

import com.stelios.RealmOfNayshia.Items.CustomItems;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.MenuCreation.Menu;
import com.stelios.RealmOfNayshia.MenuCreation.MenuButton;
import com.stelios.RealmOfNayshia.Quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpecifiedQuestInfoMenu extends Menu {

    public SpecifiedQuestInfoMenu(Player player, Quest quest, String questName) {
        super(Component.text("Quest Information", TextColor.color(0,0,0), TextDecoration.BOLD), 3);

        ////registering clickable buttons
        //back button
        MenuButton backButton = new MenuButton(CustomItems.BACK_BUTTON.getItem().build());
        backButton.setWhenClicked(clicked -> {
            new PlayerInfoQuestsMenu(clicked).open(clicked);
        });
        registerButton(backButton, 18);

        //close button
        MenuButton closeButton = new MenuButton(CustomItems.CLOSE.getItem().build());
        closeButton.setWhenClicked(clicked -> clicked.closeInventory());
        registerButton(closeButton, 22);

        //quest description button
        Item questDescriptionItem = new Item(Material.WRITABLE_BOOK, 1, false)
                .setDisplayName(Collections.singletonList("Quest Description"),
                        new ArrayList<>(Arrays.asList(63,143,63)),
                        new ArrayList<>(List.of(true)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)))
                .setLore(new ArrayList<>(List.of("Click to view the quest description.")),
                        new ArrayList<>(Arrays.asList(128,128,128)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)));
        MenuButton questDescriptionButton = new MenuButton(questDescriptionItem.build());
        questDescriptionButton.setWhenClicked(clicked -> {

            clicked.closeInventory();

            //create a book
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            bookMeta.setTitle(questName);
            bookMeta.setAuthor(CitizensAPI.getNPCRegistry().getById(quest.getQuestGiverId()).getName());
            bookMeta.addPages(quest.getBookDescription());
            book.setItemMeta(bookMeta);

            //open the book
            player.openBook(book);
        });
        registerButton(questDescriptionButton, 11);

        //cancel quest button
        MenuButton cancelQuestButton = new MenuButton(CustomItems.CANCEL_QUEST_BUTTON.getItem().build());
        cancelQuestButton.setWhenClicked(clicked -> {
            new QuestCancelMenu(player, quest, questName).open(clicked);
        });
        registerButton(cancelQuestButton, 15);


        ////registering unclickable buttons
        //quest button
        Item questItem = new Item(Material.PAPER, 1, false)
                .setDisplayName(Collections.singletonList(questName),
                        new ArrayList<>(Arrays.asList(152, 240, 115)),
                        new ArrayList<>(List.of(true)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)),
                        new ArrayList<>(List.of(false)));
        MenuButton questButton = new MenuButton(questItem.build());
        registerButton(questButton, 4);

        //blank black panes
        for (int i = 0; i < this.getInventory().getSize(); i++){

            //if there is no button registered in the current inventory slot, register a blank pane
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_BLACK_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }

    }
}
