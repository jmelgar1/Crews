package org.diffvanilla.crews.object;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.managers.WarpCancelListener;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.MessageUtilities;

import java.util.*;

public class Crew implements ConfigurationSerializable {
    private final Crews plugin;

    //name
    private String name;
    public String getName() {return this.name;}

    private Location compound;

    //date founded
    private String dateFounded;
    public String getDateFounded() { return this.dateFounded; }

    //vault
    private int vault;
    public int getVault() { return this.vault; }

    //level
    private int level;
    public int getLevel() { return this.level; }

    //influence (power score)
    private double influence;
    public double getInfluence() { return this.influence; }

    //score (add up to influence)
    private int ratingScore;
    public int getRatingScore() { return this.ratingScore;}
    private double economyScore;
    public double getEconomyScore() { return this.economyScore;}

    //description
    private String description;
    public void setDescription(String s) {
        this.description = s;
    }

    //crew upgrades
    private List<String> unlockedUpgrades;
    public List<String> getUnlockedUpgrades() { return this.unlockedUpgrades; }

    //kills
    private int kills;
    public void addKill() {
        this.kills++;
    }
    public int getKills() {
        return this.kills;
    }

    //mail
    private ArrayList<String> sentMail;
    public ArrayList<String> getSentMail() { return this.sentMail; }

    //crew elites
    private UUID boss;
    public UUID getBoss() {
        return this.boss;
    }
    private ArrayList<UUID> enforcers;
    public ArrayList<UUID> getEnforcers() {
        return this.enforcers;
    }

    //members
    private ArrayList<UUID> members = new ArrayList<>();
    public ArrayList<UUID> getMembers() {
        return this.members;
    }

    //custom color
    private TextColor color = TextColor.color(0xE0E0E0);
    public TextColor getColor() {return this.color;}
    public void setColor(TextColor color) {
        this.color = color;
    }

