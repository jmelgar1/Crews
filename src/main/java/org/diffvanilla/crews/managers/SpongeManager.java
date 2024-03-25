package org.diffvanilla.crews.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;

public class SpongeManager {
	
	//Crews instance
	private final Crews crewsClass = Crews.getInstance();
	
	public int checkForUnclaimed(Player p) {
		ConfigurationSection rewards = crewsClass.getRewards();
		String playerUUID = p.getUniqueId().toString();
		if(rewards.getConfigurationSection(playerUUID) != null) {
			ConfigurationSection rewardSection = rewards.getConfigurationSection(playerUUID);
			if(rewardSection.getInt("unclaimed") != 0) {
				return rewardSection.getInt("unclaimed");
			}
		}
		return 0;
	}	
	
	public int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max-min)) + min);
	}
}
