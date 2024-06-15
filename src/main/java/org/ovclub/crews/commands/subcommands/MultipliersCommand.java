package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.hightable.VoteItem;
import org.ovclub.crews.utilities.GUI.GUICreator;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.ArrayList;

public class MultipliersCommand implements SubCommand {

	@Override
	public String getDescription() {
		return "View daily multipliers.";
	}

	@Override
	public String getSyntax() {
		return "/c multipliers";
	}

    @Override
    public String getPermission() {
        return "crews.player.multipliers";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        if (args.length != 0) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        ArrayList<VoteItem> activeMultipliers = data.getActiveMultipliers();
        GUICreator.createActiveMultipliers(activeMultipliers, data, p);
    }
}
