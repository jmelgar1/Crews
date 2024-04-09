package org.ovclub.crews.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

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
    public static String turfwar_emoji = "⚔ ";
    public static String compound_emoji = "🕋 ";
    public static String sponge_icon = "❐";
    public static String xp_emoji = "✨ ";
    public static String trophy = "🏆";
    public static String shop_emoji = "💲 ";
    public static String rating_emoji = "🎖 ";
    public static String crew_chat_emoji = "💬 ";
    public static String discord_emoji = "🔊 ";
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
    public static TextColor description_color = TextColor.fromHexString("#FFF59D");
    public static TextColor founded_color = TextColor.fromHexString("#81C784");
    public static TextColor economy_color = TextColor.fromHexString("#4CAF50");
    public static TextColor queue_color = TextColor.fromHexString("#F9A825");
    public static TextColor rating_color = TextColor.fromHexString("#FFEB3B");
    public static TextColor turfwar_color = TextColor.fromHexString("#EF5350");
    public static TextColor boss_color = TextColor.fromHexString("#3F51B5");
    public static TextColor enforcers_color = TextColor.fromHexString("#80DEEA");
    public static TextColor members_color = TextColor.fromHexString("#B2DFDB");
    public static TextColor logo_color = TextColor.fromHexString("#4e4b5d");
    public static TextColor plugin_color = TextColor.fromHexString("#7298C2");
    public static TextColor info_text_color = TextColor.fromHexString("#E0E0E0");
    public static TextColor emoji_text_color = TextColor.fromHexString("#FFEBEE");
    public static TextColor compound_inactive = TextColor.fromHexString("#E57373");
    public static TextColor compound_active = TextColor.fromHexString("#81C784");

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
}
