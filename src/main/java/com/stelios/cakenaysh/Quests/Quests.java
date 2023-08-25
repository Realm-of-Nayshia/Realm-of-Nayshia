package com.stelios.cakenaysh.Quests;

import com.stelios.cakenaysh.Items.CustomItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public enum Quests {

    TEST_RETRIEVAL_QUEST(new Quest("Test_Retrieval_Quest", "We testing quests out to see if they work.",
            -1L, 11, 11,
            new QuestRewards(100, new ArrayList<>(Arrays.asList(CustomItems.FIERCE_AZARIAHS_EDGE.getItem())),
            new HashMap<>(){{
                put("maxHealth", 10);
                put("maxStamina", 1);
                put("stealth", 1);
                put("wilsonCoin", 100);
            }}),
            new QuestRequirements(new ArrayList<>(),false, new HashMap<>(), new ArrayList<>(), new HashMap<>()),
            new QuestRequirements(new ArrayList<>(Arrays.asList(CustomItems.WRATH_OF_SPARTA.getItem())),
                    false, new HashMap<>(), new ArrayList<>(), new HashMap<>())))

    ;


    private final Quest quest;

    Quests(Quest quest) {
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }


    //get all quests
    public static ArrayList<String> getQuestConstants() {
        ArrayList<String> quests = new ArrayList<>();
        for (Quests quest : Quests.values()) {
            quests.add(quest.name());
        }
        return quests;
    }
}
