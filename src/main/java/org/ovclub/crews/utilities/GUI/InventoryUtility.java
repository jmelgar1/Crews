package org.ovclub.crews.utilities.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;

import java.util.HashMap;
import java.util.List;

public class InventoryUtility {
    public static int getSponges(Player p) {
        int spongeCount = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == Material.SPONGE) {
                    spongeCount += item.getAmount();
                }
            }
        }
        return spongeCount;
    }

    public static void removeSponges(Player p, int amount) {
        Inventory inv = p.getInventory();
        if (amount <= 0) return;
        int size = inv.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inv.getItem(slot);
            if (is == null) continue;
            if (Material.SPONGE == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inv.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    public static int calculateTotalSpace(Player player, Material material) {
        int maxStackSize = material.getMaxStackSize();
        int totalSpace = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                totalSpace += maxStackSize;
            } else if (item.getType() == material && item.getAmount() < maxStackSize) {
                totalSpace += maxStackSize - item.getAmount();
            }
        }
        return totalSpace;
    }

    public static int distributeItems(Player player, Material material, int amount) {
        int maxStackSize = material.getMaxStackSize();
        int amountToGive = amount;
        while (amountToGive > 0) {
            int stackAmount = Math.min(maxStackSize, amountToGive);
            ItemStack itemStack = new ItemStack(material, stackAmount);
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(itemStack);

            if (!leftover.isEmpty()) {
                // Could not add any more items, inventory is full
                player.sendMessage(ConfigManager.FULL_INVENTORY);
                break;
            }
            amountToGive -= stackAmount;
        }
        return amount - amountToGive; // Return the amount successfully given
    }

    public static void updateVaultAndNotify(Player player, Crew pCrew, int givenAmount) {
        if (givenAmount > 0) {
            pCrew.removeFromVault(givenAmount, player, true);
            player.sendMessage(ConfigManager.SPONGE_WITHDRAW.replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(givenAmount))));
        }
    }
}
