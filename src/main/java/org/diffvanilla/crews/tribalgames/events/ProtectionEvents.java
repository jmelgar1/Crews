//package org.thefruitbox.fbcrews.tribalgames.events;
//
//import org.bukkit.Location;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTFEvents;
//import org.thefruitbox.fbcrews.utilities.VectorUtilities;
//
//public class ProtectionEvents extends CTFEvents implements Listener, CTF1Manager {
//
//	@EventHandler
//	public void onUse(PlayerInteractEvent e) {
//		Player p = e.getPlayer();
//		Block b = e.getClickedBlock();
//		Action a = e.getAction();
//		Location playerLoc = p.getLocation();
//
//		if(a == Action.RIGHT_CLICK_BLOCK) {
//			if(b != null) {
//				Location blockLoc = b.getLocation();
//				if(regions.getApplicableRegions(VectorUtilities.locationToBV(playerLoc)).getRegions().contains(ctf1) ||
//						regions.getApplicableRegions(VectorUtilities.locationToBV(blockLoc)).getRegions().contains(ctf1)) {
//					e.setCancelled(true);
//				}
//			}
//		}
//	}
//
//	@EventHandler
//	public void placeBlocks(BlockPlaceEvent e) {
//		Block b = (Block) e.getBlock();
//
//		if(regions.getApplicableRegions(VectorUtilities.getBlockVector(b)).getRegions().contains(ctf1)) {
//			e.setCancelled(true);
//		}
//	}
//
//	@EventHandler
//	public void breakBlocks(BlockBreakEvent e) {
//		Block b = e.getBlock();
//
//		if(!ctf.getBoolean("event")) {
//			if(regions.getApplicableRegions(VectorUtilities.getBlockVector(b)).getRegions().contains(ctf1)) {
//				e.setCancelled(true);
//			}
//		}
//	}
//}
