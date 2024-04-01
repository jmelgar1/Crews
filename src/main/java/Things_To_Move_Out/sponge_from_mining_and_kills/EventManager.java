//package org.diffvanilla.crews.managers;
//
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.block.Block;
//import org.bukkit.plugin.Plugin;
//import org.diffvanilla.crews.Crews;
//
//import net.coreprotect.CoreProtect;
//import net.coreprotect.CoreProtectAPI;
//
//public class EventManager {
//    private final Crews plugin;
//
//    public EventManager(Crews plugin) {
//        this.plugin = plugin;
//    }
//
//	protected SpongeManager spongeManager = new SpongeManager();
//
//    Location origin = new Location(Bukkit.getWorld("world"), 0, 0, 0);
//
//	//FOR MINING EVENTS
//	protected CoreProtectAPI getCoreProtect() {
//        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin("CoreProtect");
//
//        // Check that CoreProtect is loaded
//        if (!(plugin instanceof CoreProtect)) {
//            return null;
//        }
//
//        // Check that the API is enabled
//        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
//        if (!CoreProtect.isEnabled()) {
//            return null;
//        }
//
//        // Check that a compatible version of the API is loaded
//        if (CoreProtect.APIVersion() < 9) {
//            return null;
//        }
//
//        return CoreProtect;
//	}
//
//    public boolean isWithin50Blocks(Block block){
//        // Get the location of the block
//        Location blockLocation = block.getLocation();
//
//        // Create a location at (0, 0, 0) in the same world
//        Location origin = new Location(Bukkit.getWorld("world"), 0, 0, 0);
//
//        // Calculate the distance between the block and the origin
//        double distance = blockLocation.distance(origin);
//
//        // Check if the distance is less than or equal to 50 blocks
//        return distance <= 75;
//    }
//}
