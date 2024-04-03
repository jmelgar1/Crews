package org.diffvanilla.crews.commands.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.utilities.ChatUtilities;

public class InviteCommand implements SubCommand {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Invite a player to your crew.";
	}

    @Override
    public String getSyntax() {
        return "/crew invite [player]";
    }

    @Override
    public String getPermission() {
        return "crew.player.invite";
    }

    @Override
	public void perform(Player p, String[] args, Crews plugin) {
        Crew crew = plugin.getData().getCrew(p);
        if (args[0] != null) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args.length != 1) {
                p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
                return;
            }
            if (Bukkit.getServer().getPlayer(target.getName()) == null) {
                p.sendMessage(ConfigManager.PLAYER_NOT_ONLINE.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
                return;
            }
            if (crew == null) {
                p.sendMessage(ConfigManager.NOT_IN_CREW);
                return;
            }
            if (!crew.isBoss(p) || !crew.isEnforcer(p)) {
                p.sendMessage(ConfigManager.MUST_BE_HIGHERUP);
                return;
            }
            //add one for boss
            if (crew.getMemberLimit() <= crew.getMembers().size() + crew.getEnforcers().size() + 1) {
                p.sendMessage(ConfigManager.REACHED_MAX_MEMBERS.replaceText(builder -> builder.matchLiteral("{limit}").replacement(String.valueOf(crew.getMemberLimit()))));
                return;
            }
            if (target.getName().equals(p.getName())) {
                p.sendMessage(ConfigManager.CAN_NOT_INVITE_SELF);
                return;
            }
            if (plugin.getData().hasInvitation(p)) {
                p.sendMessage(ConfigManager.ALREADY_INVITED.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
                return;
            }
            if (plugin.getData().getCrew(target) == crew) {
                p.sendMessage(ConfigManager.ALREADY_IN_CREW);
                return;
            }
            if (plugin.getData().getCrew(target) != null) {
                p.sendMessage(ConfigManager.PLAYER_NOT_FREE_AGENT.replaceText(builder -> builder.matchLiteral("{player}").replacement(p.getName())));
                return;
            }

            plugin.getData().addInvitation(target, crew);
            p.sendMessage(ConfigManager.SUCCESS);

            TextComponent inviteHeader = Component.text("┌──────[ ✉ CREW INVITE ✉ ]───────").color(TextColor.color(0x42A5F5));

            TextComponent component1 = Component.text("│ ").color(TextColor.color(0x42A5F5)).append(Component.text("You have been invited to join ").color(TextColor.color(0x26A69A))
                .append(Component.text(crew.getName()).color(TextColor.color(0x0277BD))));

            TextComponent component2 = Component.text("│ ").color(TextColor.color(0x42A5F5)).append(Component.text("Type ").color(TextColor.color(0x689F38))
                .append(Component.text("/c accept " + crew.getName()).color(TextColor.color(0x4CAF50))
                    .append(Component.text(" to accept the invite!").color(TextColor.color(0x689F38)))));

            TextComponent component3 = Component.text("│ ").color(TextColor.color(0x42A5F5)).append(Component.text("Type ").color(TextColor.color(0xE65100))
                .append(Component.text("/c decline " + crew.getName()).color(TextColor.color(0xD84315))
                    .append(Component.text(" to decline the invite!").color(TextColor.color(0xE65100)))));

            TextComponent inviteFooter = Component.text("└──────────────────────────").color(TextColor.color(0x42A5F5));

            target.sendMessage(inviteHeader);
            target.sendMessage(component1);
            target.sendMessage(component2);
            target.sendMessage(component3);
            target.sendMessage(inviteFooter);

            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getData().expireInvitation(target);
                }
            }.runTaskLaterAsynchronously(plugin, 3600);
        } else {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
        }
    }
}
