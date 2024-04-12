package org.ovclub.crews.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ovclub.crews.Crews;
import org.ovclub.crews.managers.ConfigManager;

import java.util.ArrayList;

public class AskToConfirmTask extends BukkitRunnable {

    private final Crews plugin;
    public AskToConfirmTask(Crews plugin) {this.plugin = plugin;}

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            ArrayList<String> needToConfirm = plugin.getTurfWarManager().getWaitingForConfirmation(plugin.getData().getCrew(p));
            if(needToConfirm.contains(String.valueOf(p.getUniqueId()))) {
                p.sendMessage(ConfigManager.WAITING_ON_CONFIRMATION);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1.0F);
            }
        }
    }
}
