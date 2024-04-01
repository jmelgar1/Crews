package org.diffvanilla.crews.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.file.CrewsFile;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.JsonUtilities;
import org.diffvanilla.crews.utilities.UnicodeCharacters;

public class CrewManager {
	private final UnicodeCharacters unicode = new UnicodeCharacters();
	private final ChatUtilities chatUtil = new ChatUtilities();
	private final JsonUtilities json = new JsonUtilities();
    private final Crews plugin;
    public CrewManager(final Crews plugin){
        this.plugin = plugin;
    }
	
	public String getPlayercrew(Player p) {
		CrewsFile crewsFile = plugin.getCrewsFile();
        JsonObject crewsData = crewsFile.getCrewsData();
		String playerUUID = p.getUniqueId().toString();
		
		for(String crew : crewsData.keySet()) {
			JsonObject crewObject = crewsData.getAsJsonObject(crew);
			JsonArray memberArray = crewObject.getAsJsonArray("members");
			if(json.JsonArrayToStringList(memberArray).contains(playerUUID)) {
				return crew;
			}
		}
		return "none";
	}

//	public String getPlayercrew(OfflinePlayer p) {
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		String playerUUID = p.getUniqueId().toString();
//
//		for(String crew : crewsData.keySet()) {
//			JsonObject crewObject = crewsData.getAsJsonObject(crew);
//			JsonArray memberArray = crewObject.getAsJsonArray("members");
//			if(json.JsonArrayToStringList(memberArray).contains(playerUUID)) {
//				return crew;
//			}
//		}
//		return "none";
//	}
	
//	public void sendMessageToMembers(String crew, String message) {
//		 List<String> members = getcrewMembers(crew);
//
//		 for(String member : members) {
//			 UUID playerUUID = UUID.fromString(member);
//			 Player p = Bukkit.getServer().getPlayer(playerUUID);
//			 if(p != null) {
//				 p.sendMessage(message);
//			 }
//		 }
//	}
	
//	public List<String> getcrewMembers(String crew){
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		JsonArray crewMembers = crewObject.getAsJsonArray("members");
//
//		return json.JsonArrayToStringList(crewMembers);
//	}
//
//	public void setcrewMembers(String crew, List<String> updatedMemberList) {
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//
//		Gson gson = new Gson();
//		crewObject.add("members", gson.toJsonTree(updatedMemberList).getAsJsonArray());
//		crewsFile.saveCrews();
//	}
//
//	public String getcrewshowName(String crew) {
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("showname").getAsString();
//	}
//
//	public boolean CheckForChief(String crew, Player p) {
//		String playerUUID = p.getUniqueId().toString();
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		String chief = crewObject.get("chief").getAsString();
//
//		return chief.equals(playerUUID);
//	}
//
//	public boolean CheckForChief(String crew, OfflinePlayer p) {
//		String playerUUID = p.getUniqueId().toString();
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		String chief = crewObject.get("chief").getAsString();
//
//		return chief.equals(playerUUID);
//	}
//
//	public boolean CheckForElder(String crew, Player p) {
//		String playerUUID = p.getUniqueId().toString();
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		String elder = crewObject.get("elder").getAsString();
//
//		return elder.equals(playerUUID);
//	}
//
//	public boolean CheckForElder(String crew, OfflinePlayer p) {
//		String playerUUID = p.getUniqueId().toString();
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		String elder = crewObject.get("elder").getAsString();
//
//		return elder.equals(playerUUID);
//	}
//
//	public boolean CheckSamecrew(String crewOne, String crewTwo) {
//		return crewOne.equals(crewTwo);
//	}

//	public void setChief(String crew, Player p) {
//		String playerUUID = p.getUniqueId().toString();
//        CrewsFile crewsFile = plugin.getCrewsFile();
//        JsonObject crewsData = crewsFile.getCrewsData();
//		JsonObject crewObject = crewsData.getAsJsonObject(crew.toLowerCase());
//		crewObject.addProperty("chief", playerUUID);
//        crewsFile.saveCrews();
//	}
//
//	public String getChief(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("chief").getAsString();
//	}
//
//	public void setElder(String crew, Player p) {
//		String playerUUID = p.getUniqueId().toString();
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		crewObject.addProperty("elder", playerUUID);
//		crewsClass.savecrewsFileJson();
//	}
//
//	public String getElder(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("elder").getAsString();
//	}
//
//	public void removeElder(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		crewObject.addProperty("elder", "");
//		crewsClass.savecrewsFileJson();
//	}
	
//	public int getVault(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("vault").getAsInt();
//	}
	
//	public int getCTFWins(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		JsonObject tribalGamesSection = crewObject.getAsJsonObject("tribalgameswins");
//
//		int ctf = 0;
//		if(tribalGamesSection.get("ctf") != null) {
//			ctf = tribalGamesSection.get("ctf").getAsInt();
//		} else {
//			tribalGamesSection.addProperty("ctf", 0);
//		}
//
//		return ctf;
//	}
//
//	public void addToVault(String crew, int amount, Player p) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		int vault = getVault(crew);
//		int newAmount = vault + amount;
//		ChatColor transactionColor0 = net.md_5.bungee.api.ChatColor.of("#E7761E");
//		ChatColor transactionColor1 = net.md_5.bungee.api.ChatColor.of("#72C06C");
//		ChatColor transactionColor2 = net.md_5.bungee.api.ChatColor.of("#767166");
//		ChatColor transactionColor3 = net.md_5.bungee.api.ChatColor.of("#4BD613");
//		String transactionMessage = transactionColor0 + "crew Vault: " + transactionColor0 + "crew Vault: " + transactionColor1 + vault + ChatColor.DARK_GREEN + " (" + ChatColor.GREEN + "+" + amount + ChatColor.DARK_GREEN + ")" + transactionColor2 + " = " + transactionColor3 + newAmount;
//		sendMessageToMembers(crew, ChatColor.GREEN + "(" + ChatColor.DARK_GREEN + p.getName() + ChatColor.GREEN + ") " + transactionMessage);
//		crewObject.addProperty("vault", newAmount);
//
//		int totalSponges = crewObject.get("totalSponges").getAsInt();
//		totalSponges += amount;
//		crewObject.addProperty("totalSponges", totalSponges);
//
//		crewsClass.savecrewsFileJson();
//	}
//
//	public void removeFromVault(String crew, int amount, Player p) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		int vault = getVault(crew);
//		int newAmount = vault - amount;
//
//		if(newAmount >= 0) {
//			ChatColor transactionColor0 = net.md_5.bungee.api.ChatColor.of("#E7761E");
//			ChatColor transactionColor1 = net.md_5.bungee.api.ChatColor.of("#D69213");
//			ChatColor transactionColor2 = net.md_5.bungee.api.ChatColor.of("#767166");
//			ChatColor transactionColor3 = net.md_5.bungee.api.ChatColor.of("#C0A66C");
//			String transactionMessage = transactionColor0 + "crew Vault: " + transactionColor1 + vault + ChatColor.DARK_RED + " (" + ChatColor.RED + "-" + amount + ChatColor.DARK_RED + ")" + transactionColor2 + " = " + transactionColor3 + newAmount;
//			sendMessageToMembers(crew, ChatColor.GOLD + "(" + ChatColor.YELLOW + p.getName() + ChatColor.GOLD + ") " + transactionMessage);
//			crewObject.addProperty("vault", newAmount);
//			crewsClass.savecrewsFileJson();
//		} else {
//			p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You can not afford this!");
//		}
//	}
//
//	public void upgradecrew(String crew, int vault, Player p) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//
//		if(vault >= 500 && getLevel(crew) == 9) {
//			crewObject.addProperty("level", 10);
//			crewObject.addProperty("maxPlayers", 12);
//			crewObject.addProperty("requiredSponges", -1);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 10!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 10!");
//		} else if(vault >= 300 && getLevel(crew) == 8) {
//			crewObject.addProperty("level", 9);
//			crewObject.addProperty("maxPlayers", 11);
//			crewObject.addProperty("requiredSponges", 1500);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 9!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 9!");
//		} else if(vault >= 200 && getLevel(crew) == 7) {
//			crewObject.addProperty("level", 8);
//			crewObject.addProperty("maxPlayers", 10);
//			crewObject.addProperty("requiredSponges", 1100);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 8!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 8!");
//		} else if(vault >= 150 && getLevel(crew) == 6) {
//			crewObject.addProperty("level", 7);
//			crewObject.addProperty("maxPlayers", 9);
//			crewObject.addProperty("requiredSponges", 800);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 7!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 7!");
//		} else if(vault >= 125 && getLevel(crew) == 5) {
//			crewObject.addProperty("level", 6);
//			crewObject.addProperty("maxPlayers", 8);
//			crewObject.addProperty("requiredSponges", 500);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 6!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 6!");
//		} else if(vault >= 100 && getLevel(crew) == 4) {
//			crewObject.addProperty("level", 5);
//			crewObject.addProperty("maxPlayers", 7);
//			crewObject.addProperty("requiredSponges", 300);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 5!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 5!");
//		} else if(vault >= 75 && getLevel(crew) == 3) {
//			crewObject.addProperty("level", 4);
//			crewObject.addProperty("maxPlayers", 6);
//			crewObject.addProperty("requiredSponges", 200);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 4!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 4!");
//		} else if(vault >= 50 && getLevel(crew) == 2) {
//			crewObject.addProperty("level", 3);
//			crewObject.addProperty("maxPlayers", 5);
//			crewObject.addProperty("requiredSponges", 100);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 3!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 3!");
//		} else if(vault >= 25 && getLevel(crew) == 1) {
//			crewObject.addProperty("level", 2);
//			crewObject.addProperty("maxPlayers", 4);
//			crewObject.addProperty("requiredSponges", 50);
//			sendMessageToMembers(crew, ChatColor.GREEN + p.getName() + " has upgraded the crew to level 2!");
//			Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + crew + " has upgraded to level 2!");
//		} else if(vault < 25 && getLevel(crew) == 1) {
//			crewObject.addProperty("level", 1);
//			crewObject.addProperty("maxPlayers", 3);
//			crewObject.addProperty("requiredSponges", 25);
//		}
//
//		crewsClass.savecrewsFileJson();
//	}

//	public int getMaxPlayers(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("maxPlayers").getAsInt();
//	}
	
//	public int getLevel(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("level").getAsInt();
//	}
//
//	public String getFoundedDate(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("dateCreated").getAsString();
//	}
//
//	public int getRequiredAmountForLevelUp(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("requiredSponges").getAsInt();
//	}
//
//	public int getRating(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		JsonObject tribalGamesObject = crewObject.getAsJsonObject("tribalgameswins");
//
//		int rating = 0;
//
//		if(tribalGamesObject.get("rating") != null){
//			rating = tribalGamesObject.get("rating").getAsInt();
//		} else {
//			tribalGamesObject.addProperty("rating", 0);
//		}
//
//		return rating;
//	}
//
//	public double getEconomyScore(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("economyScore").getAsInt();
//	}
//
//	public double getPowerScore(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		return crewObject.get("powerScore").getAsDouble();
//	}
//
//	public void getcrewInfo(JsonObject crewsJson, JsonObject crewObject, String crew, Player p, boolean bool) {
//		WarpManager warpManager = new WarpManager();
//		if(crewsJson.getAsJsonObject(crew.toLowerCase()) != null) {
//			String crewName = crewObject.get("showname").getAsString();
//			int level = crewObject.get("level").getAsInt();
//			int vault = crewObject.get("vault").getAsInt();
//			double powerScore = crewObject.get("powerScore").getAsDouble();
//			OfflinePlayer chief = Bukkit.getServer().getOfflinePlayer(UUID.fromString(crewObject.get("chief").getAsString()));
//
//			String elder = "";
//			if(!crewObject.get("elder").getAsString().isEmpty()) {
//				elder = Bukkit.getServer().getOfflinePlayer(UUID.fromString(crewObject.get("elder").getAsString())).getName();
//			}
//
//			String dateCreated = crewObject.get("dateCreated").getAsString();
//
//
//			p.sendMessage(ChatColor.GRAY + "---------[ " + chatUtil.crewsColor + crewName + ChatColor.GRAY + " ]---------");
//			p.sendMessage(chatUtil.lightGreen + unicode.foundedDate + " Date Founded: " + chatUtil.lighterGreen + dateCreated);
//			p.sendMessage(chatUtil.lightGreen + unicode.level + " Level: " + chatUtil.lighterGreen + level);
//			p.sendMessage(chatUtil.lightGreen + unicode.vault + " Vault: " + chatUtil.lighterGreen + unicode.sponge_icon + vault);
//			p.sendMessage(chatUtil.lightGreen + unicode.powerScore + " Power Score: " + chatUtil.lighterGreen + powerScore);
//			p.sendMessage(chatUtil.lightGreen + unicode.chiefCrown + " Chief: " + chatUtil.lighterGreen + chief.getName());
//			p.sendMessage(chatUtil.lightGreen + unicode.elderFace + " Elder: " + chatUtil.lighterGreen + elder);
//
//			List<String> membersUUID = json.JsonArrayToStringList(crewObject.get("members").getAsJsonArray());
//			List<String> membersIGN = new ArrayList<String>();
//			for(String member : membersUUID) {
//				membersIGN.add(Bukkit.getServer().getOfflinePlayer(UUID.fromString(member)).getName());
//			}
//
//			String members = membersIGN.stream().collect(Collectors.joining(", "));
//
//			p.sendMessage(chatUtil.lightGreen + unicode.member + " Members (" + membersIGN.size() + "/" + getMaxPlayers(crew) + "): " + chatUtil.lighterGreen + members);
//
//			if(!bool) {
//				if(warpManager.compoundExists(crewName)) {
//					p.sendMessage(chatUtil.lightGreen + unicode.compound + " Compound: " + ChatColor.GREEN + "ACTIVE");
//				} else {
//					p.sendMessage(chatUtil.lightGreen + unicode.compound + " Compound: " + ChatColor.RED + "INACTIVE");
//				}
//
//				p.sendMessage(ChatColor.GRAY + "----------------------------");
//
//			}
//		} else {
//			p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "That crew name does not exist or the selected player is not in a crew!");
//		}
//	}


