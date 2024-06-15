package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class LeaveCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Leave a crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c leave [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.leave";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length != 1) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        Crew tCrew = data.getCrew(args[0]);
        if(!pCrew.equals(tCrew)) {
            p.sendMessage(ConfigManager.INCORRECT_CREW);
            return;
        }
        if (pCrew.isBoss(p)) {
            if (pCrew.getMembers().size() == 0) {
                pCrew.removePlayer(p.getUniqueId(), false);
                pCrew.disband();
                return;
            } else {
                p.sendMessage(ConfigManager.TRANSFER_OWNERSHIP);
            }
            return;
        }
        pCrew.removePlayer(p.getUniqueId(), false);
    }
}
