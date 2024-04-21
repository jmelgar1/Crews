package org.ovclub.crews.listeners.skirmish;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.object.skirmish.Arena;
import org.ovclub.crews.object.skirmish.Skirmish;
import org.ovclub.crews.object.skirmish.SkirmishTeam;
import org.ovclub.crews.utilities.ArenaUtilities;
import org.ovclub.crews.utilities.SoundUtilities;

import java.util.ArrayList;
import java.util.UUID;

public class ArenaListener implements Listener {
    private final Crews plugin;
    public ArenaListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(player);
        if(arena != null) {
            if(arena.getIsInCountdown()) {
                e.setCancelled(true);
            }
        }
        Location blockLoc = e.getBlock().getLocation();
        if (ArenaUtilities.isGlassWallBlock(blockLoc, player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(player);
        if(arena != null) {
            if(arena.getIsInCountdown()) {
                e.setCancelled(true);
            }
        }
        Location blockLoc = e.getBlockPlaced().getLocation();
        if (ArenaUtilities.isGlassWallBlock(blockLoc, player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();

        Arena arena = plugin.getArenaManager().getArenaByPlayer(player);
        if (arena != null) {
            Location arenaCenter = arena.getCenter();
            World world = arena.getWorld();
            if (arenaCenter != null && to.getWorld().equals(world) && arena.getHasBeenTeleported()) {
                if (arena.getIsInCountdown()) {
                    if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
                        Location newLocation = from.clone();
                        newLocation.setPitch(to.getPitch());
                        newLocation.setYaw(to.getYaw());
                        newLocation.setY(to.getY());
                        e.setTo(newLocation);
                    }
                } else {
                    if (!ArenaUtilities.isInsideBounds(to, arenaCenter)) {
                        ArenaUtilities.teleportPlayerInside(player, to, arenaCenter, world);
                    } else {
                        ArenaUtilities.updateGlassWall(player, to, arenaCenter);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        ArenaManager arenaManager = plugin.getArenaManager();
        Player deceased = event.getEntity();
        Player killer = deceased.getKiller();

        Arena arena = arenaManager.getArenaByPlayer(deceased);
        if (arena == null) {
            return;
        }

        for(String uuid : arena.getSkirmish().getMatchup().getParticipants()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if (p != null && p.isOnline()) {
                SoundUtilities.playDeathSound(p);
            }
        }

        if (killer != null && arena.equals(arenaManager.getArenaByPlayer(killer))) {
            handleKill(killer, arena);
        } else {
            handleDeathWithoutKiller(deceased, arena);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Arena arena = plugin.getArenaManager().getArenaByPlayer(player);
        if(arena == null) {
            return;
        }
        Location arenaCenter = arena.getCenter();
        SkirmishTeam pTeam = arena.getPlayerTeam(player);
        ArrayList<Location> enemyLocations = new ArrayList<>();
        for(String uuid : arena.getSkirmish().getMatchup().getParticipants()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p != null && p.isOnline()) {
                if(!arena.getPlayerTeam(p).equals(pTeam)) {
                    enemyLocations.add(p.getLocation());
                }
            }
        }
        Location respawnLocation = getFarthestLocation(arenaCenter, enemyLocations);
        float yaw = plugin.getArenaManager().getYaw(respawnLocation, arena.getCenter());
        respawnLocation.setYaw(yaw);
        e.setRespawnLocation(respawnLocation);
    }

    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player player) {
            Arena arena = plugin.getArenaManager().getArenaByPlayer(player);
            if (arena == null) {
                return;
            }
            if (event.isGliding()) {
                ItemStack chestplate = player.getInventory().getChestplate();
                if (chestplate != null && chestplate.getType() == Material.ELYTRA) {
                    event.setCancelled(true);
                    player.sendMessage(ConfigManager.DISABLED_IN_SKIRMISH);
                }
            }
        }
    }

    private Location getFarthestLocation(Location arenaCenter, ArrayList<Location> enemyLocations) {
        Location bestLocation = arenaCenter;
        double bestDistance = -1;
        for (int dx = -ConfigManager.ARENA_RADIUS; dx <= ConfigManager.ARENA_RADIUS; dx++) {
            for (int dz = -ConfigManager.ARENA_RADIUS; dz <= ConfigManager.ARENA_RADIUS; dz++) {
                Location candidate = new Location(arenaCenter.getWorld(), arenaCenter.getX() + dx, arenaCenter.getWorld().getHighestBlockYAt(arenaCenter.getBlockX() + dx, arenaCenter.getBlockZ() + dz), arenaCenter.getZ() + dz);
                if (ArenaUtilities.isInsideBounds(candidate, arenaCenter)) {
                    double minDistance = enemyLocations.stream()
                        .mapToDouble(loc -> loc.distanceSquared(candidate))
                        .min()
                        .orElse(Double.MAX_VALUE);
                    if (minDistance > bestDistance) {
                        bestDistance = minDistance;
                        bestLocation = candidate;
                    }
                }
            }
        }
        return bestLocation;
    }

    private void handleKill(Player killer, Arena arena) {
        SkirmishTeam killerTeam = arena.getPlayerTeam(killer);
        SkirmishTeam blueTeam = arena.getSkirmish().getMatchup().getATeam();

        updateScore(arena, killerTeam.equals(blueTeam), 1);
    }

    private void handleDeathWithoutKiller(Player deceased, Arena arena) {
        SkirmishTeam deceasedTeam = arena.getPlayerTeam(deceased);
        SkirmishTeam blueTeam = arena.getSkirmish().getMatchup().getATeam();

        updateScore(arena, deceasedTeam.equals(blueTeam), -1);
    }

    private void updateScore(Arena arena, boolean isBlueTeam, int scoreChange) {
        Skirmish skirmish = arena.getSkirmish();
        int currentScore = isBlueTeam ? skirmish.getBlueTeamScore() : skirmish.getRedTeamScore();
        int newScore = Math.max(0, currentScore + scoreChange);
        if (isBlueTeam) {
            skirmish.setBlueTeamScore(newScore);
            arena.updateTeamScore(true, newScore);
        } else {
            skirmish.setRedTeamScore(newScore);
            arena.updateTeamScore(false, newScore);
        }
    }
}

