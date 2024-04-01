//package org.diffvanilla.crews.runnables;
//
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.diffvanilla.crews.managers.SpongeManager;
//import org.diffvanilla.crews.utilities.ChatUtilities;
//
//public class CheckForUnclaimed extends BukkitRunnable {
//    private final ChatUtilities cu = new ChatUtilities();
//
//    SpongeManager spongeManager = new SpongeManager();
//
//    @Override
//    public void run() {
//
//        System.out.println("Checking for unclaimed");
//
//        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
//            if(spongeManager.checkForUnclaimed(p) != 0) {
//                int amount = spongeManager.checkForUnclaimed(p);
//                p.sendMessage(cu.spongeColor + "You have " + amount + " unclaimed sponges! /claimsponges");
//            }
//        }
//    }
//}
