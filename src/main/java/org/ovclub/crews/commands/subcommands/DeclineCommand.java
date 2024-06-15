package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class DeclineCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Decline a crew invite.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c decline [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.decline";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        if (args.length != 1) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        if (plugin.getData().getCrew(p) != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_CREW);
            return;
        }
        if (!plugin.getData().hasInvitation(p)) {
            p.sendMessage(ConfigManager.NO_INVITE);
            return;
        }
        Crew targetCrew = plugin.getData().getCrew(args[0]);
        plugin.getData().handleInvite(p, targetCrew, false);
        //add success message
    }
}
