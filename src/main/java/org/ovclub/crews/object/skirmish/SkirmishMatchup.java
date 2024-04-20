package org.ovclub.crews.object.skirmish;

import java.util.ArrayList;

public class SkirmishMatchup {
    private final SkirmishTeam blueTeam;
    private final SkirmishTeam redTeam;
    private final ArrayList<String> playersInConfirmation;

    public SkirmishMatchup(SkirmishTeam item1, SkirmishTeam item2, ArrayList<String> playersInConfirmation) {
        this.blueTeam = item1;
        this.redTeam = item2;
        this.playersInConfirmation = playersInConfirmation;
    }

    public boolean contains(SkirmishTeam item) {
        return item.equals(blueTeam) || item.equals(redTeam);
    }
    public SkirmishTeam getBlueTeam() {
        return this.blueTeam;
    }
    public SkirmishTeam getRedTeam() {
        return this.redTeam;
    }
    public ArrayList<String> getPlayersInConfirmation() {
        return this.playersInConfirmation;
    }
    public ArrayList<String> getParticipants() {
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(blueTeam.getPlayers());
        participants.addAll(redTeam.getPlayers());
        return participants;
    }
}
