//package org.thefruitbox.fbcrews.tribalgames.managers;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//import org.bukkit.command.ConsoleCommandSender;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.thefruitbox.fbcrews.Crews;
//
//import com.sk89q.worldedit.bukkit.BukkitAdapter;
//import com.sk89q.worldedit.math.BlockVector3;
//import com.sk89q.worldguard.WorldGuard;
//import com.sk89q.worldguard.protection.flags.Flags;
//import com.sk89q.worldguard.protection.flags.RegionGroup;
//import com.sk89q.worldguard.protection.flags.StateFlag;
//import com.sk89q.worldguard.protection.managers.RegionManager;
//import com.sk89q.worldguard.protection.managers.storage.StorageException;
//import com.sk89q.worldguard.protection.regions.ProtectedRegion;
//import com.sk89q.worldguard.protection.regions.RegionContainer;
//
//import net.md_5.bungee.api.ChatColor;
//
//public interface CTF1Manager {
//
//	//Crews instance
//	Crews crewsClass = Crews.getInstance();
//
//	//worldguard
//	public WorldGuard wg = WorldGuard.getInstance();
//
//	RegionContainer container = wg.getPlatform().getRegionContainer();
//	World world = Bukkit.getServer().getWorld("world");
//	public RegionManager regions = container.get(BukkitAdapter.adapt(world));
//	public ProtectedRegion ctf1 = regions.getRegion("ctf1");
//	public ProtectedRegion ctf1spec = regions.getRegion("ctf1spec");
//
//	//get string lists from config
//	FileConfiguration ctf = crewsClass.getCTF();
//
//	//ctf prefix
//	String ctfPrefix = ChatColor.YELLOW + "CTF: ";
//
//	static ConfigurationSection getRedTeam() {
//		ConfigurationSection redTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//		return redTeam;
//	}
//
//	static ConfigurationSection getBlueTeam() {
//		ConfigurationSection blueTeam = ctf.getConfigurationSection("teams").getConfigurationSection("blue");
//		return blueTeam;
//	}
//
//	static String getRedTeamName() {
//		ConfigurationSection redTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//		return redTeam.getString("crew");
//	}
//
//	static String getBlueTeamName() {
//		ConfigurationSection blueTeam = ctf.getConfigurationSection("teams").getConfigurationSection("blue");
//		return blueTeam.getString("crew");
//	}
//
//	static List<String> getcrews(){
//		return ctf.getStringList("crews");
//	}
//
//	static List<String> getParticipants(){
//		return ctf.getStringList("participants");
//	}
//
//	static List<String> getPlayersBycrewName(String crewName){
//		if(getBlueTeam().getString("crew").equals(crewName)) {
//			return getBluePlayers();
//		} else {
//			return getRedPlayers();
//		}
//	}
//
//	static List<String> getBluePlayers(){
//		ConfigurationSection blueTeam = ctf.getConfigurationSection("teams").getConfigurationSection("blue");
//		return blueTeam.getStringList("players");
//	}
//
//	static List<String> getRedPlayers(){
//		ConfigurationSection redTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//		return redTeam.getStringList("players");
//	}
//
//	static List<Player> getPlayersInArena(){
//		@SuppressWarnings("unchecked")
//		List<Player> playersInArena = (List<Player>) ctf.getList("playersInArena");
//		return playersInArena;
//	}
//
//	static boolean checkIfBlueFlagIsTaken() {
//		if(getRedTeam().contains("flagholder")) {
//			getRedTeam().get("flagholder");
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	static boolean checkIfRedFlagIsTaken() {
//		if(getBlueTeam().contains("flagholder")) {
//			getBlueTeam().get("flagholder");
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	static boolean checkIfPlayerHasBlueFlag(Player p) {
//		if(getRedTeam().contains("flagholder")) {
//			return getRedTeam().get("flagholder").equals(p.getName());
//		} else {
//			return false;
//		}
//	}
//
//	static boolean checkIfPlayerHasRedFlag(Player p) {
//		if(getBlueTeam().contains("flagholder")) {
//			return getBlueTeam().get("flagholder").equals(p.getName());
//		} else {
//			return false;
//		}
//	}
//
//	static String getTeam(Player p) {
//		if(getRedPlayers().contains(p.getName())) {
//			return "red";
//		} else if (getBluePlayers().contains(p.getName())){
//			return "blue";
//		} else {
//			return "none";
//		}
//	}
//
//	static void sendParticipantsMessage(String message, Sound sound, float volume, float pitch) {
//		for(String s : getParticipants()) {
//			Player p = Bukkit.getPlayer(s);
//			p.sendMessage(message);
//
//			if(sound != null) {
//				p.getWorld().playSound(p.getLocation(), sound, volume, pitch);
//			}
//		}
//	}
//
//	public static Location getRegionMiddleBlock(ProtectedRegion region, World world) {
//		BlockVector3 minPoint = region.getMinimumPoint();
//		BlockVector3 maxPoint = region.getMaximumPoint();
//
//		//get midpoint
//		int point1X = minPoint.getBlockX();
//		int point1Z = minPoint.getBlockZ();
//
//		int point2X = maxPoint.getBlockX();
//		int point2Z = maxPoint.getBlockZ();
//
//		int midpointX = (int)(point1X + point2X)/2;
//		int midpointZ = (int)(point1Z + point2Z)/2;
//
//		Location loc = new Location(world, midpointX, world.getHighestBlockYAt(midpointX, midpointZ)+1, midpointZ);
//
//		return loc;
//	}
//
//	static void resetFlag(ProtectedRegion region, Material material, World world) {
//		world.getBlockAt(getRegionMiddleBlock(region, world)).setType(material);
//	}
//
//	static void respawnPlayer(Player p, World world) {
//		if(getBluePlayers().contains(p.getName())) {
//			p.teleport(getRegionMiddleBlock(regions.getRegion("bluespawn1"), world));
//		} else if(getRedPlayers().contains(p.getName())) {
//			p.teleport(getRegionMiddleBlock(regions.getRegion("redspawn1"), world));
//		}
//	}
//
//	public static void setFlagsOnStart() {
//		List<String> test = new ArrayList<String>();
//		//don't allow players to exit
//		regions.getRegion("ctf1").setFlag(Flags.EXIT, StateFlag.State.DENY);
//		regions.getRegion("ctf1").setFlag(Flags.EXIT.getRegionGroupFlag(), RegionGroup.MEMBERS);
//
//		HashSet<String> blockedcmds = new HashSet<String>();
//		blockedcmds.add("/crews warp");
//		regions.getRegion("ctf1").setFlag(Flags.BLOCKED_CMDS, blockedcmds);
//
//		//enable keep inventory. Using console because errors of overlap between WG and WGEF
//		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
//		String command = "region flag -w world ctf1 keep-inventory true";
//		Bukkit.dispatchCommand(console, command);
//		command = "wg flushstates";
//		Bukkit.dispatchCommand(console, command);
//		command = "region flag -w world ctf1 pvp allow";
//		Bukkit.dispatchCommand(console, command);
//		command = "wg flushstates";
//		Bukkit.dispatchCommand(console, command);
//
//
//		//don't allow non-participants to enter ctf1spec
//		//prevents participants from leaving the arena and spectators from entering the arena
//		regions.getRegion("ctf1").setFlag(Flags.ENTRY, StateFlag.State.DENY);
//		regions.getRegion("ctf1").setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
//
//		try {
//			regions.save();
//		} catch (StorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public static void removeFlagsOnFinish() {
//
//		//don't allow participants to exit
//		regions.getRegion("ctf1").setFlag(Flags.EXIT, StateFlag.State.ALLOW);
//		regions.getRegion("ctf1").setFlag(Flags.EXIT.getRegionGroupFlag(), RegionGroup.NONE);
//
//		HashSet<String> blockedcmds = new HashSet<String>();
//		blockedcmds.remove("/crews warp");
//		regions.getRegion("ctf1").setFlag(Flags.BLOCKED_CMDS, blockedcmds);
//
//		//enable keep inventory. Using console because errors of overlap between WG and WGEF
//		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
//		String command = "region flag -w world ctf1 keep-inventory false";
//		Bukkit.dispatchCommand(console, command);
//		command = "wg flushstates";
//		Bukkit.dispatchCommand(console, command);
//		command = "region flag -w world ctf1 pvp deny";
//		Bukkit.dispatchCommand(console, command);
//		command = "wg flushstates";
//		Bukkit.dispatchCommand(console, command);
//
//		//don't allow non-participants to enter
//		regions.getRegion("ctf1").setFlag(Flags.ENTRY, StateFlag.State.ALLOW);
//		regions.getRegion("ctf1").setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NONE);
//
//		try {
//			regions.save();
//		} catch (StorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public static void teleportPlayersRandomly() {
//		for(String s : getParticipants()) {
//			Player p = Bukkit.getPlayer(s);
//			if(p != null) {
//				Location playerLoc = p.getLocation();
//				int playerX = (int) playerLoc.getX();
//				int playerZ = (int) playerLoc.getZ();
//
//				int x = (int)(Math.random()*(50-(-50)+1)+(-50)) + playerX;
//				int z = (int)(Math.random()*(50-(-50)+1)+(-50)) + playerZ;
//				Block block = p.getWorld().getHighestBlockAt(x, z).getRelative(BlockFace.UP);
//				Location newLoc = block.getLocation();
//
//				p.teleport(newLoc);
//			}
//		}
//	}
//}
