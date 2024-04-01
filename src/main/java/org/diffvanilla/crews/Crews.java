package org.diffvanilla.crews;

import java.io.*;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.diffvanilla.crews.commands.CommandManager;
import org.diffvanilla.crews.file.CrewsFile;
import org.diffvanilla.crews.listeners.PlayerEvents;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;

import com.google.gson.*;
import org.diffvanilla.crews.runnables.ShowInfoTask;

public final class Crews extends JavaPlugin implements Listener {
    /* New Stuff  */
    private PlayerData playerData;
    public PlayerData getData(){
        return this.playerData;
    }

    private CrewsFile crewsFile;
    public CrewsFile getCrewsFile(){
        return crewsFile;
    }

    private ProtocolManager protocolManager;
    public ProtocolManager getProtocolManager(){
        return this.protocolManager;
    }

    private ShowInfoTask showInfoTask;
    public ShowInfoTask getShowInfoTask(){
        return this.showInfoTask;
    }


    //private JDA jda;

    /* Old stuff */
    //unclaimed rewards file
    private File rewardsFile;
    private FileConfiguration rewards;

    //crew prices file
    private File pricesFile;
    private FileConfiguration prices;

    //crew prices file
    private File ctfFile;
    private FileConfiguration ctf;

    //player data file
    private File crewsFileJson;
    private JsonObject crewsJson;

    //player data file
    private File discordSettings;
    private FileConfiguration discord;

    @Override
    public void onEnable() {
        /* Crews Startup */
        System.out.println("Crews Enabled!");
        this.playerData = new PlayerData();
        this.showInfoTask = new ShowInfoTask(this);
        getCommand("crews").setExecutor(new CommandManager(this));
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigManager.loadConfig(this.getConfig());
        crewsFile = new CrewsFile(this);
        crewsFile.loadCrews();
        ConfigurationSerialization.registerClass(Crew.class);

        showInfoTask.runTaskTimer(this, 0L, 1L);

        protocolManager = ProtocolLibrary.getProtocolManager();

        /* Old Stuff Needs Updating */
        //getCommand("claimsponges").setExecutor(new claimspongesCommand());
        //getCommand("setsponges").setExecutor(new setspongesCommand());
        //getCommand("addsponges").setExecutor(new addspongesCommand());
        //getCommand("crewsupdate").setExecutor(new updateCrewsCommand());
        //getCommand("beginctf").setExecutor(new beginCTF());

        //createcrewsFile();
//        createRewardsFile();
//        createPricesFile();
//        createCTFFile();
//        createDiscordSettings();

        //createCrewsFileJson();

        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
//        getServer().getPluginManager().registerEvents(new InfoCommand(), this);
//        getServer().getPluginManager().registerEvents(new ShopCommand(), this);

        //getServer().getPluginManager().registerEvents(new CTF1Countdown(), this);
        //getServer().getPluginManager().registerEvents(new ProtectionEvents(), this);
        //getCTF().set("event", false);

//        try {
//            jda = JDABuilder.createDefault(getDiscordSettings().getString("bot_token")).build();
//            jda.awaitReady();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void onDisable() {
        System.out.println("Crews Disabled!");
        crewsFile.saveCrews();
        //jda.shutdown();

        // Allow at most 10 seconds for remaining requests to finish
//        try {
//            if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
//                jda.shutdownNow(); // Cancel all remaining requests
//                jda.awaitShutdown(); // Wait until shutdown is complete (indefinitely)
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

//    public JDA getJda(){
//        return this.jda;
//    }

    //REWARDS FILE
    public void saveRewardsFile() {
        try {
            rewards.save(rewardsFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Couldn't save unclaimedRewards.yml");
        }
    }

    public FileConfiguration getRewards() {
        return this.rewards;
    }

    private void createRewardsFile() {
        rewardsFile = new File(getDataFolder(), "unclaimedRewards.yml");
        if(!rewardsFile.exists()) {
            rewardsFile.getParentFile().mkdirs();
            saveResource("unclaimedRewards.yml", false);
            System.out.println("(!) unclaimedRewards.yml created");
        }

        rewards = new YamlConfiguration();
        try {
            rewards.load(rewardsFile);
            System.out.println("(!) unclaimedRewards.yml loaded");
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    //prices file
    public void savePricesFile() {
        try {
            prices.save(pricesFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Couldn't save prices.yml");
        }
    }

    public FileConfiguration getPrices() {
        return this.prices;
    }

    private void createPricesFile() {
        pricesFile = new File(getDataFolder(), "prices.yml");
        if(!pricesFile.exists()) {
            pricesFile.getParentFile().mkdirs();
            saveResource("prices.yml", false);
            System.out.println("(!) prices.yml created");
        }

        prices = new YamlConfiguration();
        try {
            prices.load(pricesFile);
            System.out.println("(!) prices.yml loaded");
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    //ctf file
    public void saveCTFFile() {
        try {
            ctf.save(ctfFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Couldn't save ctf.yml");
        }
    }

    public FileConfiguration getCTF() {
        return this.ctf;
    }

    public void createCTFFile() {
        ctfFile = new File(getDataFolder(), "ctf.yml");
        if(!ctfFile.exists()) {
            ctfFile.getParentFile().mkdirs();
            saveResource("ctf.yml", false);
            System.out.println("(!) ctf.yml created");
        }

        ctf = new YamlConfiguration();
        try {
            ctf.load(ctfFile);
            System.out.println("(!) ctf.yml loaded");
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void createCrewsFileJson() {
        crewsFileJson = new File(getDataFolder(), "crews_json.json");

        if (!crewsFileJson.exists()) {
            crewsFileJson.getParentFile().mkdirs();

            try {
                crewsFileJson.createNewFile();
                saveResource("crews_json.json", false);
                System.out.println("(!) crew_json.json created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileReader reader = new FileReader(crewsFileJson);
            crewsJson = JsonParser.parseReader(reader).getAsJsonObject();
            System.out.println("(!) crews_json.json loaded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savecrewsFileJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = new FileWriter(crewsFileJson)) {
            gson.toJson(crewsJson, writer);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Couldn't save crews_json.json");
        }
    }

    public JsonObject getcrewsJson() {
        return this.crewsJson;
    }

    public FileConfiguration getDiscordSettings() {
        return this.discord;
    }

    private void createDiscordSettings() {
        discordSettings = new File(getDataFolder(), "discordsettings.yml");
        if(!discordSettings.exists()) {
            discordSettings.getParentFile().mkdirs();
            saveResource("discordsettings.yml", false);
            System.out.println("(!) discordsettings.yml created");
        }

        discord = new YamlConfiguration();
        try {
            discord.load(discordSettings);
            System.out.println("(!) discordsettings.yml loaded");
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
