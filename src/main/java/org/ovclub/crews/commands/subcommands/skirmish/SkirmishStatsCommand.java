package org.ovclub.crews.commands.subcommands.skirmish;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;

public class SkirmishStatsCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "View a crew's skirmish stats.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews skirmish stats [crew]";
    }

    @Override
    public String getPermission() {
        return "crews.player.skirmish.stats";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {

    }
}
