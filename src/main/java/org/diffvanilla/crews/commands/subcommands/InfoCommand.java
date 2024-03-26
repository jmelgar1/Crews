package org.diffvanilla.crews.commands.subcommands;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.UnicodeCharacters;

public class InfoCommand implements SubCommand {
	
	private static Inventory inv;
	public static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "See your crew's info.";
	}

    @Override
    public String getSyntax() {
        return "/crew info";
    }

    @Override
    public String getPermission() {
        return "crew.player.info";
    }

    @Override
	public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        if (args.length == 0) {
            Crew c = plugin.getData().getCrewOrError(p);
            c.showInfo(p);
            return;
        }

		//player's crew
		if(args.length == 1) {
            Crew playerCrew = plugin.getData().getCrew(p);
			if(playerCrew != null) {

	        	inv = Bukkit.createInventory(null, 54, ChatUtilities.tribalGames.toString() + ChatColor.BOLD + playerCrew.getName() + " crew Profile");
				UUID playerUUID = p.getUniqueId();
				inventories.put(playerUUID, inv);
				initializeItems(p, playerCrew);
				openInventory(p, inventories.get(playerUUID));

                plugin.getData().getCrew(p).showInfo(p);
				//JsonObject crewsection = crewsFile.getAsJsonObject(playerCrew);
				//crewManager.getcrewInfo(crewsFile, crewsection, playerCrew, p, false);

			} else {
				p.sendMessage(org.bukkit.ChatColor.DARK_RED + "[✖] " + ChatColor.RED + "You are not in a crew! " +
						"Receive an invite from a crew or create your own: " + ChatColor.YELLOW + "/crews create [crew]");
			}

			//other players crew
		} else if (args.length == 2) {
			p.sendMessage(ChatUtilities.usageIcon + ChatColor.WHITE + "To view another crew's information please use "
					+ ChatColor.YELLOW + "/crews lookup [crew]");
		} else {
            //ChatUtilities.CorrectUsage(p, getSyntax());
		}
	}
	
	public void initializeItems(Player p, Crew crew) {
        String crewName = crew.getName();
		String dateCreated = crew.getDateFounded();
		int level = crew.getLevel();
		int vault = crew.getVault();
		int requiredSponges = crew.getLevelUpCost();
		int ratingScore = crew.getRatingScore();
		double economyScore = crew.getEconomyScore();
		double influence = crew.getInfluence();
		//OfflinePlayer chief = Bukkit.getServer().getOfflinePlayer(crewManager.getChief(crew));
		
		String boss = Bukkit.getServer().getOfflinePlayer(crew.getBoss()).getName();

//        List<UUID> enforceres = crew.getEnforceres();
//        //fix this enforcer multiple
//		String enforcer = "";
//		if(crew.getBoss() != null) {
//			enforcer = Bukkit.getServer().getOfflinePlayer(crew.getEnforcer()).getName();
//		} else {
//			enforcer = "NONE";
//		}
		
		Material[] levelItems = {Material.COAL, Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE
				, Material.LAPIS_LAZULI, Material.EMERALD, Material.DIAMOND, Material.NETHERITE_INGOT, Material.NETHER_STAR};

		ChatColor requiredColor = null;
		if(vault >= requiredSponges){
			requiredColor = ChatColor.GREEN;
		} else {
			requiredColor = ChatColor.RED;
		}
		if(requiredSponges != -1) {
			inv.setItem(4, createGuiItem(levelItems[level-1], ChatUtilities.tribalGames.toString() + ChatColor.BOLD + crewName.toUpperCase(),
					ChatColor.DARK_GRAY + UnicodeCharacters.foundedDate + ChatColor.GRAY + " Date Founded: " + ChatColor.WHITE + dateCreated,
					ChatColor.DARK_GRAY + UnicodeCharacters.level + ChatColor.GRAY + " Level: " + ChatColor.WHITE + level,
					ChatColor.DARK_GRAY + UnicodeCharacters.vault + ChatColor.GRAY + " Vault: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge + vault,
					ChatColor.DARK_GRAY + UnicodeCharacters.xp + ChatColor.GRAY + " Cost to upgrade: " + requiredColor + UnicodeCharacters.sponge + requiredSponges,
					"",
					ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Scores:",
					ChatColor.YELLOW + UnicodeCharacters.rating + ChatColor.GOLD + " Rating: " + ChatColor.YELLOW + ratingScore,
					ChatColor.GREEN + UnicodeCharacters.economy + ChatColor.DARK_GREEN + " Economy Score: " + ChatColor.GREEN + economyScore,
					ChatColor.LIGHT_PURPLE + UnicodeCharacters.powerScore + ChatColor.DARK_PURPLE + " Influence: " + ChatColor.LIGHT_PURPLE + influence));
		} else {
			inv.setItem(4, createGuiItem(levelItems[level-1], ChatUtilities.tribalGames.toString() + ChatColor.BOLD + crewName.toUpperCase(),
					ChatColor.DARK_GRAY + UnicodeCharacters.foundedDate + ChatColor.GRAY + " Date Founded: " + ChatColor.WHITE + dateCreated,
					ChatColor.DARK_GRAY + UnicodeCharacters.level + ChatColor.GRAY + " Level: " + ChatColor.WHITE + level,
					ChatColor.DARK_GRAY + UnicodeCharacters.vault + ChatColor.GRAY + " Vault: " + ChatColor.WHITE + UnicodeCharacters.sponge + vault,
					ChatColor.DARK_GRAY + UnicodeCharacters.xp + ChatColor.GRAY + " Cost to upgrade: " + ChatUtilities.spongeColor + "MAX LEVEL",
					"",
					ChatColor.YELLOW + UnicodeCharacters.rating + ChatColor.GOLD + " Rating: " + ChatColor.YELLOW + ratingScore,
					ChatColor.GREEN + UnicodeCharacters.economy + ChatColor.DARK_GREEN + " Economy: " + ChatColor.GREEN + economyScore,
					ChatColor.LIGHT_PURPLE + UnicodeCharacters.powerScore + ChatColor.DARK_PURPLE + " Power Score: " + ChatColor.LIGHT_PURPLE + influence));
		}
		
		String status;
		if(crew.hasCompound()) {
			status = ChatColor.GREEN + "ACTIVE";
		} else {
			status = ChatColor.RED + "INACTIVE";
		}
		
		inv.setItem(19, createGuiItem(Material.COMPASS, ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "CREW COMPOUND",
				ChatColor.GRAY + "Click to Warp",
				"",
				ChatColor.GRAY + "Status: " + status));
		
		inv.setItem(21, createGuiItem(Material.DIAMOND_HELMET, ChatColor.GOLD.toString() + ChatColor.BOLD + "CREW LEADERSHIP",
				ChatColor.DARK_GRAY + UnicodeCharacters.chiefCrown + ChatColor.GRAY + " Boss: " + ChatColor.DARK_RED + boss,
				ChatColor.DARK_GRAY + UnicodeCharacters.elderFace + ChatColor.GRAY + " Enforcer: " + ChatColor.DARK_PURPLE + "enforcer"));
		
		inv.setItem(23, createGuiItem(Material.SLIME_BALL, ChatUtilities.tribalGames.toString() + ChatColor.BOLD + "CREW GAMES",
				ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "Wins:",
				ChatColor.DARK_GRAY + UnicodeCharacters.flag + ChatColor.GRAY + " CTF: " + ChatColor.GOLD + 0,
				ChatColor.DARK_GRAY + UnicodeCharacters.koth + ChatColor.GRAY + " KOTH: " + ChatColor.GOLD + 0,
				ChatColor.DARK_GRAY + UnicodeCharacters.tott + ChatColor.GRAY + " TOTT: " + ChatColor.GOLD + 0));
		
		inv.setItem(25, createGuiItem(Material.SPONGE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "\uD83D\uDCB2 CREW UPGRADE SHOP \uD83D\uDCB2",
				"", ChatColor.WHITE + "Upgrades: "
						+ ChatColor.DARK_GREEN + "Private Chat" + ChatColor.GRAY + ", " + ChatColor.BLUE + "Private Discord" + ChatColor.GRAY + ", ",
				ChatColor.YELLOW + "crews Mail" + ChatColor.GRAY + ", " + ChatColor.AQUA + "Rename crew" + ChatColor.GRAY + "!"));
		
		inv.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED.toString() + ChatColor.BOLD + "EXIT", 
				ChatColor.GRAY + "Click to exit"));

        List<UUID> crewMembers = crew.getMembers();
		String[] memberArray = crewMembers.toArray(new String[0]);
		
		getcrewMemberSkull(38, boss, ChatColor.GOLD + boss);
		int counter = 1;
		for(int i = 39; i < 53; i++) {
			
			if(i == 43) {
				i = 46;
			}
			
			if(counter < memberArray.length) {
				String memberIGN = Bukkit.getServer().getOfflinePlayer(UUID.fromString(memberArray[counter])).getName();
				getcrewMemberSkull(i, memberIGN, ChatColor.GOLD + memberIGN);
				//compare the counter value and level
			} else if(counter < crew.getLevel()+2){
				getcrewMemberSkull(i, "Trajan", ChatColor.GREEN + "EMPTY");
			} else {
				getcrewMemberSkull(i, "MHF_Redstone", ChatColor.RED+ "LOCKED", ChatColor.RED + "Unlock at level " + (counter-1));
			}
			
			counter++;
		}
	}
	
    public static void getcrewMemberSkull(int i, String IGN, String displayName, final String...lore) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(IGN);
		meta.setDisplayName(displayName);
		meta.setLore(Arrays.asList(lore));
		skull.setItemMeta(meta);
		
    	inv.setItem(i, createGuiSkull(IGN, displayName, lore));
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
	
    protected static ItemStack createGuiSkull(final String IGN, final String displayName, final String... lore) {
    	ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
    	final ItemMeta meta = skull.getItemMeta();
    	
    	((SkullMeta) meta).setOwner(IGN);
    	
    	meta.setDisplayName(displayName);
    	
    	meta.setLore(Arrays.asList(lore));
    	
    	skull.setItemMeta(meta);
    	
    	return skull;
    }
	
	public void openInventory(final HumanEntity ent, Inventory inv) {
		ent.openInventory(inv);
	}
}
