package com.stelios.cakenaysh.Items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.cakenaysh.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleItem extends Item {

    private final float damage;
    private final float attackSpeed;
    private final float critDamage;
    private final float critChance;
    private final float strength;
    private final float health;
    private final float healthRegen;
    private final float stamina;
    private final float staminaRegen;
    private final float defense;
    private final float speed;
    private final float infernalDefense;
    private final float infernalDamage;
    private final float undeadDefense;
    private final float undeadDamage;
    private final float aquaticDefense;
    private final float aquaticDamage;
    private final float aerialDefense;
    private final float aerialDamage;
    private final float meleeDefense;
    private final float meleeDamage;
    private final float rangedDefense;
    private final float rangedDamage;
    private final float magicDefense;
    private final float magicDamage;
    private final int meleeProficiency;
    private final int rangedProficiency;
    private final int armorProficiency;
    private final PotionEffect[] potionEffects;


    //@param material: The material of the item being built.
    //@param amount: The amount of the item being built.
    //@param unstackable: Whether the item being built is stackable or not.
    //@param name: The name of the item being built.
    //@param itemType: The type of item being built.
    //@param damage: The damage of the item being built.
    //@param attackSpeed: The attack speed of the item being built.
    //@param critDamage: The crit damage of the item being built.
    //@param critChance: The crit chance of the item being built.
    //@param health: The health of the item being built.
    //@param speed: The speed of the item being built.
    //@params defense: The defense of various types for the item being built.
    //@params damage: The damage of various types for item being built.
    //@params meleeProficiency: The melee proficiency of the item being built.
    //@params rangedProficiency: The ranged proficiency of the item being built.
    //@params armorProficiency: The armor proficiency of the item being built.
    public BattleItem(Material material, int amount, boolean unstackable, String name, String itemType, float damage, float attackSpeed,
                      float critDamage, float critChance, float strength, float health, float healthRegen, float stamina, float staminaRegen,
                      float defense, float speed, float infernalDefense, float infernalDamage, float undeadDefense, float undeadDamage,
                      float aquaticDefense, float aquaticDamage, float aerialDefense, float aerialDamage,
                      float meleeDefense, float meleeDamage, float rangedDefense, float rangedDamage, float magicDefense,
                      float magicDamage, int meleeProficiency, int rangedProficiency, int armorProficiency,
                      String textureURL, PotionEffect[] potionEffects){
        super(material, amount, unstackable, name, textureURL);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.critDamage = critDamage;
        this.critChance = critChance;
        this.strength = strength;
        this.health = health;
        this.healthRegen = healthRegen;
        this.stamina = stamina;
        this.staminaRegen = staminaRegen;
        this.defense = defense;
        this.speed = speed;
        this.infernalDefense = infernalDefense;
        this.infernalDamage = infernalDamage;
        this.undeadDefense = undeadDefense;
        this.undeadDamage = undeadDamage;
        this.aquaticDefense = aquaticDefense;
        this.aquaticDamage = aquaticDamage;
        this.aerialDefense = aerialDefense;
        this.aerialDamage = aerialDamage;
        this.meleeDefense = meleeDefense;
        this.meleeDamage = meleeDamage;
        this.rangedDefense = rangedDefense;
        this.rangedDamage = rangedDamage;
        this.magicDefense = magicDefense;
        this.magicDamage = magicDamage;
        this.meleeProficiency = meleeProficiency;
        this.rangedProficiency = rangedProficiency;
        this.armorProficiency = armorProficiency;

        //if the potionEffects array is null, set it to an empty array
        if (potionEffects == null){
            potionEffects = new PotionEffect[0];
        }
        this.potionEffects = potionEffects;

        //if the texture string is not null, set the texture of the item
        if (textureURL != null) {
            SkullMeta skullMeta = (SkullMeta) getItemMeta();
            PlayerProfile profile = Bukkit.getServer().createProfile(UUID.randomUUID(), name);
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL("http://textures.minecraft.net/texture/" + textureURL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            profile.setTextures(textures);
            skullMeta.setPlayerProfile(profile);
            getItemStack().setItemMeta(skullMeta);
        }

        //attackSpeed implementation
        implementAttackSpeed(itemType);

        //setting pdc values for the item
        PersistentDataContainer pdc = this.getItemMeta().getPersistentDataContainer();

        //if the item is unstackable, add a unique identifier to the item
        if (unstackable){
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());
        }

        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING, itemType);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING, name);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "damage"), PersistentDataType.FLOAT, damage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "attackSpeed"), PersistentDataType.FLOAT, attackSpeed);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "critDamage"), PersistentDataType.FLOAT, critDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "critChance"), PersistentDataType.FLOAT, critChance);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "strength"), PersistentDataType.FLOAT, strength);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "health"), PersistentDataType.FLOAT, health);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "healthRegen"), PersistentDataType.FLOAT, healthRegen);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "stamina"), PersistentDataType.FLOAT, stamina);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "staminaRegen"), PersistentDataType.FLOAT, staminaRegen);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "defense"), PersistentDataType.FLOAT, defense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "speed"), PersistentDataType.FLOAT, speed);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "infernalDefense"), PersistentDataType.FLOAT, infernalDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "infernalDamage"), PersistentDataType.FLOAT, infernalDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "undeadDefense"), PersistentDataType.FLOAT, undeadDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "undeadDamage"), PersistentDataType.FLOAT, undeadDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aquaticDefense"), PersistentDataType.FLOAT, aquaticDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aquaticDamage"), PersistentDataType.FLOAT, aquaticDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aerialDefense"), PersistentDataType.FLOAT, aerialDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aerialDamage"), PersistentDataType.FLOAT, aerialDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "meleeDefense"), PersistentDataType.FLOAT, meleeDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "meleeDamage"), PersistentDataType.FLOAT, meleeDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "rangedDefense"), PersistentDataType.FLOAT, rangedDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "rangedDamage"), PersistentDataType.FLOAT, rangedDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "magicDefense"), PersistentDataType.FLOAT, magicDefense);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "magicDamage"), PersistentDataType.FLOAT, magicDamage);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "meleeProficiency"), PersistentDataType.INTEGER, meleeProficiency);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "rangedProficiency"), PersistentDataType.INTEGER, rangedProficiency);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "armorProficiency"), PersistentDataType.INTEGER, armorProficiency);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN, unstackable);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY, potionEffects);

        addItemFlags();
    }


    //getter
    //@param Stat: The stat being retrieved.
    //@return the value of the stat.
    public float getStat(String Stat){
        switch(Stat){
            case "damage":
                return this.damage;
            case "attackSpeed":
                return this.attackSpeed;
            case "critDamage":
                return this.critDamage;
            case "critChance":
                return this.critChance;
            case "strength":
                return this.strength;
            case "health":
                return this.health;
            case "healthRegen":
                return this.healthRegen;
            case "stamina":
                return this.stamina;
            case "staminaRegen":
                return this.staminaRegen;
            case "defense":
                return this.defense;
            case "speed":
                return this.speed;
            case "infernalDefense":
                return this.infernalDefense;
            case "infernalDamage":
                return this.infernalDamage;
            case "undeadDefense":
                return this.undeadDefense;
            case "undeadDamage":
                return this.undeadDamage;
            case "aquaticDefense":
                return this.aquaticDefense;
            case "aquaticDamage":
                return this.aquaticDamage;
            case "aerialDefense":
                return this.aerialDefense;
            case "aerialDamage":
                return this.aerialDamage;
            case "meleeDefense":
                return this.meleeDefense;
            case "meleeDamage":
                return this.meleeDamage;
            case "rangedDefense":
                return this.rangedDefense;
            case "rangedDamage":
                return this.rangedDamage;
            case "magicDefense":
                return this.magicDefense;
            case "magicDamage":
                return this.magicDamage;
            case "meleeProficiency":
                return this.meleeProficiency;
            case "rangedProficiency":
                return this.rangedProficiency;
            case "armorProficiency":
                return this.armorProficiency;
        }
        return 0;
    }

    //set the attackSpeed attribute modifier depending on the vanilla attackSpeed of the item
    public void implementAttackSpeed(String itemType){

        float baseAttackSpeed = 4.0f;
        Material itemMaterial = this.getItemStack().getType();

        //get the base attack speed of the item based on the material
        if (itemMaterial == Material.WOODEN_SWORD || itemMaterial == Material.STONE_SWORD
                || itemMaterial == Material.GOLDEN_SWORD || itemMaterial == Material.IRON_SWORD
                || itemMaterial == Material.DIAMOND_SWORD || itemMaterial == Material.NETHERITE_SWORD){
            baseAttackSpeed = 1.6f;
        }else if (itemMaterial == Material.WOODEN_SHOVEL || itemMaterial == Material.STONE_SHOVEL
                || itemMaterial == Material.GOLDEN_SHOVEL || itemMaterial == Material.IRON_SHOVEL
                || itemMaterial == Material.DIAMOND_SHOVEL || itemMaterial == Material.NETHERITE_SHOVEL){
            baseAttackSpeed = 1.0f;
        }else if (itemMaterial == Material.WOODEN_PICKAXE || itemMaterial == Material.STONE_PICKAXE
                || itemMaterial == Material.GOLDEN_PICKAXE || itemMaterial == Material.IRON_PICKAXE
                || itemMaterial == Material.DIAMOND_PICKAXE || itemMaterial == Material.NETHERITE_PICKAXE){
            baseAttackSpeed = 1.2f;
        }else if (itemMaterial == Material.TRIDENT){
            baseAttackSpeed = 1.1f;
        }else if (itemMaterial == Material.WOODEN_AXE || itemMaterial == Material.STONE_AXE){
            baseAttackSpeed = 0.8f;
        }else if (itemMaterial == Material.IRON_AXE){
            baseAttackSpeed = 0.9f;
        }else if (itemMaterial == Material.GOLDEN_AXE || itemMaterial == Material.DIAMOND_AXE
                || itemMaterial == Material.NETHERITE_AXE || itemMaterial == Material.WOODEN_HOE
                || itemMaterial == Material.GOLDEN_HOE){
            baseAttackSpeed = 1.0f;
        }else if (itemMaterial == Material.STONE_HOE){
            baseAttackSpeed = 2.0f;
        }else if (itemMaterial == Material.IRON_HOE){
            baseAttackSpeed = 3.0f;
        }

        //add attackSpeed modifiers to the items based on what type they are
        switch (itemType) {
            case "accessory": {

                //set the attack speed: added to the base attack speed of the item
                AttributeModifier swingSpeedMain = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", attackSpeed * 0.01,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND);
                this.getItemMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, swingSpeedMain);
                break;
            }
            case "armor": {

                //set the attack speed: added to the base attack speed of the item
                AttributeModifier swingSpeedMain = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", attackSpeed * 0.01,
                        AttributeModifier.Operation.ADD_NUMBER);
                this.getItemMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, swingSpeedMain);
                break;
            }
            case "weapon": {

                //set the attack speed: the base attack speed of the item
                AttributeModifier swingSpeedMain = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", attackSpeed * 0.01 - baseAttackSpeed,
                        AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
                this.getItemMeta().addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, swingSpeedMain);
                break;
            }
        }
    }



    //sets the lore of the item
    //@param loreText: The text of the lore being set to the item.
    //@param rgbValues: The rgb values of the lore being set to the item.
    //@param isBold: Makes the lore bold.
    //@param isUnderlined: Makes the lore underlined.
    //@param isItalic: makes the lore italic.
    //@param isObfuscated: Makes the lore obfuscated.
    //@param isStrikethrough: Makes the lore strikethrough.
    //@return the Item
    public BattleItem setLore(List<String> loreText, List<Integer> rgbValues, List<Boolean> isBold, List<Boolean> isUnderlined,
                              List<Boolean> isItalic, List<Boolean> isObfuscated, List<Boolean> isStrikethrough) {

        List<TextComponent> wordList = new ArrayList<>();
        List<TextComponent> loreList = new ArrayList<>();

        //variable to keep track of how many times the nl command is called
        int nlCalls = 0;

        //adding the lore to the item
        for (int i = 0; i < loreText.size(); i++) {

            //go to next line by adding the current line to the loreList and clearing the wordList
            if (loreText.get(i).equals("nl")) {
                TextComponent lore = Component.empty();
                for (TextComponent word : wordList) {
                    lore = lore.append(word);
                }
                loreList.add(lore);
                wordList.clear();
                nlCalls++;

                //add the current word to the wordList
            } else {
                wordList.add(wordList.size(), (Component.text(loreText.get(i),
                                TextColor.color(rgbValues.get((i - nlCalls) * 3), rgbValues.get((i - nlCalls) * 3 + 1), rgbValues.get((i - nlCalls) * 3 + 2)))
                        .decoration(TextDecoration.BOLD, isBold.get(i - nlCalls))
                        .decoration(TextDecoration.UNDERLINED, isUnderlined.get(i - nlCalls))
                        .decoration(TextDecoration.ITALIC, isItalic.get(i - nlCalls))
                        .decoration(TextDecoration.OBFUSCATED, isObfuscated.get(i - nlCalls))
                        .decoration(TextDecoration.STRIKETHROUGH, isStrikethrough.get(i - nlCalls))));
            }
        }

        //add the last line to the loreList
        TextComponent lore = Component.empty();
        for (TextComponent word : wordList) {
            lore = lore.append(word);
        }
        loreList.add(lore);

        //add the potion effects as lore
        if (potionEffects.length > 0){
            loreList.add(0, Component.text(""));
        }

        for (PotionEffect effect : potionEffects){

            String name = effect.getType().getName();

            switch (effect.getType().getName()) {
                case "ABSORPTION":
                    name = "Absorption";
                    break;
                case "BAD_OMEN":
                    name = "Bad Omen";
                    break;
                case "BLINDNESS":
                    name = "Blindness";
                    break;
                case "CONDUIT_POWER":
                    name = "Conduit Power";
                    break;
                case "CONFUSION":
                    name = "Nausea";
                    break;
                case "DAMAGE_RESISTANCE":
                    name = "Resistance";
                    break;
                case "DARKNESS":
                    name = "Darkness";
                    break;
                case "DOLPHINS_GRACE":
                    name = "Dolphin's Grace";
                    break;
                case "FAST_DIGGING":
                    name = "Haste";
                    break;
                case "FIRE_RESISTANCE":
                    name = "Fire Resistance";
                    break;
                case "GLOWING":
                    name = "Glowing";
                    break;
                case "HARM":
                    name = "Instant Damage";
                    break;
                case "HEAL":
                    name = "Instant Health";
                    break;
                case "HEALTH_BOOST":
                    name = "Health Boost";
                    break;
                case "HERO_OF_THE_VILLAGE":
                    name = "Hero of the Village";
                    break;
                case "HUNGER":
                    name = "Hunger";
                    break;
                case "INCREASE_DAMAGE":
                    name = "Strength";
                    break;
                case "INVISIBILITY":
                    name = "Invisibility";
                    break;
                case "JUMP":
                    name = "Jump Boost";
                    break;
                case "LEVITATION":
                    name = "Levitation";
                    break;
                case "LUCK":
                    name = "Luck";
                    break;
                case "NIGHT_VISION":
                    name = "Night Vision";
                    break;
                case "POISON":
                    name = "Poison";
                    break;
                case "REGENERATION":
                    name = "Regeneration";
                    break;
                case "SATURATION":
                    name = "Saturation";
                    break;
                case "SLOW":
                    name = "Slowness";
                    break;
                case "SLOW_DIGGING":
                    name = "Mining Fatigue";
                    break;
                case "SLOW_FALLING":
                    name = "Slow Falling";
                    break;
                case "SPEED":
                    name = "Speed";
                    break;
                case "UNLUCK":
                    name = "Bad Luck";
                    break;
                case "WATER_BREATHING":
                    name = "Water Breathing";
                    break;
                case "WEAKNESS":
                    name = "Weakness";
                    break;
                case "WITHER":
                    name = "Wither";
                    break;
            }

            TextComponent newLoreLine = Component.text(effect.getDuration()/20 + "s of ", TextColor.color(200,200,200))
                    .decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(name + " " + (effect.getAmplifier() + 1), TextColor.color(240, 40, 50)))
                            .decoration(TextDecoration.ITALIC, false);
            loreList.add(0, newLoreLine);
        }

        //adding the custom item attributes to the item lore
        loreList.add(0, Component.text(""));
        makeLoreLine(loreList, "Infernal Defense: ", this.getStat("infernalDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Infernal Damage: ", this.getStat("infernalDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Undead Defense: ", this.getStat("undeadDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Undead Damage: ", this.getStat("undeadDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Aquatic Defense: ", this.getStat("aquaticDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Aquatic Damage: ", this.getStat("aquaticDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Aerial Defense: ", this.getStat("aerialDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Aerial Damage: ", this.getStat("aerialDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Melee Defense: ", this.getStat("meleeDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Melee Damage: ", this.getStat("meleeDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Ranged Defense: ", this.getStat("rangedDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Ranged Damage: ", this.getStat("rangedDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Magic Defense: ", this.getStat("magicDefense"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Magic Damage: ", this.getStat("magicDamage"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Speed: ", this.getStat("speed"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Attack Speed: ", this.getStat("attackSpeed"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Crit Damage: ", this.getStat("critDamage"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Crit Chance: ", this.getStat("critChance"), true, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Defense: ", this.getStat("defense"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Stamina Regen: ", this.getStat("staminaRegen"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Stamina: ", this.getStat("stamina"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Health Regen: ", this.getStat("healthRegen"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Health: ", this.getStat("health"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Strength: ", this.getStat("strength"), false, false, 240, 40, 50, 200, 200, 200);
        makeLoreLine(loreList, "Damage: ", this.getStat("damage"), false, false, 240, 40, 50, 200, 200, 200);

        if (this.getStat("meleeProficiency") != 0 || this.getStat("rangedProficiency") != 0 || this.getStat("armorProficiency") != 0){
            loreList.add(0, Component.text(""));
        }

        makeLoreLine(loreList, "Armor Proficiency: ", this.getStat("armorProficiency"), false, true, 77,85,92, 200, 200, 200);
        makeLoreLine(loreList, "Ranged Proficiency: ", this.getStat("rangedProficiency"), false, true, 240, 185, 85, 200, 200, 200);
        makeLoreLine(loreList, "Melee Proficiency: ", this.getStat("meleeProficiency"), false, true, 214,88,88, 200, 200, 200);

        //setting the lore of the item
        super.getItemMeta().lore(loreList);
        return this;
    }

    //makes a lore line
    //@param loreList: The list of lore lines
    //@param statName: The name of the stat
    //@param spacedStatName: The name of the stat with spaces
    //@param isPercent: If the stat needs a percent sign
    //@param red,green,blue(number): the rgb color of the number itself
    //@param red,green,blue(stat): the rgb color of the stat itself
    private void makeLoreLine(List<TextComponent> loreList, String spacedStatName, float statValue, boolean isPercent, boolean isProficiency,
                             int redNumber, int greenNumber, int blueNumber, int redStat, int greenStat, int blueStat) {

        //if the stat value is zero do nothing
        if (statValue != 0) {

            //create the to be returned text component
            TextComponent loreLine;

            //set the sign
            String sign = "";
            if (statValue > 0) {
                sign = "+";
            }

            //if a stat is a proficiency, remove the + or - sign
            if (!isProficiency) {

                //if the stat needs a percent sign
                if (isPercent) {

                    //if the stat is a whole number, don't add a decimal point
                    if (statValue % 1 == 0) {
                        loreLine = Component.text(spacedStatName, TextColor.color(redStat, greenStat, blueStat))
                                .decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(sign + (int) statValue + "%", TextColor.color(redNumber, greenNumber, blueNumber)))
                                .decoration(TextDecoration.ITALIC, false);
                    } else {
                        loreLine = Component.text(spacedStatName, TextColor.color(redStat, greenStat, blueStat))
                                .decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(sign + statValue + "%", TextColor.color(redNumber, greenNumber, blueNumber)))
                                .decoration(TextDecoration.ITALIC, false);
                    }
                } else {

                    //if the stat is a whole number, don't add a decimal point
                    if (statValue % 1 == 0) {
                        loreLine = Component.text(spacedStatName, TextColor.color(redStat, greenStat, blueStat))
                                .decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(sign + (int) statValue, TextColor.color(redNumber, greenNumber, blueNumber)))
                                .decoration(TextDecoration.ITALIC, false);
                    } else {
                        loreLine = Component.text(spacedStatName, TextColor.color(redStat, greenStat, blueStat))
                                .decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(sign + statValue, TextColor.color(redNumber, greenNumber, blueNumber)))
                                .decoration(TextDecoration.ITALIC, false);
                    }
                }
            }else{
                loreLine = Component.text(spacedStatName, TextColor.color(redStat, greenStat, blueStat))
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text((int) statValue, TextColor.color(redNumber, greenNumber, blueNumber)))
                        .decoration(TextDecoration.ITALIC, false);
            }

            loreList.add(0, loreLine);
        }
    }

}
