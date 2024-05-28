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
import org.bukkit.potion.PotionEffect;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConsumableItemSerializer implements JsonSerializer<ConsumableItem>, JsonDeserializer<ConsumableItem> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
            .create();

    private final ItemSerializer itemSerializer = new ItemSerializer();

    @Override
    public JsonElement serialize(ConsumableItem consumableItem, Type type, JsonSerializationContext context) {
        // Serialize basic item data using the ItemSerializer logic
        JsonObject jsonObject = itemSerializer.serialize((Item) consumableItem, type, context).getAsJsonObject();

        // Get PDC to serialize specific fields
        PersistentDataContainer pdc = consumableItem.getItemMeta().getPersistentDataContainer();

        // Serialize common PDC fields
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING)) {
            jsonObject.addProperty("itemType", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING));
        }
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING)) {
            jsonObject.addProperty("itemID", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING));
        }
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN)) {
            jsonObject.addProperty("unstackable", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN));
        }

        // Serialize specific fields
        Integer foodValue = pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER);
        if (foodValue != null) {
            jsonObject.addProperty("foodValue", foodValue);
        }

        Float saturationValue = pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "saturationValue"), PersistentDataType.FLOAT);
        if (saturationValue != null) {
            jsonObject.addProperty("saturationValue", saturationValue);
        }

        String[] stats = pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "stats"), DataType.STRING_ARRAY);
        JsonArray statsArray = new JsonArray();
        if (stats != null) {
            for (String stat : stats) {
                statsArray.add(stat);
            }
        }
        jsonObject.add("stats", statsArray);

        int[] statsAmount = pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "statsAmount"), DataType.INTEGER_ARRAY);
        JsonArray statsAmountArray = new JsonArray();
        if (statsAmount != null) {
            for (int amount : statsAmount) {
                statsAmountArray.add(amount);
            }
        }
        jsonObject.add("statsAmount", statsAmountArray);

        int[] statsDuration = pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "statsDuration"), DataType.INTEGER_ARRAY);
        JsonArray statsDurationArray = new JsonArray();
        if (statsDuration != null) {
            for (int duration : statsDuration) {
                statsDurationArray.add(duration);
            }
        }
        jsonObject.add("statsDuration", statsDurationArray);

        PotionEffect[] potionEffects = pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY);
        JsonArray potionEffectsArray = new JsonArray();
        if (potionEffects != null) {
            for (PotionEffect potionEffect : potionEffects) {
                potionEffectsArray.add(gson.toJsonTree(potionEffect));
            }
        }
        jsonObject.add("potionEffects", potionEffectsArray);

        return jsonObject;
    }

    @Override
    public ConsumableItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Deserialize basic item data using the ItemSerializer logic
        Item item = itemSerializer.deserialize(jsonElement, type, context);
        Material material = item.getItemStack().getType();
        int amount = item.getItemStack().getAmount();
        boolean unstackable = item.isUnstackable();
        String name = item.getName();

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
            potionEffects[i] = gson.fromJson(potionEffectsArray.get(i), PotionEffect.class);
        }

        ConsumableItem consumableItem = new ConsumableItem(material, amount, unstackable, name, foodValue, saturationValue, stats, statsAmount, statsDuration, potionEffects);

        // Deserialize ItemMeta
        JsonObject metaObject = jsonObject.get("itemMeta").getAsJsonObject();
        ItemMeta meta = consumableItem.getItemStack().getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

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

        // Set the PDC values
        if (jsonObject.has("itemType")) {
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING, jsonObject.get("itemType").getAsString());
        }
        if (jsonObject.has("itemID")) {
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING, jsonObject.get("itemID").getAsString());
        }
        if (jsonObject.has("unstackable")) {
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN, jsonObject.get("unstackable").getAsBoolean());
        }
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER, foodValue);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "saturationValue"), PersistentDataType.FLOAT, saturationValue);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "stats"), DataType.STRING_ARRAY, stats);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "statsAmount"), DataType.INTEGER_ARRAY, statsAmount);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "statsDuration"), DataType.INTEGER_ARRAY, statsDuration);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY, potionEffects);

        consumableItem.getItemStack().setItemMeta(meta);

        return consumableItem;
    }
}
