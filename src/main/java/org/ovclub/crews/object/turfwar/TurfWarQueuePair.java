package org.ovclub.crews.object.turfwar;

import java.util.ArrayList;

public class TurfWarQueuePair {
    private final TurfWarQueueItem item1;
    private final TurfWarQueueItem item2;
    private final ArrayList<String> playersInConfirmation;

    public TurfWarQueuePair(TurfWarQueueItem item1, TurfWarQueueItem item2, ArrayList<String> playersInConfirmation) {
        this.item1 = item1;
        this.item2 = item2;
        this.playersInConfirmation = playersInConfirmation;
    }

    public boolean contains(TurfWarQueueItem item) {
        return item.equals(item1) || item.equals(item2);
    }
    public TurfWarQueueItem getItem1() {
        return this.item1;
    }
    public TurfWarQueueItem getItem2() {
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
