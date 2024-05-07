package org.ovclub.crews.utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.ovclub.crews.managers.file.ConfigManager;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArenaUtilities {
    private static final Set<Material> TRAVERSABLE_BLOCKS = EnumSet.of(
        Material.WATER, Material.COBWEB, Material.TALL_GRASS,
        Material.VINE, Material.LAVA, Material.AIR, Material.CAVE_AIR,
        Material.VOID_AIR, Material.KELP_PLANT, Material.SEAGRASS,
        Material.TALL_SEAGRASS, Material.SUGAR_CANE, Material.BAMBOO,
        Material.SHORT_GRASS, Material.FERN, Material.RAIL, Material.ACTIVATOR_RAIL,
        Material.DETECTOR_RAIL, Material.POWERED_RAIL, Material.SNOW, Material.WHEAT,
        Material.SUNFLOWER, Material.ROSE_BUSH, Material.CORNFLOWER, Material.KELP
    );

    private static final Map<Player, Map<Location, BlockData>> playerGlassBlocks = new HashMap<>();
    private static final int radius = ConfigManager.ARENA_RADIUS;
    private static final int buffer = ConfigManager.WALL_BUFFER;
    private static final int wallWidth = ConfigManager.WALL_WIDTH;
    private static final int wallHeight = ConfigManager.WALL_HEIGHT;

    public static boolean isGlassWallBlock(Location location, Player player) {
        Map<Location, BlockData> glassBlocks = playerGlassBlocks.get(player);
        return glassBlocks != null && glassBlocks.containsKey(location);
    }
    public static boolean isInsideBounds(Location loc, Location arenaCenter) {
        int centerX = arenaCenter.getBlockX();
        int centerZ = arenaCenter.getBlockZ();
        return Math.abs(loc.getBlockX() - centerX) < radius && Math.abs(loc.getBlockZ() - centerZ) < radius;
    }
    public static void teleportPlayerInside(Player player, Location loc, Location arenaCenter, World world) {
        int buffer = 2;
        int closestX = Math.max(arenaCenter.getBlockX() - radius + buffer, Math.min(loc.getBlockX(), arenaCenter.getBlockX() + radius - buffer));
        int closestZ = Math.max(arenaCenter.getBlockZ() - radius + buffer, Math.min(loc.getBlockZ(), arenaCenter.getBlockZ() + radius - buffer));

        Location nearestInside = new Location(world, closestX, loc.getBlockY(), closestZ);
        nearestInside.setY(world.getHighestBlockYAt(nearestInside));

        nearestInside.add(0, 1, 0);

        nearestInside.setYaw(loc.getYaw());
        nearestInside.setPitch(loc.getPitch());

        player.teleport(nearestInside);
        player.sendMessage(ConfigManager.STAY_IN_BOUNDS);
    }
    public static void updateGlassWall(Player player, Location loc, Location arenaCenter) {
        if (!isNearBoundary(loc, arenaCenter)) {
            clearGlass(player);
            return;
        }
        Map<Location, BlockData> newGlassBlocks = new HashMap<>();
        int nearestXBoundary = determineNearestBoundary(loc.getBlockX(), arenaCenter.getBlockX());
        int nearestZBoundary = determineNearestBoundary(loc.getBlockZ(), arenaCenter.getBlockZ());

        int startY = loc.getBlockY() - 5;
        for (int y = startY; y < startY + wallHeight; y++) {
            updateBoundaryWall(player, loc, nearestXBoundary, 'X', y, newGlassBlocks);
            updateBoundaryWall(player, loc, nearestZBoundary, 'Z', y, newGlassBlocks);
        }

        Map<Location, BlockData> previousBlocks = playerGlassBlocks.put(player, newGlassBlocks);
        if (previousBlocks != null) {
            previousBlocks.keySet().stream()
                .filter(oldLoc -> !newGlassBlocks.containsKey(oldLoc))
                .forEach(oldLoc -> player.sendBlockChange(oldLoc, previousBlocks.get(oldLoc)));
        }
    }
    private static void updateBoundaryWall(Player player, Location loc, int nearestBoundary, char axis, int y, Map<Location, BlockData> newGlassBlocks) {
        if (nearestBoundary == 0) return;

        int start = axis == 'X' ? loc.getBlockZ() - wallWidth / 2 : loc.getBlockX() - wallWidth / 2;
        int end = start + wallWidth;

        for (int i = start; i <= end; i++) {
            Location blockLoc = axis == 'X' ? new Location(loc.getWorld(), nearestBoundary, y, i) : new Location(loc.getWorld(), i, y, nearestBoundary);
            if (TRAVERSABLE_BLOCKS.contains(blockLoc.getBlock().getType())) {
                newGlassBlocks.put(blockLoc, blockLoc.getBlock().getBlockData());
                player.sendBlockChange(blockLoc, Material.RED_STAINED_GLASS.createBlockData());
            }
        }
    }
    private static void clearGlass(Player player) {
        Map<Location, BlockData> glassBlocks = playerGlassBlocks.remove(player);
        if (glassBlocks != null) {
            glassBlocks.forEach(player::sendBlockChange);
        }
    }
    private static int determineNearestBoundary(int playerCoord, int centerCoord) {
        return Math.abs(playerCoord - centerCoord) >= radius - buffer ?
            (playerCoord > centerCoord ? centerCoord + radius : centerCoord - radius) : 0;
    }
    private static boolean isNearBoundary(Location loc, Location arenaCenter) {
        int distX = Math.abs(loc.getBlockX() - arenaCenter.getBlockX());
        int distZ = Math.abs(loc.getBlockZ() - arenaCenter.getBlockZ());
        return distX <= radius + buffer || distZ <= radius + buffer;
    }
}
