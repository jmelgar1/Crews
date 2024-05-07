package org.ovclub.crews.managers.file;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.hightable.MultiplierItem;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class HightableFile {
    private final Crews plugin;
    private FileConfiguration configData;

    public HightableFile(Crews plugin) {
        this.plugin = plugin;
    }

    public void loadHightable() {
        File configFile = new File(plugin.getDataFolder(), "hightable.yml");
        if (!configFile.exists()) {
            plugin.saveResource("hightable.yml", false);
        }
        configData = YamlConfiguration.loadConfiguration(configFile);
        loadMultipliers();
        loadHighTableCrews();
    }

    private void loadMultipliers() {
        String[] sections = {"mobDrops.passive", "mobDrops.neutral", "mobDrops.hostile", "oreDrops", "xpDrops.blocks", "xpDrops.activities", "xpDrops.mobs", "discounts", "mobDifficulty"};
        for (String section : sections) {
            String finalSection = "multipliers." + section;
            ConfigurationSection sectionConfig = configData.getConfigurationSection(finalSection);
            if(sectionConfig == null) {
                configData.createSection(finalSection);
            }
            LinkedHashMap<String, Double> multipliers = new LinkedHashMap<>();
            configData.getConfigurationSection(finalSection).getKeys(false).forEach(key -> {
                double value = configData.getDouble(finalSection + "." + key);
                multipliers.put(key, value);
            });
            MultiplierItem item = new MultiplierItem(finalSection, multipliers);
            plugin.getData().addMultiplier(item);
        }
    }

    private void loadHighTableCrews() {
        ConfigurationSection highTableSection = configData.getConfigurationSection("high-table");
        if (highTableSection != null) {
            List<String> crewUUIDs = highTableSection.getStringList("crews");
            for (String uuid : crewUUIDs) {
                plugin.getData().addHightableCrew(uuid);
            }
        }
    }
}
