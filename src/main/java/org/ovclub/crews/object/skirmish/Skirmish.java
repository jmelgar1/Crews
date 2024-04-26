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

    public int getATeamScore() {
        return blueTeamScore;
    }

    public void setATeamScore(int value) {
        this.blueTeamScore = value;
    }

    public void setBTeamScore(int value) {
        this.redTeamScore = value;
    }

    public void addToATeamScore(int value) {
        this.blueTeamScore+=value;
    }

    public void addToBTeamScore(int value) {
        this.redTeamScore+=value;
    }

    public int getBTeamScore() {
        return redTeamScore;
    }

}
