package org.diffvanilla.crews.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.diffvanilla.crews.managers.EventManager;
import org.diffvanilla.crews.utilities.ChatUtilities;

import java.util.Arrays;
import java.util.List;

public class KillEvent extends EventManager implements Listener {

	private final ChatUtilities cu = new ChatUtilities();
	
	@EventHandler
	public void killMob(EntityDeathEvent event) {
		
		LivingEntity entity = event.getEntity();
		
		//ensure mob was killed by a player
		if(!(entity.getKiller() == null)) {

			Player p = entity.getKiller();

			List<Object> bossMobs = Arrays.asList(EntityType.WITHER, EntityType.ENDER_DRAGON);

			int amountDropped = 0;
			if(bossMobs.contains(entity.getType())) {
				if (entity.getType() == EntityType.ENDER_DRAGON) {
					amountDropped = spongeManager.getRandomNumber(15, 18);
					p.sendMessage(cu.spongeColor + "You earned " + amountDropped + " sponges from killing an Ender Dragon!");
				} else if (entity.getType() == EntityType.WITHER) {
					amountDropped = spongeManager.getRandomNumber(10, 13);
					p.sendMessage(cu.spongeColor + "You earned " + amountDropped + " sponges from killing a Wither!");
				}

				entity.getWorld().dropItem(entity.getLocation(), new ItemStack(Material.SPONGE, amountDropped));
			}

			if (entity.getType() != EntityType.PLAYER ||
					entity.getType() != EntityType.ARMOR_STAND) {
				amountDropped = spongeManager.getRandomNumber(0, 1201);
				if (amountDropped == 1 || amountDropped == 2 || amountDropped == 3) {
					entity.getWorld().dropItem(entity.getLocation(), new ItemStack(Material.SPONGE, amountDropped));
					p.sendMessage(cu.spongeColor + "You earned " + amountDropped + " sponges from killing " + entity.getType().getName() + "!");
				}
			}
		}
	}
}
