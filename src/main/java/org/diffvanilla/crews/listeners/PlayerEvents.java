package org.diffvanilla.crews.listeners;

import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.subcommands.ShopCommand;
//import org.diffvanilla.crews.managers.DiscordManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.JsonUtilities;
import org.diffvanilla.crews.utilities.UnicodeCharacters;

import java.util.*;

public class PlayerEvents implements Listener {

    private static Inventory inv;
    public static Map<UUID, Inventory> inventories = new HashMap<UUID, Inventory>();
    public static final Map<UUID, Boolean> crewChatStatus = new HashMap<>();
    private final Crews plugin;

    public PlayerEvents(Crews plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        Crew targetCrew = plugin.getData().getCrew(p);

        if(targetCrew != null) {
            //String showName = targetCrew.getcrewshowName(targetCrew);
            String crewChatPrefix = ChatColor.GREEN + "(" + ChatColor.DARK_GREEN + targetCrew.getName() + ChatColor.GREEN + ") ";

            String sentMessage = event.getMessage();
            String playerName = p.getDisplayName();
            String newMessage = crewChatPrefix + ChatColor.WHITE + "<" + playerName + "> " + sentMessage;

            //if crew chat is enabled only send message to crew members
            if (crewChatStatus.get(uuid) != null && crewChatStatus.get(uuid)) {
                event.setCancelled(true);
                targetCrew.broadcast(Component.text(newMessage));
            }

            //logic for renaming crew
            if (ShopCommand.waitingForInputPlayer != null && p == ShopCommand.waitingForInputPlayer) {
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Handle the user input (in this case, it's the new crew name)
                        String newcrewName = event.getMessage();

                        p.performCommand("crews rename " + newcrewName);

                        // Reset the waiting state
                        ShopCommand.waitingForInputPlayer = null;
                    }
                }.runTask(Crews.getInstance());
            }

            //logic for getting discord name
            if (ShopCommand.getDiscordName != null && p == ShopCommand.getDiscordName) {
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // Handle the user input (in this case, it's the new crew name)
                        String discordName = event.getMessage();

                        //new DiscordManager(crewManager.getPlayercrew(p), p, discordName);

                        // Reset the waiting state
                        ShopCommand.getDiscordName = null;
                    }
                }.runTask(Crews.getInstance());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        Crew targetCrew = plugin.getData().getCrew(p);
        JsonObject crewsJson = plugin.getcrewsJson();
        JsonObject crewsObject = crewsJson.getAsJsonObject(targetCrew.getName());

        if(crewsObject != null){
            if(crewsObject.getAsJsonObject("mailMessages") == null){
                JsonObject mailMessages = new JsonObject();
                crewsObject.add("mailMessages", mailMessages);
                plugin.savecrewsFileJson();
            }

            JsonObject mailMessages = crewsObject.getAsJsonObject("mailMessages");
            if(!mailMessages.keySet().isEmpty()){
                for(String message : mailMessages.keySet()) {
                    JsonUtilities json = new JsonUtilities();
                    List<String> membersUUIDList = json.JsonArrayToStringList(mailMessages.get(message).getAsJsonArray());
                    if(membersUUIDList.contains(uuid.toString())){
                        UnicodeCharacters unicode = new UnicodeCharacters();
                        p.sendMessage(ChatColor.AQUA + "[" + UnicodeCharacters.mail + "]" + " You have unopened crew mail! Use /crews mail open");
                        break;
                    }
                }
            }
        }
    }
}
