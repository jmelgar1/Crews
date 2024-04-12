package org.ovclub.crews.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.turfwar.TurfWarQueueItem;
import org.ovclub.crews.object.turfwar.TurfWarQueuePair;

import java.util.List;

public class MatchSearchTask extends BukkitRunnable {

    private final Crews plugin;
    public MatchSearchTask(Crews plugin) {this.plugin = plugin;}

    @Override
    public void run() {
        if (plugin.getTurfWarManager().getQueue().size() > 0) {
            List<TurfWarQueueItem> queueList = plugin.getTurfWarManager().getQueue().getAllItemsInQueue();
            List<TurfWarQueuePair> potentialMatchups = plugin.getTurfWarManager().getPotentialMatchups();

            for (TurfWarQueueItem item : queueList) {
                boolean isPaired = false;
                if (!potentialMatchups.isEmpty()) {
                    for (TurfWarQueuePair pair : potentialMatchups) {
                        if (pair.contains(item)) {
                            isPaired = true;
                            break;
                        }
                    }
                }
                if (!isPaired) {
                    item.getCrew().broadcast(ConfigManager.WAITING_FOR_MATCHUP);
                }
            }
        }
    }
}
