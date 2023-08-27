package com.stelios.cakenaysh.Quests;

import com.stelios.cakenaysh.Items.CustomItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public enum Quests {

    TEST_RETRIEVAL_QUEST(new Quest("Test_Retrieval_Quest",
            new TextComponent[]{Component.text("|------------------|\n\n\n\n\n\n")
                    .append(Component.text(" Test Retrieval Quest\n"))
                    .append(Component.text("   by "))
                    .append(Component.text("test quest guy", TextColor.color(255,0,0)))
                    .append(Component.text("\n\n\n\n\n\n|------------------|"))
                    , Component.text("|------------------|")
                    .append(Component.text("We do a little testing with the book formatting stuff.\n\n"))
                    .append(Component.text("Hopefully this turns out good idk.\n\n"))
                    .append(Component.text("I guess manually making these isn't as bad as I thought...\n\n"))
                    .append(Component.text("hello\n", TextColor.color(255,0,0), TextDecoration.BOLD, TextDecoration.OBFUSCATED))
                    .append(Component.text("|------------------|"))}
            , -1L, 11, 11,
            new QuestRewards(100, new ArrayList<>(Collections.singletonList(CustomItems.FIERCE_AZARIAHS_EDGE.getItem())),
                    new HashMap<>() {{
                        put("maxHealth", 10);
                        put("maxStamina", 1);
                        put("stealth", 1);
                        put("wilsonCoin", 100);
                    }}),
            new QuestRequirements(new ArrayList<>(), false, new HashMap<>(), new ArrayList<>(), new HashMap<>()),
            new QuestRequirements(new ArrayList<>(Collections.singletonList(CustomItems.WRATH_OF_SPARTA.getItem())),
                    false, new HashMap<>(), new ArrayList<>(),
                    new HashMap<>() {{
                            put("047840ab-94f7-4003-985d-24dfa7ce5ea8", 3);
                    }})))

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
