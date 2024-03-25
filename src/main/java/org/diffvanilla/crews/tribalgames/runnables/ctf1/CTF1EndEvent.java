//package org.thefruitbox.fbcrews.tribalgames.runnables.ctf1;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.stream.Collectors;
//
//import com.google.gson.JsonObject;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.potion.PotionEffectType;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.scoreboard.Scoreboard;
//import org.thefruitbox.fbcrews.managers.CrewManager;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//import org.thefruitbox.fbcrews.utilities.ChatUtilities;
//import org.thefruitbox.fbcrews.utilities.GeneralUtilities;
//
//import com.sk89q.worldguard.domains.DefaultDomain;
//import com.sk89q.worldguard.protection.regions.ProtectedRegion;
//
//import net.md_5.bungee.api.ChatColor;
//
//public class CTF1EndEvent extends BukkitRunnable implements CTF1Manager {
//
//	private final ChatUtilities cu = new ChatUtilities();
//
//	@SuppressWarnings("null")
//	@Override
//	public void run() {
//		DefaultDomain regionMembers = ctf1.getMembers();
//
//		for(String player : CTF1Manager.getParticipants()) {
//			regionMembers.removePlayer(player);
//			ctf1.setMembers(regionMembers);
//		}
//
//		String redTeamName = ChatColor.RED + CTF1Manager.getRedTeamName();
//		String blueTeamName = ChatColor.BLUE + CTF1Manager.getBlueTeamName();
//		int redTeamCaptures = CTF1Manager.getRedTeam().getInt("captures");
//		int blueTeamCaptures = CTF1Manager.getBlueTeam().getInt("captures");
//
//		CTF1Manager.sendParticipantsMessage(cu.tgPrefix + ChatColor.YELLOW + "CTF has ended!", Sound.BLOCK_END_PORTAL_SPAWN, 0.5F, 0F);
//
//		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "=======" + ChatColor.YELLOW.toString()
//		+ ChatColor.BOLD + "CTF Final Score" + ChatColor.DARK_GRAY +"=======");
//
//		Map<String, Integer> unsortedScores = new HashMap<String, Integer>();
//		unsortedScores.put(redTeamName, redTeamCaptures);
//		unsortedScores.put(blueTeamName, blueTeamCaptures);
//
//		Map<String, Integer> sortedScores = GeneralUtilities.sortByComparator(unsortedScores, false);
//
//		int counter = 1;
//		int amount = 0;
//		List<String> winners = new ArrayList<String>();
//		for (Entry<String, Integer> entry : sortedScores.entrySet())
//        {
//            Bukkit.broadcastMessage("#" + counter + ": " + entry.getKey() + ChatColor.GRAY + " - " + ChatColor.YELLOW + entry.getValue());
//
//			List<String> playersIGN = new ArrayList<String>();
//
//            for(String s : CTF1Manager.getPlayersBycrewName(ChatColor.stripColor(entry.getKey()))) {
//            	playersIGN.add(s);
//            }
//
//            String playersStringList = playersIGN.stream().collect(Collectors.joining(", "));
//
//            Bukkit.broadcastMessage(ChatColor.GRAY + "Players: " + ChatColor.GOLD + playersStringList);
//
//            //blank message to seperate team info in chat
//            if(counter == 1 && redTeamCaptures != blueTeamCaptures) {
////            	Bukkit.broadcastMessage(ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + "100 Sponges");
//            	Bukkit.broadcastMessage(" ");
//            	amount = 100;
//
//            	JsonObject crewsJson = crewsClass.getcrewsJson();
//				JsonObject crewObject = crewsJson.getAsJsonObject(entry.getKey());
//				JsonObject tribalGamesObject = crewObject.getAsJsonObject("tribalgameswins");
//
//        		int ctfWins = tribalGamesObject.get("ctf").getAsInt();
//        		ctfWins += 1;
//				tribalGamesObject.addProperty("ctf", ctfWins);
//
//
//            	crewsClass.saveCTFFile();
//
//            	//get winners to reward sponges
//             	winners = playersIGN;
//            } else if (redTeamCaptures == blueTeamCaptures) {
//            	//Bukkit.broadcastMessage(ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + "50 Sponges");
//            	Bukkit.broadcastMessage(" ");
//            	amount = 50;
//            	winners = playersIGN;
//            }
//
//            counter++;
//        }
//
//		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "==============================");
//
//		//REMOVE
//		CTF1Manager.sendParticipantsMessage("", null, 0F, 0F);
//		CTF1Manager.sendParticipantsMessage(cu.tgPrefix + ChatColor.RED + "This feature is currently in beta testing. Rewards are currently"
//				+ "disabled until feature is stable.", null, 0F, 0F);
//
//    	//giveSpongeReward(winners, amount);
//
//		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
//		for(String s : CTF1Manager.getParticipants()) {
//			Player p = Bukkit.getPlayer(s);
//			p.setScoreboard(board);
//		}
//
//		//removes members from ctf1spec so they can enter arena after game is finished
//		ProtectedRegion region = regions.getRegion("ctf1spec");
//		regionMembers = region.getMembers();
//		regionMembers.removeAll();
//		region.setMembers(regionMembers);
//		CTF1Manager.removeFlagsOnFinish();
//
//		CTF1Manager.teleportPlayersRandomly();
//
//		clearCTFFile(crewsClass.getCTF());
//
//		for(String s : CTF1Manager.getParticipants()) {
//			Player p = Bukkit.getPlayer(s);
//
//			if(p != null) {
//				p.removePotionEffect(PotionEffectType.GLOWING);
//			}
//		}
//	}
//
//	private void giveSpongeReward(List<String> players, int amount) {
//		for(String s : players) {
//			Player p = Bukkit.getPlayer(s);
//
//			if(p != null) {
//				ItemStack sponges = new ItemStack(Material.SPONGE, amount);
//		    	Map<Integer, ItemStack> spongeStack = p.getInventory().addItem(sponges);
//		    	if(!spongeStack.isEmpty()) {
//			    	for(Map.Entry<Integer, ItemStack> entry : spongeStack.entrySet()) {
//			    		ItemStack stack = entry.getValue();
//			    		if(stack.getAmount() > 0) {
//			    			int amountClaimed = 100 - stack.getAmount();
//			    			p.sendMessage(cu.spongeColor + "You have won " + amountClaimed + " sponges!");
//				    		p.sendMessage(ChatColor.RED + "Inventory full. Could not claim " + stack.getAmount() + " sponges.");
//
//				    		ConfigurationSection rewards = crewsClass.getRewards();
//		            		String playerUUID = p.getUniqueId().toString();
//				    		ConfigurationSection playerRewards = rewards.getConfigurationSection(playerUUID);
//				    		playerRewards.set("unclaimed", stack.getAmount());
//				    		break;
//			    		}
//			    	}
//		    	} else {
//		    		p.sendMessage(cu.spongeColor + "You have won " + amount + " sponges!");
//		    	}
//			}
//		}
//	}
//
//	private void clearCTFFile(FileConfiguration ctfFile) {
//		ctfFile.set("event", false);
//
//		CTF1Manager.getBlueTeam().set("captures", 0);
//		CTF1Manager.getBlueTeam().set("kills", 0);
//		CTF1Manager.getBlueTeam().set("crew", null);
//		CTF1Manager.getBlueTeam().set("players", null);
//		CTF1Manager.getBlueTeam().set("flagholder", null);
//
//		CTF1Manager.getRedTeam().set("captures", 0);
//		CTF1Manager.getRedTeam().set("kills", 0);
//		CTF1Manager.getRedTeam().set("crew", null);
//		CTF1Manager.getRedTeam().set("players", null);
//		CTF1Manager.getRedTeam().set("flagholder", null);
//
//
//		ctfFile.set("current-countdown", 0);
//		ctfFile.set("participants", null);
//		ctfFile.set("playersInArena", null);
//
//		crewsClass.saveCTFFile();
//	}
//}
