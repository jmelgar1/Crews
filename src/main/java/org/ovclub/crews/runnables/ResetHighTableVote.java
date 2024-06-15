package org.ovclub.crews.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.utilities.HightableUtility;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ResetHighTableVote extends BukkitRunnable {

    private final Crews plugin;

    public ResetHighTableVote(Crews plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        if (now.toLocalTime().getHour() == LocalTime.MIDNIGHT.getHour() && now.toLocalTime().getMinute() == LocalTime.MIDNIGHT.getMinute()) {
            HightableUtility.executeMidnightTasks(plugin);
        }
        // Log every hour when this runs, can be removed if not needed
        System.out.println("Checked at: " + now);
    }
}
