package org.ovclub.crews.commands.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.GUIUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.UUID;

public class ShopCommand implements SubCommand {

    public static Player waitingForInputPlayer = null;
	public static Player getDiscordName = null;

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "View crew upgrade shop.";
	}

    @Override
    public String getSyntax() {
        return "/crews shop";
    }

    @Override
    public String getPermission() {
        return "crews.player.shop";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (args.length != 0) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 27, Component.text(UnicodeCharacters.shop_emoji + "CREW UPGRADE SHOP " + UnicodeCharacters.shop_emoji)
            .color(UnicodeCharacters.sponge_color)
            .decorate(TextDecoration.BOLD));
        UUID playerUUID = p.getUniqueId();
        GUIUtilities.initializeCrewShopItems(pCrew, inv);
        data.getInventories().put(playerUUID, inv);
        GUIUtilities.openInventory(p, data.getInventories().get(playerUUID));
    }
	
//	@EventHandler
//	public void onInventoryClick(final InventoryClickEvent event) {
//		final Player p = (Player) event.getWhoClicked();
//		UUID playerUUID = p.getUniqueId();
//		if (!event.getInventory().equals(inventories.get(playerUUID))) return;
//
//		event.setCancelled(true);
//
//		final ItemStack clickedItem = event.getCurrentItem();
//
//		//verify current item is not null
//		if (clickedItem == null || clickedItem.getType().isAir()) return;
//
//		if (clickedItem.getType() == Material.LAPIS_BLOCK) {
//			Crew pCrew = crewManager.getPlayercrew(p);
//			if (!upgradeManager.checkForUpgrade(playerCrew, "discord")) {
//				if (crewManager.CheckForChief(playerCrew, p)) {
//					int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("discord");
//					if(crewManager.getVault(playerCrew) >= purchasePrice) {
//						getDiscordName = p;
//						p.sendMessage(ChatColor.DARK_BLUE + "[\uD83C\uDFA7]" + ChatColor.BLUE + " Please type/enter your discord username: ");
//						p.closeInventory();
//					} else {
//                        ChatUtilities.NeedMoreSponges(p);
//					}
//				} else {
//                    ChatUtilities.MustBeChief(p);
//				}
//			} else {
//                ChatUtilities.UpgradeAlreadyUnlocked(p);
//			}
//		}
//
//		if (clickedItem.getType() == Material.BAMBOO_SIGN) {
//			String playerCrew = crewManager.getPlayercrew(p);
//			if (!upgradeManager.checkForUpgrade(playerCrew, "chat")) {
//				if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
//					int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("chat");
//					if(crewManager.getVault(playerCrew) >= purchasePrice) {
//						crewManager.removeFromVault(playerCrew, purchasePrice, p);
//                        ChatUtilities.UpgradeSuccessful(playerCrew, "chat");
//						upgradeManager.editUpgrade(playerCrew, "chat", true);
//						p.closeInventory();
//					} else {
//                        ChatUtilities.NeedMoreSponges(p);
//					}
//				} else {
//                    ChatUtilities.MustBeChiefOrElder(p);
//				}
//			} else {
//                ChatUtilities.UpgradeAlreadyUnlocked(p);
//			}
//		}
//
//		//NEED TO TAKE MONEY FROM crew WHEN BUYING UPGRADES!!!!!!!!!!!!!!!!!!!!!!!!
//
//		if (clickedItem.getType() == Material.FILLED_MAP) {
//			String playerCrew = crewManager.getPlayercrew(p);
//			if (!upgradeManager.checkForUpgrade(playerCrew, "mail")) {
//				if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
//					int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("mail");
//					if(crewManager.getVault(playerCrew) >= purchasePrice) {
//						crewManager.removeFromVault(playerCrew, purchasePrice, p);
//                        ChatUtilities.UpgradeSuccessful(playerCrew, "mail");
//						upgradeManager.editUpgrade(playerCrew, "mail", true);
//						p.closeInventory();
//					} else {
//                        ChatUtilities.NeedMoreSponges(p);
//					}
//				} else {
//                    ChatUtilities.MustBeChiefOrElder(p);
//				}
//			} else {
//                ChatUtilities.UpgradeAlreadyUnlocked(p);
//			}
//		}
//
//		if (clickedItem.getType() == Material.NAME_TAG) {
//			String playerCrew = crewManager.getPlayercrew(p);
//			if (crewManager.CheckForChief(playerCrew, p)) {
//				int purchasePrice = crewsClass.getPrices().getInt("changename");
//				if(crewManager.getVault(playerCrew) >= purchasePrice) {
//					waitingForInputPlayer = p;
//					p.sendMessage(ChatColor.YELLOW + "[\uD83D\uDD8A]" + ChatColor.GOLD + " Please type/enter your new crew name: ");
//					p.closeInventory();
//				} else {
//                    ChatUtilities.NeedMoreSponges(p);
//				}
//			} else {
//                ChatUtilities.MustBeChief(p);
//			}
//
//		}
//	}
//
//	@EventHandler
//	public void onInventoryClick(final InventoryDragEvent event) {
//		if(event.getInventory().equals(inventories.get(event.getWhoClicked().getUniqueId()))) {
//			event.setCancelled(true);
//		}
//	}
//
//    @EventHandler
//    public void onInventoryClose(final InventoryCloseEvent e, Player p) {
//    	HandlerList.unregisterAll(this);
//    	Bukkit.getServer().getScheduler().runTaskLater(Crews.getPlugin(Crews.class), p::updateInventory, 1L);
//    }
}
