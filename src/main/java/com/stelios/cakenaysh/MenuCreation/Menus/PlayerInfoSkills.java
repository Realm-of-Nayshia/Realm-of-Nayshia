package com.stelios.cakenaysh.MenuCreation.Menus;

import com.stelios.cakenaysh.Events.ProficiencyChangedEvent;
import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.MenuCreation.Menu;
import com.stelios.cakenaysh.MenuCreation.MenuButton;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlayerInfoSkills extends Menu {

    public PlayerInfoSkills(Player player){
        super( Component.text("Your Skills", TextColor.color(0,0,0), TextDecoration.BOLD), 5);

        //getting the custom player
        CustomPlayer customPlayer = Main.getPlugin(Main.class).getPlayerManager().getCustomPlayer(player.getUniqueId());

        ////registering clickable buttons
        //back button
        MenuButton backButton = new MenuButton(CustomItems.BACK_BUTTON.getItem().build());
        backButton.setWhenClicked(clicked -> {
            new PlayerInfoMain(clicked).open(clicked);
        });

        registerButton(backButton, 36);

        //close button
        MenuButton closeButton = new MenuButton(CustomItems.CLOSE.getItem().build());
        closeButton.setWhenClicked(clicked -> clicked.closeInventory());

        registerButton(closeButton, 40);

        //melee proficiency
        Item meleeProficiency = new Item(Material.IRON_SWORD, 1, false)
                .setDisplayName(new ArrayList<>(Arrays.asList("Melee Level ", String.valueOf(customPlayer.getMeleeProficiency()))),
                        new ArrayList<>(Arrays.asList(214,88,88, 214,88,88)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)))
                .setLore(new ArrayList<>(Arrays.asList("Investment Points: ", String.valueOf(customPlayer.getInvestmentPoints()), "nl", "nl",
                                "Click to increase melee proficiency.", "nl",
                                "Melee proficiency unlocks the use of better melee weapons.")),
                        new ArrayList<>(Arrays.asList(153,255,51, 153,255,51,
                                128,128,128, 128,128,128)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)));

        MenuButton meleeProficiencyButton = new MenuButton(meleeProficiency.build());
        meleeProficiencyButton.setWhenClicked(clicked -> {
            if (customPlayer.getInvestmentPoints() > 0) {

                //firing the proficiency changed event
                Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "melee"));

                //add the melee proficiency, subtract the investment points, and update the menu
                customPlayer.setMeleeProficiency(customPlayer.getMeleeProficiency() + 1);
                customPlayer.setInvestmentPoints(customPlayer.getInvestmentPoints() - 1);
                new PlayerInfoSkills(clicked).open(clicked);
            }
        });

        registerButton(meleeProficiencyButton, 19);


        //ranged proficiency
        Item rangedProficiency = new Item(Material.BOW, 1, false)
                .setDisplayName(new ArrayList<>(Arrays.asList("Ranged Level ", String.valueOf(customPlayer.getRangedProficiency()))),
                        new ArrayList<>(Arrays.asList(240,185,85, 240,185,85)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)))
                .setLore(new ArrayList<>(Arrays.asList("Investment Points: ", String.valueOf(customPlayer.getInvestmentPoints()), "nl", "nl",
                                "Click to increase ranged proficiency.", "nl",
                                "Ranged proficiency unlocks the use of better ranged weapons.")),
                        new ArrayList<>(Arrays.asList(153,255,51, 153,255,51,
                                128,128,128, 128,128,128)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)));

        MenuButton rangedProficiencyButton = new MenuButton(rangedProficiency.build());
        rangedProficiencyButton.setWhenClicked(clicked -> {
            if(customPlayer.getInvestmentPoints() > 0) {

                //firing the proficiency changed event
                Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "ranged"));

                //add the ranged proficiency, subtract the investment points, and update the menu
                customPlayer.setRangedProficiency(customPlayer.getRangedProficiency() + 1);
                customPlayer.setInvestmentPoints(customPlayer.getInvestmentPoints() - 1);
                new PlayerInfoSkills(clicked).open(clicked);
            }
        });

        registerButton(rangedProficiencyButton, 20);

        //armor proficiency
        Item armorProficiency = new Item(Material.IRON_CHESTPLATE, 1, false)
                .setDisplayName(new ArrayList<>(Arrays.asList("Armor Level ", String.valueOf(customPlayer.getArmorProficiency()))),
                        new ArrayList<>(Arrays.asList(77,85,92, 77,85,92)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)),
                        new ArrayList<>(Arrays.asList(false,false)))
                .setLore(new ArrayList<>(Arrays.asList("Investment Points: ", String.valueOf(customPlayer.getInvestmentPoints()), "nl", "nl",
                                "Click to increase armor proficiency.", "nl",
                                "Armor proficiency unlocks the use of better armor.")),
                        new ArrayList<>(Arrays.asList(153,255,51, 153,255,51,
                                128,128,128, 128,128,128)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false)));

        MenuButton armorProficiencyButton = new MenuButton(armorProficiency.build());
        armorProficiencyButton.setWhenClicked(clicked -> {
            if(customPlayer.getInvestmentPoints() > 0) {

                //firing the proficiency changed event
                Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "armor"));

                //add the armor proficiency, subtract the investment points, and update the menu
                customPlayer.setArmorProficiency(customPlayer.getArmorProficiency() + 1);
                customPlayer.setInvestmentPoints(customPlayer.getInvestmentPoints() - 1);
                new PlayerInfoSkills(clicked).open(clicked);
            }
        });

        registerButton(armorProficiencyButton, 21);


        //reset proficiencies button
        Item resetProficiencies = new Item(Material.REDSTONE_TORCH, 1, false)
                .setDisplayName(new ArrayList<>(Arrays.asList("Click to reset all proficiencies")),
                        new ArrayList<>(Arrays.asList(255,0,0)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)))
                .setLore(new ArrayList<>(Arrays.asList("Resetting proficiencies will refund all investment points.")),
                        new ArrayList<>(Arrays.asList(128,128,128)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)));

        MenuButton resetProficienciesButton = new MenuButton(resetProficiencies.build());
        resetProficienciesButton.setWhenClicked(clicked -> {
            customPlayer.setMeleeProficiency(0);
            customPlayer.setRangedProficiency(0);
            customPlayer.setArmorProficiency(0);
            customPlayer.setInvestmentPoints(customPlayer.getLevel());
            new PlayerInfoSkills(clicked).open(clicked);

            //firing the proficiency changed event
            Bukkit.getPluginManager().callEvent(new ProficiencyChangedEvent(player, "all"));

            //confirmation message
            player.sendMessage(Component.text("Proficiencies reset.", TextColor.color(0,255,0)));
        });

        registerButton(resetProficienciesButton, 29);


        ////registering non-clickable buttons
        //proficiency information
        Item proficiency = new Item(Material.PLAYER_HEAD, 1,false)
                .setDisplayName(new ArrayList<>(Arrays.asList("Current Proficiency Levels")),
                        new ArrayList<>(Arrays.asList(153,255,51)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)),
                        new ArrayList<>(Collections.singletonList(false)))
                .setLore(new ArrayList<>(Arrays.asList("Melee ", String.valueOf(customPlayer.getMeleeProficiency()), "nl",
                                "Ranged ", String.valueOf(customPlayer.getRangedProficiency()), "nl",
                                "Armor ", String.valueOf(customPlayer.getArmorProficiency()), "nl", "nl",
                                "Proficiency unlocks the use of better items and abilities.", "nl",
                                "You gain one investment point each time you level up.", "nl",
                                "Click on the categories below to level up your proficiency."
                        )),
                        new ArrayList<>(Arrays.asList(
                                214,88,88, 214,88,88,
                                240,185,85, 240,185,85,
                                77,85,92, 77,85,92,
                                128,128,128, 128,128,128, 128,128,128
                                )),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false)),
                        new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false)));


        ItemStack profileItem = proficiency.build();
        SkullMeta profileMeta = (SkullMeta) profileItem.getItemMeta();
        profileMeta.setOwningPlayer(player);
        profileItem.setItemMeta(profileMeta);

        registerButton(new MenuButton(profileItem), 11);

        //skills info
        registerButton(new MenuButton(CustomItems.SKILLS.getItem().build()), 4);

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
