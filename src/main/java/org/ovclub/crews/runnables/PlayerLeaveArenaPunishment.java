package org.ovclub.crews.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.object.skirmish.Arena;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;
import org.ovclub.crews.object.skirmish.SkirmishTeam;

import java.util.ArrayList;

public class PlayerLeaveArenaPunishment extends BukkitRunnable {
    private final Player player;
    private int secondsLeft;
    private final RunnableManager runnableManager;
    private final SkirmishMatchup matchup;
    private final ArenaManager arenaManager;

    public PlayerLeaveArenaPunishment(RunnableManager runnableManager, Player player, SkirmishMatchup matchup, ArenaManager arenaManager, int seconds) {
        this.runnableManager = runnableManager;
        this.player = player;
        this.secondsLeft = seconds;
        this.matchup = matchup;
        this.arenaManager = arenaManager;
    }

    @Override
    public void run() {
        Arena arena = arenaManager.getArenaByPlayer(player);
        System.out.println(arena);
        if (secondsLeft <= 0) {
            if(arena != null) {
                matchup.broadcast(ConfigManager.PLAYER_BANNED
                    .replaceText(builder -> builder.matchLiteral("{player}").replacement(player.getName())));
                this.cancel();
                SkirmishTeam pTeam = arena.getPlayerTeam(player);
                ArrayList<String> newPlayers = pTeam.getPlayers();
                newPlayers.remove(String.valueOf(player.getUniqueId()));
                pTeam.setPlayers(newPlayers);
                if(pTeam.getPlayers().size() == 0) {
                    runnableManager.cancelPlayerArenaLeaveTimer(player);
                    runnableManager.cancelArenaCountdownTimer(arena);
                    arena.determineOutcome();
                    arena.removePlayerScoreboard();
                    arenaManager.teleportPlayersToReturnPoints(arena);
                    arenaManager.removeArena(arena);
                }
            }
            return;
        } else {
            if(arena == null) {
                runnableManager.cancelPlayerArenaLeaveTimer(player);
            }
        }
        if (secondsLeft == 120 || secondsLeft == 60 || secondsLeft == 30 ||
            secondsLeft == 20 || secondsLeft == 10 || secondsLeft == 5) {
            matchup.broadcast(ConfigManager.PLAYER_WILL_BE_BANNED
                .replaceText(builder -> builder.matchLiteral("{player}").replacement(player.getName()))
                .replaceText(builder -> builder.matchLiteral("{seconds}").replacement(String.valueOf(secondsLeft))));
        }
        secondsLeft--;
    }
}
