package org.diffvanilla.crews.commands.subcommands;

import java.util.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
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
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if(args.length >= 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (args.length == 0) {
            if (pCrew != null) {
                Crew c = plugin.getData().getCrewOrError(p);
                inv = Bukkit.createInventory(null, 54, ChatUtilities.tribalGames.toString() + ChatColor.BOLD + pCrew.getName() + " Crew Profile");
                UUID playerUUID = p.getUniqueId();
                inventories.put(playerUUID, inv);
                initializeItems(pCrew);
                openInventory(p, inventories.get(playerUUID));
                c.showInfo(p, true);
            } else {
                p.sendMessage(ConfigManager.NOT_IN_CREW);
                return;
            }
        }
		if(args.length == 1) {
            Crew tCrew = data.getCrew(args[0]);
            if (tCrew != null) {
                tCrew.showInfo(p, false);
            } else {
                p.sendMessage(ConfigManager.CREW_NOT_FOUND);
//				p.sendMessage(org.bukkit.ChatColor.DARK_RED + "[✖] " + ChatColor.RED + "You are not in a cr ew! " +
//						"Receive an invite from a crew or create your own: " + ChatColor.YELLOW + "/crews create [crew]");
            }
        }
	}
	public void initializeItems(Crew crew) {
        String crewName = crew.getName();
		String dateCreated = crew.getDateFounded();
		int level = crew.getLevel();
		int vault = crew.getVault();
		int requiredSponges = crew.getLevelUpCost();
		int influence = crew.getInfluence();
		//OfflinePlayer chief = Bukkit.getServer().getOfflinePlayer(crewManager.getChief(crew));
		
		String boss = Bukkit.getOfflinePlayer(UUID.fromString(crew.getBoss())).getName();
		
		Material[] levelItems = {Material.COAL, Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE
				, Material.LAPIS_LAZULI, Material.EMERALD, Material.DIAMOND, Material.NETHERITE_INGOT, Material.NETHER_STAR};

		TextColor requiredColor;
		if(vault >= requiredSponges){
			requiredColor = UnicodeCharacters.compound_active;
		} else {
			requiredColor = UnicodeCharacters.compound_inactive;
		}

        TextComponent crewNameComponent = Component.text(crewName).color(UnicodeCharacters.plugin_color).decorate(TextDecoration.BOLD);
        TextComponent foundedComponent = createEmojiComponent(UnicodeCharacters.founded_emoji, "Date Founded: ", UnicodeCharacters.info_text_color, dateCreated, UnicodeCharacters.founded_color);
        TextComponent levelComponent = createEmojiComponent(UnicodeCharacters.level_emoji, "Level: ", UnicodeCharacters.info_text_color, String.valueOf(level), UnicodeCharacters.level_color);
        TextComponent vaultComponent = createEmojiComponent(UnicodeCharacters.vault_emoji, "Vault: ", UnicodeCharacters.info_text_color, UnicodeCharacters.sponge_icon + vault, UnicodeCharacters.sponge_color);

        String xpText = requiredSponges != -1 ? UnicodeCharacters.sponge_icon + requiredSponges : "MAX LEVEL";
        TextColor xpColor = requiredSponges != -1 ? requiredColor : UnicodeCharacters.sponge_color;
        TextComponent xpComponent = createEmojiComponent(UnicodeCharacters.xp_emoji, "Cost to upgrade: ", UnicodeCharacters.info_text_color, xpText, xpColor);

        inv.setItem(4, createGuiItem(levelItems[level-1], crewNameComponent,
            foundedComponent,
            levelComponent,
            vaultComponent,
            xpComponent,
            Component.text(""),
            Component.text("Ranking: "),
            Component.text(""),
            createComponentWithDecoration("Crew influence is the sum of the crew's", NamedTextColor.GRAY, TextDecoration.ITALIC),
            createComponentWithDecoration("rating, vault balance, and player power.", NamedTextColor.GRAY, TextDecoration.ITALIC),
            createComponentWithDecoration("(300 Influence per player)", NamedTextColor.GRAY, TextDecoration.ITALIC),
            Component.text(""),
            createInfluenceComponent(influence)));

        String status;
		TextColor color;
		if(crew.hasCompound()) {
            status = "ACTIVE";
			color = NamedTextColor.GREEN;
		} else {
            status = "INACTIVE";
            color = NamedTextColor.RED;
		}
		
		inv.setItem(19, createGuiItem(Material.COMPASS,
        createComponentWithDecoration("CREW COMPOUND", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
        createComponent("Click to Warp", NamedTextColor.GRAY),
        Component.text(""),
        createComponentWithPlaceHolder("Status: ", NamedTextColor.GRAY, status, color)));
		
		inv.setItem(21, createGuiItem(Material.DIAMOND_HELMET,
        createComponentWithDecoration("CREW LEADERSHIP", NamedTextColor.GOLD, TextDecoration.BOLD),
        createEmojiComponent(UnicodeCharacters.boss_emoji, "Boss: ", UnicodeCharacters.info_text_color, boss, UnicodeCharacters.boss_color),
        createEmojiComponent(UnicodeCharacters.enforcers_emoji, "Enforcers: ", UnicodeCharacters.info_text_color, "", UnicodeCharacters.enforcers_color)));

        if(crew.getEnforcers() != null) {
            for (String enforcer : crew.getEnforcers()) {
                String pName = Bukkit.getServer().getOfflinePlayer(UUID.fromString(enforcer)).getName();
                createComponent("- " + pName, UnicodeCharacters.enforcers_color);
            }
        }
		
//		inv.setItem(23, createGuiItem(Material.SLIME_BALL, ChatUtilities.tribalGames.toString() + ChatColor.BOLD + "CREW GAMES",
//				ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "Wins:",
//				ChatColor.DARK_GRAY + UnicodeCharacters.flag + ChatColor.GRAY + " CTF: " + ChatColor.GOLD + 0,
//				ChatColor.DARK_GRAY + UnicodeCharacters.koth + ChatColor.GRAY + " KOTH: " + ChatColor.GOLD + 0,
//				ChatColor.DARK_GRAY + UnicodeCharacters.tott + ChatColor.GRAY + " Turf Wars: " + ChatColor.GOLD + 0));

        List<String> unlockedUpgrades = crew.getUnlockedUpgrades();
        //can have nothing or ["discord", "chat", "mail"]

		inv.setItem(25, createGuiItem(Material.SPONGE,
            Component.text(UnicodeCharacters.upgrade_emoji).color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD)
                .append(Component.text(" CREW UPGRADE SHOP ").color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD)
                .append(Component.text(UnicodeCharacters.upgrade_emoji).color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD))),
            Component.text(""),
            createComponent("Upgrades", NamedTextColor.WHITE),
            createDoubleEmojiComponent(UnicodeCharacters.crew_chat_emoji, NamedTextColor.DARK_GREEN, "Private Chat", NamedTextColor.DARK_GREEN, "icon here", NamedTextColor.DARK_GREEN),
            createDoubleEmojiComponent(UnicodeCharacters.discord_emoji, NamedTextColor.BLUE, "Private Discord", NamedTextColor.BLUE, "icon here", NamedTextColor.DARK_GREEN),
            createDoubleEmojiComponent(UnicodeCharacters.mail_emoji, NamedTextColor.AQUA, "Crew Mail", NamedTextColor.AQUA, "icon here", NamedTextColor.DARK_GREEN)));
		
		inv.setItem(8, createGuiItem(Material.BARRIER,
            createComponentWithDecoration("EXIT", NamedTextColor.RED, TextDecoration.BOLD),
                createComponent("Click to exit", NamedTextColor.GRAY)));

        List<String> crewMembers = crew.getMembers();
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
	
	protected static ItemStack createGuiItem(final Material material, final TextComponent name, final TextComponent... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		
		//set the name of item
		meta.displayName(name);
		
		//set lore of item
		meta.lore(Arrays.asList(lore));
		
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

    private TextComponent createEmojiComponent(String emoji, String titleLabel, TextColor labelColor, String titleText, TextColor titleColor) {
        return Component.text(emoji).color(UnicodeCharacters.emoji_text_color)
            .append(Component.text(titleLabel).color(labelColor)
            .append(Component.text(titleText).color(titleColor))).decoration(TextDecoration.ITALIC, false);
    }

    private TextComponent createInfluenceComponent(int influence) {
        return Component.text(UnicodeCharacters.influence_emoji).color(UnicodeCharacters.emoji_text_color)
        .append(Component.text("Influence: ").color(UnicodeCharacters.info_text_color)
        .append(Component.text("[").color(UnicodeCharacters.influence_outline_color))
        .append(Component.text(influence).color(UnicodeCharacters.influence_color))
        .append(Component.text("]").color(UnicodeCharacters.influence_outline_color)))
            .decoration(TextDecoration.ITALIC, false);
    }

    private TextComponent createDoubleEmojiComponent(String emoji, TextColor emojiColor, String titleLabel, TextColor labelColor, String unlockEmoji, TextColor unlockColor) {
        return Component.text(emoji).color(emojiColor)
            .append(Component.text(titleLabel).color(labelColor)
                .append(Component.text(unlockEmoji).color(unlockColor))).decoration(TextDecoration.ITALIC, false);
    }

    private TextComponent createComponentWithDecoration(String text, TextColor color, TextDecoration decoration) {
        return Component.text(text).color(color).decorate(decoration).decoration(TextDecoration.ITALIC, false);
    }

    private TextComponent createComponent(String text, TextColor color) {
        return Component.text(text).color(color).decoration(TextDecoration.ITALIC, false);
    }

    private TextComponent createComponentWithPlaceHolder(String text, TextColor color, String placeholder, TextColor placeholderColor) {
        return Component.text(text).color(color).append(Component.text(placeholder).color(placeholderColor)).decoration(TextDecoration.ITALIC, false);
    }
}
