package org.diffvanilla.crews.listeners;

import net.md_5.bungee.api.ChatColor;
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
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrewShopListener implements Listener {
    private static Inventory inv;
    public static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();
    private final Crews plugin;
    public CrewShopListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        PlayerData data = plugin.getData();
        final Player p = (Player) event.getWhoClicked();
        UUID playerUUID = p.getUniqueId();
        if (!event.getInventory().equals(inventories.get(playerUUID))) return;

        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        //verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        if (clickedItem.getType() == Material.LAPIS_BLOCK) {
            Crew pCrew = data.getCrew(p);
            if(pCrew.getUnlockedUpgrades().contains("discord")){
                p.sendMessage(ConfigManager.UPGRADE_ALREADY_UNLOCKED);
                return;
            }
            if(!pCrew.isHigherup(p)){
                p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
                return;
            }
            if(pCrew.getVault() < ConfigManager.UPGRADE_DISCORD_COST){
                p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
                return;
            }
            pCrew.addUpgrade("discord");
            pCrew.removeFromVault(ConfigManager.UPGRADE_DISCORD_COST, p);
            p.sendMessage(ChatColor.DARK_BLUE + "[\uD83C\uDFA7]" + ChatColor.BLUE + " Please type/enter your discord username: ");
            p.closeInventory();
        }

        if (clickedItem.getType() == Material.BAMBOO_SIGN) {
            Crew pCrew = data.getCrew(p);
            if(pCrew.getUnlockedUpgrades().contains("chat")){
                p.sendMessage(ConfigManager.UPGRADE_ALREADY_UNLOCKED);
                return;
            }
            if(!pCrew.isHigherup(p)){
                p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
                return;
            }
            if(pCrew.getVault() < ConfigManager.UPGRADE_CHAT_COST){
                p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
                return;
            }
            pCrew.addUpgrade("chat");
            pCrew.removeFromVault(ConfigManager.UPGRADE_CHAT_COST, p);
            //success message here
            p.closeInventory();
        }

        //NEED TO TAKE MONEY FROM crew WHEN BUYING UPGRADES!!!!!!!!!!!!!!!!!!!!!!!!

        if (clickedItem.getType() == Material.FILLED_MAP) {
            Crew pCrew = data.getCrew(p);
            if(pCrew.getUnlockedUpgrades().contains("mail")){
                p.sendMessage(ConfigManager.UPGRADE_ALREADY_UNLOCKED);
                return;
            }
            if(!pCrew.isHigherup(p)){
                p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
                return;
            }
            if(pCrew.getVault() < ConfigManager.UPGRADE_MAIL_COST){
                p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
                return;
            }
            pCrew.addUpgrade("mail");
            pCrew.removeFromVault(ConfigManager.UPGRADE_MAIL_COST, p);
            //success message here
            p.closeInventory();
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
