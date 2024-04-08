package org.ovclub.crews.commands.subcommands.turfwar;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;

public class TurfWarStatsCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "View a crew's turf war stats.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews turfwar stats [crew]";
    }

    @Override
    public String getPermission() {
        return "crews.player.turfwar.stats";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {

    }
}
