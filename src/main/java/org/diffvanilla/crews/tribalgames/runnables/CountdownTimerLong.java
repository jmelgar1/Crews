//package org.thefruitbox.fbcrews.tribalgames.runnables;
//
//import java.text.DecimalFormat;
//
//import org.bukkit.Bukkit;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.util.Consumer;
//import org.thefruitbox.fbcrews.tribalgames.managers.CTF1Manager;
//
//public class CountdownTimerLong implements Runnable {
//    // Crews class for bukkit scheduling
//    private JavaPlugin plugin;
//
//    // Our scheduled task's assigned id, needed for canceling
//    private Integer assignedTaskId;
//
//    // Seconds
//    private int seconds;
//    private int secondsLeft;
//    private int minute;
//    private int showSeconds;
//
//    String ddSecond, ddMinute;
//    DecimalFormat dFormat = new DecimalFormat("00");
//
//    // Actions to perform while counting down, before and after
//    private Consumer<CountdownTimerLong> everySecond;
//
//    // Construct a timer, you could create multiple so for example if
//    // you do not want these "actions"
//    public CountdownTimerLong(JavaPlugin plugin, int minute, int seconds,
//                          Consumer<CountdownTimerLong> everySecond) {
//        // Initializing fields
//        this.plugin = plugin;
//
//        this.minute = minute;
//        this.seconds = ((minute*60) + seconds);
//        this.secondsLeft = ((minute*60) + seconds);
//
//        this.everySecond = everySecond;
//
//        this.showSeconds = seconds;
//        ddSecond = dFormat.format(showSeconds);
//        ddMinute = dFormat.format(minute);
//
//    }
//
//    /**
//     * Runs the timer once, decrements seconds etc...
//     * Really wish we could make it protected/private so you couldn't access it
//     */
//    @Override
//    public void run() {
//
//    	if(CTF1Manager.ctf.getBoolean("event") != false) {
//
//	        // Is the timer up?
//	        if (secondsLeft < 1 && minute == 0) {
//
//	           // Cancel timer
//	            if (assignedTaskId != null) Bukkit.getScheduler().cancelTask(assignedTaskId);
//	            return;
//	        }
//
//	        if(secondsLeft % 60 == 0) {
//	        	minute--;
//	        	showSeconds = 59;
//	        }
//
//	        secondsLeft--;
//	        showSeconds--;
//
//	        // Do what's supposed to happen every second
//	        everySecond.accept(this);
//
//	        showSeconds = secondsLeft % 60;
//
//	        ddSecond = dFormat.format(showSeconds);
//	        ddMinute = dFormat.format(minute);
//
//	        UpdateScoreboard updateScoreboard = new UpdateScoreboard();
//	        updateScoreboard.run();
//    	}
//    }
//
//    /**
//     * Gets the total seconds this timer was set to run for
//     *
//     * @return Total seconds timer should run
//     */
//    public int getTotalSeconds() {
//        return seconds;
//    }
//
//    /**
//     * Gets the seconds left this timer should run
//     *
//     * @return Seconds left timer should run
//     */
//    public String getSecondsLeft() {
//        return ddSecond;
//    }
//
//    public String getMinute() {
//        return ddMinute;
//    }
//
//    /**
//     * Schedules this instance to "run" every second
//     */
//    public void scheduleTimer() {
//        // Initialize our assigned task's id, for later use so we can cancel
//        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
//    }
//}
