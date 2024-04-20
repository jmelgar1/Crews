package org.ovclub.crews.utilities;

import org.ovclub.crews.object.skirmish.SkirmishTeam;

public class RatingUtilities {
    public static void updateRatings(SkirmishTeam teamA, SkirmishTeam teamB, MatchResult result) {
        int ratingA = teamA.getCrew().getRating();
        int ratingB = teamB.getCrew().getRating();

        double expectedA = calculateExpectedScore(ratingA, ratingB);
        double expectedB = calculateExpectedScore(ratingB, ratingA);

        int kA = calculateDynamicK(teamA);  // Get dynamic K-factor for team A
        int kB = calculateDynamicK(teamB);  // Get dynamic K-factor for team B

        int changeA = 0, changeB = 0;
        switch (result) {
            case WIN_A -> {
                changeA = (int) (kA * (1 - expectedA));
                changeB = -(int) (kB * expectedB * 0.5); // Halve the rating loss
            }
            case WIN_B -> {
                changeA = -(int) (kA * expectedA * 0.5); // Halve the rating loss
                changeB = (int) (kB * (1 - expectedB));
            }
            case DRAW -> {
                changeA = (int) (kA * (0.5 - expectedA));
                changeB = (int) (kB * (0.5 - expectedB));
            }
            default -> throw new IllegalArgumentException("Unknown match result");
        }

        // Apply changes ensuring ratings do not drop below zero or exceed opponent's total rating
        teamA.getCrew().setRating(safeRatingUpdate(ratingA, ratingB, changeA));
        teamB.getCrew().setRating(safeRatingUpdate(ratingB, ratingA, changeB));
    }

    private static int calculateDynamicK(SkirmishTeam team) {
        int gamesPlayed = team.getCrew().getGamesPlayed();
        int rating = team.getCrew().getRating();
        int kFactor;

        if (gamesPlayed < 30) {
            kFactor = 300;
        } else if (rating < 3500) {
            kFactor = 150;
        } else {
            kFactor = 75;
        }
        return kFactor;
    }

    private static double calculateExpectedScore(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10.0, (ratingB - ratingA) / 400.0));
    }

    private static int safeRatingUpdate(int currentRating, int opponentRating, int change) {
        if (currentRating == 0 && change < 0) {
            // If current rating is 0 and the change is negative, ensure no further decrease.
            return 0;
        }

        int newRating = currentRating + change;
        newRating = Math.max(newRating, 0); // Ensure that the new rating does not drop below zero

        if (change > 0) {  // Check for gains in rating
            // Cap the maximum gain to not exceed the opponent's rating
            newRating = Math.min(newRating, currentRating + opponentRating);
        }

        return newRating;
    }
}
