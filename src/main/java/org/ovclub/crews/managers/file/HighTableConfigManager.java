package org.ovclub.crews.managers.file;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ovclub.crews.Crews;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class HighTableConfigManager {
    private static File hightableFile = null;
    private static FileConfiguration hightableConfig = null;
    private static Crews plugin;

    public static void initialize(Crews plugin) {
        HighTableConfigManager.plugin = plugin;
        reloadHighTableConfig();
    }

    public static void reloadHighTableConfig() {
        if (hightableFile == null) {
            hightableFile = new File(plugin.getDataFolder(), "hightable.yml");
        }
        hightableConfig = YamlConfiguration.loadConfiguration(hightableFile);

        Reader defConfigStream = new InputStreamReader(plugin.getResource("hightable.yml"), StandardCharsets.UTF_8);
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        hightableConfig.setDefaults(defConfig);
    }

    public static FileConfiguration getHighTableConfig() {
        if (hightableConfig == null) {
            reloadHighTableConfig();
        }
        return hightableConfig;
    }

    public static void saveHighTableConfig() {
        if (hightableConfig == null || hightableFile == null) {
            return;
        }
        try {
            getHighTableConfig().save(hightableFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save hightable.yml to " + hightableFile);
            ex.printStackTrace();
        }
    }

    public static void saveDefaultHighTableConfig() {
        if (hightableFile == null) {
            hightableFile = new File(plugin.getDataFolder(), "hightable.yml");
        }
        if (!hightableFile.exists()) {
            plugin.saveResource("hightable.yml", false);
        }
    }
}
