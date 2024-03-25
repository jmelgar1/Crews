//package org.thefruitbox.fbcrews.tribalgames.runnables.ctf1;
//
//import org.bukkit.scheduler.BukkitRunnable;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//import org.thefruitbox.fbcrews.utilities.ChatUtilities;
//
//public class CTFCheckForActiveMatch extends BukkitRunnable implements CTF1Manager {
//
//	private final ChatUtilities cu = new ChatUtilities();
//
//	@Override
//	public void run() {
//		boolean activeMatch = crewsClass.getCTF().getBoolean("event");
//
//		if(activeMatch) {
//			CTF1EndEvent ctf1endevent = new CTF1EndEvent();
//			ctf1endevent.run();
//
//			System.out.println(cu.tgPrefix + "Ending active match from console...");
//		}
//	}
//
//}
