package com.stelios.RealmOfNayshia.Util;

import com.google.gson.*;
import com.stelios.RealmOfNayshia.Items.BattleItem;
import com.stelios.RealmOfNayshia.Items.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattleItemSerializer implements JsonSerializer<BattleItem>, JsonDeserializer<BattleItem> {

    @Override
    public JsonElement serialize(BattleItem battleItem, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        // Serialize basic item data using the ItemSerializer logic
        JsonElement itemElement = context.serialize((Item) battleItem, Item.class);
        jsonObject = itemElement.getAsJsonObject();

        jsonObject.addProperty("damage", battleItem.getDamage());
        jsonObject.addProperty("attackSpeed", battleItem.getAttackSpeed());
        jsonObject.addProperty("critDamage", battleItem.getCritDamage());
        jsonObject.addProperty("critChance", battleItem.getCritChance());
        jsonObject.addProperty("strength", battleItem.getStrength());
        jsonObject.addProperty("health", battleItem.getHealth());
        jsonObject.addProperty("healthRegen", battleItem.getHealthRegen());
        jsonObject.addProperty("stamina", battleItem.getStamina());
        jsonObject.addProperty("staminaRegen", battleItem.getStaminaRegen());
        jsonObject.addProperty("defense", battleItem.getDefense());
        jsonObject.addProperty("speed", battleItem.getSpeed());
        jsonObject.addProperty("infernalDefense", battleItem.getInfernalDefense());
        jsonObject.addProperty("infernalDamage", battleItem.getInfernalDamage());
        jsonObject.addProperty("undeadDefense", battleItem.getUndeadDefense());
        jsonObject.addProperty("undeadDamage", battleItem.getUndeadDamage());
        jsonObject.addProperty("aquaticDefense", battleItem.getAquaticDefense());
        jsonObject.addProperty("aquaticDamage", battleItem.getAquaticDamage());
        jsonObject.addProperty("aerialDefense", battleItem.getAerialDefense());
        jsonObject.addProperty("aerialDamage", battleItem.getAerialDamage());
        jsonObject.addProperty("meleeDefense", battleItem.getMeleeDefense());
        jsonObject.addProperty("meleeDamage", battleItem.getMeleeDamage());
        jsonObject.addProperty("rangedDefense", battleItem.getRangedDefense());
        jsonObject.addProperty("rangedDamage", battleItem.getRangedDamage());
        jsonObject.addProperty("magicDefense", battleItem.getMagicDefense());
        jsonObject.addProperty("magicDamage", battleItem.getMagicDamage());
        jsonObject.addProperty("meleeProficiency", battleItem.getMeleeProficiency());
        jsonObject.addProperty("rangedProficiency", battleItem.getRangedProficiency());
        jsonObject.addProperty("armorProficiency", battleItem.getArmorProficiency());

        // Serialize PotionEffects
        JsonArray potionEffectsArray = new JsonArray();
        for (PotionEffect potionEffect : battleItem.getPotionEffects()) {
            potionEffectsArray.add(context.serialize(potionEffect));
        }
        jsonObject.add("potionEffects", potionEffectsArray);

        // Serialize ItemMeta
        JsonObject metaObject = new JsonObject();
        ItemMeta meta = battleItem.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        for (NamespacedKey key : pdc.getKeys()) {
            if (pdc.has(key, PersistentDataType.STRING)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.STRING));
            } else if (pdc.has(key, PersistentDataType.INTEGER)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.INTEGER));
            } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.BOOLEAN));
            } else if (pdc.has(key, PersistentDataType.FLOAT)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.FLOAT));
            }
        }

        if (meta.hasDisplayName()) {
            Component displayName = meta.displayName();
            String displayNameJson = GsonComponentSerializer.gson().serialize(displayName);
            metaObject.addProperty("displayName", displayNameJson);
        }

        if (meta.hasLore()) {
            JsonArray loreArray = new JsonArray();
            for (Component loreLine : meta.lore()) {
                String loreLineJson = GsonComponentSerializer.gson().serialize(loreLine);
                loreArray.add(loreLineJson);
            }
            metaObject.add("lore", loreArray);
        }

        jsonObject.add("itemMeta", metaObject);

        return jsonObject;
    }

    @Override
    public BattleItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Material material = Material.valueOf(jsonObject.get("material").getAsString());
        int amount = jsonObject.get("amount").getAsInt();
        boolean unstackable = jsonObject.get("unstackable").getAsBoolean();
        String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
        String textureURL = jsonObject.has("textureURL") ? jsonObject.get("textureURL").getAsString() : null;

        float damage = jsonObject.get("damage").getAsFloat();
        float attackSpeed = jsonObject.get("attackSpeed").getAsFloat();
        float critDamage = jsonObject.get("critDamage").getAsFloat();
        float critChance = jsonObject.get("critChance").getAsFloat();
        float strength = jsonObject.get("strength").getAsFloat();
        float health = jsonObject.get("health").getAsFloat();
        float healthRegen = jsonObject.get("healthRegen").getAsFloat();
        float stamina = jsonObject.get("stamina").getAsFloat();
        float staminaRegen = jsonObject.get("staminaRegen").getAsFloat();
        float defense = jsonObject.get("defense").getAsFloat();
        float speed = jsonObject.get("speed").getAsFloat();
        float infernalDefense = jsonObject.get("infernalDefense").getAsFloat();
        float infernalDamage = jsonObject.get("infernalDamage").getAsFloat();
        float undeadDefense = jsonObject.get("undeadDefense").getAsFloat();
        float undeadDamage = jsonObject.get("undeadDamage").getAsFloat();
        float aquaticDefense = jsonObject.get("aquaticDefense").getAsFloat();
        float aquaticDamage = jsonObject.get("aquaticDamage").getAsFloat();
        float aerialDefense = jsonObject.get("aerialDefense").getAsFloat();
        float aerialDamage = jsonObject.get("aerialDamage").getAsFloat();
        float meleeDefense = jsonObject.get("meleeDefense").getAsFloat();
        float meleeDamage = jsonObject.get("meleeDamage").getAsFloat();
        float rangedDefense = jsonObject.get("rangedDefense").getAsFloat();
        float rangedDamage = jsonObject.get("rangedDamage").getAsFloat();
        float magicDefense = jsonObject.get("magicDefense").getAsFloat();
        float magicDamage = jsonObject.get("magicDamage").getAsFloat();
        int meleeProficiency = jsonObject.get("meleeProficiency").getAsInt();
        int rangedProficiency = jsonObject.get("rangedProficiency").getAsInt();
        int armorProficiency = jsonObject.get("armorProficiency").getAsInt();

        // Deserialize PotionEffects
        JsonArray potionEffectsArray = jsonObject.get("potionEffects").getAsJsonArray();
        PotionEffect[] potionEffects = new PotionEffect[potionEffectsArray.size()];
        for (int i = 0; i < potionEffectsArray.size(); i++) {
            potionEffects[i] = context.deserialize(potionEffectsArray.get(i), PotionEffect.class);
        }

        BattleItem battleItem = new BattleItem(material, amount, unstackable, name, jsonObject.get("itemType").getAsString(), damage, attackSpeed,
                critDamage, critChance, strength, health, healthRegen, stamina, staminaRegen, defense, speed, infernalDefense, infernalDamage,
                undeadDefense, undeadDamage, aquaticDefense, aquaticDamage, aerialDefense, aerialDamage, meleeDefense, meleeDamage, rangedDefense,
                rangedDamage, magicDefense, magicDamage, meleeProficiency, rangedProficiency, armorProficiency, textureURL, potionEffects);

        // Deserialize ItemMeta
        JsonObject metaObject = jsonObject.get("itemMeta").getAsJsonObject();
        ItemMeta meta = battleItem.getItemStack().getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        for (Map.Entry<String, JsonElement> entry : metaObject.entrySet()) {
            NamespacedKey key = NamespacedKey.fromString(entry.getKey());
            if (entry.getValue().isJsonPrimitive()) {
                if (entry.getValue().getAsJsonPrimitive().isString()) {
                    pdc.set(key, PersistentDataType.STRING, entry.getValue().getAsString());
                } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                    if (entry.getValue().getAsJsonPrimitive().getAsString().contains(".")) {
                        pdc.set(key, PersistentDataType.FLOAT, entry.getValue().getAsFloat());
                    } else {
                        pdc.set(key, PersistentDataType.INTEGER, entry.getValue().getAsInt());
                    }
                } else if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                    pdc.set(key, PersistentDataType.BOOLEAN, entry.getValue().getAsBoolean());
                }
            }
        }

        if (metaObject.has("displayName")) {
            String displayNameJson = metaObject.get("displayName").getAsString();
            Component displayName = GsonComponentSerializer.gson().deserialize(displayNameJson);
            meta.displayName(displayName);
        }

        if (metaObject.has("lore")) {
            JsonArray loreArray = metaObject.get("lore").getAsJsonArray();
            List<Component> lore = new ArrayList<>();
            for (JsonElement loreElement : loreArray) {
                Component loreLine = GsonComponentSerializer.gson().deserialize(loreElement.getAsString());
                lore.add(loreLine);
            }
            meta.lore(lore);
        }

        battleItem.getItemStack().setItemMeta(meta);

        return battleItem;
    }
}
