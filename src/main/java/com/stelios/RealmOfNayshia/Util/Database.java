package com.stelios.RealmOfNayshia.Util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> playerStats;
    private final MongoCollection<Document> playerQuests;
    private final MongoCollection<Document> playerRecipes;
    private final MongoCollection<Document> playerStashes;
    private final MongoCollection<Document> playerItems;
    private final MongoCollection<Document> npcInfo;
    private final MongoCollection<Document> items;
    private final MongoCollection<Document> battleItems;
    private final MongoCollection<Document> consumableItems;
    private final MongoCollection<Document> quests;
    private final MongoCollection<Document> recipes;
    private final MongoCollection<Document> setBonuses;

    public Database() {
        client = MongoClients.create();
        database = client.getDatabase("cakenaysh");
        playerStats = database.getCollection("playerStats");
        playerQuests = database.getCollection("playerQuests");
        playerRecipes = database.getCollection("playerRecipes");
        playerStashes = database.getCollection("playerStashes");
        playerItems = database.getCollection("playerItems");
        npcInfo = database.getCollection("npcInfo");
        items = database.getCollection("items");
        battleItems = database.getCollection("battleItems");
        consumableItems = database.getCollection("consumableItems");
        quests = database.getCollection("quests");
        recipes = database.getCollection("recipes");
        setBonuses = database.getCollection("setBonuses");
    }

    //getters
    public MongoClient getClient() {
        return client;
    }
    public MongoDatabase getDatabase() {
        return database;
    }
    public MongoCollection<Document> getPlayerStats() {
        return playerStats;
    }
    public MongoCollection<Document> getPlayerQuests() {
        return playerQuests;
    }
    public MongoCollection<Document> getPlayerRecipes() {
        return playerRecipes;
    }
    public MongoCollection<Document> getPlayerStashes() {
        return playerStashes;
    }
    public MongoCollection<Document> getPlayerItems() {
        return playerItems;
    }
    public MongoCollection<Document> getNpcInfo() {
        return npcInfo;
    }
    public MongoCollection<Document> getItems() {
        return items;
    }
    public MongoCollection<Document> getBattleItems() {
        return battleItems;
    }
    public MongoCollection<Document> getConsumableItems() {
        return consumableItems;
    }
    public MongoCollection<Document> getQuests() {
        return quests;
    }
    public MongoCollection<Document> getRecipes() {
        return recipes;
    }
    public MongoCollection<Document> getSetBonuses() {
        return setBonuses;
    }

}
