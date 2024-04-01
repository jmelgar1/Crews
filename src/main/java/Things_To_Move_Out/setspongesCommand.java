//package Things_To_Move_Out;
//
//import org.bukkit.Bukkit;
//import org.bukkit.OfflinePlayer;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.diffvanilla.crews.Crews;
//
//import net.md_5.bungee.api.ChatColor;
//
//public class setspongesCommand implements CommandExecutor {
//
//	//Crews instance
//	private Crews crewsClass = Crews.getInstance();
//
//	String cmd1 = "setsponges";
//
//    @SuppressWarnings("deprecation")
//	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//        if (sender instanceof Player) {
//            Player p = (Player) sender;
//            	if(cmd.getName().equalsIgnoreCase(cmd1)) {
//            		if(p.hasPermission("crews.setsponges")) {
//            			if(args.length == 2) {
//
//            				OfflinePlayer rewardee = Bukkit.getServer().getOfflinePlayer(args[0]);
//            				String rewardeeUUID = rewardee.getUniqueId().toString();
//            				int amount = Integer.valueOf(args[1]);
//            				FileConfiguration rewardsFile = crewsClass.getRewards();
//
//            				if(rewardsFile.getConfigurationSection(rewardeeUUID) != null) {
//            					ConfigurationSection rewardSection = rewardsFile.getConfigurationSection(rewardeeUUID);
//            					rewardSection.set("unclaimed", amount);
//            					crewsClass.saveRewardsFile();
//            					p.sendMessage(ChatColor.GREEN + rewardee.getName() + " unclaimed sponges set to " + amount + " sponges!");
//            				} else {
//            					rewardsFile.createSection(rewardeeUUID).set("unclaimed", amount);
//            					crewsClass.saveRewardsFile();
//            					p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "NEW FILE: " + rewardee.getName() + " unclaimed sponges set to " + amount + " sponges!");
//            				}
//            			} else {
//            				p.sendMessage(ChatColor.RED + "Correct usage: /setsponges [player] [amount]");
//            			}
//            		}
//            }
//        }
//        return true;
//    }
//}
