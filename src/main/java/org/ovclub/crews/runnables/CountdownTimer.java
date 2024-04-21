package org.ovclub.crews.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.object.skirmish.Arena;

import java.util.UUID;

public class CountdownTimer extends BukkitRunnable {

    private final Arena arena;
    private final ArenaManager manager;
    public CountdownTimer(Arena arena, ArenaManager manager) {
        this.arena = arena;
        this.manager = manager;
    }

    @Override
    public void run() {
        int gameTime = arena.getGameTime()-1;
        for(String uuid : arena.getMatchup().getParticipants()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p != null && p.isOnline()) {
                arena.updateTimeOnScoreboard(p.getScoreboard(), gameTime);
            }
        }
        arena.setGameTime(gameTime);
        if (gameTime <= 0) {
            arena.determineOutcome();
            arena.removePlayerScoreboard();
            manager.teleportPlayersToReturnPoints(arena);
            manager.removeArena(arena);
            this.cancel();
        }
    }
}
