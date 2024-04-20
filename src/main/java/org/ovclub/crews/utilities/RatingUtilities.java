package org.ovclub.crews.utilities;

import org.ovclub.crews.object.skirmish.SkirmishTeam;

public class RatingUtilities {
    private static final int MAX_K = 500;
    private static final int MIN_K = 50;

    public static void updateRatings(SkirmishTeam winner, SkirmishTeam loser) {
        int winnerRating = winner.getCrew().getRating();
        int loserRating = loser.getCrew().getRating();

        double ratingDifference = Math.abs(winnerRating - loserRating);
        int dynamicK = calculateDynamicK(ratingDifference);

        double expectedWin = calculateExpectedScore(winnerRating, loserRating);
        double expectedLoss = calculateExpectedScore(loserRating, winnerRating);

        // Calculate the rating changes
        int winnerChange = (int) (dynamicK * (1 - expectedWin));
        int loserChange = (int) (dynamicK * expectedLoss);

        // Update the ratings
        winner.getCrew().setRating(winnerRating + winnerChange);
        loser.getCrew().setRating(loserRating - loserChange);
    }

    private static int calculateDynamicK(double ratingDifference) {
        double scaleFactor = Math.min(1, ratingDifference / 400.0);
        return (int) (MIN_K + (MAX_K - MIN_K) * scaleFactor);
    }

    private static double calculateExpectedScore(int ratingA, int ratingB) {
        return 1 / (1 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }
}
