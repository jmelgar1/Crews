package org.diffvanilla.crews.commands.subcommands;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class AcceptCommand implements SubCommand {
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Accept a crew invite.";
	}

    @Override
    public String getSyntax() {
        return "/crew accept [crew name]";
    }

    @Override
    public String getPermission() {
        return "crews.player.accept";
    }

    @Override
	public void perform(Player p, String[] args, Crews plugin) {
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

        targetCrew.addPlayer(p);
        plugin.getData().handleInvite(p, targetCrew, true);
        //crewManager.generateScorePercrew(otherCrew);
    }
}
