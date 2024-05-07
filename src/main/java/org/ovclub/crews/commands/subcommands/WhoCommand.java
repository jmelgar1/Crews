package org.ovclub.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;

public class WhoCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "See what crew a player is in.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews who [player]";
	}

    @Override
    public String getPermission() {
        return "crews.player.who";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        OfflinePlayer tPlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
        Crew tCrew = data.getCrew(tPlayer);
        if(tCrew == null) {
            p.sendMessage(ConfigManager.CREW_NOT_FOUND);
            return;
        }
        tCrew.showInfo(p, false);
    }
}
