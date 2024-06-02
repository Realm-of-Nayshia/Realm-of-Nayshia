package com.stelios.RealmOfNayshia.Util.Serializers;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.*;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.RealmOfNayshia.Items.ConsumableItem;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        String textureURL = item.getTextureURL();

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
            }
        }

        // Set the PDC values
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING, "consumable");
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING, name);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN, unstackable);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "foodValue"), PersistentDataType.INTEGER, foodValue);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "saturationValue"), PersistentDataType.FLOAT, saturationValue);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "stats"), DataType.STRING_ARRAY, stats);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "statsAmount"), DataType.INTEGER_ARRAY, statsAmount);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "statsDuration"), DataType.INTEGER_ARRAY, statsDuration);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY, potionEffects);

        if (unstackable) {
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());
        }

        if (textureURL != null) {
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "textureURL"), PersistentDataType.STRING, textureURL);
            SkullMeta skullMeta = (SkullMeta) meta;
            PlayerProfile profile = Main.getPlugin(Main.class).getServer().createProfile(UUID.randomUUID(), name);
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL("http://textures.minecraft.net/texture/" + textureURL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            profile.setTextures(textures);
            skullMeta.setPlayerProfile(profile);
            consumableItem.getItemStack().setItemMeta(skullMeta);
        } else {
            consumableItem.getItemStack().setItemMeta(meta);
        }

        return consumableItem;
    }
}
