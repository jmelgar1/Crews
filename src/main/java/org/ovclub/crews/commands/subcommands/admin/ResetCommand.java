package org.ovclub.crews.commands.subcommands.admin;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.HightableUtility;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class ResetCommand implements SubCommand {
	@Override
	public String getDescription() {
		return "Reset hightable things.";
	}

    @Override
    public String getSyntax() {
        return "/c reset";
    }

    @Override
    public String getPermission() {
        return "crews.admin.reset";
    }

    @Override
	public void perform(Player p, String[] args, Crews plugin) {
        if (args.length != 0) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        HightableUtility.executeMidnightTasks(plugin);
    }
}
