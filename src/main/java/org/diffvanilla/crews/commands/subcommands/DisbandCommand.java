package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class DisbandCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Disband a crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews disband [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.disband";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        String targetCrew = args[1];
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(data.getCrew(targetCrew) != pCrew){
            p.sendMessage(ConfigManager.NOT_YOUR_CREW);
            return;
        }
        if(!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        pCrew.disband();
    }
}
