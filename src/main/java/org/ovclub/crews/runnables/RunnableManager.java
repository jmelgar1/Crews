package org.ovclub.crews.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.object.skirmish.Arena;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;

import java.util.HashMap;
import java.util.UUID;

public class RunnableManager {

    private final HashMap<UUID, Integer> taskMap = new HashMap<>();

    private final Crews plugin;
    public RunnableManager(Crews plugin) {
        this.plugin = plugin;
    }

    public void startPlayerArenaLeaveTimer(Player player, SkirmishMatchup matchup, ArenaManager manager, int seconds) {
        int taskId = new PlayerLeaveArenaPunishment(this, player, matchup, manager, seconds).runTaskTimer(plugin, 0L, 20L).getTaskId();
        taskMap.put(player.getUniqueId(), taskId);
    }

    public void cancelPlayerArenaLeaveTimer(Player player) {
        if (taskMap.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(taskMap.get(player.getUniqueId()));
            taskMap.remove(player.getUniqueId());
        }
    }

    public void startArenaCountdownTimer(Arena arena, ArenaManager arenaManager) {
        int taskId = new CountdownTimer(arena, arenaManager).runTaskTimer(plugin, 0L, 20L).getTaskId();
        taskMap.put(arena.getArenaId(), taskId);
    }

    public void cancelArenaCountdownTimer(Arena arena) {
        if (taskMap.containsKey(arena.getArenaId())) {
            Bukkit.getScheduler().cancelTask(taskMap.get(arena.getArenaId()));
            taskMap.remove(arena.getArenaId());
        }
    }
}
