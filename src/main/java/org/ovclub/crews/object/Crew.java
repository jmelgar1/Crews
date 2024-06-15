package org.ovclub.crews.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import org.antlr.v4.tool.Grammar;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.managers.WarpCancelListener;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.SoundUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Crew {
    private final Crews plugin;

    private String uuid;
    public String getUuid() {return uuid;}
    public void setUuid() {this.uuid = UUID.randomUUID().toString();}

    //name
    private String name;
    public String getName() {return this.name;}
    public void setString(String newName) {this.name = newName;}

    private Location compound;
    public Location getCompound(){return this.compound;}

    //date founded
    private final String dateFounded;
    public String getDateFounded() { return this.dateFounded; }

    //vault
    private int vault;
    public int getVault() { return this.vault; }

    //level
    private int level;
    public int getLevel() { return this.level; }

    private ItemStack banner;
    public ItemStack getBanner() { return this.banner; }
    public void setBanner(ItemStack newBanner) { this.banner = newBanner; }

    //influence (power score)
    private int influence;
    public int getInfluence() { return this.influence; }
    public void setInfluence(int influence) {
        this.influence = influence;
    }
    public void addInfluence(int amount) {
        this.influence += amount;
    }
    public void removeInfluence(int amount) {
        this.influence -= amount;
    }

    //score (add up to influence)
    private int rating;
    public int getRating() { return this.rating;}
    public void setRating(int value) {
        this.rating = value;
        calculateInfluence();
    }

    private int skirmishWins;
    public int getSkirmishWins() { return this.skirmishWins;}
    public void addSkirmishWins(int amount) { this.skirmishWins += amount;}

    private int skirmishDraws;
    public int getSkirmishDraws() { return this.skirmishDraws;}
    public void addSkirmishDraws(int amount) { this.skirmishDraws += amount;}

    private int skirmishLosses;
    public int getSkirmishLosses() { return this.skirmishLosses;}
    public void addSkirmishLosses(int amount) { this.skirmishLosses += amount;}

    public int getGamesPlayed() {
        return this.skirmishDraws + this.skirmishWins + this.skirmishLosses;
    }

    //description
    private String description;
    public void setDescription(String s) {
        this.description = s;
    }
    public String getDescription() {
        return this.description;
    }

    //crew upgrades
    private List<String> unlockedUpgrades = new ArrayList<>();
    public List<String> getUnlockedUpgrades() { return this.unlockedUpgrades; }
    public void addUpgrade(String upgrade) {
        this.unlockedUpgrades.add(upgrade);
    }

    //kills
    private int kills;
    public void addKill() {
        this.kills++;
    }
    public int getKills() {
        return this.kills;
    }

    //vault deposits
    private HashMap<String, Integer> vaultDeposits;
    public HashMap<String, Integer> getVaultDeposits() {return vaultDeposits;}

    //mail
    private ArrayList<String> sentMail = new ArrayList<>();
    public ArrayList<String> getSentMail() { return this.sentMail; }

    //crew elites
    private String boss;
    public String getBoss() {
        return this.boss;
    }
    private final ArrayList<String> enforcers = new ArrayList<>();
    public ArrayList<String> getEnforcers() {return this.enforcers;}

    //members
    private final ArrayList<String> members = new ArrayList<>();
    public ArrayList<String> getMembers() {
        return this.members;
    }

    //custom color
    private TextColor white_color = TextColor.color(0xE0E0E0);
    public TextColor getColor() {return this.white_color;}
    public void setColor(TextColor color) {
        this.white_color = color;
    }

    //sponge_icon stuff
    private int levelUpCost;
    public int getLevelUpCost() { return this.levelUpCost; }
    private int memberLimit;
    public int getMemberLimit() { return this.memberLimit; }
    private final int enforcerLimit;
    public int getEnforcerLimit() { return this.enforcerLimit; }

    //warp stuff
    private final List<Player> inWarp = new ArrayList<Player>();
    private WarpCancelListener listener;

    public boolean hasCompound() {
        return this.compound != null;
    }

    public void setCompound(Player p) {
        this.compound = p.getLocation();
        broadcast(ConfigManager.COMPOUND_SET);
    }
    public void removeCompound() {
        this.compound = null;
        broadcast(ConfigManager.COMPOUND_REMOVED);
    }
    public void tpCompound(Player p) {
        if (this.compound != null) {
            inWarp.add(p);
            p.sendMessage(ChatColor.DARK_GREEN + "[\uD83C\uDFC3] " + ChatColor.GREEN + "Warping in 5 seconds...");

            BukkitRunnable warpTask = new BukkitRunnable() {

                @Override
                public void run() {
                    if (!inWarp.contains(p)) {
                        return;
                    }

                    // Teleport player
                    p.teleport(compound, PlayerTeleportEvent.TeleportCause.COMMAND);

                    int amount = ConfigManager.WARP_COST;
                    plugin.getData().getCrew(p).removeFromVault(amount, p, false);

                    p.sendMessage(ChatUtilities.successIcon + ChatColor.GREEN + "Warp successful!");
                    inWarp.remove(p);

                    // Unregister the listener and remove the played from the inWarp list
                    HandlerList.unregisterAll(listener);
                    inWarp.remove(p);
                }
            };

            listener = new WarpCancelListener(p, warpTask, inWarp);
            Bukkit.getPluginManager().registerEvents(listener, plugin);

            // Schedule the warp task to run after 5 seconds
            warpTask.runTaskLater(plugin, 100);

        } else {
            p.sendMessage(ConfigManager.CREW_NO_COMPOUND);
        }
    }

    public void setVault(int amount) {
        this.vault+= amount;
        calculateInfluence();
    }

    public boolean hasMember(Player p) {
        String pUUID = String.valueOf(p.getUniqueId());
        return this.members.contains(pUUID) ||
            this.enforcers.contains(pUUID) ||
            this.boss.equals(pUUID);
    }

    //Broadcast to all members
    public void broadcast(final Component text) {
        if(text == null) return;
        for (String member : this.members) {
            UUID pUUID = UUID.fromString(member);
            if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                Player p = Bukkit.getPlayer(pUUID);
                if(p != null){p.sendMessage(text);}
            }
        }
        for (String enforcer : this.enforcers) {
            UUID pUUID = UUID.fromString(enforcer);
            if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                Player p = Bukkit.getPlayer(pUUID);
                if(p != null){p.sendMessage(text);}
            }
        }
        if (Bukkit.getOfflinePlayer(UUID.fromString(this.boss)).isOnline()) {
            Player p = Bukkit.getPlayer(UUID.fromString(this.boss));
            if (p != null && p.isOnline()) {
                p.sendMessage(text);
            }
        }
    }

    private void editVaultDeposits(int amount, Player p) {
        String pUUID = p.getUniqueId().toString();
        vaultDeposits.merge(pUUID, amount, Integer::sum);

        if(vaultDeposits.get(pUUID) == 0) {
            vaultDeposits.remove(pUUID);
        }
    }

    //fix this redundant code
    public void addToVault(int amount, Player p, boolean showTransactionMessage){
        if(showTransactionMessage) {
            Component broadcastMessage = ConfigManager.ADD_TO_VAULT
                .replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName()))
                .replaceText(builder -> builder.matchLiteral("{current_amount}").replacement(String.valueOf(vault)))
                .replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(amount)))
                .replaceText(builder -> builder.matchLiteral("{new_amount}").replacement(String.valueOf(vault + amount)));
            broadcast(broadcastMessage);
        }
        this.vault+=amount;

        editVaultDeposits(amount, p);

        calculateVaultAndInfluence();
    }
    public void removeFromVault(int amount, Player p, boolean showTransactionMessage){
        if(showTransactionMessage) {
            Component broadcastMessage = ConfigManager.REMOVE_FROM_VAULT
                .replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName()))
                .replaceText(builder -> builder.matchLiteral("{current_amount}").replacement(String.valueOf(vault)))
                .replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(amount)))
                .replaceText(builder -> builder.matchLiteral("{new_amount}").replacement(String.valueOf(vault - amount)));
            broadcast(broadcastMessage);
        }
        this.vault-=amount;

        editVaultDeposits(-amount, p);

        calculateVaultAndInfluence();
    }

    public void calculateInfluence() {
        int influence = this.rating + this.vault;
        setInfluence(influence);
    }

    public int getVaultDeposit(Player p) {
        String pUUID = String.valueOf(p.getUniqueId());
        return vaultDeposits.getOrDefault(pUUID, 0);
    }

    //Set new crew leader & add old one to members.
    public void replaceBoss(final UUID id) {
        this.members.add(this.boss);
        this.members.remove(id.toString());
        this.boss = id.toString();
        for (String member : this.members) {
            UUID pUUID = UUID.fromString(member);
            if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                Player p = Bukkit.getPlayer(pUUID);
                if(p != null){
                    p.sendMessage(ConfigManager.NEW_BOSS
                        .replaceText(builder -> builder.matchLiteral("{player}").replacement(getPlayerName(UUID.fromString(this.boss))))
                        .replaceText(builder -> builder.matchLiteral("{crew}").replacement(this.name)));
                }
            }
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(this.boss));
        if (offlinePlayer.isOnline()) {
            Player boss = Bukkit.getPlayer(UUID.fromString(this.boss));
            if (boss != null) {
                boss.sendMessage(ConfigManager.YOU_ARE_BOSS);
            }
        }
    }

    public ArrayList<String> getOnlinePlayerUUIDs() {
        ArrayList<String> onlineMembers = new ArrayList<>();
        for (String memberUUIDString : this.members) {
            UUID memberUUID = UUID.fromString(memberUUIDString);
            Player player = Bukkit.getPlayer(memberUUID);
            if (player != null && player.isOnline()) {
                onlineMembers.add(memberUUIDString);
            }
        }
        for (String enforcerUUIDString : this.enforcers) {
            UUID enforcerUUID = UUID.fromString(enforcerUUIDString);
            Player player = Bukkit.getPlayer(enforcerUUID);
            if (player != null && player.isOnline()) {
                onlineMembers.add(enforcerUUIDString);
            }
        }
        UUID bossUUID = UUID.fromString(this.boss);
        Player boss = Bukkit.getPlayer(bossUUID);
        if(boss != null && boss.isOnline()) {
            onlineMembers.add(this.boss);
        }
        return onlineMembers;
    }

    public void addEnforcer(final UUID id) {
        if(enforcers.size() < enforcerLimit) {
            enforcers.add(id.toString());
            members.remove(id.toString());
            Player p = Bukkit.getPlayer(id);
            if(p != null) {
                p.sendMessage(ConfigManager.ENFORCER_PROMOTE);
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(id);
            String playerName = offlinePlayer.getName();
            if (playerName != null) {
                broadcast(ConfigManager.NEW_ENFORCER.replaceText(builder -> builder.matchLiteral("{player}").replacement(playerName)));
            }
        }
    }

    public void removeEnforcer(final UUID id) {
        enforcers.remove(id.toString());
        members.add(id.toString());
        Player p = Bukkit.getPlayer(id);
        if(p != null) {
            p.sendMessage(ConfigManager.ENFORCER_DEMOTE);
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(id);
        String playerName = offlinePlayer.getName();
        if (playerName != null) {
            broadcast(ConfigManager.PLAYER_ENFORCER_DEMOTE.replaceText(builder -> builder.matchLiteral("{player}").replacement(playerName)));
        }
    }

    public void showInfo(final Player p, boolean inCrew) {
        UnicodeCharacters.sendMessageWithHeader(p, "┌──────[ ", this.name, " ]───────◓");
        TextComponent vaultDepositComponent = Component.text().append(Component.text(" | ").color(NamedTextColor.GRAY)
                .append(Component.text("(").color(NamedTextColor.GRAY)
                    .append(Component.text(this.getVaultDeposit(p)).color(NamedTextColor.DARK_GRAY)
                        .append(Component.text(")").color(NamedTextColor.GRAY)))))
            .build();
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.founded_emoji, "Founded: ", this.dateFounded, UnicodeCharacters.founded_color);
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.description_emoji, "Description: ", this.description, UnicodeCharacters.description_color);
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.level_emoji, "Level: ", String.valueOf(this.level), UnicodeCharacters.level_color);
        UnicodeCharacters.sendVaultMessage(p, UnicodeCharacters.vault_emoji, "Vault: ", UnicodeCharacters.sponge_icon + this.vault, UnicodeCharacters.sponge_color, vaultDepositComponent, inCrew);
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.rating_emoji, "Rating: ", String.valueOf(this.rating), UnicodeCharacters.rating_color);
        UnicodeCharacters.sendInfluenceMessage(p, UnicodeCharacters.influence_emoji, String.valueOf(this.influence));
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.boss_emoji, "Boss: ", getPlayerName(UUID.fromString(this.boss)), UnicodeCharacters.boss_color);

        StringBuilder enforcersList = new StringBuilder();
        for (String enforcer : this.enforcers) {
            UUID pUUID = UUID.fromString(enforcer);
            String playerName = getPlayerName(pUUID);
            if (playerName != null) {
                if (enforcersList.length() > 0) {
                    enforcersList.append(", ");
                }
                enforcersList.append(playerName);
            }
        }
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.enforcers_emoji, "Enforcers: ", enforcersList.toString(), UnicodeCharacters.enforcers_color);

        StringBuilder membersList = new StringBuilder();
        for (String member : this.members) {
            UUID pUUID = UUID.fromString(member);
            String playerName = getPlayerName(pUUID);
            if(playerName != null) {
                if(membersList.length() > 0){
                    membersList.append(", ");
                }
                membersList.append(playerName);
            }
        }
        UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.members_emoji, "Members: ", membersList.toString(), UnicodeCharacters.members_color);

        if(inCrew) {
            TextColor activeColor = TextColor.fromHexString("#00FF00");
            TextColor inactiveColor = TextColor.fromHexString("#FF0000");

            String statusText = (hasCompound()) ? "ACTIVE" : "INACTIVE";
            TextColor statusColor = (hasCompound()) ? activeColor : inactiveColor;

            UnicodeCharacters.sendInfoMessage(p, UnicodeCharacters.compound_emoji, "Compound: ", statusText, statusColor);
        }
        p.sendMessage(Component.text("└────────────────────◒").color(UnicodeCharacters.logo_color));

        //get latest vault amount
        calculateVaultAndInfluence();
    }

    /* CREW LEVEL */
    public boolean isMaxLevel() {
        return this.level == 10;
    }

    //Disband crew and delete all SPlayers
    public void disband() {
        plugin.getData().removeCPlayer(Bukkit.getPlayer(UUID.fromString(this.boss)));
        for (String members : this.getMembers()) {
            UUID pUUID = UUID.fromString(members);
            Player p = Bukkit.getPlayer(pUUID);
            plugin.getData().removeCPlayer(p);

            if(p != null) {
                removeDepositAndSendBackToPlayer(p);
            }
        }
        Bukkit.broadcast(ConfigManager.CREW_DISBAND
            .replaceText(builder -> builder.matchLiteral("{crew}").replacement(Component.text(this.name).decorate(TextDecoration.BOLD))));
        SoundUtilities.playSoundToAllPlayers(SoundUtilities.crewDisbandSound, 0.25F, 0.5F);
        plugin.getData().removeCrew(this);
    }

    private String getPlayerName(UUID playerId) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
        return offlinePlayer.getName();
    }

    //Remove player from crew, whether they left or were kicked. Returns true if player was in the crew, false if player was not.
    public void removePlayer(final UUID pUUID, boolean wasKicked) {
        this.enforcers.remove(pUUID.toString());
        if (this.members.remove(pUUID.toString())) {
            plugin.getData().removeCPlayer(Bukkit.getPlayer(pUUID));
            Player p = Bukkit.getPlayer(pUUID);
            if(p != null && p.isOnline()) {
                if (wasKicked) {
                    removeDepositAndSendBackToPlayer(p);
                    this.broadcast(ConfigManager.KICKED_FROM_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
                } else {
                    removeDepositAndSendBackToPlayer(p);
                    p.sendMessage(ConfigManager.YOU_LEFT_CREW);
                    this.broadcast(ConfigManager.LEAVE_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
                }
            }
        }
        calculateVaultAndInfluence();
    }

    private void removeDepositAndSendBackToPlayer(Player p) {
        int deposit = getVaultDeposit(p);
        plugin.getEconomy().depositPlayer(p, deposit);
        vaultDeposits.remove(String.valueOf(p.getUniqueId()));
        vault -= deposit;
    }

    //Add player to crew, player must be online to join we use Player
    public void addPlayer(final Player p) {
        this.members.add(p.getUniqueId().toString());
        plugin.getData().addCPlayer(p, this);
        this.broadcast(ConfigManager.JOIN_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));;
        calculateVaultAndInfluence();
    }

    public boolean isBoss(Player p) {
        String pUUID = p.getUniqueId().toString();
        return pUUID.equals(this.boss);
    }

    public boolean isEnforcer(Player p) {
        String pUUID = p.getUniqueId().toString();
        return this.enforcers.contains(pUUID);
    }

    public void calculateVaultAndInfluence() {
        for(String uuid : members) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            double pVault = plugin.getEconomy().getBalance(p);
            this.vault += (int) pVault;
        }
        calculateInfluence();
    }

    public boolean isHigherup(Player p) {
        return isBoss(p) || isEnforcer(p);
    }

    /* Mail */
    public void addToMail(String message) {
        this.sentMail.add(message);
    }

    /* Naming */
    public void changeName(String newName) {
        this.name = newName;
    }

    public void upgradeCrew() {
        if(this.vault >= 500 && this.level == 9) {
            this.level = 10;
            this.memberLimit = 12;
            this.levelUpCost = -1;
        } else if(this.vault >= 300 && this.level == 8) {
            this.level = 9;
            memberLimit = 11;
            this.levelUpCost = 500;
        } else if(this.vault >= 200 && this.level == 7) {
            this.level = 8;
            memberLimit = 10;
            this.levelUpCost = 300;
        } else if(this.vault >= 150 && this.level == 6) {
            this.level = 7;
            memberLimit = 9;
            this.levelUpCost = 200;
        } else if(this.vault >= 125 && this.level == 5) {
            this.level = 6;
            memberLimit = 8;
            this.levelUpCost = 150;
        } else if(this.vault >= 100 && this.level == 4) {
            this.level = 5;
            memberLimit = 7;
            this.levelUpCost = 125;
        } else if(this.vault >= 75 && this.level == 3) {
            this.level = 4;
            memberLimit = 6;
            this.levelUpCost = 100;
        } else if(this.vault >= 50 && this.level == 2) {
            this.level = 3;
            memberLimit = 5;
            this.levelUpCost = 75;
        } else if(this.vault >= 25 && this.level == 1) {
            this.level = 2;
            memberLimit = 4;
            this.levelUpCost = 50;
        }
        Bukkit.broadcast(ConfigManager.CREW_UPGRADE
            .replaceText(builder -> builder.matchLiteral("{crew}").replacement(Component.text(this.name).decorate(TextDecoration.BOLD)))
            .replaceText(builder -> builder.matchLiteral("{level}").replacement(Component.text(this.level).decorate(TextDecoration.BOLD))));
        SoundUtilities.playSoundToAllPlayers(SoundUtilities.crewUpgradeSound, 0.20F, 0.7F);
    }

    public boolean isInHighTable() {
        ArrayList<String> hightableCrews = plugin.getData().getHightableCrews();
        return hightableCrews.contains(this.uuid);
    }

    public static Crew deserialize(String uuid, Map<String, Object> map, Crews data) {
        return new Crew(uuid, map, data);
    }

    //Hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crew crew = (Crew) o;
        return name.equals(crew.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    //Constructor for new crew
    public Crew(final String name, final Player p, final Crews data) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        this.uuid = UUID.randomUUID().toString();
        this.plugin = data;
        this.plugin.getData().addCPlayer(p, this);
        this.name = name;
        this.boss = p.getUniqueId().toString();
        this.dateFounded = currentDate.format(formatter);
        this.description = "No description set.";
        this.kills = 0;
        this.vault = (int) plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(String.valueOf(p.getUniqueId())));
        this.level = 1;
        this.skirmishWins = 0;
        this.skirmishLosses = 0;
        this.skirmishDraws = 0;
        this.influence = ConfigManager.DEFAULT_RATING;
        this.levelUpCost = 25;
        this.rating = ConfigManager.DEFAULT_RATING;
        this.enforcerLimit = 1;
        this.memberLimit = 3;
        this.unlockedUpgrades = new ArrayList<>();
        this.vaultDeposits = new HashMap<>();
        //this.sentMail = new ArrayList<>();
    }

    //Constructor for loaded crew
    public Crew(String uuid, Map<String, Object> map, final Crews data) {
        this.uuid = uuid;
        this.plugin = data;
        this.name = getStringFromMap(map, "name", "Unknown Name");
        this.boss = getStringFromMap(map, "boss", null);
        Object membersObj = map.get("members");
        if (membersObj instanceof ArrayList<?> membersList) {
            for (Object mObj : membersList) {
                if (mObj instanceof String m) {
                    try {
                        this.members.add(m);
                        if (Bukkit.getOfflinePlayer(m).isOnline()) {
                            Player player = Bukkit.getPlayer(m);
                            if (player != null) {
                                data.getData().addCPlayer(player, this);
                            }
                        }
                    } catch (IllegalArgumentException iae) {
                        System.err.println("Invalid UUID found: " + m);
                    }
                }
            }
        } else {
            // Handle case where 'members' is not an ArrayList or is null
            System.err.println("'members' is not an ArrayList or is null.");
        }

        Object enforcersObj = map.get("enforcers");
        if (enforcersObj instanceof ArrayList<?> enforcersList) {
            for (Object eObj : enforcersList) {
                if (eObj instanceof String e) {
                    try {
                        this.enforcers.add(e);
                        if (Bukkit.getOfflinePlayer(e).isOnline()) {
                            Player player = Bukkit.getPlayer(e);
                            if (player != null) {
                                data.getData().addCPlayer(player, this);
                            }
                        }
                    } catch (IllegalArgumentException iae) {
                        System.err.println("Invalid UUID found: " + e);
                    }
                }
            }
        }

        Object upgradesObj = map.get("unlockedUpgrades");
        if (upgradesObj instanceof ArrayList<?> upgradesList) {
            for (Object uObj : upgradesList) {
                if (uObj instanceof String u) {
                    try {
                        this.unlockedUpgrades.add(u);
                    } catch (IllegalArgumentException iae) {
                        System.err.println("Invalid upgrade found: " + u);
                    }
                }
            }
        }
//        initializePlayers("members", map);
//        initializePlayers("enforcers", map);
        //initializeMail("sentMail", map);
        this.dateFounded = getStringFromMap(map, "dateFounded", "Unknown Date");
        this.description = getStringFromMap(map, "description", "No description set.");
        this.kills = getIntFromMap(map, "kills", 0);
        this.vault = getIntFromMap(map, "vault", 0);
        this.influence = getIntFromMap(map, "influence", ConfigManager.DEFAULT_RATING);
        this.level = getIntFromMap(map, "level", 1);
        this.levelUpCost = getIntFromMap(map, "levelUpCost", 25);
        this.rating = getIntFromMap(map, "rating", ConfigManager.DEFAULT_RATING);
        this.enforcerLimit = getIntFromMap(map, "enforcerLimit", 1);
        this.memberLimit = getIntFromMap(map, "memberLimit", 3);
        this.skirmishWins = getIntFromMap(map, "skirmishWins", 0);
        this.skirmishDraws = getIntFromMap(map, "skirmishDraws", 0);
        this.skirmishLosses = getIntFromMap(map, "skirmishLosses", 0);
        this.banner = getBannerFromMap(map);
        this.compound = getLocationFromMap(map);
        this.vaultDeposits = getVaultDepositsFromMap(map, "vaultDeposits");
    }

    private void initializePlayers(String key, Map<String, Object> map) {
        List<String> players = getListFromMap(map, key);
        for (String playerId : players) {
            addPlayerToCrew(playerId);
        }
    }

    private HashMap<String, Integer> getVaultDepositsFromMap(Map<String, Object> map, String key) {
        HashMap<String, Integer> deposits = new HashMap<>();
        Object obj = map.get(key);
        if (obj instanceof Map<?, ?> depositsMap) {
            for (Map.Entry<?, ?> entry : depositsMap.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof Number) {
                    try {
                        UUID uuid = UUID.fromString((String) entry.getKey());
                        Integer amount = ((Number) entry.getValue()).intValue();
                        deposits.put(uuid.toString(), amount);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid UUID or amount in vaultDeposits: " + entry.getKey());
                    }
                } else {
                    System.err.println("Key or value type mismatch: Key should be String, value should be Number.");
                }
            }
        } else {
            System.err.println(key + " is not a Map or is null.");
        }
        return deposits;
    }

    private List<String> getListFromMap(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (obj instanceof ArrayList) {
            return (ArrayList<String>) obj;
        }
        System.err.println(key + " is not an ArrayList or is null.");
        return new ArrayList<>();
    }

    private void initializeMail(String key, Map<String, Object> map) {
        List<String> messages = getListFromMap(map, key);
        for (String m : messages) {
            addToMail(m);
        }
    }

    private void addPlayerToCrew(String uuid) {
        if (Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline()) {
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if (player != null) {
                this.plugin.getData().addCPlayer(player, this);
            }
        }
    }
    private String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        return value instanceof String ? (String) value : defaultValue;
    }

    private int getIntFromMap(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Double) {
            return (int) (double) value;
        } else if (value instanceof Integer) {
            return (Integer) value;
        }
        return defaultValue;
    }

    private ItemStack getBannerFromMap(Map<String, Object> map) {
        Object value = map.get("banner");
        return value instanceof ItemStack ? (ItemStack) value : new ItemStack(Material.WHITE_BANNER);
    }

    private Location getLocationFromMap(Map<String, Object> map) {
        Map<String, Object> compoundMap = (Map<String, Object>) map.get("compound");
        if (compoundMap != null) {
            World world = Bukkit.getWorld((String) compoundMap.get("world"));
            double x = (double) compoundMap.get("x");
            double y = (double) compoundMap.get("y");
            double z = (double) compoundMap.get("z");
            float yaw = (float) ((double) compoundMap.get("yaw"));
            float pitch = (float) ((double) compoundMap.get("pitch"));
            return new Location(world, x, y, z, yaw, pitch);
        }
        return null;
    }

    public int getRank() {
        Map<Crew, Integer> sortedList =this.plugin.getData().generateLeaderboardJson();
        int rank = 1;
        for (Map.Entry<Crew, Integer> entry : sortedList.entrySet()) {
            if (entry.getKey().equals(this)) {
                return rank;
            }
            rank++;
        }
        return -1;
    }
}
