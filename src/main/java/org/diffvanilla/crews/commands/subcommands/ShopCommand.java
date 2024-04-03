package org.diffvanilla.crews.commands.subcommands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.UnicodeCharacters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopCommand implements SubCommand {

	private static Inventory inv;
	public static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();

	public static Player waitingForInputPlayer = null;
	public static Player getDiscordName = null;

//    private CrewManager crewManager = new CrewManager();

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
        if (args.length != 0) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }

        inv = Bukkit.createInventory(null, 27, ChatColor.YELLOW.toString() + ChatColor.BOLD + "\uD83D\uDCB2 crew UPGRADE SHOP \uD83D\uDCB2");
        UUID playerUUID = p.getUniqueId();
        inventories.put(playerUUID, inv);
        initializeItems(pCrew);
        openInventory(p, inventories.get(playerUUID));
    }

	public void initializeItems(Crew pCrew) {
		inv.setItem(14, createGuiItem(Material.BAMBOO_SIGN, ChatColor.DARK_GREEN + "Private crew Chat",
				ChatColor.GRAY + "crew members will have access to /crew chat",
				"",
				ChatColor.GRAY + "Cost: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_CHAT_COST,
				"",
				ChatColor.GRAY + "Purchased: " + ((pCrew.getUnlockedUpgrades().contains("chat")) ? ChatColor.GREEN + "true" : ChatColor.RED + "false")));

		inv.setItem(16, createGuiItem(Material.LAPIS_BLOCK, ChatColor.BLUE + "Discord (Voice & Text) Channels",
				ChatColor.GRAY + "Two private crew channels on the discord server.",
				ChatColor.GRAY + "Useful to have a place of gathering outside the server.",
				"",
				ChatColor.GRAY + "Cost: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_DISCORD_COST,
				"",
				ChatColor.GRAY + "Purchased: " + ((pCrew.getUnlockedUpgrades().contains("discord")) ? ChatColor.GREEN + "true" : ChatColor.RED + "false")));

		inv.setItem(12, createGuiItem(Material.FILLED_MAP, ChatColor.YELLOW + "crew Mail",
				ChatColor.GRAY + "Send messages to offline crew members.",
				ChatColor.GRAY + "Good for offline communication.",
				"",
				ChatColor.GRAY + "Cost: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_MAIL_COST,
				"",
				ChatColor.GRAY + "Purchased: " + ((pCrew.getUnlockedUpgrades().contains("mail")) ? ChatColor.GREEN + "true" : ChatColor.RED + "false")));

		inv.setItem(10, createGuiItem(Material.NAME_TAG, ChatColor.DARK_AQUA + "Change crew Name",
				ChatColor.GRAY + "Change your crew's name.",
				"",
				ChatColor.GRAY + "Cost: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge_icon + ConfigManager.RENAME_COST));
	}
	
	protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		
		//set the name of item
		meta.setDisplayName(name);
		
		//set lore of item
		meta.setLore(Arrays.asList(lore));
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public void openInventory(final HumanEntity ent, Inventory inv) {
		ent.openInventory(inv);
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
