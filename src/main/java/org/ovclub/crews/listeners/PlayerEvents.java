package org.ovclub.crews.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.subcommands.ShopCommand;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.hightable.VoteItem;
import org.ovclub.crews.utilities.ComponentUtilities;
import org.ovclub.crews.utilities.GUI.GUICreator;
import org.ovclub.crews.utilities.HightableUtility;

import java.util.*;

public class PlayerEvents implements Listener {

    private final Crews plugin;
    public PlayerEvents(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        Crew targetCrew = plugin.getData().getCrew(p);

        if(targetCrew != null) {
            Component crewChatPrefix = Component.text()
                .append(Component.text("(", NamedTextColor.GREEN))
                .append(Component.text(targetCrew.getName(), NamedTextColor.DARK_GREEN))
                .append(Component.text(") ", NamedTextColor.GREEN))
                .build();

            String sentMessage = e.getMessage();
            Component playerName = Component.text("<").color(NamedTextColor.WHITE)
                .append(p.displayName())
                .append(Component.text("> ").color(NamedTextColor.WHITE));
            Component newMessage = crewChatPrefix.append(playerName).append(Component.text(sentMessage));

            //if crew chat is enabled only send message to crew members
            if (plugin.getData().isInCrewChat(uuid)) {
                e.setCancelled(true);
                targetCrew.broadcast(newMessage);
            }

            //logic for renaming crew
            if (ShopCommand.waitingForInputPlayer != null && p == ShopCommand.waitingForInputPlayer) {
                e.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Handle the user input (in this case, it's the new crew name)
                        String newcrewName = e.getMessage();

                        p.performCommand("crews rename " + newcrewName);

                        // Reset the waiting state
                        ShopCommand.waitingForInputPlayer = null;
                    }
                }.runTask(plugin);
            }

            //logic for getting discord name
            if (ShopCommand.getDiscordName != null && p == ShopCommand.getDiscordName) {
                e.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Handle the user input (in this case, it's the new crew name)
                        String discordName = e.getMessage();

                        //new DiscordManager(crewManager.getPlayercrew(p), p, discordName);

                        // Reset the waiting state
                        ShopCommand.getDiscordName = null;
                    }
                }.runTask(plugin);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        for (Crew c : plugin.getData().getCrews()) {
            if (c.hasMember(p)) {
                plugin.getData().addCPlayer(p, c);
                break;
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<VoteItem> activeMultipliers = plugin.getData().getActiveMultipliers();
                if(!plugin.getData().getSeenMultipliers().contains(p.getUniqueId())) {
                    GUICreator.createActiveMultipliers(activeMultipliers, plugin.getData(), p);
                    plugin.getData().addSeenMultipliers(p.getUniqueId());
                }
                if(plugin.getData().getCrew(p) != null) {
                    Crew crew = plugin.getData().getCrew(p);
                    ComponentUtilities.sendCrewMessage(p, crew.getRank(), crew.isInHighTable(), HightableUtility.hasPlayerVoted(p));
                } else {
                    ComponentUtilities.joinCrewMessage(p);
                }
            }
        }.runTaskLater(plugin, 40);
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        plugin.getData().removeCPlayer(e.getPlayer());
    }
}
