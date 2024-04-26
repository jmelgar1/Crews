package org.ovclub.crews.object.skirmish;

import org.ovclub.crews.object.Crew;

import java.util.ArrayList;

public class SkirmishTeam {
    private Crew crew;
    private ArrayList<String> players;
    private boolean inConfirmation;

    public Crew getCrew() {
        return this.crew;
    }

    public ArrayList<String> getPlayers() { return this.players; }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public boolean getInConfirmation() {
        return inConfirmation;
    }

    public void setInConfirmation(boolean value) {
        this.inConfirmation = value;
    }
}
