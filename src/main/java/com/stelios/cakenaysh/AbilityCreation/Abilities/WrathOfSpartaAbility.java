package com.stelios.cakenaysh.AbilityCreation.Abilities;

import com.stelios.cakenaysh.AbilityCreation.ItemAbility;
import com.stelios.cakenaysh.AbilityCreation.CustomAbilities;
import com.stelios.cakenaysh.Items.Item;
import com.stelios.cakenaysh.Main;
import com.stelios.cakenaysh.Util.CustomPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WrathOfSpartaAbility extends ItemAbility {

    public WrathOfSpartaAbility(CustomAbilities ability, Item item, int stamina, long cooldown) {
        super(ability, item, stamina, cooldown);
    }

    public void doAbility(Player player){

        CustomPlayer customPlayer = Main.getPlugin(Main.class).getPlayerManager().getCustomPlayer(player.getUniqueId());

        float bonusDamage = customPlayer.getBonusDamage() + customPlayer.getDamage();
        customPlayer.setBonusDamage(bonusDamage);

        player.sendMessage(Component.text("The spark within you ignites... Rage takes over...",
                TextColor.color(255,0,0)));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 15, 15);

        //wait 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {

                //remove the bonus damage
                player.sendMessage(Component.text("The rage subsides...", TextColor.color(255,0,0)));
                customPlayer.setBonusDamage(customPlayer.getBonusDamage() - bonusDamage);
            }
        }.runTaskLater(Main.getPlugin(Main.class), 200);

    }
}
