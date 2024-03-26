package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (!pCrew.isHigherup(p)) {
            p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
            return;
        }
        String amount = args[1];
        int vaultAmount = pCrew.getVault();
        int maxStackSize = Material.SPONGE.getMaxStackSize();
        int maxAmount = p.getInventory().firstEmpty() == -1 ? 0 : (p.getInventory().firstEmpty() + 1) * maxStackSize;
        int amountToGive = Math.min(maxAmount, vaultAmount);
        ItemStack sponges = new ItemStack(Material.SPONGE, amountToGive);
        if (vaultAmount == 0) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        if (amount.equalsIgnoreCase("all")) {
            if (amountToGive > 0) {
                p.getInventory().addItem(sponges);
                pCrew.removeFromVault(amountToGive, p);
                p.sendMessage(ConfigManager.SPONGE_WITHDRAW);
                //crewManager.generateScorePercrew(playerCrew);
            } else {
                p.sendMessage(ConfigManager.FULL_INVENTORY);
            }
        }
        if (isNumeric(amount)) {
            int intAmount = Integer.parseInt(amount);
            if (intAmount > pCrew.getVault()) {
                p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
                return;
            }
            if (amountToGive > 0) {
                sponges.setAmount(amountToGive);
                pCrew.removeFromVault(intAmount, p);
                p.getInventory().addItem(sponges);
                p.sendMessage(ConfigManager.SPONGE_WITHDRAW);
            } else {
                p.sendMessage(ConfigManager.FULL_INVENTORY);
            }
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
