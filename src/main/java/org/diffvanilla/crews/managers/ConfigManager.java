package org.diffvanilla.crews.managers;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.diffvanilla.crews.utilities.UnicodeCharacters;

import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class ConfigManager {
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
        ALREADY_IN_CREW = UnicodeCharacters.createXIcon(TextColor.fromHexString("#F44336")).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-in-crew")))));
        CAN_NOT_INVITE_SELF = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-invite-self")))));
        CANT_KICK_BOSS = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-boss")))));
        CANT_KICK_SAME_RANK = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-same-rank")))));
        CHOOSE_DIFFERENT_NAME = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("choose-different-name")))));
        CREW_IS_MAX_LEVEL = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-is-max-level")))));
        CREW_NO_COMPOUND = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-no-compound")))));
        CREW_NOT_FOUND = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-not-found")))));
        CREW_CHAT_ENABLED = UnicodeCharacters.createChatIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-chat-enabled")))));
        CREW_CHAT_DISABLED = UnicodeCharacters.createChatIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-chat-disabled")))));
        CREW_NAME_ONLY_ALPHABETICAL = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-only-alphabetical")))));
        CREW_NAME_BETWEEN_4_AND_16 = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-between-4-and-16")))));
        COMPOUND_REMOVED = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-removed")))));
        COMPOUND_EXISTS = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-exists")))));
        COMPOUND_SET = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("compound-set")))));
        FULL_INVENTORY = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("full-inventory")))));
        INCORRECT_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("incorrect-crew")))));
        MAX_LEVEL_ENFORCER_LIMIT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("max-level-limit-enforcer")))));
        MUST_BE_BOSS = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-boss")))));
        MUST_BE_HIGHERUP = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-higherup")))));
        NAME_TAKEN = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-invite")))));
        NO_INVITE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("name-taken")))));
        NO_PERMISSION = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-permission")))));
        NOT_IN_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-in-crew")))));
        NOT_YOUR_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-your-crew")))));
        NOT_ENOUGH_IN_VAULT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-enough-in-vault")))));
        PLAYER_NOT_FOUND = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-found")))));
        PLAYER_NOT_ENFORCER = UnicodeCharacters.createXIcon(TextColor.color(255,0,0  )).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-enforcer")))));
        PLEASE_WAIT_TELEPORT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("please-wait-tp")))));
        SUCCESS = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("msg-success")))));
        TRANSFER_OWNERSHIP = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("transfer-ownership")))));
        CAN_NOT_DEMOTE_SELF = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-demote-self")))));
        CAN_NOT_PROMOTE_SELF = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-promote-self")))));
        YOU_ARE_BOSS = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-are-boss")))));
        ENFORCER_PROMOTE = UnicodeCharacters.createPromoteIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-are-enforcer")))));
        ENFORCER_DEMOTE = UnicodeCharacters.createDemoteIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("enforcer-demote")))));

        /* Strings */
        ALREADY_INVITED = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-invited")))));
        ALREADY_ENFORCER = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-enforcer")))));
        CREW_DISBAND = UnicodeCharacters.createDisbandIcon(TextColor.color(56,50,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-disband")))));
        CREW_FOUNDED = UnicodeCharacters.createCrewsIcon(TextColor.color(21,124,234)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-founded")))));
        ENFORCER_LIMIT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("max-enforcer")))));
        JOIN_CREW =  UnicodeCharacters.createJoinIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("joined-crew")))));
        KICKED_FROM_CREW = UnicodeCharacters.createKickIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("kicked-from-crew")))));
        LEAVE_CREW = UnicodeCharacters.createLeaveIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("leave-crew")))));
        NEW_BOSS = UnicodeCharacters.createCrownIcon(TextColor.color(51, 65, 141)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("new-boss")))));
        NEW_ENFORCER = UnicodeCharacters.createPromoteIcon(TextColor.color(89, 224, 241)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("new-enforcer")))));
        SPONGE_DEPOSIT = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("sponge-deposit")))));
        SPONGE_WITHDRAW = UnicodeCharacters.createSuccessIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("sponge-withdraw")))));
        //PLAYER_IN_CREW = translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-in-crew")));
        PLAYER_NOT_IN_SAME_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-in-same-crew")))));
        PLAYER_NOT_ONLINE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-online")))));
        PLAYER_NOT_FREE_AGENT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-not-free-agent")))));
        PLAYER_ENFORCER_DEMOTE = UnicodeCharacters.createDemoteIcon(TextColor.color(255, 0, 0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-enforcer-demote")))));
        UPGRADE_NOT_UNLOCKED = UnicodeCharacters.createXIcon(TextColor.color(187,211,21)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("upgrade-not-unlocked")))));
        UPGRADE_ALREADY_UNLOCKED = UnicodeCharacters.createXIcon(TextColor.color(187,211,21)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("upgrade-already-unlocked")))));
        REACHED_MAX_MEMBERS =  UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("reached-max-members")))));
        /* Vault Stuff */
        ADD_TO_VAULT = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("add-to-vault")))));
        REMOVE_FROM_VAULT = UnicodeCharacters.createSuccessIcon(TextColor.color(211,143,21)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("remove-from-vault")))));

        NEED_MORE_SPONGE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("need-more-sponge")))));
        INVALID_AMOUNT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("invalid-amount")))));

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
    public static TextComponent ALREADY_INVITED;
    public static TextComponent ALREADY_ENFORCER;
    public static TextComponent CREW_DISBAND;
    public static TextComponent CREW_FOUNDED;
    public static TextComponent ENFORCER_LIMIT;
    public static TextComponent JOIN_CREW;
    public static TextComponent KICKED_FROM_CREW;
    public static TextComponent LEAVE_CREW;
    public static TextComponent NEW_BOSS;
    public static TextComponent NEW_ENFORCER;
    //public static TextComponent PLAYER_IN_CREW;
    public static TextComponent PLAYER_NOT_IN_SAME_CREW;
    public static TextComponent PLAYER_NOT_ONLINE;
    public static TextComponent PLAYER_NOT_FREE_AGENT;
    public static TextComponent PLAYER_ENFORCER_DEMOTE;
    public static TextComponent REACHED_MAX_MEMBERS;
    public static TextComponent SPONGE_DEPOSIT;
    public static TextComponent SPONGE_WITHDRAW;
    public static TextComponent UPGRADE_NOT_UNLOCKED;
    public static TextComponent UPGRADE_ALREADY_UNLOCKED;
    public static TextComponent ADD_TO_VAULT;
    public static TextComponent REMOVE_FROM_VAULT;
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
    public static TextComponent INVALID_AMOUNT;
    public static TextComponent MAX_LEVEL_ENFORCER_LIMIT;
    public static TextComponent MUST_BE_BOSS;
    public static TextComponent MUST_BE_HIGHERUP;
    public static TextComponent NAME_TAKEN;
    public static TextComponent NEED_MORE_SPONGE;
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
