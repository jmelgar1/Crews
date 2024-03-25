package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

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
        String playerCrew = crewManager.getPlayercrew(p);
        if (!playerCrew.equals("none")) {
            if (args.length == 2) {
                Player promotedPlayer = Bukkit.getServer().getPlayer(args[1]);
                if (promotedPlayer != null) {
                    if (crewManager.CheckForChief(playerCrew, p)) {
                        String promotedPlayercrew = crewManager.getPlayercrew(promotedPlayer);
                        if (crewManager.getElder(playerCrew).equals("")) {
                            if (playerCrew.equals(promotedPlayercrew)) {
                                if (!promotedPlayer.getName().equals(p.getName())) {
                                    crewManager.setElder(playerCrew, promotedPlayer);
                                    crewManager.sendMessageToMembers(playerCrew,
                                        ChatColor.DARK_GREEN + "[⏫] " + ChatColor.GREEN + promotedPlayer.getName() + " has been promoted to elder!");
                                } else {
                                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You can not promote yourself!");
                                }
                            } else {
                                p.sendMessage(chatUtil.errorIcon + ChatColor.RED + promotedPlayer.getName() + " is not in your crew!");
                            }
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "There can only be 1 other elder!");
                        }
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You need to be a chief to promote players!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Player " + args[1] + " not found!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
