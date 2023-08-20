package com.stelios.cakenaysh.Managers;

import com.stelios.cakenaysh.Items.EquipmentBonuses;
import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class StatsManager {

    private final Main main;
    private ArrayList<TextDisplay> textDisplays = new ArrayList<>();

    public StatsManager(Main main) {
        this.main = main;
    }

    public ArrayList<TextDisplay> getTextDisplays() {
        return textDisplays;
    }

    //set player configurations
    public void setConfigurations(Player player){
        player.setMaxHealth(40);
        main.getCombatManager().removeCombatTimer(player.getUniqueId());
        player.setMaximumNoDamageTicks(1);
    }


    //update every player's database stats
    public void updateDatabaseStatsAll(){

        //loop through all the players
        for (Player player : main.getServer().getOnlinePlayers()) {

            CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

            //remove the stats from the player's armor
            for (ItemStack item : player.getInventory().getArmorContents()) {

                if (item != null) {

                    //remove the stats from the armor
                    removePlayerStats(player, item, "armor");
                }
            }

            //remove the stats from the main hand
            removePlayerStats(player, player.getInventory().getItemInMainHand(), "weapon");

            //remove the stats from the offhand
            removePlayerStats(player, player.getInventory().getItemInOffHand(), "accessory");

            //saving the player's stats to the database
            customPlayer.saveAttributesToDatabase(player);

            //add the stats back to the player's armor
            for (ItemStack item : player.getInventory().getArmorContents()) {

                if (item != null) {

                    //remove the stats from the armor
                    addPlayerStats(player, item, "armor");
                }
            }

            //add the stats back to the offhand
            addPlayerStats(player, player.getInventory().getItemInOffHand(), "accessory");

            //add the stats back to the main hand
            addPlayerStats(player, player.getInventory().getItemInMainHand(), "weapon");
        }
    }


    //update one player's database stats
    public void updateDatabaseStatsPlayer(Player player){

        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        //remove the stats from the player's armor
        for (ItemStack item : player.getInventory().getArmorContents()) {

            if (item != null) {

                //remove the stats from the armor
                removePlayerStats(player, item, "armor");
            }
        }

        //remove the stats from the main hand
        removePlayerStats(player, player.getInventory().getItemInMainHand(), "weapon");

        //remove the stats from the offhand
        removePlayerStats(player, player.getInventory().getItemInOffHand(), "accessory");

        //saving the player's stats to the database
        customPlayer.saveAttributesToDatabase(player);

        //add the stats back to the player's armor
        for (ItemStack item : player.getInventory().getArmorContents()) {

            if (item != null) {

                //remove the stats from the armor
                addPlayerStats(player, item, "armor");
            }
        }

        //add the stats back to the offhand
        addPlayerStats(player, player.getInventory().getItemInOffHand(), "accessory");

        //add the stats back to the main hand
        addPlayerStats(player, player.getInventory().getItemInMainHand(), "weapon");
    }


    //returns true if the player has met the requirements to equip an item
    public boolean meetsItemRequirements(Player player, ItemStack item, Boolean sendMessage){

        try {

            CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

            try {
                //if the player meets the item requirements, return true
                if (itemData.get(new NamespacedKey(main, "meleeProficiency"), PersistentDataType.INTEGER) <= customPlayer.getMeleeProficiency()) {
                    if (itemData.get(new NamespacedKey(main, "rangedProficiency"), PersistentDataType.INTEGER) <= customPlayer.getRangedProficiency()) {
                        if (itemData.get(new NamespacedKey(main, "armorProficiency"), PersistentDataType.INTEGER) <= customPlayer.getArmorProficiency()) {
                            return true;
                        }
                    }
                }

            //if there is an error the item is not a battle item, return true
            } catch (NullPointerException ex) {
                return true;
            }

            //send the message if applicable
            if (sendMessage){
                player.sendMessage(Component.text("You do not meet the requirements to equip this item.", TextColor.color(255, 0, 0)));
            }

            //else return false
            return false;

        //if the item is null return true
        }catch (NullPointerException ex){
            return true;
        }
    }


    //display a text display for the damage
    public void displayDamage(Entity entity, int damage, boolean isCritical, Location location){

        //slightly offset the location
        location.add(((Math.random()*-2)+1)/2, ((Math.random()*-2)+1)/2 + 1, ((Math.random()*-2)+1)/2);

        //create the damage component
        Component damageComponent;
        if (isCritical){
            damageComponent = Component.text("⚔" + damage, TextColor.color(255,51,51));
        }else{
            damageComponent = Component.text(damage, TextColor.color(204,0,0));
        }

        //spawn the text display
        TextDisplay textDisplay = (TextDisplay) Bukkit.getWorld(entity.getWorld().getUID()).spawnEntity(location, EntityType.TEXT_DISPLAY);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setCustomNameVisible(true);
        textDisplay.customName(damageComponent);

        //add the text display to the arraylist
        textDisplays.add(textDisplay);

        //despawn the text display after 1.5 seconds
        new BukkitRunnable(){
            @Override
            public void run() {
                textDisplays.remove(textDisplay);
                textDisplay.remove();
            }
        }.runTaskLater(main, 30);
    }


    //manages the health of the player when equipping different items
    public void manageHealthAndStamina(CustomPlayer customPlayer){

        //if health is greater than max health, set health to max health
        if (customPlayer.getHealth() > customPlayer.getMaxHealth()) {
            customPlayer.setHealth(customPlayer.getMaxHealth());
        }

        //if the stamina is greater than max stamina, set the stamina to max stamina
        if (customPlayer.getStamina() > customPlayer.getMaxStamina()) {
            customPlayer.setStamina(customPlayer.getMaxStamina());
        }
    }


    //display the action bar
    public void displayActionBar(Player player){
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        player.sendActionBar(Component.text((int) customPlayer.getHealth() + " / " + customPlayer.getMaxHealth() + " ❤     ", TextColor.color(255,51,51))
                .append(Component.text(customPlayer.getStamina() + " / " + customPlayer.getMaxStamina() + " ⚡", TextColor.color(255,135,51))));
    }


    //update the player's hearts
    public void updateHearts(Player player){

        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        //if the player has no health, kill them
        if (customPlayer.getHealth() <= 0) {

            //if the player is not already dead, kill them
            if (player.getHealth() > 0) {
                player.setHealth(0);
                player.sendMessage(Component.text("You have died.", TextColor.color(255, 0, 0)));
            }

        //if the player has super low health, set the health to 1/2 a heart
        } else if (customPlayer.getHealth() / customPlayer.getMaxHealth() > 0.0001 && customPlayer.getHealth() / customPlayer.getMaxHealth() < 0.02){
            player.setHealth(1);

        //else scale the hearts normally
        }else{
            player.setHealth(customPlayer.getHealth() / customPlayer.getMaxHealth() * 40);
        }
    }


    //add the stats of the item the player is holding
    public void addPlayerStats(Player player, ItemStack item, String itemType){

        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        try {

            //get the item's data
            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

            //if the item is of the correct itemType
            if (itemData.get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING).equalsIgnoreCase(itemType)) {

                //if the player meets the item requirements
                if (meetsItemRequirements(player, item, true)) {

                    //add the item's stats to the player's stats
                    customPlayer.setDamage(customPlayer.getDamage() + itemData.get(new NamespacedKey(main, "damage"), PersistentDataType.FLOAT));
                    customPlayer.setAttackSpeed(customPlayer.getAttackSpeed() + itemData.get(new NamespacedKey(main, "attackSpeed"), PersistentDataType.FLOAT));
                    customPlayer.setCritChance(customPlayer.getCritChance() + itemData.get(new NamespacedKey(main, "critChance"), PersistentDataType.FLOAT));
                    customPlayer.setCritDamage(customPlayer.getCritDamage() + itemData.get(new NamespacedKey(main, "critDamage"), PersistentDataType.FLOAT));
                    customPlayer.setStrength(customPlayer.getStrength() + itemData.get(new NamespacedKey(main, "strength"), PersistentDataType.FLOAT));
                    customPlayer.setMaxHealth((int) (customPlayer.getMaxHealth() + itemData.get(new NamespacedKey(main, "health"), PersistentDataType.FLOAT)));
                    customPlayer.setHealthRegen((int) (customPlayer.getHealthRegen() + itemData.get(new NamespacedKey(main, "healthRegen"), PersistentDataType.FLOAT)));
                    customPlayer.setMaxStamina((int) (customPlayer.getMaxStamina() + itemData.get(new NamespacedKey(main, "stamina"), PersistentDataType.FLOAT)));
                    customPlayer.setStaminaRegen((int) (customPlayer.getStaminaRegen() + itemData.get(new NamespacedKey(main, "staminaRegen"), PersistentDataType.FLOAT)));
                    customPlayer.setDefense(customPlayer.getDefense() + itemData.get(new NamespacedKey(main, "defense"), PersistentDataType.FLOAT));
                    customPlayer.setSpeed(customPlayer.getSpeed() + itemData.get(new NamespacedKey(main, "speed"), PersistentDataType.FLOAT));
                    customPlayer.setInfernalDefense(customPlayer.getInfernalDefense() + itemData.get(new NamespacedKey(main, "infernalDefense"), PersistentDataType.FLOAT));
                    customPlayer.setInfernalDamage(customPlayer.getInfernalDamage() + itemData.get(new NamespacedKey(main, "infernalDamage"), PersistentDataType.FLOAT));
                    customPlayer.setUndeadDefense(customPlayer.getUndeadDefense() + itemData.get(new NamespacedKey(main, "undeadDefense"), PersistentDataType.FLOAT));
                    customPlayer.setUndeadDamage(customPlayer.getUndeadDamage() + itemData.get(new NamespacedKey(main, "undeadDamage"), PersistentDataType.FLOAT));
                    customPlayer.setAquaticDefense(customPlayer.getAquaticDefense() + itemData.get(new NamespacedKey(main, "aquaticDefense"), PersistentDataType.FLOAT));
                    customPlayer.setAquaticDamage(customPlayer.getAquaticDamage() + itemData.get(new NamespacedKey(main, "aquaticDamage"), PersistentDataType.FLOAT));
                    customPlayer.setAerialDefense(customPlayer.getAerialDefense() + itemData.get(new NamespacedKey(main, "aerialDefense"), PersistentDataType.FLOAT));
                    customPlayer.setAerialDamage(customPlayer.getAerialDamage() + itemData.get(new NamespacedKey(main, "aerialDamage"), PersistentDataType.FLOAT));
                    customPlayer.setMeleeDefense(customPlayer.getMeleeDefense() + itemData.get(new NamespacedKey(main, "meleeDefense"), PersistentDataType.FLOAT));
                    customPlayer.setMeleeDamage(customPlayer.getMeleeDamage() + itemData.get(new NamespacedKey(main, "meleeDamage"), PersistentDataType.FLOAT));
                    customPlayer.setRangedDefense(customPlayer.getRangedDefense() + itemData.get(new NamespacedKey(main, "rangedDefense"), PersistentDataType.FLOAT));
                    customPlayer.setRangedDamage(customPlayer.getRangedDamage() + itemData.get(new NamespacedKey(main, "rangedDamage"), PersistentDataType.FLOAT));
                    customPlayer.setMagicDefense(customPlayer.getMagicDefense() + itemData.get(new NamespacedKey(main, "magicDefense"), PersistentDataType.FLOAT));
                    customPlayer.setMagicDamage(customPlayer.getMagicDamage() + itemData.get(new NamespacedKey(main, "magicDamage"), PersistentDataType.FLOAT));
                }
            }
        } catch (NullPointerException e) {
            //do nothing
        }

        //manage the player's health and stamina while updating the player's action bar and hearts
        manageHealthAndStamina(customPlayer);
        displayActionBar(player);
        updateHearts(player);
    }


    //remove the stats of the item the player is holding
    public void removePlayerStats(Player player, ItemStack item, String itemType) {

        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        try {

            //get the item's data
            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

            //if the item is of the correct itemType
            if (itemData.get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING).equalsIgnoreCase(itemType)) {

                //if the player meets the item requirements
                if (meetsItemRequirements(player, item, false)) {

                    //remove the item's stats from the player's stats
                    customPlayer.setDamage(customPlayer.getDamage() - itemData.get(new NamespacedKey(main, "damage"), PersistentDataType.FLOAT));
                    customPlayer.setAttackSpeed(customPlayer.getAttackSpeed() - itemData.get(new NamespacedKey(main, "attackSpeed"), PersistentDataType.FLOAT));
                    customPlayer.setCritChance(customPlayer.getCritChance() - itemData.get(new NamespacedKey(main, "critChance"), PersistentDataType.FLOAT));
                    customPlayer.setCritDamage(customPlayer.getCritDamage() - itemData.get(new NamespacedKey(main, "critDamage"), PersistentDataType.FLOAT));
                    customPlayer.setStrength(customPlayer.getStrength() - itemData.get(new NamespacedKey(main, "strength"), PersistentDataType.FLOAT));
                    customPlayer.setMaxHealth((int) (customPlayer.getMaxHealth() - itemData.get(new NamespacedKey(main, "health"), PersistentDataType.FLOAT)));
                    customPlayer.setHealthRegen((int) (customPlayer.getHealthRegen() - itemData.get(new NamespacedKey(main, "healthRegen"), PersistentDataType.FLOAT)));
                    customPlayer.setMaxStamina((int) (customPlayer.getMaxStamina() - itemData.get(new NamespacedKey(main, "stamina"), PersistentDataType.FLOAT)));
                    customPlayer.setStaminaRegen((int) (customPlayer.getStaminaRegen() - itemData.get(new NamespacedKey(main, "staminaRegen"), PersistentDataType.FLOAT)));
                    customPlayer.setDefense(customPlayer.getDefense() - itemData.get(new NamespacedKey(main, "defense"), PersistentDataType.FLOAT));
                    customPlayer.setSpeed(customPlayer.getSpeed() - itemData.get(new NamespacedKey(main, "speed"), PersistentDataType.FLOAT));
                    customPlayer.setInfernalDefense(customPlayer.getInfernalDefense() - itemData.get(new NamespacedKey(main, "infernalDefense"), PersistentDataType.FLOAT));
                    customPlayer.setInfernalDamage(customPlayer.getInfernalDamage() - itemData.get(new NamespacedKey(main, "infernalDamage"), PersistentDataType.FLOAT));
                    customPlayer.setUndeadDefense(customPlayer.getUndeadDefense() - itemData.get(new NamespacedKey(main, "undeadDefense"), PersistentDataType.FLOAT));
                    customPlayer.setUndeadDamage(customPlayer.getUndeadDamage() - itemData.get(new NamespacedKey(main, "undeadDamage"), PersistentDataType.FLOAT));
                    customPlayer.setAquaticDefense(customPlayer.getAquaticDefense() - itemData.get(new NamespacedKey(main, "aquaticDefense"), PersistentDataType.FLOAT));
                    customPlayer.setAquaticDamage(customPlayer.getAquaticDamage() - itemData.get(new NamespacedKey(main, "aquaticDamage"), PersistentDataType.FLOAT));
                    customPlayer.setAerialDefense(customPlayer.getAerialDefense() - itemData.get(new NamespacedKey(main, "aerialDefense"), PersistentDataType.FLOAT));
                    customPlayer.setAerialDamage(customPlayer.getAerialDamage() - itemData.get(new NamespacedKey(main, "aerialDamage"), PersistentDataType.FLOAT));
                    customPlayer.setMeleeDefense(customPlayer.getMeleeDefense() - itemData.get(new NamespacedKey(main, "meleeDefense"), PersistentDataType.FLOAT));
                    customPlayer.setMeleeDamage(customPlayer.getMeleeDamage() - itemData.get(new NamespacedKey(main, "meleeDamage"), PersistentDataType.FLOAT));
                    customPlayer.setRangedDefense(customPlayer.getRangedDefense() - itemData.get(new NamespacedKey(main, "rangedDefense"), PersistentDataType.FLOAT));
                    customPlayer.setRangedDamage(customPlayer.getRangedDamage() - itemData.get(new NamespacedKey(main, "rangedDamage"), PersistentDataType.FLOAT));
                    customPlayer.setMagicDefense(customPlayer.getMagicDefense() - itemData.get(new NamespacedKey(main, "magicDefense"), PersistentDataType.FLOAT));
                    customPlayer.setMagicDamage(customPlayer.getMagicDamage() - itemData.get(new NamespacedKey(main, "magicDamage"), PersistentDataType.FLOAT));

                }
            }
        } catch (NullPointerException e) {
            //do nothing
        }

        //manage the player's health and stamina while updating the player's action bar and hearts
        manageHealthAndStamina(customPlayer);
        displayActionBar(player);
        updateHearts(player);
    }


    //add stats for equipment bonuses
    public void calculateEquipmentBonuses(Player player){

        EquipmentBonuses[] equipmentBonuses = EquipmentBonuses.values();
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        //remove all active equipment bonuses
        for (EquipmentBonuses equipmentBonus : customPlayer.getActiveEquipmentBonuses()){

            //get the equipment bonus's stats
            HashMap<String, Integer> stats = equipmentBonus.getStats();
            HashMap<PotionEffectType, Integer> potionEffects = equipmentBonus.getPotionEffects();

            //remove the stats
            for (String stat : stats.keySet()){
                customPlayer.removeStat(stat, stats.get(stat));
            }

            //remove the potion effects
            for (PotionEffectType potionEffect : potionEffects.keySet()){
                player.removePotionEffect(potionEffect);
            }
        }

        //clear the active equipment bonuses
        customPlayer.clearEquipmentBonuses();


        //loop through all equipment bonuses
        for (EquipmentBonuses equipmentBonus : equipmentBonuses){

            //recalculate all the equipment bonuses
            Item offHand = equipmentBonus.getOffhand();
            Item helmet = equipmentBonus.getHelmet();
            Item chestplate = equipmentBonus.getChestplate();
            Item leggings = equipmentBonus.getLeggings();
            Item boots = equipmentBonus.getBoots();
            HashMap<String, Integer> stats = equipmentBonus.getStats();
            HashMap<PotionEffectType, Integer> potionEffects = equipmentBonus.getPotionEffects();

            //if the player has the correct equipment equipped
            if (hasEquipment(player, offHand, helmet, chestplate, leggings, boots)){

                //add the equipment bonus
                customPlayer.addEquipmentBonus(equipmentBonus);

                //add the stats
                for (String stat : stats.keySet()){
                    customPlayer.addStat(stat, stats.get(stat));
                }

                //add the potion effects
                for (PotionEffectType potionEffectType : potionEffects.keySet()){
                    player.addPotionEffect(new PotionEffect(potionEffectType, -1, potionEffects.get(potionEffectType)));
                }
            }
        }
    }

    //checks if the player is wearing the correct equipment
    public boolean hasEquipment(Player player, Item offhand, Item helmet, Item chestplate, Item leggings, Item boots){

        if (offhand != null){
            try {
                if (!(player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)
                        .equals(offhand.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)))) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }

        if (helmet != null){
            try {
                if (!(player.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)
                        .equals(helmet.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)))) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }

        if (chestplate != null){
            try {
                if (!(player.getInventory().getChestplate().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)
                        .equals(chestplate.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)))) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }

        if (leggings != null){
            try {
                if (!(player.getInventory().getLeggings().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)
                        .equals(leggings.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)))) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }

        if (boots != null){
            try {
                if (!(player.getInventory().getBoots().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)
                        .equals(boots.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)))) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }

        return true;
    }



}