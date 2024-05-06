package com.stelios.RealmOfNayshia.Quests;

import com.stelios.RealmOfNayshia.Items.Item;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestRewards {

    private final int xp;
    private final HashMap<Item, Integer> items;
    private final ArrayList<ShapedRecipe> recipes;
    private final HashMap<String, Integer> stats;


    //@param xp: the xp rewarded
    //@param items: the items rewarded
    //@param stats: the stats rewarded
    public QuestRewards(int xp, HashMap<Item, Integer> items, ArrayList<ShapedRecipe> recipes, HashMap<String, Integer> stats) {
        this.xp = xp;
        this.items = items;
        this.recipes = recipes;
        this.stats = stats;
    }

    //getters
    public int getXp() {
        return xp;
    }
    public HashMap<Item, Integer> getItems() {
        return items;
    }
    public ArrayList<ShapedRecipe> getRecipes() {
        return recipes;
    }
    public HashMap<String, Integer> getStats() {
        return stats;
    }

}
