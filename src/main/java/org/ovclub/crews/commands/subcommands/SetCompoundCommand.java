package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;

public class SetCompoundCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Set the crew compound.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews setcompound";
	}

    @Override
    public String getPermission() {
        return "crews.player.setcompound";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        int cost = ConfigManager.SET_COMPOUND_COST;
        if (args.length != 0) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (!pCrew.isHigherup(p)) {
            p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
            return;
        }
        if (pCrew.hasCompound()) {
            p.sendMessage(ConfigManager.COMPOUND_EXISTS);
            return;
        }
        if (pCrew.getVault() < cost) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        pCrew.setCompound(p);
        pCrew.removeFromVault(cost, p, true);
    }
}
