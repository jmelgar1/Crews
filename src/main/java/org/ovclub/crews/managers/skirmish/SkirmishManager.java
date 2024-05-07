package org.ovclub.crews.managers.skirmish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.listeners.PlayerResponseListener;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.skirmish.*;
import org.ovclub.crews.runnables.CountdownTimer;
import org.ovclub.crews.utilities.SoundUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.*;

public class SkirmishManager {
    private final SkirmishQueue queue = new SkirmishQueue();
    private final List<SkirmishMatchup> potentialMatchups = new ArrayList<>();
    private HashMap<UUID, Integer> taskMap = new HashMap<>();

    private final Crews plugin;
    private final Map<UUID, PlayerResponseListener> responseListeners = new HashMap<>();

    public SkirmishManager(Crews plugin) {
        this.plugin = plugin;
    }

    public void queueCrew(SkirmishTeam crewQueue) {
        queue.addToQueue(crewQueue);
        createMatchupConfirmation();
    }
    public List<SkirmishMatchup> getPotentialMatchups() {
        return this.potentialMatchups;
    }
    public boolean allConfirmedForSkirmish(Crew crew) {
        for (SkirmishMatchup pair : potentialMatchups) {
            if (pair.getATeam().getCrew().equals(crew) ||
                pair.getBTeam().getCrew().equals(crew)) {
                return pair.getPlayersInConfirmation().isEmpty();
            }
        }
        return false;
    }
    public void generateFairMatchup(Crew crew) {
        SkirmishMatchup queuePair = getMatchupFromCrew(crew);
        if (queuePair == null) {
            return;
        }
        SkirmishTeam team1 = queuePair.getATeam();
        SkirmishTeam team2 = queuePair.getBTeam();

        int teamOneSize = team1.getPlayers().size();
        int teamTwoSize = team2.getPlayers().size();

        if (teamOneSize == teamTwoSize) {
            startSkirmish(team1, team2);
            return;
        }

        SkirmishTeam smallestTeam = teamOneSize < teamTwoSize ? team1 : team2;
        SkirmishTeam largestTeam = teamOneSize > teamTwoSize ? team1 : team2;

        for (String uuid : largestTeam.getPlayers()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p != null && p.isOnline()) {
                UnicodeCharacters.sendUnbalancedMessage(p, smallestTeam.getPlayers().size(), largestTeam.getPlayers().size(), false);
                SoundUtilities.playPingSound(p);
            }
        }

