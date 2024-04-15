package org.ovclub.crews.object.skirmish;

import org.ovclub.crews.object.Crew;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SkirmishQueue {
    private final List<SkirmishQueueItem> queue = new LinkedList<>();

    public synchronized void addToQueue(SkirmishQueueItem crewQueue) {
        queue.add(crewQueue);
    }
    public synchronized SkirmishQueueItem removeFromQueue() {
        if (!queue.isEmpty()) {
            return queue.remove(0);
        }
        return null;
    }
    public synchronized void removeFromQueue(Crew crew) {
        if (!queue.isEmpty()) {
            queue.removeIf(item -> item.getCrew().equals(crew));
        }
    }

    public synchronized void removeFromQueue(SkirmishQueueItem queueItem) {
        if (!queue.isEmpty()) {
            queue.removeIf(item -> item.equals(queueItem));
        }
    }
    public synchronized int getQueuePosition(SkirmishQueueItem crew) {
        return queue.indexOf(crew) + 1;
    }
    public synchronized boolean isInQueue(Crew crew) {
        for (SkirmishQueueItem item : queue) {
            if (item.getCrew().equals(crew)) {
                return true;
            }
        }
        return false;
    }
    public synchronized int size() {
        return queue.size();
    }
    public synchronized List<SkirmishQueueItem> getAllItemsInQueue() {
        return new ArrayList<>(queue);
    }
}
