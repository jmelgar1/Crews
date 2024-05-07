package org.ovclub.crews.managers.hightable;

import org.bukkit.configuration.file.FileConfiguration;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.Crew;

import java.util.LinkedHashMap;
import java.util.Map;

public class LeaderboardManager {
    private final Crews plugin;

    public LeaderboardManager(Crews plugin) {
        this.plugin = plugin;
    }

    public void updateHighTable() {
        Map<Crew, Integer> allScores = plugin.getData().generateLeaderboardJson();
        Map<Crew, Integer> topFive = getTopEntries(allScores, 5);
        FileConfiguration config = plugin.getConfig();
        if (config.getConfigurationSection("high-table") != null) {
            config.getConfigurationSection("high-table").getKeys(false).forEach(key -> {
                config.set("high-table." + key, null);
            });
        }
        int rank = 1;
        for (Map.Entry<Crew, Integer> entry : topFive.entrySet()) {
            config.set("high-table." + entry.getKey().getUuid(), rank);
            rank++;
        }
        plugin.saveConfig();
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
}
