package org.diffvanilla.crews.managers;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.diffvanilla.crews.utilities.MessageUtilities;

import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class ConfigManager {
    public static void loadConfig(final FileConfiguration config){
        MAX_MEMBERS = config.getInt("max-members");
        COMMANDS_PER_PAGE = config.getInt("commands-per-page");
        TELEPORT_DELAY = config.getInt("teleport-delay");
        WARP_PRICE = config.getInt("warp-price");


        /* Text Components */
        ALREADY_IN_CREW = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-in-crew")))));
        CAN_NOT_INVITE_SELF = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-invite-self")))));
        CREW_NO_COMPOUND = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-no-compound")))));
        CREW_CHAT_ENABLED = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-chat-enabled")))));
        CREW_CHAT_DISABLED = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-chat-disabled")))));
        CREW_NAME_ONLY_ALPHABETICAL = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-only-alphabetical")))));
        CREW_NAME_BETWEEN_4_AND_16 = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-between-4-and-16")))));
        COMPOUND_REMOVED = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-removed")))));
        COMPOUND_SET = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-set")))));
        MUST_BE_BOSS = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-boss")))));
        MUST_BE_HIGHERUP = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-higherup")))));
        NAME_TAKEN = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-invite")))));
        NO_INVITE = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("name-taken")))));
        NO_PERMISSION = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-permission")))));
        NOT_IN_CREW = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg-must-be-in-crew")))));
        NOT_ENOUGH_SPONGE = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-enough-sponge")))));
        PLAYER_NOT_FOUND = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-found")))));
        PLAYER_NOT_UNDERBOSS = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-underboss")))));
        PLEASE_WAIT_TELEPORT = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("please-wait-tp")))));
        SUCCESS = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg-success")))));
        CAN_NOT_DEMOTE_SELF = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-demote-self")))));
        YOU_ARE_BOSS = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-are-boss")))));
        UNDERBOSS_PROMOTE = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-are-underboss")))));
        UNDERBOSS_DEMOTE = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("underboss-demote")))));

        /* Strings */
        ALREADY_INVITED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-invited")));
        CREW_DISBAND = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-disband")));
        CREW_FOUNDED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-founded")));
        JOIN_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("joined-crew")));
        KICKED_FROM_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("kicked-from-crew")));
        LEAVE_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("leave-crew")));
        NEW_BOSS = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("new-boss")));
        NEW_UNDERBOSS = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("new-underboss")));
        PLAYER_IN_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-in-crew")));
        PLAYER_NOT_IN_SAME_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-in-same-crew")));
        PLAYER_NOT_ONLINE = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-online")));
        PLAYER_NOT_FREE_AGENT = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-free-agent")));
        PLAYER_UNDERBOSS_DEMOTE = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("reached-max-members")));
        UPGRADE_NOT_UNLOCKED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("upgrade-not-unlocked")));
        REACHED_MAX_MEMBERS = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("reached-max-members")));
        REACHED_MAX_MEMBERS = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("reached-max-members")));
    }

    public static int COMMANDS_PER_PAGE;
    public static int MAX_MEMBERS;
    public static int TELEPORT_DELAY;
    public static int WARP_PRICE;
    public static String ALREADY_INVITED;
    public static String CREW_DISBAND;
    public static String CREW_FOUNDED;
    public static String JOIN_CREW;
    public static String KICKED_FROM_CREW;
    public static String LEAVE_CREW;
    public static String NEW_BOSS;
    public static String NEW_UNDERBOSS;
    public static String PLAYER_IN_CREW;
    public static String PLAYER_NOT_IN_SAME_CREW;
    public static String PLAYER_NOT_ONLINE;
    public static String PLAYER_NOT_FREE_AGENT;
    public static String PLAYER_UNDERBOSS_DEMOTE;
    public static String REACHED_MAX_MEMBERS;
    public static String UPGRADE_NOT_UNLOCKED;
    public static TextComponent ALREADY_IN_CREW;
    public static TextComponent CAN_NOT_DEMOTE_SELF;
    public static TextComponent CAN_NOT_INVITE_SELF;
    public static TextComponent CREW_NAME_ONLY_ALPHABETICAL;
    public static TextComponent CREW_NAME_BETWEEN_4_AND_16;
    public static TextComponent CREW_CHAT_ENABLED;
    public static TextComponent CREW_CHAT_DISABLED;
    public static TextComponent CREW_NO_COMPOUND;
    public static TextComponent COMPOUND_REMOVED;
    public static TextComponent COMPOUND_SET;
    public static TextComponent MUST_BE_BOSS;
    public static TextComponent MUST_BE_HIGHERUP;
    public static TextComponent NAME_TAKEN;
    public static TextComponent NOT_ENOUGH_SPONGE;
    public static TextComponent NOT_IN_CREW;
    public static TextComponent NO_INVITE;
    public static TextComponent NO_PERMISSION;
    public static TextComponent PLAYER_NOT_FOUND;
    public static TextComponent PLAYER_NOT_UNDERBOSS;
    public static TextComponent PLEASE_WAIT_TELEPORT;
    public static TextComponent SUCCESS;
    public static TextComponent YOU_ARE_BOSS;
    public static TextComponent UNDERBOSS_PROMOTE;
    public static TextComponent UNDERBOSS_DEMOTE;
}

