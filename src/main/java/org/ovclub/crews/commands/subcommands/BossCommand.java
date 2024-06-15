package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;

public class BossCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "View crew boss commands.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c boss";
	}

    @Override
    public String getPermission() {
        return "crews.player.boss";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
//        CommandManager manager = new CommandManager(plugin);
//
//        p.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.DARK_RED + "crews CHIEF GUIDE" + ChatColor.GRAY + " ]-----");
//        for(int i = 0; i < manager.getBossCommands().size(); i++){
//            TextComponent commandText = new TextComponent(ChatColor.GRAY.toString() + (i + 1) +
//                ". " + ChatColor.RED + "/crews " + manager.getBossCommands().get(i).getName() +
//                ChatColor.WHITE + " - " + manager.getBossCommands().get(i).getDescription());
//
//            commandText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crews " + manager.getBossCommands().get(i).getName()));
//            commandText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/crews " + manager.getBossCommands().get(i).getName())));
//            p.spigot().sendMessage(commandText);
//        }
//        p.sendMessage(ChatColor.GRAY + "-----------------------------");
    }
}
