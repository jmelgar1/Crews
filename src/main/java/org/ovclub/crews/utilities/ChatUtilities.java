package org.ovclub.crews.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtilities {

    public static net.md_5.bungee.api.ChatColor crewsColor = net.md_5.bungee.api.ChatColor.of("#33F24D");
    public static net.md_5.bungee.api.ChatColor lightGreen = net.md_5.bungee.api.ChatColor.of("#00cc00");
    public static net.md_5.bungee.api.ChatColor lighterGreen = net.md_5.bungee.api.ChatColor.of("#84e184");
    public static net.md_5.bungee.api.ChatColor spongeColor = net.md_5.bungee.api.ChatColor.of("#dfff00");
    public static net.md_5.bungee.api.ChatColor tribalGames = net.md_5.bungee.api.ChatColor.of("#47b347");

    public String tgPrefix = tribalGames.toString() + net.md_5.bungee.api.ChatColor.BOLD + "TRIBAL GAMES: ";

    public String crewsPrefix = crewsColor + ChatColor.BOLD.toString() + "crews: ";

    public String sendError(String message) {
        return ChatColor.GRAY + "ERROR: " + message;
    }

    public String sendSuccess(String message) {
        return ChatColor.GREEN + "SUCCESS: " + message;
    }

    public String sendMajorError(String message) {
        return ChatColor.RED + "ERROR: " + ChatColor.RED + message;
    }

    public static String errorIcon = ChatColor.DARK_RED + "[✖] ";
    public static String usageIcon = ChatColor.DARK_GRAY + "[⚒] ";
    public static String successIcon = ChatColor.DARK_GREEN + "[✔] ";

    //error messages
//    public static void UpgradeSuccessful(String crew, String upgrade) {
//        CrewManager crewManager = new CrewManager();
//        crewManager.sendMessageToMembers(crew, ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "✨" + ChatColor.DARK_PURPLE + "] " + ChatColor.LIGHT_PURPLE + "Your crew has unlocked the " +
//            upgrade.toUpperCase() + " upgrade!");
//    }

    public void UpgradeNotUnlocked(Player p) {
        p.sendMessage(errorIcon + ChatColor.RED + "Your crew does not have this upgrade unlocked! "
            + ChatColor.YELLOW + "/crews shop");
    }

    public static void UpgradeAlreadyUnlocked(Player p) {
        p.sendMessage(errorIcon + ChatColor.RED + "Your crew already has this unlocked!");
    }

    public static void NeedMoreSponges(Player p) {
        p.sendMessage(errorIcon + ChatColor.RED + "You can not afford this! More sponges are required to be in the crew vault!");
    }

    public void NoActivecrewInvite(Player p) {
        p.sendMessage(errorIcon + ChatColor.RED + "You have no active crew invite! ");
    }

    public static void MustBeChiefOrElder(Player p) {
        p.sendMessage(errorIcon + ChatColor.RED + "You must be chief or elder to do this! ");
    }

    public static void MustBeChief(Player p) {
        p.sendMessage(errorIcon + ChatColor.RED + "You must be chief to do this! ");
    }


    public static void crewMembershipRequirement(Player p, String syntax) {
        p.sendMessage(errorIcon + ChatColor.RED + "You must be in a crew to use this command! " + ChatColor.YELLOW + syntax);
    }

    public static String CorrectUsage(String syntax) {
        return usageIcon + ChatColor.RED + "Correct Usage: " + ChatColor.GRAY + "\"" + syntax + "\"";
    }
}

