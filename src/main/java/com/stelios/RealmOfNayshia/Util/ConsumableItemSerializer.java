package com.stelios.RealmOfNayshia.Util;

import com.google.gson.*;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.RealmOfNayshia.Items.ConsumableItem;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
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

public class ConsumableItemSerializer implements JsonSerializer<ConsumableItem>, JsonDeserializer<ConsumableItem> {

    @Override
    public JsonElement serialize(ConsumableItem consumableItem, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        // Serialize basic item data using the ItemSerializer logic
        JsonElement itemElement = context.serialize((Item) consumableItem, Item.class);
        jsonObject = itemElement.getAsJsonObject();

        jsonObject.addProperty("foodValue", consumableItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER));
        jsonObject.addProperty("saturationValue", consumableItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(Main.class), "saturationValue"), PersistentDataType.FLOAT));

        JsonArray statsArray = new JsonArray();
        for (String stat : consumableItem.getStats()) {
            statsArray.add(stat);
        }
        jsonObject.add("stats", statsArray);

        JsonArray statsAmountArray = new JsonArray();
        for (int amount : consumableItem.getStatsAmount()) {
            statsAmountArray.add(amount);
        }
        jsonObject.add("statsAmount", statsAmountArray);

        JsonArray statsDurationArray = new JsonArray();
        for (int duration : consumableItem.getStatsDuration()) {
            statsDurationArray.add(duration);
        }
        jsonObject.add("statsDuration", statsDurationArray);

        // Serialize PotionEffects
        JsonArray potionEffectsArray = new JsonArray();
        for (PotionEffect potionEffect : consumableItem.getPotionEffects()) {
            potionEffectsArray.add(context.serialize(potionEffect));
        }
        jsonObject.add("potionEffects", potionEffectsArray);

        // Serialize ItemMeta
        JsonObject metaObject = new JsonObject();
        ItemMeta meta = consumableItem.getItemMeta();
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
            } else if (pdc.has(key, DataType.STRING_ARRAY)) {
                JsonArray array = new JsonArray();
                for (String value : pdc.get(key, DataType.STRING_ARRAY)) {
                    array.add(value);
                }
                metaObject.add(key.toString(), array);
            } else if (pdc.has(key, DataType.INTEGER_ARRAY)) {
                JsonArray array = new JsonArray();
                for (int value : pdc.get(key, DataType.INTEGER_ARRAY)) {
                    array.add(value);
                }
                metaObject.add(key.toString(), array);
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
    public ConsumableItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Material material = Material.valueOf(jsonObject.get("material").getAsString());
        int amount = jsonObject.get("amount").getAsInt();
        boolean unstackable = jsonObject.get("unstackable").getAsBoolean();
        String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;

        int foodValue = jsonObject.get("foodValue").getAsInt();
        float saturationValue = jsonObject.get("saturationValue").getAsFloat();

        JsonArray statsArray = jsonObject.get("stats").getAsJsonArray();
        String[] stats = new String[statsArray.size()];
        for (int i = 0; i < statsArray.size(); i++) {
            stats[i] = statsArray.get(i).getAsString();
        }

        JsonArray statsAmountArray = jsonObject.get("statsAmount").getAsJsonArray();
        int[] statsAmount = new int[statsAmountArray.size()];
        for (int i = 0; i < statsAmountArray.size(); i++) {
            statsAmount[i] = statsAmountArray.get(i).getAsInt();
        }

        JsonArray statsDurationArray = jsonObject.get("statsDuration").getAsJsonArray();
        int[] statsDuration = new int[statsDurationArray.size()];
        for (int i = 0; i < statsDurationArray.size(); i++) {
            statsDuration[i] = statsDurationArray.get(i).getAsInt();
        }

        // Deserialize PotionEffects
        JsonArray potionEffectsArray = jsonObject.get("potionEffects").getAsJsonArray();
        PotionEffect[] potionEffects = new PotionEffect[potionEffectsArray.size()];
        for (int i = 0; i < potionEffectsArray.size(); i++) {
            potionEffects[i] = context.deserialize(potionEffectsArray.get(i), PotionEffect.class);
        }

        ConsumableItem consumableItem = new ConsumableItem(material, amount, unstackable, name, foodValue, saturationValue, stats, statsAmount, statsDuration, potionEffects);

        // Deserialize ItemMeta
        JsonObject metaObject = jsonObject.get("itemMeta").getAsJsonObject();
        ItemMeta meta = consumableItem.getItemStack().getItemMeta();
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
            } else if (entry.getValue().isJsonArray()) {
                JsonArray array = entry.getValue().getAsJsonArray();
                if (array.size() > 0 && array.get(0).isJsonPrimitive() && array.get(0).getAsJsonPrimitive().isString()) {
                    String[] stringArray = new String[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        stringArray[i] = array.get(i).getAsString();
                    }
                    pdc.set(key, DataType.STRING_ARRAY, stringArray);
                } else if (array.size() > 0 && array.get(0).isJsonPrimitive() && array.get(0).getAsJsonPrimitive().isNumber()) {
                    int[] intArray = new int[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        intArray[i] = array.get(i).getAsInt();
                    }
                    pdc.set(key, DataType.INTEGER_ARRAY, intArray);
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

        consumableItem.getItemStack().setItemMeta(meta);

        return consumableItem;
    }
}
