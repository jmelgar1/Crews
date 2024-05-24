package org.ovclub.crews.managers.file;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.ovclub.crews.utilities.UnicodeCharacters;
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
        DEFAULT_RATING = config.getInt("default-rating");
        MAX_VAULT_AMOUNT = config.getInt("max-vault-amount");

        /* Skirmish Stuff */
        ARENA_RADIUS = config.getInt("arena-radius");
        WALL_BUFFER = config.getInt("wall-buffer");
        WALL_HEIGHT = config.getInt("wall-height");
        WALL_WIDTH = config.getInt("wall-width");

        /* Text Components */
        ALREADY_IN_CREW = UnicodeCharacters.createXIcon(TextColor.fromHexString("#F44336")).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-in-crew")))));
        ALREADY_IN_QUEUE = UnicodeCharacters.createXIcon(TextColor.fromHexString("#F44336")).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("already-in-queue")))));
        CAN_NOT_INVITE_SELF = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("can-not-invite-self")))));
        CANT_KICK_BOSS = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-boss")))));
        CANT_KICK_SAME_RANK = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-same-rank")))));
        CANT_KICK_SELF = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("cant-kick-self")))));
        CHOOSE_DIFFERENT_NAME = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("choose-different-name")))));
        CREW_HAS_JOINED_QUEUE = UnicodeCharacters.createQueueIcon(TextColor.fromHexString("#FF7043")).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-has-joined-queue")))));
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
        DESCRIPTION_SET = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("description-set")))));
        PURCHASE_SUCCESSFUL = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("purchase-successful")))));
        FULL_INVENTORY = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("full-inventory")))));
        INCORRECT_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("incorrect-crew")))));
        JOINED_QUEUE = UnicodeCharacters.createQueueIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("joined-queue")))));
        MAX_LEVEL_ENFORCER_LIMIT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("max-level-limit-enforcer")))));
        MUST_BE_BOSS = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-boss")))));
        MUST_BE_HIGHERUP = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-higherup")))));
        NAME_TAKEN = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("name-taken")))));
        NO_INVITE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-invite")))));
        NO_PERMISSION = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-permission")))));
        NOT_IN_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("must-be-in-crew")))));
        NOT_YOUR_CREW = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-your-crew")))));
        NOT_ENOUGH_IN_VAULT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-enough-in-vault")))));
        ONE_PLAYER_REQUIRED_FOR_QUEUE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("one-player-required-for-queue")))));
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
        WAITING_FOR_MATCHUP = UnicodeCharacters.createQueueIcon(UnicodeCharacters.queue_color).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("waiting-for-matchup")))));
        CREW_UPGRADE = UnicodeCharacters.createUpgradeIcon(UnicodeCharacters.upgrade_color).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-upgrade")))));
        VAULT_MAX_AMOUNT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("vault-max-amount")))));
        YOU_LEFT_CREW = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-left-crew")))));
        YOU_GOT_KICKED_CREW = UnicodeCharacters.createKickIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-got-kicked")))));


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

        /* Skirmish Started */
        SKIRMISH_STARTED = UnicodeCharacters.createSkirmishIcon(UnicodeCharacters.skirmish_color).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("skirmish-started")))));
        SKIRMISH_ENDED = LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("skirmish-ended"))));
        SKIRMISH_DRAW = LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("skirmish-draw"))));
        NOT_IN_QUEUE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-in-queue")))));
        YOU_HAVE_CONFIRMED = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("you-have-confirmed")))));
        WAITING_ON_CONFIRMATION = UnicodeCharacters.createQueueIcon(UnicodeCharacters.skirmish_color).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("waiting-on-confirmation")))));
        PLAYER_CONFIRMED = UnicodeCharacters.createSuccessIcon(TextColor.color(46,125,50)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-confirmed")))));
        INVALID_INTEGER = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("invalid-number")))));
        INVALID_RANGE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("invalid-range")))));
        CREW_HAS_CHOSEN_PLAYER_AMOUNT = UnicodeCharacters.createSkirmishIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-has-chosen-player-amount")))));
        ENEMY_HAS_CHOSEN_PLAYER_AMOUNT = UnicodeCharacters.createSkirmishIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("enemy-has-chosen-player-amount")))));
        PLAYERS_SITTING_OUT = UnicodeCharacters.createSkirmishIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("players-sitting-out")))));
        MATCH_CANCELLED_PLAYER_LEFT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("match-cancelled-player-left")))));
        MATCH_CANCELLED_DID_NOT_ACCEPT = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("match-cancelled-did-not-accept")))));
        CREW_REMOVED_FROM_QUEUE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("crew-removed-from-queue")))));
        SECONDS_LEFT_TO_ACTION = UnicodeCharacters.createAlertIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("seconds-left-to-action")))));
        TELEPORTING_COUNTDOWN = UnicodeCharacters.createSkirmishIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("teleporting-countdown")))));
        SKIRMISH_STARTING = UnicodeCharacters.createSkirmishIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("skirmish-starting")))));
        GAINED_RATING = UnicodeCharacters.createRatingIcon(TextColor.color(232,194,59)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("gained-rating")))));
        LOST_RATING = UnicodeCharacters.createRatingIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("lost-rating")))));
        NO_CHANGE_IN_RATING = UnicodeCharacters.createRatingIcon(TextColor.color(114, 107, 107)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("no-change-rating")))));
        SKIRMISH_BEGIN = UnicodeCharacters.createSkirmishIcon(TextColor.color(224,224,224)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("skirmish-begin")))));
        STAY_IN_BOUNDS = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("stay-in-bounds")))));
        DISABLED_IN_SKIRMISH = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("disabled-in-skirmish")))));
        DISABLED_COMMAND_IN_ARENA = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("disabled-command-in-arena")))));
        PLAYER_WILL_BE_BANNED = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-will-be-banned")))));
        PLAYER_BANNED = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("player-banned")))));

        /*High Table Stuff*/
        VOTE_SET = UnicodeCharacters.createVoteIcon(UnicodeCharacters.hightable_color).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("vote-set")))));
        NOT_IN_HIGHTABLE = UnicodeCharacters.createXIcon(TextColor.color(255,0,0)).append(LegacyComponentSerializer.legacyAmpersand().deserialize(translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("not-in-hightable")))));
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
    public static int DEFAULT_RATING;
    public static int MAX_VAULT_AMOUNT;
    public static int ARENA_RADIUS;
    public static int WALL_BUFFER;
    public static int WALL_HEIGHT;
    public static int WALL_WIDTH;
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
    public static TextComponent CANT_KICK_SELF;
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
    public static TextComponent PURCHASE_SUCCESSFUL;
    public static TextComponent DESCRIPTION_SET;
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
    public static TextComponent WAITING_FOR_MATCHUP;
    public static TextComponent CREW_UPGRADE;
    public static TextComponent VAULT_MAX_AMOUNT;
    public static TextComponent YOU_LEFT_CREW;
    public static TextComponent YOU_GOT_KICKED_CREW;

    /* Skirmish */
    public static TextComponent CREW_HAS_JOINED_QUEUE;
    public static TextComponent JOINED_QUEUE;
    public static TextComponent ALREADY_IN_QUEUE;
    public static TextComponent ONE_PLAYER_REQUIRED_FOR_QUEUE;
    public static TextComponent SKIRMISH_STARTED;
    public static TextComponent SKIRMISH_ENDED;
    public static TextComponent SKIRMISH_DRAW;
    public static TextComponent NOT_IN_QUEUE;
    public static TextComponent WAITING_ON_CONFIRMATION;
    public static TextComponent YOU_HAVE_CONFIRMED;
    public static TextComponent PLAYER_CONFIRMED;
    public static TextComponent INVALID_INTEGER;
    public static TextComponent INVALID_RANGE;
    public static TextComponent CREW_HAS_CHOSEN_PLAYER_AMOUNT;
    public static TextComponent ENEMY_HAS_CHOSEN_PLAYER_AMOUNT;
    public static TextComponent PLAYERS_SITTING_OUT;
    public static TextComponent MATCH_CANCELLED_PLAYER_LEFT;
    public static TextComponent MATCH_CANCELLED_DID_NOT_ACCEPT;
    public static TextComponent CREW_REMOVED_FROM_QUEUE;
    public static TextComponent SECONDS_LEFT_TO_ACTION;
    public static TextComponent TELEPORTING_COUNTDOWN;
    public static TextComponent SKIRMISH_STARTING;
    public static TextComponent GAINED_RATING;
    public static TextComponent LOST_RATING;
    public static TextComponent NO_CHANGE_IN_RATING;
    public static TextComponent SKIRMISH_BEGIN;
    public static TextComponent STAY_IN_BOUNDS;
    public static TextComponent DISABLED_IN_SKIRMISH;
    public static TextComponent DISABLED_COMMAND_IN_ARENA;
    public static TextComponent PLAYER_WILL_BE_BANNED;
    public static TextComponent PLAYER_BANNED;

    /*High table stuff*/
    public static TextComponent VOTE_SET;
    public static TextComponent NOT_IN_HIGHTABLE;
}
