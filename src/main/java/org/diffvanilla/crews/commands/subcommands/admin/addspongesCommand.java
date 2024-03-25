package org.diffvanilla.crews.commands.subcommands.admin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;

import net.md_5.bungee.api.ChatColor;

public class addspongesCommand implements CommandExecutor {
	
	//Crews instance
	private Crews crewsClass = Crews.getInstance();
	
	String cmd1 = "addsponges";

    @SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            	if(cmd.getName().equalsIgnoreCase(cmd1)) {
            		if(p.hasPermission("crews.addrewards")) {
            			if(args.length == 2) {
            				
            				OfflinePlayer rewardee = Bukkit.getServer().getOfflinePlayer(args[0]);
            				String rewardeeUUID = rewardee.getUniqueId().toString();
            				int amount = Integer.valueOf(args[1]);
            				FileConfiguration rewardsFile = crewsClass.getRewards();
            				
            				if(rewardsFile.getConfigurationSection(rewardeeUUID) != null) {
            					ConfigurationSection rewardSection = rewardsFile.getConfigurationSection(rewardeeUUID);
            					
            					int oldAmount = rewardSection.getInt("unclaimed", amount);
            					oldAmount += amount;
            					rewardSection.set("unclaimed", oldAmount);
            					
            					crewsClass.saveRewardsFile();
            					p.sendMessage(ChatColor.GREEN + rewardee.getName() + " unclaimed sponges new amount is " + oldAmount + " sponges!");
            				} else {
            					p.sendMessage(ChatColor.RED + rewardee.getName() + " does not exist in rewards file! Use /setrewards");
            				}
            				
            			} else {
            				p.sendMessage(ChatColor.RED + "Correct usage: /addsponges [player] [amount]");
            			}
            		}
            }
        }
        return true;
    } 
}
