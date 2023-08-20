package com.stelios.cakenaysh.MenuCreation.Menus;

import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Util.CustomPlayer;
import com.stelios.cakenaysh.MenuCreation.Menu;
import com.stelios.cakenaysh.MenuCreation.MenuButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayerInfoMain extends Menu {

    public PlayerInfoMain(Player player) {

        //create the menu
        super(Component.text("Navigation", TextColor.color(0,0,0), TextDecoration.BOLD), 5);

        //getting the custom player
        CustomPlayer customPlayer = Main.getPlugin(Main.class).getPlayerManager().getCustomPlayer(player.getUniqueId());

        ////registering clickable buttons
        //profile button
        Item profile = new Item(Material.PLAYER_HEAD, 1,false)
                .setDisplayName(new ArrayList<>(Collections.singletonList(player.getName() + "'s Profile")),
                        new ArrayList<>(Arrays.asList(0,255,0)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)))
                .setLore(new ArrayList<>(Arrays.asList("nl", String.valueOf(customPlayer.getRank()), "nl",
                                "Level ", String.valueOf(customPlayer.getLevel()), "nl",
                                "Investment Points ", String.valueOf(customPlayer.getInvestmentPoints()), "nl",
                                "\uD83D\uDF9B Experience ", String.valueOf(customPlayer.getXp()), "nl", "nl",
                                "❤ Health ", String.valueOf((int) customPlayer.getHealth()), "nl",
                                "❤ Health Regen ", String.valueOf(customPlayer.getHealthRegen()), "nl",
                                "⚡ Stamina ", String.valueOf(customPlayer.getStamina()), "nl",
                                "⚡ Stamina Regen ", String.valueOf(customPlayer.getStaminaRegen()), "nl",
                                "✦ Speed ", (int) customPlayer.getSpeed() + 100 + "%"
                                )),
                        new ArrayList<>(Arrays.asList(255,255,255,
                                153,255,51, 255,255,255,
                                153,255,51, 255,255,255,
                                153,255,51, 255,255,255,
                                255,51,51, 255,255,255,
                                255,51,51, 255,255,255,
                                255,135,51, 255,255,255,
                                255,135,51, 255,255,255,
                                255,255,255, 255,255,255)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)));


        ItemStack profileItem = profile.build();
        SkullMeta profileMeta = (SkullMeta) profileItem.getItemMeta();
        profileMeta.setOwningPlayer(player);
        profileItem.setItemMeta(profileMeta);

        MenuButton profileButton = new MenuButton(profileItem);
        profileButton.setWhenClicked(clicked ->
                clicked.sendMessage("You clicked on your profile"));

        registerButton(profileButton, 4);


        //stats button
        Item combatStats = new Item(Material.DIAMOND_SWORD, 1,false)
                .setDisplayName(new ArrayList<>(Collections.singletonList("Combat Stats")),
                        new ArrayList<>(Arrays.asList(0,255,0)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)))
                .setLore(new ArrayList<>(Arrays.asList("View your combat stats.","nl",
                                "Click to see more.", "nl", "nl",
                                "Damage ", String.valueOf((int) (customPlayer.getDamage() + customPlayer.getBonusDamage())), "nl",
                                "Attack Speed ", String.valueOf((int) customPlayer.getAttackSpeed()), "nl",
                                "Crit Damage ", customPlayer.getCritDamage() + "%", "nl",
                                "Crit Chance ", customPlayer.getCritChance() + "%", "nl",
                                "Strength ", String.valueOf((int) customPlayer.getStrength()), "nl",
                                "Defense ", String.valueOf((int) customPlayer.getDefense()))),
                        new ArrayList<>(Arrays.asList(128,128,128, 128,128,128,
                                255,51,51, 255,255,255,
                                255,51,51, 255,255,255,
                                66,64,219, 255,255,255,
                                66,64,219, 255,255,255,
                                255,51,51, 255,255,255,
                                72,163,44, 255,255,255)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false)));

        MenuButton statsButton = new MenuButton(combatStats.build());
        statsButton.setWhenClicked(clicked ->
                new PlayerInfoStats(player).open(clicked));

        registerButton(statsButton, 20);

        //skills button
        MenuButton skillsButton = new MenuButton(CustomItems.SKILLS.getItem().build());
        skillsButton.setWhenClicked(clicked ->
                new PlayerInfoSkills(player).open(clicked));

        registerButton(skillsButton, 21);

        //quests button
        MenuButton questsButton = new MenuButton(CustomItems.QUESTS.getItem().build());
        questsButton.setWhenClicked(clicked ->
                clicked.sendMessage("You clicked on your quests"));

        registerButton(questsButton, 23);

        //recipe book button
        MenuButton recipeBookButton = new MenuButton(CustomItems.RECIPE_BOOK.getItem().build());
        recipeBookButton.setWhenClicked(clicked ->
                new PlayerInfoRecipes(player).open(clicked));

        registerButton(recipeBookButton, 24);

        //character management button
        MenuButton characterManagementButton = new MenuButton(CustomItems.CHARACTER_MANAGEMENT.getItem().build());
        characterManagementButton.setWhenClicked(clicked ->
                clicked.sendMessage("You clicked on your character management"));

        registerButton(characterManagementButton, 39);

        //settings button
        MenuButton settingsButton = new MenuButton(CustomItems.SETTINGS.getItem().build());
        settingsButton.setWhenClicked(clicked ->
                clicked.sendMessage("You clicked on your settings"));

        registerButton(settingsButton, 41);

        //close button
        MenuButton closeButton = new MenuButton(CustomItems.CLOSE.getItem().build());
        closeButton.setWhenClicked(clicked -> clicked.closeInventory());

        registerButton(closeButton, 40);

        ////registering non-clickable buttons
        //blank panes
        for (int i = 0; i < this.getInventory().getSize(); i++){

            //if there is no button registered in the current inventory slot, register a blank pane
            if (!this.getButtonMap().containsKey(i)){
                MenuButton blankPane = new MenuButton(CustomItems.BLANK_BLACK_PANE.getItem().build());
                registerButton(blankPane, i);
            }
        }

    }
}
