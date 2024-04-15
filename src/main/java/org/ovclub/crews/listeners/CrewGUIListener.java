package org.ovclub.crews.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.skirmish.SkirmishQueueItem;
import org.ovclub.crews.utilities.GUICreator;

import java.util.*;

public class CrewGUIListener implements Listener {

    private final Crews plugin;
    public CrewGUIListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        PlayerData data = plugin.getData();
        final Player p = (Player) e.getWhoClicked();
        UUID playerUUID = p.getUniqueId();
        if(!e.getInventory().equals(data.getInventories().get(playerUUID))) return;
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        Crew pCrew = data.getCrew(p);

        if(clickedItem.getType() == Material.COMPASS) {
            p.closeInventory();
        }

        if(clickedItem.getType() == Material.BARRIER) {
            p.closeInventory();
        }

        //crews shop item
        if(clickedItem.getType() == Material.SPONGE) {
            p.closeInventory();
            p.performCommand("crews shop");
        }

        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Crew Skirmishes"))) {
            p.closeInventory();
            GUICreator.createSkirmishSelectPlayersGUI(data, p, pCrew);
        }

        if (clickedItem.getType() == Material.PLAYER_HEAD && e.getView().title().equals(Component.text("Skirmish Setup"))) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String playerName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
                Player player = Bukkit.getPlayerExact(playerName);

                if (player != null && player.isOnline()) {
                    List<Component> currentLore = meta.lore();
                    boolean isQueued = false;
                    if (currentLore != null) {
                        for (Component line : currentLore) {
                            if (PlainTextComponentSerializer.plainText().serialize(line).equals("Player is queued.")) {
                                isQueued = true;
                                break;
                            }
                        }
                    }
                    List<Component> newLore = new ArrayList<>();
                    if (isQueued) {
                        newLore.add(Component.text("Click to select this member for the queue.").color(NamedTextColor.GRAY));
                        data.removeFromSelectedForQueue(pCrew, player.getUniqueId().toString());
                    } else {
                        newLore.add(Component.text("Player is queued.").color(NamedTextColor.BLUE));
                        data.addToSelectedForQueue(pCrew, player.getUniqueId().toString());
                    }
                    meta.lore(newLore);
                    clickedItem.setItemMeta(meta);
                }
            }
        }

        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Skirmish Setup"))) {
            if(plugin.getSkirmishManager().getQueue().isInQueue(pCrew)) {
                p.sendMessage(ConfigManager.ALREADY_IN_QUEUE);
                return;
            }
            if(data.getSelectedForQueue().size() >= 1) {
                p.closeInventory();
                SkirmishQueueItem queueItem = new SkirmishQueueItem();
                queueItem.setCrew(pCrew);
                queueItem.setPlayers(data.getSelectedForQueue().get(pCrew));
                data.getSelectedForQueue().remove(pCrew);
                plugin.getSkirmishManager().queueCrew(queueItem);
                Bukkit.broadcast(ConfigManager.CREW_HAS_JOINED_QUEUE);
                for(String stringUUID : queueItem.getPlayers()) {
                    UUID pUUID = UUID.fromString(stringUUID);
                    Player player = Bukkit.getPlayer(pUUID);
                    if(player != null) {
                        if (player.isOnline()) {
                            player.sendMessage(ConfigManager.JOINED_QUEUE);
                        } else {
                            //throw error here
                        }
                    }
                }
                } else {
                    p.sendMessage(ConfigManager.ONE_PLAYER_REQUIRED_FOR_QUEUE);
                }
            }

        if(clickedItem.getType().equals(Material.LILY_PAD)) {
            if(e.getView().title().equals(Component.text("Change Crew Banner"))) {
                p.closeInventory();
                GUICreator.createCrewInfoGUI(data, p, pCrew);
            }
        }

        if(clickedItem.getType().toString().endsWith("_BANNER")) {
            if(e.getView().title().equals(Component.text(pCrew.getName() + " - Profile"))) {
                if(!pCrew.isHigherup(p)) {
                    p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
                    return;
                }
                p.closeInventory();
                GUICreator.createBannerSelectGUI(data, p, pCrew);
            }
            else if(e.getView().title().equals(Component.text("Change Crew Banner"))) {
                if(p.getInventory().contains(clickedItem)) {
                    p.closeInventory();
                    ItemStack newBanner = clickedItem.clone();
                    newBanner.setAmount(1);
                    pCrew.setBanner(newBanner);
                    GUICreator.createBannerSelectGUI(data, p, pCrew);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent event) {
        PlayerData data = plugin.getData();
        if(event.getInventory().equals(data.getInventories().get(event.getWhoClicked().getUniqueId()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        plugin.getData().getInventories().remove(p.getUniqueId());
    }
}
