package org.ovclub.crews.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.file.HighTableConfigManager;
import org.ovclub.crews.managers.hightable.DailyMultiplierManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.hightable.MultiplierItem;
import org.ovclub.crews.object.hightable.VoteItem;

import java.util.*;
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
        Map<String, Double> mobsXP = manager.getMobXPDropRates();

        blocksXP.forEach((k, v) -> hightableConfig.set("multipliers.xpDrops.blocks." + k.name(), v));
        activitiesXP.forEach((k, v) -> hightableConfig.set("multipliers.xpDrops.activities." + k.name(), v));
        mobsXP.forEach((k, v) -> hightableConfig.set("multipliers.xpDrops.mobs." + k, v));
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

        config.set("high-table.crews", null);

        ConfigurationSection sectionConfig = config.getConfigurationSection("high-table.crews");

        if (sectionConfig == null) {
            sectionConfig = config.createSection("high-table.crews");
        }

        List<String> topFiveList = topFive.keySet().stream()
            .map(Crew::getUuid)
            .toList();

        for(String crew : topFiveList) {
            sectionConfig.createSection(crew);
        }

        HighTableConfigManager.saveHighTableConfig();
    }

    public static void updateActiveMultipliers(List<VoteItem> topItems) {
        FileConfiguration config = HighTableConfigManager.getHighTableConfig();

        config.set("active-multipliers.top-votes", null);

        int index = 1;
        for (VoteItem item : topItems) {
            String path = "active-multipliers.top-votes.vote" + index;
            config.set(path + ".section", item.getSection());
            config.set(path + ".item", item.getItem());
            config.set(path + ".multiplier", Double.valueOf(item.getMultiplier()));
            index++;
        }

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

    public static boolean hasPlayerVoted(Player p) {
        boolean hasPlayerVoted = false;
        FileConfiguration config = HighTableConfigManager.getHighTableConfig();
        ConfigurationSection crewsSection = config.getConfigurationSection("high-table.crews");
        for (String crewUuid : crewsSection.getKeys(false)) {
            String votesPath = "high-table.crews." + crewUuid + ".votes";
            ConfigurationSection votesSection = config.getConfigurationSection(votesPath);
            if (votesSection != null) {
                for (String playerUuid : votesSection.getKeys(false)) {
                    if (playerUuid.equals(p.getUniqueId().toString())) {
                        hasPlayerVoted = true;
                        break;
                    }
                }
            }
        }
        return hasPlayerVoted;
    }

    public static TextComponent isSelected(Player p, String section, String material) {
        FileConfiguration config = HighTableConfigManager.getHighTableConfig();
        ConfigurationSection crewsSection = config.getConfigurationSection("high-table.crews");
        boolean isSelectedByPlayer = false;
        int matchingPlayers = 0;
        if (crewsSection == null) {
            return Component.text("Click to select", NamedTextColor.DARK_GRAY);
        }
        for (String crewUuid : crewsSection.getKeys(false)) {
            String votesPath = "high-table.crews." + crewUuid + ".votes";
            ConfigurationSection votesSection = config.getConfigurationSection(votesPath);
            if (votesSection != null) {
                for (String playerUuid : votesSection.getKeys(false)) {
                    String value = votesSection.getString(playerUuid + "." + section);
                    if (value != null && Objects.equals(value, material)) {
                        if (playerUuid.equals(p.getUniqueId().toString())) {
                            isSelectedByPlayer = true;
                        }
                        matchingPlayers++;
                    }
                }
            }
        }
        if (matchingPlayers == 0) {
            return ComponentUtilities.createComponent("Click to select", NamedTextColor.DARK_GRAY);
        } else if (isSelectedByPlayer) {
            return ComponentUtilities.createComponent("You and " + (matchingPlayers - 1) + " other players have selected this", NamedTextColor.GREEN);
        } else {
            return ComponentUtilities.createComponent(matchingPlayers + " players have selected this", NamedTextColor.GREEN);
        }
    }

    public static List<VoteItem> getTopVotedItems(Crews plugin) {
        FileConfiguration config = HighTableConfigManager.getHighTableConfig();
        ConfigurationSection crewsSection = config.getConfigurationSection("high-table.crews");
        Map<String, Integer> voteCounts = new HashMap<>();

        if (crewsSection == null) {
            return Collections.emptyList();
        }
        for (String crewUuid : crewsSection.getKeys(false)) {
            String votesPath = "high-table.crews." + crewUuid + ".votes";
            ConfigurationSection votesSection = config.getConfigurationSection(votesPath);
            if (votesSection != null) {
                for (String playerUuid : votesSection.getKeys(false)) {
                    ConfigurationSection playerVotes = votesSection.getConfigurationSection(playerUuid);
                    if (playerVotes != null) {
                        for (String voteKey : playerVotes.getKeys(false)) {
                            String voteValue = playerVotes.getString(voteKey);
                            String fullVote = voteKey + ": " + voteValue;
                            voteCounts.put(fullVote, voteCounts.getOrDefault(fullVote, 0) + 1);
                        }
                    }
                }
            }
        }

        // Create a list from the entry set and sort it by values (counts)
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(voteCounts.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        List<VoteItem> topVotedItems = new ArrayList<>();
        int itemsAdded = 0;

        // Shuffle and select top 5 with proper handling for ties
        for (int i = 0; i < sortedEntries.size() && itemsAdded < 5; i++) {
            int currentCount = sortedEntries.get(i).getValue();
            List<String> candidates = sortedEntries.stream()
                .filter(e -> e.getValue() == currentCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            Collections.shuffle(candidates);

            for (String candidate : candidates) {
                String[] parts = candidate.split(": ");
                String section = parts[0].trim();
                String item = parts[1].trim();
                String multiplierText = getMultiplierText(candidate, plugin);
                VoteItem voteItem = new VoteItem(section, item, multiplierText);

                if (!topVotedItems.contains(voteItem)) {
                    topVotedItems.add(voteItem);
                    itemsAdded++;
                    if (itemsAdded == 5) break;
                }
            }
        }
        return topVotedItems;
    }

    private static String getMultiplierText(String voteItem, Crews plugin) {
        String[] parts = voteItem.split(": ");
        if (parts.length < 2) return "N/A";
        String section = parts[0].trim();
        String item = parts[1].trim();

        if (section.equals("mobDrops")) {
            EntityType type = EntityType.valueOf(item);
            String mobType = getMobType(type);
            switch (mobType) {
                case "passive" -> section = section.concat(".passive");
                case "neutral" -> section = section.concat(".neutral");
                case "hostile" -> section = section.concat(".hostile");
                default -> {
                }
            }
        }

        if (section.equals("xpDrops")) {
            switch (item) {
                case "NEUTRAL", "PASSIVE", "HOSTILE" -> section = section.concat(".mobs");
                case "EMERALD", "FISHING_ROD", "WHEAT", "FURNACE", "GRINDSTONE" -> section = section.concat(".activities");
                case "ANCIENT_DEBRIS", "DIAMOND_ORE", "SPAWNER", "SKULK" -> section = section.concat(".blocks");
                default -> {
                }
            }
        }

        MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection(section);
        if (multiplierItem.getSection().endsWith(section)) {
            Double multiplier = multiplierItem.getValues().get(item);
            if (multiplier != null) {
                return multiplier.toString();
            }
        }
        return "N/A";
    }

    public static String getMobType(EntityType item) {
        if (Arrays.asList(passiveMobs).contains(item)) {
            return "passive";
        } else if (Arrays.asList(neutralMobs).contains(item)) {
            return "neutral";
        } else if (Arrays.asList(hostileMobs).contains(item)) {
            return "hostile";
        }
        return "unknown";
    }

    public static List<TextComponent> generateMultiplierString(VoteItem voteItem) {
        String section = voteItem.getSection();
        String item = voteItem.getItem();
        String multiplier = voteItem.getMultiplier();

        List<TextComponent> components = new ArrayList<>();

        switch (section) {
            case "mobDrops" -> {
                components.add(Component.text(UnicodeCharacters.formatString(item) + "s").color(NamedTextColor.DARK_GREEN)
                    .append(Component.text(" have a ").color(NamedTextColor.YELLOW))
                    .append(Component.text(UnicodeCharacters.convertDoubleToPercent(multiplier)).color(NamedTextColor.DARK_GREEN))
                    .append(Component.text(" increased").color(NamedTextColor.YELLOW)));
                components.add(Component.text("chance of dropping extra loot.").color(NamedTextColor.YELLOW));
            }
            case "oreDrops" -> {
                components.add(Component.text(UnicodeCharacters.formatString(item)).color(NamedTextColor.DARK_GREEN)
                    .append(Component.text(" has a ").color(NamedTextColor.YELLOW))
                    .append(Component.text(UnicodeCharacters.convertDoubleToPercent(multiplier)).color(NamedTextColor.DARK_GREEN))
                    .append(Component.text(" increased").color(NamedTextColor.YELLOW)));
                components.add(Component.text("chance of dropping extra items.").color(NamedTextColor.YELLOW));
            }
            case "xpDrops" -> {
                if (isValidMaterial(item)) {
                    if (Arrays.stream(xpDropActivities).toList().contains(Material.valueOf(item))) {
                        switch (item) {
                            case "FURNACE" -> item = "COOKING";
                            case "EMERALD" -> item = "TRADING";
                            case "FISHING_ROD" -> item = "FISHING";
                            case "WHEAT" -> item = "BREEDING";
                        }
                        components.add(Component.text(UnicodeCharacters.formatString(item)).color(NamedTextColor.DARK_GREEN)
                            .append(Component.text(" rewards").color(NamedTextColor.YELLOW)));
                        components.add(Component.text(UnicodeCharacters.convertDoubleToPercent(multiplier)).color(NamedTextColor.DARK_GREEN)
                            .append(Component.text(" more XP.").color(NamedTextColor.YELLOW)));
                    } else if (Arrays.stream(xpDropBlockTypes).toList().contains(Material.valueOf(item))) {
                        switch (item) {
                            case "DIAMOND_ORE" -> item = "Overworld Ores";
                            case "ANCIENT_DEBRIS" -> item = "Nether Ores";
                        }
                        components.add(Component.text(item).color(NamedTextColor.DARK_GREEN)
                            .append(Component.text(" drops").color(NamedTextColor.YELLOW)));
                        components.add(Component.text(UnicodeCharacters.convertDoubleToPercent(multiplier)).color(NamedTextColor.DARK_GREEN)
                            .append(Component.text(" more XP.").color(NamedTextColor.YELLOW)));
                    }
                } else if (Arrays.stream(xpDropMobTypes).toList().contains(item)) {
                    components.add(Component.text(UnicodeCharacters.formatString(item)).color(NamedTextColor.DARK_GREEN)
                        .append(Component.text(" mobs drop").color(NamedTextColor.YELLOW)));
                    components.add(Component.text(UnicodeCharacters.convertDoubleToPercent(multiplier)).color(NamedTextColor.DARK_GREEN)
                        .append(Component.text(" more XP.").color(NamedTextColor.YELLOW)));
                }
            }
            case "discounts" -> {
                switch (item) {
                    case "EMERALD" -> item = "TRADING";
                    case "ANVIL" -> item = "REPAIRING";
                    case "ENCHANTING_TABLE" -> item = "ENCHANTING";
                }
                components.add(Component.text(UnicodeCharacters.formatString(item)).color(NamedTextColor.DARK_GREEN)
                    .append(Component.text(" costs").color(NamedTextColor.YELLOW)));
                components.add(Component.text(UnicodeCharacters.convertToPercentAndFindDifference(multiplier)).color(NamedTextColor.DARK_GREEN)
                    .append(Component.text(" less.").color(NamedTextColor.YELLOW)));
            }
            case "mobDifficulty" -> {
                double dMultiplier = Double.parseDouble(multiplier);
                if (dMultiplier > 1.0) {
                    components.add(Component.text("The ").color(NamedTextColor.YELLOW)
                        .append(Component.text(UnicodeCharacters.formatString(item)).color(NamedTextColor.DARK_RED)
                            .append(Component.text(" mob is").color(NamedTextColor.YELLOW))));
                    components.add(Component.text(UnicodeCharacters.convertDoubleToPercent(multiplier)).color(NamedTextColor.DARK_RED)
                        .append(Component.text(" stronger.").color(NamedTextColor.YELLOW)));
                } else {
                    components.add(Component.text("The ").color(NamedTextColor.YELLOW)
                        .append(Component.text(UnicodeCharacters.formatString(item)).color(NamedTextColor.DARK_GREEN)
                            .append(Component.text(" mob is").color(NamedTextColor.YELLOW))));
                    components.add(Component.text(UnicodeCharacters.convertToPercentAndFindDifference(multiplier)).color(NamedTextColor.DARK_GREEN)
                        .append(Component.text(" weaker.").color(NamedTextColor.YELLOW)));
                }
            }
        }

        if (components.isEmpty()) {
            components.add(Component.text("unknown"));
        }

        return components;
    }
    public static boolean isValidMaterial(String item) {
        try {
            Material.valueOf(item);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidEntity(String item) {
        try {
            EntityType.valueOf(item);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static EntityType[] passiveMobs = {
        EntityType.CAT,
        EntityType.CHICKEN,
        EntityType.COD,
        EntityType.COW,
        EntityType.DONKEY,
        EntityType.GLOW_SQUID,
        EntityType.HORSE,
        EntityType.MUSHROOM_COW,
        EntityType.MULE,
        EntityType.OCELOT,
        EntityType.PARROT,
        EntityType.PIG,
        EntityType.PUFFERFISH,
        EntityType.RABBIT,
        EntityType.SALMON,
        EntityType.SHEEP,
        EntityType.SKELETON_HORSE,
        EntityType.SNOWMAN,
        EntityType.SQUID,
        EntityType.STRIDER,
        EntityType.TROPICAL_FISH,
        EntityType.TURTLE
    };

    public static EntityType[] neutralMobs = {
        EntityType.CAVE_SPIDER,
        EntityType.DOLPHIN,
        EntityType.DROWNED,
        EntityType.ENDERMAN,
        EntityType.IRON_GOLEM,
        EntityType.LLAMA,
        EntityType.PANDA,
        EntityType.PIGLIN,
        EntityType.POLAR_BEAR,
        EntityType.SPIDER,
        EntityType.TRADER_LLAMA,
        EntityType.ZOMBIFIED_PIGLIN
    };

    public static EntityType[] hostileMobs = {
        EntityType.BLAZE,
        EntityType.CREEPER,
        EntityType.ELDER_GUARDIAN,
        EntityType.EVOKER,
        EntityType.GHAST,
        EntityType.GUARDIAN,
        EntityType.HOGLIN,
        EntityType.HUSK,
        EntityType.MAGMA_CUBE,
        EntityType.PIGLIN_BRUTE,
        EntityType.PILLAGER,
        EntityType.SHULKER,
        EntityType.SKELETON,
        EntityType.SLIME,
        EntityType.STRAY,
        EntityType.VINDICATOR,
        EntityType.WITCH,
        EntityType.WITHER_SKELETON,
        EntityType.ZOGLIN,
        EntityType.ZOMBIE,
        EntityType.ZOMBIE_VILLAGER
    };

    public static Material[] oreDropBlockTypes = {
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.REDSTONE_ORE,
        Material.GOLD_ORE,
        Material.LAPIS_ORE,
        Material.EMERALD_ORE,
        Material.DIAMOND_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.ANCIENT_DEBRIS
    };

    public static Material[] xpDropBlockTypes = {
        Material.DIAMOND_ORE,
        Material.ANCIENT_DEBRIS,
        Material.SPAWNER,
        Material.SCULK,
    };

    public static Material[] overworldOres = {
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.COPPER_ORE,
        Material.REDSTONE_ORE,
        Material.GOLD_ORE,
        Material.LAPIS_ORE,
        Material.EMERALD_ORE,
        Material.DIAMOND_ORE,
    };

    public static Material[] netherOres = {
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE
    };

    public static Material[] xpDropActivities = {
        Material.EMERALD,
        Material.FISHING_ROD,
        Material.WHEAT,
        Material.FURNACE,
        Material.GRINDSTONE,
    };

    public static String[] xpDropMobTypes = {
        "PASSIVE",
        "NEUTRAL",
        "HOSTILE"
    };

    public static Material[] discounts = {
        Material.ENCHANTING_TABLE,
        Material.ANVIL,
        Material.EMERALD
    };
}
