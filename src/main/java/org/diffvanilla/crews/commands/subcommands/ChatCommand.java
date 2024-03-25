package org.diffvanilla.crews.commands.subcommands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.commands.SubCommand;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.ConfigManager;
import org.diffvanilla.crews.object.Crew;

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
            p.sendMessage(ConfigManager.UPGRADE_NOT_UNLOCKED);
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
