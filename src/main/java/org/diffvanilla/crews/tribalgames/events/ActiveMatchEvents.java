//package org.thefruitbox.fbcrews.tribalgames.events;
//
//import java.util.List;
//
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.block.Block;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.entity.EntityToggleGlideEvent;
//import org.bukkit.event.entity.PlayerDeathEvent;
//import org.bukkit.event.player.PlayerItemDamageEvent;
//import org.bukkit.event.player.PlayerRespawnEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.PlayerInventory;
//import org.bukkit.potion.PotionEffectType;
//import org.spigotmc.event.entity.EntityMountEvent;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTFEvents;
//import org.thefruitbox.fbcrews.tribalgames.runnables.ctf1.CTF1EndEvent;
//import org.thefruitbox.fbcrews.utilities.UnicodeCharacters;
//import org.thefruitbox.fbcrews.utilities.VectorUtilities;
//
//import de.netzkronehd.wgregionevents.events.RegionEnteredEvent;
//import net.md_5.bungee.api.ChatColor;
//
//public class ActiveMatchEvents extends CTFEvents implements Listener, CTF1Manager {
//
//	List<String> participants = CTF1Manager.getParticipants();
//	List<String> redPlayers = CTF1Manager.getRedPlayers();
//	List<String> bluePlayers = CTF1Manager.getBluePlayers();
//
//	ConfigurationSection redTeam = ctf.getConfigurationSection("teams").getConfigurationSection("red");
//	ConfigurationSection blueTeam = ctf.getConfigurationSection("teams").getConfigurationSection("blue");
//
//	String prefix = CTF1Manager.ctfPrefix;
//
//	UnicodeCharacters uc = new UnicodeCharacters();
//
//	@EventHandler
//	public void onSteal(BlockBreakEvent e) {
//		Block b = e.getBlock();
//		Player p = e.getPlayer();
//		String playerName = p.getName();
//
//		Material material = b.getType();
//
//		System.out.println(material);
//		if(regions.getApplicableRegions(VectorUtilities.locationToBV(b.getLocation())).getRegions().contains(ctf1)) {
//			if(material == Material.BLUE_BANNER || material == Material.RED_BANNER) {
//
//				e.setDropItems(false);
//
//				if(redPlayers.contains(playerName)) {
//					if(material == Material.BLUE_BANNER) {
//						getFlagStolen(ChatColor.RED, "Blue flag", playerName, redTeam, p);
//					} else {
//						sendWrongFlagMessage(p, e);
//					}
//
//				} else if (bluePlayers.contains(playerName)) {
//					if(material == Material.RED_BANNER) {
//						getFlagStolen(ChatColor.BLUE, "Red flag", playerName, blueTeam, p);
//					} else {
//						sendWrongFlagMessage(p, e);
//					}
//				}
//			} else {
//				e.setCancelled(true);
//			}
//		}
//	}
//
//	@EventHandler
//	public void onCapture(RegionEnteredEvent e) {
//		Player p = e.getPlayer();
//		String playerName = p.getName();
//
//		if(e.getRegion().equals(regions.getRegion("blueflag1")) && CTF1Manager.checkIfPlayerHasRedFlag(p)) {
//			if(!CTF1Manager.checkIfBlueFlagIsTaken()) {
//				getCaptureTeam(ChatColor.BLUE, "Red Flag", playerName, blueTeam, p);
//				CTF1Manager.resetFlag(regions.getRegion("redflag1"), Material.RED_BANNER, world);
//			} else {
//				p.sendMessage(prefix + ChatColor.BLUE + "You need to retrieve the blue flag before capturing the red flag!");
//			}
//		}
//
//		if(e.getRegion().equals(regions.getRegion("redflag1")) && CTF1Manager.checkIfPlayerHasBlueFlag(p)) {
//			if(!CTF1Manager.checkIfRedFlagIsTaken()) {
//				getCaptureTeam(ChatColor.RED, "Blue Flag", playerName, redTeam, p);
//				CTF1Manager.resetFlag(regions.getRegion("blueflag1"), Material.BLUE_BANNER, world);
//			} else {
//				p.sendMessage(prefix + ChatColor.RED + "You need to retrieve the red flag before capturing the blue flag!");
//			}
//		}
//	}
//
//	private void getFlagStolen(ChatColor color, String flag, String playerName, ConfigurationSection team, Player p) {
//		CTF1Manager.sendParticipantsMessage(prefix + color + uc.flag + flag + " has been stolen by " + playerName, Sound.BLOCK_NOTE_BLOCK_HARP, 1F, 0F);
//
//		team.set("flagholder", playerName);
//
//		p.addPotionEffect(PotionEffectType.GLOWING.createEffect(144000, 0));
//	}
//
//	private void sendWrongFlagMessage(Player p, BlockBreakEvent e) {
//		p.sendMessage(prefix + ChatColor.BLUE + "This is your team's flag!");
//		e.setCancelled(true);
//	}
//
//	private void getCaptureTeam(ChatColor color, String flag, String playerName, ConfigurationSection team, Player p) {
//		CTF1Manager.sendParticipantsMessage(prefix + color + uc.flag + flag + " has been captured by " + playerName, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);
//		p.removePotionEffect(PotionEffectType.GLOWING);
//		addCaptures(team);
//		team.set("flagholder", null);
//	}
//
//	@EventHandler
//	public void onKill(PlayerDeathEvent e) {
//		Player p = e.getEntity();
//		Entity entity = p.getKiller();
//
//		if(entity != null) {
//			if(participants.contains(p.getName())) {
//				Player killer = (Player) entity;
//				String killerTeam = CTF1Manager.getTeam(killer);
//
//				if(killerTeam.equals("red")) {
//					if(CTF1Manager.checkIfPlayerHasRedFlag(p)) {
//						resetFlagholder(blueTeam, ChatColor.RED, "Red flag");
//						CTF1Manager.resetFlag(regions.getRegion("redflag1"), Material.RED_BANNER, world);
//					}
//
//					killedPlayerMessage(killer, ChatColor.RED, "red", redTeam);
//
//				} else if(killerTeam.equals("blue")) {
//					if(CTF1Manager.checkIfPlayerHasBlueFlag(p)) {
//						resetFlagholder(redTeam, ChatColor.BLUE, "Blue flag");
//						CTF1Manager.resetFlag(regions.getRegion("blueflag1"), Material.BLUE_BANNER, world);
//					}
//
//					killedPlayerMessage(killer, ChatColor.BLUE, "blue", blueTeam);
//				}
//			}
//		}
//	}
//
//	@EventHandler
//	public void onPlayerRespawn(PlayerRespawnEvent e) {
//		Player p = e.getPlayer();
//
//		if(participants.contains(p.getName())) {
//			if(CTF1Manager.getBluePlayers().contains(p.getName())) {
//				e.setRespawnLocation(CTF1Manager.getRegionMiddleBlock(regions.getRegion("bluespawn1"), world));
//			} else if(CTF1Manager.getRedPlayers().contains(p.getName())) {
//				e.setRespawnLocation(CTF1Manager.getRegionMiddleBlock(regions.getRegion("redspawn1"), world));
//			}
//		}
//	}
//
//	@EventHandler
//	public void onMount(EntityMountEvent e) {
//		Entity entity = e.getEntity();
//		if(entity instanceof Player) {
//			Player p = (Player) entity;
//
//			if(regions.getApplicableRegions(VectorUtilities.getPlayerVector(p)).getRegions().contains(ctf1)) {
//				e.setCancelled(true);
//			}
//		}
//	}
//
//	@EventHandler
//	public void onElytraGlide(EntityToggleGlideEvent e) {
//		if (e.getEntity().getType().equals(EntityType.PLAYER)) {
//			Player p = (Player) e.getEntity();
//			if(participants.contains(p.getName())) {
//				dequipElytra((Player) e.getEntity());
//			}
//		}
//	}
//
//	@EventHandler
//	public void onItemDamage(PlayerItemDamageEvent e) {
//		if(participants.contains(e.getPlayer())) {
//	       e.setCancelled(true);
//		}
//	}
//
//	private void dequipElytra(Player p) {
//		PlayerInventory i = p.getInventory();
//        if (!( (i.getChestplate() != null) && i.getChestplate().getType().equals(Material.ELYTRA) ))
//            return;
//
//            ItemStack elytra = i.getChestplate();
//            i.setChestplate(null);
//
//            // inventory full?
//            if (i.firstEmpty() != -1) {
//                i.addItem(elytra);
//            } else {
//                Location l = i.getLocation();
//                l.getWorld().dropItemNaturally(l, elytra);
//                p.updateInventory();
//            }
//	}
//
//	private void killedPlayerMessage(Player killer, ChatColor color, String playerTeam, ConfigurationSection team) {
//		killer.sendMessage(prefix + color + "You have killed a " + playerTeam + " player!");
//		addKills(team);
//	}
//
//	private void resetFlagholder(ConfigurationSection team, ChatColor color, String flag) {
//		team.set("flagholder", null);
//		CTF1Manager.sendParticipantsMessage(prefix + color + uc.flag + flag + " has been reset!", null, 0F, 0F);
//	}
//
//	void addKills(ConfigurationSection team) {
//		int kills = team.getInt("kills");
//		kills += 1;
//		team.set("kills", kills);
//	}
//
//	void addCaptures(ConfigurationSection team) {
//		int captures = team.getInt("captures");
//
//		if(captures+1 != 5) {
//			captures += 1;
//			team.set("captures", captures);
//		} else {
//			team.set("captures", 5);
//			CTF1EndEvent ctf1endevent = new CTF1EndEvent();
//			ctf1endevent.run();
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
//}
