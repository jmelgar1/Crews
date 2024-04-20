package org.ovclub.crews.object.skirmish;

public class Skirmish {
    private SkirmishMatchup matchup;
    private SkirmishState state;
    private int blueTeamScore;
    private int redTeamScore;

    // Constructor
    public Skirmish(SkirmishMatchup matchup) {
        this.matchup = matchup;
        this.state = SkirmishState.WAITING;
        this.blueTeamScore = 0;
        this.redTeamScore = 0;
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
    public void updateScore(SkirmishTeam crewQueue, int points) {
        if (crewQueue.getCrew().equals(matchup.getBlueTeam().getCrew())) {
            blueTeamScore += points;
        } else if (crewQueue.getCrew().equals(matchup.getRedTeam().getCrew())) {
            redTeamScore += points;
        }
        // maybe more logic to check if the match should end based on score
    }

    public SkirmishMatchup getMatchup() { return this.matchup; }

    public SkirmishState getState() {
        return state;
    }

    public int getBlueTeamScore() {
        return blueTeamScore;
    }

    public void setBlueTeamScore(int value) {
        this.blueTeamScore = value;
    }

    public void setRedTeamScore(int value) {
        this.redTeamScore = value;
    }

    public int getRedTeamScore() {
        return redTeamScore;
    }

    //add more methods here for match management, like time checks, player deaths, etc.
}
