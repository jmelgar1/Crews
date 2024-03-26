package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        Player tPlayer = Bukkit.getPlayer(args[1]);
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(tPlayer == null) {
            p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            return;
        }
        if(!data.getCrew(tPlayer).equals(pCrew)) {
            p.sendMessage(ConfigManager.PLAYER_NOT_IN_SAME_CREW);
            return;
        }
        if(!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        pCrew.replaceBoss(tPlayer.getUniqueId());
    }
}
