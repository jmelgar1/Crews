package org.ovclub.crews.listeners;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jline.utils.Log;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.hightable.MultiplierItem;
import org.ovclub.crews.object.skirmish.SkirmishTeam;
import org.ovclub.crews.utilities.GUICreator;
import org.ovclub.crews.utilities.HightableUtility;
import org.ovclub.crews.utilities.skull.CustomHead;

import java.util.*;

public class CrewGUIListener implements Listener {

    private final Crews plugin;
    public CrewGUIListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        PlayerData data = plugin.getData();
        final Player p = (Player) e.getWhoClicked();
        UUID playerUUID = p.getUniqueId();
        if(!e.getInventory().equals(data.getInventories().get(playerUUID))) return;
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        Crew pCrew = data.getCrew(p);

        if(clickedItem.getType() == Material.COMPASS) {
            p.closeInventory();
        }

        if(clickedItem.getType() == Material.BARRIER) {
            p.closeInventory();
        }

        //crews shop item
        if(clickedItem.getType() == Material.SPONGE) {
            p.closeInventory();
            p.performCommand("crews shop");
        }

        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Crew Skirmishes"))) {
            p.closeInventory();
            GUICreator.createSkirmishSelectPlayersGUI(data, p, pCrew);
        }

        if (clickedItem.getType() == Material.PLAYER_HEAD && e.getView().title().equals(Component.text("Skirmish Setup"))) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta != null && meta.hasDisplayName()) {
                String playerName = PlainTextComponentSerializer.plainText().serialize(meta.displayName());
                Player player = Bukkit.getPlayerExact(playerName);

                if (player != null && player.isOnline()) {
                    List<Component> currentLore = meta.lore();
                    boolean isQueued = false;
                    if (currentLore != null) {
                        for (Component line : currentLore) {
                            if (PlainTextComponentSerializer.plainText().serialize(line).equals("Player is queued.")) {
                                isQueued = true;
                                break;
                            }
                        }
                    }
                    List<Component> newLore = new ArrayList<>();
                    if (isQueued) {
                        newLore.add(Component.text("Click to select this member for the queue.").color(NamedTextColor.GRAY));
                        data.removeFromSelectedForQueue(pCrew, player.getUniqueId().toString());
                    } else {
                        newLore.add(Component.text("Player is queued.").color(NamedTextColor.BLUE));
                        data.addToSelectedForQueue(pCrew, player.getUniqueId().toString());
                    }
                    meta.lore(newLore);
                    clickedItem.setItemMeta(meta);
                }
            }
        }

        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Skirmish Setup"))) {
            if(plugin.getSkirmishManager().getQueue().isInQueue(pCrew)) {
                p.sendMessage(ConfigManager.ALREADY_IN_QUEUE);
                return;
            }
            if(data.getSelectedForQueue().size() >= 1) {
                p.closeInventory();
                SkirmishTeam queueItem = new SkirmishTeam();
                queueItem.setCrew(pCrew);
                queueItem.setPlayers(data.getSelectedForQueue().get(pCrew));
                data.getSelectedForQueue().remove(pCrew);
                plugin.getSkirmishManager().queueCrew(queueItem);
                Bukkit.broadcast(ConfigManager.CREW_HAS_JOINED_QUEUE);
                for(String stringUUID : queueItem.getPlayers()) {
                    UUID pUUID = UUID.fromString(stringUUID);
                    Player player = Bukkit.getPlayer(pUUID);
                    if(player != null) {
                        if (player.isOnline()) {
                            player.sendMessage(ConfigManager.JOINED_QUEUE);
                        } else {
                            //throw error here
                        }
                    }
                }
                } else {
                    p.sendMessage(ConfigManager.ONE_PLAYER_REQUIRED_FOR_QUEUE);
                }
            }

        if(clickedItem.getType().equals(Material.LILY_PAD)) {
            if(e.getView().title().equals(Component.text("Change Crew Banner"))) {
                p.closeInventory();
                GUICreator.createCrewInfoGUI(data, p, pCrew);
            }
        }

        if(clickedItem.getType().toString().endsWith("_BANNER")) {
            if(e.getView().title().equals(Component.text(pCrew.getName() + " - Profile"))) {
                if(!pCrew.isHigherup(p)) {
                    p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
                    return;
                }
                p.closeInventory();
                GUICreator.createBannerSelectGUI(data, p, pCrew);
            }
            else if(e.getView().title().equals(Component.text("Change Crew Banner"))) {
                if(p.getInventory().contains(clickedItem)) {
                    p.closeInventory();
                    ItemStack newBanner = clickedItem.clone();
                    newBanner.setAmount(1);
                    pCrew.setBanner(newBanner);
                    GUICreator.createBannerSelectGUI(data, p, pCrew);
                }
            }
        }

        /* High Table Listeners */
        if(clickedItem.getType() == Material.NETHERITE_PICKAXE && e.getView().title().equals(Component.text("Crew High Table Vote"))) {
            p.closeInventory();

            MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("oreDrops");
            GUICreator.createOreDropVoteGUI(multiplierItem.getValues(), data, p);
        }
        if(clickedItem.getType() == Material.DIAMOND_SWORD && e.getView().title().equals(Component.text("Crew High Table Vote"))) {
            p.closeInventory();
            GUICreator.createHighTableMobDropSelectionGUI(data, p);
        }
        if(clickedItem.getType() == Material.EXPERIENCE_BOTTLE && e.getView().title().equals(Component.text("Crew High Table Vote"))) {
            p.closeInventory();
            MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("xpDrops.activities");
            GUICreator.createHighTableXPDropSelectionGUI(plugin, multiplierItem.getValues(), p);
        }
        if(clickedItem.getType() == Material.ANVIL && e.getView().title().equals(Component.text("Crew High Table Vote"))) {
            p.closeInventory();
            MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("discounts");
            GUICreator.createHighTableDiscountsDropSelectionGUI(plugin, multiplierItem.getValues(), p);
        }
        if(clickedItem.getType() == Material.NETHERITE_CHESTPLATE && e.getView().title().equals(Component.text("Crew High Table Vote"))) {
            p.closeInventory();
            MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("mobDifficulty");
            GUICreator.createMobDifficultyDropVoteGUI(multiplierItem.getValues(), data, p);
        }
        if(clickedItem.getType() == Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
            if(e.getView().title().equals(Component.text("Passive Mob Drop Rates")) ||
                e.getView().title().equals(Component.text("Neutral Mob Drop Rates")) ||
                e.getView().title().equals(Component.text("Hostile Mob Drop Rates"))) {
                p.closeInventory();
                GUICreator.createHighTableMobDropSelectionGUI(plugin.getData(), p);
                return;
            }
            if(e.getView().title().equals(Component.text("Ore Drop Rates")) ||
                e.getView().title().equals(Component.text("Mob Drop Rates")) ||
                e.getView().title().equals(Component.text("Mob Difficulty Rates")) ||
                e.getView().title().equals(Component.text("XP Drop Rates")) ||
                e.getView().title().equals(Component.text("Discounts"))) {
                p.closeInventory();
                GUICreator.createHighTableVoteGUI(plugin.getData(), p);
                return;
            }
            if (e.getView().title().equals(Component.text("Mob XP Drop Rates")) ||
                e.getView().title().equals(Component.text("Ores & Blocks XP Drop Rates"))) {
                p.closeInventory();
                MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("xpDrops.activities");
                GUICreator.createHighTableXPDropSelectionGUI(plugin, multiplierItem.getValues(), p);
                return;
            }
        }
        if(isMultiplier(clickedItem)) {
            if(e.getView().title().equals(Component.text("Ore Drop Rates"))) {
                recordVote(p, pCrew, "oreDrop", clickedItem.getType().name());
                return;
            }
            if(e.getView().title().equals(Component.text("Passive Mob Drop Rates"))) {
                recordVote(p, pCrew, "mobDrop", getHeadName(clickedItem));
                return;
            }
            if(e.getView().title().equals(Component.text("Neutral Mob Drop Rates"))) {
                recordVote(p, pCrew, "mobDrop", getHeadName(clickedItem));
                return;
            }
            if(e.getView().title().equals(Component.text("Hostile Mob Drop Rates"))) {
                recordVote(p, pCrew, "mobDrop", getHeadName(clickedItem));
                return;
            }
            if(e.getView().title().equals(Component.text("Mob Difficulty Rates"))) {
                recordVote(p, pCrew, "mobDifficulty", getHeadName(clickedItem));
                return;
            }
            if(e.getView().title().equals(Component.text("XP Drop Rates"))) {
                if(clickedItem.getType() != Material.GOLDEN_PICKAXE && clickedItem.getType() != Material.GOLDEN_SWORD) {
                    recordVote(p, pCrew, "xpDrop", clickedItem.getType().name());
                    return;
                }
            }
            if(e.getView().title().equals(Component.text("Mob XP Drop Rates"))) {
                recordVote(p, pCrew, "xpDrop", getHeadName(clickedItem));
                return;
            }
            if(e.getView().title().equals(Component.text("Ores & Blocks XP Drop Rates"))) {
                recordVote(p, pCrew, "xpDrop", clickedItem.getType().name());
                return;
            }
            if(e.getView().title().equals(Component.text("Discounts"))) {
                recordVote(p, pCrew, "discounts", clickedItem.getType().name());
                return;
            }
        }
        if (e.getView().title().equals(Component.text("Mob Drop Rates"))) {
            if (clickedItem.getType() == Material.PLAYER_HEAD) {
                ItemMeta meta = clickedItem.getItemMeta();
                if (clickedItem.getType() != Material.PLAYER_HEAD || !(meta instanceof SkullMeta)) return;

                SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
                PlayerProfile profile = skullMeta.getPlayerProfile();
                if(profile != null) {
                UUID skullUUID = profile.getId();
                    for (CustomHead head : CustomHead.values()) {
                        if (head.getUuid().equals(skullUUID)) {
                            if(head.name().equals("COW")) {
                                p.closeInventory();
                                MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("mobDrops.passive");
                                GUICreator.createPassiveMobDropVoteGUI(multiplierItem.getValues(), data, p);
                                break;
                            }
                            if(head.name().equals("IRON_GOLEM")) {
                                p.closeInventory();
                                MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("mobDrops.neutral");
                                GUICreator.createNeutralMobDropVoteGUI(multiplierItem.getValues(), data, p);
                                break;
                            }
                            if(head.name().equals("CREEPER")) {
                                p.closeInventory();
                                MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("mobDrops.hostile");
                                GUICreator.createHostileMobDropVoteGUI(multiplierItem.getValues(), data, p);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (e.getView().title().equals(Component.text("XP Drop Rates"))) {
            if (clickedItem.getType() == Material.GOLDEN_SWORD) {
                p.closeInventory();
                MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("xpDrops.mobs");
                GUICreator.createHighTableMobXPDropSelectionGUI(plugin, multiplierItem.getValues(), p);
            }

            if (clickedItem.getType() == Material.GOLDEN_PICKAXE) {
                p.closeInventory();
                MultiplierItem multiplierItem = plugin.getData().getMultiplierBySection("xpDrops.blocks");
                GUICreator.createBlockXPDropVoteGUI(plugin, multiplierItem.getValues(), p);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent event) {
        PlayerData data = plugin.getData();
        if(event.getInventory().equals(data.getInventories().get(event.getWhoClicked().getUniqueId()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        plugin.getData().getInventories().remove(p.getUniqueId());
    }

    private String getHeadName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != Material.PLAYER_HEAD || !(meta instanceof SkullMeta)) return null;

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = skullMeta.getPlayerProfile();
        if(profile != null) {
            UUID skullUUID = profile.getId();
            for (CustomHead head : CustomHead.values()) {
                if (head.getUuid().equals(skullUUID)) {
                    return head.name();
                }
            }
        }
        return null;
    }

    private boolean isMultiplier(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            List<Component> lore = meta.lore();
            for (Component line : lore) {
                String plainText = PlainTextComponentSerializer.plainText().serialize(line);
                if (plainText.contains("Drop Multiplier") || plainText.contains("Discount") || plainText.contains("Difficulty Amplifier")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void recordVote(Player p, Crew crew, String category, String item) {
        FileConfiguration config = plugin.getConfig();
        String path = "high-table." + crew.getName() + ".votes." + p.getName() + "." + category + "." + item;
        int currentCount = config.getInt(path, 0);
        config.set(path, currentCount + 1);
        plugin.saveConfig();
    }
}
