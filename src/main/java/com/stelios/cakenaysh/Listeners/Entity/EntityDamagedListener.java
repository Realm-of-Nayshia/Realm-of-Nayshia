package com.stelios.cakenaysh.Listeners.Entity;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Managers.StatsManager;
import com.stelios.cakenaysh.Npc.Traits.NpcStats;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcmonkey.sentinel.SentinelTrait;

import java.util.Objects;
import java.util.logging.Level;

public class EntityDamagedListener implements Listener {

    private final Main main;
    private final StatsManager statsManager;

    public EntityDamagedListener(Main main, StatsManager statsManager){
        this.main = main;
        this.statsManager = statsManager;
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent e){

        //if the entity is a player and not a npc
        if (e.getEntity() instanceof Player && !CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {

            Player player = (Player) e.getEntity();
            CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

            //if the player took fall damage
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                customPlayer.addHealth((float) ((e.getDamage() * -5)));

            //if the player took damage from hunger
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.STARVATION)){
                customPlayer.addHealth((float) customPlayer.getMaxHealth() / -16);

            //if the player took damage from drowning
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)){
                customPlayer.addHealth((float) customPlayer.getMaxHealth() / -16);

            //if the player took damage from poison
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.POISON)){

                //get the level of poison and reduce the health
                int poisonLevel = player.getPotionEffect(PotionEffectType.POISON).getAmplifier() + 1;

                //if the poison would kill the player, set the health to 1
                if (customPlayer.getHealth() - (3*poisonLevel) <= 0) {
                    customPlayer.setHealth(1);
                } else {
                    customPlayer.addHealth(-3 * poisonLevel);
                }

            //if the player took damage from fire, and doesn't have fire resistance
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) && !player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)){
                customPlayer.addHealth((int) ((customPlayer.getMaxHealth() / -25) * (1 - customPlayer.getInfernalDefense())));

            //if the player took damage from fire tick, and doesn't have fire resistance
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) && !player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)){
                customPlayer.addHealth((int) ((customPlayer.getMaxHealth() / -40) * (1 - customPlayer.getInfernalDefense())));

            //if the player took damage from lava, and doesn't have fire resistance
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.LAVA) && !player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)){
                customPlayer.addHealth((int) ((customPlayer.getMaxHealth() / -12) * (1 - customPlayer.getInfernalDefense())));

            //if the player took damage from wither
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.WITHER)){

                //get the level of wither and reduce the health
                int witherLevel = player.getPotionEffect(PotionEffectType.WITHER).getAmplifier() + 1;
                customPlayer.addHealth(-3*witherLevel);

            //if the player took damage from the void
            }else if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                customPlayer.addHealth((float) customPlayer.getMaxHealth() /-10);

            }

            //negate the normal damage
            e.setDamage(0);

            //update the hearts and display the action bar
            statsManager.updateHearts(player);
            statsManager.displayActionBar(player);
        }
    }


    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        //if the defending entity is a npc
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {

            //if the defending entity is a sentinel with the NpcStats trait
            if (CitizensAPI.getNPCRegistry().getNPC(e.getEntity()).hasTrait(SentinelTrait.class) && CitizensAPI.getNPCRegistry().getNPC(e.getEntity()).hasTrait(NpcStats.class)) {

                //get the npc defender and their stats
                NPC defender = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
                NpcStats defenderNpcStats = defender.getOrAddTrait(NpcStats.class);

                float defenderDefense = defenderNpcStats.getDefense();
                float defenderInfernalDefense = defenderNpcStats.getInfernalDefense();
                float defenderUndeadDefense = defenderNpcStats.getUndeadDefense();
                float defenderAquaticDefense = defenderNpcStats.getAquaticDefense();
                float defenderAerialDefense = defenderNpcStats.getAerialDefense();
                float defenderMeleeDefense = defenderNpcStats.getMeleeDefense();
                float defenderRangedDefense = defenderNpcStats.getRangedDefense();
                float defenderMagicDefense = defenderNpcStats.getMagicDefense();

                //if the attacking entity is a NPC
                if (CitizensAPI.getNPCRegistry().isNPC(e.getDamager())) {

                    //if the attacking entity is a npc with the NpcStats trait
                    if (CitizensAPI.getNPCRegistry().getNPC(e.getDamager()).hasTrait(SentinelTrait.class) && CitizensAPI.getNPCRegistry().getNPC(e.getDamager()).hasTrait(NpcStats.class)) {

                        //get the npc attacker and their stats
                        NPC attacker = CitizensAPI.getNPCRegistry().getNPC(e.getDamager());
                        SentinelTrait attackerSentinelTrait = attacker.getOrAddTrait(SentinelTrait.class);
                        NpcStats attackerNpcStats = attacker.getOrAddTrait(NpcStats.class);

                        float attackerBaseDamage = (float) attackerSentinelTrait.getDamage();
                        float attackerCritChance = attackerNpcStats.getCritChance();
                        float attackerCritDamage = attackerNpcStats.getCritDamage();
                        float attackerStrength = attackerNpcStats.getStrength();
                        float attackerInfernalDamage = attackerNpcStats.getInfernalDamage();
                        float attackerUndeadDamage = attackerNpcStats.getUndeadDamage();
                        float attackerAquaticDamage = attackerNpcStats.getAquaticDamage();
                        float attackerAerialDamage = attackerNpcStats.getAerialDamage();
                        float attackerMeleeDamage = attackerNpcStats.getMeleeDamage();
                        float attackerRangedDamage = attackerNpcStats.getRangedDamage();
                        float attackerMagicDamage = attackerNpcStats.getMagicDamage();

                        ////calculate the damage
                        //crit chance calculations
                        if (e.isCritical()){
                            attackerCritChance += 50;
                        }
                        if (Math.random() * 100 < attackerCritChance){
                            attackerCritChance = 100;
                        }else{
                            attackerCritChance = 0;
                        }

                        //type damage calculations
                        float typeDamage = attackerBaseDamage + attackerInfernalDamage * (1-defenderInfernalDefense/100) + attackerUndeadDamage * (1-defenderUndeadDefense/100) +
                                attackerAquaticDamage * (1-defenderAquaticDefense/100) + attackerAerialDamage * (1-defenderAerialDefense/100) +
                                attackerMeleeDamage * (1-defenderMeleeDefense/100) + attackerRangedDamage * (1-defenderRangedDefense/100) +
                                attackerMagicDamage * (1-defenderMagicDefense/100);

                        //crit damage and strength calculations
                        boolean isCritical = false;
                        if (attackerCritChance == 100){
                            typeDamage = typeDamage * (1+attackerStrength/100) * (1+attackerCritDamage/100);
                            isCritical = true;
                        }else{
                            typeDamage = typeDamage * (1+attackerStrength/100);
                        }

                        //defense calculation
                        float finalDefenderDamage = typeDamage * (1-(defenderDefense/(defenderDefense+100)));

                        //if the damage is negative set it to zero
                        if (finalDefenderDamage < 0){
                            finalDefenderDamage = 0;
                        }

                        //apply the weapon's effects if it has any
                        if (attackerSentinelTrait.itemHelper.getHeldItem() != null) {
                            if (attackerSentinelTrait.itemHelper.getHeldItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)
                                    && Objects.requireNonNull(attackerSentinelTrait.itemHelper.getHeldItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)).equals("weapon")) {

                                //get the weapon's effects
                                PotionEffect[] potionEffects = attackerSentinelTrait.itemHelper.getHeldItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "potionEffects"), DataType.POTION_EFFECT_ARRAY);
                                LivingEntity defenderLivingEntity = (LivingEntity) defender.getEntity();

                                //apply the effects
                                for (PotionEffect potionEffect : Objects.requireNonNull(potionEffects)) {
                                    defenderLivingEntity.addPotionEffect(potionEffect);
                                }
                            }
                        }

                        //display the damage
                        statsManager.displayDamage(defender.getEntity(), (int) finalDefenderDamage, isCritical, defender.getStoredLocation());

                        //deal the damage
                        e.setDamage(finalDefenderDamage);


                    } else {
                        //error: attacking entity either isn't a sentinel or doesn't have NpcStats trait
                        main.getLogger().log(Level.WARNING, "ERROR: attacking entity either isn't a sentinel or doesn't have the NpcStats trait");
                    }

                //if the attacking entity is a player
                } else if (e.getDamager() instanceof Player){

                    //get the player attacker and their stats
                    Player player = (Player) e.getDamager();
                    CustomPlayer attackerPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());

                    //potion effects
                    float attackerStrengthPotionLevel = 0;
                    if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
                        attackerStrengthPotionLevel = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() + 1;
                        if (attackerStrengthPotionLevel > 20){
                            attackerStrengthPotionLevel = 20;
                        }
                    }

                    float attackerWeaknessPotionLevel = 0;
                    if (player.hasPotionEffect(PotionEffectType.WEAKNESS)){
                        attackerWeaknessPotionLevel = player.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier() + 1;
                        if (attackerWeaknessPotionLevel > 10){
                            attackerWeaknessPotionLevel = 10;
                        }
                    }

                    //stats
                    float attackerBaseDamage = attackerPlayer.getDamage() + attackerPlayer.getBonusDamage();
                    float attackerCritChance = attackerPlayer.getCritChance();
                    float attackerCritDamage = attackerPlayer.getCritDamage();
                    float attackerStrength = attackerPlayer.getStrength();
                    float attackerInfernalDamage = attackerPlayer.getInfernalDamage();
                    float attackerUndeadDamage = attackerPlayer.getUndeadDamage();
                    float attackerAquaticDamage = attackerPlayer.getAquaticDamage();
                    float attackerAerialDamage = attackerPlayer.getAerialDamage();
                    float attackerMeleeDamage = attackerPlayer.getMeleeDamage();
                    float attackerRangedDamage = attackerPlayer.getRangedDamage();
                    float attackerMagicDamage = attackerPlayer.getMagicDamage();

                    ////calculate the damage
                    //crit chance calculations
                    if (e.isCritical()){
                        attackerCritChance += 50;
                    }
                    if (Math.random() * 100 < attackerCritChance){
                        attackerCritChance = 100;
                    }else{
                        attackerCritChance = 0;
                    }

                    //type damage calculations
                    float typeDamage = attackerBaseDamage + attackerInfernalDamage * (1-defenderInfernalDefense/100) + attackerUndeadDamage * (1-defenderUndeadDefense/100) +
                            attackerAquaticDamage * (1-defenderAquaticDefense/100) + attackerAerialDamage * (1-defenderAerialDefense/100) +
                            attackerMeleeDamage * (1-defenderMeleeDefense/100) + attackerRangedDamage * (1-defenderRangedDefense/100) +
                            attackerMagicDamage * (1-defenderMagicDefense/100);

                    //crit damage and strength calculations
                    boolean isCritical = false;
                    if (attackerCritChance == 100){
                        typeDamage = typeDamage * (1+attackerStrength/100) * (1+attackerCritDamage/100);
                        isCritical = true;
                    }else{
                        typeDamage = typeDamage * (1+attackerStrength/100);
                    }

                    //cooldown debuff calculations
                    float cooldownDebuff;
                    if (player.getAttackCooldown() < 0.8){
                        cooldownDebuff = player.getAttackCooldown()/1.5f;
                    }else{
                        cooldownDebuff = player.getAttackCooldown();
                    }

                    //defense calculation
                    float finalDefenderDamage = typeDamage * (1-(defenderDefense/(defenderDefense+100)));

                    //potion effects
                    finalDefenderDamage = finalDefenderDamage * (1 + ((attackerStrengthPotionLevel/10) + (-attackerWeaknessPotionLevel/10)));

                    //cooldown debuff
                    finalDefenderDamage = finalDefenderDamage * cooldownDebuff;

                    //if the damage is negative set it to zero
                    if (finalDefenderDamage <= 0){
                        finalDefenderDamage = 0;

                    //if the damage is greater than 0, add the damage to be tracked on the npc trait
                    } else {

                        LivingEntity livingEntity = (LivingEntity) e.getEntity();
                        //if the damage will kill the npc, set the damage to the npc's health
                        if (finalDefenderDamage >= livingEntity.getHealth()){
                            defenderNpcStats.addPlayerDamage(player.getUniqueId(), (float) livingEntity.getHealth());
                        } else {
                            defenderNpcStats.addPlayerDamage(player.getUniqueId(), finalDefenderDamage);
                        }
                    }

                    //apply the weapon's effects if it has any
                    if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)
                                && Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)).equals("weapon")) {

                            //get the weapon's effects
                            PotionEffect[] potionEffects = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "potionEffects"), DataType.POTION_EFFECT_ARRAY);
                            LivingEntity defenderLivingEntity = (LivingEntity) defender.getEntity();

                            //apply the effects
                            for (PotionEffect potionEffect : Objects.requireNonNull(potionEffects)) {
                                defenderLivingEntity.addPotionEffect(potionEffect);
                            }
                        }
                    }

                    //put the player in combat
                    main.getCombatManager().addCombatTimer(player.getUniqueId());

                    //display the damage
                    statsManager.displayDamage(player, (int) finalDefenderDamage, isCritical, defender.getStoredLocation());

                    //deal the damage
                    e.setDamage(finalDefenderDamage);
                }

            }
            //else do nothing: defending entity either isn't a sentinel or doesn't have NpcStats trait



        //if the defending entity is a player
        } else if (e.getEntity() instanceof Player) {

            //set the default damage to zero
            e.setDamage(0.01);

            //get the player defender and their stats
            Player playerDefend = (Player) e.getEntity();
            CustomPlayer defenderPlayer = main.getPlayerManager().getCustomPlayer(playerDefend.getUniqueId());

            //potion effects
            float defenderResistancePotionLevel = 0;
            if (playerDefend.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                defenderResistancePotionLevel = playerDefend.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1;
                if (defenderResistancePotionLevel > 10) {
                    defenderResistancePotionLevel = 10;
                }
            }

            //stats
            float defenderDefense = defenderPlayer.getDefense();
            float defenderInfernalDefense = defenderPlayer.getInfernalDefense();
            float defenderUndeadDefense = defenderPlayer.getUndeadDefense();
            float defenderAquaticDefense = defenderPlayer.getAquaticDefense();
            float defenderAerialDefense = defenderPlayer.getAerialDefense();
            float defenderMeleeDefense = defenderPlayer.getMeleeDefense();
            float defenderRangedDefense = defenderPlayer.getRangedDefense();
            float defenderMagicDefense = defenderPlayer.getMagicDefense();

            //if the attacking entity is a NPC
            if (CitizensAPI.getNPCRegistry().isNPC(e.getDamager())) {

                //if the attacking entity is a npc with the NpcStats trait
                if (CitizensAPI.getNPCRegistry().getNPC(e.getDamager()).hasTrait(SentinelTrait.class) && CitizensAPI.getNPCRegistry().getNPC(e.getDamager()).hasTrait(NpcStats.class)) {

                    //get the npc attacker and their stats
                    NPC attacker = CitizensAPI.getNPCRegistry().getNPC(e.getDamager());
                    SentinelTrait attackerSentinelTrait = attacker.getOrAddTrait(SentinelTrait.class);
                    NpcStats attackerNpcStats = attacker.getOrAddTrait(NpcStats.class);

                    float attackerBaseDamage = (float) attackerSentinelTrait.getDamage();
                    float attackerCritChance = attackerNpcStats.getCritChance();
                    float attackerCritDamage = attackerNpcStats.getCritDamage();
                    float attackerStrength = attackerNpcStats.getStrength();
                    float attackerInfernalDamage = attackerNpcStats.getInfernalDamage();
                    float attackerUndeadDamage = attackerNpcStats.getUndeadDamage();
                    float attackerAquaticDamage = attackerNpcStats.getAquaticDamage();
                    float attackerAerialDamage = attackerNpcStats.getAerialDamage();
                    float attackerMeleeDamage = attackerNpcStats.getMeleeDamage();
                    float attackerRangedDamage = attackerNpcStats.getRangedDamage();
                    float attackerMagicDamage = attackerNpcStats.getMagicDamage();

                    ////calculate the damage
                    //crit chance calculations
                    if (e.isCritical()){
                        attackerCritChance += 50;
                    }
                    if (Math.random() * 100 < attackerCritChance){
                        attackerCritChance = 100;
                    }else{
                        attackerCritChance = 0;
                    }

                    //type damage calculations
                    float typeDamage = attackerBaseDamage + attackerInfernalDamage * (1-defenderInfernalDefense/100) + attackerUndeadDamage * (1-defenderUndeadDefense/100) +
                            attackerAquaticDamage * (1-defenderAquaticDefense/100) + attackerAerialDamage * (1-defenderAerialDefense/100) +
                            attackerMeleeDamage * (1-defenderMeleeDefense/100) + attackerRangedDamage * (1-defenderRangedDefense/100) +
                            attackerMagicDamage * (1-defenderMagicDefense/100);

                    //crit damage and strength calculations
                    boolean isCritical = false;
                    if (attackerCritChance == 100){
                        typeDamage = typeDamage * (1+attackerStrength/100) * (1+attackerCritDamage/100);
                        isCritical = true;
                    }else{
                        typeDamage = typeDamage * (1+attackerStrength/100);
                    }

                    //defense calculation
                    float finalDefenderDamage = typeDamage * (1-(defenderDefense/(defenderDefense+100)));

                    //potion effects
                    finalDefenderDamage = finalDefenderDamage * (1-defenderResistancePotionLevel/10);

                    //if the damage is negative set it to zero
                    if (finalDefenderDamage < 0){
                        finalDefenderDamage = 0;
                    }

                    //apply the weapon's effects if it has any
                    if (attackerSentinelTrait.itemHelper.getHeldItem() != null) {
                        if (attackerSentinelTrait.itemHelper.getHeldItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)
                                && Objects.requireNonNull(attackerSentinelTrait.itemHelper.getHeldItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)).equals("weapon")) {

                            //get the weapon's effects
                            PotionEffect[] potionEffects = attackerSentinelTrait.itemHelper.getHeldItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "potionEffects"), DataType.POTION_EFFECT_ARRAY);

                            //apply the effects
                            for (PotionEffect potionEffect : Objects.requireNonNull(potionEffects)) {
                                playerDefend.addPotionEffect(potionEffect);
                            }
                        }
                    }

                    //put the player in combat
                    main.getCombatManager().addCombatTimer(playerDefend.getUniqueId());

                    //display the damage
                    statsManager.displayDamage(playerDefend, (int) finalDefenderDamage, isCritical, playerDefend.getLocation());

                    //deal the damage
                    defenderPlayer.setHealth((int) (defenderPlayer.getHealth() - finalDefenderDamage));

                    //update the player's health bar
                    statsManager.displayActionBar(playerDefend);
                    statsManager.updateHearts(playerDefend);

                } else {
                    //error: attacking entity either isn't a sentinel or doesn't have NpcStats trait
                    main.getLogger().log(Level.WARNING, "ERROR: attacking entity either isn't a sentinel or doesn't have the NpcStats trait");
                }

            //if the attacking entity is a player
            } else if (e.getDamager() instanceof Player){

                //get the player attacker and their stats
                Player playerAttack = (Player) e.getDamager();
                CustomPlayer attackerPlayer = main.getPlayerManager().getCustomPlayer(playerAttack.getUniqueId());

                //potion effects
                float attackerStrengthPotionLevel = 0;
                if (playerAttack.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
                    attackerStrengthPotionLevel = playerAttack.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() + 1;
                    if (attackerStrengthPotionLevel > 20){
                        attackerStrengthPotionLevel = 20;
                    }
                }

                float attackerWeaknessPotionLevel = 0;
                if (playerAttack.hasPotionEffect(PotionEffectType.WEAKNESS)){
                    attackerWeaknessPotionLevel = playerAttack.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier() + 1;
                    if (attackerWeaknessPotionLevel > 10){
                        attackerWeaknessPotionLevel = 10;
                    }
                }

                //stats
                float attackerBaseDamage = attackerPlayer.getDamage() + attackerPlayer.getBonusDamage();
                float attackerCritChance = attackerPlayer.getCritChance();
                float attackerCritDamage = attackerPlayer.getCritDamage();
                float attackerStrength = attackerPlayer.getStrength();
                float attackerInfernalDamage = attackerPlayer.getInfernalDamage();
                float attackerUndeadDamage = attackerPlayer.getUndeadDamage();
                float attackerAquaticDamage = attackerPlayer.getAquaticDamage();
                float attackerAerialDamage = attackerPlayer.getAerialDamage();
                float attackerMeleeDamage = attackerPlayer.getMeleeDamage();
                float attackerRangedDamage = attackerPlayer.getRangedDamage();
                float attackerMagicDamage = attackerPlayer.getMagicDamage();


                ////calculate the damage
                //crit chance calculations
                if (e.isCritical()){
                    attackerCritChance += 50;
                }
                if (Math.random() * 100 < attackerCritChance){
                    attackerCritChance = 100;
                }else{
                    attackerCritChance = 0;
                }

                //type damage calculations
                float typeDamage = attackerBaseDamage + attackerInfernalDamage * (1-defenderInfernalDefense/100) + attackerUndeadDamage * (1-defenderUndeadDefense/100) +
                        attackerAquaticDamage * (1-defenderAquaticDefense/100) + attackerAerialDamage * (1-defenderAerialDefense/100) +
                        attackerMeleeDamage * (1-defenderMeleeDefense/100) + attackerRangedDamage * (1-defenderRangedDefense/100) +
                        attackerMagicDamage * (1-defenderMagicDefense/100);

                //crit damage and strength calculations
                boolean isCritical = false;
                if (attackerCritChance == 100){
                    typeDamage = typeDamage * (1+attackerStrength/100) * (1+attackerCritDamage/100);
                    isCritical = true;
                }else{
                    typeDamage = typeDamage * (1+attackerStrength/100);
                }

                //cooldown debuff calculations
                float cooldownDebuff;
                if (playerAttack.getAttackCooldown() < 0.8){
                    cooldownDebuff = playerAttack.getAttackCooldown()/1.5f;
                }else{
                    cooldownDebuff = playerAttack.getAttackCooldown();
                }

                //defense calculation
                float finalDefenderDamage = typeDamage * (1-(defenderDefense/(defenderDefense+100)));

                //potions
                finalDefenderDamage = finalDefenderDamage * (1 + ((attackerStrengthPotionLevel/10) + (-attackerWeaknessPotionLevel/10)));
                finalDefenderDamage = finalDefenderDamage * (1 - (defenderResistancePotionLevel/10));

                //cooldown debuff
                finalDefenderDamage = finalDefenderDamage * cooldownDebuff;

                //if the damage is negative set it to zero
                if (finalDefenderDamage < 0){
                    finalDefenderDamage = 0;
                }

                //apply the weapon's effects if it has any
                if (playerAttack.getInventory().getItemInMainHand().getItemMeta() != null) {
                    if (playerAttack.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)
                            && Objects.requireNonNull(playerAttack.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING)).equals("weapon")) {

                        //get the weapon's effects
                        PotionEffect[] potionEffects = playerAttack.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "potionEffects"), DataType.POTION_EFFECT_ARRAY);

                        //apply the effects
                        for (PotionEffect potionEffect : Objects.requireNonNull(potionEffects)) {
                            playerDefend.addPotionEffect(potionEffect);
                        }
                    }
                }

                //put the players in combat
                main.getCombatManager().addCombatTimer(playerDefend.getUniqueId());
                main.getCombatManager().addCombatTimer(playerAttack.getUniqueId());

                //display the damage
                statsManager.displayDamage(playerDefend, (int) finalDefenderDamage, isCritical, playerDefend.getLocation());

                //deal the damage
                defenderPlayer.setHealth((int) (defenderPlayer.getHealth() - finalDefenderDamage));
                playerDefend.sendMessage("You took " + finalDefenderDamage + " damage from " + playerAttack.getName());
                playerAttack.sendMessage("You dealt " + finalDefenderDamage + " damage to " + playerDefend.getName());

                //update the player's health bar
                statsManager.displayActionBar(playerDefend);
                statsManager.updateHearts(playerDefend);
            }
        }
    }

}
