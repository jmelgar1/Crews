package org.ovclub.crews.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.hightable.DailyMultiplierManager;
import org.ovclub.crews.utilities.HightableUtility;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ResetHighTableVote extends BukkitRunnable {

    private final Crews plugin;
    private final DailyMultiplierManager manager;
    public ResetHighTableVote(Crews plugin, DailyMultiplierManager manager) {
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime nextHour = now.withMinute(0).withSecond(0).plusHours(1);
        long initialDelay = Duration.between(now, nextHour).getSeconds() * 20;
        long period = 20L * 60 * 60;

        new BukkitRunnable() {
            @Override
            public void run() {
                ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
                LocalTime targetTime = LocalTime.MIDNIGHT;

                if (currentTime.getHour() == targetTime.getHour() && currentTime.getMinute() == targetTime.getMinute()) {
                    HightableUtility.updateHighTable(plugin.getData().generateLeaderboardJson());
                    HightableUtility.saveMultipliers(manager);
                    plugin.getHightableFile().loadHightable();
                }
            }
        }.runTaskTimer(plugin, initialDelay, period);
    }
}
