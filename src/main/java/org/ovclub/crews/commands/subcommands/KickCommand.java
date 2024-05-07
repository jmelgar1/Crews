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

public class KickCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Kick a player from your crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews kick [player]";
	}

    @Override
    public String getPermission() {
        return "crews.player.kick";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        Player kPlayer = Bukkit.getPlayer(args[0]);
        if(kPlayer == null) {
            p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            return;
        }
        if(kPlayer.equals(p)){
            p.sendMessage(ConfigManager.CANT_KICK_SELF);
            return;
        }
        if(!data.getCrew(kPlayer).equals(pCrew)){
            p.sendMessage(ConfigManager.PLAYER_NOT_IN_SAME_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
            return;
        }
        if(!pCrew.isHigherup(p)) {
            p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
            return;
        }
        if(pCrew.isBoss(kPlayer)) {
            p.sendMessage(ConfigManager.CANT_KICK_BOSS);
            return;
        }
        if(!pCrew.isBoss(p) && pCrew.isEnforcer(kPlayer)) {
            p.sendMessage(ConfigManager.CANT_KICK_SAME_RANK);
            return;
        }

        pCrew.removePlayer(kPlayer.getUniqueId(), true);
    }
}
