//package org.thefruitbox.fbcrews.tribalgames.runnables.ctf1;
//
//import java.util.Random;
//
//import org.bukkit.Bukkit;
//import org.bukkit.Sound;
//import org.bukkit.World;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerRespawnEvent;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.scoreboard.DisplaySlot;
//import org.bukkit.scoreboard.Objective;
//import org.bukkit.scoreboard.Scoreboard;
//import org.thefruitbox.fbcrews.Crews;
//import org.thefruitbox.fbcrews.tribalgames.events.ActiveMatchEvents;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//import org.thefruitbox.fbcrews.tribalgames.runnables.UpdateScoreboard;
//
//import com.sk89q.worldedit.bukkit.BukkitAdapter;
//import com.sk89q.worldedit.math.BlockVector3;
//import com.sk89q.worldguard.domains.DefaultDomain;
//
//import net.md_5.bungee.api.ChatColor;
//import org.thefruitbox.fbcrews.utilities.ChatUtilities;
//
//public class CTF1BeginEvent extends BukkitRunnable implements CTF1Manager {
//
//	//Crews instance
//	private Crews crewsClass = Crews.getInstance();
//
//	private final ChatUtilities cu = new ChatUtilities();
//
//	ConfigurationSection redTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//	String teamRed = redTeam.getString("crew");
//
//	ConfigurationSection blueTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//	String teamBlue = redTeam.getString("crew");
//
//	@Override
//	public void run() {
//		crewsClass.getCTF().set("event", true);
//		crewsClass.saveCTFFile();
//
//		BlockVector3 minPoint = ctf1spec.getMinimumPoint();
//		BlockVector3 maxPoint = ctf1spec.getMaximumPoint();
//
//		World world = Bukkit.getWorld("world");
//
//		for(Player p : CTF1Manager.getPlayersInArena()) {
//			if(!CTF1Manager.getParticipants().contains(p.getName())) {
//
//				//find a space to teleport players who are not chosen to participate outside ctf arena
//				BlockVector3 position;
//				do {
//
//					Random rand = new Random();
//					int randX = rand.nextInt(maxPoint.getBlockX() - minPoint.getBlockX()) + minPoint.getBlockX();
//					int randZ = rand.nextInt(maxPoint.getBlockZ() - minPoint.getBlockZ()) + minPoint.getBlockZ();
//					position = BlockVector3.at(randX, (world.getHighestBlockYAt(randX, randZ))+1, randZ);
//					System.out.println(position);
//
//				} while(regions.getApplicableRegions(position).getRegions().contains(ctf1spec) &&
//						regions.getApplicableRegions(position).getRegions().contains(ctf1));
//
//				p.teleport(BukkitAdapter.adapt(world, position));
//				p.sendMessage(cu.tgPrefix + ChatColor.RED + "You have not been selected to participate in CTF!");
//			} else {
//
//				//add players who are selected as members to region
//				DefaultDomain regionMembers = ctf1.getMembers();
//				regionMembers.addPlayer(p.getName());
//				ctf1.setMembers(regionMembers);
//			}
//		}
//
//		//create game scoreboard
//		createScoreboard();
//
//		//register events
//		ActiveMatchEvents fce1 = new ActiveMatchEvents();
//		fce1.registerEvents();
//
//		CTF1Manager.setFlagsOnStart();
//
//		teleportPlayersToTeamSpawns();
//
//		CTFCheckForActiveMatch checkActive = new CTFCheckForActiveMatch();
//		checkActive.runTaskLater(crewsClass, 36000);
//	}
//
//	private void teleportPlayersToTeamSpawns() {
//		for(String s : CTF1Manager.getParticipants()) {
//			Player p = Bukkit.getPlayer(s);
//			World world = p.getWorld();
//			if(CTF1Manager.getRedPlayers().contains(s)) {
//				p.teleport(CTF1Manager.getRegionMiddleBlock(regions.getRegion("redspawn1"), world));
//			} else if (CTF1Manager.getBluePlayers().contains(s)) {
//				p.teleport(CTF1Manager.getRegionMiddleBlock(regions.getRegion("bluespawn1"), world));
//			}
//		}
//	}
//
//	void createScoreboard() {
//		//int redKills = crewsClass.getCTF().getConfigurationSection("teams").getConfigurationSection("red").getInt("kills");
//		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
//
//		String title = cu.tgPrefix + ChatColor.YELLOW.toString() + ChatColor.BOLD + "CTF";
//		Objective obj = board.registerNewObjective("FBCTF", "dummy", title);
//
//		//empty space
//		obj.getScore(ChatColor.RESET.toString()).setScore(11);
//
//		//red team section
//		obj.getScore(ChatColor.RED.toString() + ChatColor.BOLD + teamRed.toUpperCase()).setScore(10);
//		obj.getScore(ChatColor.GRAY + "Captures: " + ChatColor.YELLOW + 0 + ChatColor.GOLD + "/" + ChatColor.YELLOW + 5 + ChatColor.RESET.toString()).setScore(9);
//		obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + 0 + ChatColor.RESET.toString()).setScore(8);
//
//		//empty space
//		obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(7);
//
//		//blue team section
//		obj.getScore(ChatColor.BLUE.toString() + ChatColor.BOLD + teamBlue.toUpperCase()).setScore(6);
//		obj.getScore(ChatColor.GRAY + "Captures: " + ChatColor.YELLOW + 0 + ChatColor.GOLD + "/" + ChatColor.YELLOW + 5).setScore(5);
//		obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + 0).setScore(4);
//
//		//empty space
//		obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(3);
//
//		//time left counter
//		obj.getScore(ChatColor.GRAY + "Time Left: " + ChatColor.GREEN + ctf.getString("current-countdown")).setScore(2);
//
//		//empty space
//		obj.getScore(ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString()).setScore(1);
//
//		//server ip
//		obj.getScore(ChatColor.GREEN + "play.thefruitbox.net").setScore(0);
//
//		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
//		for(String s : CTF1Manager.getParticipants()) {
//			Player p = Bukkit.getPlayer(s);
//			p.setScoreboard(board);
//		}
//
//		UpdateScoreboard updateScoreboard = new UpdateScoreboard();
//		updateScoreboard.setCountdown();
//		updateScoreboard.run();
//	}
//}
