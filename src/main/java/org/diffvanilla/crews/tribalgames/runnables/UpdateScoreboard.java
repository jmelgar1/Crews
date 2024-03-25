//package org.thefruitbox.fbcrews.tribalgames.runnables;
//
//import org.bukkit.Bukkit;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.scoreboard.DisplaySlot;
//import org.bukkit.scoreboard.Objective;
//import org.bukkit.scoreboard.Scoreboard;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//
//import net.md_5.bungee.api.ChatColor;
//import org.thefruitbox.fbcrews.utilities.ChatUtilities;
//
//public class UpdateScoreboard extends BukkitRunnable implements CTF1Manager {
//
//	private final ChatUtilities cu = new ChatUtilities();
//
//	@Override
//	public void run() {
//
//		do {
//			ConfigurationSection redTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//			String redName = redTeam.getString("crew");
//			int redCaptures = redTeam.getInt("captures");
//			int redKills = redTeam.getInt("kills");
//
//			ConfigurationSection blueTeam = ctf.getConfigurationSection("teams").getConfigurationSection("blue");
//			String blueName = blueTeam.getString("crew");
//			int blueCaptures = blueTeam.getInt("captures");
//			int blueKills = blueTeam.getInt("kills");
//
//			for(String s : CTF1Manager.getParticipants()) {
//				//create new scoreboard
//				Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
//				String title = cu.tgPrefix + ChatColor.YELLOW.toString() + ChatColor.BOLD + "CTF";
//				Objective obj = board.registerNewObjective("FBCTF", "dummy", title);
//
//				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//
//				Player p = Bukkit.getPlayer(s);
//				//empty space
//				obj.getScore(ChatColor.RESET.toString()).setScore(11);
//
//				if(redCaptures > blueCaptures) {
//					redTeamLead(obj, redCaptures, redKills, blueCaptures, blueKills, redName, blueName);
//				} else if (blueCaptures > redCaptures) {
//					blueTeamLead(obj, redCaptures, redKills, blueCaptures, blueKills, redName, blueName);
//				} else if (redKills > blueKills){
//					redTeamLead(obj, redCaptures, redKills, blueCaptures, blueKills, redName, blueName);
//				} else if (redKills < blueKills) {
//					blueTeamLead(obj, redCaptures, redKills, blueCaptures, blueKills, redName, blueName);
//				} else {
//					redTeamLead(obj, redCaptures, redKills, blueCaptures, blueKills, redName, blueName);
//				}
//
//				//empty space
//				obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(3);
//
//				//time left counter
//				obj.getScore(ChatColor.GRAY + "Time Left: " + ChatColor.GREEN + ctf.getString("current-countdown")).setScore(2);
//
//				//empty space
//				obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(1);
//
//				//server ip
//				obj.getScore(ChatColor.GREEN + "play.thefruitbox.net").setScore(0);
//
//				if(p != null) {
//					p.setScoreboard(board);
//				}
//			}
//		} while (!CTF1Manager.ctf.getBoolean("event"));
//
//		if(!CTF1Manager.ctf.getBoolean("event")) {
//			cancel();
//		}
//	}
//
//	void redTeamLead(Objective obj, int redCaptures, int redKills, int blueCaptures, int blueKills, String redName, String blueName) {
//		//red team on top
//		obj.getScore(ChatColor.RED.toString() + ChatColor.BOLD + redName.toUpperCase()).setScore(10);
//		obj.getScore(ChatColor.GRAY + "Captures: " + ChatColor.YELLOW + redCaptures + ChatColor.GOLD + "/" + ChatColor.YELLOW + 5 + ChatColor.RESET.toString()).setScore(9);
//		obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + redKills + ChatColor.RESET.toString()).setScore(8);
//
//		//empty space
//		obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(7);
//
//		//blue team below
//		obj.getScore(ChatColor.BLUE.toString() + ChatColor.BOLD + blueName.toUpperCase()).setScore(6);
//		obj.getScore(ChatColor.GRAY + "Captures: " + ChatColor.YELLOW + blueCaptures + ChatColor.GOLD + "/" + ChatColor.YELLOW + 5).setScore(5);
//		obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + blueKills).setScore(4);
//	}
//
//	void blueTeamLead(Objective obj, int redCaptures, int redKills, int blueCaptures, int blueKills, String redName, String blueName) {
//		//blue team on top
//		obj.getScore(ChatColor.BLUE.toString() + ChatColor.BOLD + blueName.toUpperCase()).setScore(10);
//		obj.getScore(ChatColor.GRAY + "Captures: " + ChatColor.YELLOW + blueCaptures + ChatColor.GOLD + "/" + ChatColor.YELLOW + 5).setScore(9);
//		obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + blueKills).setScore(8);
//
//		//empty space
//		obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(7);
//
//		//red team below
//		obj.getScore(ChatColor.RED.toString() + ChatColor.BOLD + redName.toUpperCase()).setScore(6);
//		obj.getScore(ChatColor.GRAY + "Captures: " + ChatColor.YELLOW + redCaptures + ChatColor.GOLD + "/" + ChatColor.YELLOW + 5 + ChatColor.RESET.toString()).setScore(5);
//		obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + redKills + ChatColor.RESET.toString()).setScore(4);
//	}
//
//	public void setCountdown() {
//		CountdownTimerLong timer = new CountdownTimerLong(crewsClass,
//		        30, 0, (t) -> ctf.set("current-countdown", (t.getMinute() + ":" + (t.getSecondsLeft())))
//		);
//		timer.scheduleTimer();
//	}
//}
