package org.diffvanilla.crews.commands.subcommands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.MailManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MailCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Send mail to all crew members";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews mail [message]";
	}

    @Override
    public String getPermission() {
        return "crews.player.mail";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        String playerCrew = crewManager.getPlayercrew(p);
        if (!playerCrew.equals("none")) {
            if(upgradeManager.checkForUpgrade(playerCrew, "mail")) {
                if (args.length >= 2) {
                    if(!Objects.equals(args[1], "open")) {
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        String dateString = currentDateTime.format(formatter);

                        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                        String mailMessage = "[" + dateString + "] " + p.getName() + ": " + message;

                        JsonObject crewsJson = crewsClass.getcrewsJson();
                        JsonObject crewObject = crewsJson.getAsJsonObject(playerCrew);

                        //add messages if it is null
                        if (crewObject.getAsJsonObject("mailMessages") == null) {
                            JsonObject mailMessages = new JsonObject();
                            crewObject.add("mailMessages", mailMessages);
                            crewsClass.savecrewsFileJson();
                        }

                        List<String> crewMembers = crewManager.getcrewMembers(playerCrew);
                        JsonObject mailMessages = crewObject.getAsJsonObject("mailMessages");

                        //remove sender from list of receivers
                        crewMembers.remove(p.getUniqueId().toString());

                        if (crewMembers.size() != 0) {
                            Gson gson = new Gson();
                            mailMessages.add(mailMessage, gson.toJsonTree(crewMembers).getAsJsonArray());
                            p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "Mail sent to " + crewMembers.size() + " crew members!");
                            crewsClass.savecrewsFileJson();
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You can not send mail to yourself!");
                        }
                    } else {
                        MailManager mailManager = new MailManager();
                        mailManager.opencrewMail(p);
                    }
                } else {
                    chatUtil.CorrectUsage(p, getSyntax());
                }
            } else {
                chatUtil.UpgradeNotUnlocked(p);
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }
}
