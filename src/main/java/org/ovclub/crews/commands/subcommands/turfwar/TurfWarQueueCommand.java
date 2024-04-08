package org.ovclub.crews.commands.subcommands.turfwar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.object.turfwar.TurfWarQueue;
import org.ovclub.crews.utilities.GUIUtilities;

import java.util.UUID;

public class TurfWarQueueCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Queue up for a turf war.";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews turfwar queue";
    }

    @Override
    public String getPermission() {
        return "crews.player.turfwar.queue";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        TurfWarQueue queue = plugin.getTurfWarManager().getQueue();
        if(pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(plugin.getTurfWarManager().getQueue().isInQueue(pCrew)) {
            p.sendMessage(ConfigManager.ALREADY_IN_QUEUE);
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 9, Component.text("Crew Turf Wars"));
        UUID playerUUID = p.getUniqueId();
        GUIUtilities.initializeTurfWarQueueItems(inv, queue.size());
        data.getInventories().put(playerUUID, inv);
        GUIUtilities.openInventory(p, data.getInventories().get(playerUUID));
    }
}
