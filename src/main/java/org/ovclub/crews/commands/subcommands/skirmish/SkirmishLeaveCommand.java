package org.ovclub.crews.commands.subcommands.skirmish;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.skirmish.SkirmishQueue;

public class SkirmishLeaveCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Queue up for a skirmish.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews skirmish leave";
    }

    @Override
    public String getPermission() {
        return "crews.player.skirmish.leave";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        SkirmishQueue queue = plugin.getSkirmishManager().getQueue();
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(!queue.isInQueue(pCrew)) {
            p.sendMessage("not in queue");
            return;
        }
        queue.removeFromQueue(pCrew);
        p.sendMessage(ConfigManager.SUCCESS);
    }
}
