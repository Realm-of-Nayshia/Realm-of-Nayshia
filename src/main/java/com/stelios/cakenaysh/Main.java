package com.stelios.cakenaysh;

import com.stelios.cakenaysh.AbilityCreation.Abilities.DialOfTheSunAbility;
import com.stelios.cakenaysh.AbilityCreation.Abilities.WrathOfSpartaAbility;
import com.stelios.cakenaysh.AbilityCreation.CustomAbilities;
import com.stelios.cakenaysh.Commands.*;
import com.stelios.cakenaysh.Commands.TabComplete.*;
import com.stelios.cakenaysh.Items.CustomItems;
import com.stelios.cakenaysh.Items.Recipes;
import com.stelios.cakenaysh.Listeners.Entity.*;
import com.stelios.cakenaysh.Listeners.Inventory.InventoryAlteredListener;
import com.stelios.cakenaysh.Listeners.Server.ConnectionListener;
import com.stelios.cakenaysh.Listeners.Server.ServerListPingListener;
import com.stelios.cakenaysh.Listeners.Server.ServerStartupListener;
import com.stelios.cakenaysh.Listeners.Stats.ProficiencyChangedListener;
import com.stelios.cakenaysh.Listeners.Stats.SpeedChangedListener;
import com.stelios.cakenaysh.Listeners.Stats.XpGainListener;
import com.stelios.cakenaysh.Managers.*;
import com.stelios.cakenaysh.MenuCreation.MenuListener;
import com.stelios.cakenaysh.Npc.Traits.NpcStats;
import com.stelios.cakenaysh.Util.Database;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private Database database;
    private PlayerManager playerManager;
    private StashManager stashManager;
    private CombatManager combatManager;
    private StatsManager statsManager;
    private RecipeManager recipeManager;
    private PacketManager packetManager;

    @Override
    public void onEnable() {

        //check if Citizens is present and enabled.
        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //database setup
        database = new Database();

        //setup of important plugin info
        registerGameRules();
        registerManagers();
        registerEvents();
        registerCommands();
        registerAbilities();
        registerTraits();
        registerRecipes();
    }


    private void registerGameRules(){
        for (World world : Bukkit.getWorlds()){
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.DO_LIMITED_CRAFTING, true);
        }
    }

    private void registerManagers(){
        playerManager = new PlayerManager();
        stashManager = new StashManager();
        combatManager = new CombatManager();
        statsManager = new StatsManager(this);
        recipeManager = new RecipeManager();
        packetManager = new PacketManager();
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ServerStartupListener(this, statsManager), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamagedListener(this, statsManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerStatusChangeListener(this, statsManager), this);
        Bukkit.getPluginManager().registerEvents(new InventoryAlteredListener(this, statsManager), this);
        Bukkit.getPluginManager().registerEvents(new ProficiencyChangedListener(this, statsManager), this);
        Bukkit.getPluginManager().registerEvents(new EntityPotionEffectListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpeedChangedListener(), this);
        Bukkit.getPluginManager().registerEvents(new ServerListPingListener(), this);
        Bukkit.getPluginManager().registerEvents(new XpGainListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SentinelStatusChangeListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerAdvancementCompletedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDiscoverRecipeEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerItemConsumeListener(this), this);
    }

    @SuppressWarnings("DataFlowIssue")
    private void registerCommands(){
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("kill").setExecutor(new KillCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("stash").setExecutor(new StashCommand());
        getCommand("playtime").setExecutor(new PlayTimeCommand());

        getCommand("giveitem").setExecutor(new GiveItemCommand());
        getCommand("giveitem").setTabCompleter(new GiveItemTabComplete());

        getCommand("setattribute").setExecutor(new SetAttributesCommand());
        getCommand("setattribute").setTabCompleter(new SetAttributesTabComplete());

        getCommand("addattribute").setExecutor(new AddAttributesCommand());
        getCommand("addattribute").setTabCompleter(new AddAttributesTabComplete());

        getCommand("getattributes").setExecutor(new GetAttributesCommand());
        getCommand("getattributes").setTabCompleter(new GetAttributesTabComplete());

        getCommand("resetattributes").setExecutor(new ResetAttributesCommand());
        getCommand("resetattributes").setTabCompleter(new ResetAttributesTabComplete());

        getCommand("resetnpcstats").setExecutor(new ResetNpcStatsCommand());
        getCommand("getnpcstats").setExecutor(new GetNpcStatsCommand());
        getCommand("setnpcstat").setExecutor(new SetNpcStatCommand());
        getCommand("setnpcstat").setTabCompleter(new SetNpcStatTabComplete());

        getCommand("menu").setExecutor(new MenuCommand());

        getCommand("recipe").setExecutor(new RecipeCommand());
        getCommand("recipe").setTabCompleter(new RecipeTabComplete());

        getCommand("updatecollection").setExecutor(new UpdateCollectionCommand());
        getCommand("updatecollection").setTabCompleter(new UpdateCollectionTabComplete());
    }

    private void registerAbilities(){
        new WrathOfSpartaAbility(CustomAbilities.SPARTAN_WRATH, CustomItems.WRATH_OF_SPARTA.getItem(), 5, 5);
        new DialOfTheSunAbility(CustomAbilities.GRADUAL_SET_DAY, CustomItems.DIAL_OF_THE_SUN.getItem(), 25, 15);
    }

    private void registerTraits(){
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcStats.class).withName("npcstats"));
    }

    private void registerRecipes() {
        for (Recipes recipe : Recipes.values()) {
            Bukkit.addRecipe(recipe.getRecipe());
        }
    }

    //manager getters
    public Database getDatabase() {return database;}
    public PlayerManager getPlayerManager() {return playerManager;}
    public StashManager getStashManager() {return stashManager;}
    public CombatManager getCombatManager() {return combatManager;}
    public StatsManager getStatsManager() {return statsManager;}
    public RecipeManager getRecipeManager() {return recipeManager;}
    public PacketManager getPacketManager() {return packetManager;}


    @Override
    public void onDisable() {

        //save player data and kick all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            statsManager.updateDatabaseStatsPlayer(player);
            Objects.requireNonNull(player.getPlayer()).kick(Component.text("Server is shutting down.", TextColor.color(255,0,0)));
        }

        //loop through all the npcs
        for (NPC npc : CitizensAPI.getNPCRegistry()) {

            //remove all player data from every npcStats npc
            if (npc.hasTrait(NpcStats.class)) {
                npc.getOrAddTrait(NpcStats.class).clearPlayerDamages();
            }
        }

        //remove all temporary damage text displays
        ArrayList<TextDisplay> textDisplays = statsManager.getTextDisplays();
        for (TextDisplay textDisplay : textDisplays) {
            textDisplay.remove();
        }

        //disconnect from the database
        database.getClient().close();

    }

}
