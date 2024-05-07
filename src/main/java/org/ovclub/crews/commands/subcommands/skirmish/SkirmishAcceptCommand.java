package org.ovclub.crews.commands.subcommands.skirmish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.skirmish.SkirmishQueue;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;

import java.util.UUID;

public class SkirmishAcceptCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Accept a skirmish matchup.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews skirmish accept";
    }

    @Override
    public String getPermission() {
        return "crews.player.skirmish.accept";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        SkirmishQueue queue = plugin.getSkirmishManager().getQueue();
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        SkirmishMatchup pair = plugin.getSkirmishManager().getMatchupFromCrew(pCrew);
        if (!queue.isInQueue(pCrew) || pair == null) {
            p.sendMessage(ConfigManager.NOT_IN_QUEUE);
            return;
        }
        if (pair.getPlayersInConfirmation().contains(String.valueOf(p.getUniqueId()))) {
            p.sendMessage(ConfigManager.YOU_HAVE_CONFIRMED);
            pair.getPlayersInConfirmation().remove(String.valueOf(p.getUniqueId()));
            for (String pUUID : pair.getParticipants()) {
                Player tPlayer = Bukkit.getPlayer(UUID.fromString(pUUID));
                if (tPlayer != null && tPlayer.isOnline()) {
                    int total = pair.getParticipants().size();
                    int accepted = total - pair.getPlayersInConfirmation().size();
                    tPlayer.sendMessage(ConfigManager.PLAYER_CONFIRMED
                        .replaceText(builder -> builder.matchLiteral("{accepted}").replacement(String.valueOf(accepted)))
                        .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(total))));
                }
            }

            if (plugin.getSkirmishManager().allConfirmedForSkirmish(pCrew)) {
                plugin.getSkirmishManager().generateFairMatchup(pCrew);
            }
        }
    }
}
