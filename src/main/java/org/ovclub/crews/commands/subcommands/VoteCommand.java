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

public class VoteCommand implements SubCommand {

	@Override
	public String getDescription() {
		return "Hightable voting.";
	}

	@Override
	public String getSyntax() {
		return "/crews vote";
	}

    @Override
    public String getPermission() {
        return "crews.player.vote";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew crew = data.getCrew(p);
        if (args.length != 0) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(crew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(!crew.isInHighTable()) {
            p.sendMessage(ConfigManager.NOT_IN_HIGHTABLE);
            return;
        }
        GUICreator.createHighTableVoteGUI(plugin.getData(), p);
    }
}
