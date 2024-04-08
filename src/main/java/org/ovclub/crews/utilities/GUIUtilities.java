package org.ovclub.crews.utilities;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GUIUtilities {

    public static void initializeCrewInfoItems(Crew crew, Inventory inv) {
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
        TextComponent foundedComponent = ComponentUtilities.createEmojiComponent(UnicodeCharacters.founded_emoji, "Date Founded: ", UnicodeCharacters.info_text_color, dateCreated, UnicodeCharacters.founded_color);
        TextComponent levelComponent = ComponentUtilities.createEmojiComponent(UnicodeCharacters.level_emoji, "Level: ", UnicodeCharacters.info_text_color, String.valueOf(level), UnicodeCharacters.level_color);
        TextComponent vaultComponent = ComponentUtilities.createEmojiComponent(UnicodeCharacters.vault_emoji, "Vault: ", UnicodeCharacters.info_text_color, UnicodeCharacters.sponge_icon + vault, UnicodeCharacters.sponge_color);

        String xpText = requiredSponges != -1 ? UnicodeCharacters.sponge_icon + requiredSponges : "MAX LEVEL";
        TextColor xpColor = requiredSponges != -1 ? requiredColor : UnicodeCharacters.sponge_color;
        TextComponent xpComponent = ComponentUtilities.createEmojiComponent(UnicodeCharacters.xp_emoji, "Cost to upgrade: ", UnicodeCharacters.info_text_color, xpText, xpColor);

        inv.setItem(4, createGuiItem(levelItems[level-1], crewNameComponent,
            foundedComponent,
            levelComponent,
            vaultComponent,
            xpComponent,
            Component.text(""),
            Component.text("Ranking: "),
            Component.text(""),
            ComponentUtilities.createComponentWithDecoration("Crew influence is the sum of the crew's", NamedTextColor.GRAY, TextDecoration.ITALIC),
            ComponentUtilities.createComponentWithDecoration("rating, vault balance, and player power.", NamedTextColor.GRAY, TextDecoration.ITALIC),
            ComponentUtilities.createComponentWithDecoration("(300 Influence per player)", NamedTextColor.GRAY, TextDecoration.ITALIC),
            Component.text(""),
            ComponentUtilities.createInfluenceComponent(influence)));

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
            ComponentUtilities.createComponentWithDecoration("CREW COMPOUND", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to Warp", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Status: ", NamedTextColor.GRAY, status, color)));

        inv.setItem(21, createGuiItem(Material.DIAMOND_HELMET,
            ComponentUtilities.createComponentWithDecoration("CREW LEADERSHIP", NamedTextColor.GOLD, TextDecoration.BOLD),
            ComponentUtilities.createEmojiComponent(UnicodeCharacters.boss_emoji, "Boss: ", UnicodeCharacters.info_text_color, boss, UnicodeCharacters.boss_color),
            ComponentUtilities.createEmojiComponent(UnicodeCharacters.enforcers_emoji, "Enforcers: ", UnicodeCharacters.info_text_color, "", UnicodeCharacters.enforcers_color)));

        if(crew.getEnforcers() != null) {
            for (String enforcer : crew.getEnforcers()) {
                String pName = Bukkit.getServer().getOfflinePlayer(UUID.fromString(enforcer)).getName();
                ComponentUtilities.createComponent("- " + pName, UnicodeCharacters.enforcers_color);
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
            Component.text(UnicodeCharacters.shop_emoji).color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD)
                .append(Component.text(" CREW UPGRADE SHOP ").color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD)
                    .append(Component.text(UnicodeCharacters.shop_emoji).color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD))),
            Component.text(""),
            ComponentUtilities.createComponent("Upgrades", NamedTextColor.WHITE),
            ComponentUtilities.createDoubleEmojiComponent(UnicodeCharacters.crew_chat_emoji, NamedTextColor.DARK_GREEN, "Private Chat", NamedTextColor.DARK_GREEN, "icon here", NamedTextColor.DARK_GREEN),
            ComponentUtilities.createDoubleEmojiComponent(UnicodeCharacters.discord_emoji, NamedTextColor.BLUE, "Private Discord", NamedTextColor.BLUE, "icon here", NamedTextColor.DARK_GREEN),
            ComponentUtilities.createDoubleEmojiComponent(UnicodeCharacters.mail_emoji, NamedTextColor.AQUA, "Crew Mail", NamedTextColor.AQUA, "icon here", NamedTextColor.DARK_GREEN)));

        inv.setItem(8, createGuiItem(Material.BARRIER,
            ComponentUtilities.createComponentWithDecoration("EXIT", NamedTextColor.RED, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to exit", NamedTextColor.GRAY)));

        List<String> crewMembers = crew.getMembers();
        String[] memberArray = crewMembers.toArray(new String[0]);

        Component bossName = Component.text(boss, NamedTextColor.GOLD);
        getCrewMemberSkull(38, UUID.fromString(crew.getBoss()), bossName, inv);
        int counter = 1;
        for (int i = 39; i < 53; i++) {
            if (i == 43) {
                i = 46;
            }
            if (counter < memberArray.length) {
                UUID memberUUID = UUID.fromString(memberArray[counter]);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(memberUUID);
                String memberIGN = offlinePlayer.getName();
                Component displayName = Component.text(memberIGN, NamedTextColor.GOLD);

                getCrewMemberSkull(i, memberUUID, displayName, inv, Component.text("Click to select this member for the queue."));
            } else if (counter < crew.getLevel() + 2) {
                UUID empty = Bukkit.getOfflinePlayer("MHF_Redstone").getUniqueId();
                getCrewMemberSkull(i, empty, Component.text("EMPTY", NamedTextColor.GREEN), inv);
            } else {
                UUID filled = Bukkit.getOfflinePlayer("Trajan").getUniqueId();
                getCrewMemberSkull(i, filled, Component.text("LOCKED", NamedTextColor.RED), inv,
                    Component.text("Unlock at level " + (counter - 1), NamedTextColor.RED));
            }
            counter++;
        }
    }

    public static void initializeCrewShopItems(Crew pCrew, Inventory inv) {
        inv.setItem(14, createGuiItem(Material.BAMBOO_SIGN,
            ComponentUtilities.createComponent("Private Crew Chat", NamedTextColor.DARK_GREEN),
            ComponentUtilities.createComponent("Crew members will have access to /crews chat", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_CHAT_COST, UnicodeCharacters.sponge_color),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, pCrew.getUnlockedUpgrades().contains("chat") ? "True" : "False", pCrew.getUnlockedUpgrades().contains("chat") ? NamedTextColor.GREEN : NamedTextColor.RED)));

        inv.setItem(16, createGuiItem(Material.LAPIS_BLOCK,
            ComponentUtilities.createComponent("Discord (Voice & Text) Channels", NamedTextColor.BLUE),
            ComponentUtilities.createComponent("Two private crew channels on the discord server.", NamedTextColor.GRAY),
            ComponentUtilities.createComponent("Useful to have a place of gathering outside the server.", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_DISCORD_COST, UnicodeCharacters.sponge_color),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, pCrew.getUnlockedUpgrades().contains("discord") ? "True" : "False", pCrew.getUnlockedUpgrades().contains("discord") ? NamedTextColor.GREEN : NamedTextColor.RED)));

        inv.setItem(12, createGuiItem(Material.FILLED_MAP,
            ComponentUtilities.createComponent("Crew Mail", NamedTextColor.YELLOW),
            ComponentUtilities.createComponent("Send messages to offline crew members.", NamedTextColor.GRAY),
            ComponentUtilities.createComponent("Good for offline communication.", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_MAIL_COST, UnicodeCharacters.sponge_color),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, pCrew.getUnlockedUpgrades().contains("mail") ? "True" : "False", pCrew.getUnlockedUpgrades().contains("mail") ? NamedTextColor.GREEN : NamedTextColor.RED)));
