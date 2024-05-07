package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.GUICreator;

public class InfoCommand implements SubCommand {

    @Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "See your crew's info.";
	}

    @Override
    public String getSyntax() {
        return "/crew info";
    }

    @Override
    public String getPermission() {
        return "crew.player.info";
    }

    @Override
	public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if(args.length >= 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (args.length == 0) {
            if (pCrew != null) {
                Crew c = plugin.getData().getCrewOrError(p);
                GUICreator.createCrewInfoGUI(data, p, pCrew);
                c.showInfo(p, true);
            } else {
                p.sendMessage(ConfigManager.NOT_IN_CREW);
                return;
            }
        }
		if(args.length == 1) {
            Crew tCrew = data.getCrew(args[0]);
            if (tCrew != null) {
                tCrew.showInfo(p, false);
            } else {
                p.sendMessage(ConfigManager.CREW_NOT_FOUND);
            }
        }
	}
}
