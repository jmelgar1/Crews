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
import org.ovclub.crews.object.PlayerData;

import java.util.*;

public class GUICreator {
    public static void createCrewInfoGUI(PlayerData data, Player p, Crew crew) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text(crew.getName() + " - Profile"));
        data.getInventories().put(p.getUniqueId(), inv);

        String crewName = crew.getName();
        String dateCreated = crew.getDateFounded();
        int level = crew.getLevel();
        int vault = crew.getVault();
        int requiredSponges = crew.getLevelUpCost();
        int influence = crew.getInfluence();
        int rating = crew.getRating();
        int skirmishWins = crew.getSkirmishWins();
        int skirmishLosses = crew.getSkirmishLosses();
        int skirmishDraws = crew.getSkirmishDraws();
        ItemStack banner = Optional.ofNullable(crew.getBanner()).orElse(new ItemStack(Material.WHITE_BANNER));
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
            ComponentUtilities.createComponentWithDecoration("(100 Influence per player)", NamedTextColor.GRAY, TextDecoration.ITALIC),
            Component.text(""),
            ComponentUtilities.createInfluenceComponent(influence)));

        inv.setItem(19, createGuiItem(banner,
            ComponentUtilities.createComponentWithDecoration("CREW BANNER", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to change the crew's banner.", NamedTextColor.GRAY)));

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

		inv.setItem(23, createGuiItem(Material.GOLDEN_SWORD,
            ComponentUtilities.createComponentWithDecoration("CREW SKIRMISHES", NamedTextColor.GRAY, TextDecoration.BOLD),
            ComponentUtilities.createEmojiComponent(UnicodeCharacters.rating_emoji, "Rating: ", UnicodeCharacters.info_text_color, String.valueOf(rating), UnicodeCharacters.rating_color),
            ComponentUtilities.createWinLossRatio(UnicodeCharacters.skirmish_emoji, UnicodeCharacters.emoji_text_color, "W/L Record: ", UnicodeCharacters.info_text_color, String.valueOf(skirmishWins), String.valueOf(skirmishDraws), String.valueOf(skirmishLosses))));
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
        getPlayerHead(38, UUID.fromString(crew.getBoss()), bossName, inv);
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

                getPlayerHead(i, memberUUID, displayName, inv, Component.text("Click to select this member for the queue."));
            } else if (counter < crew.getLevel() + 2) {
                UUID empty = Bukkit.getOfflinePlayer("Trajan").getUniqueId();
                getPlayerHead(i, empty, Component.text("EMPTY", NamedTextColor.GREEN), inv);
            } else {
                UUID filled = Bukkit.getOfflinePlayer("MHF_Redstone").getUniqueId();
                getPlayerHead(i, filled, Component.text("LOCKED", NamedTextColor.RED), inv,
                    Component.text("Unlock at level " + (counter - 1), NamedTextColor.RED));
            }
            counter++;
        }
        GUICreator.openInventory(p, data.getInventories().get(p.getUniqueId()));
    }

    public static void createCrewShopGUI(PlayerData data, Player p, Crew crew) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text(UnicodeCharacters.shop_emoji + "CREW UPGRADE SHOP " + UnicodeCharacters.shop_emoji)
            .color(UnicodeCharacters.sponge_color)
            .decorate(TextDecoration.BOLD));
        data.getInventories().put(p.getUniqueId(), inv);

        inv.setItem(14, createGuiItem(Material.BAMBOO_SIGN,
            ComponentUtilities.createComponent("Private Crew Chat", NamedTextColor.DARK_GREEN),
            ComponentUtilities.createComponent("Crew members will have access to /crews chat", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_CHAT_COST, UnicodeCharacters.sponge_color),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, crew.getUnlockedUpgrades().contains("chat") ? "True" : "False", crew.getUnlockedUpgrades().contains("chat") ? NamedTextColor.GREEN : NamedTextColor.RED)));

        inv.setItem(16, createGuiItem(Material.LAPIS_BLOCK,
            ComponentUtilities.createComponent("Discord (Voice & Text) Channels", NamedTextColor.BLUE),
            ComponentUtilities.createComponent("Two private crew channels on the discord server.", NamedTextColor.GRAY),
            ComponentUtilities.createComponent("Useful to have a place of gathering outside the server.", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_DISCORD_COST, UnicodeCharacters.sponge_color),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, crew.getUnlockedUpgrades().contains("discord") ? "True" : "False", crew.getUnlockedUpgrades().contains("discord") ? NamedTextColor.GREEN : NamedTextColor.RED)));

        inv.setItem(12, createGuiItem(Material.FILLED_MAP,
            ComponentUtilities.createComponent("Crew Mail", NamedTextColor.YELLOW),
            ComponentUtilities.createComponent("Send messages to offline crew members.", NamedTextColor.GRAY),
            ComponentUtilities.createComponent("Good for offline communication.", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_MAIL_COST, UnicodeCharacters.sponge_color),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, crew.getUnlockedUpgrades().contains("mail") ? "True" : "False", crew.getUnlockedUpgrades().contains("mail") ? NamedTextColor.GREEN : NamedTextColor.RED)));

        GUICreator.openInventory(p, data.getInventories().get(p.getUniqueId()));
    }
    public static void createSkirmishQueueGUI(PlayerData data, Player p, int queueSize) {
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Crew Skirmishes"));
        data.getInventories().put(p.getUniqueId(), inv);

        NamedTextColor queueSizeColor = queueSize > 0 ? NamedTextColor.DARK_GREEN : NamedTextColor.DARK_GRAY;
        inv.setItem(4, createGuiItem(Material.DIAMOND_SWORD,
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.skirmish_emoji + "Skirmishes", UnicodeCharacters.skirmish_color, " - Queue", NamedTextColor.GRAY),
            Component.text(""),
            Component.text("There are currently ").color(NamedTextColor.GRAY)
                .append(Component.text(queueSize).color(queueSizeColor)
                .append(Component.text(" crews in queue!").color(NamedTextColor.GRAY))).decoration(TextDecoration.ITALIC, false),
            Component.text(""),
            ComponentUtilities.createComponent("Click to join the queue!", UnicodeCharacters.economy_color)));

        GUICreator.openInventory(p, data.getInventories().get(p.getUniqueId()));
    }
    public static void createSkirmishSelectPlayersGUI(PlayerData data, Player p, Crew pCrew) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Skirmish Setup"));
        data.getInventories().put(p.getUniqueId(), inv);

        ArrayList<String> onlineMembers = pCrew.getOnlinePlayerUUIDs();
        int[] slots = new int[]{0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21};

        for (int i = 0; i < onlineMembers.size() && i < slots.length; i++) {
            String memberUUIDString = onlineMembers.get(i);
            Player player = Bukkit.getPlayer(UUID.fromString(memberUUIDString));
            if (player != null) {
                UUID pUUID = player.getUniqueId();
                Component displayName = Component.text(player.getName());
                Component[] lore = new Component[]{Component.text("Click to select this member for the queue.").color(NamedTextColor.GRAY)};
                getPlayerHead(slots[i], pUUID, displayName, inv, lore);
            }
        }
        inv.setItem(8, createGuiItem(Material.BARRIER,
            ComponentUtilities.createComponentWithDecoration("EXIT", NamedTextColor.RED, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to exit.", NamedTextColor.GRAY)));

        inv.setItem(15, createGuiItem(Material.DIAMOND_SWORD,
            ComponentUtilities.createComponentWithDecoration("JOIN QUEUE", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to join the queue.", NamedTextColor.GRAY)));

        GUICreator.openInventory(p, data.getInventories().get(p.getUniqueId()));
    }
//    public static void createSkirmishMatchBalancingGUI(PlayerData data, Player p, SkirmishTeam team1, SkirmishTeam team2) {
//        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Skirmish Match Balancing"));
//        data.getInventories().put(p.getUniqueId(), inv);
//
//        List<String> teamOnePlayerUUIDs = team1.getPlayers();
//        for (int i = 0; i < teamOnePlayerUUIDs.size() && i < 9; i++) {
//            UUID uuid = UUID.fromString(teamOnePlayerUUIDs.get(i));
//            getPlayerHead(i, uuid, Component.text("Player " + (i + 1)), inv);
//        }
//
//        List<String> teamTwoPlayerUUIDs = team2.getPlayers();
//        for (int i = 0; i < teamTwoPlayerUUIDs.size() && i < 9; i++) {
//            UUID uuid = UUID.fromString(teamTwoPlayerUUIDs.get(i));
//            getPlayerHead(18 + i, uuid, Component.text("Player " + (i + 1)), inv);
//        }
//
//        GUICreator.openInventory(p, data.getInventories().get(p.getUniqueId()));
//    }
    public static void createBannerSelectGUI(PlayerData data, Player p, Crew crew) {
        Inventory bannerInv = Bukkit.createInventory(null, 9, Component.text("Change Crew Banner"));
        ItemStack banner = Optional.ofNullable(crew.getBanner()).orElse(new ItemStack(Material.WHITE_BANNER));
        data.getInventories().put(p.getUniqueId(), bannerInv);

        bannerInv.setItem(0, createGuiItem(Material.LILY_PAD,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));
        for (int i = 3; i < 6; i++) {
            bannerInv.setItem(i, createGuiItem(banner,
                ComponentUtilities.createComponent("Click on a banner in your", NamedTextColor.GRAY),
                ComponentUtilities.createComponent("inventory to make a change!", NamedTextColor.GRAY)));
        }
        bannerInv.setItem(8, createGuiItem(Material.BARRIER,
            ComponentUtilities.createComponentWithDecoration("EXIT", NamedTextColor.RED, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to exit.", NamedTextColor.GRAY)));

        GUICreator.openInventory(p, bannerInv);
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
    protected static ItemStack createGuiItem(final ItemStack itemStack, final TextComponent name, final TextComponent... lore) {
        final ItemStack item = itemStack.clone();
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(name);
            meta.lore(Arrays.asList(lore));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }
    public static void getPlayerHead(int slot, UUID playerUUID, Component displayName, Inventory inventory, final Component... lore) {
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
