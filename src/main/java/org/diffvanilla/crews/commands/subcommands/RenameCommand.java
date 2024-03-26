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
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        String newName = args[1];
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (pCrew.getVault() < ConfigManager.RENAME_COST) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        if (!pCrew.isBoss(p)) {
            p.sendMessage(ConfigManager.MUST_BE_BOSS);
            return;
        }
        if (pCrew.getName().equals(newName)) {
            p.sendMessage(ConfigManager.CHOOSE_DIFFERENT_NAME);
            return;
        }

        if (data.isValidCrewName(p, newName)) {
            pCrew.changeName(newName);
            pCrew.removeFromVault(ConfigManager.RENAME_COST, p);
        }
    }

//	private boolean isNameAvailable(JsonObject jsonObject, String newName){
//		JsonObject destinationSection = jsonObject.getAsJsonObject(newName.toLowerCase());
//		return destinationSection == null;
//	}
//
//	public static void copyConfigSection(JsonObject jsonObject, String oldName, String newName, String newShowName) {
//		JsonObject sourceSection = jsonObject.getAsJsonObject(oldName.toLowerCase());
//
//		//null check if crew exists
//		if (sourceSection == null) {
//			return;
//		}
//
//		JsonObject destinationSection = new JsonObject();
//		jsonObject.add(newName, destinationSection);
//
//		for (Map.Entry<String, JsonElement> entry : sourceSection.entrySet()) {
//			String key = entry.getKey();
//			JsonElement value = entry.getValue();
//
//			if (value.isJsonArray()) {
//				JsonArray jsonArray = value.getAsJsonArray();
//				JsonArray newArray = new JsonArray();
//				for (JsonElement element : jsonArray) {
//					newArray.add(element);
//				}
//				destinationSection.add(key, newArray);
//			} else {
//				destinationSection.add(key, value);
//			}
//
//			destinationSection.addProperty("showname", newShowName);
//
//			jsonObject.remove(oldName);
//		}
//	}
}
