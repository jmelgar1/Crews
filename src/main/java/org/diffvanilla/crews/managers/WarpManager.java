//package org.diffvanilla.crews.managers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gson.JsonObject;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.entity.Player;
//import org.bukkit.event.HandlerList;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.diffvanilla.crews.Crews;
//
//import net.md_5.bungee.api.ChatColor;
//import org.diffvanilla.crews.utilities.ChatUtilities;
//
//public class WarpManager {
//
//    private WarpCancelListener listener;
//
//	//Crews instance
//	private static WarpManager instance;
//
//	//Crews instance
//	private final Crews crewsClass = Crews.getInstance();
//
//	CrewManager crewManager = new CrewManager();
//	ChatUtilities chatUtil = new ChatUtilities();
//
//	public List<Player> inWarp = new ArrayList<Player>();
//
//	public static WarpManager getInstance() {
//		return instance;
//	}
//
//	public void setCompound(String crew, Player p) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		String playerCrew = crewManager.getPlayercrew(p);
//		JsonObject crewObject = crewsJson.getAsJsonObject(playerCrew.toLowerCase());
//
//		Location loc = p.getLocation();
//		JsonObject compound = new JsonObject();
//		compound.addProperty("world", loc.getWorld().getName());
//		compound.addProperty("x", String.valueOf(loc.getX()));
//		compound.addProperty("y", String.valueOf(loc.getY()));
//		compound.addProperty("z", String.valueOf(loc.getZ()));
//		compound.addProperty("yaw", String.valueOf(loc.getYaw()));
//		compound.addProperty("pitch", String.valueOf(loc.getPitch()));
//
//		crewObject.add("compound", compound);
//
//		crewManager.sendMessageToMembers(playerCrew, chatUtil.successIcon + ChatColor.GREEN + "crew compound has been set!");
//		//setNumberOfWarps(playerCrew, 1);
//
//		crewsClass.savecrewsFileJson();
//	}
//
//	public void deleteCompound(String crew, Player p) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		String playerCrew = crewManager.getPlayercrew(p);
//		JsonObject crewObject = crewsJson.getAsJsonObject(playerCrew.toLowerCase());
//
//		if(compoundExists(playerCrew)) {
//			crewObject.add("compound", null);
//			crewManager.sendMessageToMembers(playerCrew, chatUtil.errorIcon + ChatColor.RED + "crew compound has been removed!");
//			//setNumberOfWarps(playerCrew, 0);
//
//			crewsClass.savecrewsFileJson();
//		} else {
//			p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "crew compound is not set!");
//		}
//	}
//
//	public Boolean compoundExists(String crew) {
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//		JsonObject crewObject = crewsJson.getAsJsonObject(crew.toLowerCase());
//		JsonObject compoundObject = crewObject.getAsJsonObject("compound");
//
//		return compoundObject != null;
//	}
//
//	public void warpPlayer(String crew, Player p) {
//	    inWarp.add(p);
//
//		JsonObject crewsJson = crewsClass.getcrewsJson();
//	    String playerCrew = crewManager.getPlayercrew(p);
//		JsonObject crewObject = crewsJson.getAsJsonObject(playerCrew.toLowerCase());
//		JsonObject compoundObject = crewObject.getAsJsonObject("compound");
//
//	    if (compoundObject != null) {
//	        p.sendMessage(ChatColor.DARK_GREEN + "[\uD83C\uDFC3] " + ChatColor.GREEN + "Warping in 5 seconds...");
//
//	        BukkitRunnable warpTask = new BukkitRunnable() {
//
//	            @Override
//	            public void run() {
//	                if (!inWarp.contains(p)) {
//	                    // Player has cancelled the warp by moving
//	                    return;
//	                }
//
//	                // Retrieve warp location from config
//	                double x = Double.parseDouble(compoundObject.get("x").getAsString());
//	                double y = Double.parseDouble(compoundObject.get("y").getAsString());
//	                double z = Double.parseDouble(compoundObject.get("z").getAsString());
//	                int yaw = (int) Double.parseDouble(compoundObject.get("yaw").getAsString());
//	                int pitch = (int) Double.parseDouble(compoundObject.get("pitch").getAsString());
//	                String world = compoundObject.get("world").getAsString();
//	                Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
//
//	                // Teleport player
//	                p.teleport(loc);
//
//	                int priceToWarp = crewsClass.getPrices().getInt("gotowarp");
//	                crewManager.removeFromVault(playerCrew, priceToWarp, p);
//
//	                p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "Warp successful!");
//	                inWarp.remove(p);
//
//	                // Unregister the listener and remove the played from the inWarp list
//	                HandlerList.unregisterAll(listener);
//	                inWarp.remove(p);
//	            }
//	        };
//
//            listener = new WarpCancelListener(p, warpTask, inWarp);
//            Bukkit.getPluginManager().registerEvents(listener, crewsClass);
//
//	        // Schedule the warp task to run after 5 seconds
//	        warpTask.runTaskLater(crewsClass, 100);
//
//	    } else {
//	        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Compound does not exist!");
//	    }
//	}
//}
