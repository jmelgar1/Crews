package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;

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
        if (args.length != 1) {
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
        String newName = args[0];
        if (pCrew.getName().equals(newName)) {
            p.sendMessage(ConfigManager.CHOOSE_DIFFERENT_NAME);
            return;
        }

        if (data.isValidCrewName(p, newName)) {
            pCrew.changeName(newName);
            pCrew.removeFromVault(ConfigManager.RENAME_COST, p, true);
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
