package com.stelios.RealmOfNayshia.Util;

import com.google.gson.*;
import com.stelios.RealmOfNayshia.Items.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemSerializer implements JsonSerializer<Item>, JsonDeserializer<Item> {

    @Override
    public JsonElement serialize(Item item, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("material", item.getItemStack().getType().name());
        jsonObject.addProperty("amount", item.getItemStack().getAmount());
        jsonObject.addProperty("unstackable", item.isUnstackable());
        jsonObject.addProperty("name", item.getName());
        jsonObject.addProperty("textureURL", item.getTextureURL());

        // Serialize ItemMeta
        JsonObject metaObject = new JsonObject();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        // Manually handle known data types
        for (NamespacedKey key : pdc.getKeys()) {
            if (pdc.has(key, PersistentDataType.STRING)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.STRING));
            } else if (pdc.has(key, PersistentDataType.INTEGER)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.INTEGER));
            } else if (pdc.has(key, PersistentDataType.BOOLEAN)) {
                metaObject.addProperty(key.toString(), pdc.get(key, PersistentDataType.BOOLEAN));
            }
        }

        // Serialize display name if present
        if (meta.hasDisplayName()) {
            Component displayName = meta.displayName();
            String displayNameJson = GsonComponentSerializer.gson().serialize(displayName);
            metaObject.addProperty("displayName", displayNameJson);
        }

        // Serialize lore if present
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
    public Item deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Material material = Material.valueOf(jsonObject.get("material").getAsString());
        int amount = jsonObject.get("amount").getAsInt();
        boolean unstackable = jsonObject.get("unstackable").getAsBoolean();
        String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : null;
        String textureURL = jsonObject.has("textureURL") ? jsonObject.get("textureURL").getAsString() : null;

        Item item = new Item(material, amount, unstackable, name, textureURL);

        // Deserialize ItemMeta
        JsonObject metaObject = jsonObject.get("itemMeta").getAsJsonObject();
        ItemMeta meta = item.getItemStack().getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        for (Map.Entry<String, JsonElement> entry : metaObject.entrySet()) {
            if (entry.getKey().equals("displayName")) {
                String displayNameJson = entry.getValue().getAsString();
                Component displayName = GsonComponentSerializer.gson().deserialize(displayNameJson);
                meta.displayName(displayName);
            } else if (entry.getKey().equals("lore")) {
                JsonArray loreArray = entry.getValue().getAsJsonArray();
                List<Component> lore = new ArrayList<>();
                for (JsonElement loreElement : loreArray) {
                    Component loreLine = GsonComponentSerializer.gson().deserialize(loreElement.getAsString());
                    lore.add(loreLine);
                }
                meta.lore(lore);
            } else {
                NamespacedKey key = NamespacedKey.fromString(entry.getKey());
                if (entry.getValue().isJsonPrimitive()) {
                    if (entry.getValue().getAsJsonPrimitive().isString()) {
                        pdc.set(key, PersistentDataType.STRING, entry.getValue().getAsString());
                    } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                        pdc.set(key, PersistentDataType.INTEGER, entry.getValue().getAsInt());
                    } else if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                        pdc.set(key, PersistentDataType.BOOLEAN, entry.getValue().getAsBoolean());
                    }
                }
            }
        }

        item.getItemStack().setItemMeta(meta);

        return item;
    }
}
