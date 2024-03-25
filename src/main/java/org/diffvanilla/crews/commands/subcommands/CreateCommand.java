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
        if (args.length != 2) {
            p.sendMessage(ChatUtilities.CorrectUsage(getSyntax()));
            return;
        }
        if (pCrew != null) {
            p.sendMessage(ConfigManager.ALREADY_IN_CREW);
            return;
        }

        //this should be in a seperate class. For all the checks.
        String proposedCrewName = args[1];
        if (isValidCrewName(p, proposedCrewName, data)) {
            Crew newCrew = new Crew(args[0], p, plugin);
            data.addCrew(newCrew);
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize((ConfigManager.CREW_FOUNDED.replaceAll("\\{sector}", args[0]))));
        }
    }

    private boolean isValidCrewName(Player p, String crewName, PlayerData data) {
        if (!crewName.matches("[a-zA-Z]+")) {
            p.sendMessage(ConfigManager.CREW_NAME_ONLY_ALPHABETICAL);
            return false;
        }

        if (crewName.length() > 16 || crewName.length() < 4) {
            p.sendMessage(ConfigManager.CREW_NAME_BETWEEN_4_AND_16);
            return false;
        }

        if(data.getCrewRawName(crewName) != null) {
            p.sendMessage(ConfigManager.NAME_TAKEN);
            return false;
        }

        return true;
    }
}

