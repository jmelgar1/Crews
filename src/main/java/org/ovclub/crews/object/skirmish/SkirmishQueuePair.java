package org.ovclub.crews.object.skirmish;

import java.util.ArrayList;

public class SkirmishQueuePair {
    private final SkirmishQueueItem item1;
    private final SkirmishQueueItem item2;
    private final ArrayList<String> playersInConfirmation;

    public SkirmishQueuePair(SkirmishQueueItem item1, SkirmishQueueItem item2, ArrayList<String> playersInConfirmation) {
        this.item1 = item1;
        this.item2 = item2;
        this.playersInConfirmation = playersInConfirmation;
    }

    public boolean contains(SkirmishQueueItem item) {
        return item.equals(item1) || item.equals(item2);
    }
    public SkirmishQueueItem getItem1() {
        return this.item1;
    }
    public SkirmishQueueItem getItem2() {
        return this.item2;
    }
    public ArrayList<String> getPlayersInConfirmation() {
        return this.playersInConfirmation;
    }
    public ArrayList<String> getParticipants() {
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(item1.getPlayers());
        participants.addAll(item2.getPlayers());
        return participants;
    }
}
