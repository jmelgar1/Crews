package org.diffvanilla.crews.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.diffvanilla.crews.object.Crew;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIUtilities {

    public static void initializeItems(Crew crew, Inventory inv) {
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

        String underBoss = "";
        if(crew.getBoss() != null) {
            underBoss = Bukkit.getServer().getOfflinePlayer(crew.getUnderBoss()).getName();
        } else {
            underBoss = "NONE";
        }

        List<UUID> crewMembers = crew.getMembers();

        Material[] levelItems = {Material.COAL, Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT, Material.REDSTONE
            , Material.LAPIS_LAZULI, Material.EMERALD, Material.DIAMOND, Material.NETHERITE_INGOT, Material.NETHER_STAR};

        net.md_5.bungee.api.ChatColor requiredColor = null;
        if(vault >= requiredSponges){
            requiredColor = net.md_5.bungee.api.ChatColor.GREEN;
        } else {
            requiredColor = net.md_5.bungee.api.ChatColor.RED;
        }
        if(requiredSponges != -1) {
            inv.setItem(4, createGuiItem(levelItems[level-1], ChatUtilities.tribalGames.toString() + net.md_5.bungee.api.ChatColor.BOLD + crewName.toUpperCase(),
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.foundedDate + net.md_5.bungee.api.ChatColor.GRAY + " Date Founded: " + net.md_5.bungee.api.ChatColor.WHITE + dateCreated,
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.level + net.md_5.bungee.api.ChatColor.GRAY + " Level: " + net.md_5.bungee.api.ChatColor.WHITE + level,
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.vault + net.md_5.bungee.api.ChatColor.GRAY + " Vault: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge + vault,
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.xp + net.md_5.bungee.api.ChatColor.GRAY + " Cost to upgrade: " + requiredColor + UnicodeCharacters.sponge + requiredSponges,
                "",
                net.md_5.bungee.api.ChatColor.GOLD.toString() + net.md_5.bungee.api.ChatColor.UNDERLINE + "Scores:",
                net.md_5.bungee.api.ChatColor.YELLOW + UnicodeCharacters.rating + net.md_5.bungee.api.ChatColor.GOLD + " Rating: " + net.md_5.bungee.api.ChatColor.YELLOW + ratingScore,
                net.md_5.bungee.api.ChatColor.GREEN + UnicodeCharacters.economy + net.md_5.bungee.api.ChatColor.DARK_GREEN + " Economy Score: " + net.md_5.bungee.api.ChatColor.GREEN + economyScore,
                net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + UnicodeCharacters.powerScore + net.md_5.bungee.api.ChatColor.DARK_PURPLE + " Influence: " + net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + influence));
        } else {
            inv.setItem(4, createGuiItem(levelItems[level-1], ChatUtilities.tribalGames.toString() + net.md_5.bungee.api.ChatColor.BOLD + crewName.toUpperCase(),
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.foundedDate + net.md_5.bungee.api.ChatColor.GRAY + " Date Founded: " + net.md_5.bungee.api.ChatColor.WHITE + dateCreated,
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.level + net.md_5.bungee.api.ChatColor.GRAY + " Level: " + net.md_5.bungee.api.ChatColor.WHITE + level,
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.vault + net.md_5.bungee.api.ChatColor.GRAY + " Vault: " + net.md_5.bungee.api.ChatColor.WHITE + UnicodeCharacters.sponge + vault,
                net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.xp + net.md_5.bungee.api.ChatColor.GRAY + " Cost to upgrade: " + ChatUtilities.spongeColor + "MAX LEVEL",
                "",
                net.md_5.bungee.api.ChatColor.YELLOW + UnicodeCharacters.rating + net.md_5.bungee.api.ChatColor.GOLD + " Rating: " + net.md_5.bungee.api.ChatColor.YELLOW + ratingScore,
                net.md_5.bungee.api.ChatColor.GREEN + UnicodeCharacters.economy + net.md_5.bungee.api.ChatColor.DARK_GREEN + " Economy: " + net.md_5.bungee.api.ChatColor.GREEN + economyScore,
                net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + UnicodeCharacters.powerScore + net.md_5.bungee.api.ChatColor.DARK_PURPLE + " Power Score: " + net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + influence));
        }

        String status;
        if(crew.hasCompound()) {
            status = net.md_5.bungee.api.ChatColor.GREEN + "ACTIVE";
        } else {
            status = net.md_5.bungee.api.ChatColor.RED + "INACTIVE";
        }

        inv.setItem(19, createGuiItem(Material.COMPASS, net.md_5.bungee.api.ChatColor.LIGHT_PURPLE.toString() + net.md_5.bungee.api.ChatColor.BOLD + "CREW COMPOUND",
            net.md_5.bungee.api.ChatColor.GRAY + "Click to Warp",
            "",
            net.md_5.bungee.api.ChatColor.GRAY + "Status: " + status));

        inv.setItem(21, createGuiItem(Material.DIAMOND_HELMET, net.md_5.bungee.api.ChatColor.GOLD.toString() + net.md_5.bungee.api.ChatColor.BOLD + "CREW LEADERSHIP",
            net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.chiefCrown + net.md_5.bungee.api.ChatColor.GRAY + " Boss: " + net.md_5.bungee.api.ChatColor.DARK_RED + boss,
            net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.elderFace + net.md_5.bungee.api.ChatColor.GRAY + " Underboss: " + net.md_5.bungee.api.ChatColor.DARK_PURPLE + underBoss));

        inv.setItem(23, createGuiItem(Material.SLIME_BALL, ChatUtilities.tribalGames.toString() + net.md_5.bungee.api.ChatColor.BOLD + "CREW GAMES",
            net.md_5.bungee.api.ChatColor.YELLOW.toString() + net.md_5.bungee.api.ChatColor.UNDERLINE + "Wins:",
            net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.flag + net.md_5.bungee.api.ChatColor.GRAY + " CTF: " + net.md_5.bungee.api.ChatColor.GOLD + 0,
            net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.koth + net.md_5.bungee.api.ChatColor.GRAY + " KOTH: " + net.md_5.bungee.api.ChatColor.GOLD + 0,
            net.md_5.bungee.api.ChatColor.DARK_GRAY + UnicodeCharacters.tott + net.md_5.bungee.api.ChatColor.GRAY + " TOTT: " + net.md_5.bungee.api.ChatColor.GOLD + 0));

        inv.setItem(25, createGuiItem(Material.SPONGE, net.md_5.bungee.api.ChatColor.YELLOW.toString() + net.md_5.bungee.api.ChatColor.BOLD + "\uD83D\uDCB2 CREW UPGRADE SHOP \uD83D\uDCB2",
            "", net.md_5.bungee.api.ChatColor.WHITE + "Upgrades: "
                + net.md_5.bungee.api.ChatColor.DARK_GREEN + "Private Chat" + net.md_5.bungee.api.ChatColor.GRAY + ", " + net.md_5.bungee.api.ChatColor.BLUE + "Private Discord" + net.md_5.bungee.api.ChatColor.GRAY + ", ",
            net.md_5.bungee.api.ChatColor.YELLOW + "crews Mail" + net.md_5.bungee.api.ChatColor.GRAY + ", " + net.md_5.bungee.api.ChatColor.AQUA + "Rename crew" + net.md_5.bungee.api.ChatColor.GRAY + "!"));

        inv.setItem(8, createGuiItem(Material.BARRIER, net.md_5.bungee.api.ChatColor.RED.toString() + net.md_5.bungee.api.ChatColor.BOLD + "EXIT",
            net.md_5.bungee.api.ChatColor.GRAY + "Click to exit"));

        String[] memberArray = crewMembers.toArray(new String[0]);

        getcrewMemberSkull(38, boss, net.md_5.bungee.api.ChatColor.GOLD + boss, inv);
        int counter = 1;
        for(int i = 39; i < 53; i++) {

            if(i == 43) {
                i = 46;
            }

            if(counter < memberArray.length) {
                String memberIGN = Bukkit.getServer().getOfflinePlayer(UUID.fromString(memberArray[counter])).getName();
                getcrewMemberSkull(i, memberIGN, net.md_5.bungee.api.ChatColor.GOLD + memberIGN, inv);
                //compare the counter value and level
            } else if(counter < crew.getLevel()+2){
                getcrewMemberSkull(i, "Trajan", net.md_5.bungee.api.ChatColor.GREEN + "EMPTY", inv);
            } else {
                getcrewMemberSkull(i, "MHF_Redstone", net.md_5.bungee.api.ChatColor.RED+ "LOCKED", inv, net.md_5.bungee.api.ChatColor.RED + "Unlock at level " + (counter-1));
            }

            counter++;
        }
    }

    public static void getcrewMemberSkull(int i, String IGN, String displayName, Inventory inv, final String...lore) {
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

    public static void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
}
