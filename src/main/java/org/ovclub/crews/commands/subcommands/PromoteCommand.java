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

public class PromoteCommand implements SubCommand {
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Promote a player to elder status.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews promote [player]";
	}

    @Override
    public String getPermission() {
        return "crews.player.promote";
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
        Crew tCrew = data.getCrew(tPlayer);
        if(tPlayer == null) {
            p.sendMessage(ConfigManager.PLAYER_NOT_FOUND);
            return;
        }
        if(!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        if(!pCrew.equals(tCrew)) {
            p.sendMessage(ConfigManager.PLAYER_NOT_IN_SAME_CREW.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
            return;
        }
        if(pCrew.isEnforcer(tPlayer)) {
            p.sendMessage(ConfigManager.ALREADY_ENFORCER
                .replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
            return;
        }
        if(pCrew.getEnforcers().size() >= pCrew.getEnforcerLimit()) {
            if(pCrew.isMaxLevel()) {
                p.sendMessage(ConfigManager.MAX_LEVEL_ENFORCER_LIMIT);
            } else {
                p.sendMessage(ConfigManager.ENFORCER_LIMIT
                    .replaceText(builder -> builder.matchLiteral("{limit}").replacement(String.valueOf(pCrew.getEnforcerLimit()))));
            }
            return;
        }
        if(tPlayer.equals(p)) {
            p.sendMessage(ConfigManager.CAN_NOT_PROMOTE_SELF);
            return;
        }
        pCrew.addEnforcer(tPlayer.getUniqueId());
    }
}
