package com.stelios.cakenaysh.Listeners.Entity;

import com.stelios.cakenaysh.Events.XpChangedEvent;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Managers.StashManager;
import com.stelios.cakenaysh.Npc.Traits.NpcStats;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.mcmonkey.sentinel.SentinelTrait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SentinelStatusChangeListener implements Listener {

    Main main;

    public SentinelStatusChangeListener(Main main){
        this.main = main;
    }


    @EventHandler
    public void onPlayerSpawn(NPCSpawnEvent e){

        //if the npc has the sentinel and npc stats traits
        if(CitizensAPI.getNPCRegistry().getNPC(e.getNPC().getEntity()).hasTrait(SentinelTrait.class) && CitizensAPI.getNPCRegistry().getNPC(e.getNPC().getEntity()).hasTrait(NpcStats.class)) {

            //set the maximum no damage ticks to 1 so the npc can be damaged in rapid succession
            LivingEntity livingEntity = (LivingEntity) e.getNPC().getEntity();
            livingEntity.setMaximumNoDamageTicks(1);

        }
    }



    @EventHandler
    public void onPlayerDeath(EntityDeathEvent e){

        //if the player is a npc with the sentinel and npc stats trait
        if(CitizensAPI.getNPCRegistry().isNPC(e.getEntity()) && CitizensAPI.getNPCRegistry().getNPC(e.getEntity()).hasTrait(SentinelTrait.class) && CitizensAPI.getNPCRegistry().getNPC(e.getEntity()).hasTrait(NpcStats.class)){

            //get the important information about the dead npc
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
            NpcStats npcStats = npc.getOrAddTrait(NpcStats.class);
            SentinelTrait sentinelTrait = npc.getOrAddTrait(SentinelTrait.class);
            HashMap<UUID, Float> playerDamages = npcStats.getPlayerDamages();

            float totalDamage = 0;
            for (Float damage : playerDamages.values()) {
                totalDamage += damage;
            }

            ArrayList<ItemStack> drops = new ArrayList<>(sentinelTrait.drops);
            ArrayList<Double> dropChances = new ArrayList<>(sentinelTrait.dropChances);


            //if the killer is a player and not a npc
            if (e.getEntity().getKiller() instanceof Player && !CitizensAPI.getNPCRegistry().isNPC(e.getEntity().getKiller())) {

                //if there is one drop chance
                if (dropChances.size() == 1) {

                    //add the same drop chance for each drop
                    for (int i = 1; i < drops.size(); i++) {
                        dropChances.add(dropChances.get(0));
                    }
                }

                //loop through the players that dealt damage to the npc
                for (UUID uuid : playerDamages.keySet()) {

                    Player player = Bukkit.getPlayer(uuid);

                    //if the player is offline continue
                    if (player == null) continue;

                    ////give xp to each player based on how much damage they dealt to the npc
                    //calculate the xp the player will get
                    int xp = (int) (playerDamages.get(uuid) / totalDamage * npcStats.getXp());

                    //if the xp is greater than the npc's xp set it to the npc's xp
                    if (xp > npcStats.getXp()) {
                        xp = (int) npcStats.getXp();
                    }

                    //add xp to the player
                    main.getPlayerManager().getCustomPlayer(uuid).addXp(xp);
                    player.sendMessage(Component.text("+" + xp + "XP", TextColor.color(0, 255, 0)));

                    //call the xp changed event
                    Bukkit.getPluginManager().callEvent(new XpChangedEvent(player, xp, npc));


                    ////give the player their earned drops
                    //loop through the dropped items
                    for (ItemStack item : drops) {

                        //calculate the chance of the drop based on how much damage the player dealt to the npc
                        double chance = dropChances.get(0) * (playerDamages.get(uuid) / totalDamage);

                        player.sendMessage("drop chance: " + chance);

                        //if the chance is greater than a random number between 0 and 1 give the player the drop
                        if (chance > Math.random()) {

                            ItemMeta itemMeta = item.getItemMeta();
                            PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

                            //if the item is unstackable
                            if (Boolean.TRUE.equals(itemData.get(new NamespacedKey(main, "unstackable"), PersistentDataType.BOOLEAN))) {

                                //randomize the item's unique id
                                itemData.set(new NamespacedKey(main, "uniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());
                                item.setItemMeta(itemMeta);
                            }

                            //if the player's inventory isn't full add the item to their inventory
                            if (player.getInventory().firstEmpty() != -1) {
                                player.getInventory().addItem(item);

                            //add the item to the player's stash
                            } else {
                                StashManager stashManager = main.getStashManager();
                                stashManager.addItemToStash(player, item);
                            }
                        }
                    }
                }
            }
            //clear the regularly dropped items
            e.getDrops().clear();

            //clear the players and their damages to the npc
            npcStats.clearPlayerDamages();
        }
    }
}