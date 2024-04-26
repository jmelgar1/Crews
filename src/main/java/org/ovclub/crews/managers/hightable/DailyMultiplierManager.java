package org.ovclub.crews.managers.hightable;

import org.ovclub.crews.object.hightable.WeightedRandomMultiplier;

import java.util.ArrayList;
import java.util.List;

public class DailyMultiplierManager {
    private List<Double> dailyMultipliers;
    private final WeightedRandomMultiplier weightedRandom;

    public DailyMultiplierManager() {
        this.weightedRandom = new WeightedRandomMultiplier();
        initializeWeights();
        generateDailyMultipliers();
    }

    private void initializeWeights() {
        for (double i = 1.01; i <= 2.0; i += 0.01) {
            double weight = 1 / i;
            weightedRandom.add(weight, i);
        }
    }

    public void generateDailyMultipliers() {
        dailyMultipliers = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // Generate 5 multipliers per day CHANGE THIS
            dailyMultipliers.add(weightedRandom.next());
        }
    }

    public List<Double> getDailyMultipliers() {
        return dailyMultipliers;
    }
}

