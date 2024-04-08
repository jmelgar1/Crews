package org.ovclub.crews.utilities;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PaginationUtilities {
    public static TextComponent createPageNavigation(String text, int page) {
        TextComponent component = new TextComponent(text);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crews help " + page));
        return component;
    }
}
