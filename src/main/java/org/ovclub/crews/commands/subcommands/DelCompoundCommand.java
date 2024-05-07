package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.ChatUtilities;

public class DelCompoundCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Delete the crew compound.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews delcompound";
	}

    @Override
    public String getPermission() {
        return "crews.player.delcompound";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        Crew pCrew = plugin.getData().getCrew(p);
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (!pCrew.hasCompound()) {
            p.sendMessage(ConfigManager.CREW_NO_COMPOUND);
            return;
        }
        if(!pCrew.isHigherup(p)) {
            p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
            return;
        }
        pCrew.removeCompound();
    }
}
