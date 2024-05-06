package com.stelios.RealmOfNayshia.Items;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.RealmOfNayshia.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConsumableItem extends Item {

    private final String[] stats;
    private final int[] statsAmount;
    private final int[] statsDuration;
    private final PotionEffect[] potionEffects;

    public ConsumableItem (Material material, int amount, boolean unstackable, String name,
                           int foodValue, float saturationValue, String[] stats, int[] statsAmount,
                           int[] statsDuration, PotionEffect[] potionEffects) {
        super(material, amount, unstackable, name, null);
        this.stats = stats;
        this.statsAmount = statsAmount;
        this.statsDuration = statsDuration;
        this.potionEffects = potionEffects;

        //setting pdc values for the item
        PersistentDataContainer pdc = this.getItemMeta().getPersistentDataContainer();

        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING, "consumable");
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING, name);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER, foodValue - calculateFoodLevel(material));
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "saturationValue"), PersistentDataType.FLOAT, saturationValue - calculateSaturationLevel(material));
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "stats"), DataType.STRING_ARRAY, stats);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "statsAmount"), DataType.INTEGER_ARRAY, statsAmount);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "statsDuration"), DataType.INTEGER_ARRAY, statsDuration);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY, potionEffects);

        //if the item is unstackable, add a unique identifier to the item
        if (unstackable){
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());
        }

        addItemFlags();

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
    public Item setLore(List<String> loreText, List<Integer> rgbValues, List<Boolean> isBold,
                        List<Boolean> isUnderlined, List<Boolean> isItalic, List<Boolean> isObfuscated,
                        List<Boolean> isStrikethrough){

        List<TextComponent> wordList = new ArrayList<>();
        List<TextComponent> loreList = new ArrayList<> ();

        //add the stats as lore
        for (int i = 0; i < stats.length; i++){

            if (stats[i].equalsIgnoreCase("Health")) {
                TextComponent newLoreLine = Component.text("+" + statsAmount[i] + " " + stats[i], TextColor.color(240, 40, 50))
                        .decoration(TextDecoration.ITALIC, false);
                loreList.add(newLoreLine);

            } else {
                TextComponent newLoreLine = Component.text("+" + statsAmount[i] + " " + stats[i], TextColor.color(240, 40, 50))
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(" for " + statsDuration[i] + "s", TextColor.color(200, 200, 200))
                                .decoration(TextDecoration.ITALIC, false));
                loreList.add(newLoreLine);
            }
        }

        //add the potion effects as lore
        for (PotionEffect effect : potionEffects){

            String name = switch (effect.getType().getName()) {
                case "ABSORPTION" -> "Absorption";
                case "BAD_OMEN" -> "Bad Omen";
                case "BLINDNESS" -> "Blindness";
                case "CONDUIT_POWER" -> "Conduit Power";
                case "CONFUSION" -> "Nausea";
                case "DAMAGE_RESISTANCE" -> "Resistance";
                case "DARKNESS" -> "Darkness";
                case "DOLPHINS_GRACE" -> "Dolphin's Grace";
                case "FAST_DIGGING" -> "Haste";
                case "FIRE_RESISTANCE" -> "Fire Resistance";
                case "GLOWING" -> "Glowing";
                case "HARM" -> "Instant Damage";
                case "HEAL" -> "Instant Health";
                case "HEALTH_BOOST" -> "Health Boost";
                case "HERO_OF_THE_VILLAGE" -> "Hero of the Village";
                case "HUNGER" -> "Hunger";
                case "INCREASE_DAMAGE" -> "Strength";
                case "INVISIBILITY" -> "Invisibility";
                case "JUMP" -> "Jump Boost";
                case "LEVITATION" -> "Levitation";
                case "LUCK" -> "Luck";
                case "NIGHT_VISION" -> "Night Vision";
                case "POISON" -> "Poison";
                case "REGENERATION" -> "Regeneration";
                case "SATURATION" -> "Saturation";
                case "SLOW" -> "Slowness";
                case "SLOW_DIGGING" -> "Mining Fatigue";
                case "SLOW_FALLING" -> "Slow Falling";
                case "SPEED" -> "Speed";
                case "UNLUCK" -> "Bad Luck";
                case "WATER_BREATHING" -> "Water Breathing";
                case "WEAKNESS" -> "Weakness";
                case "WITHER" -> "Wither";
                default -> effect.getType().getName();
            };

            TextComponent newLoreLine = Component.text(name + " " + (effect.getAmplifier() + 1), TextColor.color(240, 40, 50))
                    .decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(" for " + effect.getDuration()/20 + "s", TextColor.color(200,200,200))
                            .decoration(TextDecoration.ITALIC, false));
            loreList.add(newLoreLine);
        }

        //if stats or potion effects have been added, add a line break
        if (stats.length > 0 || potionEffects.length > 0){
            loreList.add(Component.empty());
        }

        //variable to keep track of how many times the nl command is called
        int nlCalls = 0;

        //adding the lore to the item
        for (int i = 0; i < loreText.size(); i++){

            //go to next line by adding the current line to the loreList and clearing the wordList
            if (loreText.get(i).equals("nl")){
                TextComponent lore = Component.empty();
                for (TextComponent word : wordList){
                    lore = lore.append(word);
                }
                loreList.add(lore);
                wordList.clear();
                nlCalls++;

                //add the current word to the wordList
            }else{
                wordList.add(wordList.size(),(Component.text(loreText.get(i),
                                TextColor.color(rgbValues.get((i-nlCalls)*3), rgbValues.get((i-nlCalls)*3+1), rgbValues.get((i-nlCalls)*3+2)))
                        .decoration(TextDecoration.BOLD, isBold.get(i-nlCalls))
                        .decoration(TextDecoration.UNDERLINED, isUnderlined.get(i-nlCalls))
                        .decoration(TextDecoration.ITALIC, isItalic.get(i-nlCalls))
                        .decoration(TextDecoration.OBFUSCATED, isObfuscated.get(i-nlCalls))
                        .decoration(TextDecoration.STRIKETHROUGH, isStrikethrough.get(i-nlCalls))));
            }
        }

        //add the last line to the loreList
        TextComponent lore = Component.empty();
        for (TextComponent word : wordList){
            lore = lore.append(word);
        }
        loreList.add(lore);


        //add the last line to the loreList
        this.getItemMeta().lore(loreList);
        return this;
    }


    //return the default food level of the material
    private int calculateFoodLevel(Material material){
        switch (material) {
            case BEETROOT, POTATO, DRIED_KELP, TROPICAL_FISH, PUFFERFISH -> {
                return 1;
            }
            case SPIDER_EYE, MELON_SLICE, POISONOUS_POTATO, MUTTON, CHICKEN, COOKIE, GLOW_BERRIES, COD, SALMON, SWEET_BERRIES -> {
                return 2;
            }
            case CARROT, BEEF, PORKCHOP, RABBIT -> {
                return 3;
            }
            case ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, APPLE, CHORUS_FRUIT, ROTTEN_FLESH -> {
                return 4;
            }
            case BAKED_POTATO, BREAD, COOKED_COD, COOKED_RABBIT -> {
                return 5;
            }
            case GOLDEN_CARROT, COOKED_MUTTON, COOKED_SALMON, BEETROOT_SOUP, COOKED_CHICKEN, MUSHROOM_STEW, SUSPICIOUS_STEW, HONEY_BOTTLE -> {
                return 6;
            }
            case COOKED_PORKCHOP, COOKED_BEEF, PUMPKIN_PIE -> {
                return 8;
            }
            case RABBIT_STEW -> {
                return 10;
            }
        }
        return 0;
    }


    //return the default saturation level of the material
    private float calculateSaturationLevel(Material material){
        switch (material){
            case TROPICAL_FISH, PUFFERFISH -> {
                return 0.2f;
            }
            case COOKIE, GLOW_BERRIES, COD, SALMON, SWEET_BERRIES -> {
                return 0.4f;
            }
            case POTATO, DRIED_KELP -> {
                return 0.6f;
            }
            case ROTTEN_FLESH -> {
                return 0.8f;
            }
            case BEETROOT, MELON_SLICE, POISONOUS_POTATO, MUTTON, CHICKEN, HONEY_BOTTLE -> {
                return 1.2f;
            }
            case BEEF, PORKCHOP, RABBIT -> {
                return 1.8f;
            }
            case APPLE, CHORUS_FRUIT -> {
                return 2.4f;
            }
            case SPIDER_EYE -> {
                return 3.2f;
            }
            case CARROT -> {
                return 3.6f;
            }
            case PUMPKIN_PIE -> {
                return 4.8f;
            }
            case BAKED_POTATO, BREAD, COOKED_COD, COOKED_RABBIT -> {
                return 6f;
            }
            case BEETROOT_SOUP, COOKED_CHICKEN, MUSHROOM_STEW, SUSPICIOUS_STEW -> {
                return 7.2f;
            }
            case ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, COOKED_MUTTON, COOKED_SALMON -> {
                return 9.6f;
            }
            case RABBIT_STEW -> {
                return 12f;
            }
            case COOKED_BEEF, COOKED_PORKCHOP -> {
                return 12.8f;
            }
            case GOLDEN_CARROT -> {
                return 14.4f;
            }
        }
        return 0f;
    }

}
