package org.diffvanilla.crews.commands.subcommands;

import java.util.List;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class LeaveCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Leave a crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews leave [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.leave";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        String playerCrew = crewManager.getPlayercrew(p);
        if (!playerCrew.equals("none")) {
            if (args.length == 2) {
                String leftcrew = args[1];
                if (!crewManager.CheckForChief(playerCrew, p)) {
                    if (leftcrew.equalsIgnoreCase(playerCrew)) {

                        List<String> members = crewManager.getcrewMembers(playerCrew);
                        members.remove(p.getUniqueId().toString());
                        crewManager.setcrewMembers(playerCrew, members);

                        crewManager.generateScorePercrew(playerCrew);

                        if (crewManager.CheckForElder(playerCrew, p)) {
                            crewManager.removeElder(playerCrew);
                        }

                        p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "You have left " + playerCrew + "!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Please use /crews delete");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
