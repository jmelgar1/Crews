//package org.diffvanilla.crews.tribalgames.managers;
//
//import org.bukkit.Bukkit;
//import org.bukkit.event.HandlerList;
//import org.bukkit.event.Listener;
//import org.diffvanilla.crews.Crews;
//
//public class CTFEvents implements Listener {
//
//	//Crews instance
//	protected Crews crewsClass = Crews.getInstance();
//
//	public void registerEvents() {
//		try {
//			Bukkit.getServer().getPluginManager().registerEvents(this, crewsClass);
//			System.out.println("FBCTF: Events Registered");
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
//
//	public void unregisterEvents() {
//		try {
//			HandlerList.unregisterAll(this);
//			System.out.println("FBCTF: Events Unregistered");
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
//}
