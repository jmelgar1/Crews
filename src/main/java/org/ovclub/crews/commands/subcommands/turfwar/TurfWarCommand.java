package org.ovclub.crews.commands.subcommands.turfwar;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;

public class TurfWarCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "TurfWar Commands.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews turfwar";
	}

    @Override
    public String getPermission() {
        return "crews.player.turfwar";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
//        if (args.length == 0) {
//            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
//            return;
//        }
        //args
        //turfwar - returns turfwar commands and a short intro
        //queue - shows the player a gui with queue info and a queue up button
        //queue up button shows the player a gui of all online crew members
        //player selects the player heads and selected players heads turn green
        //when player selects QUEUE UP it puts the new QueueItem model into thq queue
        //QueueItem = {Crew, rating, playerCount, timeInCrew}
        //ALSO SWITCH VOTING SITES SOMEITMES NEW SERVERS GET A BUMP DO THIS ONCE ITS FINISHED

        //FIX CROSSPLAY
        //FIX ANTI-CHEAT
    }
}
