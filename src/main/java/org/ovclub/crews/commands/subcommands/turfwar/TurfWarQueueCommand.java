package org.ovclub.crews.commands.subcommands.turfwar;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.turfwar.TurfWarQueue;
import org.ovclub.crews.utilities.GUICreator;

public class TurfWarQueueCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Queue up for a turf war.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews turfwar queue";
    }

    @Override
    public String getPermission() {
        return "crews.player.turfwar.queue";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        TurfWarQueue queue = plugin.getTurfWarManager().getQueue();
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(plugin.getTurfWarManager().getQueue().isInQueue(pCrew)) {
            p.sendMessage(ConfigManager.ALREADY_IN_QUEUE);
            return;
        }
        GUICreator.createTurfWarQueueGUI(data, p, queue.size());
    }
}
