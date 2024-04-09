package org.ovclub.crews.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;

public class CreateCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Create a crew.";
    }

    @Override
    public String getSyntax() {
        return "/crews create [name]";
    }

    @Override
    public String getPermission() {
        return "crews.player.create";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (pCrew != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_CREW);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        String proposedCrewName = args[0];
        if (data.isValidCrewName(p, proposedCrewName)) {
            Crew newCrew = new Crew(args[0], p, plugin);
            data.addCrew(newCrew);
            Bukkit.broadcast(ConfigManager.CREW_FOUNDED.replaceText(builder -> builder.matchLiteral("{crew}").replacement(newCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
        }
    }
}
