package org.diffvanilla.crews.commands.subcommands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.managers.CrewManager;
import org.diffvanilla.crews.managers.UpgradeManager;

public class updateCrewsCommand implements CommandExecutor {

	private CrewManager crewManager = new CrewManager();
	private UpgradeManager upgradeManager = new UpgradeManager();

	String cmd1 = "crewsupdate";

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase(cmd1)) {
				if (p.hasPermission("crews.update")) {
					crewManager.generateScore();
					upgradeManager.addUpgradesSection();
				}
			}
		}
		return true;
	}
}
