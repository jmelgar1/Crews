package org.ovclub.crews.commands.subcommands.turfwar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.turfwar.TurfWarQueue;
import org.ovclub.crews.object.turfwar.TurfWarQueuePair;

import java.util.List;
import java.util.UUID;

public class TurfWarAcceptCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Accept a turf war matchup.";
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
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        TurfWarQueue queue = plugin.getTurfWarManager().getQueue();
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(!queue.isInQueue(pCrew)) {
            p.sendMessage(ConfigManager.NOT_IN_QUEUE);
            return;
        }
        p.sendMessage(ConfigManager.YOU_HAVE_CONFIRMED);
        plugin.getTurfWarManager().getWaitingForConfirmation(pCrew).remove(String.valueOf(p.getUniqueId()));
        TurfWarQueuePair pair = plugin.getTurfWarManager().findQueuePairForCrew(pCrew);
        for(String pUUID : pair.getParticipants()) {
            Player tPlayer = Bukkit.getPlayer(UUID.fromString(pUUID));
            if(tPlayer != null && tPlayer.isOnline()) {
                int total = pair.getParticipants().size();
                int accepted = total - plugin.getTurfWarManager().getWaitingForConfirmation(pCrew).size();
                tPlayer.sendMessage(ConfigManager.PLAYER_CONFIRMED
                    .replaceText(builder -> builder.matchLiteral("{accepted}").replacement(String.valueOf(accepted)))
                    .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(total))));
            }
        }

        if(plugin.getTurfWarManager().allConfirmedForTurfWar(pCrew)){
            plugin.getTurfWarManager().generateFairMatchup(pCrew);
        }
    }
}
