package org.ovclub.crews.managers.hightable;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.ovclub.crews.object.hightable.WeightedRandomMultiplier;
import org.ovclub.crews.utilities.HightableUtility;

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
    private Map<String, Double> mobXPDropRates;

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
        passiveMobDropRates = new HashMap<>();

        for (EntityType mobType : HightableUtility.passiveMobs) {
            double multiplier = dropRates.next();
            passiveMobDropRates.put(mobType, multiplier);
        }
    }

    public void generateNeutralMobDailyMultipliers() {
        neutralMobDropRates = new HashMap<>();

        for (EntityType mobType : HightableUtility.neutralMobs) {
            double multiplier = dropRates.next();
            neutralMobDropRates.put(mobType, multiplier);
        }
    }

    public void generateHostileMobDailyMultipliers() {
        hostileMobDropRates = new HashMap<>();

        for (EntityType mobType : HightableUtility.hostileMobs) {
            double multiplier = dropRates.next();
            hostileMobDropRates.put(mobType, multiplier);
        }
    }

    public void generateOreDropMultiplier() {
        oreDropRates = new LinkedHashMap<>();

        for (Material blockType : HightableUtility.oreDropBlockTypes) {
            double multiplier = dropRates.next();
            oreDropRates.put(blockType, multiplier);
        }
    }

    public void generateBlockXPDropRates() {
        oresAndBlocksXPDropRates = new LinkedHashMap<>();

        for (Material blockType : HightableUtility.xpDropBlockTypes) {
            double multiplier = dropRates.next();
            oresAndBlocksXPDropRates.put(blockType, multiplier);
        }
    }

    public void generateMobDifficultyMultipliers() {
        mobDifficultyRates = new HashMap<>();

        for (EntityType mobType : HightableUtility.hostileMobs) {
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
        mobXPDropRates = new HashMap<>();

        for (String mobType : HightableUtility.xpDropMobTypes) {
            double multiplier = dropRates.next();
            mobXPDropRates.put(mobType, multiplier);
        }
    }

    public void generateDiscounts() {
        discountMultipliers = new HashMap<>();

        for (Material itemType : HightableUtility.discounts) {
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
    public Map<String, Double> getMobXPDropRates() {return this.mobXPDropRates;}
    public Map<Material, Double> getDiscountMultipliers() {return this.discountMultipliers;}
    public Map<EntityType, Double> getMobDifficultyRates() {return mobDifficultyRates;}


}

