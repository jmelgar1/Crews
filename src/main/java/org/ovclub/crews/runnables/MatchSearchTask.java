package org.ovclub.crews.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.skirmish.SkirmishTeam;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;

import java.util.List;

public class MatchSearchTask extends BukkitRunnable {

    private final Crews plugin;
    public MatchSearchTask(Crews plugin) {this.plugin = plugin;}

    @Override
    public void run() {
        if (plugin.getSkirmishManager().getQueue().size() > 0) {
            List<SkirmishTeam> queueList = plugin.getSkirmishManager().getQueue().getAllItemsInQueue();
            List<SkirmishMatchup> matchups = plugin.getSkirmishManager().getPotentialMatchups();
            for (SkirmishTeam item : queueList) {
                boolean isPaired = false;
                if (!matchups.isEmpty()) {
                    for (SkirmishMatchup pair : matchups) {
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
