package com.stelios.RealmOfNayshia.Listeners.Entity;

import com.stelios.RealmOfNayshia.Main;
import com.stelios.RealmOfNayshia.MenuCreation.Menu;
import com.stelios.RealmOfNayshia.MenuCreation.Menus.QuestAcceptMenu;
import com.stelios.RealmOfNayshia.Npc.Traits.NpcQuest;
import com.stelios.RealmOfNayshia.Quests.Quest;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class NpcClickedListener implements Listener {

    Main main;

    public NpcClickedListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onNpcLeftClicked (NPCLeftClickEvent e) {

        //if the npc has the NpcQuest trait run the quest logic
        if (e.getNPC().hasTrait(NpcQuest.class)) {
            questLogic(e.getClicker(), e.getNPC());
        }


    }

    @EventHandler
    public void onNpcRightClicked (NPCRightClickEvent e) {

        //if the npc has the NpcQuest trait run the quest logic
        if (e.getNPC().hasTrait(NpcQuest.class)) {
            questLogic(e.getClicker(), e.getNPC());
        }


    }


    private void questLogic(Player player, NPC npc) {

        //get the npc names
        String fullNpcName = npc.getFullName();
        String npcName = "[" + npc.getName() + "]: ";

        //take the color codes from the full name and add them to the start of the npc name
        for (int i = 0; i < fullNpcName.length(); i++) {
            if (fullNpcName.charAt(i) == '§') {
                npcName = ("§" + fullNpcName.charAt(i + 1)).concat(npcName);
            }
        }

        String finalNpcName = npcName.concat("§r");

        //update the player's quest status
        main.getQuestManager().updateQuestStatus(player);

        //get the trait
        NpcQuest trait = npc.getOrAddTrait(NpcQuest.class);


        //if the npc has no quest linked to it or if the npc is speaking
        if (trait.getQuestName() == null || trait.isSpeaking()){
            return;
        }

        //get the quest
        Quest quest = main.getQuestManager().getQuestFromName(trait.getQuestName());

        //if the quest doesn't exist, return
        if (quest == null) {
            return;
        }

        //if the npc is the quest completer and the player can complete the quest
        if (quest.getQuestCompleterId() == npc.getId() && main.getQuestManager().canCompleteQuest(player, quest)) {

            //set the npc to speaking
            trait.setSpeaking(true);

            //send the npc's complete text with a delay between each message
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {

                    //complete the quest
                    main.getQuestManager().completeQuest(player, quest);

                    //send the npc's complete text
                    player.sendMessage(finalNpcName.concat(trait.getCompletedText().get(i)));
                    i++;
                    if (i >= trait.getCompletedText().size()) {
                        trait.setSpeaking(false);
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 15);

        //if the npc is the quest giver and the quest is not completed
        } else if (quest.getQuestGiverId() == npc.getId() && !main.getQuestManager().hasQuestCompleted(player, quest)) {

            //if the player doesn't have the quest active or completed
            if (!main.getQuestManager().hasQuestActive(player, quest)) {

                //if the player can't accept the quest
                if (!main.getQuestManager().canAcceptQuest(player, quest, false)) {

                    //set the npc to speaking
                    trait.setSpeaking(true);

                    //send the npc's locked text with a delay between each message
                    new BukkitRunnable() {
                        int i = 0;
                        @Override
                        public void run() {

                            player.sendMessage(finalNpcName.concat(trait.getLockedText().get(i)));
                            i++;
                            if (i >= trait.getLockedText().size()) {
                                trait.setSpeaking(false);
                                cancel();
                            }
                        }
                    }.runTaskTimer(main, 0, 15);

                //if the player can't accept the quest due to being at the max amount of quests
                } else if (!main.getQuestManager().canAcceptQuest(player, quest, true)) {

                    //send that the player is at max quests
                    player.sendMessage(Component.text(finalNpcName + "I need some help, but you seem to already have your hands full."));

                //if the player can accept the quest
                } else if (main.getQuestManager().canAcceptQuest(player, quest, true)) {

                    //set the npc to speaking
                    trait.setSpeaking(true);

                    //send the npc's unlocked text with a delay between each message
                    new BukkitRunnable() {
                        int i = 0;
                        @Override
                        public void run() {

                            player.sendMessage(finalNpcName.concat(trait.getUnlockedText().get(i)));
                            i++;
                            if (i >= trait.getUnlockedText().size()) {

                                //open the confirmation menu
                                Menu confirmationMenu = new QuestAcceptMenu(quest);
                                confirmationMenu.open(player);
                                trait.setSpeaking(false);
                                cancel();
                            }
                        }
                    }.runTaskTimer(main, 0, 15);
                }

            //if the player already has the quest active
            } else {

                //set the npc to speaking
                trait.setSpeaking(true);

                //send the npc's active text with a delay between each message
                new BukkitRunnable() {
                    int i = 0;
                    @Override
                    public void run() {

                        player.sendMessage(finalNpcName + trait.getActiveText().get(i));
                        i++;
                        if (i >= trait.getActiveText().size()) {
                            trait.setSpeaking(false);
                            cancel();
                        }
                    }
                }.runTaskTimer(main, 0, 15);
            }

        //if the quest is already completed
        } else {

            //set the npc to speaking
            trait.setSpeaking(true);

            //send the npc's completed text with a delay between each message
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {

                    player.sendMessage(finalNpcName + trait.getCompletedText().get(i));
                    i++;
                    if (i >= trait.getCompletedText().size()) {
                        trait.setSpeaking(false);
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 15);
        }
    }
}
