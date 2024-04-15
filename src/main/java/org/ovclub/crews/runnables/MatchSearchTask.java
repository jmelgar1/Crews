package org.ovclub.crews.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.skirmish.SkirmishQueueItem;
import org.ovclub.crews.object.skirmish.SkirmishQueuePair;

import java.util.List;

public class MatchSearchTask extends BukkitRunnable {

    private final Crews plugin;
    public MatchSearchTask(Crews plugin) {this.plugin = plugin;}

    @Override
    public void run() {
        if (plugin.getSkirmishManager().getQueue().size() > 0) {
            List<SkirmishQueueItem> queueList = plugin.getSkirmishManager().getQueue().getAllItemsInQueue();
            List<SkirmishQueuePair> potentialMatchups = plugin.getSkirmishManager().getPotentialMatchups();
            for (SkirmishQueueItem item : queueList) {
                boolean isPaired = false;
                if (!potentialMatchups.isEmpty()) {
                    for (SkirmishQueuePair pair : potentialMatchups) {
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
