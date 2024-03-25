package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (playerCrew.getVault() < ConfigManager.WARP_PRICE) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_SPONGE);
            return;
        }
        if (!playerCrew.hasCompound()) {
            p.sendMessage(ConfigManager.CREW_NO_COMPOUND);
        }
        if (plugin.getData().getPendingTeleports().containsKey(p)) {
            p.sendMessage(ConfigManager.PLEASE_WAIT_TELEPORT);
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
