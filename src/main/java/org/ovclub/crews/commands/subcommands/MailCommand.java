package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.managers.MailManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class MailCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Send mail to all crew members";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews mail send/open [message]";
	}

    @Override
    public String getPermission() {
        return "crews.player.mail";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length < 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(!pCrew.getUnlockedUpgrades().contains("mail")) {
            p.sendMessage(ConfigManager.UPGRADE_NOT_UNLOCKED.replaceText(builder -> builder.matchLiteral("{upgrade}").replacement("mail")));
            return;
        }
        if(args[1].equalsIgnoreCase("open")) {
            MailManager mailManager = new MailManager(plugin);
            mailManager.openCrewMail(p);
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String dateString = currentDateTime.format(formatter);

            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            String mailMessage = "[" + dateString + "] " + p.getName() + ": " + message;

            List<String> crewMembers = pCrew.getMembers();
            crewMembers.remove(p.getUniqueId().toString());
            pCrew.addToMail(mailMessage);
        }
    }
}
