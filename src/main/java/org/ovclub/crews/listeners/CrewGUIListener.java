package org.ovclub.crews.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.turfwar.TurfWarQueueItem;
import org.ovclub.crews.utilities.GUIUtilities;

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
//        String inventoryTitle = net.md_5.bungee.api.ChatColor.stripColor(event.getView().getTitle());
//        Crew targetCrew = plugin.getData().getCrew(p);

        if(clickedItem.getType() == Material.COMPASS) {
            //warp logic
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

        TextComponent turfWarSetup = Component.text("Turf War Setup");
        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Crew Turf Wars"))) {
            p.closeInventory();
            Inventory queueInv = Bukkit.createInventory(null, 27, turfWarSetup);
            GUIUtilities.initializeTurfWarSelectPlayerItems(pCrew, queueInv);
            data.getInventories().put(playerUUID, queueInv);
            GUIUtilities.openInventory(p, queueInv);
        }

        if (clickedItem.getType() == Material.PLAYER_HEAD && e.getView().title().equals(turfWarSetup)) {
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
            } else {
                p.sendMessage(ConfigManager.ONE_PLAYER_REQUIRED_FOR_QUEUE);
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
