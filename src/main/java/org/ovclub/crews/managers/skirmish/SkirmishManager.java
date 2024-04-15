package org.ovclub.crews.managers.skirmish;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.listeners.PlayerResponseListener;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.skirmish.Skirmish;
import org.ovclub.crews.object.skirmish.SkirmishQueue;
import org.ovclub.crews.object.skirmish.SkirmishQueueItem;
import org.ovclub.crews.object.skirmish.SkirmishQueuePair;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.*;

public class SkirmishManager {
    private final SkirmishQueue queue = new SkirmishQueue();
    private final Map<SkirmishQueuePair, Skirmish> activeWars = new HashMap<>();
    private final List<SkirmishQueuePair> potentialMatchups = new ArrayList<>();

    private final JavaPlugin plugin;
    private final Map<UUID, PlayerResponseListener> responseListeners = new HashMap<>();

    private final ArenaManager arenaManager;

    public SkirmishManager(Crews plugin) {
        this.plugin = plugin;
        this.arenaManager = new ArenaManager(Bukkit.getWorld("world"), plugin);
    }

    public void queueCrew(SkirmishQueueItem crewQueue) {
        queue.addToQueue(crewQueue);
        createMatchupConfirmation();
    }
    public List<SkirmishQueuePair> getPotentialMatchups() {
        return this.potentialMatchups;
    }
    public boolean allConfirmedForSkirmish(Crew crew) {
        for (SkirmishQueuePair pair : potentialMatchups) {
            if (pair.getItem1().getCrew().equals(crew) ||
                pair.getItem2().getCrew().equals(crew)) {
                return pair.getPlayersInConfirmation().isEmpty();
            }
        }
        return false;
    }
    public void generateFairMatchup(Crew crew) {
        SkirmishQueuePair queuePair = findQueuePairForCrew(crew);
        if (queuePair == null) {
            return;
        }
        SkirmishQueueItem team1 = queuePair.getItem1();
        SkirmishQueueItem team2 = queuePair.getItem2();

        int teamOneSize = team1.getPlayers().size();
        int teamTwoSize = team2.getPlayers().size();

        if (teamOneSize == teamTwoSize) {
            startSkirmish(team1, team2);
            return;
        }

        SkirmishQueueItem smallestTeam = teamOneSize < teamTwoSize ? team1 : team2;
        SkirmishQueueItem largestTeam = teamOneSize > teamTwoSize ? team1 : team2;

        for (String uuid : largestTeam.getPlayers()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p != null && p.isOnline()) {
                UnicodeCharacters.sendUnbalancedMessage(p, smallestTeam.getPlayers().size(), largestTeam.getPlayers().size(), false);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.0F);
            }
        }

        String pUUID = smallestTeam.getPlayers().get(0);
        Player p = Bukkit.getPlayer(UUID.fromString(pUUID));
        if (p != null && p.isOnline()) {
            UnicodeCharacters.sendUnbalancedMessage(p, smallestTeam.getPlayers().size(), largestTeam.getPlayers().size(), true);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.0F);

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

    public SkirmishQueuePair findQueuePairForCrew(Crew crew) {
        for (SkirmishQueuePair pair : potentialMatchups) {
            if (pair.getItem1().getCrew().equals(crew) || pair.getItem2().getCrew().equals(crew)) {
                return pair;
            }
        }
        return null;
    }

    public void createMatchupConfirmation() {
        List<SkirmishQueueItem> totalQueue = queue.getAllItemsInQueue();
        SkirmishQueueItem firstItem = null;
        SkirmishQueueItem secondItem = null;

        for (SkirmishQueueItem item : totalQueue) {
            boolean isAlreadyMatched = false;
            for (SkirmishQueuePair pair : potentialMatchups) {
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
            SkirmishQueuePair matchup = new SkirmishQueuePair(firstItem, secondItem, playerList);
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
                                    SkirmishQueueItem itemToRemove = matchup.getItem1().getPlayers().contains(playerId) ? matchup.getItem1() : matchup.getItem2();
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
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.0F);
                        }
                    }
                    count++;
                }
            };
            task.runTaskTimer(plugin, 0L, 20L * 5);
        }
    }

    private void startSkirmish(SkirmishQueueItem queueItemOne, SkirmishQueueItem queueItemTwo) {
        Location arenaCenter = arenaManager.getRandomArenaLocation();

        new BukkitRunnable() {
            private int secondsLeft = 15;
            final SkirmishQueuePair pair = findPairContainingItem(queueItemOne);

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    // Start the actual skirmish
                    Skirmish war = new Skirmish(queueItemOne, queueItemTwo);
                    if (areParticipantsOnline(queueItemOne) && areParticipantsOnline(queueItemTwo)) {
                        potentialMatchups.removeIf(pair -> pair.contains(queueItemOne) || pair.contains(queueItemTwo));
                        queue.removeFromQueue(queueItemOne);
                        queue.removeFromQueue(queueItemTwo);
                        activeWars.put(pair, war);
                        Bukkit.broadcast(ConfigManager.SKIRMISH_STARTED
                            .replaceText(builder -> builder.matchLiteral("{crew1}").replacement(queueItemOne.getCrew().getName()))
                            .replaceText(builder -> builder.matchLiteral("{crew2}").replacement(queueItemTwo.getCrew().getName())));
                        arenaManager.setupArena(arenaCenter);
                    } else {
                        cancelMatchup(queueItemOne, queueItemTwo, true);
                    }
                    cancel();
                } else {
                    if (secondsLeft == 15 || secondsLeft == 10 || secondsLeft <= 5) {
                        for(String uuid : pair.getParticipants()) {
                            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                            if(p != null && p.isOnline()) {
                                p.sendMessage(ConfigManager.SKIRMISH_STARTING
                                    .replaceText(builder -> builder.matchLiteral("{seconds}").replacement(String.valueOf(secondsLeft))));
                            }
                        }
                    }
                    secondsLeft--;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public boolean areParticipantsOnline(SkirmishQueueItem queueItem) {
        ArrayList<String> participants = queueItem.getPlayers();
        for(String sUUID : participants) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(sUUID));
            if(!p.isOnline()) {
                return false;
            }
        }
        return true;
    }

    private void cancelMatchup(SkirmishQueueItem queueItemOne, SkirmishQueueItem queueItemTwo, boolean playerLeft) {
        SkirmishQueuePair pair = findPairContainingItem(queueItemOne);
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

    public SkirmishQueuePair findPairContainingItem(SkirmishQueueItem item) {
        for (SkirmishQueuePair pair : potentialMatchups) {
            if (pair.getItem1().equals(item) || pair.getItem2().equals(item)) {
                return pair;
            }
        }
        return null;
    }

    public SkirmishQueue getQueue() {
        return this.queue;
    }
}
