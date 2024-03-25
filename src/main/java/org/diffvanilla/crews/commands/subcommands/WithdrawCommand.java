package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import net.md_5.bungee.api.ChatColor;
import org.diffvanilla.crews.exceptions.NotInCrew;

public class WithdrawCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Withdraw sponges from the crew vault.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/crews withdraw [amount]/all";
	}

    @Override
    public String getPermission() {
        return "crews.player.withdraw";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        String playerCrew = crewManager.getPlayercrew(p);
        if(!playerCrew.equals("none")) {
            if(args.length == 2) {
                if (crewManager.CheckForElder(playerCrew, p) || crewManager.CheckForChief(playerCrew, p)) {
                    String amount = args[1];
                    if (amount.equalsIgnoreCase("all")) {
                        int vault = crewManager.getVault(playerCrew);

                        // Give the player as many sponges as can fit in their inventory. If it doesn't fit,
                        // do not take it out of the vault.
                        int maxStackSize = Material.SPONGE.getMaxStackSize();
                        int maxAmount = p.getInventory().firstEmpty() == -1 ? 0 : (p.getInventory().firstEmpty() + 1) * maxStackSize;
                        int amountToGive = Math.min(maxAmount, vault);
                        ItemStack sponges = new ItemStack(Material.SPONGE, amountToGive);

                        if (amountToGive > 0) {
                            p.getInventory().addItem(sponges);
                            crewManager.removeFromVault(playerCrew, amountToGive, p);
                            p.sendMessage(ChatColor.GREEN + "You received " + amountToGive + " sponges from the crew vault.");
                            crewManager.generateScorePercrew(playerCrew);
                        } else {
                            p.sendMessage(ChatColor.RED + "Your inventory is full. Could not retrieve any sponges.");
                        }


                    } else if (isNumeric(amount)) {
                        int intAmount = Integer.parseInt(amount);
                        ItemStack sponges = new ItemStack(Material.SPONGE, intAmount);

                        if (intAmount <= crewManager.getVault(playerCrew)) {
                            int maxStackSize = Material.SPONGE.getMaxStackSize();
                            int maxAmount = p.getInventory().firstEmpty() == -1 ? 0 : (p.getInventory().firstEmpty() + 1) * maxStackSize;
                            int amountToGive = Math.min(maxAmount, intAmount);

                            if (amountToGive > 0) {
                                sponges.setAmount(amountToGive);
                                crewManager.removeFromVault(playerCrew, amountToGive, p);
                                p.getInventory().addItem(sponges);
                                p.sendMessage(chatUtil.successIcon + ChatColor.GREEN + "You received " + amountToGive + " sponges.");

                                crewManager.generateScorePercrew(playerCrew);
                            } else {
                                p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Your inventory is full. Could not give any sponges.");
                            }
                        } else {
                            p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "You do not have enough sponges in the crew vault!");
                        }

                    } else {
                        p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Invalid amount!");
                    }
                } else {
                    p.sendMessage(chatUtil.errorIcon + ChatColor.RED + "Only chiefs and elders can withdraw sponges!");
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
