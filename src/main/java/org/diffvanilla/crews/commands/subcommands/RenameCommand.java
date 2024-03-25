package org.diffvanilla.crews.commands.subcommands;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class RenameCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Rename your crew.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews rename [new name]";
	}

    @Override
    public String getPermission() {
        return "crews.player.rename";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        String oldcrewName = crewManager.getPlayercrew(p);
        if(!oldcrewName.equals("none")) {

            int priceToRename = crewsClass.getPrices().getInt("changename");
            JsonObject crewsJson = crewsClass.getcrewsJson();

            if (args.length == 2) {

                String newcrewNameDatabase = args[1].toLowerCase();
                String newcrewNameShowName = args[1];

                if (newcrewNameDatabase.matches("[a-zA-Z]+")) {
                    if (newcrewNameDatabase.length() <= 16 && newcrewNameDatabase.length() >= 4) {
                        if (crewManager.CheckForChief(oldcrewName, p)) {
                            if (!oldcrewName.equals(newcrewNameDatabase)) {
                                if (isNameAvailable(crewsJson, newcrewNameDatabase)) {
                                    int vault = crewManager.getVault(oldcrewName);
                                    if (vault >= priceToRename) {
                                        crewManager.removeFromVault(oldcrewName, priceToRename, p);
                                        copyConfigSection(crewsJson, oldcrewName, newcrewNameDatabase, newcrewNameShowName);
                                        p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "crew renamed!");

                                        crewsClass.savecrewsFileJson();
                                    } else {
                                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You need at least " + priceToRename + " sponges to in the crew vault change the crew name!");
                                    }
                                } else {
                                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "This name is already taken.");
                                }
                            } else {
                                p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Please choose a different name.");
                            }
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You are not crew chief.");
                        }
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "crew names must be between 4 and 16 characters long!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "crew names must only contain upper or lower case letters!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }

	private boolean isNameAvailable(JsonObject jsonObject, String newName){
		JsonObject destinationSection = jsonObject.getAsJsonObject(newName.toLowerCase());
		return destinationSection == null;
	}

	public static void copyConfigSection(JsonObject jsonObject, String oldName, String newName, String newShowName) {
		JsonObject sourceSection = jsonObject.getAsJsonObject(oldName.toLowerCase());

		//null check if crew exists
		if (sourceSection == null) {
			return;
		}

		JsonObject destinationSection = new JsonObject();
		jsonObject.add(newName, destinationSection);

		for (Map.Entry<String, JsonElement> entry : sourceSection.entrySet()) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();

			if (value.isJsonArray()) {
				JsonArray jsonArray = value.getAsJsonArray();
				JsonArray newArray = new JsonArray();
				for (JsonElement element : jsonArray) {
					newArray.add(element);
				}
				destinationSection.add(key, newArray);
			} else {
				destinationSection.add(key, value);
			}

			destinationSection.addProperty("showname", newShowName);

			jsonObject.remove(oldName);
		}
	}
}