        String pUUID = smallestTeam.getPlayers().get(0);
        Player p = Bukkit.getPlayer(UUID.fromString(pUUID));
        if (p != null && p.isOnline()) {
            UnicodeCharacters.sendUnbalancedMessage(p, smallestTeam.getPlayers().size(), largestTeam.getPlayers().size(), true);
            SoundUtilities.playPingSound(p);

            PlayerResponseListener responseListener = new PlayerResponseListener(plugin, p.getUniqueId(), smallestTeam, largestTeam);
            Bukkit.getPluginManager().registerEvents(responseListener, plugin);
            responseListeners.put(p.getUniqueId(), responseListener);

            BukkitRunnable task = new BukkitRunnable() {
                private int secondsLeft = 30;

                @Override
                public void run() {
                    PlayerResponseListener listener = responseListeners.get(p.getUniqueId());
                    if (listener != null) {
                        if (!listener.isAwaitingResponse()) {
                            int selectedTeamSize = listener.getChosenTeamSize();
                            if (largestTeam.equals(team1)) {
                                setRandomizedTeamPlayers(team1, selectedTeamSize);
                            } else {
                                setRandomizedTeamPlayers(team2, selectedTeamSize);
                            }
                            startSkirmish(team1, team2);
                            cancel();
                        } else if (secondsLeft <= 0) {
                            responseListeners.remove(p.getUniqueId());
                            cancelMatchup(team1, team2, false);
                            AsyncPlayerChatEvent.getHandlerList().unregister(listener);
                            cancel();
                        } else {
                            if (secondsLeft % 10 == 0 || secondsLeft <= 5) {
                                p.sendMessage(ConfigManager.SECONDS_LEFT_TO_ACTION
                                    .replaceText(builder -> builder.matchLiteral("{seconds}").replacement(String.valueOf(secondsLeft)))
                                    .replaceText(builder -> builder.matchLiteral("{action}").replacement("choose your opponent's team size!")));
                            }
                        }
                        secondsLeft--;
                    } else {
                        cancel();
                    }
                }
            };
            task.runTaskTimer(plugin, 20L, 20L);
        }
    }

    public SkirmishMatchup getMatchupFromCrew(Crew crew) {
        for (SkirmishMatchup pair : potentialMatchups) {
            if (pair.getATeam().getCrew().equals(crew) || pair.getBTeam().getCrew().equals(crew)) {
                return pair;
            }
        }
        return null;
    }

    private void setRandomizedTeamPlayers(SkirmishTeam team, int selectedTeamSize) {
        ArrayList<String> players = team.getPlayers();
        Collections.shuffle(players);
        if (selectedTeamSize > players.size()) {
            selectedTeamSize = players.size();
        }
        ArrayList<String> selectedPlayers = new ArrayList<>(players.subList(0, selectedTeamSize));
        team.setPlayers(selectedPlayers);
    }

    public void createMatchupConfirmation() {
        List<SkirmishTeam> totalQueue = queue.getAllItemsInQueue();
        SkirmishTeam firstItem = null;
        SkirmishTeam secondItem = null;

        for (SkirmishTeam item : totalQueue) {
            boolean isAlreadyMatched = false;
            for (SkirmishMatchup pair : potentialMatchups) {
                if (pair.contains(item)) {
                    isAlreadyMatched = true;
                    break;
                }
            }
            if (!isAlreadyMatched) {
                if (firstItem == null) {
                    firstItem = item;
                } else {
                    secondItem = item;
                    break;
                }
            }
        }
        if (firstItem != null && secondItem != null) {
            ArrayList<String> playerList = new ArrayList<>();
            playerList.addAll(firstItem.getPlayers());
            playerList.addAll(secondItem.getPlayers());
            SkirmishMatchup matchup = new SkirmishMatchup(firstItem, secondItem, playerList);
            potentialMatchups.add(matchup);

            BukkitRunnable task = new BukkitRunnable() {
                private int count = 0;
                final ArrayList<String> needToConfirm = matchup.getPlayersInConfirmation();

                @Override
                public void run() {
                    if (count >= 12) {
                        if (!Collections.disjoint(matchup.getParticipants(), needToConfirm)) {
                            this.cancel();
                            for (String playerId : matchup.getParticipants()) {
                                Player p = Bukkit.getPlayer(UUID.fromString(playerId));
                                if (p != null && p.isOnline()) {
                                    p.sendMessage(ConfigManager.MATCH_CANCELLED_DID_NOT_ACCEPT);
                                }
                                if (needToConfirm.contains(playerId)) {
                                    SkirmishTeam itemToRemove = matchup.getATeam().getPlayers().contains(playerId) ? matchup.getATeam() : matchup.getBTeam();
                                    queue.removeFromQueue(itemToRemove);
                                    if (p != null && p.isOnline()) {
                                        p.sendMessage(ConfigManager.CREW_REMOVED_FROM_QUEUE);
                                    }
                                }
                                potentialMatchups.remove(matchup);
                                needToConfirm.remove(playerId);
                            }
                            createMatchupConfirmation();
                        }
                        return;
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (needToConfirm.contains(String.valueOf(p.getUniqueId()))) {
                            p.sendMessage(ConfigManager.WAITING_ON_CONFIRMATION);
                            SoundUtilities.playPingSound(p);
                        }
                    }
                    count++;
                }
            };
            task.runTaskTimer(plugin, 0L, 20L * 5);
        }
    }

    private void startSkirmish(SkirmishTeam queueItemOne, SkirmishTeam queueItemTwo) {
        ArenaManager arenaManager = plugin.getArenaManager();
        new BukkitRunnable() {
            private int secondsLeft = 15;
            final SkirmishMatchup matchup = findPairContainingItem(queueItemOne);

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    if (areParticipantsOnline(queueItemOne) && areParticipantsOnline(queueItemTwo)) {
                        potentialMatchups.removeIf(pair -> pair.contains(queueItemOne) || pair.contains(queueItemTwo));
                        queue.removeFromQueue(queueItemOne);
                        queue.removeFromQueue(queueItemTwo);
                        arenaManager.setupArena(matchup);

                        Arena arena = arenaManager.getArenaByMatchup(matchup);
                            new BukkitRunnable() {
                                int remainingSeconds = 10;

                                public void run() {
                                    if (remainingSeconds <= 0) {
                                        arena.setIsInCountdown(false);
                                        for (String uuid : arena.getSkirmish().getMatchup().getParticipants()) {
                                            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                                            if (p != null) {
                                                p.sendMessage(ConfigManager.SKIRMISH_BEGIN);
                                                SoundUtilities.playHornSound(p);
                                                arena.removePlayerFromTeams(p);
                                            }
                                        }
                                        Bukkit.broadcast(ConfigManager.SKIRMISH_STARTED
                                            .replaceText(builder -> builder.matchLiteral("{crew1}").replacement(Component.text(queueItemOne.getCrew().getName()).decorate(TextDecoration.BOLD)))
                                            .replaceText(builder -> builder.matchLiteral("{crew2}").replacement(Component.text(queueItemTwo.getCrew().getName()).decorate(TextDecoration.BOLD))));
                                        arena.setupScoreboardForAllPlayers();
                                        plugin.getRunnableManager().startArenaCountdownTimer(arena, arenaManager);
                                        cancel();
                                    } else {
                                        if (remainingSeconds == 10 || remainingSeconds <= 5) {
                                            for (String uuid : arena.getSkirmish().getMatchup().getParticipants()) {
                                                Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                                                if (p != null) {
                                                    if(remainingSeconds <= 3) {
                                                        SoundUtilities.playPingSound(p);
                                                    }
                                                    p.sendMessage(ConfigManager.SKIRMISH_STARTING
                                                        .replaceText(builder -> builder.matchLiteral("{seconds}").replacement(String.valueOf(remainingSeconds))));
                                                }
                                            }
                                        }
                                        remainingSeconds--;
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 20L);

                    } else {
                        cancelMatchup(queueItemOne, queueItemTwo, true);
                    }
                    cancel();
                } else {
                    if (secondsLeft == 15 || secondsLeft == 10 || secondsLeft <= 5) {
                        for(String uuid : matchup.getParticipants()) {
                            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                            if(p != null && p.isOnline()) {
                                if(secondsLeft <= 3) {
                                    SoundUtilities.playPingSound(p);
                                }
                                p.sendMessage(ConfigManager.TELEPORTING_COUNTDOWN
                                    .replaceText(builder -> builder.matchLiteral("{seconds}").replacement(String.valueOf(secondsLeft))));
                            }
                        }
                    }
                    secondsLeft--;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public boolean areParticipantsOnline(SkirmishTeam queueItem) {
        ArrayList<String> participants = queueItem.getPlayers();
        for(String sUUID : participants) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(sUUID));
            if(!p.isOnline()) {
                return false;
            }
        }
        return true;
    }

    private void cancelMatchup(SkirmishTeam queueItemOne, SkirmishTeam queueItemTwo, boolean playerLeft) {
        SkirmishMatchup pair = findPairContainingItem(queueItemOne);
        potentialMatchups.remove(pair);
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(queueItemOne.getPlayers());
        participants.addAll(queueItemTwo.getPlayers());
        for(String pUUID : participants) {
            Player p = Bukkit.getPlayer(UUID.fromString(pUUID));
            if(p != null && p.isOnline()) {
                if(playerLeft) {
                    p.sendMessage(ConfigManager.MATCH_CANCELLED_PLAYER_LEFT);
                } else {
                    p.sendMessage(ConfigManager.MATCH_CANCELLED_DID_NOT_ACCEPT);
                }
            }
            pair. getPlayersInConfirmation().remove(pUUID);
        }
        queue.removeFromQueue(queueItemOne);
        queue.removeFromQueue(queueItemTwo);
    }

    public SkirmishMatchup findPairContainingItem(SkirmishTeam item) {
        for (SkirmishMatchup pair : potentialMatchups) {
            if (pair.getATeam().equals(item) || pair.getBTeam().equals(item)) {
                return pair;
            }
        }
        return null;
    }

    public SkirmishQueue getQueue() {
        return this.queue;
    }

    public HashMap<UUID, Integer> getTaskMap() {
        return taskMap;
    }

    public void scheduleTimer(Arena arena, ArenaManager manager) {
        int taskId = new CountdownTimer(arena, manager).runTaskTimer(plugin, 0L, 20L).getTaskId();
        taskMap.put(arena.getArenaId(), taskId);
    }

    public void cancelTimer(Arena arena) {
        if (taskMap.containsKey(arena.getArenaId())) {
            Bukkit.getScheduler().cancelTask(taskMap.get(arena.getArenaId()));
            taskMap.remove(arena.getArenaId());
        }
    }

    public void removeTask(UUID arenaId) {
        taskMap.remove(arenaId);
    }
}
