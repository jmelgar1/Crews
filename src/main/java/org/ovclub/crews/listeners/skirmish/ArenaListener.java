package org.ovclub.crews.listeners.skirmish;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.skirmish.ArenaManager;
import org.ovclub.crews.object.skirmish.Arena;
import org.ovclub.crews.object.skirmish.Skirmish;
import org.ovclub.crews.object.skirmish.SkirmishTeam;
import org.ovclub.crews.utilities.ArenaUtilities;

public class ArenaListener implements Listener {
    private final Crews plugin;
    public ArenaListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Location blockLoc = e.getBlock().getLocation();
        if (ArenaUtilities.isGlassWallBlock(blockLoc, player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
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
                    if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                        Location newLocation = from.clone();
                        newLocation.setPitch(to.getPitch());
                        newLocation.setYaw(to.getYaw());
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

        if (killer != null && arena.equals(arenaManager.getArenaByPlayer(killer))) {
            handleKill(killer, arena);
        } else {
            handleDeathWithoutKiller(deceased, arena);
        }
    }

    private void handleKill(Player killer, Arena arena) {
        SkirmishTeam killerTeam = arena.getPlayerTeam(killer);
        SkirmishTeam blueTeam = arena.getSkirmish().getMatchup().getBlueTeam();

        updateScore(arena, killerTeam.equals(blueTeam), 1);
    }

    private void handleDeathWithoutKiller(Player deceased, Arena arena) {
        SkirmishTeam deceasedTeam = arena.getPlayerTeam(deceased);
        SkirmishTeam blueTeam = arena.getSkirmish().getMatchup().getBlueTeam();

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


    /*/
    ================================================================================
    ===============================================================================
    =============================================================================
    TEMP TEMP TEMP==================================================================
    ===================================================================================
    /*/
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow && event.getEntity() instanceof Player player) {

            if (isHeadshot(arrow, player)) {
                double distance = arrow.getLocation().distance(player.getLocation());
                double damage = calculateDamage(distance);
                event.setDamage(damage);
                player.sendMessage("You got shot!");

                ProjectileSource shooter = arrow.getShooter();
                if (shooter instanceof Player shootingPlayer) {
                    shootingPlayer.sendMessage("[🗣️] Headshot! + " + damage + " damage done.");
                }
            }
        }
    }

    private boolean isHeadshot(Arrow arrow, Player player) {
        BoundingBox playerBox = player.getBoundingBox();
        double headHeight = 1.6;
        double headMinY = playerBox.getMaxY() - headHeight;

        BoundingBox headBox = new BoundingBox(
            playerBox.getMinX(), headMinY, playerBox.getMinZ(),
            playerBox.getMaxX(), playerBox.getMaxY(), playerBox.getMaxZ()
        );

        Vector arrowImpact = arrow.getLocation().toVector();
        System.out.println("Checking headshot: Arrow at " + arrowImpact + ", Head Box " + headBox);

        return headBox.contains(arrowImpact.getX(), arrowImpact.getY(), arrowImpact.getZ());
    }


    private double calculateDamage(double distance) {
        double baseDamage = 9;
        double scalingFactor = 1.5;
        double minDistanceForExtraDamage = 5.0;
        double additionalDamage = distance > minDistanceForExtraDamage ? (distance - minDistanceForExtraDamage) * scalingFactor : 0;
        double totalDamage = baseDamage + additionalDamage;
        return Math.min(totalDamage, 50); // Max damage cap
    }
}

