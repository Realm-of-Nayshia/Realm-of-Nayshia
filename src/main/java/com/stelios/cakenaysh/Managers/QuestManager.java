package com.stelios.cakenaysh.Managers;

import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Quests.Quest;
import com.stelios.cakenaysh.Quests.Quests;
import com.stelios.cakenaysh.Util.CustomPlayer;
import org.bson.Document;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class QuestManager {

    private final Main main = Main.getPlugin(Main.class);
    private final MongoCollection<Document> quests = main.getDatabase().getPlayerQuests();


    //create a quest document for the specified player
    public void createQuestFile(Player player){

        //if the player already has a quest file, don't create a new one
        if (hasQuestFile(player)) {
            return;
        }

        //create the quest file
        ArrayList<String> questName = new ArrayList<>();
        ArrayList<String> questStatus = new ArrayList<>();
        ArrayList<Integer> questTimesCompleted = new ArrayList<>();
        ArrayList<Date> questLastCompleted = new ArrayList<>();
        ArrayList<ArrayList<Double>> startingKillCount = updateStartingKillCount(player);

        //add all quests to the quest file
        for (Quests quest : Quests.values()){
            questName.add(quest.getQuest().getName());
            questStatus.add("locked");
            questTimesCompleted.add(0);
            questLastCompleted.add(new Date(0));
        }

        quests.insertOne(new Document("uuid", player.getUniqueId().toString()).append("player", player.getName())
                .append("activeQuestCount", 0)
                .append("questInfo", questName)
                .append("questStatus", questStatus)
                .append("questTimesCompleted", questTimesCompleted)
                .append("questLastCompleted", questLastCompleted)
                .append("startingKillCount", startingKillCount));
    }


    //get the player's active quests
    public ArrayList<String> getActiveQuests (Player player) {

        //make sure the player has a quest file
        if (!hasQuestFile(player)) {
            return new ArrayList<>();
        }

        //get the player's active quests
        ArrayList<String> activeQuests = new ArrayList<>();
        ArrayList<String> questStatus = getQuestStatuses(player);

        for (int i = 0; i < questStatus.size(); i++) {
            if (questStatus.get(i).equals("active")) {
                activeQuests.add(getQuestInfo(player).get(i));
            }
        }

        return activeQuests;
    }


    //if the player has the specified quest active
    public boolean hasQuestActive(Player player, Quest quest){
        return getActiveQuests(player).contains(quest.getName());
    }

    //if the player has the specified quest completed
    public boolean hasQuestCompleted(Player player, Quest quest){
        return getQuestStatuses(player).get(getQuestInfo(player).indexOf(quest.getName())).equals("completed");
    }

    //if the player can complete the specified quest
    public boolean canCompleteQuest (Player player, Quest quest) {

        //make sure the player has a quest file and the quest is active
        if (!hasQuestFile(player) && !hasQuestActive(player, quest)) {
            return false;
        }

        //if the player doesn't have the required items, stats, quests, or npc kills, return false
        return hasItemsToComplete(player, quest) && hasStatsToComplete(player, quest) && hasNpcsKillsToComplete(player, quest);
    }


    //change a quest status for the target player
    public void setQuestStatus (Player player, Quest quest, String status){

        //make sure the player has a quest file
        if (!hasQuestFile(player)) {
            return;
        }

        //if the player has too many active quests or can't accept the quest at the moment, don't set the quest to active
        if (status.equals("active") && !canAcceptQuest(player, quest,true)) {
            return;
        }

        //if the quest is being set to active
        if (status.equals("active")) {

            //update the starting kill count if the quest is being set to active
            updateStartingKillCount(player);

            //take the items from the player if the quest requires it
            if (quest.getQuestAcceptRequirements().takeItems()) {
                main.getPlayerInventoryManager().removeItemsWithNameFromInventory(player, quest.getQuestAcceptRequirements().getItems());
            }
        }

        //set the quest to the specified status
        ArrayList<String> questStatus = getQuestStatuses(player);
        questStatus.set(getQuestInfo(player).indexOf(quest.getName()), status);
        quests.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("questStatus", questStatus)));

        //recalculate the player's active quest count
        recalculateQuestCount(player);

        //update the quest statuses
        updateQuestStatus(player);
    }


    //complete the specified quest for target player
    public void completeQuest (Player player, Quest quest) {

        //make sure the player has a quest file
        if (!hasQuestFile(player)) {
            return;
        }

        //if the player doesn't have the quest active or doesn't the completion requirements, don't complete the quest
        if (!getQuestStatuses(player).get(getQuestInfo(player).indexOf(quest.getName())).equals("active") || !canCompleteQuest(player, quest)) {
            return;
        }

        //take the required items from the player
        if (quest.getQuestCompletionRequirements().takeItems()) {
            main.getPlayerInventoryManager().removeItemsWithNameFromInventory(player, quest.getQuestCompletionRequirements().getItems());
        }

        //complete the quest
        ArrayList<Integer> questTimesCompleted = getQuestTimesCompleted(player);
        ArrayList<Date> questLastCompleted = getQuestLastCompleted(player);
        questTimesCompleted.set(getQuestInfo(player).indexOf(quest.getName()), questTimesCompleted.get(getQuestInfo(player).indexOf(quest.getName())) + 1);
        questLastCompleted.set(getQuestInfo(player).indexOf(quest.getName()), new Date());

        //if the quest is a one time quest, set the quest to completed
        if (quest.getCooldown() < 0) {
            setQuestStatus(player, quest, "completed");

        //else, set the quest to locked
        } else {
            setQuestStatus(player, quest, "locked");
        }

        quests.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("questTimesCompleted", questTimesCompleted)));
        quests.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("questLastCompleted", questLastCompleted)));

        ////give the player the quest rewards
        main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addXp(quest.getQuestRewards().getXp());
        main.getPlayerInventoryManager().addItemsToInventory(player, quest.getQuestRewards().getItems());

        //add the recipes to the player
        for (ShapedRecipe recipe : quest.getQuestRewards().getRecipes()) {
            main.getRecipeManager().addRecipe(player, recipe.getKey().getKey());
        }

        //add the stats to the player
        for (String stat : quest.getQuestRewards().getStats().keySet()) {
            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addPermanentStat(stat, quest.getQuestRewards().getStats().get(stat));
        }

        //update the startingKillCount
        updateStartingKillCount(player);

        //update the quest statuses
        updateQuestStatus(player);
    }


    //lock or unlock all applicable quests for the target player
    public void updateQuestStatus (Player player) {

        //make sure the player has a quest file
        if (!hasQuestFile(player)) {
            return;
        }

        //get the player's quest file
        ArrayList<String> questInfo = getQuestInfo(player);
        ArrayList<String> questStatus = getQuestStatuses(player);

        //loop through all the quests
        for (int i = 0; i < questInfo.size(); i++) {

            //if the player has met the requirements for the quest, unlock the quest
            if (questStatus.get(i).equals("locked") && canAcceptQuest(player, getQuestFromName(questInfo.get(i)),false)) {
                questStatus.set(i, "unlocked");

            //if the player has not met the requirements for the quest, lock the quest
            } else if (questStatus.get(i).equals("unlocked") && !canAcceptQuest(player, getQuestFromName(questInfo.get(i)),false)) {
                questStatus.set(i, "locked");
            }
        }

        //update the quest statuses
        quests.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("questStatus", questStatus)));
    }


    //recalculate the player's active quest count
    public void recalculateQuestCount (Player player) {

        //make sure the player has a quest file
        if (!hasQuestFile(player)) {
            return;
        }

        //get the player's quest file
        ArrayList<String> questStatus = getQuestStatuses(player);

        //loop through all the quests
        int activeQuestCount = 0;
        for (String questStatuses : questStatus) {
            if (questStatuses.equals("active")) {
                activeQuestCount++;
            }
        }

        //update the database
        quests.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("activeQuestCount", activeQuestCount)));
    }


    //if the player accept the quest
    public boolean canAcceptQuest(Player player, Quest quest, boolean includeQuestLimit){

        //make sure the player has a quest file
        if (!hasQuestFile(player)) {
            return false;
        }

        //if the player has too many active quests, return false
        if (includeQuestLimit && getActiveQuests(player).size() >= 3) {
            return false;
        }

        //if the player has the required amount of quests, items, stats, quests, and cooldown, return true
        return hasItemsToAccept(player, quest) && hasStatsToAccept(player, quest) && hasRequiredQuestsCompleted(player, quest)
                && hasNpcsKillsToAccept(player, quest) && (Math.abs(Duration.between(getQuestLastCompleted(player).get(getQuestInfo(player).indexOf(quest.getName())).toInstant(), new Date().toInstant()).toMinutes()) > quest.getCooldown());
    }


    //if the player has the required items to accept the quest
    public boolean hasItemsToAccept(Player player, Quest quest) {

        //get the required items
        ArrayList<String> requiredItemNames = quest.getQuestAcceptRequirements().getItems();

        //get the items in the player's inventory
        ItemStack[] playerItems = player.getInventory().getContents();

        //get the number of required items obtained
        int requiredItemsObtained = 0;

        //loop through all the required item names
        for (String itemName : requiredItemNames) {

            //loop through all the player's items
            for (ItemStack item : playerItems) {

                //if the item is null or doesn't have a custom name, continue
                if (item == null || item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING) == null) {
                    continue;
                }

                //if the item's name is the same as the required item name, add one to the required items obtained
                if (Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)).equals(itemName)) {
                    requiredItemsObtained++;
                    break;
                }
            }
        }

        //return if the player has the required items
        return requiredItemsObtained == requiredItemNames.size();
    }


    //if the player has the required stats to accept the quest
    public boolean hasStatsToAccept(Player player, Quest quest) {

        //get the required stats
        HashMap<String, Float> requiredStats = quest.getQuestAcceptRequirements().getStats();
        CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

        //loop through all the required stats
        for (String stat : requiredStats.keySet()) {

            //if the player doesn't have the required stat, return false
            if (customPlayer.meetsStatRequirement(stat, requiredStats.get(stat), null)) {
                return false;
            }
        }

        return true;
    }


    //if the player has the required npc kills to accept the quest
    public boolean hasNpcsKillsToAccept(Player player, Quest quest) {

        //get the required npc kills
        HashMap<ArrayList<String>, Integer> requiredNpcKills = quest.getQuestAcceptRequirements().getNpcKills();

        //loop through all the required npc arrays
        for (ArrayList<String> npcUuidList : requiredNpcKills.keySet()) {

            //total kills
            int totalKills = 0;

            //loop through all the npcs
            for (String npcUUID : npcUuidList) {

                //if the npc's killed document doesn't exist, continue
                if (main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first() == null) {
                    continue;
                }

                //get the npc's killed document
                HashMap<String, Double> npcKilled = getNpcKilled(npcUUID);

                //if the player hasn't killed the npc at all, continue;
                if (!npcKilled.containsKey(player.getName())) {
                    continue;
                }

                //add the player's npc kills to the total kills
                totalKills += npcKilled.get(player.getName());

            }

            //if the player hasn't killed the required amount of the npcs, return false
            if (totalKills < requiredNpcKills.get(npcUuidList)) {
                return false;
            }
        }

        return true;
    }


    //if the player has the required quests to accept the quest
    public boolean hasRequiredQuestsCompleted(Player player, Quest quest) {

        //get the quests and the times they have been completed
        ArrayList<String> questInfo = getQuestInfo(player);
        ArrayList<Integer> questTimesCompleted = getQuestTimesCompleted(player);

        //loop through all the required quests
        for (Quest requiredQuest : quest.getQuestAcceptRequirements().getQuests()) {

            //if the player hasn't completed the required quest, return false
            if (questTimesCompleted.get(questInfo.indexOf(requiredQuest.getName())) == 0) {
                return false;
            }
        }

        return true;
    }


    //if the player has the required items to complete the quest
    public boolean hasItemsToComplete (Player player, Quest quest) {

        //get the required items
        ArrayList<String> requiredItemNames = quest.getQuestCompletionRequirements().getItems();

        //get the items in the player's inventory
        ItemStack[] playerItems = player.getInventory().getContents();

        //get the number of required items obtained
        int requiredItemsObtained = 0;

        //loop through all the required item names
        for (String itemName : requiredItemNames) {

            //loop through all the player's items
            for (ItemStack item : playerItems) {

                //if the item is null or doesn't have a custom name, continue
                if (item == null || item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING) == null) {
                    continue;
                }

                //if the item's name is the same as the required item name, add one to the required items obtained
                if (Objects.requireNonNull(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "name"), PersistentDataType.STRING)).equals(itemName)) {
                    requiredItemsObtained++;
                    break;
                }
            }
        }

        //return if the player has the required items
        return requiredItemsObtained == requiredItemNames.size();
    }


    //if the player has the required stats to complete the quest
    public boolean hasStatsToComplete (Player player, Quest quest) {

        //get the required stats
        HashMap<String, Float> requiredStats = quest.getQuestCompletionRequirements().getStats();

        //loop through all the required stats
        for (String stat : requiredStats.keySet()) {

            //if the player doesn't have the required stat, return false
            if (!main.getPlayerManager().getCustomPlayer(player.getUniqueId()).meetsStatRequirement(stat, requiredStats.get(stat), null)) {
                return false;
            }
        }

        return true;
    }


    //if the player has the required npc kills to complete the quest
    public boolean hasNpcsKillsToComplete (Player player, Quest quest) {

        //get the required npc kills
        HashMap<ArrayList<String>, Integer> requiredNpcKills = quest.getQuestCompletionRequirements().getNpcKills();

        //loop through all the required npc arrays
        for (ArrayList<String> npcUuidList : requiredNpcKills.keySet()) {

            //total kills
            int totalKills = 0;

            //loop through all the npcs
            for (String npcUUID : npcUuidList) {

                //if the npc's killed document doesn't exist, continue
                if (main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first() == null) {
                    continue;
                }

                //get the npc's killed document
                HashMap<String, Double> npcKilled = getNpcKilled(npcUUID);

                //if the player hasn't killed the npc at all, continue
                if (!npcKilled.containsKey(player.getName())) {
                    continue;
                }

                //add the player's npc kills to the total kills
                totalKills += npcKilled.get(player.getName());

            }

            //get the required amount of npcs the player needs to kill
            double requiredKills = requiredNpcKills.get(npcUuidList);

            //get the index of the quest
            int questIndex = getQuestInfo(player).indexOf(quest.getName());

            //loop through the player's starting kill count for the specified quest
            for (int j = 0; j < getStartingKillCount(player).get(questIndex).size(); j++) {

                //add the player's starting kill count to the required kills
                requiredKills += getStartingKillCount(player).get(questIndex).get(j);
            }

            //if the player hasn't killed the required amount of the npcs, return false
            if (totalKills < requiredKills) {
                return false;
            }
        }

        return true;
    }


    //update the active starting kill count for the specified quest
    public ArrayList<ArrayList<Double>> updateStartingKillCount(Player player) {

        ArrayList<ArrayList<Double>> startingKillCount = new ArrayList<>();

        //add the starting active kill count for each npc
        ArrayList<Double> killCountActive = new ArrayList<>();

        //loop through all quests
        for (Quests quest : Quests.values()) {

            //clear the kill count
            killCountActive.clear();

            //loop through all the npc arrays
            for (ArrayList<String> npcUuidList : quest.getQuest().getQuestCompletionRequirements().getNpcKills().keySet()) {

                //loop through all the npcs
                for (String npcUUID : npcUuidList) {

                    //if the npc's killed document doesn't exist, continue
                    if (main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first() == null) {
                        continue;
                    }

                    //get the npc's killed document
                    HashMap<String, Double> npcKilled = getNpcKilled(npcUUID);

                    //set the kill count to the number of times the player has killed the npc
                    killCountActive.add(npcKilled.getOrDefault(player.getName(), 0.0));
                }
            }

            startingKillCount.add(killCountActive);
        }

        //update the database if it exists
        if (!hasQuestFile(player)) {
            return startingKillCount;
        }
        quests.updateOne(Filters.eq("uuid", player.getUniqueId().toString()), new Document("$set", new Document("startingKillCount", startingKillCount)));

        return startingKillCount;
    }


    //gets the quest object from the quest name
    public Quest getQuestFromName(String questName){
        for (Quests quest : Quests.values()){
            if (quest.getQuest().getName().equalsIgnoreCase(questName)){
                return quest.getQuest();
            }
        }
        return null;
    }


    //if the player has a quest file
    public boolean hasQuestFile (Player player) {
        return quests.find(new Document("uuid", player.getUniqueId().toString())).first() != null;
    }


    //getters
    public ArrayList<String> getQuestInfo(Player player){
        return (ArrayList<String>) quests.find(new Document("uuid", player.getUniqueId().toString())).first().get("questInfo");
    }
    public ArrayList<String> getQuestStatuses(Player player){
        return (ArrayList<String>) quests.find(new Document("uuid", player.getUniqueId().toString())).first().get("questStatus");
    }
    public ArrayList<Integer> getQuestTimesCompleted(Player player){
        return (ArrayList<Integer>) quests.find(new Document("uuid", player.getUniqueId().toString())).first().get("questTimesCompleted");
    }
    public ArrayList<Date> getQuestLastCompleted(Player player){
        return (ArrayList<Date>) quests.find(new Document("uuid", player.getUniqueId().toString())).first().get("questLastCompleted");
    }
    public ArrayList<ArrayList<Double>> getStartingKillCount(Player player){
        return (ArrayList<ArrayList<Double>>) quests.find(new Document("uuid", player.getUniqueId().toString())).first().get("startingKillCount");
    }
    public HashMap<String, Double> getNpcKilled(String npcUUID) {
        return new GsonBuilder().create().fromJson(Objects.requireNonNull(main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first()).get("killed").toString(), HashMap.class);
    }

}
