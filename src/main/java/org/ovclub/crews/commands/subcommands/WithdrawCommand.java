package org.ovclub.crews.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.GeneralUtilities;

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
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (!pCrew.isHigherup(p)) {
            p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
            return;
        }
        String amount = args[0];
        int vaultAmount = pCrew.getVault();
        if (vaultAmount == 0) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        if (amount.equalsIgnoreCase("all")) {
            int maxStackSize = Material.SPONGE.getMaxStackSize();
            int maxAmount = p.getInventory().firstEmpty() == -1 ? 0 : (p.getInventory().firstEmpty() + 1) * maxStackSize;
            int amountToGive = Math.min(maxAmount, vaultAmount);
            ItemStack sponges = new ItemStack(Material.SPONGE, amountToGive);

            if (amountToGive > 0) {
                p.getInventory().addItem(sponges);
                pCrew.removeFromVault(amountToGive, p, true);
                p.sendMessage(ConfigManager.SPONGE_WITHDRAW.replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(amountToGive))));
            } else {
                p.sendMessage(ConfigManager.FULL_INVENTORY);
            }
        } else if (GeneralUtilities.isNumeric(amount)) {
            int intAmount = Integer.parseInt(amount);
            ItemStack sponges = new ItemStack(Material.SPONGE, intAmount);
            int maxStackSize = Material.SPONGE.getMaxStackSize();
            int maxAmount = p.getInventory().firstEmpty() == -1 ? 0 : (p.getInventory().firstEmpty() + 1) * maxStackSize;
            int amountToGive = Math.min(maxAmount, intAmount);
            if (intAmount > pCrew.getVault()) {
                p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
                return;
            }
            if (amountToGive > 0) {
                sponges.setAmount(amountToGive);
                pCrew.removeFromVault(intAmount, p, true);
                p.getInventory().addItem(sponges);
                p.sendMessage(ConfigManager.SPONGE_WITHDRAW.replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(amountToGive))));
            } else {
                p.sendMessage(ConfigManager.FULL_INVENTORY);
            }
        } else {
            p.sendMessage(ConfigManager.INVALID_AMOUNT);
        }
    }
}
