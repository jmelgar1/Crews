package org.diffvanilla.crews.managers;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.diffvanilla.crews.utilities.MessageUtilities;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class ConfigManager {
    /**
     * Translates hex color codes and alternate color codes in the given message.
     *
     * @param message The message to translate color codes in.
     * @return The message with color codes translated.
     */
    public static String translateColors(String message) {
        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }
        matcher.appendTail(buffer);

        // Translate alternate color codes (&c, &l, etc.)
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static void loadConfig(final FileConfiguration config){
        MAX_MEMBERS = config.getInt("max-members");
        COMMANDS_PER_PAGE = config.getInt("commands-per-page");
        TELEPORT_DELAY = config.getInt("teleport-delay");
        WARP_COST = config.getInt("warp-price");
        SET_COMPOUND_COST = config.getInt("set-compound-cost");
        RENAME_COST = config.getInt("crew-rename-cost");
        UPGRADE_CHAT_COST = config.getInt("upgrade-chat-cost");
        UPGRADE_MAIL_COST = config.getInt("upgrade-mail-cost");
        UPGRADE_DISCORD_COST = config.getInt("upgrade-discord-cost");
        INFLUENCE_PER_PLAYER = config.getInt("influence-per-player");


        /* Text Components */
        ALREADY_IN_CREW = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-in-crew")))));
        CAN_NOT_INVITE_SELF = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-invite-self")))));
        CANT_KICK_BOSS = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-boss")))));
        CANT_KICK_SAME_RANK = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-same-rank")))));
        CHOOSE_DIFFERENT_NAME = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("choose-different-name")))));
        CREW_IS_MAX_LEVEL = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-is-max-level")))));
        CREW_NO_COMPOUND = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-no-compound")))));
        CREW_NOT_FOUND = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-not-found")))));
        CREW_CHAT_ENABLED = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-chat-enabled")))));
        CREW_CHAT_DISABLED = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-chat-disabled")))));
        CREW_NAME_ONLY_ALPHABETICAL = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-only-alphabetical")))));
        CREW_NAME_BETWEEN_4_AND_16 = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-between-4-and-16")))));
        COMPOUND_REMOVED = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-removed")))));
        COMPOUND_EXISTS = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-exists")))));
        COMPOUND_SET = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-set")))));
        FULL_INVENTORY = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("full-inventory")))));
        INCORRECT_CREW = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("incorrect-crew")))));
        MAX_LEVEL_ENFORCER_LIMIT = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("max-level-limit-enforcer")))));
        MUST_BE_BOSS = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-boss")))));
        MUST_BE_HIGHERUP = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-higherup")))));
        NAME_TAKEN = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-invite")))));
        NO_INVITE = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("name-taken")))));
        NO_PERMISSION = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-permission")))));
        NOT_IN_CREW = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-in-crew")))));
        NOT_YOUR_CREW = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-your-crew")))));
        NOT_ENOUGH_IN_VAULT = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-enough-sponge")))));
        PLAYER_NOT_FOUND = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-found")))));
        PLAYER_NOT_ENFORCER = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-enforcer")))));
        PLEASE_WAIT_TELEPORT = MessageUtilities.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("please-wait-tp")))));
        SUCCESS = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg-success")))));
        TRANSFER_OWNERSHIP = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("transfer-ownership")))));
        CAN_NOT_DEMOTE_SELF = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-demote-self")))));
        CAN_NOT_PROMOTE_SELF = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-promote-self")))));
        YOU_ARE_BOSS = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-are-boss")))));
        ENFORCER_PROMOTE = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-are-enforcer")))));
        ENFORCER_DEMOTE = MessageUtilities.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("enforcer-demote")))));

        /* Strings */
        ALREADY_INVITED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-invited")));
        ALREADY_ENFORCER = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-enforcer")));
        CREW_DISBAND = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-disband")));
        CREW_FOUNDED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-founded")));
        ENFORCER_LIMIT = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("max-enforcer")));
        JOIN_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("joined-crew")));
        KICKED_FROM_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("kicked-from-crew")));
        LEAVE_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("leave-crew")));
        NEW_BOSS = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("new-boss")));
        NEW_ENFORCER = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("new-enforcer")));
        SPONGE_DEPOSIT = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("sponge-deposit")));
        SPONGE_WITHDRAW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("sponge-withdraw")));
        PLAYER_IN_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-in-crew")));
        PLAYER_NOT_IN_SAME_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-in-same-crew")));
        PLAYER_NOT_ONLINE = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-online")));
        PLAYER_NOT_FREE_AGENT = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-free-agent")));
        PLAYER_ENFORCER_DEMOTE = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-enforcer-demote")));
        UPGRADE_NOT_UNLOCKED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("upgrade-not-unlocked")));
        UPGRADE_ALREADY_UNLOCKED = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("upgrade-already-unlocked")));
        REACHED_MAX_MEMBERS = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("reached-max-members")));
    }

    public static int COMMANDS_PER_PAGE;
    public static int MAX_MEMBERS;
    public static int TELEPORT_DELAY;
    public static int WARP_COST;
    public static int SET_COMPOUND_COST;
    public static int RENAME_COST;
    public static int UPGRADE_CHAT_COST;
    public static int UPGRADE_MAIL_COST;
    public static int UPGRADE_DISCORD_COST;
    public static int INFLUENCE_PER_PLAYER;
    public static String ALREADY_INVITED;
    public static String ALREADY_ENFORCER;
    public static String CREW_DISBAND;
    public static String CREW_FOUNDED;
    public static String ENFORCER_LIMIT;
    public static String JOIN_CREW;
    public static String KICKED_FROM_CREW;
    public static String LEAVE_CREW;
    public static String NEW_BOSS;
    public static String NEW_ENFORCER;
    public static String PLAYER_IN_CREW;
    public static String PLAYER_NOT_IN_SAME_CREW;
    public static String PLAYER_NOT_ONLINE;
    public static String PLAYER_NOT_FREE_AGENT;
    public static String PLAYER_ENFORCER_DEMOTE;
    public static String REACHED_MAX_MEMBERS;
    public static String SPONGE_DEPOSIT;
    public static String SPONGE_WITHDRAW;
    public static String UPGRADE_NOT_UNLOCKED;
    public static String UPGRADE_ALREADY_UNLOCKED;
    public static TextComponent ALREADY_IN_CREW;
    public static TextComponent CAN_NOT_DEMOTE_SELF;
    public static TextComponent CAN_NOT_PROMOTE_SELF;
    public static TextComponent CAN_NOT_INVITE_SELF;
    public static TextComponent CANT_KICK_BOSS;
    public static TextComponent CANT_KICK_SAME_RANK;
    public static TextComponent CHOOSE_DIFFERENT_NAME;
    public static TextComponent CREW_IS_MAX_LEVEL;
    public static TextComponent CREW_NAME_ONLY_ALPHABETICAL;
    public static TextComponent CREW_NAME_BETWEEN_4_AND_16;
    public static TextComponent CREW_CHAT_ENABLED;
    public static TextComponent CREW_CHAT_DISABLED;
    public static TextComponent CREW_NO_COMPOUND;
    public static TextComponent CREW_NOT_FOUND;
    public static TextComponent COMPOUND_REMOVED;
    public static TextComponent COMPOUND_EXISTS;
    public static TextComponent COMPOUND_SET;
    public static TextComponent FULL_INVENTORY;
    public static TextComponent INCORRECT_CREW;
    public static TextComponent MAX_LEVEL_ENFORCER_LIMIT;
    public static TextComponent MUST_BE_BOSS;
    public static TextComponent MUST_BE_HIGHERUP;
    public static TextComponent NAME_TAKEN;
    public static TextComponent NOT_ENOUGH_IN_VAULT;
    public static TextComponent NOT_IN_CREW;
    public static TextComponent NOT_YOUR_CREW;
    public static TextComponent NO_INVITE;
    public static TextComponent NO_PERMISSION;
    public static TextComponent PLAYER_NOT_FOUND;
    public static TextComponent PLAYER_NOT_ENFORCER;
    public static TextComponent PLEASE_WAIT_TELEPORT;
    public static TextComponent SUCCESS;
    public static TextComponent TRANSFER_OWNERSHIP;
    public static TextComponent YOU_ARE_BOSS;
    public static TextComponent ENFORCER_PROMOTE;
    public static TextComponent ENFORCER_DEMOTE;
}
