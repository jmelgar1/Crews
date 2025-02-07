package org.ovclub.crews.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtilities {
    public static TextComponent createEmojiComponent(String emoji, String titleLabel, TextColor labelColor, String titleText, TextColor titleColor) {
        return Component.text(emoji).color(UnicodeCharacters.emoji_text_color)
            .append(Component.text(titleLabel).color(labelColor)
                .append(Component.text(titleText).color(titleColor)))
            .decoration(TextDecoration.ITALIC, false);
    }
    public static TextComponent createInfluenceComponent(int influence) {
        return Component.text(UnicodeCharacters.influence_emoji).color(UnicodeCharacters.emoji_text_color)
            .append(Component.text("Influence: ").color(UnicodeCharacters.info_text_color)
                .append(Component.text("[").color(UnicodeCharacters.influence_outline_color))
                .append(Component.text(influence).color(UnicodeCharacters.influence_color))
                .append(Component.text("]").color(UnicodeCharacters.influence_outline_color)))
            .decoration(TextDecoration.BOLD, true);
    }
    public static TextComponent createDoubleEmojiComponent(String emoji, TextColor emojiColor, String titleLabel, TextColor labelColor, TextComponent unlockedIcon) {
        return Component.text(emoji).color(emojiColor)
            .append(Component.text(titleLabel).color(labelColor)
                .append(unlockedIcon))
            .decoration(TextDecoration.ITALIC, false);
    }
    public static TextComponent createWinLossRatio(String emoji, TextColor emojiColor, String titleLabel, TextColor labelColor, String wins, String draws, String losses) {
        return Component.text(emoji).color(emojiColor)
            .append(Component.text(titleLabel).color(labelColor)
            .append(Component.text(wins).color(NamedTextColor.DARK_GREEN)
            .append(Component.text("-").color(NamedTextColor.DARK_GRAY)
            .append(Component.text(draws).color(NamedTextColor.GRAY)
            .append(Component.text("-").color(NamedTextColor.DARK_GRAY)
            .append(Component.text(losses).color(NamedTextColor.DARK_RED)))))))
            .decoration(TextDecoration.ITALIC, false);
    }
    public static TextComponent createComponentWithDecoration(String text, TextColor color, TextDecoration decoration) {
        return Component.text(text).color(color).decorate(decoration)
            .decoration(TextDecoration.ITALIC, false);
    }
    public static TextComponent createComponent(String text, TextColor color) {
        return Component.text(text).color(color)
            .decoration(TextDecoration.ITALIC, false);
    }
    public static TextComponent createComponentWithPlaceHolder(String text, TextColor color, String placeholder, TextColor placeholderColor) {
        return Component.text(text).color(color).append(Component.text(placeholder).color(placeholderColor))
            .decoration(TextDecoration.ITALIC, false);
    }
    public static TextComponent[] splitVoteString(TextComponent voteString) {
        String voteStringContent = getContent(voteString);
        int midpoint = voteStringContent.length() / 2;
        int splitIndex = voteStringContent.lastIndexOf(' ', midpoint);
        if (splitIndex == -1) {
            splitIndex = voteStringContent.indexOf(' ', midpoint);
        }
        if (splitIndex == -1) {
            splitIndex = midpoint;
        }
        String firstPart = voteStringContent.substring(0, splitIndex).trim();
        String secondPart = voteStringContent.substring(splitIndex).trim();
        TextComponent firstLine = buildTextComponent(voteString, firstPart);
        TextComponent secondLine = buildTextComponent(voteString, secondPart);

        return new TextComponent[]{firstLine, secondLine};
    }
    private static String getContent(Component component) {
        if (component instanceof TextComponent textComponent) {
            StringBuilder content = new StringBuilder(textComponent.content());
            for (Component child : textComponent.children()) {
                content.append(getContent(child));
            }
            return content.toString();
        }
        return "";
    }
    private static TextComponent buildTextComponent(TextComponent original, String content) {
        List<Component> children = new ArrayList<>();
        int length = 0;
        for (Component child : original.children()) {
            String childContent = getContent(child);
            if (length + childContent.length() <= content.length()) {
                children.add(child);
                length += childContent.length();
            } else {
                break;
            }
        }
        return Component.text(content, original.style()).children(children);
    }

    public static void sendCrewMessage(Player player, int rank, boolean isHighTable, boolean hasVoted) {
        Component message = Component.empty()
            .append(Component.newline());

        if (isHighTable) {
            message = message
                .append(Component.text("┌─────[").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("The High Table").color(UnicodeCharacters.hightable_color).decorate(TextDecoration.BOLD))
                .append(Component.text(" ]──────◓").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.newline());

            if (!hasVoted) {
                message = message
                    .append(Component.text("│ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                    .append(Component.text("You have not voted yet! ")
                        .append(Component.text("VOTE HERE")
                            .color(NamedTextColor.LIGHT_PURPLE)
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/c vote"))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to vote").color(NamedTextColor.YELLOW))))
                        .color(UnicodeCharacters.hightable_color))
                    .append(Component.newline());
            } else {
                message = message
                    .append(Component.text("│ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                    .append(Component.text("You have already voted! ")
                        .append(Component.text("CHANGE VOTE")
                            .color(NamedTextColor.AQUA)
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/c vote"))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to change vote").color(NamedTextColor.YELLOW))))
                        .color(NamedTextColor.DARK_AQUA))
                    .append(Component.newline());
            }
        } else {
            message = message
                .append(Component.text("┌─────[ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.text("Crew Ranking").color(UnicodeCharacters.plugin_color).decorate(TextDecoration.BOLD))
                .append(Component.text(" ]──────◓").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
                .append(Component.newline());
        }

        // Rank line (common for both cases
        message = message
            .append(Component.text("│ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.text("Your crew is currently ranked ")
                .append(Component.text("#" + rank)
                    .color(NamedTextColor.GOLD))
                .color(NamedTextColor.YELLOW))
            .append(Component.newline())
            .append(Component.text("│ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.text("View daily multipliers ")
                .append(Component.text("HERE")
                    .color(NamedTextColor.DARK_PURPLE)
                    .decorate(TextDecoration.BOLD)
                    .clickEvent(ClickEvent.runCommand("/c multipliers"))
                    .hoverEvent(HoverEvent.showText(Component.text("Click to view").color(NamedTextColor.YELLOW))))
                .color(NamedTextColor.LIGHT_PURPLE))
            .append(Component.newline())
            .append(Component.text("└─────────────────────◓").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.newline()).append(Component.empty());

        player.sendMessage(message);
    }

    public static void joinCrewMessage(Player player) {
        Component message = Component.empty()
            .append(Component.newline());

        message = message
            .append(Component.text("┌───────[ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.text("Crews").color(UnicodeCharacters.plugin_color).decorate(TextDecoration.BOLD))
            .append(Component.text(" ]────────◓").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.newline())
            .append(Component.text("│ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.text("You are not part of a crew! ")
                .append(Component.text("CREATE ONE")
                    .color(NamedTextColor.AQUA)
                    .decorate(TextDecoration.BOLD)
                    .clickEvent(ClickEvent.suggestCommand("/crews create"))
                    .hoverEvent(HoverEvent.showText(Component.text("Click to create a crew").color(NamedTextColor.YELLOW))))
                .color(NamedTextColor.DARK_AQUA))
            .append(Component.newline())
            .append(Component.text("│ ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.text("View daily multipliers ")
                .append(Component.text("HERE")
                    .color(NamedTextColor.DARK_PURPLE)
                    .decorate(TextDecoration.BOLD)
                    .clickEvent(ClickEvent.runCommand("/c multipliers"))
                    .hoverEvent(HoverEvent.showText(Component.text("Click to view").color(NamedTextColor.YELLOW))))
                .color(NamedTextColor.LIGHT_PURPLE))
            .append(Component.newline())
            .append(Component.text("└────────────────────◓").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD))
            .append(Component.newline()).append(Component.empty());

        player.sendMessage(message);;
    }
    public static TextComponent toTitleCaseComponent(String section) {
        String formatted = section.replaceAll("([a-z])([A-Z])", "$1 $2");
        String[] words = formatted.split(" ");
        StringBuilder titleCase = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
            }
        }
        String titleCaseString = titleCase.toString().trim();
        TextColor color = switch (section) {
            case "oreDrops" -> UnicodeCharacters.oreDrops_color;
            case "mobDrops" -> UnicodeCharacters.mobDrops_color;
            case "mobDifficulty" -> UnicodeCharacters.mobDifficulty_color;
            case "xpDrops" -> UnicodeCharacters.xpDrops_color;
            case "discounts" -> UnicodeCharacters.discounts_color;
            default -> NamedTextColor.WHITE;
        };

        return Component.text(titleCaseString, color).decorate(TextDecoration.BOLD);
    }
}
