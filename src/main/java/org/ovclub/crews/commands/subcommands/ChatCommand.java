package org.ovclub.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.commands.SubCommand;
import org.ovclub.crews.exceptions.NotInCrew;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;

public class ChatCommand implements SubCommand {
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Enable/disable crew chat";
    }

    @Override
    public String getSyntax() {
        // TODO Auto-generated method stub
        return "/crews chat";
    }

    @Override
    public String getPermission() {
        return "crews.player.chat";
    }

    @Override
    public void perform(Player p, String[] args, Crews plugin) throws NotInCrew {
        Crew playerCrew = plugin.getData().getCrew(p);
        if(playerCrew == null){
            p.sendMessage(ConfigManager.NOT_IN_CREW);
            return;
        }
        if(!playerCrew.getUnlockedUpgrades().contains("chat")) {
            p.sendMessage(ConfigManager.UPGRADE_NOT_UNLOCKED.replaceText(builder -> builder.matchLiteral("{upgrade}").replacement("chat")));
            return;
        }
        if(plugin.getData().isInCrewChat(p.getUniqueId())) {
            p.sendMessage(ConfigManager.CREW_CHAT_DISABLED);
            plugin.getData().disableCrewChat(p.getUniqueId());
            return;
        }
        if(!plugin.getData().isInCrewChat(p.getUniqueId())) {
            p.sendMessage(ConfigManager.CREW_CHAT_ENABLED);
            plugin.getData().enableCrewChat(p.getUniqueId());
        }
    }
}
