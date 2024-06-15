package org.ovclub.crews.managers.skirmish;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.skirmish.Arena;
import org.ovclub.crews.object.skirmish.Skirmish;
import org.ovclub.crews.object.skirmish.SkirmishMatchup;
import org.ovclub.crews.object.skirmish.SkirmishTeam;
import org.ovclub.crews.utilities.SoundUtilities;

import java.util.*;

public class ArenaManager {
    private final ArrayList<Arena> arenas = new ArrayList<>();
    private final World world = Bukkit.getWorld("world");
    private final Random random = new Random();

    public Location getRandomArenaLocation() {
        World world = Bukkit.getWorld("world");
        Location location;
        int x, z;
        boolean isOcean;
        do {
            do {
                x = random.nextInt(3001) - 1500;
                z = random.nextInt(3001) - 1500;
            } while (!(Math.abs(x) > 500 && Math.abs(z) > 500));

            assert world != null;
            location = new Location(world, x, world.getHighestBlockYAt(x, z), z);
            Biome biome = world.getBiome(x, world.getHighestBlockYAt(x, z), z);
            isOcean = biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.FROZEN_OCEAN ||
                biome == Biome.COLD_OCEAN || biome == Biome.WARM_OCEAN || biome == Biome.DEEP_COLD_OCEAN ||
                biome == Biome.DEEP_LUKEWARM_OCEAN || biome == Biome.DEEP_FROZEN_OCEAN || biome == Biome.RIVER ||
                biome == Biome.FROZEN_RIVER;
        } while (isOcean);
        return location;
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

        corner1.setY(center.getWorld().getHighestBlockYAt(corner1));
        corner2.setY(center.getWorld().getHighestBlockYAt(corner2));

        Skirmish skirmish = new Skirmish(matchup);
        Arena arena = new Arena(world, center, ConfigManager.ARENA_RADIUS, skirmish, true);
        arenas.add(arena);

        teleportTeamToArena(matchup.getATeam(), corner1, arena, true);
        teleportTeamToArena(matchup.getBTeam(), corner2, arena, false);

        arena.setHasBeenTeleported(true);
    }

    public void teleportTeamToArena(SkirmishTeam team, Location location, Arena arena, boolean isTeamOne) {
        for (String playerUUID : team.getPlayers()) {
            Player p = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (p != null) {
                arena.addPlayerReturnPoint(p.getUniqueId(), p.getLocation());
                Location safeLocation = getSafeLocation(location);
                float yaw = getYaw(safeLocation, arena.getCenter());
                safeLocation.setYaw(yaw);
                p.teleport(safeLocation);
                SoundUtilities.playTeleportSound(p);
                ChatColor color = isTeamOne ? ChatColor.BLUE : ChatColor.RED;
                arena.setTeamColor(p, color);
                PotionEffect glowingEffect = new PotionEffect(PotionEffectType.GLOWING, 200, 0, false, false, true);
                p.addPotionEffect(glowingEffect);
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
        if (original == null || original.getWorld() == null) {
            return original;
        }
        World world = original.getWorld();
        int x = original.getBlockX();
        int z = original.getBlockZ();

        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                int currentY = world.getHighestBlockYAt(x + dx, z + dz);
                Material topMaterial = world.getBlockAt(x + dx, currentY, z + dz).getType();
                if (topMaterial == Material.WATER || topMaterial == Material.SEAGRASS || topMaterial == Material.LAVA) {
                    continue;
                }
                if (checkSurroundingsSafe(world, x + dx, currentY + 1, z + dz)) {
                    return new Location(world, x + dx + 0.5, currentY + 1.5, z + dz + 0.5);
                }
            }
        }
        return original;
    }

    private boolean checkSurroundingsSafe(World world, int x, int y, int z) {
        if (world == null) {
            return false;
        }
        for (int dy = 0; dy <= 2; dy++) {
            Material blockType = world.getBlockAt(x, y + dy, z).getType();
            if (blockType != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    public float getYaw(Location from, Location to) {
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
