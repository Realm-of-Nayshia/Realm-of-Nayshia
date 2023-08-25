package com.stelios.cakenaysh.Quests;

import com.stelios.cakenaysh.Items.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestRewards {

    private final int xp;
    private final ArrayList<Item> items;
    private final HashMap<String, Integer> stats;


    //@param xp: the xp rewarded
    //@param items: the items rewarded
    //@param stats: the stats rewarded
    public QuestRewards(int xp, ArrayList<Item> items, HashMap<String, Integer> stats) {
        this.xp = xp;
        this.items = items;
        this.stats = stats;
    }

    //getters
    public int getXp() {
        return xp;
    }
    public ArrayList<Item> getItems() {
        return items;
    }
    public HashMap<String, Integer> getStats() {
        return stats;
    }

}
