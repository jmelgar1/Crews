package org.ovclub.crews.object.skirmish;

import java.util.HashMap;
import java.util.Map;

public class Rating {
    private final double ratingA;
    private final double ratingB;
    private final int result;
    private final Map<Integer, Integer> kFactors;

    public Rating(double ratingA, double ratingB, int result, Map<Integer, Integer> kFactors) {
        this.ratingA = ratingA;
        this.ratingB = ratingB;
        this.result = result;
        this.kFactors = new HashMap<>(kFactors);
    }

    public Map<String, Double> getNewRatings() {
        double expectedA = 1.0 / (1.0 + Math.pow(10.0, (ratingB - ratingA) / 400.0));
        double expectedB = 1 - expectedA;
        int kA = getKFactor(ratingA);
        int kB = getKFactor(ratingB);

        double changeA = 0;
        double changeB = 0;
        switch (result) {
            case 1 -> { // WIN
                changeA = kA * (1 - expectedA);
                changeB = -kB * expectedB;
            }
            case -1 -> { // LOSS
                changeA = -kA * expectedA;
                changeB = kB * (1 - expectedB);
            }
            case 0 -> { // DRAW
                changeA = kA * (0.5 - expectedA);
                changeB = kB * (0.5 - expectedB);
            }
        }

        // Update the ratings and ensure they don't go below 0
        double newRatingA = Math.max(ratingA + changeA, 0);
        double newRatingB = Math.max(ratingB + changeB, 0);

        Map<String, Double> results = new HashMap<>();
        results.put("a", newRatingA);
        results.put("b", newRatingB);
        return results;
    }

        private int getKFactor(double rating) {
            return kFactors.entrySet().stream()
                .filter(entry -> rating < entry.getKey())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(32);
    }
}