    //FOCUS ON THIS STUFF BELOW
//
//	public void generateEconomy() {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		for(String crew : crewsJson.keySet()) {
//			JsonObject crewObject = crewsJson.getAsJsonObject(crew);
//
//			int totalSponges;
//			if(crewObject.get("totalSponges") == null){
//				crewObject.addProperty("totalSponges", 0);
//			}
//
//			totalSponges = crewObject.get("totalSponges").getAsInt();
//
//			crewObject.addProperty("economyScore", (double)totalSponges);
//			crewsClass.savecrewsFileJson();
//		}
//	}
//
//	//disabled for now
//	public void generateRating() {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		for(String crew : crewsJson.keySet()) {
//			JsonObject crewObject = crewsJson.getAsJsonObject(crew);
//			JsonObject tribalGamesObject = crewObject.getAsJsonObject("tribalgameswins");
//
//			int ctf;
//			int koth;
//			int tott;
//			if(tribalGamesObject.get("ctf") == null){
//				tribalGamesObject.addProperty("ctf", 0);
//			}
//
//			if(tribalGamesObject.get("koth") == null){
//				tribalGamesObject.addProperty("koth", 0);
//			}
//
//			if(tribalGamesObject.get("tott") == null){
//				tribalGamesObject.addProperty("tott", 0);
//			}
//
//			ctf = tribalGamesObject.get("ctf").getAsInt();
//			koth = tribalGamesObject.get("koth").getAsInt();
//			tott = tribalGamesObject.get("tott").getAsInt();
//
//			int totalWins = ctf + koth + tott;
//			int rating = totalWins*100;
//			crewObject.addProperty("rating", rating);
//			crewsClass.savecrewsFileJson();
//		}
//	}
//
//	public void generatePowerScore() {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		for(String crew : crewsJson.keySet()) {
//			JsonObject crewObject = crewsJson.getAsJsonObject(crew);
//			double economyScore = crewObject.get("economyScore").getAsDouble();
//			JsonObject tribalGamesObject = crewObject.getAsJsonObject("tribalgameswins");
//			int currentMembers = crewObject.get("members").getAsJsonArray().size();
//
//			double ratingScore;
//			if(tribalGamesObject.get("rating") == null){
//				tribalGamesObject.addProperty("rating", 0);
//			}
//
//			ratingScore = tribalGamesObject.get("rating").getAsDouble();
//			double powerScore = economyScore + ratingScore + (currentMembers*200);
//
//			double powerScoreRounded = new BigDecimal(powerScore).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//			crewObject.addProperty("powerScore", powerScoreRounded);
//			crewsClass.savecrewsFileJson();
//		}
//	}
//
//	public void generateEconomyPercrew(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew);
//		int totalSponges = crewObject.get("totalSponges").getAsInt();
//		crewObject.addProperty("economyScore", (double)totalSponges);
//		crewsClass.savecrewsFileJson();
//	}
//
//	public void generatePowerScorePercrew(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew);
//		double economyScore = crewObject.get("economyScore").getAsDouble();
//		JsonObject tribalGamesObject = crewObject.getAsJsonObject("tribalgameswins");
//		int currentMembers = crewObject.get("members").getAsJsonArray().size();
//		double ratingScore = tribalGamesObject.get("rating").getAsDouble();
//		double powerScore = economyScore + ratingScore + (currentMembers*200);
//
//		double powerScoreRounded = new BigDecimal(powerScore).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//
//		crewObject.addProperty("powerScore", powerScoreRounded);
//		crewsClass.savecrewsFileJson();
//	}
//
//	public HashMap<String, Double> generateLeaderboardJson() {
//		HashMap<String, Double> crewAndScore = new HashMap<>();
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		for(String crew : crewsJson.keySet()) {
//			JsonObject crewObject = crewsJson.getAsJsonObject(crew);
//			double powerScore = crewObject.get("powerScore").getAsDouble();
//			String showName = crewObject.get("showname").getAsString();
//			crewAndScore.put(showName, powerScore);
//		}
//		return crewAndScore;
//	}

//	public void generateScorePercrew(String crew) {
//		generateEconomyPercrew(crew);
//		generatePowerScorePercrew(crew);
//	}
//
//	public void generateScore() {
//		generateEconomy();
//
//		//disabled for now
//		generateRating();
//		generatePowerScore();
//	}
}
