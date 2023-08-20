package com.stelios.cakenaysh.Util;

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

    public Database() {
        client = MongoClients.create();
        database = client.getDatabase("cakenaysh");
        playerStats = database.getCollection("playerStats");
        playerQuests = database.getCollection("playerQuests");
        playerRecipes = database.getCollection("playerRecipes");
        playerStashes = database.getCollection("playerStashes");
        playerItems = database.getCollection("playerItems");
        npcInfo = database.getCollection("npcInfo");
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

}
