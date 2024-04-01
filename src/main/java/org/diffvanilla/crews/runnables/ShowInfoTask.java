package org.diffvanilla.crews.runnables;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.utilities.CrewInfoUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShowInfoTask extends BukkitRunnable {
    private final Map<UUID, PacketContainer> playerMarkers = new HashMap<>();
    public Map<UUID, PacketContainer> getPlayerMarkers() {return playerMarkers;}

    private final Map<UUID, Integer> playerMarkerDistances = new HashMap<>();
    public Map<UUID, Integer> getPlayerMarkerDistances() {return playerMarkerDistances;}

    private final Crews plugin;
    public ShowInfoTask(Crews plugin) {this.plugin = plugin;}

    @Override
    public void run() {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getData().getInCrewInfo().contains(p)) {
                Location targetLocation = CrewInfoUtilities.getTargetLocation(p);
                PacketContainer marker = playerMarkers.get(p.getUniqueId());

                if (marker == null) {
                    PacketContainer newPacket = CrewInfoUtilities.setMarkerPacket(targetLocation, p, plugin);
                    plugin.getShowInfoTask().getPlayerMarkers().put(p.getUniqueId(), newPacket);
                } else {
                    CrewInfoUtilities.teleportMarkerPacket(marker, targetLocation, p, plugin);
                }
            } else if (playerMarkers.containsKey(p.getUniqueId())) {
                CrewInfoUtilities.clearAllPositionsAndMarkers(p, plugin);
            }
        }
    }
}