    //sponge stuff
    private int levelUpCost;
    public int getLevelUpCost() { return this.levelUpCost; }
    private int memberLimit;
    public int getMemberLimit() { return this.memberLimit; }
    private int enforcerLimit;
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
                    plugin.getData().getCrew(p).removeFromVault(amount, p);

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
    }

    public boolean hasMember(Player p) {
        if (this.members.contains(p.getUniqueId())) return true;
        return this.boss.equals(p.getUniqueId());
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
        this.name = name;
        this.boss = p.getUniqueId();
        this.plugin = data;
        this.plugin.getData().addCPlayer(p, this);
        this.kills = 0;
    }

    //Constructor for loaded crew
    public Crew(Map<String, Object> map, final Crews data) {
        this.plugin = data;
        this.name = (String) map.get("name");
        if (map.get("boss") != null) this.boss = UUID.fromString((String) map.get("boss"));
        for (String m : (ArrayList<String>) map.get("members")) {
            this.members.add(UUID.fromString(m));
            if (Bukkit.getOfflinePlayer(UUID.fromString(m)).isOnline())
                data.getData().addCPlayer(Bukkit.getPlayer(UUID.fromString(m)), this);
        }
        this.description = (String) map.get("description");
        if (map.get("kills") == null) this.kills = 0; else this.kills = (int) map.get("kills");
        this.color = TextColor.fromHexString((String) map.get("color"));
        this.compound = (Location) map.get("compound");
    }

    //Broadcast to all members
    public void broadcast(final TextComponent text) {
        if(text == null) return;
        for (UUID pUUID : this.members) {
            if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                Player p = Bukkit.getPlayer(pUUID);
                if(p != null){p.sendMessage(text);}
            }
        }
        if (Bukkit.getOfflinePlayer(this.boss).isOnline()) {
            Player p = Bukkit.getPlayer(this.boss);
            if (p != null && p.isOnline()) {
                p.sendMessage(text);
            }
        }
    }

    //vault
    public void addToVault(int amount, Player p){
        this.vault = vault + amount;
    }
    public void removeFromVault(int amount, Player p){
        //takes player to put in the custom message (Player) removed vault blah
        this.vault = vault - amount;
    }

    //Set new crew leader & add old one to members.
    public void replaceBoss(final UUID id) {
        this.members.add(this.boss);
        this.boss = id;
        Crew pCrew = plugin.getData().getCrew(Bukkit.getPlayer(this.boss));
        String crewName = pCrew.getName();
        for (UUID uuid : this.members) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
                Player p = Bukkit.getPlayer(uuid);
                if(p != null){
                    p.sendMessage(MessageUtilities.createCrownIcon(TextColor.color(56, 142, 60)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.NEW_BOSS.replace("{player}", p.getName()).replace("{crew}", crewName))));
                }
            }
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.boss);
        if (offlinePlayer.isOnline()) {
            Player boss = Bukkit.getPlayer(this.boss);
            if (boss != null) {
                boss.sendMessage(ConfigManager.YOU_ARE_BOSS);
            }
        }
    }

    public void addEnforcer(final UUID id) {
        if(enforcers.size() < enforcerLimit) {
            enforcers.add(id);

            Player p = Bukkit.getPlayer(id);
            if(p != null) {
                p.sendMessage(ConfigManager.ENFORCER_PROMOTE);
            }
            broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.NEW_ENFORCER));
        }
    }

    public void removeEnforcer(final UUID id) {
        enforcers.remove(id);

        Player p = Bukkit.getPlayer(id);
        if(p != null) {
            p.sendMessage(ConfigManager.ENFORCER_DEMOTE);
        }
        broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.PLAYER_ENFORCER_DEMOTE));
    }

    public void showInfo(final Player p) {
        sendMessageWithHeader(p, "┌──────[ ", this.name, " ]───────◓");
        if(this.description == null) {
            this.description = "No description set";
        }
        sendInfoMessage(p, "Founded: ", this.dateFounded, descriptionColor);
        sendInfoMessage(p, "Description: ", this.description, descriptionColor);
        sendInfoMessage(p, "Level: ", String.valueOf(this.level), descriptionColor);
        sendInfoMessage(p, "Vault: ", String.valueOf(this.vault), descriptionColor);
        sendInfoMessage(p, "Influence: ", String.valueOf(this.influence), descriptionColor);
        //sendInfoMessage(p, "Kills: ", String.valueOf(this.kills), killsColor);

        String boss = getOnlinePlayerName(this.boss);
        if (boss != null) {
            sendInfoMessage(p, "Boss: ", boss, leaderColor);
        }

        StringBuilder enforceresList = new StringBuilder();
        if(!this.enforcers.isEmpty()) {
            for (UUID id : this.enforcers) {
                String playerName = getOnlinePlayerName(id);
                if (playerName != null) {
                    if (enforceresList.length() > 0) {
                        enforceresList.append(", ");
                    }
                    enforceresList.append(playerName);
                }
            }
        }
        sendInfoMessage(p, "Enforceres: ", enforceresList.toString(), membersColor);

        StringBuilder membersList = new StringBuilder();
        for (UUID id : this.members) {
            String playerName = getOnlinePlayerName(id);
            if(playerName != null) {
                if(membersList.length() > 0){
                    membersList.append(", ");
                }
                membersList.append(playerName);
            }
        }
        sendInfoMessage(p, "Members: ", membersList.toString(), membersColor);
        p.sendMessage(Component.text("└───────────────────◒").color(headerColor));
    }

    /* CREW LEVEL */
    public boolean isMaxLevel() {
        return this.level == 10;
    }

    //Disband crew and delete all SPlayers
    public void disband() {
        plugin.getData().removeCPlayer(Bukkit.getPlayer(this.boss));
        for (UUID id : this.getMembers()) {
            plugin.getData().removeCPlayer(Bukkit.getPlayer(id));
        }
        plugin.getData().removeCrew(this);
        Bukkit.broadcast(MessageUtilities.createCrewIcon(
                TextColor.color(211,47,47))
            .append(LegacyComponentSerializer.legacyAmpersand()
                .deserialize(ConfigManager.CREW_DISBAND
                    .replace("{crew}", this.name))));
    }

    private void sendInfoMessage(Player p, String prefix, String text, TextColor color) {
        p.sendMessage(Component.text("│ ").color(headerColor)
            .append(Component.text(prefix).color(infoColor))
            .append(Component.text(text).color(color)));
    }

    private void sendMessageWithHeader(Player p, String prefix, String headerText, String suffix) {
        p.sendMessage(Component.text(prefix).color(headerColor)
            .append(Component.text(headerText).color(defaultTextColor))
            .append(Component.text(suffix).color(headerColor)));
    }

    private String getOnlinePlayerName(UUID playerId) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
        if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null) {
            return offlinePlayer.getPlayer().getName();
        } else {
            return offlinePlayer.getName();
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> membersStr = new ArrayList<>();
        for (UUID i : this.members) {
            membersStr.add(i.toString());
        }
        map.put("name", this.name);
        map.put("leader", this.boss.toString());
        map.put("members", membersStr);
        map.put("description", this.description);
        map.put("compound", this.compound);
        map.put("kills", this.kills);
        return map;
    }

    //Remove player from crew, whether they left or were kicked. Returns true if player was in the crew, false if player was not.
    public void removePlayer(final UUID pUUID, boolean wasKicked) {
        this.enforcers.remove(pUUID);
        if (this.members.remove(pUUID)) {
            plugin.getData().removeCPlayer(Bukkit.getPlayer(pUUID));
            String pName = Bukkit.getOfflinePlayer(pUUID).getName();
            if(wasKicked){
                this.broadcast(MessageUtilities.createKickIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.KICKED_FROM_CREW.replace("{player}", pName))));
            } else {
                this.broadcast(MessageUtilities.createLeaveIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.LEAVE_CREW.replace("{player}", pName))));
            }
        }
    }

    //Add player to crew, player must be online to join so we use Player
    public void addPlayer(final Player p) {
        this.members.add(p.getUniqueId());
        plugin.getData().addCPlayer(p, this);
        this.broadcast(MessageUtilities.createJoinIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.JOIN_CREW.replace("{player}", p.getName()))));
    }

    public boolean isBoss(Player p) {
        UUID pUUID = p.getUniqueId();
        return pUUID.equals(this.boss);
    }

    public boolean isEnforcer(Player p) {
        UUID pUUID = p.getUniqueId();
        return this.enforcers.contains(pUUID);
    }

    public boolean isHigherup(Player p) {
        UUID pUUID = p.getUniqueId();
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

    public static Crew deserialize(Map<String, Object> map, Crews data) {
        return new Crew(map, data);
    }

    private final TextColor headerColor = TextColor.color(0xEF9A9A);
    private final TextColor infoColor = TextColor.color(0xFFF9C4);
    private final TextColor defaultTextColor = TextColor.color(0xEEEEEE);
    private final TextColor descriptionColor = TextColor.color(0xE0E0E0);
    private final TextColor killsColor = TextColor.color(0xFFA726);
    private final TextColor dtrColor = TextColor.color(0xE57373);
    private final TextColor leaderColor = TextColor.color(0xCE93D8);
    private final TextColor membersColor = TextColor.color(0x2E7D32);
    private final TextColor claimColor = TextColor.color(0x7CB342);
    private final TextColor noClaimColor = TextColor.color(0xD32F2F);
}
