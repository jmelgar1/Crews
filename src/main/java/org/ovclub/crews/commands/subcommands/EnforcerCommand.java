package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;

public class EnforcerCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "View crew enforcer commands.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews enforcer";
	}

    @Override
    public String getPermission() {
        return "crews.player.enforcer";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
//        CommandManager manager = new CommandManager();
//
//        p.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.DARK_PURPLE + "crews ELDER GUIDE" + ChatColor.GRAY + " ]-----");
//        for(int i = 0; i < manager.getBossCommands().size(); i++){
//            TextComponent commandText = new TextComponent(ChatColor.GRAY.toString() + (i + 1) +
//                ". " + ChatColor.LIGHT_PURPLE + "/crews " + manager.getEnforcerCommands().get(i).getName() +
//                ChatColor.WHITE + " - " + manager.getEnforcerCommands().get(i).getDescription());
//
//            commandText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crews " + manager.getEnforcerCommands().get(i).getName()));
//            commandText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/crews " + manager.getEnforcerCommands().get(i).getName())));
//            p.spigot().sendMessage(commandText);
//        }
//        p.sendMessage(ChatColor.GRAY + "-----------------------------");
    }
}
