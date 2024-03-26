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
import org.bukkit.inventory.ItemStack;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;

import java.util.UUID;

public class CrewShopListener implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player p = (Player) event.getWhoClicked();
        UUID playerUUID = p.getUniqueId();
        if (!event.getInventory().equals(inventories.get(playerUUID))) return;

        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();

        //verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        if (clickedItem.getType() == Material.LAPIS_BLOCK) {
            Crew pCrew = crewManager.getPlayercrew(p);
            if (!upgradeManager.checkForUpgrade(playerCrew, "discord")) {
                if (crewManager.CheckForChief(playerCrew, p)) {
                    int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("discord");
                    if(crewManager.getVault(playerCrew) >= purchasePrice) {
                        getDiscordName = p;
                        p.sendMessage(ChatColor.DARK_BLUE + "[\uD83C\uDFA7]" + ChatColor.BLUE + " Please type/enter your discord username: ");
                        p.closeInventory();
                    } else {
                        ChatUtilities.NeedMoreSponges(p);
                    }
                } else {
                    ChatUtilities.MustBeChief(p);
                }
            } else {
                ChatUtilities.UpgradeAlreadyUnlocked(p);
            }
        }

        if (clickedItem.getType() == Material.BAMBOO_SIGN) {
            String playerCrew = crewManager.getPlayercrew(p);
            if (!upgradeManager.checkForUpgrade(playerCrew, "chat")) {
                if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
                    int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("chat");
                    if(crewManager.getVault(playerCrew) >= purchasePrice) {
                        crewManager.removeFromVault(playerCrew, purchasePrice, p);
                        ChatUtilities.UpgradeSuccessful(playerCrew, "chat");
                        upgradeManager.editUpgrade(playerCrew, "chat", true);
                        p.closeInventory();
                    } else {
                        ChatUtilities.NeedMoreSponges(p);
                    }
                } else {
                    ChatUtilities.MustBeChiefOrElder(p);
                }
            } else {
                ChatUtilities.UpgradeAlreadyUnlocked(p);
            }
        }

        //NEED TO TAKE MONEY FROM crew WHEN BUYING UPGRADES!!!!!!!!!!!!!!!!!!!!!!!!

        if (clickedItem.getType() == Material.FILLED_MAP) {
            String playerCrew = crewManager.getPlayercrew(p);
            if (!upgradeManager.checkForUpgrade(playerCrew, "mail")) {
                if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
                    int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("mail");
                    if(crewManager.getVault(playerCrew) >= purchasePrice) {
                        crewManager.removeFromVault(playerCrew, purchasePrice, p);
                        ChatUtilities.UpgradeSuccessful(playerCrew, "mail");
                        upgradeManager.editUpgrade(playerCrew, "mail", true);
                        p.closeInventory();
                    } else {
                        ChatUtilities.NeedMoreSponges(p);
                    }
                } else {
                    ChatUtilities.MustBeChiefOrElder(p);
                }
            } else {
                ChatUtilities.UpgradeAlreadyUnlocked(p);
            }
        }

        if (clickedItem.getType() == Material.NAME_TAG) {
            String playerCrew = crewManager.getPlayercrew(p);
            if (crewManager.CheckForChief(playerCrew, p)) {
                int purchasePrice = crewsClass.getPrices().getInt("changename");
                if(crewManager.getVault(playerCrew) >= purchasePrice) {
                    waitingForInputPlayer = p;
                    p.sendMessage(ChatColor.YELLOW + "[\uD83D\uDD8A]" + ChatColor.GOLD + " Please type/enter your new crew name: ");
                    p.closeInventory();
                } else {
                    ChatUtilities.NeedMoreSponges(p);
                }
            } else {
                ChatUtilities.MustBeChief(p);
            }

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
