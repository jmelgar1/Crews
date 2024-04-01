package org.diffvanilla.crews.commands.subcommands;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;

import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;
import org.diffvanilla.crews.object.PlayerData;
import org.diffvanilla.crews.utilities.ChatUtilities;

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
        if (args.length != 1) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_CREW);
            return;
        }

        String proposedCrewName = args[0];
        p.sendMessage(proposedCrewName);
        if (data.isValidCrewName(p, proposedCrewName)) {
            Crew newCrew = new Crew(args[0], p, plugin);
            data.addCrew(newCrew);
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize((ConfigManager.CREW_FOUNDED.replaceAll("\\{sector}", args[0]))));
        }
    }
}

