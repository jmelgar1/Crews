package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class WhoCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "See what crew a player is in.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews who [player]";
	}

    @Override
    public String getPermission() {
        return "crews.player.who";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {

        JsonObject crewsJson = crewsClass.getcrewsJson();

        if(args.length == 2) {

            OfflinePlayer pl = Bukkit.getServer().getOfflinePlayer(args[1]);

            String playerCrew = crewManager.getPlayercrew(pl);
            JsonObject crewsection = crewsJson.getAsJsonObject(playerCrew);
            crewManager.getcrewInfo(crewsJson, crewsection, playerCrew, p, true);
        } else {
            chatUtil.CorrectUsage(p, getSyntax());
        }
    }
}