//
//        inv.setItem(10, createGuiItem(Material.NAME_TAG, ChatColor.DARK_AQUA + "Change crew Name",
//            ChatColor.GRAY + "Change your crew's name.",
//            "",
//            ChatColor.GRAY + "Cost: " + ChatUtilities.spongeColor + UnicodeCharacters.sponge_icon + ConfigManager.RENAME_COST));
    }

    public static void initializeTurfWarQueueItems(Inventory inv, int queueSize) {
        NamedTextColor queueSizeColor = queueSize > 0 ? NamedTextColor.DARK_GREEN : NamedTextColor.DARK_GRAY;
        inv.setItem(4, createGuiItem(Material.DIAMOND_SWORD,
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.turfwar_emoji + "Turf Wars", UnicodeCharacters.turfwar_color, " - Queue", NamedTextColor.GRAY),
            Component.text(""),
            Component.text("There are currently ").color(NamedTextColor.GRAY)
                .append(Component.text(queueSize).color(queueSizeColor)
                .append(Component.text(" crews in queue!").color(NamedTextColor.GRAY))).decoration(TextDecoration.ITALIC, false),
            Component.text(""),
            ComponentUtilities.createComponent("Click to join the queue!", UnicodeCharacters.economy_color)));
    }

    public static void initializeTurfWarSelectPlayerItems(Crew pCrew, Inventory inv) {
        ArrayList<String> onlineMembers = pCrew.getOnlinePlayerUUIDs();
        int[] slots = new int[]{0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21};

        for (int i = 0; i < onlineMembers.size() && i < slots.length; i++) {
            String memberUUIDString = onlineMembers.get(i);
            Player player = Bukkit.getPlayer(UUID.fromString(memberUUIDString));
            if (player != null) {
                UUID pUUID = player.getUniqueId();
                Component displayName = Component.text(player.getName());
                Component[] lore = new Component[]{Component.text("Click to select this member for the queue.").color(NamedTextColor.GRAY)};
                getCrewMemberSkull(slots[i], pUUID, displayName, inv, lore);
            }
        }

        inv.setItem(8, createGuiItem(Material.BARRIER,
            ComponentUtilities.createComponentWithDecoration("EXIT", NamedTextColor.RED, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to exit.", NamedTextColor.GRAY)));

        inv.setItem(15, createGuiItem(Material.DIAMOND_SWORD,
            ComponentUtilities.createComponentWithDecoration("JOIN QUEUE", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to join the queue.", NamedTextColor.GRAY)));
    }

    protected static ItemStack createGuiItem(final Material material, final TextComponent name, final TextComponent... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(Arrays.asList(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        return item;
    }
    public static void getCrewMemberSkull(int slot, UUID playerUUID, Component displayName, Inventory inventory, final Component... lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            // Fetch the player's profile using the UUID
            PlayerProfile profile = Bukkit.createProfile(playerUUID);
            meta.setPlayerProfile(profile);

            meta.displayName(displayName);
            meta.lore(Arrays.asList(lore));
            skull.setItemMeta(meta);
        }

        inventory.setItem(slot, skull);
    }
    public static ItemStack createGuiSkull(final Component IGN, final Component displayName, final Component... lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        ItemMeta meta = skull.getItemMeta();
        if (meta != null) {

            meta.displayName(displayName);
            meta.lore(Arrays.asList(lore));
            skull.setItemMeta(meta);
        }

        return skull;
    }
    public static void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
}
