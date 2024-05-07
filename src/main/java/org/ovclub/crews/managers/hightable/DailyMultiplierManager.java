package org.ovclub.crews.managers.hightable;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.ovclub.crews.object.hightable.WeightedRandomMultiplier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DailyMultiplierManager {
    /*MOB DROPS*/
    private Map<EntityType, Double> passiveMobDropRates;
    private Map<EntityType, Double> neutralMobDropRates;
    private Map<EntityType, Double> hostileMobDropRates;

    /*ORE DROPS*/
    private Map<Material, Double> oreDropRates;

    /*XP DROPS*/
    private Map<Material, Double> oresAndBlocksXPDropRates;
    private Map<Material, Double> activitiesXPDropRates;
    private Map<EntityType, Double> mobXPDropRates;

    /*DISCOUNTS*/
    private Map<Material, Double> discountMultipliers;

    /*MOB DIFFICULT*/
    private Map<EntityType, Double> mobDifficultyRates;

    private final WeightedRandomMultiplier dropRates;
    private final WeightedRandomMultiplier discountRates;
    private final WeightedRandomMultiplier difficultyRates;

    public DailyMultiplierManager() {
        this.dropRates = new WeightedRandomMultiplier();
        this.discountRates = new WeightedRandomMultiplier();
        this.difficultyRates = new WeightedRandomMultiplier();
        initializeWeights();
        initializeDiscounts();
        initializeDifficultWeight();
        generateDailyMultipliers();
    }

    private void initializeWeights() {
        for (double i = 1.01; i <= 2.0; i += 0.01) {
            double weight = Math.exp(-(i - 1.0) * 4.3);
            dropRates.add(weight, i);
        }
    }

    private void initializeDiscounts() {
        for (double i = 0.50; i < 1.00; i += 0.01) {
            double weight = Math.exp(i - 0.50) * 2.3;
            discountRates.add(weight, i);
        }
    }

    private void initializeDifficultWeight() {
        for (double i = 0.50; i <= 2.0; i += 0.01) {
            double distance = (i - 1.25);
            double weight = Math.exp(-Math.pow(distance, 2) / (2 * Math.pow(0.25, 2)));
            difficultyRates.add(weight, i);
        }
    }

    private void generateDailyMultipliers() {
        generateHostileMobDailyMultipliers();
        generateNeutralMobDailyMultipliers();
        generatePassiveMobDailyMultipliers();
        generateOreDropMultiplier();
        generateBlockXPDropRates();
        generateMobDifficultyMultipliers();
        generateXPDrops();
        generateMobXPDrops();
        generateDiscounts();
    }

    public void generatePassiveMobDailyMultipliers() {
        EntityType[] mobTypes = {
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

        passiveMobDropRates = new HashMap<>();

        for (EntityType mobType : mobTypes) {
            double multiplier = dropRates.next();
            passiveMobDropRates.put(mobType, multiplier);
        }
    }

    public void generateNeutralMobDailyMultipliers() {
        EntityType[] mobTypes = {
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

        neutralMobDropRates = new HashMap<>();

        for (EntityType mobType : mobTypes) {
            double multiplier = dropRates.next();
            neutralMobDropRates.put(mobType, multiplier);
        }
    }

    public void generateHostileMobDailyMultipliers() {
        EntityType[] mobTypes = {
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

        hostileMobDropRates = new HashMap<>();

        for (EntityType mobType : mobTypes) {
            double multiplier = dropRates.next();
            hostileMobDropRates.put(mobType, multiplier);
        }
    }

    public void generateOreDropMultiplier() {
        Material[] blockTypes = {
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

        oreDropRates = new LinkedHashMap<>();

        for (Material blockType : blockTypes) {
            double multiplier = dropRates.next();
            oreDropRates.put(blockType, multiplier);
        }
    }

    public void generateBlockXPDropRates() {
        Material[] blockTypes = {
            Material.DIAMOND_ORE,
            Material.ANCIENT_DEBRIS,
            Material.SPAWNER,
            Material.SCULK,
        };

        oresAndBlocksXPDropRates = new LinkedHashMap<>();

        for (Material blockType : blockTypes) {
            double multiplier = dropRates.next();
            oresAndBlocksXPDropRates.put(blockType, multiplier);
        }
    }

    public void generateMobDifficultyMultipliers() {
        EntityType[] mobTypes = {
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

        mobDifficultyRates = new HashMap<>();

        for (EntityType mobType : mobTypes) {
            double multiplier = difficultyRates.next();
            mobDifficultyRates.put(mobType, multiplier);
        }
    }

    public void generateXPDrops() {
        Material[] itemTypes = {
            Material.EMERALD,
            Material.FISHING_ROD,
            Material.WHEAT,
            Material.FURNACE,
            Material.GRINDSTONE,
        };

        activitiesXPDropRates = new HashMap<>();

        for (Material itemType : itemTypes) {
            double multiplier = dropRates.next();
            activitiesXPDropRates.put(itemType, multiplier);
        }
    }

    public void generateMobXPDrops() {
        EntityType[] mobTypes = {
            EntityType.COW,
            EntityType.IRON_GOLEM,
            EntityType.CREEPER
        };

        mobXPDropRates = new HashMap<>();

        for (EntityType mobType : mobTypes) {
            double multiplier = dropRates.next();
            mobXPDropRates.put(mobType, multiplier);
        }
    }

    public void generateDiscounts() {
        Material[] itemTypes = {
            Material.ENCHANTING_TABLE,
            Material.ANVIL,
            Material.EMERALD
        };

        discountMultipliers = new HashMap<>();

        for (Material itemType : itemTypes) {
            double multiplier = discountRates.next();
            discountMultipliers.put(itemType, multiplier);
        }
    }

    public Map<EntityType, Double> getPassiveMobDropRates() {return passiveMobDropRates;}
    public Map<EntityType, Double> getNeutralMobDropRates() {return neutralMobDropRates;}
    public Map<EntityType, Double> getHostileMobDropRates() {return hostileMobDropRates;}
    public Map<Material, Double> getOreDropRates() {return oreDropRates;}
    public Map<Material, Double> getOresAndBlocksXPDropRates() {return oresAndBlocksXPDropRates;}
    public Map<Material, Double> getActivitiesXPDrops() {return this.activitiesXPDropRates;}
    public Map<EntityType, Double> getMobXPDropRates() {return this.mobXPDropRates;}
    public Map<Material, Double> getDiscountMultipliers() {return this.discountMultipliers;}
    public Map<EntityType, Double> getMobDifficultyRates() {return mobDifficultyRates;}


}

