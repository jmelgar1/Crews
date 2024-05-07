package org.ovclub.crews.commands.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;

import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.object.PlayerData;
import org.ovclub.crews.utilities.ChatUtilities;
import org.ovclub.crews.utilities.SoundUtilities;

public class CreateCommand implements SubCommand {

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Create a crew.";
    }

    @Override
    public String getSyntax() {
        return "/crews create [name]";
    }

    @Override
    public String getPermission() {
        return "crews.player.create";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        PlayerData data = plugin.getData();
        Crew pCrew = data.getCrew(p);
        if (pCrew != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_CREW);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        String proposedCrewName = args[0];
        if (data.isValidCrewName(p, proposedCrewName)) {
            Crew newCrew = new Crew(args[0], p, plugin);
            data.addCrew(newCrew);
            Bukkit.broadcast(ConfigManager.CREW_FOUNDED
                .replaceText(builder -> builder.matchLiteral("{crew}").replacement(Component.text(newCrew.getName()).decorate(TextDecoration.BOLD)))
                .replaceText(builder -> builder.matchLiteral("{player}").replacement(Component.text(p.getName()).decorate(TextDecoration.BOLD))));
            SoundUtilities.playSoundToAllPlayers(SoundUtilities.crewCreateSound, 0.45F, 0.8F);
        }
    }
}
