package org.ovclub.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;

public class OnwershipCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Transfer crew chief ownership.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews ownership [player]";
	}

    @Override
    public String getPermission() {
        return "crews.player.ownership";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        Player tPlayer = Bukkit.getPlayer(args[0]);
        if(tPlayer == null) {
            p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            return;
        }
        if(!data.getCrew(tPlayer).equals(pCrew)) {
            p.sendMessage(ConfigManager.PLAYER_NOT_IN_SAME_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
            return;
        }
        if(!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        pCrew.replaceBoss(tPlayer.getUniqueId());
    }
}
