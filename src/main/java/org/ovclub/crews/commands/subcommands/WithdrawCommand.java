package org.ovclub.crews.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.GUI.InventoryUtility;
import org.ovclub.crews.utilities.GeneralUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class WithdrawCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Withdraw sponges from the crew vault.";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/c withdraw [amount]/all";
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
            p.sendMessage(UnicodeCharacters.CorrectUsage(getSyntax()));
            return;
        }
//        if (!pCrew.isHigherup(p)) {
//            p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
//            return;
//        }
        String amount = args[0];
        int vaultAmount = pCrew.getVault();
        int vaultDepositAmount = pCrew.getVaultDeposit(p);
        if (vaultAmount == 0) {
            p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
            return;
        }
        if (amount.equalsIgnoreCase("all")) {
            int totalSpace = InventoryUtility.calculateTotalSpace(p, Material.SPONGE);
            int amountToGive = Math.min(totalSpace, vaultDepositAmount);

            if (vaultDepositAmount == 0) {
                p.sendMessage(ConfigManager.NOT_ENOUGH_DEPOSIT);
                return;
            }

            int amountGiven = InventoryUtility.distributeItems(p, Material.SPONGE, amountToGive);
            InventoryUtility.updateVaultAndNotify(p, pCrew, amountGiven);
        } else if (GeneralUtilities.isNumeric(amount)) {
            int intAmount = Integer.parseInt(amount);

            if (intAmount > pCrew.getVault()) {
                p.sendMessage(ConfigManager.NOT_ENOUGH_IN_VAULT);
                return;
            }

            if (intAmount > vaultDepositAmount) {
                p.sendMessage(ConfigManager.NOT_ENOUGH_DEPOSIT);
                return;
            }

            int totalSpace = InventoryUtility.calculateTotalSpace(p, Material.SPONGE);
            int amountToGive = Math.min(totalSpace, intAmount);
            int amountGiven = InventoryUtility.distributeItems(p, Material.SPONGE, amountToGive);

            InventoryUtility.updateVaultAndNotify(p, pCrew, amountGiven);
        } else {
            p.sendMessage(ConfigManager.INVALID_AMOUNT);
        }
    }
}
