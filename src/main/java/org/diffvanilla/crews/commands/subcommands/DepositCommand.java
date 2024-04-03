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
import org.diffvanilla.crews.utilities.GeneralUtilities;
import org.diffvanilla.crews.utilities.InventoryUtility;

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
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew == null) {
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        String amount = args[0];
        int spongeCount;
        if (amount.equalsIgnoreCase("all")) {
            spongeCount = InventoryUtility.getSponges(p);
            if(spongeCount > 0) {
                InventoryUtility.removeSponges(p, spongeCount);
            } else {
                p.sendMessage(ConfigManager.NEED_MORE_SPONGE);
                return;
            }
        } else if (GeneralUtilities.isNumeric(amount)) {
            spongeCount = Integer.parseInt(amount);
            ItemStack sponges = new ItemStack(Material.SPONGE, spongeCount);
            if (p.getInventory().containsAtLeast(sponges, spongeCount)) {
                InventoryUtility.removeSponges(p, spongeCount);
            } else {
                p.sendMessage(ConfigManager.NEED_MORE_SPONGE);
                return;
            }
        } else {
            p.sendMessage(ConfigManager.INVALID_AMOUNT);
            return;
        }
        pCrew.addToVault(spongeCount, p, true);
        int finalSpongeCount = spongeCount;
        p.sendMessage(ConfigManager.SPONGE_DEPOSIT.replaceText(builder -> builder.matchLiteral("{amount}").replacement(String.valueOf(finalSpongeCount))));
        //update influence
    }
}
