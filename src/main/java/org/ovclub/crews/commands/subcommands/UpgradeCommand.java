package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class UpgradeCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Upgrade your crew level.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c upgrade";
	}

    @Override
    public String getPermission() {
        return "crews.player.upgrade";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length != 0) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        if (pCrew.isMaxLevel()) {
            p.sendMessage(ConfigManager.CREW_IS_MAX_LEVEL);
            return;
        }
        int levelUpCost = pCrew.getLevelUpCost();
        if(levelUpCost > pCrew.getVault()) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        pCrew.removeFromVault(levelUpCost, p, false);
        pCrew.upgradeCrew();
    }
}
