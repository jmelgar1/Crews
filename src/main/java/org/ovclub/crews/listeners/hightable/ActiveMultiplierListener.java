package org.ovclub.crews.listeners.hightable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ovclub.crews.Crews;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.GUI.GUICreator;

public class ActiveMultiplierListener implements Listener {

    private final Crews plugin;
    public ActiveMultiplierListener(Crews plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Crew c : plugin.getData().getCrews()) {
            if (c.hasMember(e.getPlayer())) {
                plugin.getData().addCPlayer(e.getPlayer(), c);
                if(c.isInHighTable()) {
                    GUICreator.createHighTableVoteGUI(plugin.getData(), e.getPlayer());
                }
                break;
            }
        }
    }
}
