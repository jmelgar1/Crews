package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class LeaveCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Leave a crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews leave [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.leave";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        Crew tCrew = data.getCrew(args[1]);
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(!pCrew.equals(tCrew)) {
            p.sendMessage(ConfigManager.INCORRECT_CREW);
            return;
        }
        if (pCrew.isBoss(p)) {
            if (pCrew.getMembers().size() == 1) {
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
