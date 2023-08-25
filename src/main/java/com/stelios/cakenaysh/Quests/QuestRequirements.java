package com.stelios.cakenaysh.Quests;

import com.stelios.cakenaysh.Items.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestRequirements {


    private final ArrayList<Item> items;
    private final boolean takeItems;
    private final HashMap<String, Float> stats;
    private final ArrayList<Quest> quests;
    private final HashMap<String, Integer> npcKills;


    //@param items: the items required to unlock a quest
    //@param takeItems: should the required items be taken to unlock the quest
    //@param stats: the stats required to unlock a quest
    public QuestRequirements(ArrayList<Item> items, boolean takeItems, HashMap<String, Float> stats, ArrayList<Quest> quests,
                             HashMap<String, Integer> npcKills) {
        this.items = items;
        this.takeItems = takeItems;
        this.stats = stats;
        this.quests = quests;
        this.npcKills = npcKills;
    }

    //getters
    public ArrayList<Item> getItems() {
        return items;
    }
    public boolean takeItems() {
        return takeItems;
    }
    public HashMap<String, Float> getStats() {
        return stats;
    }
    public ArrayList<Quest> getQuests() {
        return quests;
    }
    public HashMap<String, Integer> getNpcKills() {
        return npcKills;
    }
}
