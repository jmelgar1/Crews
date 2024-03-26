package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        if (args.length != 1) {
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
        if (pCrew.getVault() >= cost) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        pCrew.setCompound(p);
        pCrew.removeFromVault(cost, p);
    }
}
