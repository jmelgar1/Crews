package org.ovclub.crews.managers.file;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.hightable.MultiplierItem;
import org.ovclub.crews.object.hightable.VoteItem;

import java.io.File;
import java.util.LinkedHashMap;

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
        loadActiveMultipliers();
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
        ConfigurationSection highTableSection = configData.getConfigurationSection("high-table.crews");
        if (highTableSection != null) {
            highTableSection.getKeys(false).forEach(key -> {
                plugin.getData().addHightableCrew(key);
            });
        }
    }

    private void loadActiveMultipliers() {
        ConfigurationSection activeSection = configData.getConfigurationSection("active-multipliers.top-votes");
        if (activeSection != null) {
            activeSection.getKeys(false).forEach(key -> {
                String path = "active-multipliers.top-votes." + key;
                String section = configData.getString(path + ".section");
                String item = configData.getString(path + ".item");
                String multiplier = configData.getString(path + ".multiplier");

                VoteItem voteItem = new VoteItem(section, item, multiplier);
                plugin.getData().addActiveMultipliers(voteItem);
            });
        }
    }
}
