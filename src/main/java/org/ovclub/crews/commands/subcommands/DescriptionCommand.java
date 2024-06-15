package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.Arrays;

public class DescriptionCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Set your crew description.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c description [new desc]";
	}

    @Override
    public String getPermission() {
        return "crews.player.description";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length < 1) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
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
        String description = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        pCrew.setDescription(description);
        p.sendMessage(ConfigManager.DESCRIPTION_SET);
    }
}
