package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

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
        String playerCrew = crewManager.getPlayercrew(p);
        if(!playerCrew.equals("none")) {
            if (args.length == 2) {
                Player newChief = Bukkit.getServer().getPlayer(args[1]);
                if (newChief != null) {
                    String targetPlayer = crewManager.getPlayercrew(newChief);
                    if (crewManager.CheckForChief(playerCrew, p)) {
                        if (crewManager.CheckSamecrew(playerCrew, targetPlayer)) {
                            if (!newChief.getName().equals(p.getName())) {
                                crewManager.setChief(playerCrew, newChief);
                                crewManager.sendMessageToMembers(playerCrew, ChatColor.GREEN + p.getName()
                                    + " has tranferred crew ownership to "
                                    + newChief.getName() + "!");
                                if (crewManager.CheckForElder(playerCrew, newChief)) {
                                    crewManager.removeElder(playerCrew);
                                    crewManager.setElder(playerCrew, p);
                                }
                            } else {
                                p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You are already the chief!");
                            }
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + newChief.getName() + " is not in your crew!");
                        }
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You must be a chief to use this command!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + args[1] + " not found!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
