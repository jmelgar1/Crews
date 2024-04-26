//package org.ovclub.crews.managers;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.ovclub.crews.Crews;
//import org.ovclub.crews.file.CrewsFile;
//import org.ovclub.crews.object.Crew;
//import org.ovclub.crews.object.PlayerData;
//import org.ovclub.crews.utilities.ChatUtilities;
//import org.ovclub.crews.utilities.JsonUtilities;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MailManager {
//
//    ChatUtilities chatUtil = new ChatUtilities();
//    private final Crews plugin;
//
//    public MailManager(Crews plugin) {
//        this.plugin = plugin;
//    }
//
//    public void openCrewMail(Player p){
//        UUID uuid = p.getUniqueId();
//
//        PlayerData data = plugin.getData();
//        Crew pCrew = data.getCrew(p);
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        //JsonObject crewsData = crewsFile.getCrewsData();
//
//        JsonObject crewsObject = crewsData.getAsJsonObject(pCrew.getName());
//
//        if(crewsObject.getAsJsonObject("mailMessages") == null){
//            JsonObject mailMessages = new JsonObject();
//            crewsObject.add("mailMessages", mailMessages);
//            crewsFile.saveCrews();
//        }
//
//        JsonObject mailMessages = crewsObject.getAsJsonObject("mailMessages");
//        if(!mailMessages.keySet().isEmpty()){
//            JsonUtilities json = new JsonUtilities();
//            Gson gson = new Gson();
//            // Convert the key set to a List
//            List<String> messageKeys = new ArrayList<>(mailMessages.keySet());
//
//            // Iterate over the list in reverse order
//            for (int i = messageKeys.size() - 1; i >= 0; i--) {
//                String message = messageKeys.get(i);
//                List<String> membersUUIDList = json.JsonArrayToStringList(mailMessages.get(message).getAsJsonArray());
//
//                if (membersUUIDList.contains(uuid.toString())) {
//                    p.sendMessage(ChatColor.GRAY + message);
//                    membersUUIDList.remove(uuid.toString());
//
//                    if (membersUUIDList.isEmpty()) {
//                        mailMessages.remove(message);
//                        crewsFile.saveCrews();
//                    } else {
//                        mailMessages.add(message, gson.toJsonTree(membersUUIDList).getAsJsonArray());
//                        crewsFile.saveCrews();
//                    }
//                }
//            }
//        } else {
//            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You have no mail to open!");
//        }
//    }
//}
