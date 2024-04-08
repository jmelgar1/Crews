package org.ovclub.crews.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ComponentUtilities {
    public static TextComponent createEmojiComponent(String emoji, String titleLabel, TextColor labelColor, String titleText, TextColor titleColor) {
        return Component.text(emoji).color(UnicodeCharacters.emoji_text_color)
            .append(Component.text(titleLabel).color(labelColor)
                .append(Component.text(titleText).color(titleColor))).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent createInfluenceComponent(int influence) {
        return Component.text(UnicodeCharacters.influence_emoji).color(UnicodeCharacters.emoji_text_color)
            .append(Component.text("Influence: ").color(UnicodeCharacters.info_text_color)
                .append(Component.text("[").color(UnicodeCharacters.influence_outline_color))
                .append(Component.text(influence).color(UnicodeCharacters.influence_color))
                .append(Component.text("]").color(UnicodeCharacters.influence_outline_color)))
            .decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent createDoubleEmojiComponent(String emoji, TextColor emojiColor, String titleLabel, TextColor labelColor, String unlockEmoji, TextColor unlockColor) {
        return Component.text(emoji).color(emojiColor)
            .append(Component.text(titleLabel).color(labelColor)
                .append(Component.text(unlockEmoji).color(unlockColor))).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent createComponentWithDecoration(String text, TextColor color, TextDecoration decoration) {
        return Component.text(text).color(color).decorate(decoration).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent createComponent(String text, TextColor color) {
        return Component.text(text).color(color).decoration(TextDecoration.ITALIC, false);
    }

    public static TextComponent createComponentWithPlaceHolder(String text, TextColor color, String placeholder, TextColor placeholderColor) {
        return Component.text(text).color(color).append(Component.text(placeholder).color(placeholderColor)).decoration(TextDecoration.ITALIC, false);
    }
}
