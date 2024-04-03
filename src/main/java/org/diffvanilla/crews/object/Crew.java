package org.diffvanilla.crews.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
import org.diffvanilla.crews.utilities.UnicodeCharacters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final int ratingScore;
    public int getRatingScore() { return this.ratingScore;}

    //description
    private String description;
    public void setDescription(String s) {
        this.description = s;
    }

    //crew upgrades
    private List<String> unlockedUpgrades;
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

    //mail
    private ArrayList<String> sentMail;
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
    private final int levelUpCost;
    public int getLevelUpCost() { return this.levelUpCost; }
    private final int memberLimit;
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
        this.vault = vault + amount;
        calculateInfluence();
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
        this.vault = vault - amount;
        calculateInfluence();
    }

    public void calculateInfluence() {
        int totalPlayers = this.members.size() + this.enforcers.size() + 1;
        int playerPower = totalPlayers * ConfigManager.INFLUENCE_PER_PLAYER;
        int influence = this.vault + this.ratingScore + playerPower;
        setInfluence(influence);
    }

    //Set new crew leader & add old one to members.
    public void replaceBoss(final UUID id) {
        this.members.add(this.boss);
        this.boss = id.toString();
        Crew pCrew = plugin.getData().getCrew(Bukkit.getPlayer(this.boss));
        String crewName = pCrew.getName();
        for (String member : this.members) {
            UUID pUUID = UUID.fromString(member);
            if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                Player p = Bukkit.getPlayer(pUUID);
                if(p != null){
                    p.sendMessage(ConfigManager.NEW_BOSS
                        .replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName()))
                        .replaceText(builder -> builder.matchLiteral("{crew}").replacement(crewName)));
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
        sendMessageWithHeader(p, "┌──────[ ", this.name, " ]───────◓");
        if(this.description == null) {
            this.description = "No description set";
        }
        sendInfoMessage(p, UnicodeCharacters.founded_emoji, "Founded: ", this.dateFounded, UnicodeCharacters.founded_color);
        sendInfoMessage(p, UnicodeCharacters.description_emoji, "Description: ", this.description, UnicodeCharacters.description_color);
        sendInfoMessage(p, UnicodeCharacters.level_emoji, "Level: ", String.valueOf(this.level), UnicodeCharacters.level_color);
        sendInfoMessage(p, UnicodeCharacters.vault_emoji, "Vault: ", UnicodeCharacters.sponge_icon + this.vault, UnicodeCharacters.sponge_color);
        sendInfluenceMessage(p, UnicodeCharacters.influence_emoji, String.valueOf(this.influence));
        sendInfoMessage(p, UnicodeCharacters.boss_emoji, "Boss: ", getPlayerName(UUID.fromString(this.boss)), UnicodeCharacters.boss_color);

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
        sendInfoMessage(p, UnicodeCharacters.enforcers_emoji, "Enforcers: ", enforcersList.toString(), UnicodeCharacters.enforcers_color);

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
        sendInfoMessage(p, UnicodeCharacters.members_emoji, "Members: ", membersList.toString(), UnicodeCharacters.members_color);

        if(inCrew) {
            TextColor activeColor = TextColor.fromHexString("#00FF00");
            TextColor inactiveColor = TextColor.fromHexString("#FF0000");

            String statusText = (hasCompound()) ? "ACTIVE" : "INACTIVE";
            TextColor statusColor = (hasCompound()) ? activeColor : inactiveColor;

            sendInfoMessage(p, UnicodeCharacters.compound_emoji, "Compound: ", statusText, statusColor);
        }
        p.sendMessage(Component.text("└────────────────────◒").color(UnicodeCharacters.logo_color));
    }

    /* CREW LEVEL */
    public boolean isMaxLevel() {
        return this.level == 10;
    }

    //Disband crew and delete all SPlayers
    public void disband() {
        plugin.getData().removeCPlayer(Bukkit.getPlayer(this.boss));
        for (String members : this.getMembers()) {
            UUID pUUID = UUID.fromString(members);
            plugin.getData().removeCPlayer(Bukkit.getPlayer(pUUID));
        }
        Bukkit.broadcast(ConfigManager.CREW_DISBAND.replaceText(builder -> builder.matchLiteral("{crew}").replacement(this.name)));
        plugin.getData().removeCrew(this);
    }

    private void sendInfoMessage(Player p, String prefixEmoji, String prefix, String text, TextColor color) {
        p.sendMessage(Component.text("│ ").color(UnicodeCharacters.logo_color)
                .append(Component.text(prefixEmoji).color(UnicodeCharacters.emoji_text_color))
            .append(Component.text(prefix).color(UnicodeCharacters.info_text_color))
            .append(Component.text(text).color(color)));
    }

    private void sendInfluenceMessage(Player p, String prefixEmoji, String influence) {
        p.sendMessage(Component.text("│ ").color(UnicodeCharacters.logo_color)
            .append(Component.text(prefixEmoji).color(UnicodeCharacters.emoji_text_color))
            .append(Component.text("Influence: ").color(UnicodeCharacters.info_text_color))
            .append(Component.text("[").color(UnicodeCharacters.influence_outline_color))
            .append(Component.text(influence).color(UnicodeCharacters.influence_color))
            .append(Component.text("]").color(UnicodeCharacters.influence_outline_color)));
    }

    private void sendMessageWithHeader(Player p, String prefix, String headerText, String suffix) {
        p.sendMessage(Component.text(prefix).color(UnicodeCharacters.logo_color)
            .append(Component.text(headerText).color(UnicodeCharacters.plugin_color))
            .append(Component.text(suffix).color(UnicodeCharacters.logo_color)));
    }

    private String getOnlinePlayerName(UUID playerId) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
        if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null) {
            return offlinePlayer.getPlayer().getName();
        } else {
            return offlinePlayer.getName();
        }
    }

    private String getPlayerName(UUID playerId) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
        System.out.println(offlinePlayer.getName());
        return offlinePlayer.getName();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> unlockedUpgrades = new ArrayList<>(this.unlockedUpgrades);
        ArrayList<String> sentMail = new ArrayList<>(this.sentMail);
        ArrayList<String> membersStr = new ArrayList<>(this.members);
        ArrayList<String> enforcersStr = new ArrayList<>(this.enforcers);

        map.put("name", this.name);
        map.put("level", this.level);
        map.put("dateFounded", this.dateFounded);
        map.put("vault", this.vault);
        map.put("boss", this.boss);
        map.put("enforcers", enforcersStr);
        map.put("levelUpCost", this.levelUpCost);
        map.put("memberLimit", this.memberLimit);
        map.put("members", membersStr);
        map.put("description", this.description);
        map.put("compound", this.compound);
        map.put("kills", this.kills);
        map.put("ratingScore", this.ratingScore);
        map.put("influence", this.influence);
        map.put("unlockedUpgrades", unlockedUpgrades);
        map.put("sentMail", sentMail);
        return map;
    }

    //Remove player from crew, whether they left or were kicked. Returns true if player was in the crew, false if player was not.
    public void removePlayer(final UUID pUUID, boolean wasKicked) {
        this.enforcers.remove(pUUID.toString());
        if (this.members.remove(pUUID.toString())) {
            plugin.getData().removeCPlayer(Bukkit.getPlayer(pUUID));
            String pName = Bukkit.getOfflinePlayer(pUUID).getName();
            if(wasKicked){
                this.broadcast(ConfigManager.KICKED_FROM_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(pName)));
            } else {
                this.broadcast(ConfigManager.LEAVE_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(pName)));
            }
            calculateInfluence();
        }
    }

    //Add player to crew, player must be online to join so we use Player
    public void addPlayer(final Player p) {
        this.members.add(p.getUniqueId().toString());
        plugin.getData().addCPlayer(p, this);
        this.broadcast(ConfigManager.JOIN_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
        calculateInfluence();
    }

    public boolean isBoss(Player p) {
        String pUUID = p.getUniqueId().toString();
        return pUUID.equals(this.boss);
    }

    public boolean isEnforcer(Player p) {
        String pUUID = p.getUniqueId().toString();
        return this.enforcers.contains(pUUID);
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

    public static Crew deserialize(Map<String, Object> map, Crews data) {
        return new Crew(map, data);
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

        this.plugin = data;
        this.plugin.getData().addCPlayer(p, this);
        this.name = name;
        this.boss = p.getUniqueId().toString();
        this.dateFounded = currentDate.format(formatter);
        this.description = "No description set.";
        this.kills = 0;
        this.vault = 0;
        this.level = 1;
        this.influence = 300;
        this.levelUpCost = 25;
        this.ratingScore = 0;
        this.enforcerLimit = 1;
        this.memberLimit = 3;
        this.unlockedUpgrades = new ArrayList<>();
    }

    //Constructor for loaded crew
    public Crew(Map<String, Object> map, final Crews data) {
        this.plugin = data;
        this.name = (String) map.get("name");
        if (map.get("boss") != null) {
            this.boss = (String) map.get("boss");
//            if (Bukkit.getOfflinePlayer(UUID.fromString(this.boss)).isOnline()) {
//                Player player = Bukkit.getPlayer(UUID.fromString(this.boss));
//                if (player != null) {
//                    data.getData().addCPlayer(player, this);
//                }
//            }
        }
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
                            System.out.println(e);
                            Player player = Bukkit.getPlayer(e);
                            if (player != null) {data.getData().addCPlayer(player, this);}
                        }
                    } catch (IllegalArgumentException iae) {
                        System.err.println("Invalid UUID found: " + e);
                    }
                }
            }
        } else {
            // Handle case where 'enforcers' is not an ArrayList or is null
            System.err.println("'enforcers' is not an ArrayList or is null.");
        }
        this.dateFounded = (String) map.get("dateFounded");
        if(map.get("description") == null) this.description = "No description set."; else this.description = (String) map.get("description");
        if (map.get("kills") == null) this.kills = 0; else this.kills = (int) map.get("kills");
        if (map.get("compound") != null) {
            Map<String, Object> compoundMap = (Map<String, Object>) map.get("compound");
            World world = Bukkit.getWorld((String) compoundMap.get("world"));
            double x = Double.parseDouble((String) compoundMap.get("x"));
            double y = Double.parseDouble((String) compoundMap.get("y"));
            double z = Double.parseDouble((String) compoundMap.get("z"));
            float yaw = Float.parseFloat((String) compoundMap.get("yaw"));
            float pitch = Float.parseFloat((String) compoundMap.get("pitch"));

            this.compound = new Location(world, x, y, z, yaw, pitch);
        }
        if(map.get("vault") == null) this.vault = 0; else this.vault = (int) ((double) map.get("vault"));
        if(map.get("influence") == null) this.influence = 0; else this.influence = (int) ((double) map.get("influence"));
        if(map.get("level") == null) this.level = 1; else this.level = (int) ((double) map.get("level"));
        if(map.get("levelUpCost") == null) this.levelUpCost = 25; else this.levelUpCost = (int) map.get("levelUpCost");
        if(map.get("ratingScore") == null) this.ratingScore = 0; else this.ratingScore = (int) ((double) map.get("ratingScore"));
        if(map.get("enforcerLimit") == null) this.enforcerLimit = 1; else this.enforcerLimit = (int) map.get("enforcerLimit");
        if(map.get("memberLimit") == null) this.memberLimit = 3; else this.memberLimit = (int) map.get("memberLimit");
    }
}
