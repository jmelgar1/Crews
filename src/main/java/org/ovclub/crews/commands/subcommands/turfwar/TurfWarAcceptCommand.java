package org.ovclub.crews.commands.subcommands.turfwar;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;

public class TurfWarAcceptCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Accept a turfwar match-up.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews turfwar accept";
    }

    @Override
    public String getPermission() {
        return "crews.player.turfwar.accept";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {

    }
}
