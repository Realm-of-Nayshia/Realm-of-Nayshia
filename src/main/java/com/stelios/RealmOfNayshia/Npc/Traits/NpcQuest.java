package com.stelios.RealmOfNayshia.Npc.Traits;

import com.stelios.RealmOfNayshia.Main;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

import java.util.ArrayList;
import java.util.Arrays;

public class NpcQuest extends Trait {

    public NpcQuest() {
        super("NpcQuest");
        main = Main.getPlugin(Main.class);
    }


    Main main;


    //quest info
    @Persist("lockedText") ArrayList<String> lockedText =
            new ArrayList<>(Arrays.asList(
                    "Quest locked text 1",
                    "Quest locked text 2",
                    "Quest locked text 3"));
    @Persist("unlockedText")ArrayList<String> unlockedText =
            new ArrayList<>(Arrays.asList(
                    "Quest unlocked text 1",
                    "Quest unlocked text 2",
                    "Quest unlocked text 3"));
    @Persist("activeText") ArrayList<String> activeText =
            new ArrayList<>(Arrays.asList(
                    "Quest active text 1",
                    "Quest active text 2",
                    "Quest active text 3"));
    @Persist("completedText") ArrayList<String> completedText =
            new ArrayList<>(Arrays.asList(
                    "Quest completed text 1",
                    "Quest completed text 2",
                    "Quest completed text 3"));
    @Persist("questName") String questName;
    @Persist("isSpeaking") boolean isSpeaking = false;


    //getters
    public ArrayList<String> getLockedText() {
        return lockedText;
    }
    public ArrayList<String> getUnlockedText() {
        return unlockedText;
    }
    public ArrayList<String> getActiveText() {
        return activeText;
    }
    public ArrayList<String> getCompletedText() {
        return completedText;
    }
    public String getQuestName() {
        return questName;
    }
    public boolean isSpeaking() {
        return isSpeaking;
    }


    //setters
    public void setLockedText(ArrayList<String> lockedText) {
        this.lockedText = lockedText;
    }
    public void setUnlockedText(ArrayList<String> unlockedText) {
        this.unlockedText = unlockedText;
    }
    public void setActiveText(ArrayList<String> activeText) {
        this.activeText = activeText;
    }
    public void setCompletedText(ArrayList<String> completedText) {
        this.completedText = completedText;
    }
    public void setQuestName(String questName) {
        this.questName = questName;
    }
    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    //setters for size
    public void setLockedResponseSize(int size) {

        //get the current size
        int currentSize = lockedText.size();

        //if the current size is greater than the new size
        if (currentSize > size) {

            //remove the extra text
            lockedText.subList(size, currentSize).clear();

        //if the current size is less than the new size
        } else {

            //add the extra text
            for (int i = currentSize; i < size; i++) {
                lockedText.add("Quest locked text " + (i + 1));
            }
        }
    }

    public void setUnlockedResponseSize(int size) {

        //get the current size
        int currentSize = unlockedText.size();

        //if the current size is greater than the new size
        if (currentSize > size) {

            //remove the extra text
            unlockedText.subList(size, currentSize).clear();

        //if the current size is less than the new size
        } else {

            //add the extra text
            for (int i = currentSize; i < size; i++) {
                unlockedText.add("Quest unlocked text " + (i + 1));
            }
        }
    }

    public void setActiveResponseSize(int size) {

        //get the current size
        int currentSize = activeText.size();

        //if the current size is greater than the new size
        if (currentSize > size) {

            //remove the extra text
            activeText.subList(size, currentSize).clear();

        //if the current size is less than the new size
        } else {

            //add the extra text
            for (int i = currentSize; i < size; i++) {
                activeText.add("Quest active text " + (i + 1));
            }
        }
    }

    public void setCompletedResponseSize(int size) {

        //get the current size
        int currentSize = completedText.size();

        //if the current size is greater than the new size
        if (currentSize > size) {

            //remove the extra text
            completedText.subList(size, currentSize).clear();

        //if the current size is less than the new size
        } else {

            //add the extra text
            for (int i = currentSize; i < size; i++) {
                completedText.add("Quest completed text " + (i + 1));
            }
        }
    }
}
