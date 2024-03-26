package org.diffvanilla.crews.managers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.JsonUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailManager {

    CrewManager crewManager = new CrewManager();
    ChatUtilities chatUtil = new ChatUtilities();

    public void opencrewMail(Player p){
        UUID uuid = p.getUniqueId();

        String playerCrew = crewManager.getPlayercrew(p);
        JsonObject crewsJson = crewsClass.getcrewsJson();
        JsonObject crewsObject = crewsJson.getAsJsonObject(playerCrew);

        if(crewsObject.getAsJsonObject("mailMessages") == null){
            JsonObject mailMessages = new JsonObject();
            crewsObject.add("mailMessages", mailMessages);
            crewsClass.savecrewsFileJson();
        }

        JsonObject mailMessages = crewsObject.getAsJsonObject("mailMessages");
        if(!mailMessages.keySet().isEmpty()){
            JsonUtilities json = new JsonUtilities();
            Gson gson = new Gson();
            // Convert the key set to a List
            List<String> messageKeys = new ArrayList<>(mailMessages.keySet());

            // Iterate over the list in reverse order
            for (int i = messageKeys.size() - 1; i >= 0; i--) {
                String message = messageKeys.get(i);
                List<String> membersUUIDList = json.JsonArrayToStringList(mailMessages.get(message).getAsJsonArray());

                if (membersUUIDList.contains(uuid.toString())) {
                    p.sendMessage(ChatColor.GRAY + message);
                    membersUUIDList.remove(uuid.toString());

                    if (membersUUIDList.isEmpty()) {
                        mailMessages.remove(message);
                        crewsClass.savecrewsFileJson();
                    } else {
                        mailMessages.add(message, gson.toJsonTree(membersUUIDList).getAsJsonArray());
                        crewsClass.savecrewsFileJson();
                    }
                }
            }
        } else {
            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You have no mail to open!");
        }
    }
}
