package com.stelios.cakenaysh.Items;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.cakenaysh.Main;
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
                           int foodValue, String[] stats, int[] statsAmount,
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
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER, foodValue);
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
}
