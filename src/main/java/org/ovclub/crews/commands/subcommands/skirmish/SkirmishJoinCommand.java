package org.ovclub.crews.commands.subcommands.skirmish;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.skirmish.SkirmishQueue;
import org.ovclub.crews.utilities.GUICreator;

public class SkirmishJoinCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Queue up for a skirmish.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews skirmish join";
    }

    @Override
    public String getPermission() {
        return "crews.player.skirmish.join";
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
        if(plugin.getSkirmishManager().getQueue().isInQueue(pCrew)) {
            p.sendMessage(ConfigManager.ALREADY_IN_QUEUE);
            return;
        }
        if(plugin.getArenaManager().getArenaByPlayer(p) != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_QUEUE);
            return;
        }
        GUICreator.createSkirmishQueueGUI(data, p, queue.size());
    }
}
