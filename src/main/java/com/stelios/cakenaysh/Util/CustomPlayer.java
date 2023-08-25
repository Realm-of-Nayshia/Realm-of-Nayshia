package com.stelios.cakenaysh.Util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.stelios.cakenaysh.Events.SpeedChangedEvent;
import com.stelios.cakenaysh.Events.XpChangedEvent;
import com.stelios.cakenaysh.Items.EquipmentBonuses;
import com.stelios.cakenaysh.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CustomPlayer {

    private final Main main;


    //info from database
    private final UUID uuid;
    private String rank;
    private String faction;
    private String joinDate;
    private double playTime;
    private int level;
    private int investmentPoints;
    private int xp;
    private int staminaRegen;
    private int stamina;
    private int maxStamina;
    private int healthRegen;
    private double health;
    private int maxHealth;
    private int meleeProficiency;
    private int rangedProficiency;
    private int armorProficiency;
    private int wilsonCoin;
    private int piety;
    private int charisma;
    private int deception;
    private int agility;
    private int luck;
    private int stealth;

    //info from items
    private float damage;
    private float bonusDamage;
    private float attackSpeed;
    private float critDamage;
    private float critChance;
    private float strength;
    private float defense;
    private float speed;
    private float infernalDefense;
    private float infernalDamage;
    private float undeadDefense;
    private float undeadDamage;
    private float aquaticDefense;
    private float aquaticDamage;
    private float aerialDefense;
    private float aerialDamage;
    private float meleeDefense;
    private float meleeDamage;
    private float rangedDefense;
    private float rangedDamage;
    private float magicDefense;
    private float magicDamage;

    //equipment bonuses
    private ArrayList<EquipmentBonuses> activeEquipmentBonuses = new ArrayList<>();

    public CustomPlayer(Main main, UUID uuid) {
        this.main = main;
        this.uuid = uuid;

        MongoCollection<Document> playerStats = main.getDatabase().getPlayerStats();

        //create a unique uuid index
        playerStats.createIndex(Indexes.ascending("uuid"), new IndexOptions().unique(true));

        Document filter = new Document("uuid", uuid.toString());
        Document existingPlayer = playerStats.find(filter).first();

        //if the player is in the database
        if (existingPlayer != null) {

            //get the player's stats
            rank = existingPlayer.getString("rank");
            faction = existingPlayer.getString("faction");
            joinDate = existingPlayer.getString("joinDate");
            playTime = existingPlayer.getDouble("playTime");
            level = existingPlayer.getInteger("level");
            investmentPoints = existingPlayer.getInteger("investmentPoints");
            xp = existingPlayer.getInteger("xp");
            staminaRegen = existingPlayer.getInteger("staminaRegen");
            stamina = existingPlayer.getInteger("stamina");
            maxStamina = existingPlayer.getInteger("maxStamina");
            healthRegen = existingPlayer.getInteger("healthRegen");
            health = existingPlayer.getDouble("health");
            maxHealth = existingPlayer.getInteger("maxHealth");
            meleeProficiency = existingPlayer.getInteger("meleeProficiency");
            rangedProficiency = existingPlayer.getInteger("rangedProficiency");
            armorProficiency = existingPlayer.getInteger("armorProficiency");
            wilsonCoin = existingPlayer.getInteger("wilsonCoin");
            piety = existingPlayer.getInteger("piety");
            charisma = existingPlayer.getInteger("charisma");
            deception = existingPlayer.getInteger("deception");
            agility = existingPlayer.getInteger("agility");
            luck = existingPlayer.getInteger("luck");
            stealth = existingPlayer.getInteger("stealth");

        //if the player is not in the database
        } else {

            //set the player's stats to default
            rank = "Guest";
            faction = "None";
            joinDate = Calendar.getInstance().getTime().toString();
            playTime = 0;
            level = 0;
            investmentPoints = 0;
            xp = 0;
            staminaRegen = 1;
            stamina = 100;
            maxStamina = 100;
            healthRegen = 1;
            health = 100;
            maxHealth = 100;
            meleeProficiency = 0;
            rangedProficiency = 0;
            armorProficiency = 0;
            wilsonCoin = 0;
            piety = 0;
            charisma = 0;
            deception = 0;
            agility = 0;
            luck = 0;
            stealth = 0;

            //add the player to the database with the default stats
            Document document = new Document("uuid", uuid.toString())
                    .append("name", Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName())
                    .append("rank", rank)
                    .append("faction", faction)
                    .append("joinDate", joinDate)
                    .append("playTime", playTime)
                    .append("level", level)
                    .append("investmentPoints", investmentPoints)
                    .append("xp", xp)
                    .append("staminaRegen", staminaRegen)
                    .append("stamina", stamina)
                    .append("maxStamina", maxStamina)
                    .append("healthRegen", healthRegen)
                    .append("health", health)
                    .append("maxHealth", maxHealth)
                    .append("meleeProficiency", meleeProficiency)
                    .append("rangedProficiency", rangedProficiency)
                    .append("armorProficiency", armorProficiency)
                    .append("wilsonCoin", wilsonCoin)
                    .append("piety", piety)
                    .append("charisma", charisma)
                    .append("deception", deception)
                    .append("agility", agility)
                    .append("luck", luck)
                    .append("stealth", stealth);
            playerStats.insertOne(document);
        }
    }

    ////getter methods
    public String getRank() {
        return rank;
    }
    public String getFaction() {
        return faction;
    }
    public String getJoinDate() {
        return joinDate;
    }
    public double getPlayTime() {
        return playTime;
    }
    public int getLevel() {
        return level;
    }
    public int getInvestmentPoints() {
        return investmentPoints;
    }
    public int getXp() {
        return xp;
    }
    public int getStaminaRegen() {
        return staminaRegen;
    }
    public int getStamina() {
        return stamina;
    }
    public int getMaxStamina() {
        return maxStamina;
    }
    public int getHealthRegen() {
        return healthRegen;
    }
    public double getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getMeleeProficiency() {
        return meleeProficiency;
    }
    public int getRangedProficiency() {
        return rangedProficiency;
    }
    public int getArmorProficiency() {
        return armorProficiency;
    }
    public int getWilsonCoin() {
        return wilsonCoin;
    }
    public int getPiety() {
        return piety;
    }
    public int getCharisma() {
        return charisma;
    }
    public int getDeception() {
        return deception;
    }
    public int getAgility() {
        return agility;
    }
    public int getLuck() {
        return luck;
    }
    public int getStealth() {
        return stealth;
    }


    public float getDamage() {
        return damage;
    }
    public float getBonusDamage() {
        return bonusDamage;
    }
    public float getAttackSpeed() {
        return attackSpeed;
    }
    public float getCritChance() {
        return critChance;
    }
    public float getCritDamage() {
        return critDamage;
    }
    public float getStrength() {
        return strength;
    }
    public float getDefense() {
        return defense;
    }
    public float getSpeed() {
        return speed;
    }
    public float getInfernalDefense() {
        return infernalDefense;
    }
    public float getInfernalDamage() {
        return infernalDamage;
    }
    public float getUndeadDefense() {
        return undeadDefense;
    }
    public float getUndeadDamage() {
        return undeadDamage;
    }
    public float getAquaticDefense() {
        return aquaticDefense;
    }
    public float getAquaticDamage() {
        return aquaticDamage;
    }
    public float getAerialDefense() {
        return aerialDefense;
    }
    public float getAerialDamage() {
        return aerialDamage;
    }
    public float getMeleeDefense() {
        return meleeDefense;
    }
    public float getMeleeDamage() {
        return meleeDamage;
    }
    public float getRangedDefense() {
        return rangedDefense;
    }
    public float getRangedDamage() {
        return rangedDamage;
    }
    public float getMagicDefense() {
        return magicDefense;
    }
    public float getMagicDamage() {
        return magicDamage;
    }


    //get all the attributes of the player
    //used for the /attributes command
    public String getAttributes() {
        return "\n" + "Rank: " + rank +
                "   Faction: " + faction + "\n\n" +
                "Play Time: " + playTime + "\n\n" +
                "Join Date: " + joinDate + "\n" +
                "Level: " + level +
                "   Investment Points: " + investmentPoints +
                "   XP: " + xp + "\n\n" +
                "Stamina Regen: " + staminaRegen +
                "   Stamina: " + stamina +
                "   Max Stamina: " + maxStamina + "\n" +
                "Health Regen: " + healthRegen +
                "   Health: " + (int) health +
                "   Max Health: " + maxHealth + "\n\n" +
                "Proficiencies \n" +
                "Melee: " + meleeProficiency +
                "   Ranged: " + rangedProficiency +
                "   Armor: " + armorProficiency + "\n\n" +
                "WilsonCoin: " + wilsonCoin + "\n\n" +
                "Piety: " + piety +
                "   Charisma: " + charisma +
                "   Deception: " + deception + "\n" +
                "Agility: " + agility +
                "   Luck: " + luck +
                "   Stealth: " + stealth + "\n";
    }


    //does the player meet the specified stat level
    public boolean meetsStatRequirement(String stat, Float numberValue, String stringValue) {

        //if the stat is a string
        if (stringValue != null) {

            switch (stat) {
                case "rank" -> {
                    if (rank.equals(stringValue)) {
                        return true;
                    }
                }
                case "faction" -> {
                    if (faction.equals(stringValue)) {
                        return true;
                    }
                }
                case "joinDate" -> {
                    if (joinDate.equals(stringValue)) {
                        return true;
                    }
                }
                default -> {
                    return false;
                }
            }
        }

        //if the stat is a number
        switch (stat) {
            case "playTime" -> {
                if (playTime >= numberValue) {
                    return true;
                }
            }
            case "level" -> {
                if (level >= numberValue) {
                    return true;
                }
            }
            case "investmentPoints" -> {
                if (investmentPoints >= numberValue) {
                    return true;
                }
            }
            case "xp" -> {
                if (xp >= numberValue) {
                    return true;
                }
            }
            case "staminaRegen" -> {
                if (staminaRegen >= numberValue) {
                    return true;
                }
            }
            case "stamina" -> {
                if (stamina >= numberValue) {
                    return true;
                }
            }
            case "maxStamina" -> {
                if (maxStamina >= numberValue) {
                    return true;
                }
            }
            case "healthRegen" -> {
                if (healthRegen >= numberValue) {
                    return true;
                }
            }
            case "health" -> {
                if (health >= numberValue) {
                    return true;
                }
            }
            case "maxHealth" -> {
                if (maxHealth >= numberValue) {
                    return true;
                }
            }
            case "meleeProficiency" -> {
                if (meleeProficiency >= numberValue) {
                    return true;
                }
            }
            case "rangedProficiency" -> {
                if (rangedProficiency >= numberValue) {
                    return true;
                }
            }
            case "armorProficiency" -> {
                if (armorProficiency >= numberValue) {
                    return true;
                }
            }
            case "wilsonCoin" -> {
                if (wilsonCoin >= numberValue) {
                    return true;
                }
            }
            case "piety" -> {
                if (piety >= numberValue) {
                    return true;
                }
            }
            case "charisma" -> {
                if (charisma >= numberValue) {
                    return true;
                }
            }
            case "deception" -> {
                if (deception >= numberValue) {
                    return true;
                }
            }
            case "agility" -> {
                if (agility >= numberValue) {
                    return true;
                }
            }
            case "luck" -> {
                if (luck >= numberValue) {
                    return true;
                }
            }
            case "stealth" -> {
                if (stealth >= numberValue) {
                    return true;
                }
            }
            case "damage" -> {
                if (damage >= numberValue) {
                    return true;
                }
            }
            case "bonusDamage" -> {
                if (bonusDamage >= numberValue) {
                    return true;
                }
            }
            case "attackSpeed" -> {
                if (attackSpeed >= numberValue) {
                    return true;
                }
            }
            case "critChance" -> {
                if (critChance >= numberValue) {
                    return true;
                }
            }
            case "critDamage" -> {
                if (critDamage >= numberValue) {
                    return true;
                }
            }
            case "strength" -> {
                if (strength >= numberValue) {
                    return true;
                }
            }
            case "defense" -> {
                if (defense >= numberValue) {
                    return true;
                }
            }
            case "speed" -> {
                if (speed >= numberValue) {
                    return true;
                }
            }
            case "infernalDefense" -> {
                if (infernalDefense >= numberValue) {
                    return true;
                }
            }
            case "infernalDamage" -> {
                if (infernalDamage >= numberValue) {
                    return true;
                }
            }
            case "undeadDefense" -> {
                if (undeadDefense >= numberValue) {
                    return true;
                }
            }
            case "undeadDamage" -> {
                if (undeadDamage >= numberValue) {
                    return true;
                }
            }
            case "aquaticDefense" -> {
                if (aquaticDefense >= numberValue) {
                    return true;
                }
            }
            case "aquaticDamage" -> {
                if (aquaticDamage >= numberValue) {
                    return true;
                }
            }
            case "aerialDefense" -> {
                if (aerialDefense >= numberValue) {
                    return true;
                }
            }
            case "aerialDamage" -> {
                if (aerialDamage >= numberValue) {
                    return true;
                }
            }
            case "meleeDefense" -> {
                if (meleeDefense >= numberValue) {
                    return true;
                }
            }
            case "meleeDamage" -> {
                if (meleeDamage >= numberValue) {
                    return true;
                }
            }
            case "rangedDefense" -> {
                if (rangedDefense >= numberValue) {
                    return true;
                }
            }
            case "rangedDamage" -> {
                if (rangedDamage >= numberValue) {
                    return true;
                }
            }
            case "magicDefense" -> {
                if (magicDefense >= numberValue) {
                    return true;
                }
            }
            case "magicDamage" -> {
                if (magicDamage >= numberValue) {
                    return true;
                }
            }
            default -> {
                return false;
            }
        }

        return false;
    }


    //resets all the database attributes of the player
    public void resetAttributes(Player player) {
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());
        customPlayer.setRank("Guest");
        customPlayer.setFaction("None");
        customPlayer.setLevel(0);
        customPlayer.setInvestmentPoints(0);
        customPlayer.setXp(0);
        customPlayer.setStaminaRegen(1);
        customPlayer.setStamina(100);
        customPlayer.setMaxStamina(100);
        customPlayer.setHealthRegen(1);
        customPlayer.setHealth(100);
        customPlayer.setMaxHealth(100);
        customPlayer.setMeleeProficiency(0);
        customPlayer.setRangedProficiency(0);
        customPlayer.setArmorProficiency(0);
        customPlayer.setWilsonCoin(0);
        customPlayer.setPiety(0);
        customPlayer.setCharisma(0);
        customPlayer.setDeception(0);
        customPlayer.setAgility(0);
        customPlayer.setLuck(0);
        customPlayer.setStealth(0);
    }

    //saves all the attributes to the database
    public void saveAttributesToDatabase(Player player){

        Document document = new Document("uuid", uuid.toString())
                .append("rank", rank)
                .append("faction", faction)
                .append("joinDate", joinDate)
                .append("playTime", (double) player.getStatistic(Statistic.PLAY_ONE_MINUTE) / (20*60*60))
                .append("level", level)
                .append("investmentPoints", investmentPoints)
                .append("xp", xp)
                .append("staminaRegen", staminaRegen)
                .append("stamina", stamina)
                .append("maxStamina", maxStamina)
                .append("healthRegen", healthRegen)
                .append("health", health)
                .append("maxHealth", maxHealth)
                .append("meleeProficiency", meleeProficiency)
                .append("rangedProficiency", rangedProficiency)
                .append("armorProficiency", armorProficiency)
                .append("wilsonCoin", wilsonCoin)
                .append("piety", piety)
                .append("charisma", charisma)
                .append("deception", deception)
                .append("agility", agility)
                .append("luck", luck)
                .append("stealth", stealth);

        main.getDatabase().getPlayerStats().updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", document));
    }

    //gets the amount of xp needed to level up
    public int howManyLevelUps(){

        if (level < 50){

            ArrayList<Integer> xpRequirementsPerLevel = new ArrayList<>(Arrays.asList(0, 10, 15, 20, 35, 50, 75, 115, 170, 255, 385, 480, 600, 750, 940,
                    1170, 1470, 1800, 2300, 2850, 3600, 4100, 4750, 5450, 6250, 7200, 8300, 9500, 11000, 12600, 14500, 16000, 17500, 19200, 21200,
                    23300, 25600, 28200, 31000, 34000, 37500, 41300, 45500, 50000, 55000, 60000, 67000, 73000, 80000, 89000, 100000));

            //if the player has enough xp to level up
            for (int i = level+1; i < xpRequirementsPerLevel.size(); i++){
                if (xp > xpRequirementsPerLevel.get(i) && xp > xpRequirementsPerLevel.get(i + 1)){

                } else if (xp >= xpRequirementsPerLevel.get(i) && xp < xpRequirementsPerLevel.get(i + 1)){
                    return i-level;
                }
            }

            return 0;
        }

        return 0;
    }


    //equipment bonus management
    public ArrayList<EquipmentBonuses> getActiveEquipmentBonuses() {
        return activeEquipmentBonuses;
    }
    public void addEquipmentBonus(EquipmentBonuses equipmentBonus) {
        activeEquipmentBonuses.add(equipmentBonus);
    }
    public void clearEquipmentBonuses() {
        activeEquipmentBonuses.clear();
    }


    ////setters
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void setBonusDamage(float bonusDamage){
        this.bonusDamage = bonusDamage;
    }
    public void setAttackSpeed(float attackSpeed){
        this.attackSpeed = attackSpeed;
    }
    public void setCritChance(float critChance){
        this.critChance = critChance;
    }
    public void setCritDamage(float critDamage){
        this.critDamage = critDamage;
    }
    public void setStrength(float strength){
        this.strength = strength;
    }
    public void setDefense(float defense){
        this.defense = defense;
    }
    public void setSpeed(float speed){
        this.speed = speed;
        Bukkit.getPluginManager().callEvent(new SpeedChangedEvent(Bukkit.getPlayer(uuid), speed));
    }
    public void setInfernalDefense(float infernalDefense){
        this.infernalDefense = infernalDefense;
    }
    public void setInfernalDamage(float infernalDamage){
        this.infernalDamage = infernalDamage;
    }
    public void setUndeadDefense(float undeadDefense){
        this.undeadDefense = undeadDefense;
    }
    public void setUndeadDamage(float undeadDamage){
        this.undeadDamage = undeadDamage;
    }
    public void setAquaticDefense(float aquaticDefense){
        this.aquaticDefense = aquaticDefense;
    }
    public void setAquaticDamage(float aquaticDamage){
        this.aquaticDamage = aquaticDamage;
    }
    public void setAerialDefense(float aerialDefense){
        this.aerialDefense = aerialDefense;
    }
    public void setAerialDamage(float aerialDamage){
        this.aerialDamage = aerialDamage;
    }
    public void setMeleeDefense(float meleeDefense){
        this.meleeDefense = meleeDefense;
    }
    public void setMeleeDamage(float meleeDamage){
        this.meleeDamage = meleeDamage;
    }
    public void setRangedDefense(float rangedDefense){
        this.rangedDefense = rangedDefense;
    }
    public void setRangedDamage(float rangedDamage){
        this.rangedDamage = rangedDamage;
    }
    public void setMagicDefense(float magicDefense){
        this.magicDefense = magicDefense;
    }
    public void setMagicDamage(float magicDamage){
        this.magicDamage = magicDamage;
    }


    //adds the specified stat and amount to the player for a specified amount of time
    public void addStatWithDelay(String stat, int amount, long ticks){

        switch (stat.toLowerCase()) {
            case "health" -> health += amount;
            case "damage" -> {
                damage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        damage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "bonusdamage" -> {
                bonusDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        bonusDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "critchance" -> {
                critChance += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        critChance -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "critdamage" -> {
                critDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        critDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "strength" -> {
                strength += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        strength -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "defense" -> {
                defense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        defense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "speed" -> {
                setSpeed(speed + amount);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        setSpeed(speed - amount);
                    }
                }.runTaskLater(main, ticks);
            }
            case "infernaldefense" -> {
                infernalDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        infernalDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "infernaldamage" -> {
                infernalDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        infernalDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "undeaddefense" -> {
                undeadDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        undeadDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "undeaddamage" -> {
                undeadDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        undeadDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "aquaticdefense" -> {
                aquaticDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        aquaticDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "aquaticdamage" -> {
                aquaticDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        aquaticDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "aerialdefense" -> {
                aerialDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        aerialDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "aerialdamage" -> {
                aerialDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        aerialDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "meleedefense" -> {
                meleeDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        meleeDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "meleedamage" -> {
                meleeDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        meleeDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "rangeddefense" -> {
                rangedDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        rangedDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "rangeddamage" -> {
                rangedDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        rangedDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "magicdefense" -> {
                magicDefense += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        magicDefense -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
            case "magicdamage" -> {
                magicDamage += amount;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        magicDamage -= amount;
                    }
                }.runTaskLater(main, ticks);
            }
        }
    }


    //adds a stat permanently to the player
    public void addPermanentStat(String stat, int amount){

        switch (stat) {
            case ("investmentPoints") -> investmentPoints += amount;
            case ("staminaRegen") -> staminaRegen += amount;
            case ("maxStamina") -> maxStamina += amount;
            case ("healthRegen") -> healthRegen += amount;
            case ("maxHealth") -> maxHealth += amount;
            case ("meleeProficiency") -> meleeProficiency += amount;
            case ("rangedProficiency") -> rangedProficiency += amount;
            case ("armorProficiency") -> armorProficiency += amount;
            case ("wilsonCoin") -> wilsonCoin += amount;
            case ("piety") -> piety += amount;
            case ("charisma") -> charisma += amount;
            case ("deception") -> deception += amount;
            case ("agility") -> agility += amount;
            case ("luck") -> luck += amount;
            case ("stealth") -> stealth += amount;
        }
    }


    //adds the specified stat and amount to the player
    public void addStat(String stat, int amount){

        switch (stat) {
            case "maxhealth" -> maxHealth += amount;
            case "damage" -> damage += amount;
            case "bonusDamage" -> bonusDamage += amount;
            case "critChance" -> critChance += amount;
            case "critDamage" -> critDamage += amount;
            case "strength" -> strength += amount;
            case "defense" -> defense += amount;
            case "speed" -> setSpeed(speed + amount);
            case "infernalDefense" -> infernalDefense += amount;
            case "infernalDamage" -> infernalDamage += amount;
            case "undeadDefense" -> undeadDefense += amount;
            case "undeadDamage" -> undeadDamage += amount;
            case "aquaticDefense" -> aquaticDefense += amount;
            case "aquaticDamage" -> aquaticDamage += amount;
            case "aerialDefense" -> aerialDefense += amount;
            case "aerialDamage" -> aerialDamage += amount;
            case "meleeDefense" -> meleeDefense += amount;
            case "meleeDamage" -> meleeDamage += amount;
            case "rangedDefense" -> rangedDefense += amount;
            case "rangedDamage" -> rangedDamage += amount;
            case "magicDefense" -> magicDefense += amount;
            case "magicDamage" -> magicDamage += amount;
        }
    }


    //removes the specified stat and amount to the player
    public void removeStat(String stat, int amount){

        switch (stat) {
            case "maxhealth" -> maxHealth -= amount;
            case "damage" -> damage -= amount;
            case "bonusDamage" -> bonusDamage -= amount;
            case "critChance" -> critChance -= amount;
            case "critDamage" -> critDamage -= amount;
            case "strength" -> strength -= amount;
            case "defense" -> defense -= amount;
            case "speed" -> setSpeed(speed - amount);
            case "infernalDefense" -> infernalDefense -= amount;
            case "infernalDamage" -> infernalDamage -= amount;
            case "undeadDefense" -> undeadDefense -= amount;
            case "undeadDamage" -> undeadDamage -= amount;
            case "aquaticDefense" -> aquaticDefense -= amount;
            case "aquaticDamage" -> aquaticDamage -= amount;
            case "aerialDefense" -> aerialDefense -= amount;
            case "aerialDamage" -> aerialDamage -= amount;
            case "meleeDefense" -> meleeDefense -= amount;
            case "meleeDamage" -> meleeDamage -= amount;
            case "rangedDefense" -> rangedDefense -= amount;
            case "rangedDamage" -> rangedDamage -= amount;
            case "magicDefense" -> magicDefense -= amount;
            case "magicDamage" -> magicDamage -= amount;
        }
    }

    //setting player stats locally within the class
    public void setRank(String rank) {
        this.rank = rank;
    }
    public void setFaction(String faction) {
        this.faction = faction;
    }
    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }
    public void setPlayTime(double playTime) {
        this.playTime = playTime;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setInvestmentPoints(int investmentPoints) {
        this.investmentPoints = investmentPoints;
    }
    public void setXp(int xp) {
        int oldXp = this.xp;
        this.xp = xp;

        //call the xp gain event
        main.getServer().getPluginManager().callEvent(new XpChangedEvent(Bukkit.getPlayer(uuid), xp-oldXp, null));
    }
    public void setStaminaRegen(int staminaRegen) {
        this.staminaRegen = staminaRegen;
    }
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }
    public void setHealthRegen(int healthRegen) {
        this.healthRegen = healthRegen;
    }
    public void setHealth(double health) {
        this.health = health;
    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    public void setMeleeProficiency(int meleeProficiency) {
        this.meleeProficiency = meleeProficiency;
    }
    public void setRangedProficiency(int rangedProficiency) {
        this.rangedProficiency = rangedProficiency;
    }
    public void setArmorProficiency(int armorProficiency) {
        this.armorProficiency = armorProficiency;
    }
    public void setWilsonCoin(int wilsonCoin) {
        this.wilsonCoin = wilsonCoin;
    }
    public void setPiety(int piety) {
        this.piety = piety;
    }
    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }
    public void setDeception(int deception) {
        this.deception = deception;
    }
    public void setAgility(int agility) {
        this.agility = agility;
    }
    public void setLuck(int luck) {
        this.luck = luck;
    }
    public void setStealth(int stealth) {
        this.stealth = stealth;
    }


    //adding stats to the player locally within the class
    public void addLevels(int level) {
        this.level += level;
    }
    public void addInvestmentPoints(int investmentPoints) {
        this.investmentPoints += investmentPoints;
    }
    public void addXp(int xp) {
        this.xp += xp;

        //call the xp gain event
        main.getServer().getPluginManager().callEvent(new XpChangedEvent(Bukkit.getPlayer(uuid), xp, null));
    }
    public void addStaminaRegen(int staminaRegen) {
        this.staminaRegen += staminaRegen;
    }
    public void addStamina(int stamina) {
        this.stamina += stamina;
    }
    public void addMaxStamina(int maxStamina) {
        this.maxStamina += maxStamina;
    }
    public void addHealthRegen(int healthRegen) {
        this.healthRegen += healthRegen;
    }
    public void addHealth(float health) {
        this.health += health;
    }
    public void addMaxHealth(int maxHealth) {
        this.maxHealth += maxHealth;
    }
    public void addMeleeProficiency(int meleeProficiency) {
        this.meleeProficiency += meleeProficiency;
    }
    public void addRangedProficiency(int rangedProficiency) {
        this.rangedProficiency += rangedProficiency;
    }
    public void addArmorProficiency(int armorProficiency) {
        this.armorProficiency += armorProficiency;
    }
    public void addWilsonCoin(int wilsonCoin) {
        this.wilsonCoin += wilsonCoin;
    }
    public void addPiety(int piety) {
        this.piety += piety;
    }
    public void addCharisma(int charisma) {
        this.charisma += charisma;
    }
    public void addDeception(int deception) {
        this.deception += deception;
    }
    public void addAgility(int agility) {
        this.agility += agility;
    }
    public void addLuck(int luck) {
        this.luck += luck;
    }
    public void addStealth(int stealth) {
        this.stealth += stealth;
    }


}
