package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.InventoryManager;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;

public class DepositCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Deposit sponges into crew vault.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews deposit [amount]/all";
	}

    @Override
    public String getPermission() {
        return "crews.player.deposit";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if(!playerCrew.equals("none")) {
            if(args.length == 2) {
                String amount = args[1];
                if(amount.equalsIgnoreCase("all")) {

                    int spongeCount = 0;
                    for(ItemStack item : p.getInventory().getContents()) {
                        if(item != null) {
                            if(item.getType() == Material.SPONGE) {
                                spongeCount += item.getAmount();
                            }
                        }
                    }

                    InventoryManager.removeItems(p.getInventory(), Material.SPONGE, spongeCount);
                    crewManager.addToVault(playerCrew, spongeCount, p);
                    p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "You have deposited " + spongeCount + " sponges into the crew vault!");

                    crewManager.generateScorePercrew(playerCrew);

                } else if(isNumeric(amount)) {
                    int intAmount = Integer.parseInt(amount);
                    ItemStack sponges = new ItemStack(Material.SPONGE, intAmount);
                    if(p.getInventory().containsAtLeast(sponges, intAmount)) {
                        InventoryManager.removeItems(p.getInventory(), Material.SPONGE, intAmount);

                        crewManager.addToVault(playerCrew, intAmount, p);
                        p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "You have deposited " + intAmount + " sponges into the crew vault!");
                        crewManager.generateScorePercrew(playerCrew);
                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You do not have " + intAmount + " sponges in your inventory!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Invalid amount!");
                }
            } else {
                chatUtil.CorrectUsage(p, getSyntax());
            }
        } else {
            chatUtil.crewMembershipRequirement(p, getSyntax());
        }
    }

	boolean isNumeric(String input) {
		if(input == null) {
			return false;
		}
		
		try {
			double d = Double.parseDouble(input);
		} catch (NumberFormatException nfe){
			return false;
		}
		return true;
	}
}
