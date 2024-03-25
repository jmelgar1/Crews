//package org.thefruitbox.fbcrews.commands;
//
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.thefruitbox.fbcrews.Crews;
//import org.thefruitbox.fbcrews.tribalgames.runnables.ctf1.CTF1Countdown;
//
//public class beginCTF implements CommandExecutor {
//
//	private final Crews crewsClass = Crews.getInstance();
//
//	@Override
//	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//		if (!(sender instanceof Player)) {
//        	if(cmd.getName().equalsIgnoreCase("beginctf")) {
//        		CTF1Countdown ctf1countdown = new CTF1Countdown();
//        		ctf1countdown.runTaskTimer(crewsClass, 0L, 20);
//
//        		System.out.println("CTF: Starting CTF Countdown...");
//            }
//        }
//		return false;
//	}
//
//}
