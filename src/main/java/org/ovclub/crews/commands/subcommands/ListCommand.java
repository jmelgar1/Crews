package org.ovclub.crews.commands.subcommands;

import java.util.Map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;

import org.ovclub.crews.utilities.UnicodeCharacters;

public class ListCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "List all active crews";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c list [page #]";
	}

    @Override
    public String getPermission() {
        return "crews.player.list";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            int pageNum;
            if (args.length == 1) {
                try {
                    pageNum = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
                    return;
                }
            } else {
                pageNum = 1;
            }

            Map<Crew, Integer> sortedList = data.generateLeaderboardJson();
            int totalEntries = sortedList.size();
            int entriesPerPage = 10;
            int lastPageNum = (int) Math.ceil(totalEntries / (double) entriesPerPage);
            if (pageNum < 1 || pageNum > lastPageNum) {
                p.sendMessage(Component.text("Page number is out of range. Please enter a number between 1 and " + lastPageNum + ".", TextColor.fromHexString("#FF0000")));
                return;
            }

            int firstEntryIndex = (pageNum - 1) * entriesPerPage;
            int count = 0;
            boolean isAlternateColor = false;

            TextComponent leaderBoardTitle = Component.text("=======[ ").color(TextColor.fromHexString("#BDBDBD"))
                .append(Component.text((UnicodeCharacters.trophy + " CREWS LEADERBOARD " + UnicodeCharacters.trophy)).color(TextColor.fromHexString("#FFD54F"))
                    .decorate(TextDecoration.BOLD)).append(Component.text(" ]=======")).color(TextColor.fromHexString("#BDBDBD"));
            TextComponent crewHighTable = Component.text("-=-=").color(TextColor.fromHexString("#BDBDBD"))
                .append(Component.text((" Crew High Table ")).color(TextColor.fromHexString("#9575CD")))
                .append(Component.text("=-=-")).color(TextColor.fromHexString("#BDBDBD"));
            TextComponent crewHighTableFooter = Component.text("-=-=-=-=-=-=-=-=-=-=-=-").color(TextColor.fromHexString("#BDBDBD"));
            p.sendMessage(leaderBoardTitle);
            p.sendMessage(Component.text(""));
            for (Map.Entry<Crew, Integer> entry : sortedList.entrySet()) {
                if (count >= firstEntryIndex && count < firstEntryIndex + entriesPerPage) {
                    String crewName = entry.getKey().getName();
                    int influence = entry.getValue();

                    if(count == 0) {
                        p.sendMessage(crewHighTable);
                    }

                    TextColor color;
                    if(pageNum == 1 && count == 5) {
                        p.sendMessage(crewHighTableFooter);
                    }
                    if (entry.getKey().isInHighTable()) {
                        color = (count % 2 == 0) ? TextColor.fromHexString("#FFD700") : TextColor.fromHexString("#C5A500");
                    } else {
                        color = isAlternateColor ? TextColor.fromHexString("#90A4AE") : TextColor.fromHexString("#546E7A");
                        isAlternateColor = !isAlternateColor;
                    }

                    Component message = Component.text((count + 1) + ". ", color)
                        .append(Component.text(crewName, color))
                        .append(Component.text(" [", UnicodeCharacters.influence_outline_color))
                        .append(Component.text(influence, UnicodeCharacters.influence_color))
                        .append(Component.text("]", UnicodeCharacters.influence_outline_color));

                    message = message.hoverEvent(HoverEvent.showText(Component.text("View " + crewName)))
                        .clickEvent(ClickEvent.runCommand("/crews info " + crewName));

                    p.sendMessage(message);
                }
                if (++count >= firstEntryIndex + entriesPerPage) break;
            }

            // Combine pagination controls into a single line
            if (pageNum > 1 || pageNum < lastPageNum) {
                Component paginationLine = Component.empty(); // Start with an empty component

                if (pageNum > 1) {
                    Component previousPage = Component.text("< Previous", TextColor.fromHexString("#DCE775"))
                        .clickEvent(ClickEvent.runCommand("/crews list " + (pageNum - 1)))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to view previous page", TextColor.fromHexString("#BDBDBD"))))
                        .decorate(TextDecoration.BOLD);;
                    paginationLine = paginationLine.append(previousPage);
                }

                if (pageNum < lastPageNum) {
                    if (pageNum > 1) {
                        Component separator = Component.text(" | ", TextColor.fromHexString("#FFFFFF"));
                        paginationLine = paginationLine.append(separator);
                    }

                    Component nextPage = Component.text("Next >", TextColor.fromHexString("#AED581"))
                        .clickEvent(ClickEvent.runCommand("/crews list " + (pageNum + 1)))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to view next page", TextColor.fromHexString("#BDBDBD"))))
                        .decorate(TextDecoration.BOLD);;
                    paginationLine = paginationLine.append(nextPage);
                }

                p.sendMessage(paginationLine);
            }
        });
    }
}
