package org.ovclub.crews.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SoundUtilities {
    public static Sound pingSound = Sound.BLOCK_NOTE_BLOCK_PLING;
    public static Sound teleportSound = Sound.ENTITY_PLAYER_TELEPORT;
    public static Sound crewUpgradeSound = Sound.BLOCK_ANVIL_USE;
    public static Sound crewDisbandSound = Sound.ENTITY_TURTLE_EGG_BREAK;
    public static Sound crewCreateSound = Sound.ENTITY_FIREWORK_ROCKET_TWINKLE;
    public static Sound skirmishVictorySound = Sound.ENTITY_VILLAGER_CELEBRATE;
    public static Sound skirmishDefeatSound = Sound.ENTITY_VILLAGER_NO;
    public static Sound skirmishDrawSound = Sound.BLOCK_NOTE_BLOCK_BASS;
    public static Sound crewShopPurchaseSound = Sound.BLOCK_GLASS_BREAK;
    /*Skirmish*/
    public static Sound skirmishBeginSound = Sound.ENTITY_ILLUSIONER_CAST_SPELL;
    public static Sound skirmishDeathSound = Sound.BLOCK_BEACON_DEACTIVATE;

    public static void playPingSound(Player p) {
        p.playSound(p.getLocation(), pingSound, 0.5F, 1.0F);
    }

    public static void playTeleportSound(Player p) {
        p.playSound(p.getLocation(), teleportSound, 0.5F, 1.0F);
    }

    public static void playHornSound(Player p) {
        p.playSound(p.getLocation(), skirmishBeginSound, 0.5F, 1.0F);
    }

    public static void playDeathSound(Player p) {
        p.playSound(p.getLocation(), skirmishDeathSound, 0.5F, 1.0F);
    }

    public static void playPurchaseSound(Player p) {
        p.playSound(p.getLocation(), crewShopPurchaseSound, 0.5F, 1.3F);
    }

    public static void playSoundToAllPlayers(Sound sound, float volume, float pitch) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void playSoundToSpecificPlayers(ArrayList<String> uuids, Sound sound, float volume, float pitch) {
        for (String uuidString : uuids) {
            UUID uuid = UUID.fromString(uuidString);
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.playSound(player.getLocation(), sound, volume, pitch);
            }
        }
    }
}
