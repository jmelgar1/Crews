package org.diffvanilla.crews.commands.subcommands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

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
        String playerCrew = crewManager.getPlayercrew(p);
        if (!playerCrew.equals("none")) {
            if (args.length == 2) {
                OfflinePlayer kickedPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
                String kickedPlayercrew = crewManager.getPlayercrew(kickedPlayer);
                if (kickedPlayercrew != null && kickedPlayercrew.equals(playerCrew)) {
                    if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
                        if (!crewManager.CheckForChief(playerCrew, kickedPlayer)) {
                            List<String> members = crewManager.getcrewMembers(playerCrew);
                            members.remove(kickedPlayer.getUniqueId().toString());
                            crewManager.setcrewMembers(playerCrew, members);

                            if (crewManager.CheckForChief(playerCrew, p)) {
                                if (crewManager.CheckForElder(playerCrew, kickedPlayer)) {
                                    crewManager.removeElder(playerCrew);
                                }
                            }

                            Player player = kickedPlayer.getPlayer();
                            if (player != null && player.isOnline()) {
                                player.sendMessage(ChatColor.RED + "You have been kicked from " + kickedPlayercrew + "!");
                            }

                            crewManager.sendMessageToMembers(playerCrew, ChatColor.DARK_RED + "[\uD83D\uDC62] " + ChatColor.RED + kickedPlayer.getName() + " has been kicked from the crew!");
                            crewManager.generateScorePercrew(playerCrew);
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You can not kick the crew chief!");
                        }
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You must be a chief or an elder to use this command!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Player must be in your crew!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
