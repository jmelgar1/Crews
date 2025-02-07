package org.ovclub.crews.commands;

import java.util.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.subcommands.*;

import org.ovclub.crews.commands.subcommands.admin.ResetCommand;
import org.ovclub.crews.commands.subcommands.skirmish.*;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.utilities.UnicodeCharacters;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();
    private final Map<String, SubCommand> enforcerCommands = new LinkedHashMap<>();
    private final Map<String, SubCommand> bossCommands = new LinkedHashMap<>();
    private final Map<String, SubCommand> allCommands = new LinkedHashMap<>();
    private final Map<String, SubCommand> skirmishCommands = new LinkedHashMap<>();
    private final Map<String, SubCommand> adminCommands = new LinkedHashMap<>();

    private void sendHelp(Player p, String icon, String helpTitle, TextColor titleColor, Map<String, SubCommand> commandListType, String[] args) {
        int commandsPerPage = ConfigManager.COMMANDS_PER_PAGE;
        int page = 1;
        int lastPage = (int) Math.ceil((double) commandListType.size() / commandsPerPage);

        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(Component.text("Invalid page number.", NamedTextColor.RED));
                return;
            }
        }

        int startIndex = (page - 1) * commandsPerPage;
        int endIndex = Math.min(startIndex + commandsPerPage, commandListType.size());

        if (startIndex >= commandListType.size()) {
            p.sendMessage(Component.text("Page not found.", NamedTextColor.RED));
            return;
        }

        sendCrewsGuideMessage(p, icon, helpTitle, titleColor, page, lastPage);

        List<Map.Entry<String, SubCommand>> entries = new ArrayList<>(commandListType.entrySet());
        for (int i = startIndex; i < endIndex; i++) {
            Map.Entry<String, SubCommand> entry = entries.get(i);
            SubCommand command = entry.getValue();
            String commandName = entry.getKey();

            Component commandText = Component.text((i + 1) + ". ", NamedTextColor.DARK_GRAY)
                .append(Component.text("/crews " + commandName + " - ", NamedTextColor.GRAY))
                .append(Component.text(command.getDescription(), NamedTextColor.WHITE))
                .clickEvent(ClickEvent.suggestCommand("/crews " + commandName))
                .hoverEvent(HoverEvent.showText(Component.text("/crews " + commandName, NamedTextColor.GRAY)));

            p.sendMessage(commandText);
        }

        // Pagination logic
        Component pagination = Component.empty();
        if (page > 1) {
            Component previousPage = Component.text("< Previous", TextColor.fromHexString("#DCE775"))
                .clickEvent(ClickEvent.runCommand("/crews help " + (page - 1)))
                .hoverEvent(HoverEvent.showText(Component.text("Go to page " + (page - 1), NamedTextColor.YELLOW)))
                .decorate(TextDecoration.BOLD);
            pagination = pagination.append(previousPage);
        }
        if (page > 1 && page < lastPage) {
            pagination = pagination.append(Component.text(" | ", NamedTextColor.DARK_GRAY));
        }
        if (page < lastPage) {
            Component nextPage = Component.text("Next >", TextColor.fromHexString("#AED581"))
                .clickEvent(ClickEvent.runCommand("/crews help " + (page + 1)))
                .hoverEvent(HoverEvent.showText(Component.text("Go to page " + (page + 1), NamedTextColor.YELLOW)))
                .decorate(TextDecoration.BOLD);
            pagination = pagination.append(nextPage);
        }
        if (!pagination.equals(Component.empty())) {
            p.sendMessage(pagination);
        }
    }

  private final Crews plugin;
	public CommandManager(final Crews inst) {
    this.plugin = inst;
		subCommands.put("create", new CreateCommand());
		subCommands.put("list", new ListCommand());
		subCommands.put("info", new InfoCommand());
        subCommands.put("who", new WhoCommand());
        subCommands.put("skirmish", new SkirmishCommand());
        subCommands.put("deposit", new DepositCommand());
        subCommands.put("withdraw", new WithdrawCommand());
		//subCommands.put("shop", new ShopCommand());
		subCommands.put("compound", new CompoundCommand());
		subCommands.put("accept", new AcceptCommand());
		subCommands.put("decline", new DeclineCommand());
		subCommands.put("leave", new LeaveCommand());
        subCommands.put("chat", new ChatCommand());
        subCommands.put("description", new DescriptionCommand());
		//subCommands.put("mail", new MailCommand());
		subCommands.put("boss", new BossCommand());
		subCommands.put("enforcer", new EnforcerCommand());
        subCommands.put("multipliers", new MultipliersCommand());
        subCommands.put("vote", new VoteCommand());
		enforcerCommands.put("invite", new InviteCommand());
		enforcerCommands.put("kick", new KickCommand());
		enforcerCommands.put("setcompound", new SetCompoundCommand());
		enforcerCommands.put("delcompound", new DelCompoundCommand());
		enforcerCommands.put("rename", new RenameCommand());
		bossCommands.put("promote", new PromoteCommand());
		bossCommands.put("demote", new DemoteCommand());
		bossCommands.put("ownership", new OnwershipCommand());
		bossCommands.put("disband", new DisbandCommand());
		bossCommands.put("upgrade", new UpgradeCommand());
        skirmishCommands.put("skirmish join", new SkirmishJoinCommand());
        skirmishCommands.put("skirmish leave", new SkirmishLeaveCommand());
        skirmishCommands.put("skirmish stats", new SkirmishStatsCommand());
        skirmishCommands.put("skirmish accept", new SkirmishAcceptCommand());
        allCommands.putAll(subCommands);
        allCommands.putAll(enforcerCommands);
        allCommands.putAll(bossCommands);

        adminCommands.put("reset", new ResetCommand());
	}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (args.length == 0) {
            sendHelp(p, UnicodeCharacters.crews,"Crews Guide", UnicodeCharacters.plugin_color, subCommands, args);
            return true;
        }
        String argument = args[0].toLowerCase();
        String[] pass = Arrays.copyOfRange(args, 1, args.length);
        if(adminCommands.containsKey(argument)) {
            if (p.hasPermission("crews.admin.*")) {
                SubCommand subCmd = adminCommands.get(argument);
                try {
                    subCmd.perform(p, pass, plugin);
                } catch (NotInCrew e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!allCommands.containsKey(argument)) {
            sendHelp(p, UnicodeCharacters.crews,"Crews Guide", UnicodeCharacters.plugin_color, subCommands, args);
            return true;
        } else if(argument.equalsIgnoreCase("skirmish")) {
            if(pass.length == 0) {
                sendHelp(p, UnicodeCharacters.skirmish_emoji, "Skirmish Guide", UnicodeCharacters.skirmish_color, skirmishCommands, args);
            } else {
                String subCommandKey = "skirmish " + String.join(" ", pass);
                SubCommand subCmd = skirmishCommands.get(subCommandKey);
                if(subCmd != null) {
                    try {
                        subCmd.perform(p, pass, plugin);
                    } catch (NotInCrew e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    p.sendMessage(Component.text("Command not found.", NamedTextColor.RED));
                }
            }
            return true;
        }
        try {
            SubCommand subCmd = allCommands.get(argument);
            if (p.hasPermission("crews.player.*")) {
                subCmd.perform(p, pass, plugin);
            } else {
                p.sendMessage(ConfigManager.NO_PERMISSION);
            }
        } catch (NotInCrew ignored) {
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            allCommands.keySet().forEach(cmd -> {
                if (sender.hasPermission("crews.player." + cmd)) {
                    completions.add(cmd);
                }
            });
            return completions;
        } else if (args.length > 1) {
            String subCmdKey = args[0].toLowerCase();
            SubCommand subCmd = allCommands.get(subCmdKey);
            if (subCmd instanceof TabCompleter) {
                return ((TabCompleter) subCmd).onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return null;
    }

    public void sendCrewsGuideMessage(Player p, String icon, String guideTitle, TextColor titleColor, int page, int lastPage) {
        TextComponent message = Component.text()
            .append(Component.text("-----[ ", TextColor.fromHexString(UnicodeCharacters.textColor)))
            .append(Component.text(icon, TextColor.fromHexString(UnicodeCharacters.logoColor)))
            .append(Component.text(guideTitle + " ", titleColor))
            .append(Component.text(icon, TextColor.fromHexString(UnicodeCharacters.logoColor)))
            .append(Component.text(" (", TextColor.fromHexString("#7B7B7B")))
            .append(Component.text(page + "/" + lastPage, TextColor.fromHexString("#FFFFFF")))
            .append(Component.text(") ]-----", TextColor.fromHexString(UnicodeCharacters.textColor)))
            .build();
        p.sendMessage(message);
    }
}
