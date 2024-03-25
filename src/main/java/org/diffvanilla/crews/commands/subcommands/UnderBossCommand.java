package org.diffvanilla.crews.commands.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.CommandManager;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class UnderBossCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "View crew underboss commands.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews underboss";
	}

    @Override
    public String getPermission() {
        return "crews.player.underboss";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        CommandManager manager = new CommandManager();

        p.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.DARK_PURPLE + "crews ELDER GUIDE" + ChatColor.GRAY + " ]-----");
        for(int i = 0; i < manager.getBossCommands().size(); i++){
            TextComponent commandText = new TextComponent(ChatColor.GRAY.toString() + (i + 1) +
                ". " + ChatColor.LIGHT_PURPLE + "/crews " + manager.getUnderBossCommands().get(i).getName() +
                ChatColor.WHITE + " - " + manager.getUnderBossCommands().get(i).getDescription());

            commandText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crews " + manager.getUnderBossCommands().get(i).getName()));
            commandText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/crews " + manager.getUnderBossCommands().get(i).getName())));
            p.spigot().sendMessage(commandText);
        }
        p.sendMessage(ChatColor.GRAY + "-----------------------------");
    }
}
