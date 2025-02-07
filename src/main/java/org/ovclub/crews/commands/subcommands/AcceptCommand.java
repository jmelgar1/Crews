package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class AcceptCommand implements SubCommand {
	@Override
	public String getDescription() {
		return "Accept a crew invite.";
	}

    @Override
    public String getSyntax() {
        return "/c accept [crew name]";
    }

    @Override
    public String getPermission() {
        return "crews.player.accept";
    }

    @Override
	public void perform(Player p, String[] args, Crews plugin) {
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
        targetCrew.addPlayer(p);
        plugin.getData().handleInvite(p, targetCrew, true);
    }
}
