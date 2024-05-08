package org.ovclub.crews.utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

//    public static double getDropMultiplier(ItemStack item) {
//        if (item != null && item.hasItemMeta()) {
//            ItemMeta meta = item.getItemMeta();
//            if (meta.hasLore()) {
//                List<String> lore = meta.getLore();
//                for (String line : lore) {
//                    if (line.contains("Drop Multiplier:")) {
//                        String[] parts = line.split(" ");
//                        for (String part : parts) {
//                            if (part.endsWith("x")) {
//                                String number = part.substring(0, part.length() - 1); // Remove the 'x' character
//                                try {
//                                    return Double.parseDouble(number);
//                                } catch (NumberFormatException e) {
//                                    // Handle the case where the number is not valid
//                                    System.out.println("Failed to parse multiplier: " + part);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return 1.0;
//    }
}
