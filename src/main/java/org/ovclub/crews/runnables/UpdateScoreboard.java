package org.ovclub.crews.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;
import org.ovclub.crews.object.skirmish.SkirmishTeam;

import java.util.List;

public class UpdateScoreboard extends BukkitRunnable {

    private final Crews plugin;
    public UpdateScoreboard(Crews plugin) {this.plugin = plugin;}

    @Override
    public void run() {
        List<SkirmishTeam> queueList = plugin.getSkirmishManager().getQueue().getAllItemsInQueue();
        List<SkirmishMatchup> matchups = plugin.getSkirmishManager().getPotentialMatchups();

    }
}
