package com.stelios.cakenaysh.Managers;

import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Quests.Quest;
import com.stelios.cakenaysh.Quests.Quests;
import com.stelios.cakenaysh.Util.CustomPlayer;
import org.bson.Document;
import org.bukkit.entity.Player;

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
                .append("questLastCompleted", questLastCompleted));
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
        main.getPlayerInventoryManager().removeItemsFromInventory(player, quest.getQuestCompletionRequirements().getItems());

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

        //add the stats to the player
        for (String stat : quest.getQuestRewards().getStats().keySet()) {
            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).addPermanentStat(stat, quest.getQuestRewards().getStats().get(stat));
        }

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

        //if the player has the required amount of active quests, items, stats, quests, and cooldown, return true
        return !hasItemsToAccept(player, quest) || !hasStatsToAccept(player, quest) || !hasRequiredQuestsCompleted(player, quest)
                || !hasNpcsKillsToAccept(player, quest) || !(new Date().getTime() - getQuestLastCompleted(player).get(getQuestInfo(player).indexOf(quest.getName())).getTime() < quest.getCooldown());
    }


    //if the player has the required items to accept the quest
    public boolean hasItemsToAccept(Player player, Quest quest) {

        //get the required items
        ArrayList<Item> requiredItems = quest.getQuestAcceptRequirements().getItems();

        //loop through all the required items
        for (Item item : requiredItems) {

            //if the player doesn't have the required item, return false
            if (!player.getInventory().contains(item.getItemStack())) {
                return false;
            }
        }

        return true;
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
        HashMap<String, Integer> requiredNpcKills = quest.getQuestAcceptRequirements().getNpcKills();

        //loop through all the required npc kills
        for (String npcUUID : requiredNpcKills.keySet()) {

            //get the player's npc kills
            HashMap<String, Double> npcKills = new GsonBuilder().create().fromJson((Objects.requireNonNull(main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first())).get("killed").toString(), HashMap.class);

            //if the player hasn't killed the npc at all, return false
            if (!npcKills.containsKey(player.getName())) {
                return false;
            }

            //if the player hasn't killed the required amount of the npc, return false
            if (npcKills.get(player.getName()) < requiredNpcKills.get(npcUUID)) {
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
        ArrayList<Item> requiredItems = quest.getQuestCompletionRequirements().getItems();

        //loop through all the required items
        for (Item item : requiredItems) {

            //if the player doesn't have the required item, return false
            if (!player.getInventory().contains(item.getItemStack())) {
                return false;
            }
        }

        return true;
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
        HashMap<String, Integer> requiredNpcKills = quest.getQuestCompletionRequirements().getNpcKills();

        //loop through all the required npc kills
        for (String npcUUID : requiredNpcKills.keySet()) {

            //if the npc doesn't have an info document, return false
            if (main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first() == null){
                return false;
            }

            //get the player's npc kills
            HashMap<String, Double> npcKills = new GsonBuilder().create().fromJson((Objects.requireNonNull(main.getDatabase().getNpcInfo().find(new Document("uuid", npcUUID)).first())).get("killed").toString(), HashMap.class);

            //if the player hasn't killed the npc at all, return false
            if (!npcKills.containsKey(player.getName())) {
                return false;
            }

            //if the player hasn't killed the required amount of the npc, return false
            if (npcKills.get(player.getName()) < requiredNpcKills.get(npcUUID)) {
                return false;
            }
        }

        return true;
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

}
