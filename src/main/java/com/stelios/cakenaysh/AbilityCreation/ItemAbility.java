package com.stelios.cakenaysh.AbilityCreation;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import com.stelios.cakenaysh.Main;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class ItemAbility implements Listener {

    private final CustomAbilities ability;
    private final Item item;
    private final int stamina;
    private final Cache<UUID, Long> cooldown;
    private final long cooldownTime;
    private static boolean firstEventRegistered = false;

    public ItemAbility(CustomAbilities ability, Item item, int stamina, long cooldown) {

        //register the first event
        if (!firstEventRegistered) {
            Bukkit.getPluginManager().registerEvents(this, Main.getPlugin(Main.class));
            firstEventRegistered = true;

        //unregister the previous event and register the new one
        } else {
            unregister();
            Bukkit.getPluginManager().registerEvents(this, Main.getPlugin(Main.class));
        }

        this.ability = ability;
        this.item = item;
        this.stamina = stamina;
        this.cooldownTime = cooldown;
        this.cooldown = CacheBuilder.newBuilder().expireAfterWrite(cooldown, TimeUnit.SECONDS).build();
    }

    //getters
    public CustomAbilities getAbility() {
        return ability;
    }

    public ItemStack getItem() {
        return item.getItemStack();
    }

    public Boolean hasStaminaCost() {
        return stamina > 0;
    }

    public int getStamina() {
        return stamina;
    }

    public Cache<UUID, Long> getCooldown() {
        return cooldown;
    }

    public Boolean hasCooldown() {
        return cooldownTime > 0;
    }

    //abstract method for executing the ability
    public abstract void doAbility(Player player);

    //executing the ability
    public void executeAbility(Player player) {
        if (canUseAbility(player)) {
            doAbility(player);
        }
    }

    //unregister all events
    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    //can use ability both stam and cooldown
    public boolean canUseAbility(Player player) {

        //get the main class
        Main main = Main.getPlugin(Main.class);

        //check if the player doesn't meet the proficiency requirements
        try {
            CustomPlayer customPlayer = main.getPlayerManager().getCustomPlayer(player.getUniqueId());
            PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

            //if the item is a battle item
            if (itemData.get(new NamespacedKey(main, "itemType"), PersistentDataType.STRING) == ("weapon")) {

                //if the player doesn't meet the item requirements, return false
                if (itemData.get(new NamespacedKey(main, "meleeProficiency"), PersistentDataType.INTEGER) > customPlayer.getMeleeProficiency() ||
                        itemData.get(new NamespacedKey(main, "rangedProficiency"), PersistentDataType.INTEGER) > customPlayer.getRangedProficiency() ||
                        itemData.get(new NamespacedKey(main, "armorProficiency"), PersistentDataType.INTEGER) > customPlayer.getArmorProficiency()) {
                    player.sendMessage(Component.text("You do not meet the requirements to use this item.", TextColor.color(255, 0, 0)));
                    return false;
                }
            }

        //if the item is not a regular or battle item do nothing
        }catch(NullPointerException ex){
            //do nothing
        }

        //if the stamina is below the requirement
        if (hasStaminaCost() && main.getPlayerManager().getCustomPlayer(player.getUniqueId()).getStamina() < this.stamina){

            //send the player a message and play an error sound
            player.sendMessage(Component.text("You do not have enough stamina!", TextColor.color(255,0,0)));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1, 1);
            player.sendActionBar(Component.text("NOT ENOUGH STAMINA!", TextColor.color(210,125,45)));
            return false;
        }

        if (hasCooldown() && cooldown.asMap().containsKey(player.getUniqueId())){

            //get the time left on the cooldown and send it to the player
            long distance = cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis();
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1, 1);
            player.sendMessage(Component.text("You cannot use this ability for " + TimeUnit.MILLISECONDS.toSeconds(distance) + " more seconds!", TextColor.color(255,0,0)));
            player.sendActionBar(Component.text("ON COOLDOWN!", TextColor.color(255,0,0)));
            return false;
        }

        if (hasStaminaCost()){
            //remove the stamina and set the action bar
            main.getPlayerManager().getCustomPlayer(player.getUniqueId()).setStamina(main.getPlayerManager().getCustomPlayer(player.getUniqueId()).getStamina() - this.stamina);
            player.sendActionBar(Component.text("-" + stamina + " ⚡", TextColor.color(210,125,45)));
        }

        if (hasCooldown()) {
            //add the player to the cooldown cache
            cooldown.put(player.getUniqueId(), System.currentTimeMillis() + this.cooldownTime * 1000);
        }

        if (ability.getRemoveItem()){
            //remove the item from the player's inventory
            if (player.getInventory().getItemInMainHand().getAmount() > 1)
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            else if (player.getInventory().getItemInMainHand().getAmount() == 1){
                player.getInventory().setItemInMainHand(null);
            }
        }

        return true;
    }



    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        Player player = e.getPlayer();

        //if the player is holding an item
        if (e.getItem() != null) {

            try {

                //only fire the event if the correct item is in the main hand
                if (e.getHand() == EquipmentSlot.HAND &&
                        (e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING)
                                .equals(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(Main.class), "name"), PersistentDataType.STRING)))) {

                    //if the player left clicks and the ability is a left click ability
                    if (e.getAction().isLeftClick() && ability.getClickType() == ClickType.LEFT) {
                        executeAbility(player);

                    //if the player right clicks and the ability is a right click ability
                    } else if (e.getAction().isRightClick() && ability.getClickType() == ClickType.RIGHT) {

                        //if the player is trying to interact with a block that is interactable
                        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().isInteractable()) {
                            return;
                        }

                        executeAbility(player);
                    }
                }

            }catch (NullPointerException ex){
                //item is not a custom item
            }
        }
    }
}
