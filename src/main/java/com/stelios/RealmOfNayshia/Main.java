package com.stelios.RealmOfNayshia;

import com.stelios.RealmOfNayshia.AbilityCreation.Abilities.DialOfTheSunAbility;
import com.stelios.RealmOfNayshia.AbilityCreation.Abilities.WrathOfSpartaAbility;
import com.stelios.RealmOfNayshia.AbilityCreation.CustomAbilities;
import com.stelios.RealmOfNayshia.Commands.*;
import com.stelios.RealmOfNayshia.Commands.TabComplete.*;
import com.stelios.RealmOfNayshia.Items.CustomItems;
import com.stelios.RealmOfNayshia.Items.Recipes;
import com.stelios.RealmOfNayshia.Listeners.Entity.*;
import com.stelios.RealmOfNayshia.Listeners.Inventory.InventoryAlteredListener;
import com.stelios.RealmOfNayshia.Listeners.Server.ConnectionListener;
import com.stelios.RealmOfNayshia.Listeners.Server.ServerListPingListener;
import com.stelios.RealmOfNayshia.Listeners.Server.ServerStartupListener;
import com.stelios.RealmOfNayshia.Listeners.Stats.ProficiencyChangedListener;
import com.stelios.RealmOfNayshia.Listeners.Stats.SpeedChangedListener;
import com.stelios.RealmOfNayshia.Listeners.Stats.XpGainListener;
import com.stelios.RealmOfNayshia.Managers.*;
import com.stelios.RealmOfNayshia.MenuCreation.MenuListener;
import com.stelios.RealmOfNayshia.Npc.Traits.NpcQuest;
import com.stelios.RealmOfNayshia.Npc.Traits.NpcStats;
import com.stelios.RealmOfNayshia.Util.Database;
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
import org.mcmonkey.sentinel.SentinelTrait;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private Database database;
    private PlayerManager playerManager;
    private StashManager stashManager;
    private CombatManager combatManager;
    private StatsManager statsManager;
    private PlayerItemManager playerItemManager;
    private PlayerInventoryManager playerInventoryManager;
    private RecipeManager recipeManager;
    private QuestManager questManager;
    private NpcInfoManager npcInfoManager;
    private PacketManager packetManager;
    private FileManager fileManager;

    @Override
    public void onEnable() {

        //check if Citizens is present and enabled.
        if(getServer().getPluginManager().getPlugin("Citizens") == null || !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
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
        playerItemManager = new PlayerItemManager();
        playerInventoryManager = new PlayerInventoryManager();
        recipeManager = new RecipeManager();
        questManager = new QuestManager();
        npcInfoManager = new NpcInfoManager();
        packetManager = new PacketManager();
        fileManager = new FileManager();
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
        Bukkit.getPluginManager().registerEvents(new NpcClickedListener(this), this);
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

        getCommand("setnpcquest").setExecutor(new SetNpcQuestCommand());
        getCommand("setnpcquest").setTabCompleter(new SetNpcQuestTabComplete());

        getCommand("questresponse").setExecutor(new QuestResponseCommand());
        getCommand("questresponse").setTabCompleter(new QuestResponseTabComplete(this));
        getCommand("questresponsesize").setExecutor(new QuestResponseSizeCommand());
        getCommand("questresponsesize").setTabCompleter(new QuestResponseTypeTabComplete());
        getCommand("questresponseget").setExecutor(new QuestResponseGetCommand());
        getCommand("questresponseget").setTabCompleter(new QuestResponseTypeTabComplete());
        getCommand("questresponsereset").setExecutor(new QuestResponseResetCommand());
        getCommand("questresponsereset").setTabCompleter(new QuestResponseTypeTabComplete());

        getCommand("getnpcid").setExecutor(new GetNpcIdCommand());
        getCommand("getnbt").setExecutor(new GetNBTCommand());
    }

    private void registerAbilities(){
        new WrathOfSpartaAbility(CustomAbilities.SPARTAN_WRATH, CustomItems.WRATH_OF_SPARTA.getItem(), 5, 5);
        new DialOfTheSunAbility(CustomAbilities.GRADUAL_SET_DAY, CustomItems.DIAL_OF_THE_SUN.getItem(), 25, 15);
    }

    private void registerTraits(){
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcStats.class).withName("npcstats"));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcQuest.class).withName("npcquest"));
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
    public PlayerItemManager getPlayerItemManager() {return playerItemManager;}
    public PlayerInventoryManager getPlayerInventoryManager() {return playerInventoryManager;}
    public RecipeManager getRecipeManager() {return recipeManager;}
    public QuestManager getQuestManager() {return questManager;}
    public NpcInfoManager getNpcInfoManager() {return npcInfoManager;}
    public PacketManager getPacketManager() {return packetManager;}
    public FileManager getFileManager() {return fileManager;}


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

                //update npc info
                if (npc.hasTrait(SentinelTrait.class)){
                    npcInfoManager.updateNpcInfo(npc);
                }
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
