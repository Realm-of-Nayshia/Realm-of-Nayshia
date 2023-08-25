package com.stelios.cakenaysh.Npc.Traits;

import com.stelios.cakenaysh.Main;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;

public class NpcQuest extends Trait {

    public NpcQuest() {
        super("NpcQuest");
        main = Main.getPlugin(Main.class);
    }


    Main main;


    //quest info
    @Persist("lockedText") ArrayList<TextComponent> lockedText = new ArrayList<>(Arrays.asList(
            Component.text("Quest locked text 1"),
            Component.text("Quest locked text 2"),
            Component.text("Quest locked text 3")));
    @Persist("unlockedText") ArrayList<TextComponent> unlockedText = new ArrayList<>(Arrays.asList(
            Component.text("Quest unlocked text 1"),
            Component.text("Quest unlocked text 2"),
            Component.text("Quest unlocked text 3")));
    @Persist("activeText") ArrayList<TextComponent> activeText = new ArrayList<>(Arrays.asList(
            Component.text("Quest active text 1"),
            Component.text("Quest active text 2"),
            Component.text("Quest active text 3")));
    @Persist("completedText") ArrayList<TextComponent> completedText = new ArrayList<>(Arrays.asList(
            Component.text("Quest completed text 1"),
            Component.text("Quest completed text 2"),
            Component.text("Quest completed text 3")));
    @Persist("quest") String questName;


    //getters
    public ArrayList<TextComponent> getLockedText() {
        return lockedText;
    }
    public ArrayList<TextComponent> getUnlockedText() {
        return unlockedText;
    }
    public ArrayList<TextComponent> getActiveText() {
        return activeText;
    }
    public ArrayList<TextComponent> getCompletedText() {
        return completedText;
    }
    public String getQuestName() {
        return questName;
    }


    //setters
    public void setLockedText(ArrayList<TextComponent> lockedText) {
        this.lockedText = lockedText;
    }
    public void setUnlockedText(ArrayList<TextComponent> unlockedText) {
        this.unlockedText = unlockedText;
    }
    public void setActiveText(ArrayList<TextComponent> activeText) {
        this.activeText = activeText;
    }
    public void setCompletedText(ArrayList<TextComponent> completedText) {
        this.completedText = completedText;
    }
    public void setQuestName(String questName) {
        this.questName = questName;
    }


    //reset all text
    public void resetText(){
        lockedText = new ArrayList<>();
        unlockedText = new ArrayList<>();
        activeText = new ArrayList<>();
        completedText = new ArrayList<>();
        questName = "";
    }

}
