package org.ovclub.crews.object.turfwar;

import org.ovclub.crews.object.Crew;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TurfWarQueue {
    private final List<TurfWarQueueItem> queue = new LinkedList<>();

    public synchronized void addToQueue(TurfWarQueueItem crewQueue) {
        queue.add(crewQueue);
    }
    public synchronized TurfWarQueueItem removeFromQueue() {
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

    public synchronized void removeFromQueue(TurfWarQueueItem queueItem) {
        if (!queue.isEmpty()) {
            queue.removeIf(item -> item.equals(queueItem));
        }
    }
    public synchronized int getQueuePosition(TurfWarQueueItem crew) {
        return queue.indexOf(crew) + 1;
    }
    public synchronized boolean isInQueue(Crew crew) {
        for (TurfWarQueueItem item : queue) {
            if (item.getCrew().equals(crew)) {
                return true;
            }
        }
        return false;
    }
    public synchronized int size() {
        return queue.size();
    }
    public synchronized List<TurfWarQueueItem> getAllItemsInQueue() {
        return new ArrayList<>(queue);
    }
}
