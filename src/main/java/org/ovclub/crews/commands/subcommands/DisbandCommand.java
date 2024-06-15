package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class DisbandCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Disband a crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c disband [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.disband";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        String targetCrew = args[0];
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
