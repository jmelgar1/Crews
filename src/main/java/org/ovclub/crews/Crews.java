package org.ovclub.crews;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ovclub.crews.commands.CommandManager;
import org.ovclub.crews.file.CrewsFile;
import org.ovclub.crews.listeners.CrewGUIListener;
import org.ovclub.crews.listeners.CrewShopListener;
import org.ovclub.crews.listeners.PlayerEvents;
import org.ovclub.crews.listeners.hightable.ActiveMultiplierListener;
import org.ovclub.crews.listeners.skirmish.ArenaListener;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.managers.file.HighTableConfigManager;
import org.ovclub.crews.managers.file.HightableFile;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.managers.skirmish.SkirmishManager;
import org.ovclub.crews.object.PlayerData;

import org.ovclub.crews.runnables.MatchSearchTask;
import org.ovclub.crews.runnables.ResetHighTableVote;
import org.ovclub.crews.runnables.RunnableManager;
import org.ovclub.crews.runnables.UpdateScoreboard;

public final class Crews extends JavaPlugin implements Listener {
    /* New Stuff  */
    private PlayerData playerData;
    public PlayerData getData(){return this.playerData;}

    private CrewsFile crewsFile;
    public CrewsFile getCrewsFile(){return this.crewsFile;}

    private HightableFile hightableFile;
    public HightableFile getHightableFile() { return hightableFile;}

    private SkirmishManager skirmishManager;
    public SkirmishManager getSkirmishManager(){return skirmishManager;}

    private ArenaManager arenaManager;
    public ArenaManager getArenaManager() { return arenaManager; }

    private RunnableManager runnableManager;
    public RunnableManager getRunnableManager() { return this.runnableManager; }

    @Override
    public void onEnable() {
        /* Crews Startup */
        System.out.println("Crews Enabled!");
        this.playerData = new PlayerData();
        this.arenaManager = new ArenaManager();
        this.runnableManager = new RunnableManager(this);
        this.skirmishManager = new SkirmishManager(this);
        ResetHighTableVote resetHighTableVote = new ResetHighTableVote(this);
        MatchSearchTask matchSearchTask = new MatchSearchTask(this);
        UpdateScoreboard updateScoreboard = new UpdateScoreboard(this);
        getCommand("crews").setExecutor(new CommandManager(this));
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigManager.loadConfig(this.getConfig());
        crewsFile = new CrewsFile(this);
        crewsFile.loadCrews();

        hightableFile = new HightableFile(this);
        hightableFile.loadHightable();

        HighTableConfigManager.initialize(this);
        HighTableConfigManager.saveDefaultHighTableConfig();

        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        getServer().getPluginManager().registerEvents(new CrewGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new CrewShopListener(this), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(this), this);
        getServer().getPluginManager().registerEvents(new ActiveMultiplierListener(this), this);

        matchSearchTask.runTaskTimer(this, 0L, 20L * 60);
        resetHighTableVote.run();
    }

    public void onDisable() {
        System.out.println("Crews Disabled!");
        crewsFile.saveCrews();
        HighTableConfigManager.saveHighTableConfig();
    }
}
