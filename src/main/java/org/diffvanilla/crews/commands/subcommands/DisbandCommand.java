package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class DisbandCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Disband a crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews disband [crew]";
	}

    @Override
    public String getPermission() {
        return "crews.player.disband";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        String playerCrew = crewManager.getPlayercrew(p);
        if (!playerCrew.equals("none")) {
            if (args.length == 2) {
                String deletedcrew = args[1];
                if (crewManager.CheckForChief(playerCrew, p)) {
                    if (deletedcrew.equalsIgnoreCase(playerCrew)) {
                        String crewName = crewManager.getPlayercrew(p);
                        JsonObject crewsJson = crewsClass.getcrewsJson();
                        crewManager.sendMessageToMembers(crewName, ChatColor.RED.toString() + ChatColor.BOLD + "The crew has been deleted!");

                        crewsJson.remove(crewName);
                        crewsClass.savecrewsFileJson();
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You are not in that crew!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You are not the chief!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
