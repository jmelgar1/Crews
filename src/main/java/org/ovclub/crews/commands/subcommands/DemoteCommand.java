package org.ovclub.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class DemoteCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Demote a player from elder status.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews demote [player]";
	}

    @Override
    public String getPermission() {
        return "crews.player.demote";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        Player demotedPlayer = Bukkit.getServer().getPlayer(args[0]);
        if (demotedPlayer == null) {
            p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            return;
        }
        if (!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        if (!data.getCrew(demotedPlayer).equals(pCrew)) {
            p.sendMessage(ConfigManager.PLAYER_NOT_IN_SAME_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
            return;
        }
        if (!pCrew.isEnforcer(demotedPlayer)) {
            p.sendMessage(ConfigManager.PLAYER_NOT_ENFORCER);
            return;
        }
        if (p.equals(demotedPlayer)) {
            p.sendMessage(ConfigManager.CAN_NOT_DEMOTE_SELF);
            return;
        }
        pCrew.removeEnforcer(demotedPlayer.getUniqueId());
        //MORE MESSAGES?
        //add demote functionality
    }
}
