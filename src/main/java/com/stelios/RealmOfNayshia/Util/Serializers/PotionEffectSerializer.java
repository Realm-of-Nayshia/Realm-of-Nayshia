package com.stelios.RealmOfNayshia.Util.Serializers;

import com.google.gson.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectSerializer implements JsonSerializer<PotionEffect>, JsonDeserializer<PotionEffect> {

    @Override
    public JsonElement serialize(PotionEffect potionEffect, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", potionEffect.getType().getName());
        jsonObject.addProperty("duration", potionEffect.getDuration());
        jsonObject.addProperty("amplifier", potionEffect.getAmplifier());
        jsonObject.addProperty("ambient", potionEffect.isAmbient());
        jsonObject.addProperty("particles", potionEffect.hasParticles());
        jsonObject.addProperty("icon", potionEffect.hasIcon());
        return jsonObject;
    }

    @Override
    public PotionEffect deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        PotionEffectType effectType = PotionEffectType.getByName(jsonObject.get("type").getAsString());
        int duration = jsonObject.get("duration").getAsInt();
        int amplifier = jsonObject.get("amplifier").getAsInt();
        boolean ambient = jsonObject.get("ambient").getAsBoolean();
        boolean particles = jsonObject.get("particles").getAsBoolean();
        boolean icon = jsonObject.get("icon").getAsBoolean();
        return new PotionEffect(effectType, duration, amplifier, ambient, particles, icon);
    }
}
