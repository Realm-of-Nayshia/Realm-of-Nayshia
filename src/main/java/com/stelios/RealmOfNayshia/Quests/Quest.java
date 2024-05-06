package com.stelios.RealmOfNayshia.Quests;

import net.kyori.adventure.text.TextComponent;

public class Quest {

    private final String name;
    private final TextComponent[] bookDescription;
    private final long cooldown;
    private final int questGiverId;
    private final int questCompleterId;
    private final QuestRewards questRewards;
    private final QuestRequirements questAcceptRequirements;
    private final QuestRequirements questCompletionRequirements;


    //@param name: the name of the quest
    //@param cooldown: the cooldown for the quest
    //@param questGiver: the npc that gives the quest
    //@param questCompleter: the npc that completes the quest
    //@param questRewards: the rewards for completing the quest
    //@param questAcceptRequirements: the requirements to accept a quest
    //@param questCompletionRequirements: the requirements to complete a quest
    public Quest (String name, TextComponent[] bookDescription, long cooldown, int questGiverId, int questCompleterId,
                  QuestRewards questRewards, QuestRequirements questAcceptRequirements, QuestRequirements questCompletionRequirements){
        this.name = name;
        this.bookDescription = bookDescription;
        this.cooldown = cooldown;
        this.questGiverId = questGiverId;
        this.questCompleterId = questCompleterId;
        this.questRewards = questRewards;
        this.questAcceptRequirements = questAcceptRequirements;
        this.questCompletionRequirements = questCompletionRequirements;
    }

    //getters
    public String getName() {
        return name;
    }
    public TextComponent[] getBookDescription() {
        return bookDescription;
    }
    public double getCooldown() {
        return cooldown;
    }
    public int getQuestGiverId() {
        return questGiverId;
    }
    public int getQuestCompleterId() {
        return questCompleterId;
    }
    public QuestRewards getQuestRewards() {
        return questRewards;
    }
    public QuestRequirements getQuestAcceptRequirements() {
        return questAcceptRequirements;
    }
    public QuestRequirements getQuestCompletionRequirements() {
        return questCompletionRequirements;
    }
}
