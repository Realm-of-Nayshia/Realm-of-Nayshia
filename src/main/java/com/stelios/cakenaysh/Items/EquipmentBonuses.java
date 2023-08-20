package com.stelios.cakenaysh.Items;

import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public enum EquipmentBonuses {

    SPARTA_SET(null, CustomItems.getItemFromName("HELM_OF_SPARTA"), CustomItems.getItemFromName("CHEST_OF_SPARTA"),
            CustomItems.getItemFromName("LEGS_OF_SPARTA"), CustomItems.getItemFromName("BOOTS_OF_SPARTA"),
            new HashMap<String, Integer>() {{
                put("strength", 10);
            }}, new HashMap<PotionEffectType, Integer>() {{
                put(PotionEffectType.INCREASE_DAMAGE, 0);
                put(PotionEffectType.DAMAGE_RESISTANCE, 0);
            }}),

    BLANK_SET(null, CustomItems.getItemFromName("BLANK_HELMET"), CustomItems.getItemFromName("BLANK_CHESTPLATE"),
            CustomItems.getItemFromName("BLANK_LEGGINGS"), CustomItems.getItemFromName("BLANK_BOOTS"),
            new HashMap<String, Integer>() {{
                put("damage", 10);
                put("strength", 10);
            }}, new HashMap<PotionEffectType, Integer>() {{
                put(PotionEffectType.INCREASE_DAMAGE, 2);
                put(PotionEffectType.DAMAGE_RESISTANCE, 0);
            }}),


    ;

    private final Item offhand;
    private final Item helmet;
    private final Item chestplate;
    private final Item leggings;
    private final Item boots;
    private final HashMap<String, Integer> stats;
    private final HashMap<PotionEffectType, Integer> potionEffects;

    EquipmentBonuses(Item offhand, Item helmet, Item chestplate, Item leggings, Item boots,
                     HashMap<String, Integer> stats, HashMap<PotionEffectType, Integer> potionEffects){
        this.offhand = offhand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.stats = stats;
        this.potionEffects = potionEffects;
    }

    //getters
    public Item getOffhand() {
        return offhand;
    }
    public Item getHelmet() {
        return helmet;
    }
    public Item getChestplate() {
        return chestplate;
    }
    public Item getLeggings() {
        return leggings;
    }
    public Item getBoots() {
        return boots;
    }
    public HashMap<String, Integer> getStats() {
        return stats;
    }
    public HashMap<PotionEffectType, Integer> getPotionEffects() {
        return potionEffects;
    }
}
