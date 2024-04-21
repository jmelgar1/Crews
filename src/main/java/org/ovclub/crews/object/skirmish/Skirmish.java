package org.ovclub.crews.object.skirmish;

public class Skirmish {
    private SkirmishMatchup matchup;
    private int blueTeamScore;
    private int redTeamScore;

    // Constructor
    public Skirmish(SkirmishMatchup matchup) {
        this.matchup = matchup;
        this.blueTeamScore = 0;
        this.redTeamScore = 0;
    }

    public SkirmishMatchup getMatchup() { return this.matchup; }

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

}
