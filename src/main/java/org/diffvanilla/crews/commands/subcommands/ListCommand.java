package org.diffvanilla.crews.commands.subcommands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.GeneralUtilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ListCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "List all active crews";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews list [page #]";
	}

    @Override
    public String getPermission() {
        return "crews.player.list";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();

        //Slow process so run asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                int pageNum = 0;
                int firstEntry = 0;
                int lastEntry = 0;

                if(args.length == 1) {
                    pageNum = 1;
                    firstEntry = 1;
                    lastEntry = 10;
                } else if(args.length == 2) {
                    pageNum = Integer.parseInt(args[1]);
                    firstEntry = (pageNum*10)-9;
                    lastEntry = (pageNum*10);
                }

                Map<String, Double> sortedList = data.generateLeaderboardJson();
                int count = 1;
                int lastPageNum = (int)Math.ceil((sortedList.size()/10.0));
                if(firstEntry <= sortedList.size()) {
                    p.sendMessage(ChatUtilities.crewsColor + ChatColor.UNDERLINE.toString() + "Server crews:");

                    for(Map.Entry<String, Double> entry : sortedList.entrySet()) {
                        if(count >= firstEntry && count <= lastEntry) {
                            String crewName = entry.getKey();
                            Double powerScore = entry.getValue();

                            ChatColor countColor;
                            ChatColor placementColor;
                            if(count % 2 == 0){
                                countColor = ChatColor.GREEN;
                                placementColor = countColor;
                            } else {
                                countColor = ChatColor.DARK_GREEN;
                                placementColor = countColor;
                            }

                            if(count <= 5){
                                placementColor = ChatColor.GOLD;
                                countColor = ChatColor.YELLOW;
                            }

                            TextComponent crewInfo = new TextComponent(placementColor + "#" + count + ". " +
                                countColor + crewName + ChatColor.DARK_PURPLE + " ["
                                + ChatColor.LIGHT_PURPLE + powerScore + ChatColor.DARK_PURPLE + "]");
                            crewInfo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crews lookup " + crewName));
                            crewInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("View " + crewName)));
                            p.spigot().sendMessage(crewInfo);
                        }
                        count++;
                    }

                    String pageSelector;
                    if(lastPageNum > pageNum) {
                        pageSelector = ChatColor.GRAY + "Next Page >>>";
                        TextComponent nextPage = new TextComponent(pageSelector);
                        nextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crews list " + (pageNum + 1)));
                        nextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to view next page")));
                        p.spigot().sendMessage(nextPage);
                    }

                } else {
                    p.sendMessage(ChatUtilities.errorIcon + ChatColor.RED + "Last page number is " + (int)Math.ceil((sortedList.size()/10.0)));
                }
            }
        });
    }
}
