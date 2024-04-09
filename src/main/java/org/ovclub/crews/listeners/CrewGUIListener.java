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
import org.ovclub.crews.object.turfwar.TurfWarQueueItem;
import org.ovclub.crews.utilities.GUICreator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Crew Turf Wars"))) {
            p.closeInventory();
            GUICreator.createTurfWarSelectPlayersGUI(data, p, pCrew);
        }

        if (clickedItem.getType() == Material.PLAYER_HEAD && e.getView().title().equals("Turf War Setup")) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String playerName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
                Player player = Bukkit.getPlayerExact(playerName);

                if (player != null && player.isOnline()) {
                    data.getSelectedForQueue().add(player.getUniqueId().toString());
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
                    } else {
                        newLore.add(Component.text("Player is queued.").color(NamedTextColor.BLUE));
                    }
                    meta.lore(newLore);
                    clickedItem.setItemMeta(meta);
                }
            }
        }

        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Turf War Setup"))) {
            if(data.getSelectedForQueue().size() >= 1) {
                p.closeInventory();
                TurfWarQueueItem queueItem = new TurfWarQueueItem();
                queueItem.setCrew(data.getCrew(p));
                queueItem.setPlayers(data.getSelectedForQueue());
                plugin.getTurfWarManager().queueCrew(queueItem);
                plugin.getData().broadcastToAllOnlineCrewMembers(ConfigManager.CREW_HAS_JOINED_QUEUE);
                p.sendMessage(ConfigManager.JOINED_QUEUE);
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
