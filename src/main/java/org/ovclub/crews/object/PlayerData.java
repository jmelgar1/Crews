package org.ovclub.crews.object;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.utilities.GeneralUtilities;

import java.util.*;

public class PlayerData {
    public PlayerData(){
        crews = new ArrayList<>();
        cPlayers = new HashMap<>();
        invites = new HashMap<>();
        pendingTeleports = new HashMap<>();
        inCrewInfo = new ArrayList<>();
        inCrewChat = new ArrayList<>();
        inventories = new HashMap<>();
        selectedForQueue = new HashMap<>();

        // In case of server /reload
        for(Player p : Bukkit.getOnlinePlayers()){
            for (Crew c : getCrews()) {
                if (c.hasMember(p)) addCPlayer(p, c); break;
            }
        }
    }

        /*
     Inventories for events
     */
    private final HashMap<UUID, Inventory> inventories;
    public HashMap<UUID, Inventory> getInventories(){
        return inventories;
    }

    private final HashMap<Crew, ArrayList<String>> selectedForQueue;
    public HashMap<Crew, ArrayList<String>> getSelectedForQueue(){
        return selectedForQueue;
    }
    public void addToSelectedForQueue(Crew pCrew, String pUUID){
        if (selectedForQueue.containsKey(pCrew)) {
            selectedForQueue.get(pCrew).add(pUUID);
        } else {
            ArrayList<String> uuidList = new ArrayList<>();
            uuidList.add(pUUID);
            selectedForQueue.put(pCrew, uuidList);
        }
    }

    public void removeFromSelectedForQueue(Crew pCrew, String pUUID) {
        if (selectedForQueue.containsKey(pCrew)) {
            ArrayList<String> uuidList = selectedForQueue.get(pCrew);
            uuidList.remove(pUUID);
            if (uuidList.isEmpty()) {
                selectedForQueue.remove(pCrew);
            }
        } else {
            System.out.println("No such crew in the queue or player was not in the queue.");
        }
    }

    /*
    Pending teleports
    */
    private final List<UUID> inCrewChat;
    public void enableCrewChat(UUID pUUID){
        inCrewChat.add(pUUID);
    }
    public void disableCrewChat(UUID pUUID){
        inCrewChat.remove(pUUID);
    }
    public boolean isInCrewChat(UUID pUUID){
        return inCrewChat.contains(pUUID);
    }

    /*
   Pending teleports
    */
    private final HashMap<Player, Integer> pendingTeleports;
    public HashMap<Player, Integer> getPendingTeleports(){
        return pendingTeleports;
    }

    /*
   Looking at Crew Info Hologram
    */
    private final ArrayList<Player> inCrewInfo;
    public ArrayList<Player> getInCrewInfo(){
        return inCrewInfo;
    }

    /*
   Loaded crews
    */
    private final ArrayList<Crew> crews;
    public ArrayList<Crew> getCrews(){
        return crews;
    }

    public void addCrew(Crew s){
        crews.add(s);
    }

    public void removeCrew(Crew s){
        crews.remove(s);
        invites.values().remove(s);
    }

    public Crew getCrew(String name){
        for(Crew s : crews){
            if(s.getName().equalsIgnoreCase(name)) return s;
        }
        for(Player p : cPlayers.keySet()){
            if(p.getName().equalsIgnoreCase(name)) return getCrew(p);
        }
        return null;
    }

    public Crew getCrew(Player p){
        return cPlayers.get(p);
    }

    public Crew getCrew(OfflinePlayer p){
        if(p.isOnline()) return getCrew((Player) p);
        String id = String.valueOf(p.getUniqueId());
        for(Crew c : getCrews()){
            if(c.getMembers().contains(id) || c.getBoss().equals(id) || c.getEnforcers().contains(id)) return c;
        }
        return null;
    }
    public Crew getCrewOrError(Player p) throws NotInCrew {
        if(!cPlayers.containsKey(p)){
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            throw new NotInCrew();
        }
        return cPlayers.get(p);
    }
    public Crew getCrewRawName(String crewName){
        for(Crew c : crews){
            if(c.getName().equalsIgnoreCase(crewName)) return c;
        }
        return null;
    }
    /*
       Online players & crew association
        */
    private final HashMap<Player, Crew> cPlayers;
    public void addCPlayer(Player p, Crew s){
        cPlayers.put(p, s);
    }
    public void removeCPlayer(Player p){
        cPlayers.remove(p);
    }
    public boolean isPlayerInCrew(Player p){
        return cPlayers.containsKey(p);
    }

