package org.ovclub.crews.object.skirmish;

import java.util.ArrayList;

public class SkirmishMatchup {
    private final SkirmishTeam a_team;
    private final SkirmishTeam b_team;
    private final ArrayList<String> playersInConfirmation;

    public SkirmishMatchup(SkirmishTeam item1, SkirmishTeam item2, ArrayList<String> playersInConfirmation) {
        this.a_team = item1;
        this.b_team = item2;
        this.playersInConfirmation = playersInConfirmation;
    }

    public boolean contains(SkirmishTeam item) {
        return item.equals(a_team) || item.equals(b_team);
    }
    public SkirmishTeam getATeam() {
        return this.a_team;
    }
    public SkirmishTeam getBTeam() {
        return this.b_team;
    }
    public ArrayList<String> getPlayersInConfirmation() {
        return this.playersInConfirmation;
    }
    public ArrayList<String> getParticipants() {
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(a_team.getPlayers());
        participants.addAll(b_team.getPlayers());
        return participants;
    }
}
