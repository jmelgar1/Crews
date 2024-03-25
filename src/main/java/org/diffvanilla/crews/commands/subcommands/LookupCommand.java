package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class LookupCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "View another crew's info.";
	}

    @Override
    public String getSyntax() {
        return "/crews lookup [crew]";
    }

    @Override
    public String getPermission() {
        return "crews.player.lookup";
    }

    @Override
	public void perform(Player p, String[] args, Crews crews) throws NotInCrew {
		JsonObject crewsFile = crews.getcrewsJson();

		if (args.length == 2) {
			String otherCrew = args[1];

			JsonObject crewSection = crewsFile.getAsJsonObject(otherCrew.toLowerCase());
			crewManager.getcrewInfo(crewsFile, crewSection, otherCrew, p, true);
		} else {
			ChatUtilities.CorrectUsage(p);
		}
	}
}
