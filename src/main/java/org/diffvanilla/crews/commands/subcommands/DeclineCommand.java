package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class DeclineCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Decline a crew invite.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews decline [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.decline";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        Crew targetCrew = plugin.getData().getCrew(args[1]);
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }

        if (!plugin.getData().hasInvitation(p)) {
            p.sendMessage(ConfigManager.NO_INVITE);
            return;
        }

        if (plugin.getData().getCrew(p) != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_CREW);
            return;
        }

        plugin.getData().handleInvite(p, targetCrew, false);
        //add success message
    }
}
