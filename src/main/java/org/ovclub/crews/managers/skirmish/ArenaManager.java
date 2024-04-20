package org.ovclub.crews.managers.skirmish;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.skirmish.Arena;
import org.ovclub.crews.object.skirmish.Skirmish;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;
import org.ovclub.crews.object.skirmish.SkirmishTeam;

import java.util.*;

public class ArenaManager {
    private final ArrayList<Arena> arenas = new ArrayList<>();
    private final World world = Bukkit.getWorld("world");
    private final Random random = new Random();

    public Location getRandomArenaLocation() {
        int x = random.nextInt(3001) - 1500;
        int z = random.nextInt(3001) - 1500;
        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }

    public void setupArena(SkirmishMatchup matchup) {
        Location center = getRandomArenaLocation();
        double radius = ConfigManager.ARENA_RADIUS;
        double maxInwardOffset = Math.sqrt(2) * radius / 2;
        int diagonalInward = (int) (maxInwardOffset - 1);

        Location corner1 = new Location(center.getWorld(),
            center.getX() - radius + diagonalInward,
            center.getY(),
            center.getZ() - radius + diagonalInward);

        Location corner2 = new Location(center.getWorld(),
            center.getX() + radius - diagonalInward,
            center.getY(),
            center.getZ() + radius - diagonalInward);

        // Adjusting Y to be at the highest block at each location
        corner1.setY(center.getWorld().getHighestBlockYAt(corner1));
        corner2.setY(center.getWorld().getHighestBlockYAt(corner2));

        Skirmish skirmish = new Skirmish(matchup);
        Arena arena = new Arena(world, center, ConfigManager.ARENA_RADIUS, skirmish, true);
        arenas.add(arena);

        // Teleport each team to the calculated corners
        teleportTeamToArena(matchup.getBlueTeam(), corner1, arena, true);
        teleportTeamToArena(matchup.getRedTeam(), corner2, arena, false);

        arena.setHasBeenTeleported(true);

        Bukkit.broadcastMessage("An arena has been setup at " + formatLocation(center) + " for the upcoming skirmish!");
    }

    public void teleportTeamToArena(SkirmishTeam team, Location location, Arena arena, boolean isTeamOne) {
        for (String playerUUID : team.getPlayers()) {
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (player != null) {
                arena.addPlayerReturnPoint(player.getUniqueId(), player.getLocation());

                Location safeLocation = getSafeLocation(location);
                float yaw = getYaw(safeLocation, arena.getCenter());
                safeLocation.setYaw(yaw);
                player.teleport(safeLocation);

                // Set team color based on whether it's Team One or not
                ChatColor color = isTeamOne ? ChatColor.BLUE : ChatColor.RED;
                arena.setTeamColor(player, color);

                // Apply the glowing effect
                PotionEffect glowingEffect = new PotionEffect(PotionEffectType.GLOWING, 200, 0, false, false, true);
                player.addPotionEffect(glowingEffect);
                player.sendMessage("You have been teleported to the arena!");
            }
        }
    }

    public void teleportPlayersToReturnPoints(Arena arena) {
        HashMap<UUID, Location> playerReturnPoints = arena.getPlayerReturnPoint();
        for (String playerUUID : arena.getMatchup().getParticipants()) {
            UUID uuid = UUID.fromString(playerUUID);
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && playerReturnPoints.containsKey(uuid)) {
                Location returnLocation = playerReturnPoints.get(uuid);
                player.teleport(returnLocation);
            }
        }
    }

    private Location getSafeLocation(Location original) {
        World world = original.getWorld();
        int x = original.getBlockX();
        int z = original.getBlockZ();
        int radius = 5; // Checks a 10x10 area centered on the original location

        while (true) {
            int waterCount = 0;
            int totalCount = 0;

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int currentY = world.getHighestBlockYAt(x + dx, z + dz);
                    Material topMaterial = world.getBlockAt(x + dx, currentY, z + dz).getType();
                    if (topMaterial == Material.WATER || topMaterial == Material.SEAGRASS) {
                        waterCount++;
                    }
                    totalCount++;
                }
            }

            double waterRatio = (double) waterCount / totalCount;
            if (waterRatio < 0.5) { // Less than 50% of the area is water
                int safeY = world.getHighestBlockYAt(x, z);
                return new Location(world, x + 0.5, safeY+1, z + 0.5, original.getYaw(), original.getPitch());
            } else {
                // Adjust the location and try again if too much water
                x += 10; // Move 10 blocks over on each check, could be randomized or iterated differently
            }
        }
    }

    private boolean isNonSolid(Material mat) {
        return !mat.isSolid();
    }

    private boolean isSolid(Material mat) {
        return mat.isSolid() && mat != Material.LAVA && mat != Material.WATER;
    }

    private float getYaw(Location from, Location to) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        double angle = Math.atan2(deltaZ, deltaX);

        return (float)Math.toDegrees(angle) - 90;
    }
    public Arena getArenaByMatchup(SkirmishMatchup matchup) {
        if(!arenas.isEmpty()) {
            for (Arena arena : arenas) {
                if(matchup.equals(arena.getSkirmish().getMatchup())) {
                    return arena;
                }
            }
        }
        return null;
    }
    private String formatLocation(Location loc) {
        return "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ();
    }
    public Arena getArenaByPlayer(Player p) {
        if(!arenas.isEmpty()) {
            for (Arena arena : arenas) {
                SkirmishMatchup matchup = arena.getSkirmish().getMatchup();
                if(matchup.getParticipants().contains(String.valueOf(p.getUniqueId()))) {
                    return arena;
                }
            }
        }
        return null;
    }
    public void removeArena(Arena arena) {
        this.arenas.remove(arena);
    }
}
