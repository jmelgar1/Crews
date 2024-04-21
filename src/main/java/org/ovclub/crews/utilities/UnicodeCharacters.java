package org.ovclub.crews.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class UnicodeCharacters {

    /* Unicode Characters */

    /* Crews /info */
    public static String crews = "👥 ";
    public static String boss_emoji = "👑 ";
    public static String founded_emoji = "📖 ";
    public static String description_emoji = "📰 ";
    public static String level_emoji = "🥃 ";
    public static String vault_emoji = "🏛 ";
    public static String influence_emoji = "🌍 " ;
    public static String enforcers_emoji = "⛑ ";
    public static String members_emoji = "⚔ ";
    public static String skirmish_emoji = "⚔ ";
    public static String compound_emoji = "🕋 ";
    public static String sponge_icon = "❐";
    public static String xp_emoji = "✨ ";
    public static String trophy = "🏆";
    public static String shop_emoji = "💲 ";
    public static String rating_emoji = "🎖 ";
    public static String crew_chat_emoji = "💬 ";
    public static String discord_emoji = "🔊 ";
    public static String siren_emoji = "🚨 ";
    public static String upgrade_icon = "\uD83E\uDC81 ";
    public static String mail_emoji = "\uD83D\uDCEA ";
    public static String vault = "\uD83C\uDFE6";
    public static String foundedDate = "\uD83D\uDCDD";
    public static String level = "\uD83D\uDD2E";
    public static String powerScore = "\uD83C\uDFC6";
    public static String chiefCrown = "\uD83D\uDC51";
    public static String elderFace = "\uD83D\uDC77";
    public static String member = "\uD83E\uDD16";
    public static String compound = "\uD83C\uDFE0";
    public static String mail = "\uD83D\uDCEA";
    public static String rating = "\uD83C\uDFC5";
    public static String economy_icon = "\uD83D\uDCB0 ";
    public static String xp = "✨";
    public static String flag = "\uD83D\uDEA9 ";
    public static String koth = "⛰";
    public static String tott = "\uD83D\uDDFC";
    public static String checkmark_emoji = "✅ ";
    public static String x_emoji = "❌ ";

    //DECENT HOLOGRAMS TELEPORT INFO TO YOU

    /* Hex Colors */
    public static String logoColor = "#4e4b5d";
    public static String textColor = "#777585";
    public static TextColor sponge_color = TextColor.fromHexString("#dfff00");
    public static TextColor influence_outline_color = TextColor.fromHexString("#8E2DE2");
    public static TextColor influence_color = TextColor.fromHexString("#D32CE6");
    public static TextColor level_color = TextColor.fromHexString("#F44336");
    public static TextColor upgrade_color = TextColor.fromHexString("#2E7D32");
    public static TextColor description_color = TextColor.fromHexString("#FFF59D");
    public static TextColor founded_color = TextColor.fromHexString("#81C784");
    public static TextColor economy_color = TextColor.fromHexString("#4CAF50");
    public static TextColor queue_color = TextColor.fromHexString("#F9A825");
    public static TextColor rating_color = TextColor.fromHexString("#FFEB3B");
    public static TextColor skirmish_color = TextColor.fromHexString("#EF5350");
    public static TextColor boss_color = TextColor.fromHexString("#3F51B5");
    public static TextColor enforcers_color = TextColor.fromHexString("#80DEEA");
    public static TextColor members_color = TextColor.fromHexString("#B2DFDB");
    public static TextColor logo_color = TextColor.fromHexString("#4e4b5d");
    public static TextColor plugin_color = TextColor.fromHexString("#7298C2");
    public static TextColor info_text_color = TextColor.fromHexString("#E0E0E0");
    public static TextColor emoji_text_color = TextColor.fromHexString("#FFEBEE");
    public static TextColor compound_inactive = TextColor.fromHexString("#E57373");
    public static TextColor compound_active = TextColor.fromHexString("#81C784");
    public static TextColor teamA_color = TextColor.fromHexString("#90CAF9");
    public static TextColor teamB_color = TextColor.fromHexString("#EF9A9A");

    /* Create Message Icon Headers */
    public static TextComponent createAlertIcon(TextColor color) {
        return Component.text("[💡] ").color(color);
    }
    public static TextComponent createXIcon(TextColor color) {return Component.text("[✘] ").color(color);}
    public static TextComponent createSuccessIcon(TextColor color) {
        return Component.text("[✔] ").color(color);
    }
    public static TextComponent createTeleportIcon(TextColor color) {
        return Component.text("[⤼] ").color(color);
    }
    public static TextComponent createCrewIcon(TextColor color) {
        return Component.text("[🎲] ").color(color);
    }
    public static TextComponent createCrownIcon(TextColor color) {return Component.text("[👑] ").color(color);}
    public static TextComponent createKickIcon(TextColor color) {
        return Component.text("[👢] ").color(color);
    }
    public static TextComponent createLeaveIcon(TextColor color){
        return Component.text("[⬅] ").color(color);
    }
    public static TextComponent createJoinIcon(TextColor color){
        return Component.text("[➡] ").color(color);
    }
    public static TextComponent createSpongeIcon(TextColor color) {
        return Component.text("[⧠] ").color(color);
    }
    public static TextComponent createPromoteIcon(TextColor color) {
        return Component.text("[⟰] ").color(color);
    }
    public static TextComponent createDemoteIcon(TextColor color) {
        return Component.text("[⟱] ").color(color);
    }
    public static TextComponent createMailIcon(TextColor color) {return Component.text("[📪] ").color(color);}
    public static TextComponent createCompoundIcon(TextColor color) {return Component.text("[🏯] ").color(color);}
    public static TextComponent createChatIcon(TextColor color) {return Component.text("[✉] ").color(color);}
    public static TextComponent createDisbandIcon(TextColor color) {return Component.text("[💥] ").color(color);}
    public static TextComponent createCrewsIcon(TextColor color) {return Component.text("[👥] ").color(color);}
    public static TextComponent createQueueIcon(TextColor color) {return Component.text("[⏳] ").color(color);}
    public static TextComponent createSkirmishIcon(TextColor color) {return Component.text("[⚔] ").color(color);}
    public static TextComponent createRatingIcon(TextColor color) {return Component.text("[🎖] ").color(color);}
    public static TextComponent createUpgradeIcon(TextColor color) {return Component.text("[🢁] ").color(color);}


    public static void sendInfoMessage(Player p, String prefixEmoji, String prefix, String text, TextColor color) {
        p.sendMessage(Component.text("│ ").color(UnicodeCharacters.logo_color)
            .append(Component.text(prefixEmoji).color(UnicodeCharacters.emoji_text_color))
            .append(Component.text(prefix).color(UnicodeCharacters.info_text_color))
            .append(Component.text(text).color(color)));
    }

    public static  void sendInfluenceMessage(Player p, String prefixEmoji, String influence) {
        p.sendMessage(Component.text("│ ").color(UnicodeCharacters.logo_color)
            .append(Component.text(prefixEmoji).color(UnicodeCharacters.emoji_text_color))
            .append(Component.text("Influence: ").color(UnicodeCharacters.info_text_color))
            .append(Component.text("[").color(UnicodeCharacters.influence_outline_color))
            .append(Component.text(influence).color(UnicodeCharacters.influence_color))
            .append(Component.text("]").color(UnicodeCharacters.influence_outline_color)));
    }

    public static  void sendMessageWithHeader(Player p, String prefix, String headerText, String suffix) {
        p.sendMessage(Component.text(prefix).color(UnicodeCharacters.logo_color)
            .append(Component.text(headerText).color(UnicodeCharacters.plugin_color).decorate(TextDecoration.BOLD))
            .append(Component.text(suffix).color(UnicodeCharacters.logo_color)));
    }

    public static void sendUnbalancedMessage(Player p, int smallSize, int largeSize, boolean smallerTeam) {
        Component messageComponent;
        String orderOne;
        String orderTwo;

        if(smallerTeam) {
            messageComponent = Component.text()
                .append(Component.text("Since you are the smaller team please type the amount\n").color(NamedTextColor.WHITE))
                .append(Component.text("│ ").color(UnicodeCharacters.logo_color))
                .append(Component.text("of opponents you would like to fight!").color(NamedTextColor.WHITE))
                .build();
            orderOne = "- Your Team Size: ";
            orderTwo = "- Opponent's Team Size: ";
        } else {
            messageComponent = Component.text()
                .append(Component.text("Since you are the larger team please wait for the other\n").color(NamedTextColor.WHITE))
                .append(Component.text("│ ").color(UnicodeCharacters.logo_color))
                .append(Component.text("team to determine your team size!").color(NamedTextColor.WHITE))
                .build();
            orderOne = "- Opponent's Team Size: ";
            orderTwo = "- Your Team Size: ";
        }

        p.sendMessage(Component.text()
            .append(Component.text("┌──────────────────◓ \n").color(UnicodeCharacters.logo_color))
            .append(Component.text("│ ").color(UnicodeCharacters.logo_color))
            .append(Component.text(UnicodeCharacters.siren_emoji).color(NamedTextColor.RED))
            .append(Component.text("ALERT: ").color(NamedTextColor.YELLOW))
            .append(Component.text("Unbalanced Match! \n").color(NamedTextColor.GRAY))
            .append(Component.text("│ ").color(UnicodeCharacters.logo_color))
            .append(messageComponent)
            .append(Component.text("\n│ ").color(UnicodeCharacters.logo_color))
            .append(Component.text(orderOne).color(NamedTextColor.GRAY))
            .append(Component.text(Integer.toString(smallSize)).color(NamedTextColor.DARK_GREEN))
            .append(Component.text("\n│ ").color(UnicodeCharacters.logo_color))
            .append(Component.text(orderTwo).color(NamedTextColor.GRAY))
            .append(Component.text(Integer.toString(largeSize)).color(NamedTextColor.DARK_GREEN))
            .append(Component.text("\n└──────────────────◒").color(UnicodeCharacters.logo_color))
            .build());
    }

}
