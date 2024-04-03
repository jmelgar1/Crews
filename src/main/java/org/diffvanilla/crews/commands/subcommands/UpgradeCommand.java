package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class UpgradeCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Upgrade your crew level.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews upgrade";
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
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
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
        int currentLevel = pCrew.getLevel();
        if (levelUpCost <= pCrew.getVault()) {
            pCrew.removeFromVault(levelUpCost, p, false);
            //new upgrade feature
            //crewManager.upgradecrew(playerCrew, crewVault, p);
        }
    }
}
