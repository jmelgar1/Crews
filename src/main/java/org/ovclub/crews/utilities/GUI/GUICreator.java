package org.ovclub.crews.utilities.GUI;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.hightable.VoteItem;
import org.ovclub.crews.utilities.ComponentUtilities;
import org.ovclub.crews.utilities.HightableUtility;
import org.ovclub.crews.utilities.UnicodeCharacters;
import org.ovclub.crews.utilities.skull.CustomHead;
import org.ovclub.crews.utilities.skull.SkullCreator;

import java.util.*;

public class GUICreator {
    public static void createCrewInfoGUI(PlayerData data, Player p, Crew crew) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text(crew.getName() + " - Profile"));
        data.getInventories().put(p.getUniqueId(), inv);

        String crewName = crew.getName();
        String dateCreated = crew.getDateFounded();
        int level = crew.getLevel();
        int vault = crew.getVault();
        int vaultDeposit = crew.getVaultDeposit(p);
        int requiredSponges = crew.getLevelUpCost();
        int influence = crew.getInfluence();
        int rating = crew.getRating();
        int skirmishWins = crew.getSkirmishWins();
        int skirmishLosses = crew.getSkirmishLosses();
        int skirmishDraws = crew.getSkirmishDraws();
        List<String> upgrades = crew.getUnlockedUpgrades();
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
        TextComponent vaultDepositComponent = Component.text().append(Component.text(" | ").color(NamedTextColor.GRAY)
            .append(Component.text("(").color(NamedTextColor.GRAY)
                .append(Component.text(vaultDeposit).color(NamedTextColor.DARK_GRAY)
                    .append(Component.text(")").color(NamedTextColor.GRAY)))))
            .build();

        String xpText = requiredSponges != -1 ? UnicodeCharacters.sponge_icon + requiredSponges : "MAX LEVEL";
        TextColor xpColor = requiredSponges != -1 ? requiredColor : UnicodeCharacters.sponge_color;
        TextComponent xpComponent = ComponentUtilities.createEmojiComponent(UnicodeCharacters.xp_emoji, "Cost to upgrade: ", UnicodeCharacters.info_text_color, xpText, xpColor);

        inv.setItem(4, createGuiItem(levelItems[level-1], crewNameComponent,
            foundedComponent,
            levelComponent,
            vaultComponent.append(vaultDepositComponent),
            xpComponent,
            Component.text(""),
            Component.text("Server Ranking: ").append(Component.text("#" + crew.getRank()).color(NamedTextColor.GOLD)),
            ComponentUtilities.createInfluenceComponent(influence),
            Component.text(""),
            ComponentUtilities.createComponentWithDecoration("Crew influence is the sum of the crew's", NamedTextColor.GRAY, TextDecoration.ITALIC),
            ComponentUtilities.createComponentWithDecoration("rating and vault balance.", NamedTextColor.GRAY, TextDecoration.ITALIC)));

        inv.setItem(20, createGuiItem(banner,
            ComponentUtilities.createComponentWithDecoration("CREW BANNER", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to change the crew's banner.", NamedTextColor.GRAY)));

        inv.setItem(22, createGuiItem(Material.DIAMOND_HELMET,
            ComponentUtilities.createComponentWithDecoration("CREW LEADERSHIP", NamedTextColor.GOLD, TextDecoration.BOLD),
            ComponentUtilities.createEmojiComponent(UnicodeCharacters.boss_emoji, "Boss: ", UnicodeCharacters.info_text_color, boss, UnicodeCharacters.boss_color),
            ComponentUtilities.createEmojiComponent(UnicodeCharacters.enforcers_emoji, "Enforcers: ", UnicodeCharacters.info_text_color, "", UnicodeCharacters.enforcers_color)));

		inv.setItem(24, createGuiItem(Material.GOLDEN_SWORD,
            ComponentUtilities.createComponentWithDecoration("CREW SKIRMISHES", NamedTextColor.GRAY, TextDecoration.BOLD),
            ComponentUtilities.createEmojiComponent(UnicodeCharacters.rating_emoji, "Rating: ", UnicodeCharacters.info_text_color, String.valueOf(rating), UnicodeCharacters.rating_color),
            ComponentUtilities.createWinLossRatio(UnicodeCharacters.skirmish_emoji, UnicodeCharacters.emoji_text_color, "W/L Record: ", UnicodeCharacters.info_text_color, String.valueOf(skirmishWins), String.valueOf(skirmishDraws), String.valueOf(skirmishLosses))));

//        boolean hasMail = upgrades.contains("mail");
//        boolean hasChat = upgrades.contains("chat");
//
//        TextComponent crewMail = createStatusComponent(hasMail);
//        TextComponent crewChat = createStatusComponent(hasChat);

//        inv.setItem(25, createGuiItem(Material.SPONGE,
//            Component.text(UnicodeCharacters.shop_emoji).color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD)
//                .append(Component.text(" CREW UPGRADE SHOP ").color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD)
//                    .append(Component.text(UnicodeCharacters.shop_emoji).color(UnicodeCharacters.sponge_color).decorate(TextDecoration.BOLD))),
//            Component.text(""),
//            ComponentUtilities.createComponent("Upgrades", NamedTextColor.WHITE),
//            ComponentUtilities.createDoubleEmojiComponent(UnicodeCharacters.crew_chat_emoji, NamedTextColor.DARK_GREEN, "Private Chat ", NamedTextColor.DARK_GREEN, crewChat),
//            ComponentUtilities.createDoubleEmojiComponent(UnicodeCharacters.mail_emoji, NamedTextColor.AQUA, "Crew Mail ", NamedTextColor.AQUA, crewMail)));

        inv.setItem(8, createGuiItem(Material.BARRIER,
            ComponentUtilities.createComponentWithDecoration("EXIT", NamedTextColor.RED, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to exit", NamedTextColor.GRAY)));

        List<String> crewMembers = crew.getMembers();
        String[] memberArray = crewMembers.toArray(new String[0]);

        if(crew.getEnforcers() != null) {
            for (String enforcer : crew.getEnforcers()) {
                String pName = Bukkit.getServer().getOfflinePlayer(UUID.fromString(enforcer)).getName();
                ComponentUtilities.createComponent("- " + pName, UnicodeCharacters.enforcers_color);
            }
        }

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

//        inv.setItem(16, createGuiItem(Material.LAPIS_BLOCK,
//            ComponentUtilities.createComponent("Discord (Voice & Text) Channels", NamedTextColor.BLUE),
//            ComponentUtilities.createComponent("Two private crew channels on the discord server.", NamedTextColor.GRAY),
//            ComponentUtilities.createComponent("Useful to have a place of gathering outside the server.", NamedTextColor.GRAY),
//            Component.text(""),
//            ComponentUtilities.createComponentWithPlaceHolder("Cost: ", NamedTextColor.GRAY, UnicodeCharacters.sponge_icon + ConfigManager.UPGRADE_DISCORD_COST, UnicodeCharacters.sponge_color),
//            Component.text(""),
//            ComponentUtilities.createComponentWithPlaceHolder("Purchased: ", NamedTextColor.GRAY, crew.getUnlockedUpgrades().contains("discord") ? "True" : "False", crew.getUnlockedUpgrades().contains("discord") ? NamedTextColor.GREEN : NamedTextColor.RED)));

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

    public static void createHighTableVoteGUI(PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Crew High Table Vote"));

        inv.setItem(0, createGuiItem(Material.NETHERITE_PICKAXE,
            ComponentUtilities.createComponent("⛏ Ore Drop Rates", UnicodeCharacters.oreDrops_color),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(2, createGuiItem(Material.DIAMOND_SWORD,
            ComponentUtilities.createComponent("🗡 Mob Drop Rates", UnicodeCharacters.mobDrops_color),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(4, createGuiItem(Material.NETHERITE_CHESTPLATE,
            ComponentUtilities.createComponent("☠ Mob Difficulty", UnicodeCharacters.mobDifficulty_color),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(6, createGuiItem(Material.EXPERIENCE_BOTTLE,
            ComponentUtilities.createComponent("🔮 XP Drop Rates", UnicodeCharacters.xpDrops_color),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(8, createGuiItem(Material.ANVIL,
            ComponentUtilities.createComponent("🧰 Discounts", UnicodeCharacters.discounts_color),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }

    public static void createHighTableMobDropSelectionGUI(PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Mob Drop Rates"));
        inv.setItem(2, createGuiItem(SkullCreator.getCustomHead(CustomHead.COW),
            ComponentUtilities.createComponent("☆ Passive Mobs ☆", NamedTextColor.DARK_GREEN),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(4, createGuiItem(SkullCreator.getCustomHead(CustomHead.IRON_GOLEM),
            ComponentUtilities.createComponent("☆ Neutral Mobs ☆", NamedTextColor.YELLOW),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(6, createGuiItem(SkullCreator.getCustomHead(CustomHead.CREEPER),
            ComponentUtilities.createComponent("☆ Hostile Mobs ☆", NamedTextColor.DARK_RED),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(8, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createHighTableXPDropSelectionGUI(Crews plugin, Map<String, Double> difficultyMultipliers, Player p) {
        PlayerData data = plugin.getData();
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("XP Drop Rates"));

        inv.setItem(1, createGuiItem(Material.GOLDEN_SWORD,
            ComponentUtilities.createComponent("☆ XP from Mobs ☆", NamedTextColor.DARK_GREEN),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(3, createGuiItem(Material.GOLDEN_PICKAXE,
            ComponentUtilities.createComponent("☆ XP from Ores & Blocks ☆", NamedTextColor.AQUA),
            Component.text(""),
            ComponentUtilities.createComponent("Click to view options.", NamedTextColor.DARK_GRAY)));

        inv.setItem(5, createGuiItem(Material.EMERALD,
            ComponentUtilities.createComponent("☆ XP from Trading ☆", NamedTextColor.GREEN),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", difficultyMultipliers.get(Material.EMERALD.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "EMERALD")));

        inv.setItem(7, createGuiItem(Material.FISHING_ROD,
            ComponentUtilities.createComponent("☆ XP from Fishing ☆", NamedTextColor.BLUE),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", difficultyMultipliers.get(Material.FISHING_ROD.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "FISHING_ROD")));

        inv.setItem(11, createGuiItem(Material.WHEAT,
            ComponentUtilities.createComponent("☆ XP from Breeding ☆", NamedTextColor.RED),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", difficultyMultipliers.get(Material.WHEAT.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "WHEAT")));

        inv.setItem(13, createGuiItem(Material.FURNACE,
            ComponentUtilities.createComponent("☆ XP from Smelting & Cooking ☆", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", difficultyMultipliers.get(Material.FURNACE.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "FURNACE")));

        inv.setItem(15, createGuiItem(Material.GRINDSTONE,
            ComponentUtilities.createComponent("☆ XP from Grindstones ☆", NamedTextColor.YELLOW),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", difficultyMultipliers.get(Material.GRINDSTONE.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "GRINDSTONE")));


        inv.setItem(26, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createHighTableMobXPDropSelectionGUI(Crews plugin, Map<String, Double> multipliers, Player p) {
        PlayerData data = plugin.getData();
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Mob XP Drop Rates"));

        inv.setItem(2, createGuiItem(SkullCreator.getCustomHead(CustomHead.COW),
            ComponentUtilities.createComponent("☆ Passive Mobs ☆", NamedTextColor.DARK_GREEN),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", multipliers.get("PASSIVE")) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "PASSIVE")));


        inv.setItem(4, createGuiItem(SkullCreator.getCustomHead(CustomHead.IRON_GOLEM),
            ComponentUtilities.createComponent("☆ Neutral Mobs ☆", NamedTextColor.YELLOW),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", multipliers.get("NEUTRAL")) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "NEUTRAL")));


        inv.setItem(6, createGuiItem(SkullCreator.getCustomHead(CustomHead.CREEPER),
            ComponentUtilities.createComponent("☆ Hostile Mobs ☆", NamedTextColor.DARK_RED),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", multipliers.get("HOSTILE")) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "xpDrops", "HOSTILE")));

        inv.setItem(8, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createBlockXPDropVoteGUI(Crews plugin, Map<String, Double> multipliers, Player p) {
        PlayerData data = plugin.getData();
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Ores & Blocks XP Drop Rates"));

        int slotIndex = 0;
        for(Map.Entry<String, Double> entry : multipliers.entrySet()) {
            String name;
            if(entry.getKey().equals("DIAMOND_ORE")) {
                name = "Overworld Ores";
            } else if(entry.getKey().equals("ANCIENT_DEBRIS")) {
                name = "Nether Ores";
            } else {
                name = entry.getKey();
            }
            inv.setItem(slotIndex, createGuiItem(Material.valueOf(entry.getKey()),
                ComponentUtilities.createComponentWithPlaceHolder("• Block Type: ", NamedTextColor.GRAY, name, NamedTextColor.YELLOW),
                Component.text(""),
                ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "XP Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", entry.getValue()) + "x", NamedTextColor.AQUA),
                Component.text(""),
                HightableUtility.isSelected(p, "xpDrops", entry.getKey())));

            slotIndex += 2;
        }

        inv.setItem(26, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createHighTableDiscountsDropSelectionGUI(Crews plugin, Map<String, Double> discounts, Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Discounts"));
        plugin.getData().replaceInventory(p.getUniqueId(), inv);

        inv.setItem(2, createGuiItem(Material.ENCHANTING_TABLE,
            ComponentUtilities.createComponent("☆ Enchant Discount ☆", NamedTextColor.AQUA),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Discount: ", NamedTextColor.WHITE, String.format("%.2f", discounts.get(Material.ENCHANTING_TABLE.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "discounts", "ENCHANTING_TABLE")));

        inv.setItem(4, createGuiItem(Material.ANVIL,
            ComponentUtilities.createComponent("☆ Repair Discount ☆", NamedTextColor.GRAY),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Discount: ", NamedTextColor.WHITE, String.format("%.2f", discounts.get(Material.ANVIL.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "discounts", "ANVIL")));

        inv.setItem(6, createGuiItem(Material.EMERALD,
            ComponentUtilities.createComponent("☆ Trading Discount ☆", NamedTextColor.GREEN),
            Component.text(""),
            ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Discount: ", NamedTextColor.WHITE, String.format("%.2f", discounts.get(Material.EMERALD.name())) + "x", NamedTextColor.AQUA),
            Component.text(""),
            HightableUtility.isSelected(p, "discounts", "EMERALD")));

        inv.setItem(8, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        GUICreator.openInventory(p, inv);
    }
    public static void createPassiveMobDropVoteGUI(Map<String, Double> passiveMultipliers, PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Passive Mob Drop Rates"));

        int slotIndex = 0;
        for (CustomHead head : CustomHead.values()) {
            if(head.getCategory().equals(CustomHead.Category.PASSIVE)) {
                inv.setItem(slotIndex, createGuiItem(SkullCreator.getCustomHead(head),
                    ComponentUtilities.createComponentWithPlaceHolder("• Mob Type: ", NamedTextColor.GRAY, head.name(), NamedTextColor.YELLOW),
                    Component.text(""),
                    ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", passiveMultipliers.get(head.name())) + "x", NamedTextColor.AQUA),
                    Component.text(""),
                    HightableUtility.isSelected(p, "mobDrops", head.name())));

                slotIndex += 2;
            }
        }

        inv.setItem(53, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createNeutralMobDropVoteGUI(Map<String, Double> neutralMultipliers, PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Neutral Mob Drop Rates"));

        int slotIndex = 0;
        for (CustomHead head : CustomHead.values()) {
            if(head.getCategory().equals(CustomHead.Category.NEUTRAL)) {
                inv.setItem(slotIndex, createGuiItem(SkullCreator.getCustomHead(head),
                    ComponentUtilities.createComponentWithPlaceHolder("• Mob Type: ", NamedTextColor.GRAY, head.name(), NamedTextColor.YELLOW),
                    Component.text(""),
                    ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", neutralMultipliers.get(head.name())) + "x", NamedTextColor.AQUA),
                    Component.text(""),
                    HightableUtility.isSelected(p, "mobDrops", head.name())));

                slotIndex += 2;
            }
        }

        inv.setItem(53, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createHostileMobDropVoteGUI(Map<String, Double> hostileMultipliers, PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Hostile Mob Drop Rates"));

        int slotIndex = 0;
        for (CustomHead head : CustomHead.values()) {

            if(head.getCategory().equals(CustomHead.Category.HOSTILE)) {
                inv.setItem(slotIndex, createGuiItem(SkullCreator.getCustomHead(head),
                    ComponentUtilities.createComponentWithPlaceHolder("• Mob Type: ", NamedTextColor.GRAY, head.name(), NamedTextColor.YELLOW),
                    Component.text(""),
                    ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", hostileMultipliers.get(head.name())) + "x", NamedTextColor.AQUA),
                    Component.text(""),
                    HightableUtility.isSelected(p, "mobDrops", head.name())));

                slotIndex += 2;
            }
        }

        inv.setItem(53, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createMobDifficultyDropVoteGUI(Map<String, Double> difficultyMultipliers, PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("Mob Difficulty Rates"));
        int slotIndex = 0;
        for (CustomHead head : CustomHead.values()) {
            if(head.getCategory().equals(CustomHead.Category.HOSTILE)) {
                inv.setItem(slotIndex, createGuiItem(SkullCreator.getCustomHead(head),
                    ComponentUtilities.createComponentWithPlaceHolder("• Mob Type: ", NamedTextColor.GRAY, head.name(), NamedTextColor.YELLOW),
                    Component.text(""),
                    ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Difficulty Amplifier: ", NamedTextColor.WHITE, String.format("%.2f", difficultyMultipliers.get(head.name())) + "x", NamedTextColor.AQUA),
                    Component.text(""),
                    HightableUtility.isSelected(p, "mobDifficulty", head.name())));

                slotIndex += 2;
            }
        }

        inv.setItem(53, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createOreDropVoteGUI(Map<String, Double> oreDropMultipliers, PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Ore Drop Rates"));
        int slotIndex = 0;
            for(Map.Entry<String, Double> entry : oreDropMultipliers.entrySet()) {
                inv.setItem(slotIndex, createGuiItem(Material.valueOf(entry.getKey()),
                    ComponentUtilities.createComponentWithPlaceHolder("• Ore Type: ", NamedTextColor.GRAY, entry.getKey(), NamedTextColor.YELLOW),
                    Component.text(""),
                    ComponentUtilities.createComponentWithPlaceHolder(UnicodeCharacters.multiplier + "Drop Multiplier: ", NamedTextColor.WHITE, String.format("%.2f", entry.getValue()) + "x", NamedTextColor.AQUA),
                    Component.text(""),
                    HightableUtility.isSelected(p, "oreDrops", entry.getKey())));

                slotIndex += 2;
            }

        inv.setItem(26, createGuiItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            ComponentUtilities.createComponentWithDecoration("GO BACK", NamedTextColor.GREEN, TextDecoration.BOLD),
            ComponentUtilities.createComponent("Click to go back.", NamedTextColor.GRAY)));

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    public static void createActiveMultipliers(ArrayList<VoteItem> activeMultipliers, PlayerData data, Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Active Multipliers"));

        int slotIndex = 0;
        for(VoteItem item : activeMultipliers) {
            ItemStack material = null;
            if(HightableUtility.isValidMaterial(item.getItem())) {
                material = new ItemStack(Material.valueOf(item.getItem()));
            } else if (Arrays.stream(HightableUtility.xpDropMobTypes).toList().contains(item.getItem())) {
                switch (item.getItem()) {
                    case "PASSIVE" -> material = SkullCreator.getCustomHead(CustomHead.COW);
                    case "NEUTRAL" -> material = SkullCreator.getCustomHead(CustomHead.IRON_GOLEM);
                    case "HOSTILE" -> material = SkullCreator.getCustomHead(CustomHead.CREEPER);
                }
            } else {
                material = SkullCreator.getCustomHead(CustomHead.valueOf(item.getItem()));
            }

            if(material == null) {
                material = new ItemStack(Material.DIRT);
            }

            List<TextComponent> voteStrings = HightableUtility.generateMultiplierString(item);

            inv.setItem(slotIndex, createGuiItem(material,
                ComponentUtilities.toTitleCaseComponent(item.getSection()),
                Component.text(""),
                ComponentUtilities.createComponent("• Multiplier Info: ", NamedTextColor.GRAY).decorate(TextDecoration.UNDERLINED),
                voteStrings.get(0),
                voteStrings.get(1)));

            slotIndex += 2;
        }

        while (slotIndex < 9) {
            ItemStack placeholder = new ItemStack(Material.GRAY_DYE);
            inv.setItem(slotIndex, createGuiItem(placeholder,
                Component.text("Vote not placed."),
                Component.text("No active multiplier in this slot.")));
            slotIndex += 2;
        }

        data.replaceInventory(p.getUniqueId(), inv);
        GUICreator.openInventory(p, inv);
    }
    protected static ItemStack createGuiItem(final Material material, final TextComponent name, final TextComponent... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        meta.lore(Arrays.asList(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
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
            meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
            item.setItemMeta(meta);
        }
        return item;
    }
    public static void getPlayerHead(int slot, UUID playerUUID, Component displayName, Inventory inventory, final Component... lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            PlayerProfile profile = Bukkit.createProfile(playerUUID);
            meta.setPlayerProfile(profile);

            meta.displayName(displayName);
            meta.lore(Arrays.asList(lore));
            skull.setItemMeta(meta);
        }

        inventory.setItem(slot, skull);
    }
    private static TextComponent createStatusComponent(boolean isEnabled) {
        if (isEnabled) {
            return Component.text(UnicodeCharacters.checkmark_emoji).color(NamedTextColor.GREEN);
        } else {
            return Component.text(UnicodeCharacters.x_emoji).color(NamedTextColor.RED);
        }
    }
    public static void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
}

