package org.ovclub.crews.utilities;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.jline.utils.Log;
import org.ovclub.crews.managers.file.HighTableConfigManager;
import org.ovclub.crews.managers.hightable.DailyMultiplierManager;
import org.ovclub.crews.object.Crew;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HightableUtility{
    public static void saveMultipliers(DailyMultiplierManager manager) {
        saveMobDropRates(manager);
        saveOreDropRates(manager);
        saveXPDrops(manager);
        saveDiscounts(manager);
        saveMobDifficultyRates(manager);
        HighTableConfigManager.saveHighTableConfig();
    }

    private static void saveMobDropRates(DailyMultiplierManager manager) {
        FileConfiguration hightableConfig = HighTableConfigManager.getHighTableConfig();
        Map<EntityType, Double> passive = manager.getPassiveMobDropRates();
        Map<EntityType, Double> neutral = manager.getNeutralMobDropRates();
        Map<EntityType, Double> hostile = manager.getHostileMobDropRates();

        passive.forEach((k, v) -> hightableConfig.set("multipliers.mobDrops.passive." + k.name(), v));
        neutral.forEach((k, v) -> hightableConfig.set("multipliers.mobDrops.neutral." + k.name(), v));
        hostile.forEach((k, v) -> hightableConfig.set("multipliers.mobDrops.hostile." + k.name(), v));
    }

    private static void saveOreDropRates(DailyMultiplierManager manager) {
        FileConfiguration hightableConfig = HighTableConfigManager.getHighTableConfig();
        Map<Material, Double> ores = manager.getOreDropRates();
        ores.forEach((k, v) -> hightableConfig.set("multipliers.oreDrops." + k.name(), v));
    }

    private static void saveXPDrops(DailyMultiplierManager manager) {
        FileConfiguration hightableConfig = HighTableConfigManager.getHighTableConfig();
        Map<Material, Double> blocksXP = manager.getOresAndBlocksXPDropRates();
        Map<Material, Double> activitiesXP = manager.getActivitiesXPDrops();
        Map<EntityType, Double> mobsXP = manager.getMobXPDropRates();

        blocksXP.forEach((k, v) -> hightableConfig.set("multipliers.xpDrops.blocks." + k.name(), v));
        activitiesXP.forEach((k, v) -> hightableConfig.set("multipliers.xpDrops.activities." + k.name(), v));
        mobsXP.forEach((k, v) -> hightableConfig.set("multipliers.xpDrops.mobs." + k.name(), v));
    }

    private static void saveDiscounts(DailyMultiplierManager manager) {
        FileConfiguration hightableConfig = HighTableConfigManager.getHighTableConfig();
        Map<Material, Double> discounts = manager.getDiscountMultipliers();
        discounts.forEach((k, v) -> hightableConfig.set("multipliers.discounts." + k.name(), v));
    }

    private static void saveMobDifficultyRates(DailyMultiplierManager manager) {
        FileConfiguration hightableConfig = HighTableConfigManager.getHighTableConfig();
        Map<EntityType, Double> difficulties = manager.getMobDifficultyRates();
        difficulties.forEach((k, v) -> hightableConfig.set("multipliers.mobDifficulty." + k.name(), v));
    }

    public static void updateHighTable(Map<Crew, Integer> leaderboard) {
        Map<Crew, Integer> topFive = getTopEntries(leaderboard, 5);
        FileConfiguration config = HighTableConfigManager.getHighTableConfig();
        ConfigurationSection sectionConfig = config.getConfigurationSection("high-table");

        if (sectionConfig == null) {
            sectionConfig = config.createSection("high-table");
        }

        sectionConfig.set("crews", null);

        List<String> topFiveList = topFive.keySet().stream()
            .map(Crew::getUuid)
            .collect(Collectors.toList());

        sectionConfig.set("crews", topFiveList);

        HighTableConfigManager.saveHighTableConfig();
    }

    public static <K, V> Map<K, V> getTopEntries(Map<K, V> sortedMap, int n) {
        Map<K, V> result = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<K, V> entry : sortedMap.entrySet()) {
            if (count++ < n) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                break;
            }
        }
        return result;
    }

    public static LinkedHashMap<Material, Double> convertStringToMaterialMap(LinkedHashMap<String, Double> stringMap) {
        LinkedHashMap<Material, Double> materialMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : stringMap.entrySet()) {
            try {
                Material material = Material.valueOf(entry.getKey().toUpperCase());
                materialMap.put(material, entry.getValue());
            } catch (IllegalArgumentException e) {
                Log.error("Invalid material: " + entry.getKey());
            }
        }
        return materialMap;
    }
}
