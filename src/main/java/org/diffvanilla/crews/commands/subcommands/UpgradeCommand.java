package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

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
        String playerCrew = crewManager.getPlayercrew(p);

        if(!playerCrew.equals("none")) {
            JsonObject crewsJson = crewsClass.getcrewsJson();
            JsonObject crewObject = crewsJson.getAsJsonObject(playerCrew);
            int upgradeCost = crewObject.get("requiredSponges").getAsInt();
            int crewVault = crewObject.get("vault").getAsInt();
            if(args.length == 1) {
                if(crewManager.CheckForChief(playerCrew, p)) {
                    if(crewManager.getLevel(playerCrew) != 10) {
                        crewManager.removeFromVault(playerCrew, upgradeCost, p);
                        crewManager.upgradecrew(playerCrew, crewVault, p);
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Your crew is max level!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Only crew chiefs can upgrade the crew!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
