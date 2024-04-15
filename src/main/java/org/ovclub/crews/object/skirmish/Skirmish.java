package org.ovclub.crews.object.skirmish;

public class Skirmish {
    private final SkirmishQueueItem crew1;
    private final SkirmishQueueItem crew2;
    private SkirmishState state;
    private int scoreCrew1;
    private int scoreCrew2;

    // Constructor
    public Skirmish(SkirmishQueueItem crew1, SkirmishQueueItem crew2) {
        this.crew1 = crew1;
        this.crew2 = crew2;
        this.state = SkirmishState.WAITING;
        this.scoreCrew1 = 0;
        this.scoreCrew2 = 0;
    }

    public void start() {
        this.state = SkirmishState.ACTIVE;
        // initialize the arena, teleport players, and any other setup
    }

    public void end() {
        this.state = SkirmishState.COMPLETED;
        // determine winner, handle rewards, clean up, etc.
    }

    // Update score for a crew
    public void updateScore(SkirmishQueueItem crewQueue, int points) {
        if (crewQueue.getCrew().equals(crew1.getCrew())) {
            scoreCrew1 += points;
        } else if (crewQueue.getCrew().equals(crew2.getCrew())) {
            scoreCrew2 += points;
        }
        // maybe more logic to check if the match should end based on score
    }
    public SkirmishQueueItem getCrewModel1() {
        return crew1;
    }

    public SkirmishQueueItem getCrewModel2() {
        return crew2;
    }

    public SkirmishState getState() {
        return state;
    }

    public int getScoreCrew1() {
        return scoreCrew1;
    }

    public int getScoreCrew2() {
        return scoreCrew2;
    }

    //add more methods here for match management, like time checks, player deaths, etc.
}
