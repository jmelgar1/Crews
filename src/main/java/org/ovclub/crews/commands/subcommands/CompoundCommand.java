package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class CompoundCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Teleport to your crew's compound.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews compound";
	}

    @Override
    public String getPermission() {
        return "crews.player.compound";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        Crew playerCrew = plugin.getData().getCrew(p);
        if (playerCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (args.length != 0) {
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
        if (playerCrew.getVault() < ConfigManager.WARP_COST) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        if (!playerCrew.hasCompound()) {
            p.sendMessage(ConfigManager.CREW_NO_COMPOUND);
            return;
        }
        if (plugin.getData().getPendingTeleports().containsKey(p)) {
            p.sendMessage(ConfigManager.PLEASE_WAIT_TELEPORT);
            return;
        }
        int teleportRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                playerCrew.tpCompound(p);
                plugin.getData().getPendingTeleports().remove(p);
            }
        }.runTaskLater(plugin, ConfigManager.TELEPORT_DELAY).getTaskId();
        plugin.getData().getPendingTeleports().put(p, teleportRunnable);
    }
}
