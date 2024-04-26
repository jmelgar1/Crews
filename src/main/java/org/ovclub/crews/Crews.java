package org.ovclub.crews;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ovclub.crews.commands.CommandManager;
import org.ovclub.crews.file.CrewsFile;
import org.ovclub.crews.listeners.CrewGUIListener;
import org.ovclub.crews.listeners.PlayerEvents;
import org.ovclub.crews.listeners.skirmish.ArenaListener;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.managers.hightable.DailyMultiplierManager;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.managers.skirmish.SkirmishManager;
import org.ovclub.crews.object.PlayerData;

import org.ovclub.crews.runnables.MatchSearchTask;
import org.ovclub.crews.runnables.RunnableManager;
import org.ovclub.crews.runnables.UpdateScoreboard;

public final class Crews extends JavaPlugin implements Listener {
    /* New Stuff  */
    private PlayerData playerData;
    public PlayerData getData(){
        return this.playerData;
    }

    private CrewsFile crewsFile;

    private DailyMultiplierManager multiplierManager;

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
        MatchSearchTask matchSearchTask = new MatchSearchTask(this);
        UpdateScoreboard updateScoreboard = new UpdateScoreboard(this);
        getCommand("crews").setExecutor(new CommandManager(this));
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigManager.loadConfig(this.getConfig());
        crewsFile = new CrewsFile(this);
        crewsFile.loadCrews();

        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        getServer().getPluginManager().registerEvents(new CrewGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(this), this);

        matchSearchTask.runTaskTimer(this, 0L, 20L * 60);

        multiplierManager = new DailyMultiplierManager();
    }

    public void onDisable() {
        System.out.println("Crews Disabled!");
        crewsFile.saveCrews();
    }
}
