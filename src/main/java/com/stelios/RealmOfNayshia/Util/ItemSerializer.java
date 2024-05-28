package com.stelios.RealmOfNayshia.Util;

import com.google.gson.*;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
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
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING)) {
            metaObject.addProperty("itemType", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING));
        }
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING)) {
            metaObject.addProperty("name", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING));
        }
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN)) {
            metaObject.addProperty("unstackable", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN));
        }
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING)) {
            metaObject.addProperty("uniqueID", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING));
        }

        // Serialize custom model data if present
        if (meta.hasCustomModelData()) {
            metaObject.addProperty("customModelData", meta.getCustomModelData());
        }

        // Serialize ItemFlags
        JsonArray flagsArray = new JsonArray();
        for (ItemFlag flag : meta.getItemFlags()) {
            flagsArray.add(flag.name());
        }
        metaObject.add("itemFlags", flagsArray);

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
            switch (entry.getKey()) {
                case "displayName":
                    String displayNameJson = entry.getValue().getAsString();
                    Component displayName = GsonComponentSerializer.gson().deserialize(displayNameJson);
                    meta.displayName(displayName);
                    break;
                case "lore":
                    JsonArray loreArray = entry.getValue().getAsJsonArray();
                    List<Component> lore = new ArrayList<>();
                    for (JsonElement loreElement : loreArray) {
                        Component loreLine = GsonComponentSerializer.gson().deserialize(loreElement.getAsString());
                        lore.add(loreLine);
                    }
                    meta.lore(lore);
                    break;
                case "customModelData":
                    meta.setCustomModelData(entry.getValue().getAsInt());
                    break;
                case "itemFlags":
                    JsonArray flagsArray = entry.getValue().getAsJsonArray();
                    for (JsonElement flagElement : flagsArray) {
                        meta.addItemFlags(ItemFlag.valueOf(flagElement.getAsString()));
                    }
                    break;
                case "itemType":
                    pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING, entry.getValue().getAsString());
                    break;
                case "name":
                    pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING, entry.getValue().getAsString());
                    break;
                case "unstackable":
                    pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN, entry.getValue().getAsBoolean());
                    break;
                case "uniqueID":
                    pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING, entry.getValue().getAsString());
                    break;
            }
        }

        item.getItemStack().setItemMeta(meta);

        return item;
    }
}
