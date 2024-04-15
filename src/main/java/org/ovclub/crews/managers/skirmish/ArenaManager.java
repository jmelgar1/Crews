package org.ovclub.crews.managers.skirmish;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.ovclub.crews.Crews;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ArenaManager implements Listener {
    private final World world;
    private final Random random = new Random();
    private Location arenaCenter;
    private int radius = 25;  // Half of 50 for a 50x50 area
    private int glassBuffer = 2;  // Change to desired threshold for showing glass walls

    public ArenaManager(World world, Crews plugin) {
        this.world = world;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Location getRandomArenaLocation() {
        int x = random.nextInt(3001) - 1500;
        int z = random.nextInt(3001) - 1500;
        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }

    public void setupArena(Location center) {
        this.arenaCenter = center;
        Bukkit.broadcastMessage("An arena has been setup at " + formatLocation(center) + " for the upcoming skirmish!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        if (this.arenaCenter != null && to.getWorld().equals(world)) {
            if (isNearBoundary(to)) {
                showGlassWall(player, to);
            } else {
                hideGlassWall(player, to);
            }
        }
    }

    private boolean isNearBoundary(Location loc) {
        int distX = Math.abs(loc.getBlockX() - arenaCenter.getBlockX());
        int distZ = Math.abs(loc.getBlockZ() - arenaCenter.getBlockZ());
        return distX >= radius - glassBuffer && distX <= radius || distZ >= radius - glassBuffer && distZ <= radius;
    }

    private void showGlassWall(Player player, Location loc) {
        int yStart = 0;  // Bedrock level
        int yEnd = world.getMaxHeight();  // Sky limit
        for (int y = yStart; y < yEnd; y++) {
            Location blockLoc = new Location(world, loc.getBlockX(), y, loc.getBlockZ());
            if (blockLoc.getBlock().getType() == Material.AIR) {
                player.sendBlockChange(blockLoc, Material.GLASS.createBlockData());
            }
        }
    }

    private void hideGlassWall(Player player, Location loc) {
        int yStart = 0;
        int yEnd = world.getMaxHeight();
        for (int y = yStart; y < yEnd; y++) {
            Location blockLoc = new Location(world, loc.getBlockX(), y, loc.getBlockZ());
            if (blockLoc.getBlock().getType() == Material.GLASS) {
                player.sendBlockChange(blockLoc, Material.AIR.createBlockData());
            }
        }
    }

    private String formatLocation(Location loc) {
        return "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ();
    }
}