    /*
    Online players with active invitations
     */
    private final HashMap<Player, Crew> invites;
    public boolean hasInvitation(Player p){
        return invites.containsKey(p);
    }
    public void addInvitation(Player p, Crew s) {invites.put(p, s);}
    public void expireInvitation(Player p) {
        if(invites.get(p) != null) {
            p.sendMessage(Component.text("Crew invite expired!").color(TextColor.color(0xF44336)));
            invites.remove(p);
        }
    }

    public void handleInvite(Player p, Crew s, boolean accept) {
        if (invites.containsKey(p)) {
            Crew crew = invites.get(p);
            if (crew.equals(s)) {
                if (accept) {
                    if (crew.getMembers().size() >= ConfigManager.MAX_MEMBERS) {
                        crew.addPlayer(p);
                    }
                }
                invites.remove(p);
            } else {
                p.sendMessage(Component.text("You do not have an invitation from this crew!"));
            }
        }
    }

    /* Crew Naming */
    public boolean isValidCrewName(Player p, String crewName) {
        if (!crewName.matches("[a-zA-Z]+")) {
            p.sendMessage(ConfigManager.CREW_NAME_ONLY_ALPHABETICAL);
            return false;
        }
        if (crewName.length() > 16 || crewName.length() < 4) {
            p.sendMessage(ConfigManager.CREW_NAME_BETWEEN_4_AND_16);
            return false;
        }
        if(getCrewRawName(crewName) != null) {
            p.sendMessage(ConfigManager.NAME_TAKEN);
            return false;
        }
        return true;
    }

    public void calculateInfluenceForAllCrews() {
        for(Crew crew : crews) {
            calculateInfluence(crew);
        }
    }

    public void calculateInfluence(Crew crew) {
        int vault = crew.getVault();
        int totalPlayers = crew.getMembers().size() + crew.getEnforcers().size() + 1;
        int playerPower = totalPlayers * ConfigManager.INFLUENCE_PER_PLAYER;
        int rating = crew.getRating();
        int influence = vault + playerPower + rating;
        crew.setInfluence(influence);
    }


    /* Crew Influence */
//    public void generateEconomy() {
//        for(Crew crew : crews) {
//            int totalSponges;
////            if(crewObject.get("totalSponges") == null){
////                crewObject.addProperty("totalSponges", 0);
////            }
////
////            totalSponges = crewObject.get("totalSponges").getAsInt();
//
//            crewObject.addProperty("economyScore", (double)totalSponges);
//            crewsClass.savecrewsFileJson();
//        }
//    }

    public Map<String, Integer> generateLeaderboardJson() {
        HashMap<String, Integer> crewAndScore = new HashMap<>();
        for(Crew crew : crews) {
            int influence = crew.getInfluence();
            crewAndScore.put(crew.getName(), influence);
        }
        return GeneralUtilities.sortByComparator(crewAndScore, false);
    }

    public void broadcastToAllOnlineCrewMembers(final Component text) {
        if(text == null) return;
        for(Crew crew: crews) {
            for (String member : crew.getMembers()) {
                UUID pUUID = UUID.fromString(member);
                if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                    Player p = Bukkit.getPlayer(pUUID);
                    if(p != null){p.sendMessage(text);}
                }
            }
            for (String enforcer : crew.getEnforcers()) {
                UUID pUUID = UUID.fromString(enforcer);
                if (Bukkit.getOfflinePlayer(pUUID).isOnline()) {
                    Player p = Bukkit.getPlayer(pUUID);
                    if(p != null){p.sendMessage(text);}
                }
            }
            if (Bukkit.getOfflinePlayer(UUID.fromString(crew.getBoss())).isOnline()) {
                Player p = Bukkit.getPlayer(UUID.fromString(crew.getBoss()));
                if (p != null && p.isOnline()) {
                    p.sendMessage(text);
                }
            }
        }
    }
}
