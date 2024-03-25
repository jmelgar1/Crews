package org.diffvanilla.crews.commands;

import java.util.*;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.subcommands.*;

import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;

public class CommandManager implements CommandExecutor {
//
    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();
	public final Map<String, SubCommand> underBossCommands = new LinkedHashMap<>();
	public final Map<String, SubCommand> bossCommands = new LinkedHashMap<>();
    public final Map<String, SubCommand> allCommands = new LinkedHashMap<>();

    private void sendHelp(Player p, Map<String, SubCommand> commandListType, String[] args) {
        int commandsPerPage = ConfigManager.COMMANDS_PER_PAGE;
        int page = 1;
        int lastPage = (int) Math.ceil((double) commandListType.size() / commandsPerPage);

        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Invalid page number.");
                return;
            }
        }

        int startIndex = (page - 1) * commandsPerPage;
        int endIndex = Math.min(startIndex + commandsPerPage, commandListType.size());

        if (startIndex >= commandListType.size()) {
            p.sendMessage(ChatColor.RED + "Page not found.");
            return;
        }

        p.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.GREEN + "Crews Guide" + ChatColor.GRAY + " (" + page + "/" + lastPage + ") ]-----");

        List<Map.Entry<String, SubCommand>> entries = new ArrayList<>(commandListType.entrySet());
        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<String, SubCommand> entry = entries.get(i);
            SubCommand command = entry.getValue();
            String commandName = entry.getKey();
            TextComponent commandText = new TextComponent(ChatColor.GRAY.toString() + (i + 1) + ". " + ChatColor.DARK_GREEN + "/crews " + commandName + ChatColor.WHITE + " - " + command.getDescription());
            commandText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/crews " + commandName));
            commandText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/crews " + commandName)));
            p.spigot().sendMessage(commandText);
        }

        // Pagination logic remains the same as your original function
        // Just ensure it uses the corrected variables and logic as per the corrections above
    }


  private final Crews plugin;
	public CommandManager(final Crews inst) {
    this.plugin = inst;
		subCommands.put("create", new CreateCommand());
		subCommands.put("list", new ListCommand());
		subCommands.put("lookup", new LookupCommand());
		subCommands.put("info", new InfoCommand());
		subCommands.put("chat", new ChatCommand());
		subCommands.put("deposit", new DepositCommand());
		subCommands.put("shop", new ShopCommand());
		subCommands.put("compound", new CompoundCommand());
		subCommands.put("who", new WhoCommand());
		subCommands.put("accept", new AcceptCommand());
		subCommands.put("decline", new DeclineCommand());
		subCommands.put("leave", new LeaveCommand());
		subCommands.put("mail", new MailCommand());
		subCommands.put("boss", new BossCommand());
		subCommands.put("underboss", new UnderBossCommand());
		underBossCommands.put("withdraw", new WithdrawCommand());
		underBossCommands.put("invite", new InviteCommand());
		underBossCommands.put("kick", new KickCommand());
		underBossCommands.put("setcompound", new SetCompoundCommand());
		underBossCommands.put("delcompound", new DelCompoundCommand());
		underBossCommands.put("rename", new RenameCommand());
		bossCommands.put("promote", new PromoteCommand());
		bossCommands.put("demote", new DemoteCommand());
		bossCommands.put("ownership", new OnwershipCommand());
		bossCommands.put("disband", new DisbandCommand());
		bossCommands.put("upgrade", new UpgradeCommand());
        allCommands.putAll(subCommands);
        allCommands.putAll(underBossCommands);
        allCommands.putAll(bossCommands);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player p)) return true;
    if (args.length == 0) {
      sendHelp(p, subCommands, args);
      return true;
    }

    String argument = args[0].toLowerCase();
    String[] pass = Arrays.copyOfRange(args, 1, args.length);
    if (!allCommands.containsKey(argument)) {
      sendHelp(p, subCommands, args);
      return true;
    }
    try {
      SubCommand subCmd = allCommands.get(argument);
      if (p.hasPermission(subCmd.getPermission())) {
        subCmd.perform(p, pass, plugin);
      } else {
        p.sendMessage(ConfigManager.NO_PERMISSION);
      }
    } catch (NotInCrew ignored) {}
    return true;
  }
//
//		if(sender instanceof Player p) {
//      if(args.length == 0) {
//				sendHelp(p, this.getSubCommands());
//			} else {
//				for(int i = 0; i < this.getSubCommands().size(); i++) {
//					if(args[0].equalsIgnoreCase(this.getSubCommands().get(i).getName())) {
//						this.getSubCommands().get(i).perform(p, args);
//
//						return true;
//					}
//				}
//
//				for(int i = 0; i < this.getBossCommands().size(); i++) {
//					if (args[0].equalsIgnoreCase(this.getBossCommands().get(i).getName())) {
//						this.getBossCommands().get(i).perform(p, args);
//
//						return true;
//					}
//				}
//
//				for(int i = 0; i < this.getUnderBossCommands().size(); i++) {
//					if (args[0].equalsIgnoreCase(this.getUnderBossCommands().get(i).getName())) {
//						this.getUnderBossCommands().get(i).perform(p, args);
//
//						return true;
//					}
//				}
//
//				p.sendMessage(ChatColor.WHITE + "Unknown command. Type " + "\"" + "/crews" + "\"" + " for help.");
//			}
//		}
//
//		return true;
	
	public Map<String, SubCommand> getSubCommands() {
		return subCommands;
	}

	public Map<String, SubCommand> getBossCommands() {
		return bossCommands;
	}

	public Map<String, SubCommand> getUnderBossCommands() {
		return underBossCommands;
	}
}
