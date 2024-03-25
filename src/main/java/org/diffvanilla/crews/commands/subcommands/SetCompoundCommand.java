package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class SetCompoundCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Set the crew compound.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews setcompound";
	}

    @Override
    public String getPermission() {
        return "crews.player.setcompound";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        String playerCrew = crewManager.getPlayercrew(p);
        if (!playerCrew.equals("none")) {
            int priceToSetCompound = crewsClass.getPrices().getInt("setwarp");
            if (args.length == 1) {
                if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
                    if (!warpManager.compoundExists(playerCrew)) {
                        int vault = crewManager.getVault(playerCrew);
                        if (vault >= priceToSetCompound) {
                            crewManager.removeFromVault(playerCrew, priceToSetCompound, p);
                            warpManager.setCompound(playerCrew, p);
                            crewManager.generateScorePercrew(playerCrew);
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You need at least " + priceToSetCompound + " sponges in the crew vault to set the crew compound!");
                        }
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "The crew's compound is already set!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Only chiefs and elders can set the compound!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
