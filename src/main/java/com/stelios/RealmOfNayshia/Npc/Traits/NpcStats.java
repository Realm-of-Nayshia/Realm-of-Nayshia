package com.stelios.RealmOfNayshia.Npc.Traits;

import com.stelios.RealmOfNayshia.Main;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

import java.util.HashMap;
import java.util.UUID;

public class NpcStats extends Trait {
    public NpcStats() {
        super("npcstats");
        main = Main.getPlugin(Main.class);
    }

    Main main;

    //stats
    @Persist("xp") float xp = 0.0f;
    @Persist("critDamage") float critDamage = 0.0f;
    @Persist("critChance") float critChance = 0.0f;
    @Persist("strength") float strength = 0.0f;
    @Persist("defense") float defense = 0.0f;
    @Persist("infernalDefense") float infernalDefense = 0.0f;
    @Persist("infernalDamage") float infernalDamage = 0.0f;
    @Persist("undeadDefense") float undeadDefense = 0.0f;
    @Persist("undeadDamage") float undeadDamage = 0.0f;
    @Persist("aquaticDefense") float aquaticDefense = 0.0f;
    @Persist("aquaticDamage") float aquaticDamage = 0.0f;
    @Persist("aerialDefense") float aerialDefense = 0.0f;
    @Persist("aerialDamage") float aerialDamage = 0.0f;
    @Persist("meleeDefense") float meleeDefense = 0.0f;
    @Persist("meleeDamage") float meleeDamage = 0.0f;
    @Persist("rangedDefense") float rangedDefense = 0.0f;
    @Persist("rangedDamage") float rangedDamage = 0.0f;
    @Persist("magicDefense") float magicDefense = 0.0f;
    @Persist("magicDamage") float magicDamage = 0.0f;
    @Persist("faction") String faction = "None";
    @Persist("playerDamages") HashMap<UUID, Float> playerDamages = new HashMap<>();


    //getters
    public float getXp() {
        return xp;
    }
    public float getCritDamage() {
        return critDamage;
    }
    public float getCritChance() {
        return critChance;
    }
    public float getStrength() {
        return strength;
    }
    public float getDefense() {
        return defense;
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
    public String getFaction() {
        return faction;
    }
    public HashMap<UUID, Float> getPlayerDamages() {
        return playerDamages;
    }


    //setting a player's damage
    public void addPlayerDamage(UUID uuid, float damage){

        //if the player has already damaged the npc, add the damage to their total
        if (playerDamages.containsKey(uuid)){
            playerDamages.put(uuid, playerDamages.get(uuid) + damage);

        //if the player hasn't damaged the npc, add them to the list
        } else {
            playerDamages.put(uuid, damage);
        }
    }


    //clearing player's data
    public void clearPlayerDamages(){
        playerDamages.clear();
    }


    //setting a stat
    public void setStat(String name, float value){
        switch (name) {
            case "xp" -> xp = value;
            case "critdamage" -> critDamage = value;
            case "critchance" -> critChance = value;
            case "strength" -> strength = value;
            case "defense" -> defense = value;
            case "infernaldefense" -> infernalDefense = value;
            case "infernaldamage" -> infernalDamage = value;
            case "undeaddefense" -> undeadDefense = value;
            case "undeaddamage" -> undeadDamage = value;
            case "aquaticdefense" -> aquaticDefense = value;
            case "aquaticdamage" -> aquaticDamage = value;
            case "aerialdefense" -> aerialDefense = value;
            case "aerialdamage" -> aerialDamage = value;
            case "meleedefense" -> meleeDefense = value;
            case "meleedamage" -> meleeDamage = value;
            case "rangeddefense" -> rangedDefense = value;
            case "rangeddamage" -> rangedDamage = value;
            case "magicdefense" -> magicDefense = value;
            case "magicdamage" -> magicDamage = value;
        }
    }


    //setting the faction
    public void setFaction(String faction) {
        this.faction = faction;
    }


    //resetting all stats
    public void resetStats(){
        xp = 0.0f;
        critDamage = 0.0f;
        critChance = 0.0f;
        strength = 0.0f;
        defense = 0.0f;
        infernalDefense = 0.0f;
        infernalDamage = 0.0f;
        undeadDefense = 0.0f;
        undeadDamage = 0.0f;
        aquaticDefense = 0.0f;
        aquaticDamage = 0.0f;
        aerialDefense = 0.0f;
        aerialDamage = 0.0f;
        meleeDefense = 0.0f;
        meleeDamage = 0.0f;
        rangedDefense = 0.0f;
        rangedDamage = 0.0f;
        magicDefense = 0.0f;
        magicDamage = 0.0f;
        faction = "None";
        clearPlayerDamages();
    }

}
