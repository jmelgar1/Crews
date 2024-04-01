package org.diffvanilla.crews.utilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.diffvanilla.crews.Crews;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CrewInfoUtilities {
    public static PacketContainer setMarkerPacket(Location location, Player p, Crews plugin) {
        World world = location.getWorld();
        if (world == null) return null;
        Entity tempEntity = world.spawnEntity(location, EntityType.ARMOR_STAND);
        tempEntity.remove();

        PacketContainer spawnPacket = plugin.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, tempEntity.getEntityId());
        spawnPacket.getUUIDs().write(0, tempEntity.getUniqueId());
        spawnPacket.getDoubles()
            .write(0, tempEntity.getLocation().getX())
            .write(1, tempEntity.getLocation().getY())
            .write(2, tempEntity.getLocation().getZ());
        spawnPacket.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        try {plugin.getProtocolManager().sendServerPacket(p, spawnPacket);} catch (Exception e) {e.printStackTrace();}

//        PacketContainer metadataPacket = plugin.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
//        WrappedDataWatcher watcher = new WrappedDataWatcher();
//        WrappedDataWatcher.WrappedDataWatcherObject invisibility = new WrappedDataWatcher.WrappedDataWatcherObject(0, Serializer.BYTE_SERIALIZER); // Index might vary based on version
//        WrappedDataWatcher.WrappedDataWatcherObject customNameVisibility = new WrappedDataWatcher.WrappedDataWatcherObject(3, Serializer.BOOLEAN_SERIALIZER); // Index for custom name visibility
//        WrappedDataWatcher.WrappedDataWatcherObject name = new WrappedDataWatcher.WrappedDataWatcherObject(2, Serializer.OPTIONAL_CHAT_COMPONENT_SERIALIZER); // Index for custom name
//
//        watcher.setObject(invisibility, (byte)0x20); // Bit mask for invisibility
//        watcher.setObject(customNameVisibility, true); // Make custom name visible
//        watcher.setObject(name, Optional.of(WrappedChatComponent.fromText(customName).getHandle())); // Set the custom name
//
//        metadataPacket.getIntegers().write(0, entityId);
//        metadataPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
//
//        // Send the metadata packet to configure the entity appearance
//        try {
//            plugin.getProtocolManager().sendServerPacket(player, metadataPacket);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        PacketContainer metadata = plugin.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadata.getIntegers().write(0, tempEntity.getEntityId());
        List<WrappedDataValue> dataValues = List.of(new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) (0x20 | 0x10 | 0x08)));
        List<WrappedDataValue> dataValues2 = List.of(new WrappedDataValue(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true), "custom"));

        metadata.getDataValueCollectionModifier().write(0, dataValues);

        try {plugin.getProtocolManager().sendServerPacket(p, metadata);} catch (Exception e) {e.printStackTrace();}
        return spawnPacket;
    }
    public static Location getTargetLocation(Player p){
        int distance = 3;
        Vector direction = p.getLocation().getDirection();
        return p.getEyeLocation().add(direction.multiply(distance));
    }
    public static void clearAllPositionsAndMarkers(Player p, Crews plugin) {
        UUID pUUID = p.getUniqueId();
        plugin.getShowInfoTask().getPlayerMarkerDistances().remove(pUUID);

        //remove any active markers
        if(plugin.getShowInfoTask().getPlayerMarkers().get(pUUID) != null){
            CrewInfoUtilities.removeMarketPacket(p, plugin.getShowInfoTask().getPlayerMarkers().get(pUUID), plugin);
        }
    }
    public static void removeMarketPacket(Player p, PacketContainer packet, Crews plugin) {
        PacketContainer destroyPacket = plugin.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        List<Integer> ids = Collections.singletonList(packet.getIntegers().read(0));
        destroyPacket.getIntLists().write(0, ids);
        try {
            plugin.getProtocolManager().sendServerPacket(p, destroyPacket);
            plugin.getShowInfoTask().getPlayerMarkers().remove(p.getUniqueId());
        } catch (Exception e) {e.printStackTrace();}
    }

    public static void teleportMarkerPacket(PacketContainer packet, Location newLocation, Player p, Crews plugin) {
        PacketContainer teleportPacket = plugin.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        teleportPacket.getIntegers().write(0, packet.getIntegers().read(0));
        teleportPacket.getDoubles()
            .write(0, (double) newLocation.getBlockX())
            .write(1, (double) newLocation.getBlockY())
            .write(2, (double) newLocation.getBlockZ());

        try {
            plugin.getProtocolManager().sendServerPacket(p, teleportPacket);
            plugin.getShowInfoTask().getPlayerMarkers().replace(p.getUniqueId(), teleportPacket);
        } catch (Exception e) {e.printStackTrace();}
    }
}
