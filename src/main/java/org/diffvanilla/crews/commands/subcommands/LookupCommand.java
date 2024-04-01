package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
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
	public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew tCrew = data.getCrew(args[0]);
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }

        p.sendMessage(tCrew.getName());
        tCrew.showInfo(p, false);
	}
}
