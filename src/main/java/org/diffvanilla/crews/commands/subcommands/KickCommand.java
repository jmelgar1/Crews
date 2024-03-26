package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        Player kPlayer = Bukkit.getPlayer(args[1]);
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(kPlayer == null) {
            p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            return;
        }
        if(!data.getCrew(kPlayer).equals(pCrew)){
            p.sendMessage(ConfigManager.PLAYER_NOT_IN_SAME_CREW);
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
        }

        pCrew.removePlayer(kPlayer.getUniqueId(), true);
    }
}
