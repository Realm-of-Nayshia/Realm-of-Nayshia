package com.stelios.RealmOfNayshia.Util;

import com.google.gson.*;
import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.RealmOfNayshia.Items.BattleItem;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class BattleItemSerializer implements JsonSerializer<BattleItem>, JsonDeserializer<BattleItem> {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
            .create();

    @Override
    public JsonElement serialize(BattleItem battleItem, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        // Serialize basic item data using the ItemSerializer logic
        JsonElement itemElement = context.serialize((Item) battleItem, Item.class);
        jsonObject = itemElement.getAsJsonObject();

        System.out.println(itemElement.getAsJsonObject());

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

        // Serialize itemType
        PersistentDataContainer pdc = battleItem.getItemMeta().getPersistentDataContainer();
        if (pdc.has(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING)) {
            jsonObject.addProperty("itemType", pdc.get(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING));
        }

        // Serialize PotionEffects
        PotionEffect[] potionEffects = battleItem.getPotionEffects();
        JsonArray potionEffectsArray = new JsonArray();
        if (potionEffects != null) {
            for (PotionEffect potionEffect : potionEffects) {
                potionEffectsArray.add(gson.toJsonTree(potionEffect));
            }
        }
        jsonObject.add("potionEffects", potionEffectsArray);

        // Serialize AttributeModifiers
        JsonObject attributeModifiersObject = new JsonObject();
        for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : battleItem.getItemMeta().getAttributeModifiers().asMap().entrySet()) {
            JsonArray modifiersArray = new JsonArray();
            for (AttributeModifier modifier : entry.getValue()) {
                JsonObject modifierObject = new JsonObject();
                modifierObject.addProperty("name", modifier.getName());
                modifierObject.addProperty("amount", modifier.getAmount());
                modifierObject.addProperty("operation", modifier.getOperation().name());
                if (modifier.getSlot() != null) { modifierObject.addProperty("slot", modifier.getSlot().name()); }
                modifierObject.addProperty("uuid", modifier.getUniqueId().toString());
                modifiersArray.add(modifierObject);
            }
            attributeModifiersObject.add(entry.getKey().name(), modifiersArray);
        }
        jsonObject.add("attributeModifiers", attributeModifiersObject);

        // Serialize ItemMeta
        JsonObject metaObject = new JsonObject();
        ItemMeta meta = battleItem.getItemMeta();

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

        if (meta.hasCustomModelData()) {
            metaObject.addProperty("customModelData", meta.getCustomModelData());
        }

        if (!meta.getItemFlags().isEmpty()) {
            JsonArray itemFlagsArray = new JsonArray();
            for (ItemFlag itemFlag : meta.getItemFlags()) {
                itemFlagsArray.add(itemFlag.name());
            }
            metaObject.add("itemFlags", itemFlagsArray);
        }

        if (meta.isUnbreakable()) {
            metaObject.addProperty("unbreakable", true);
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
        String itemType = jsonObject.has("itemType") ? jsonObject.get("itemType").getAsString() : null;
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
            potionEffects[i] = gson.fromJson(potionEffectsArray.get(i), PotionEffect.class);
        }

        BattleItem battleItem = new BattleItem(material, amount, unstackable, name, itemType, damage, attackSpeed,
                critDamage, critChance, strength, health, healthRegen, stamina, staminaRegen, defense, speed, infernalDefense, infernalDamage,
                undeadDefense, undeadDamage, aquaticDefense, aquaticDamage, aerialDefense, aerialDamage, meleeDefense, meleeDamage, rangedDefense,
                rangedDamage, magicDefense, magicDamage, meleeProficiency, rangedProficiency, armorProficiency, textureURL, potionEffects);

        // Deserialize ItemMeta
        JsonObject metaObject = jsonObject.get("itemMeta").getAsJsonObject();
        ItemMeta meta = battleItem.getItemStack().getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        // Deserialize known PDC fields
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "unstackable"), PersistentDataType.BOOLEAN, unstackable);
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "itemType"), PersistentDataType.STRING, jsonObject.get("itemType").getAsString());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING, jsonObject.get("name").getAsString());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "damage"), PersistentDataType.FLOAT, jsonObject.get("damage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "attackSpeed"), PersistentDataType.FLOAT, jsonObject.get("attackSpeed").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "critDamage"), PersistentDataType.FLOAT, jsonObject.get("critDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "critChance"), PersistentDataType.FLOAT, jsonObject.get("critChance").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "strength"), PersistentDataType.FLOAT, jsonObject.get("strength").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "health"), PersistentDataType.FLOAT, jsonObject.get("health").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "healthRegen"), PersistentDataType.FLOAT, jsonObject.get("healthRegen").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "stamina"), PersistentDataType.FLOAT, jsonObject.get("stamina").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "staminaRegen"), PersistentDataType.FLOAT, jsonObject.get("staminaRegen").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "defense"), PersistentDataType.FLOAT, jsonObject.get("defense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "speed"), PersistentDataType.FLOAT, jsonObject.get("speed").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "infernalDefense"), PersistentDataType.FLOAT, jsonObject.get("infernalDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "infernalDamage"), PersistentDataType.FLOAT, jsonObject.get("infernalDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "undeadDefense"), PersistentDataType.FLOAT, jsonObject.get("undeadDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "undeadDamage"), PersistentDataType.FLOAT, jsonObject.get("undeadDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aquaticDefense"), PersistentDataType.FLOAT, jsonObject.get("aquaticDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aquaticDamage"), PersistentDataType.FLOAT, jsonObject.get("aquaticDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aerialDefense"), PersistentDataType.FLOAT, jsonObject.get("aerialDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "aerialDamage"), PersistentDataType.FLOAT, jsonObject.get("aerialDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "meleeDefense"), PersistentDataType.FLOAT, jsonObject.get("meleeDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "meleeDamage"), PersistentDataType.FLOAT, jsonObject.get("meleeDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "rangedDefense"), PersistentDataType.FLOAT, jsonObject.get("rangedDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "rangedDamage"), PersistentDataType.FLOAT, jsonObject.get("rangedDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "magicDefense"), PersistentDataType.FLOAT, jsonObject.get("magicDefense").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "magicDamage"), PersistentDataType.FLOAT, jsonObject.get("magicDamage").getAsFloat());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "meleeProficiency"), PersistentDataType.INTEGER, jsonObject.get("meleeProficiency").getAsInt());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "rangedProficiency"), PersistentDataType.INTEGER, jsonObject.get("rangedProficiency").getAsInt());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "armorProficiency"), PersistentDataType.INTEGER, jsonObject.get("armorProficiency").getAsInt());
        pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "potionEffects"), DataType.POTION_EFFECT_ARRAY, potionEffects);

        if (unstackable) {
            pdc.set(new NamespacedKey(Main.getPlugin(Main.class), "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());
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

        if (metaObject.has("customModelData")) {
            meta.setCustomModelData(metaObject.get("customModelData").getAsInt());
        }

        if (metaObject.has("itemFlags")) {
            JsonArray itemFlagsArray = metaObject.get("itemFlags").getAsJsonArray();
            for (JsonElement flagElement : itemFlagsArray) {
                meta.addItemFlags(ItemFlag.valueOf(flagElement.getAsString()));
            }
        }

        if (metaObject.has("unbreakable")) {
            meta.setUnbreakable(metaObject.get("unbreakable").getAsBoolean());
        }

        // Deserialize AttributeModifiers
        if (jsonObject.has("attributeModifiers")) {
            JsonObject attributeModifiersObject = jsonObject.getAsJsonObject("attributeModifiers");
            for (Map.Entry<String, JsonElement> entry : attributeModifiersObject.entrySet()) {
                Attribute attribute = Attribute.valueOf(entry.getKey());
                JsonArray modifiersArray = entry.getValue().getAsJsonArray();
                for (JsonElement modifierElement : modifiersArray) {
                    JsonObject modifierObject = modifierElement.getAsJsonObject();
                    String modifierName = modifierObject.get("name").getAsString();
                    double modifierAmount = modifierObject.get("amount").getAsDouble();
                    AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(modifierObject.get("operation").getAsString());
                    EquipmentSlot slot = EquipmentSlot.valueOf(modifierObject.get("slot").getAsString());
                    UUID modifierUUID = UUID.fromString(modifierObject.get("uuid").getAsString());
                    AttributeModifier modifier = new AttributeModifier(modifierUUID, modifierName, modifierAmount, operation, slot);
                    meta.addAttributeModifier(attribute, modifier);
                }
            }
        }
        battleItem.getItemStack().setItemMeta(meta);

        // Handle texture URL for SkullMeta
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
            battleItem.getItemStack().setItemMeta(skullMeta);
        } else {
            battleItem.getItemStack().setItemMeta(meta);
        }

        return battleItem;
    }
}
