package org.ovclub.crews.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.skirmish.SkirmishTeam;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerResponseListener implements Listener {
    private final JavaPlugin plugin;
    private final UUID playerUUID;
    private final SkirmishTeam smallerTeam;
    private final SkirmishTeam largerTeam;
    private long responseStartTime;
    private int chosenTeamSize;
    private boolean awaitingResponse;

    public PlayerResponseListener(JavaPlugin plugin, UUID playerUUID, SkirmishTeam smallerTeam, SkirmishTeam largerTeam) {
        this.plugin = plugin;
        this.playerUUID = playerUUID;
        this.smallerTeam = smallerTeam;
        this.largerTeam = largerTeam;
        this.responseStartTime = System.currentTimeMillis();
        this.awaitingResponse = true;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (awaitingResponse && event.getPlayer().getUniqueId().equals(playerUUID)) {
            int smallerTeamSize = smallerTeam.getPlayers().size();
            int largerTeamSize = largerTeam.getPlayers().size();
            ArrayList<String> participants = new ArrayList<>();
            event.setCancelled(true);
            int response;
            try {
                response = Integer.parseInt(event.getMessage());
            } catch (NumberFormatException e) {
                event.getPlayer().sendMessage(ConfigManager.INVALID_INTEGER);
                return;
            }

            if (response < smallerTeamSize || response > largerTeamSize) {
                event.getPlayer().sendMessage(ConfigManager.INVALID_RANGE
                    .replaceText(builder -> builder.matchLiteral("{min}").replacement(String.valueOf(smallerTeamSize)))
                    .replaceText(builder -> builder.matchLiteral("{max}").replacement(String.valueOf(largerTeamSize))));
            } else {
                participants.addAll(smallerTeam.getPlayers());
                participants.addAll(largerTeam.getPlayers());
                handleValidResponse(response, participants);

                awaitingResponse = false;

                AsyncPlayerChatEvent.getHandlerList().unregister(this);
            }
        }
    }

    private void handleValidResponse(int response, ArrayList<String> participants) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            for(String uuid : participants) {
                Player p = Bukkit.getPlayer(UUID.fromString(uuid));
                if(p != null && p.isOnline()) {
                    if(smallerTeam.getPlayers().contains(uuid)) {
                        p.sendMessage(ConfigManager.CREW_HAS_CHOSEN_PLAYER_AMOUNT
                            .replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(response))));
                    }
                    if(largerTeam.getPlayers().contains(uuid)) {
                        int playersNotPlaying = largerTeam.getPlayers().size() - response;
                        p.sendMessage(ConfigManager.ENEMY_HAS_CHOSEN_PLAYER_AMOUNT
                            .replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(response))));
                        if(response != largerTeam.getPlayers().size()) {
                            p.sendMessage(ConfigManager.PLAYERS_SITTING_OUT
                                .replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(playersNotPlaying))));
                            chosenTeamSize = response;
                        }
                    }
                }
            }
        });
    }

    public boolean isAwaitingResponse() {
        return awaitingResponse;
    }

    public int getChosenTeamSize() {
        return chosenTeamSize;
    }

    public long getResponseStartTime() {
        return responseStartTime;
    }
}
