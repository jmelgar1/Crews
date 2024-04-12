package org.ovclub.crews.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.listeners.PlayerResponseListener;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.turfwar.TurfWar;
import org.ovclub.crews.object.turfwar.TurfWarQueue;
import org.ovclub.crews.object.turfwar.TurfWarQueueItem;
import org.ovclub.crews.object.turfwar.TurfWarQueuePair;

import java.util.*;

public class TurfWarManager {
    private final TurfWarQueue queue = new TurfWarQueue();
    private final Map<TurfWarQueueItem, TurfWar> activeWars = new HashMap<>();
    private final List<TurfWarQueuePair> potentialMatchups = new ArrayList<>();

    private final JavaPlugin plugin;
    private final Map<UUID, PlayerResponseListener> responseListeners = new HashMap<>();

    public TurfWarManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void queueCrew(TurfWarQueueItem crewQueue) {
        queue.addToQueue(crewQueue);
        createMatchupConfirmation();
    }
    public List<TurfWarQueuePair> getPotentialMatchups() {
        return this.potentialMatchups;
    }
    public ArrayList<String> getWaitingForConfirmation(Crew crew){
        for (TurfWarQueuePair pair : potentialMatchups) {
            if (pair.getItem1().getCrew().equals(crew) ||
                pair.getItem2().getCrew().equals(crew)) {
                return pair.getPlayersInConfirmation();
            }
        }
        return new ArrayList<>();
    }
    public boolean allConfirmedForTurfWar(Crew crew) {
        for (TurfWarQueuePair pair : potentialMatchups) {
            if (pair.getItem1().getCrew().equals(crew) ||
                pair.getItem2().getCrew().equals(crew)) {
                return pair.getPlayersInConfirmation().isEmpty();
            }
        }
        return false;
    }
    public void generateFairMatchup(Crew crew) {
        TurfWarQueuePair queuePair = findQueuePairForCrew(crew);
        if (queuePair == null) {
            return;
        }
        TurfWarQueueItem team1 = queuePair.getItem1();
        TurfWarQueueItem team2 = queuePair.getItem2();

        int teamOneSize = team1.getPlayers().size();
        int teamTwoSize = team2.getPlayers().size();

        if (teamOneSize == teamTwoSize) {
            startTurfWar(team1, team2);
            return;
        }

        TurfWarQueueItem smallestTeam = teamOneSize < teamTwoSize ? team1 : team2;
        TurfWarQueueItem largestTeam = teamOneSize > teamTwoSize ? team1 : team2;

        for (String uuid : largestTeam.getPlayers()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p != null && p.isOnline()) {
                p.sendMessage(Component.text("ALERT: ", NamedTextColor.RED)
                    .append(Component.text("UNBALANCED MATCH!\n", NamedTextColor.WHITE))
                    .append(Component.text("Since you are the larger team please wait for the other team to determine your team size!\n", NamedTextColor.WHITE))
                    .append(Component.text("- Opponent's Team Size: " + smallestTeam.getPlayers().size() + "\n", NamedTextColor.GRAY))
                    .append(Component.text("- Your Team Size: " + largestTeam.getPlayers().size(), NamedTextColor.GRAY)));
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.0F);
            }
        }

        String pUUID = smallestTeam.getPlayers().get(0);
        Player p = Bukkit.getPlayer(UUID.fromString(pUUID));
        if (p != null && p.isOnline()) {
            p.sendMessage(Component.text("ALERT: ", NamedTextColor.RED)
                .append(Component.text("UNBALANCED MATCH!\n", NamedTextColor.WHITE))
                .append(Component.text("Since you are the smaller team please type the amount of opponents you would like to fight!\n", NamedTextColor.WHITE))
                .append(Component.text("- Your Team Size: " + smallestTeam.getPlayers().size() + "\n", NamedTextColor.GRAY))
                .append(Component.text("- Opponent's Team Size: " + largestTeam.getPlayers().size(), NamedTextColor.GRAY)));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.0F);

            PlayerResponseListener responseListener = new PlayerResponseListener(plugin, p.getUniqueId(), smallestTeam, largestTeam);
            Bukkit.getPluginManager().registerEvents(responseListener, plugin);
            responseListeners.put(p.getUniqueId(), responseListener);

            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerResponseListener listener = responseListeners.remove(p.getUniqueId());
                    if (listener != null && listener.isAwaitingResponse()) {
                        cancelMatchup(team1, team2);
                        AsyncPlayerChatEvent.getHandlerList().unregister(listener);
                    } else {
                        startTurfWar(team1, team2);
                    }
                }
            }.runTaskLater(plugin, 600L);
        } else {
            p.sendMessage("leader not online");
        }
    }

    public TurfWarQueuePair findQueuePairForCrew(Crew crew) {
        for (TurfWarQueuePair pair : potentialMatchups) {
            if (pair.getItem1().getCrew().equals(crew) || pair.getItem2().getCrew().equals(crew)) {
                return pair;
            }
        }
        return null;
    }

    public void createMatchupConfirmation() {
        List<TurfWarQueueItem> totalQueue = queue.getAllItemsInQueue();
        TurfWarQueueItem firstItem = null;
        TurfWarQueueItem secondItem = null;

        for (TurfWarQueueItem item : totalQueue) {
            boolean isAlreadyMatched = false;
            for (TurfWarQueuePair pair : potentialMatchups) {
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
            potentialMatchups.add(new TurfWarQueuePair(firstItem, secondItem, playerList));
        }
    }

    private void startTurfWar(TurfWarQueueItem queueItemOne, TurfWarQueueItem queueItemTwo) {
        TurfWar war = new TurfWar(queueItemOne, queueItemTwo);
        if(areParticipantsOnline(queueItemOne) && areParticipantsOnline( queueItemTwo)) {
            potentialMatchups.removeIf(pair -> pair.contains(queueItemOne) || pair.contains(queueItemTwo));
            queue.removeFromQueue(queueItemOne);
            queue.removeFromQueue(queueItemTwo);
            activeWars.put(queueItemOne, war);
            activeWars.put(queueItemTwo, war);
            Bukkit.broadcast(ConfigManager.TURF_WAR_STARTED
                .replaceText(builder -> builder.matchLiteral("{crew1}").replacement(queueItemOne.getCrew().getName()))
                .replaceText(builder -> builder.matchLiteral("{crew2}").replacement(queueItemTwo.getCrew().getName())));
        } else {
            cancelMatchup(queueItemOne, queueItemTwo);
        }
    }

    public boolean areParticipantsOnline(TurfWarQueueItem queueItem) {
        ArrayList<String> participants = queueItem.getPlayers();
        for(String sUUID : participants) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(sUUID));
            if(!p.isOnline()) {
                return false;
            }
        }
        return true;
    }

    private void cancelMatchup(TurfWarQueueItem queueItemOne, TurfWarQueueItem queueItemTwo) {
        TurfWarQueuePair pair = findPairContainingItem(queueItemOne);
        potentialMatchups.remove(pair);
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(queueItemOne.getPlayers());
        participants.addAll(queueItemTwo.getPlayers());
        for(String pUUID : participants) {
            Player p = Bukkit.getPlayer(UUID.fromString(pUUID));
            if(p != null && p.isOnline()) {
                p.sendMessage(ConfigManager.MATCH_CANCELLED);
            }
            pair. getPlayersInConfirmation().remove(pUUID);
        }
        queue.removeFromQueue(queueItemOne);
        queue.removeFromQueue(queueItemTwo);
    }

    public TurfWarQueuePair findPairContainingItem(TurfWarQueueItem item) {
        for (TurfWarQueuePair pair : potentialMatchups) {
            if (pair.getItem1().equals(item) || pair.getItem2().equals(item)) {
                return pair;
            }
        }
        return null;
    }

    public TurfWarQueue getQueue() {
        return this.queue;
    }
    // Methods to handle end of a turf war, cleanup, and rewards
}
