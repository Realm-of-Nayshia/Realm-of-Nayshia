package com.stelios.RealmOfNayshia.Managers;

import com.google.gson.*;
import com.mongodb.client.MongoCollection;
import com.stelios.RealmOfNayshia.Items.BattleItem;
import com.stelios.RealmOfNayshia.Items.ConsumableItem;
import com.stelios.RealmOfNayshia.Items.Item;
import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.Util.Serializers.BattleItemSerializer;
import com.stelios.RealmOfNayshia.Util.Serializers.ConsumableItemSerializer;
import com.stelios.RealmOfNayshia.Util.Serializers.ItemSerializer;
import com.stelios.RealmOfNayshia.Util.Serializers.PotionEffectSerializer;
import org.bson.Document;
import org.bukkit.potion.PotionEffect;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    Main main = Main.getPlugin(Main.class);
    MongoCollection<Document> itemsCollection = main.getDatabase().getItems();
    MongoCollection<Document> battleItemCollection = main.getDatabase().getBattleItems();
    MongoCollection<Document> consumableItemCollection = main.getDatabase().getConsumableItems();
    MongoCollection<Document> recipesCollection = main.getDatabase().getRecipes();
    MongoCollection<Document> questsCollection = main.getDatabase().getQuests();
    MongoCollection<Document> setBonusesCollection = main.getDatabase().getSetBonuses();

    //pull the latest data from the mongodb item collection
    public void pullItemsFromDatabase(File itemFile){

        //create Gson instances
        Gson itemGson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemSerializer())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
                .setPrettyPrinting().create();
        Gson battleItemGson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemSerializer())
                .registerTypeAdapter(BattleItem.class, new BattleItemSerializer())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
                .setPrettyPrinting().create();
        Gson consumableGson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemSerializer())
                .registerTypeAdapter(ConsumableItem.class, new ConsumableItemSerializer())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
                .setPrettyPrinting().create();

        //grab the item json files from the database
        ArrayList<Document> items = itemsCollection.find().into(new ArrayList<>());
        ArrayList<Document> battleItems = battleItemCollection.find().into(new ArrayList<>());
        ArrayList<Document> consumableItems = consumableItemCollection.find().into(new ArrayList<>());

        //write the items to the file
        try (Writer writer = new FileWriter(itemFile)) {

            //ensure the file starts with an opening bracket to make it a valid JSON array
            writer.write("[\n");

            //helper method to write items to the file with proper formatting
            writeItems(writer, items, itemGson, true);
            writeItems(writer, battleItems, battleItemGson, items.isEmpty());
            writeItems(writer, consumableItems, consumableGson, items.isEmpty() && battleItems.isEmpty());

            //ensure the file ends with a closing bracket to make it a valid JSON array
            writer.write("\n]");
            writer.flush();
            main.getLogger().info("Successfully wrote items to file");
        } catch (IOException e) {
            main.getLogger().severe("Failed to write items to file" + e.getMessage());
        }
    }

    private void writeItems(Writer writer, List<Document> items, Gson gson, boolean isFirst) throws IOException {
        for (Document item : items) {
            if (!isFirst) {
                writer.write(",\n");
            }
            isFirst = false;
            item.remove("_id");
            JsonElement jsonElement = JsonParser.parseString(item.toJson());
            writer.write(gson.toJson(jsonElement));
        }
    }

    //pull the latest data from the mongodb recipe collection
    public void pullRecipeesFromDatabase(File recipeFile){

    }

    //pull the latest data from the mongodb quest collection
    public void pullQuestsFromDatabase(File questFile){

    }

    //pull the latest data from the mongodb setBonuses collection
    public void pullSetBonusesFromDatabase(File setBonuseFile){

    }

    //push the latest data from json files into the mongodb item collection
    public void pushItemsFromFile(File itemFile) {

        //create Gson instances
        Gson itemGson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemSerializer())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
                .setPrettyPrinting().create();
        Gson battleItemGson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemSerializer())
                .registerTypeAdapter(BattleItem.class, new BattleItemSerializer())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
                .setPrettyPrinting().create();
        Gson consumableGson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemSerializer())
                .registerTypeAdapter(ConsumableItem.class, new ConsumableItemSerializer())
                .registerTypeAdapter(PotionEffect.class, new PotionEffectSerializer())
                .setPrettyPrinting().create();

        //read the item json file
        try (FileReader reader = new FileReader(itemFile)) {

            //parse json file
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            List<Document> items = new ArrayList<>();
            List<Document> battleItems = new ArrayList<>();
            List<Document> consumableItems = new ArrayList<>();

            //iterate through the json array and add the items to the appropriate list
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String type = jsonObject.get("itemType").getAsString();

                //check if the item already exists in the database
                boolean exists = checkIfItemExists(name, type);

                if (!exists) {
                    switch (type) {
                        case "regular":
                            Document itemDocument = Document.parse(itemGson.toJson(jsonObject));
                            items.add(itemDocument);
                            break;
                        case "accessory", "armor", "weapon":
                            Document battleDocument = Document.parse(battleItemGson.toJson(jsonObject));
                            battleItems.add(battleDocument);
                            break;
                        case "consumable":
                            Document consumableDocument = Document.parse(consumableGson.toJson(jsonObject));
                            consumableItems.add(consumableDocument);
                            break;
                    }
                }
            }

            //insert documents into the appropriate collections
            if (!items.isEmpty()) {
                itemsCollection.insertMany(items);
            }
            if (!battleItems.isEmpty()) {
                battleItemCollection.insertMany(battleItems);
            }
            if (!consumableItems.isEmpty()) {
                consumableItemCollection.insertMany(consumableItems);
            }
            main.getLogger().info("Successfully pushed items from file to database!");
        } catch (IOException e) {
            main.getLogger().severe("Failed to read items from file: " + e.getMessage());
        }
    }

    //check if an item with the given name and type already exists in the database
    private boolean checkIfItemExists(String name, String type) {
        MongoCollection<Document> collection = switch (type) {
            case "weapon", "armor", "accessory" -> battleItemCollection;
            case "consumable" -> consumableItemCollection;
            default -> itemsCollection;
        };

        Document query = new Document("name", name);
        long count = collection.countDocuments(query);
        return count > 0;
    }

    //push the latest data from json files into the mongodb recipe collection
    public void pushRecipesFromFile(File recipeFile){

    }

    //push the latest data from json files into the mongodb quest collection
    public void pushQuestsFromFile(File questFile){

    }

    //push the latest data from json files into the mongodb setBonuses collection
    public void pushSetBonusesFromFile(File setBonuseFile){

    }

}
