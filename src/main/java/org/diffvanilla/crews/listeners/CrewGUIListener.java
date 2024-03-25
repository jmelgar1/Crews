package org.diffvanilla.crews.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.GUIUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrewGUIListener implements Listener {

    private static Inventory inv;
    public static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();
    private final Crews plugin;
    public CrewGUIListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player p = (Player) event.getWhoClicked();
        UUID playerUUID = p.getUniqueId();
        if(!event.getInventory().equals(inventories.get(playerUUID))) return;

        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        //verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        String inventoryTitle = net.md_5.bungee.api.ChatColor.stripColor(event.getView().getTitle());
        Crew targetCrew = plugin.getData().getCrew(p);

        if(clickedItem.getType() == Material.COMPASS) {

            p.closeInventory();
        }

        if(clickedItem.getType() == Material.BARRIER) {
            if(inventoryTitle.contains("Crew Profile")) {
                p.closeInventory();
            } else {
                inv = Bukkit.createInventory(null, 54, ChatUtilities.tribalGames.toString() + net.md_5.bungee.api.ChatColor.BOLD + targetCrew.getName().toUpperCase() + " Crew Profile");
                GUIUtilities.openInventory(p, inventories.get(playerUUID));
                GUIUtilities.initializeItems(targetCrew, inv);
            }
        }

        if(clickedItem.getType() == Material.SPONGE) {
            p.closeInventory();
            p.performCommand("crews shop");
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent event) {
        if(event.getInventory().equals(inventories.get(event.getWhoClicked().getUniqueId()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e, Player p) {
        HandlerList.unregisterAll(this);
        Bukkit.getServer().getScheduler().runTaskLater(Crews.getPlugin(Crews.class), p::updateInventory, 1L);
    }
}
