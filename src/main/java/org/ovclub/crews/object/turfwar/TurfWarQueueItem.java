package org.ovclub.crews.object.turfwar;

import org.ovclub.crews.object.Crew;

import java.util.ArrayList;

public class TurfWarQueueItem {
    private Crew crew;
    private ArrayList<String> players;

    public Crew getCrew() {
        return this.crew;
    }

    public ArrayList<String> getPlayers() { return this.players; }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }
}
