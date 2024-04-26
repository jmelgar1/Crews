package org.ovclub.crews.object.skirmish;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

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
    public void broadcast(Component message) {
        for(String sUUID : getParticipants()) {
            Player p = Bukkit.getPlayer(UUID.fromString(sUUID));
            if (p != null && p.isOnline()) {
                p.sendMessage(message);
            }
        }
    }
}
