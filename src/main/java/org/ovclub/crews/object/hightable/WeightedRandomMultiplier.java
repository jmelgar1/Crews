package org.ovclub.crews.object.hightable;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedRandomMultiplier {
    private final NavigableMap<Double, Double> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public WeightedRandomMultiplier() {
        this.random = new Random();
    }

    public void add(double weight, double result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public double next() {
        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }
}
