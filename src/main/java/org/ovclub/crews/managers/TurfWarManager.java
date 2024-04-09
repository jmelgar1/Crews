package org.ovclub.crews.managers;

import org.ovclub.crews.object.turfwar.TurfWar;
import org.ovclub.crews.object.turfwar.TurfWarQueue;
import org.ovclub.crews.object.turfwar.TurfWarQueueItem;

import java.util.HashMap;
import java.util.Map;

public class TurfWarManager {
    private final TurfWarQueue queue = new TurfWarQueue();
    private final Map<TurfWarQueueItem, TurfWar> activeWars = new HashMap<>();

    public void queueCrew(TurfWarQueueItem crewQueue) {
        queue.addToQueue(crewQueue);
        checkForMatches();
    }

    public void checkForMatches() {
        while (queue.size() >= 2) {
            TurfWarQueueItem crew1 = queue.removeFromQueue();
            TurfWarQueueItem crew2 = queue.removeFromQueue();
            startTurfWar(crew1, crew2);
        }
    }

    private void startTurfWar(TurfWarQueueItem crew1, TurfWarQueueItem crew2) {
        TurfWar war = new TurfWar(crew1, crew2);
        activeWars.put(crew1, war);
        activeWars.put(crew2, war);
    }

    public TurfWarQueue getQueue() {
        return this.queue;
    }
    // Methods to handle end of a turf war, cleanup, and rewards
}
